<%@ page language="java"
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
body {
	text-align: center;
	margin-top: 150px;
}
.cp_iptxt {
	position: relative;
	width: 50%;
	margin: 20px auto;
}
.cp_iptxt input[type=number] {
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
.cp_iptxt input[type=number]:focus {
	border-color: #da3c41;
}
.cp_iptxt input[type=number] {
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
.cp_iptxt input[type=number]:focus + i {
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
</style>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<% String message = (String)request.getAttribute("message"); %>
	<span style="color: red"><%= message == null ? "" : message %></span>
	<form name="authCodeForm" action="auth" method="post">
		<h2>メールを送信しました！</h2>
		<p>メールに記載された認証コードを入力してください</p>
		<div class="cp_iptxt">
			<input type="number" placeholder="4ケタの認証コード" name="authCode">
			<i class="fa fa-user fa-lg fa-fw" aria-hidden="true"></i>
		</div>
		<div class="button_wrapper"><input class="button" type="submit" value="SUBMIT"></div>
	</form>
</body>
</html>