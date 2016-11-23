$("p").delegate(
		".addtocartandbuynow",
		"click",
		function() {
			//startLoading();
			var quantityId = $(this).parent('p').siblings('.qty-class')
					.children('.qty-inner-div').find('.qty-select').val();
					
			var goodsId = $(this).parent('p').data("goods");
			var sellerId = $(this).parent('p').data("seller");
			
			console.log(quantityId+"...."+goodsId+"...."+sellerId);
			
			var quantityUom = $(this).parent('p').siblings('.qty-class').children('.qty-inner-div').find('.qty-select option:selected').text()
			
			var goodsImage = $(this).parent('p').parent('div.fruitsbox').find('.goods-img').attr('src');
			
			var goodsName = $(this).parent('p').data("goodsname");
			
			var rupee = 0;
			
			if(!($(this).parent('p').siblings('.qty-class').is(':visible'))){
				rupee = $(this).parent('p').siblings('.rupee-class')
				.children('.qty-inner-div-rupee').find('.qty-select-rupee').val();
			}
			
			$.ajax({
				url: "addtocart",
				data: {quantityId:quantityId, goodsId:goodsId, sellerId:sellerId, rupee:rupee},
				dataType:"json",
				success: function(response){
					console.log(response);
					$('.item-count').text(response);
					stopLoading();
					
					notif({
						  msg: "<p class='text-justify'><b>Success:</b> "+goodsName+" is added successfully into your cart</p>",
						  type: "success",
						  position: "bottom"
						});
				},
				error: function(response){
					stopLoading();
				}
				
			});
		
});

$( "td" ).delegate( "a", "click", function() {
		var itemId = $(this).data("itemid");
		
		var item = $(this).parent("td").parent("tr");
		$.ajax({
			url: "deleteitem",
			data: {itemId:itemId},
			success: function(response){
				console.log(response);
				$(".total-amount").text(response.totalAmount);
				$(".total-msrp").text(response.totalMsrp);
				$(".total-saving").text(response.totalSaving);
				
				item.remove();
				updateCartItemCount();
				
				if(response.totalAmount==0){
					$(".table-div").hide();
		  			$("#totalamountdiv").hide();
		  			$("#empty-cart-div").show();
					$( ".place-order" ).addClass( "disabled" );
					$( ".empty-cart" ).addClass( "disabled" );
				}
				else{
					$(".table-div").show();
		  			$("#totalamountdiv").show();
		  			$("#empty-cart-div").hide();
				}
			},
			error: function(response){
				console.log(response);
			}
			
		});
		
});


$( "td" ).delegate( ".qty-change", "change", function() {
			var cartItemTr = $(this).parents("tr");
			
			var cartItemId = $(this).data("itemid");
			var quantity = $(this).val();
			
			console.log(cartItemId+"...."+quantity);
			
			$.ajax({
				url: "quantitychange",
				data: {cartItemId:cartItemId, quantity:quantity},
				dataType:"json",
				success: function(response){
					console.log(response);
					console.log(response);
					var cartItem = response.cartItems[0];
					console.log(cartItem);
					cartItemTr.find(".price-span").text(cartItem.price);
					cartItemTr.find(".msrp-span").text(cartItem.msrp);
					cartItemTr.find(".save-span").text(cartItem.saving);
					
					$(".total-amount").text(response.totalAmount);
					$(".total-msrp").text(response.totalMsrp);
					$(".total-saving").text(response.totalSaving);
					
				},
				error: function(response){
					updateCartItemCount();
				}
				
			});
		
});

$(".empty-cart").click(function(){
		
		var cartId = $(this).data("cartid");
		console.log(cartId);
		
		$.ajax({
			url: "emptycart",
			data: {cartId:cartId},
			dataType:"text",
			success: function(response){
				console.log(response);
				
				$(".total-amount").text(0);
				$(".total-msrp").text(0);
				$(".total-saving").text(0);
				
				updateCartItemCount();
				$("#table-item").empty();
				$(".table-div").hide();
	  			$("#totalamountdiv").hide();
	  			$("#empty-cart-div").show();
				$( ".place-order" ).addClass( "disabled" );
				$( ".empty-cart" ).addClass( "disabled" );
			},
			error: function(response){
				console.log(response);
				alert("Not able to clear the cart, Please contact to customer service.");
			}
			
		});
		
	});

$(".place-order").click(function(){
	window.location.href = "placeorder";
});

/*$(".save-address").click(function(){
	$("#address-form").submit();
});*/
	

$("#remove").click(function() {
	$("#notification").fadeOut();
	$("#tri").fadeOut();
})	



$("div").delegate(".qty-option-radio","change",function() {
	var selectedVal = $(this).val();
	if(selectedVal == "qty-wise"){
		$(this).parent('div').parent('div').siblings('div.qty-class').show();
		$(this).parent('div').parent('div').siblings('div.rupee-class').hide();
	}else if(selectedVal == "rupee-wise"){
		$(this).parent('div').parent('div').siblings('div.qty-class').hide();
		$(this).parent('div').parent('div').siblings('div.rupee-class').show();
	}else{
		alert("Something went wrong, please contact to customer care.");
	}
});