package com.cigital.integration.util;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.cigital.integration.vo.UserVo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class UserUtil {
	public static Boolean isGuestUser(HttpSession session){
		if(session!=null){
			UserVo userDetails = (UserVo)session.getAttribute("userDetails");
			return userDetails==null ? true : false;
		}
		else{
			return null;
		}
	}
	
	public static Date getCurrentDateTime(){
		java.util.Date date = new java.util.Date();
		return new Date(date.getTime());
	}
	
	public static List<GlobalSearchVo> getSearchResults(String completeJsonStr, String searchResult){
		
		try {
		    JsonElement jelement = new JsonParser().parse(completeJsonStr);
		    JsonArray jarray = jelement.getAsJsonArray() ;
		    
		    List<GlobalSearchVo> searchResultList = new ArrayList<>(); 
		    for (int i=0; i<jarray.size() ;i++ )
		    {
			    String result = jarray.get(i).getAsJsonObject().get("goodsName").toString();
			    String goodsId = jarray.get(i).getAsJsonObject().get("goodsId").toString();
			    result = result.replace("\"", "");
			    
			    String resultf = result.toLowerCase();
			    searchResult = searchResult.toLowerCase();
			    
			    if(resultf.startsWith(searchResult)){
			    	GlobalSearchVo globalSearchVo = new GlobalSearchVo();
			    	globalSearchVo.setKey(goodsId);
			    	globalSearchVo.setValue(result);
			    	searchResultList.add(globalSearchVo);
			    }
		    }
	        
		    System.out.println(searchResultList);
		    return searchResultList;
	    }
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
}
