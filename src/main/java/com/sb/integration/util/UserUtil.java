package com.sb.integration.util;

import java.io.FileOutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.sb.integration.service.GoodsService;
import com.sb.integration.service.impl.GoodsServicesImpl;
import com.sb.integration.vo.CategoryVo;
import com.sb.integration.vo.GoodsVo;
import com.sb.integration.vo.QuantityVo;
import com.sb.integration.vo.UserVo;

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
			    String unitPrice = jarray.get(i).getAsJsonObject().get("price").toString();
			    String qtyPerUom = jarray.get(i).getAsJsonObject().get("quantity_per_annum").toString();
			    String uom = jarray.get(i).getAsJsonObject().get("uom").toString();
			    result = result.replace("\"", "");
			    qtyPerUom = qtyPerUom.replace("\"", "");
			    uom = uom.replace("\"", "");
			    
			    String resultf = result.toLowerCase();
			    searchResult = searchResult.toLowerCase();
			    
			    if(resultf.contains(searchResult)){
			    	GlobalSearchVo globalSearchVo = new GlobalSearchVo();
			    	globalSearchVo.setKey(goodsId);
			    	globalSearchVo.setValue(result);
			    	globalSearchVo.setUnitPrice(unitPrice);
			    	globalSearchVo.setQtyPerUom(qtyPerUom);
			    	globalSearchVo.setUom(uom);
			    	
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
	
	public static BigDecimal calculateOfferPrice(BigDecimal totalAmount, Integer offerPercentage){
		return totalAmount.multiply(new BigDecimal(offerPercentage)).divide(new BigDecimal(100));
	}
	
	public static String messageForDealOfTheDay(Connection con){
		GoodsService goodsService = new GoodsServicesImpl();
		try{
			List<GoodsVo>goodsVos = goodsService.getDealOfTheDayGoods(con, false, false);
			String message = "Dear customer, check out deal of the day on http://sablebadiya.com/practiceapp/deal_of_the_day ,";
			
			for (GoodsVo goodsVo : goodsVos) {
				message = message + goodsVo.getGoodsName() + "(Rs. " + goodsVo.getPrice() + "/" + goodsVo.getUom() + "), "; 
			}
			
			message = message + "hurry up offers valid till stock is available, you can order by website or call us at +91-9584069665.";
			
			return message;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getMediaUrl(String imageUrl){
		String subVal = imageUrl.substring(imageUrl.indexOf("media"), imageUrl.length());
		
		int count = StringUtils.countMatches(subVal, "/");
		if(count==1){
			imageUrl = imageUrl.replaceAll("media", "media/app");
		}else{
			int lastIndex = imageUrl.lastIndexOf("/");
			String mediaName = imageUrl.substring(lastIndex, imageUrl.length());
			
			imageUrl = imageUrl.replaceAll(mediaName, "/app"+mediaName);
		}
		
		/*if(imageUrl.contains("/media/fruits/")){
			imageUrl = imageUrl.replaceAll("/media/fruits/", "/media/fruits/app/");
		}else if(imageUrl.contains("/media/vegetables/")){
			imageUrl = imageUrl.replaceAll("/media/vegetables/", "/media/vegetables/app/");
		}else{
			imageUrl = imageUrl.replaceAll("/media/", "/media/app/");
		}*/
		return imageUrl;
	}
	
	public static List<QuantityVo> getRuppeQuantity(){
		List<QuantityVo> rupeeQuantity = new ArrayList<>();
		for(int i=1; i<5; i++){
			Long id = new Long(i*5);
            QuantityVo quantityVo = new QuantityVo(id, id.toString(), "Rs");
            rupeeQuantity.add(quantityVo);
		}
		return rupeeQuantity;
	}
	
	public static void convertHtmlStringToPdf(String htmlContent){
		
		try {
			Document document = new Document(PageSize.LETTER);
		      PdfWriter pdfWriter = PdfWriter.getInstance
		           (document, new FileOutputStream("d://testpdf.pdf"));
		      document.open();
		      document.addAuthor("Real Gagnon");
		      document.addCreator("Real's HowTo");
		      document.addSubject("Thanks for your support");
		      document.addCreationDate();
		      document.addTitle("Please read this");

		      /*HTMLWorker htmlWorker = new HTMLWorker(document);
		      htmlWorker.parse(new StringReader(htmlContent));
		      document.close();*/
		      XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
		      worker.parseXHtml(pdfWriter, document, new StringReader(htmlContent));
		      document.close();
		      System.out.println("Done");
		      }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		
	}
	
	public static String getMediaUrlForCategory(String imageUrl){
		return imageUrl = imageUrl.replaceAll("/media/", "/media/app/");
	}
	
	public static void getLinkForCategory(List<CategoryVo> allCategory, Long categoryId){
		List<String> linkList = new ArrayList<>();
		
		getLinkForCategoryFurther(allCategory, categoryId, linkList);
		
		System.out.println(linkList);
	}
	
	public static void getLinkForCategoryFurther(List<CategoryVo> allCategory, Long categoryId, List<String> linkList){
		
		for (CategoryVo categoryVo : allCategory) {
			if(categoryId.equals(categoryVo.getCategoryId())){
				String val="<a href='getgoods?categoryId=catId&isFromSearch=false'> "+
						categoryVo.getCategoryName() + "</a>";
				
				val = val.replaceAll("catId", categoryId.toString());
				
				linkList.add(val);
				if(!categoryVo.getCategoryId().equals(categoryVo.getParentCategoryId())){
					getLinkForCategoryFurther(allCategory, categoryVo.getParentCategoryId(), linkList);
				}
			}
		}		
	}
	
}
