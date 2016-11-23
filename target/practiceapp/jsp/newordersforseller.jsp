<body>
	<table id="example" class="display responsive nowrap" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Order Number</th>
                <th>Order Date</th>
                <th>Order Total</th>
                <th>Order Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>Order Number</th>
                <th>Order Date</th>
                <th>Order Total</th>
                <th>Order Status</th>
                <th>Actions</th>
            </tr>
        </tfoot>
    </table>
    
</body>
<script type="text/javascript">
$(document).ready(function() {
	$('#example').DataTable( {
        "ajax": "viewnewordersforseller",
        "columns": [
            { "data": "orderNumber" },
            { "data": "orderDate" },
            { "data": "totalAmount" },
            { "data": "cartStatusName" },
            { "data": "viewLink" }
        ],
        "fnDrawCallback": function (oSettings) {
        	$(".new-order-count").text(oSettings.aoData.length);
         }
    } );
} );

function viewNewOrder(cartId){
	
	$.ajax({
		url: "sellerhome/view_new_order_seller",
		data:{cartId:cartId},
		dataType:"text",
		success: function(response){
			console.log(response);
			$("#main-container").html(response);
		},
		error: function(response){
			console.log(response.status);
			console.log(response);
		}
		
	});
}

</script>
</html>