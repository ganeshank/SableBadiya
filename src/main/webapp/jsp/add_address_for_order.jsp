<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <br/><br/><br/>
<div class="container">
	<div class="form-group">
	  <div class="col-sm-4 label-class">
	  	<label for="orderdate" data-lebel="orderDate" class="label-tag">Select Search Type:</label>
	  	<select class="form-control" id="searchTYpe" name="fieldEx">
	  		<option value="0">---Please Select---</option>
             <option value="1">Full Name</option>
             <option value="2">Mobile Number</option>
             <option value="3">Email</option>
             <option value="4">Address</option>  
         </select>
      </div>
       <div class="col-sm-4">
       <label for="orderdate" data-lebel="orderDate" class="label-tag">Search Value</label>
	  	<input type="text" class="form-control" id="search_value"/>
	  </div>
	  <div class="col-sm-4">
	  	<label for="orderdate" data-lebel="orderDate" class="label-tag">Action </label><br/>
	  	<button type="button" name="goods_name" class="btn btn-info search-address">Search</button>
	  	<button type="button" name="goods_name" class="btn btn-info add-new-address">New Address</button>
	  </div>
      
    </div>
</div><br/><br/>

<div id="search_table">
	<table id="example" class="display responsive nowrap" style="display: none;" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Full Name</th>
                <th>Mobile Number</th>
                <th>Email</th>
                <th>Address</th>
                <th>City</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>Full Name</th>
                <th>Mobile Number</th>
                <th>Email</th>
                <th>Address</th>
                <th>City</th>
                <th>Actions</th>
            </tr>
        </tfoot>
    </table>
</div>

<script>
var Table;
	$(document).ready(function(){
		
		Table = $("#example").DataTable({
		data:[],
		columns: [
			{ "data": "fullName" },
            { "data": "totalAmount" },
            { "data": "orderDate" },
            { "data": "orderedBy" },
            { "data": "cartStatusName" },
            { "data": "viewLink" }
		]
		});
		
		$("#example_wrapper").hide();
	})
	
	$(".search-address").click(function(){
		$.ajax({
			url: "adminhome/search_address",
			dataType:"text",
			success: function(response){
				$("#example").show();
				Table.clear().draw();
				Table.rows.add(result.data).draw();
			},
			error: function(response){
				console.log(response.status);
				console.log(response);
			}
			
		});
	});
</script>