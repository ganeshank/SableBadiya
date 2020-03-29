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
<script type="text/javascript">
$(document).ready(function() {
	console.log("hhhh");
	$('#example').DataTable( {
        "ajax": "viewneworders",
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
	
	/* $(".view-new-order").click(function(){
		var cartId = $(this).data("cartid");
		alert(cartId);
	}); */
	
} );

function viewNewOrder(cartId){
	
	$.ajax({
		url: "adminhome/view_new_order_admin",
		data:{cartId:cartId},
		dataType:"text",
		success: function(response){
			
			$("#main-container").html(response);
		},
		error: function(response){
			console.log(response.status);
			console.log(response);
		}
		
	});
}

function generateBill(cartId){
	
	window.open('generate_bill?cartId='+ cartId,'_blank');
}


</script>