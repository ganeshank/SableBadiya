$( document ).ready(function() {
	loadContainer("adminhome/neworders_view");
	/*var eventSource = new EventSource("getneworders");

	eventSource.onmessage = function(event) {
		//console.log(event.data);
		$(".new-order-count").text(event.data);
	};
	
	var eventApprovedOrder = new EventSource("getapprovedorderscount");

	eventApprovedOrder.onmessage = function(event) {
		//console.log(event.data);
		$(".seller-approved-count").text(event.data);
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


