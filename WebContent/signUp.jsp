<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Money Forward with Family</title>
</head>
<body>
	<h1>サインアップ</h1>
	<% String message = (String)request.getAttribute("message"); %>
	<span style="color: red"><%= message == null ? "" : message %></span>
	<form name="signUpForm" action="signUp" method="post">
		<p>メールアドレス<input type="text" name="email"></p>
		<p>パスワード<input type="text" name="password"></p>
		<p>確認用パスワード<input type="text" name="confirmation"></p>
		<p><input type="submit" value="submit"></p>
	</form>
</body>
</html>