	<table id="example" class="display responsive nowrap" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Order Number</th>
                <th>Total Amount</th>
                <th>Order Date</th>
                <th>User Name</th>
                <th>Order Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>Order Number</th>
                <th>Total Amount</th>
                <th>Order Date</th>
                <th>User Name</th>
                <th>Order Status</th>
                <th>Actions</th>
            </tr>
        </tfoot>
    </table>
    
     <button type="button" style="display: none;" id="btn-id" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">Open Modal</button>
    <!-- Modal -->
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">View Order</h4>
        </div>
        <div class="modal-body" id="view-order">
          
        </div>
        <div class="modal-footer">
          <!-- <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
        </div>
      </div>
      
    </div>
  </div>
    
<script type="text/javascript">
$(document).ready(function() {
	$('#example').DataTable( {
        "ajax": "ordersearch",
        "columns": [
            { "data": "orderNumber" },
            { "data": "totalAmount" },
            { "data": "orderDate" },
            { "data": "orderedBy" },
            { "data": "cartStatusName" },
            { "data": "viewLink" }
        ],
        "fnDrawCallback": function (oSettings) {
        	$(".new-order-count").text(oSettings.aoData.length);
         }
    } );
	
} );

function viewNewOrder(cartId){
	//alert(cartId);
	$.ajax({
		url: "adminhome/view_new_order_admin",
		data:{cartId:cartId},
		dataType:"text",
		success: function(response){
			
			
			//$("#main-container").html(response);
		},
		error: function(response){
			console.log(response.status);
			console.log(response);
		}
		
	});
}


</script>