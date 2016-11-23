$("p").delegate(
		".deliver-here",
		"click",
		function() {
			startLoading();
			var addressId = $(this).parent('p').data("addressid");
			
			$.ajax({
				url: "selectaddress",
				data: {addressId:addressId},
				dataType:"text",
				success: function(response){
					console.log(response);
					
					/*document.getElementById('delivery-address').style.pointerEvents = 'none';
					document.getElementById('order-summary').style.pointerEvents = 'auto';
			  		document.getElementById('delivery-option').style.pointerEvents = 'none';
			  		document.getElementById('payment-method').style.pointerEvents = 'none';*/
					
					$("#order-summary").attr("data-toggle","collapse");
			  		$("#delivery-option").removeAttr("data-toggle");
			  		$("#payment-method").removeAttr("data-toggle");
					
			  		$("#order-summary").click();
			  		stopLoading();
				},
				error: function(response){
					stopLoading();
				}
				
			});
		
});

$(".proceed-order-summary").click(function(){
	/*document.getElementById('delivery-address').style.pointerEvents = 'none';
	document.getElementById('order-summary').style.pointerEvents = 'none';
	document.getElementById('delivery-option').style.pointerEvents = 'auto';
	document.getElementById('payment-method').style.pointerEvents = 'none';*/
	startLoading();
	
	$("#delivery-option").attr("data-toggle","collapse");
	$("#payment-method").removeAttr("data-toggle");
		
	$("#delivery-option").click();
	stopLoading();
});

$(".proceed-delivery-option").click(function(){
	startLoading();
	var deliveryOption;
	switch($('input[type=radio][name=optradio]:checked').val()) {
	    case 'Deliver By Tomorrow':
	    	deliveryOption = "Deliver Tomorrow, "+ $("#sel1 option:selected").val();
	        break;
	        
	    case 'Deliver in 1 Hour':
	    	deliveryOption = "Deliver in 1 Hour";
	        break;
    
	}
	console.log(comment + "......." + deliveryOption);
	var comment = $("#comment").val();
	
	$.ajax({
		url: "adddelivery",
		data: {comment:comment, deliveryOption:deliveryOption},
		dataType:"text",
		success: function(response){
			console.log(response);
			
			/*document.getElementById('delivery-address').style.pointerEvents = 'none';
			document.getElementById('order-summary').style.pointerEvents = 'none';
			document.getElementById('delivery-option').style.pointerEvents = 'none';
			document.getElementById('payment-method').style.pointerEvents = 'auto';*/
			$("#payment-method").attr("data-toggle","collapse");
			$("#payment-method").click();
			stopLoading();
		},
		error: function(response){
			stopLoading();
		}
		
	});
	
});

$(".order-confirm").click(function(){
	startLoading();
	var cartId = $(this).parent("p").data("cartid");
	$.ajax({
		url: "orderconfirm",
		data: {cartId:cartId},
		dataType:"text",
		success: function(response){
			/*console.log(response);*/
			window.location.href = "placeordersuccess";
		},
		error: function(response){
			alert("Order is not created, Please contact to customer care executive.");
			stopLoading();
		}
		
	});
});

$('input[type=radio][name=optradio]').on('change', function() {
    switch($(this).val()) {
        case 'Deliver By Tomorrow':
            $('#sel1').prop('disabled', false);
            break;
            
        case 'Deliver in 1 Hour':
            $('#sel1').prop('disabled', 'disabled');
            break;
        
    }
});
