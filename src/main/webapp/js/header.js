$( document ).ready(function() {
	updateCartItemCount();
});

function updateCartItemCount(){
	$.ajax({
        url : 'cartitemcount',
        success : function(responseText) {
           $(".item-count").text(responseText);
        }
    });
}
