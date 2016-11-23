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

<script type="text/javascript">
$(document).ready(function() {
	$('#example').DataTable( {
        "ajax": "sellerapprovedorders",
        "columns": [
            { "data": "orderNumber" },
            { "data": "orderDate" },
            { "data": "totalAmount" },
            { "data": "cartStatusName" },
            { "data": "viewLink" }
        ],
        "fnDrawCallback": function (oSettings) {
        	$(".seller-approved-count").text(oSettings.aoData.length);
         }
    } );
} );

</script>