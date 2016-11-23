package com.cigital.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.cigital.integration.service.OrderService;
import com.cigital.integration.service.impl.OrderServiceImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.util.UserUtil;
import com.cigital.integration.vo.AddressVo;
import com.cigital.integration.vo.CartDetails;

/**
 * Servlet implementation class DeleteAddress
 */
@WebServlet("/deleteaddress")
public class DeleteAddress extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAddress() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String addressId = request.getParameter("addressId");
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			HttpSession session = request.getSession();
			
			if(UserUtil.isGuestUser(session)){
				CartDetails cartDetails = (CartDetails)session.getAttribute("guestCart");
				List<AddressVo> addressVos = cartDetails.getAddresses();
				
				Iterator<AddressVo> itAddress = addressVos.iterator();
				while(itAddress.hasNext()){
					if(itAddress.next().getAddressId().equals(Long.parseLong(addressId))){
						itAddress.remove();
						break;
					}
				}
				cartDetails.setAddresses(addressVos);
				session.setAttribute("guestCart", cartDetails);
			}else{
				OrderService orderService = new OrderServiceImpl();
				orderService.deleteAddressForUser(con, Long.parseLong(addressId));
			}
			
			
			response.setContentType("text/plain");
		    response.getWriter().write("Address is deleted successfully!!");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
