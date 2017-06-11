<%@page import="com.sl.util.GlobalConstants"%>
<html>
<head>
<title>Email Registration</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.min.css">
<script src="js/jquery-1.11.3.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/validator.min.js"></script>
<style>
.form-horizontal .control-label {
   margin-bottom: 5px;
}
.form-horizontal .form-group {
    margin-bottom: 5px;
    margin-left:0px;
    margin-right:0px;
}
body {
    background-color:#f4f4f4;
}
</style>
</head>
<body>
<div style="max-width:400px;padding:10px 20px;margin:auto;margin-top:calc(50vh - 220px);">
<h3 style="text-align:center">Welcome back <%=session.getAttribute(GlobalConstants.USER_NAME)%></h3>
You can access below links<br/>
<a href="ChangePassword">change password</a> <br/>
<a href="Logout">Logout</a> <br/>
</div>
</body>
</html>