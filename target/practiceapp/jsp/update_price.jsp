<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<form action="updatepriceforitems" method="post">
  <div class="container">
  
  <div class="row">
	  <div class="col-sm-9"><h2>Items Based On Categories:</h2></div>
	  <div class="col-sm-3"><button class="btn btn-primary update-click">Click For Update</button></div>
  </div>
  <div class="panel-group" id="accordion">
	  <c:forEach items="${categories}" var="category" varStatus="loop">
	    <div class="panel panel-default">
	      <div class="panel-heading">
	        <h4 class="panel-title">
	          <a data-toggle="collapse" data-parent="#accordion" href="#collapse${category.categoryId}">${category.categoryName}</a>
	        </h4>
	      </div>
	      <c:choose>
	      	<c:when test="${loop.index==0}">
	      		 <div id="collapse${category.categoryId}" class="panel-collapse collapse in">
	      	</c:when>
	      	<c:otherwise>
	      		 <div id="collapse${category.categoryId}" class="panel-collapse collapse">
	      	</c:otherwise>
	      </c:choose>
	     
	        <div class="panel-body">
		    	<table class="table table-striped table-bordered">
		            <tbody>
		              <tr>
		                <th>ITEM NAME</th>
		                <th>OUT OF STOCK</th>
		                <th>PRICE</th>
		                <th>MSRP</th>
		                <th>DOTD</th>
		              </tr>
		              <c:forEach items="${category.goods}" var="good" varStatus="status">
			              <tr>
			  				<td>${good.goodsName}</td>
			  				<td>
			  					<select name="select-stock">
			  						<c:choose>
			  							<c:when test="${good.inStock}">
			  								<option value="out ${good.goodsId}">Out Of Stock</option>
			  								<option value="in ${good.goodsId}" selected>In Stock</option>
			  							</c:when>
			  							<c:otherwise>
			  								<option value="out ${good.goodsId}" selected>Out Of Stock</option>
			  								<option value="in ${good.goodsId}">In Stock</option>
			  							</c:otherwise>
			  						</c:choose>
			  					</select>
			  				</td>
			                <td data-id="${good.goodsId}"><input type="text" name="price" class="price" style="width:50px;" value="${good.price}"></td>
			                <td data-id="${good.goodsId}"><input type="text" class="msrp" name="msrp" style="width:50px;" value="${good.msrp}"></td>
			              	<td>
			              		<select name="todaysDeal">
			  						<c:choose>
			  							<c:when test="${good.isTodaysDeal}">
			  								<option value="false ${good.goodsId}">No</option>
			  								<option value="true ${good.goodsId}" selected>Yes</option>
			  							</c:when>
			  							<c:otherwise>
			  								<option value="false ${good.goodsId}" selected>No</option>
			  								<option value="true ${good.goodsId}">Yes</option>
			  							</c:otherwise>
			  						</c:choose>
			  					</select>
			              	</td>
			              </tr>
		              </c:forEach>
		            </tbody>
		          </table>
	        </div>
	      </div>
	    </div>
    </c:forEach>
  </div>
</div>

</form>


<script>
	$(".update-click").click(function(){
		startLoading();
		$('.price').each(function(index, obj){
		    var goodsId = $(this).parent('td').data('id');
		    var price = $(this).val();
		    
		    $(this).val(price+" "+goodsId);
		});
		
		$('.msrp').each(function(index, obj){
		    var goodsId = $(this).parent('td').data('id');
		    var price = $(this).val();
		    
		    $(this).val(price+" "+goodsId);
		});
		
	});


</script>