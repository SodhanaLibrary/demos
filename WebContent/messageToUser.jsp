<%@page import="com.sl.util.GlobalConstants"%>
<html>
<head>
<title>Email Registration</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.min.css">
<style>
.tooltip.right {
	width: 100%;
}
</style>
</head>
<body>
	<div style="width: 700px; margin: auto;">
		<div class="jumbotron">
			<p>
			<% 
             String message = (String)request.getAttribute(GlobalConstants.MESSAGE);		
			%>
			<%=message %>
			</p>
		</div>
	</div>
</body>
</html>