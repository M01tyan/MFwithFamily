<%@ page language="java"
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">

body {
	text-align: center;
	margin-top: 150px;
}
.background {
}

h1 {
	color: orange;
}

.cp_iptxt {
	position: relative;
	width: 50%;
	margin: 20px auto;
}
.cp_iptxt input[type=text], .cp_iptxt input[type=password] {
	font: 15px/24px sans-serif;
	box-sizing: border-box;
	width: 100%;
	margin: 8px 0;
	padding: 0.3em;
	transition: 0.3s;
	border: 1px solid #1b2538;
	border-radius: 4px;
	outline: none;
}
.cp_iptxt input[type=text]:focus, .cp_iptxt input[type=password]:focus {
	border-color: #da3c41;
}
.cp_iptxt input[type=text], .cp_iptxt input[type=password] {
	padding-left: 40px;
}
.cp_iptxt i {
	position: absolute;
	top: 8px;
	left: 0;
	padding: 9px 8px;
	transition: 0.3s;
	color: #aaaaaa;
}
.cp_iptxt input[type=text]:focus + i {
	color: #da3c41;
}
.button {
	padding: 20px 40px;
	font-size: 1.2em;
	border-style: none;
	border: double 1px black;
	color: orange;
	margin-bottom: 20px;
	background-color: white;
}
input[type="submit"]:hover {
	background-color: orange;
	color: white;
}
.new-accunt {
	color: orange;
}
</style>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="background">
	<h1>Money Forward with Family</h1>
	<h2>ログイン</h2>
	<% String message = (String)request.getAttribute("message"); %>
	<span style="color: red"><%= message == null ? "" : message %></span>
	<form name="loginForm" action="login" method="post">
		<div class="cp_iptxt">
			<input type="text" placeholder="メールアドレス" name="email">
			<i class="fa fa-user fa-lg fa-fw" aria-hidden="true"></i>
		</div>
		<div class="cp_iptxt">
			<input type="password" placeholder="パスワード" name="password">
			<i class="fa fa-user fa-lg fa-fw" aria-hidden="true"></i>
		</div>
		<div class="button_wrapper"><input class="button" type="submit" value="LOGIN"></div>
	</form>

	<a href="/MFwithFamily/signUp" class="new-accunt">新規登録</a>
</div>
</body>
</html>