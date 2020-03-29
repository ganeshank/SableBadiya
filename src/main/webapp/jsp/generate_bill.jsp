<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    
    <title>Example 2</title>

  </head>
  <body>
    <header class="clearfix" style="content: ""; display: table;clear: both;">
      <div id="logo" align="center">
        <span class="name" style="font-size:25px;">Order Invoice</span>
		
      </div>
      
    </header>
    <main>
      <div id="details">
        <div style="display:inline-block; float:left; padding-left:80px;">
			<span style="font-size:20px;">Order By:</span>
			<span style="font-size:20px;">${orderDetails.shippingAddress.contactName}</span><br/>
		<span style="font-size:15px;">${orderDetails.shippingAddress.addressLine1}</span><br/>
		<span style="font-size:15px;">Phone: ${orderDetails.shippingAddress.contactNumber}</span>
		<span style="font-size:15px;">Delivery Date: ${orderDetails.deliveryDate}</span>
		</div>
		
		<div style="display:inline-block; float:right; padding-right:80px;">
			<span style="font-size:20px;">SableBadiya.com</span><br/>
			<span style="font-size:15px;">MIG 1078, aditya nagar durg, (C.G.)</span><br/>
			<span style="font-size:15px;">+91-9584069665</span>
			

		</div>
      </div>
	  <br/><br/>
<br/><br/><br/>	  
	  
	  
	  
      <table align="center">
        <thead>
          <tr>
            <th class="no" align="center" style="border: 1px solid black; font-size: 13px;">#SN</th>
            <th class="desc" align="center" style="border: 1px solid black;font-size: 13px;">ITEM NAME</th>
            <th class="unit" align="center" style="border: 1px solid black;font-size: 13px;">UNIT PRICE</th>
            <th class="qty" align="center" style="border: 1px solid black;font-size: 13px;">QUANTITY</th>
            <th class="total" align="center" style="border: 1px solid black;font-size: 13px;">TOTAL</th>
          </tr>
        </thead>
        <tbody>
        	<c:forEach items="${orderDetails.cartItems}" var="item" varStatus="itemIndex">
				<tr style="border: 1px solid black;">
	            <td class="no" align="center" style="font-size: 13px;">${itemIndex.index+1}</td>
	            <td class="desc" align="center" style="font-size: 13px;">${item.goodsVo.goodsName}</td>
	            <td class="unit" align="center" style="font-size: 13px;">Rs. ${item.goodsVo.price}/${item.goodsVo.uom}</td>
	            <td class="qty" align="center" style="font-size: 13px;">${item.quantity} ${item.uom}</td>
	            <td class="total" align="center" style="font-size: 13px;">Rs. ${item.price}</td>
	          </tr>
			</c:forEach>
	   	  
        </tbody>
        <tfoot>
          <tr>
            <td colspan="2"></td>
            <td colspan="2" style="font-size: 13px;">SUBTOTAL</td>
            <td style="font-size: 13px;">${orderDetails.subtotalAmount}
          </tr>
          <tr>
            <td colspan="2"></td>
            <td colspan="2" style="font-size: 13px;">SHIPPING CHARGE</td>
            <td style="font-size: 13px;">${orderDetails.shippingCharge}</td>
          </tr>
		  <tr>
            <td colspan="2"></td>
            <td colspan="2" style="font-size: 13px;">Offer</td>
            <td style="font-size: 13px;">00</td>
          </tr>
          <tr>
            <td colspan="2"></td>
            <td colspan="2" style="font-size: 13px;">GRAND TOTAL</td>
            <td style="font-size: 13px;">${orderDetails.totalAmount}</td>
          </tr>
        </tfoot>
      </table><br/>
      <div id="thanks" align="center" style="font-size: 15px;">Thank you for shopping on SableBadiya.</div>
    </main>
    <footer>
      <div  align="center" style="font-size: 15px;">Order details was created on a computer and is valid without the signature and seal.
		</div>
	</footer>
  </body>
</html>