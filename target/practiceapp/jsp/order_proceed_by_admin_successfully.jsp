<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	  	<title>Admin Order Processed</title>
	  	<meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
	    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
	    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">
  	</head>
<body>
	<button type="button" id="modal-button-click" style="display: none" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal"></button>
	<a href="../adminhome" id="new-order-click" style="display: none"></a>
  <!-- Modal -->
  <div class="modal fade" data-backdrop="static" data-keyboard="false" id="myModal" role="dialog">
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Order Successfully processed</h4>
        </div>
        <div class="modal-body">
          <p>Order is successfully processed and send notification to respective seller and field executive.</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default close-click" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
	$(".close-click").click(function(){
		var href = $('#new-order-click').attr('href');
	    window.location.href = href;
	});
	
	$(document).ready(function(){
		$("#modal-button-click").click();
	});
</script>
	
</body>
</html>