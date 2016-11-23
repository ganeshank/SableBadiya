$( document ).ready(function() {
	loadContainer("sellerhome/new_orders_for_seller");
	/*var eventSource = new EventSource("getneworderscountforseller");

	eventSource.onmessage = function(event) {
		console.log(event.data);
		$(".new-order-count").text(event.data);
	};*/
});

function loadContainer(url){
	$.ajax({
		url: url,
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