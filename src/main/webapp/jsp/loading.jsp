<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>jQuery loadingModal Plugin Demos</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="css/jquery.loadingModal.min.css">

    <style>
    body { font-family:'Open Sans';}
        #wrapper {
            text-align: center;
            padding: 30px;
        }
    </style>
    <script src="js/jquery.loadingModal.min.js" type="text/javascript"></script>
</head>
<body>

<script type="text/javascript">
function startLoading(){
	$('body').loadingModal('show');
    $('body').loadingModal({text: 'Your request is in progress...', 'animation': 'wanderingCubes'});
}   
 
function stopLoading(){
    $('body').loadingModal('hide');
}

</script>
</body>
</html>