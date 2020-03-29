	<table id="example" class="display responsive nowrap" cellspacing="0" width="100%">
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
});


</script>