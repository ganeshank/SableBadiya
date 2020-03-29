<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
      <title>SableBadiya</title>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
      <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
      <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
      <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">
      <link rel="stylesheet" href="css/dataTables.bootstrap.min.css">
      <link rel="stylesheet" type="text/css" href="css/main.css">

  </head>
<body>
	<div class="container-fluid text-center" style="background-color:black;color:white;font-family:verdana;">
    <h4>Admin Portal</h4>
  </div>

  <div class="container text-center" style="background-color:#9494b8;">
    <div class="btn-group text">
      <button type="button" class="btn btn-primary" style="width:105px;">User</button>
      <div class="btn-group">
        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" style="width:105px;">
        Seller <span class="caret"></span></button>
        <ul class="dropdown-menu" role="menu">
          <li><a href="addsellerclick">Add Seller</a></li>
          <li><a href="updatesellerclick">View Seller</a></li>
        </ul>
      </div>
      <div class="btn-group">
        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" style="width:105px;">
        Category <span class="caret"></span></button>
        <ul class="dropdown-menu" role="menu">
          <li><a href="addcategory.html">Add Category</a></li>
          <li><a href="updatecategory.html">Update Category</a></li>
        </ul>
      </div>
    </div>
  </div>

  <div class="container">
    <h3>Seller Details</h3>
    <div class="table-responsive">
      <table class="table table-striped table-bordered table-hover" id="mydata">
        <thead>
          <tr>
            <th>SNo.</th>
            <th>Seller Name</th>
            <th>Contact No.</th>
            <th>Email Id</th>
            <th>Action</th>
          </tr>
        </thead>

        <tfoot>
          <tr>
            <th>SNo.</th>
            <th>Seller Name</th>
            <th>Contact No.</th>
            <th>Email Id</th>
            <th>Action</th>
          </tr>
        </tfoot>
        <tbody>
          <tr>
            <td>01</td>
            <td>abc</td>
            <td>69861654</td>
            <td>woeryuewir@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-edit"></span></td>
          </tr>

          <tr>
            <td>02</td>
            <td>def</td>
            <td>69861263549</td>
            <td>vmlxkfl@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>03</td>
            <td>iuty</td>
            <td>65423</td>
            <td>nkjfmf@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>04</td>
            <td>utoeri</td>
            <td>5236822355</td>
            <td>kljyafkjr@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>05</td>
            <td>oeriuto</td>
            <td>12398525</td>
            <td>fdtrlifkr@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>06</td>
            <td>oiurtow</td>
            <td>894352122</td>
            <td>lsjvs[pa@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>07</td>
            <td>lairo</td>
            <td>6986698</td>
            <td>mosuifosir@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>08</td>
            <td>wypiowjf</td>
            <td>566385</td>
            <td>sertrdwir@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>09</td>
            <td>kllosjo</td>
            <td>89665</td>
            <td>xsdferedir@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>10</td>
            <td>ierwtpio</td>
            <td>56341</td>
            <td>wiytpwoir@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>11</td>
            <td>uytropwi</td>
            <td>456321896</td>
            <td>tyruoue8riir@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>12</td>
            <td>opi</td>
            <td>896341</td>
            <td>wuerypoqr@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>13</td>
            <td>ofskj</td>
            <td>45630114</td>
            <td>nmbbaifiar@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>

          <tr>
            <td>14</td>
            <td>uwyrpq</td>
            <td>896264</td>
            <td>loiyaifoair@mail.com</td>
            <td><span class="glyphicon glyphicon-remove-circle"></span> <span class="glyphicon glyphicon-ok-circle"></span></td>
          </tr>
        </tbody>
      </table>
    </div>
    
  </div>
  
    <script src="js/jquery.js"></script>  
  <script src="js/bootstrap.min.js"></script>  
  <script src="js/jquery.dataTables.min.js"></script>    
  <script src="js/dataTables.bootstrap.min.js"></script>
  <script>
  $('#mydata').dataTable();
  </script>
</body>
</html>