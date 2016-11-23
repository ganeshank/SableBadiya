package com.cigital.integration.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cigital.integration.util.Constants;
import com.cigital.integration.util.UserUtil;
import com.cigital.integration.vo.UserVo;

public class AuthFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		
		
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		
		HttpSession session = request.getSession();
		Boolean justSessionStart = (Boolean)session.getAttribute("jsutUserSessionStart");
		
		if(justSessionStart==null){
			request.setAttribute("justSessionStart", true);
			session.setAttribute("jsutUserSessionStart", false);
		}
		
		String url = request.getServletPath();
		if(url.contains(".")){
			arg2.doFilter(arg0, arg1);
			return;
		}
		
		ServletContext ctx = request.getServletContext();
		@SuppressWarnings("unchecked")
		Map<Integer, List<String>> restrictedUrlMap = (Map<Integer, List<String>>)ctx.getAttribute("restrictedUrlMap");

		Integer roleId = null;
		if (UserUtil.isGuestUser(session)) {
			roleId = Constants.GUEST_USER_ROLE_ID;
		} else {
			UserVo userDetails = (UserVo) session.getAttribute("userDetails");
			roleId = userDetails.getUserRole().getRoleId().intValue();
		}
		
		List<String> urls = restrictedUrlMap.get(roleId.intValue());
		if(urls.contains(url)){
			arg2.doFilter(arg0, arg1);
		}else{
			// send it back to error page
			RequestDispatcher rd = request.getRequestDispatcher("jsp/error.jsp");
			rd.forward(request, response);
			return;
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
