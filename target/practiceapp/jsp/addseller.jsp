<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
      <title>SableBadiya</title>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
      <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
      <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">
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

  <div class="container" style="margin-top:15px;">
    <h3 class="col-sm-offset-2">Add Seller</h3>
      <form class="form-horizontal" role="form">
        <div class="form-group">
          <label class="control-label col-sm-4" for="sellername">Seller Name:</label>
          <div class="col-sm-5">
            <input type="text" class="form-control" name="seller_name" id="sellername" placeholder="Enter Name">
          </div>
        </div>

        <div class="form-group">
          <label class="control-label col-sm-4" for="contactnumber">Contact Number:</label>
          <div class="col-sm-5">
            <input type="number" class="form-control" name="seller_contact" id="contactnumber" placeholder="Enter Contact Number">
          </div>
        </div>

        <div class="form-group">
          <label class="control-label col-sm-4" for="address1">Address Line 1:</label>
          <div class="col-sm-5">
            <input type="text" class="form-control" name="address1" id="address1" placeholder="Enter Address Line 1">
          </div>
        </div>

        <div class="form-group">
          <label class="control-label col-sm-4" for="address2">Address Line 2:</label>
          <div class="col-sm-5">
            <input type="text" class="form-control" name="address2" id="address2" placeholder="Enter Address Line 2">
          </div>
        </div>

        <div class="form-group">
          <label class="control-label col-sm-4" for="city">City :</label>
          <div class="col-sm-5">
            <select class="form-control" id="city" name="city">
              <option>Durg</option>
            </select>
          </div>
        </div>

          <div class="form-group">
            <label class="control-label col-sm-4" for="state">State :</label>
            <div class="col-sm-5">
              <select class="form-control" id="state" name="state">
                <option>Chhattisgarh</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label class="control-label col-sm-4" for="pincode">PIN code :</label>
            <div class="col-sm-5">
              <input type="number" class="form-control" name="pincode" id="pincode" placeholder="Enter six digit PIN code" required>
            </div>
          </div>

          <div class="form-group">
          <label class="control-label col-sm-4" for="username">Username:</label>
          <div class="col-sm-5">
            <input type="text" class="form-control" name="username" id="username" placeholder="Enter Username">
          </div>
        </div>

        <div class="form-group">
          <label class="control-label col-sm-4" for="password">Password:</label>
          <div class="col-sm-5">
            <input type="password" class="form-control" name="password" id="password" placeholder="Enter Password">
          </div>
        </div>

        <div class="form-group">
          <label class="control-label col-sm-4" for="password1">Confirm Password:</label>
          <div class="col-sm-5">
            <input type="password" class="form-control" id="password1" placeholder="Re-type Password">
          </div>
        </div>

        <button type="button" class="btn btn-warning col-sm-offset-5" style="margin-top:25px;">ADD SELLER</button>
      </form> 
  </div>
</body>
</html>