<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Money Forward with Family</h1>
	<h2>ログイン</h2>
	<% String message = (String)request.getAttribute("message"); %>
	<span style="color: red"><%= message == null ? "" : message %></span>
	<form name="loginForm" action="login" method="post">
		<p>メールアドレス<input type="text" name="email"></p>
		<p>パスワード<input type="password" name="password"></p>
		<p><input type="submit" value="submit"></p>
	</form>

	<a href="/MFwithFamily/signUp">新規登録</a>
</body>
</html>