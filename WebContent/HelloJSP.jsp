<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Hello, JSP!</h1>
	<form method="post" action="HelloServlet">
	    <input type="text" name="name"><br><br>

	    <input type="checkbox" name="vehicle" value="車">車<br>
	    <input type="checkbox" name="vehicle" value="バイク">バイク<br>
	    <input type="checkbox" name="vehicle" value="飛行機">飛行機<br><br>

	    <input type="submit" value="submit">
	</form>
</body>
</html>