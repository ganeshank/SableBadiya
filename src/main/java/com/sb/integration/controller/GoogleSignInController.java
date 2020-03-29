package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.UserService;
import com.sb.integration.service.impl.UserServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.UserVo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 * Servlet implementation class GoogleSignInController
 */
@WebServlet("/google_signin")
public class GoogleSignInController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleSignInController() {
        super();
        // TODO Auto-generated constructor stub
    }
    private static final String CLIENT_ID = "1082344612925-6bfsqivi3u0vmj1i2el7qbc6chnofkn8.apps.googleusercontent.com";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Google Sign controller enter-----------------------------*****");
		String idTokenStr = request.getParameter("token_id");
		try {
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
		    .setAudience(Collections.singletonList(CLIENT_ID))
		    .setIssuer("accounts.google.com").build();
	
			// (Receive idTokenString by HTTPS POST)
		
			GoogleIdToken idToken=null;
		
			idToken = verifier.verify(idTokenStr);
			
			if (idToken != null) {
			  Payload payload = idToken.getPayload();
		
			  // Print user identifier
			  String userId = payload.getSubject();
			  System.out.println("User ID: " + userId);
		
			  // Get profile information from payload
			  String email = payload.getEmail();
			  boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			  String name = (String) payload.get("name");
			  /*String pictureUrl = (String) payload.get("picture");
			  String locale = (String) payload.get("locale");
			  String familyName = (String) payload.get("family_name");
			  String givenName = (String) payload.get("given_name");*/
			  
			  String userIdForPassword = payload.getSubject();
			  
			  System.out.println(email+"....."+emailVerified+"...."+name);
			  
			  UserVo userVo = new UserVo();
			  userVo.setFullName(name);
			  userVo.setUserName(name);
			  userVo.setEmail(email);
			  userVo.setPassword(userIdForPassword);
			  
			  ServletContext ctx = request.getServletContext();
			  DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			  Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			  
			  UserService userService = new UserServiceImpl();
			  UserVo userDetails = userService.otherSignUpAndSignIn(userVo, con, 2);
			  
			  HttpSession session = request.getSession();
			  session.setAttribute("userDetails", userDetails);
			  session.setAttribute("itemCount", null);
			  
			  response.setContentType("text/plain");
			  response.setStatus(200);
		      response.getWriter().write("Successfully login.");
		
			} else {
			  System.out.println("Invalid ID token.");
			  throw new Exception("Invalid ID token.");
			}
			
			
			
		} catch (Exception e) {
			System.out.println("Some exception come at gmail login::::::"+e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setContentType("text/plain");
			response.setStatus(500);
		    response.getWriter().write("Something goes wrong.");
		}
		
	}
}
