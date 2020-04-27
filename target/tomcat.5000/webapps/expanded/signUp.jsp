<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
body {
	text-align: center;
	position: relative;
	margin-top: 150px;
}
h1 {
	color: orange;
}
a {
	position: absolute;
	left: 80px;
	top: 10px;
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
.note-txt {
	font-size: 13px;
	paddin-left: 0px;
	color: grey;
}
input[type="submit"]:hover {
	background-color: orange;
	color: white;
}
</style>
<meta charset="UTF-8">
<title>Money Forward with Family</title>
</head>
<body>
	<h1>サインアップ</h1>
	<% String message = (String)request.getAttribute("message"); %>
	<span style="color: red"><%= message == null ? "" : message %></span>
	<a href="${pageContext.request.contextPath}/">&lt; 戻る</a>
	<form name="signUpForm" action="signUp" method="post">
		<div class="cp_iptxt">
			<input type="text" placeholder="メールアドレス" name="email">
			<i class="fa fa-user fa-lg fa-fw" aria-hidden="true"></i>
		</div>
		<p class="note-txt">パスワードは半角英数字で6~20文字で入力してください</p>
		<div class="cp_iptxt">
			<input type="password" placeholder="パスワード" name="password">
			<i class="fa fa-user fa-lg fa-fw" aria-hidden="true"></i>
		</div>
		<div class="cp_iptxt">
			<input type="password" placeholder="パスワード確認用" name="confirmation">
			<i class="fa fa-user fa-lg fa-fw" aria-hidden="true"></i>
		</div>
		<div class="button_wrapper"><input class="button" type="submit" value="SIGNUP"></div>
	</form>
</body>
</html>