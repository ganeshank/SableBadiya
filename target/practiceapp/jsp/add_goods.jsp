<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<div class="container col-sm-4 col-sm-offset-4">
		<form role="form" action="adminhome/add_goods" method="post" id="additem_form" enctype="multipart/form-data">
    		<div class="form-group">
      			<label for="name">Goods Name:</label>
      			<input type="text" class="form-control" id="goods_name" name="goods_name" placeholder="Enter goods name" required>
    		</div>
	        <div class="form-group">
	        	<div class="row">
    				<div class="col-sm-6 col-xs-6">
    					<p><strong>Select Category:</strong></p>
    				</div>
    				<div class="col-sm-6 col-xs-6">
    					<select name="category">
			            	<option value="select">Select</option>
			            	<c:forEach items="${categories}" var="category" varStatus="status">
			            		<option value="${category.categoryId}">${category.categoryName}</option>
			            	</c:forEach>
			            	<!-- <option value="veg">Vegetable</option>
			            	<option value="fruit">Fruits</option> -->
			            </select>
    				</div>
    			</div>
	        </div>
	        <div class="form-group">
	        	<label for="name">Check Quantity:</label><br/>
	        	<div class="row">
	        	<c:forEach items="${quantityVos}" var="quantity" varStatus="status">
	        		<div class="col-sm-4">
	        			<input type="checkbox" name="qty" value="${quantity.quantityId}">${quantity.weight} ${quantity.uom}</input>
	        		</div>
	        	</c:forEach>
	        	</div>
	        </div>
    		<div class="form-group">
    			<div class="row">
    				<div class="col-sm-6 col-xs-6">
    					<p><strong>Select Seller:</strong></p>
    				</div>
    				<div class="col-sm-6 col-xs-6">
    					<select name="seller">
			            	<option value="select">Select</option>
			            	<option value="1">SB Retail</option>
			            </select>
    				</div>
    			</div>
    		</div>
    		<div class="form-group">
    			<div class="row">
    				<div class="col-sm-6 col-xs-6">
    					<p><strong>Allow Rupee Option:</strong></p>
    				</div>
    				<div class="col-sm-6 col-xs-6">
    					<select name="allow_rupee">
			            	<option value="select">Select</option>
			            	<option value="true">Yes</option>
			            	<option value="false">No</option>
			            </select>
    				</div>
    			</div>
    		</div>
    		<div class="form-group">
    			<div class="row">
    				<div class="col-sm-6 col-xs-6">
    					<p><strong>Stock Management:</strong></p>
    				</div>
    				<div class="col-sm-6 col-xs-6">
    					<select name="stock">
			            	<option value="select">Select</option>
			            	<option value="true">In Stock</option>
			            	<option value="false">Out Stock</option>
			            </select>
    				</div>
    			</div>
    		</div>
    		<div class="form-group">
	            <label for="confirm password">Select Image:</label>
	            <input type="file" class="form-control" name="image" placeholder="Select image for goods" required>
	        </div>
	        <div class="form-group">
	            <label for="confirm password">Price:</label>
	            <input type="input" class="form-control" name="price" placeholder="Enter Price" required>
	        </div>
	        <div class="form-group">
	            <label for="confirm password">MRP:</label>
	            <input type="input" class="form-control" name="mrp" placeholder="Enter MRP" required>
	        </div>
    		<input class="btn btn-success" type="button" value="Add Goods" onclick="validate()">
    		<br><br>
		 </form>
	</div>
	
	
	<script>
		function validate(){
			$("#additem_form").submit();
		}
		
	
	</script>