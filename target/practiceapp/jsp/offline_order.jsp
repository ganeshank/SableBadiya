<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<script src="js/offline_order.js"></script>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

		<form role="form" action="createofflineorder" method="post" id="offline_order_form">
		<div class="container">
			<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Customer Name: </b> 
	  				<input type="text" class="form-control" id="customer_name" name="customer_name" placeholder="Enter customer name" required>
	  			</p>
	  		</div>
	  		
	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Mobile Number: </b> 
	  				<input type="tel" class="form-control" id="mobile" title="Enter valid 10 digit mobile number" name="mobile_number" pattern=".{10,}" 
	  				placeholder="Enter only 10 digits of your mobile number" required>
	  			</p>
	  		</div>
	  		
	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Customer Address: </b> 
	  				<textarea class="form-control" rows="3" id="address" name="address"></textarea>
	  			</p>
	  		</div>
	  		
	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Email: </b> 
	  				<input type="text" class="form-control" id="email" name="email" placeholder="Enter Email here" required>
	  			</p>
	  		</div>
	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Order Date:</b> 
	  				<input type="date" id="order_date" name="order_date">
	  			</p>
	  		</div>
		</div>
	       <div class="container well" style="margin-top:0px;padding-bottom:0px;">
	       <div class="row">
	       		<div class="col-sm-4">
	  			<div class="col-sm-3">
	  				<p style="font-family:verdana;"><b>Goods:</b></p>
	  			</div>
	  			<div class="col-sm-9">
		            <input type="text" class="form-control" id="goods_id" name="goods" placeholder="Enter goods name" required>
		            <input type="hidden" id="g_id" value="0"/>
		        </div>
 			</div>
  			
	  		<div class="col-sm-4">
	  			<div class="col-sm-3">
	  				<p style="font-family:verdana;"><b>Quantity:</b></p>
	  			</div>
	  			<div class="col-sm-9">
	  				<select id="qty-goods" class="form-control">
	  					<option value="select">Select Quantity</option>
	  					<c:forEach items="${quantities}" var="quantity" varStatus="status">
	  						<option value="${quantity.quantityId}">${quantity.weight} ${quantity.uom}</option>
	  					</c:forEach>
	  					<option value="5 Rs">5 Rs</option>
		              	<option value="10 Rs">10 Rs</option>
		              	<option value="15 Rs">15 Rs</option>
		              	<option value="20 Rs">20 Rs</option>
		              	<option value="25 Rs">25 Rs</option>
		              	<option value="30 Rs">30 Rs</option>
	  				</select>
		        </div>
	  		</div>
	  		
	  		<div class="col-sm-4">
	  			<div class="col-sm-3">
	  				<p style="font-family:verdana;"><b>Price:</b></p>
	  			</div>
	  			<div class="col-sm-9">
		            <input type="text" class="form-control" id="price" name="price" placeholder="Enter price for goods" required>
		        </div>
 			</div>
	       </div>
		  	
 			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
	 			<div class="col-sm-4">
		  			<input class="btn btn-success" id="addGoods" type="button" value="Add Goods">
	 			</div>
	  		
 			</div>
 			
	  		</div>
	  		
	  		<table id="example" class="display responsive nowrap" cellspacing="0" width="100%">
		        <thead>
		            <tr>
		                <th>Goods Name</th>
		                <th>Quantity</th>
		                <th>Price</th>
		                <th>Action</th>
		            </tr>
		        </thead>
		    </table>
		    <br/>
		    <div class="row">
	 			<div class="col-sm-12">
		  			<input class="btn btn-primary" id="create_order" type="button" value="Create Order">
	 			</div>
 			</div>
		 </form>