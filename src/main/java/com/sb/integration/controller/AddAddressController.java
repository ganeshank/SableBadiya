package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.UserService;
import com.sb.integration.service.impl.UserServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.util.UserUtil;
import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.UserVo;

/**
 * Servlet implementation class AddAddressController
 */
public class AddAddressController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddAddressController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AddressVo addressVo = new AddressVo();
		addressVo.setContactName(request.getParameter("full-name"));
		addressVo.setContactNumber(request.getParameter("contact-number"));
		addressVo.setAddressLine1(request.getParameter("address1"));
		addressVo.setAddressLine2(request.getParameter("address2"));
		addressVo.setCity(request.getParameter("city"));
		addressVo.setState(request.getParameter("state"));
		addressVo.setPinCode(request.getParameter("pincode"));
		addressVo.setLandMark(request.getParameter("landmark"));
		addressVo.setEmail(request.getParameter("email"));
		
		String isFromOrder = request.getParameter("isfromorder");
		String buttonClick = request.getParameter("button-click");
		
		ServletContext ctx = request.getServletContext();
		DataSource ds = (DataSource) ctx.getAttribute("dataSource");
		Connection con = DataSourceUtil.getConnectionThruDataSource(ds);

		try {
			UserService userService = new UserServiceImpl();
			HttpSession session = request.getSession();
			if(buttonClick!=null){
				if(buttonClick.equals("new")){
					UserVo userDetails = (UserVo) session.getAttribute("userDetails");
					
					Long userId = null;
					Boolean isGuestUser = UserUtil.isGuestUser(session);
					if(!isGuestUser){
						userId = userDetails.getUserId();
					}
					userService.addAddressForUser(con, addressVo, userId, isGuestUser);
					
					if(isGuestUser){
						CartDetails cartDetails = (CartDetails)session.getAttribute("guestCart");
						List<AddressVo> addressVos = cartDetails.getAddresses();
						if(addressVos==null)
							addressVos = new ArrayList<>();
						
						addressVos.add(addressVo);
						System.out.println("----"+addressVo.getAddressId());
						
						cartDetails.setAddresses(addressVos);
						session.setAttribute("guestCart", cartDetails);
					}
					
					if (isFromOrder.equalsIgnoreCase("true")) {
						/*request.setAttribute("message", "Address is addded successfully");
						RequestDispatcher rd = request.getRequestDispatcher("placeorder");
						rd.forward(request, response);*/
						session.setAttribute("addAddressMessage", "Address is addded successfully");
						response.sendRedirect("placeorder");
					}
					else{
						// TODO
					}
				}else if(buttonClick.equals("edit")){
					Long addressId = Long.parseLong(request.getParameter("addressIdForEdit"));
					addressVo.setAddressId(addressId);
					
					userService.updateAddress(con, addressVo);
					session.setAttribute("addAddressMessage", "Address is updated successfully");
					response.sendRedirect("placeorder");
					// Edit new address logic will come here!!!!!
					
				}else{
					throw new Exception();
				}
			}else{
				throw new Exception();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
