<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.orange-pink.min.css" />
<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
<meta charset="UTF-8">
<title>Money Forward with Family</title>
<style>
body {
	text-align: center;
}
h1 {
	margin-top: 0;
	font-weight: bold;
}
form {
	display: flex;
	flex-direction: column;
	justicy-content: center;
	align-items: center;
}
.button {
	color: orange;
    background-color: white;
    border: solid 1px;
    border-radius: 10px;
    margin-bottom: 20px;
}
.button:hover {
	color: white;
	background-color: orange;
}
a {
	position: absolute;
	top: 170px;
	left: 50px;
}
#progress-bar {
	width: 100%;
	display: none;
}
</style>
</head>
<body>
	<div id="progress-bar" class="mdl-progress mdl-js-progress mdl-progress__indeterminate"></div>
	<h2 style="padding-top: 150px;">「お金をつなぐ。繋がりを結ぶ。」</h2>
	<h1>Money Forward <span style="color: orange;">with Family</span></h1>
	<% String message = (String)request.getAttribute("message"); %>
	<h2 style="color: orange;">新規登録</h2>
	<a href="${pageContext.request.contextPath}/">&lt; 戻る</a>
	<span style="color: red"><%= message == null ? "" : message %></span>
	<form action="signUp" method="post">
	  <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
	    <input class="mdl-textfield__input" type="text" id="email" name="email">
	    <label class="mdl-textfield__label" for="sample3">メールアドレス</label>
	  </div>
	  <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
	    <input class="mdl-textfield__input" type="password" id="password" name="password">
	    <label class="mdl-textfield__label" for="sample3">パスワード  </label>
	  </div>
	  <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
	    <input class="mdl-textfield__input" type="password" id="confirmation" name="confirmation">
	    <label class="mdl-textfield__label" for="sample3">パスワード確認用</label>
	  </div>
	  <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
	    <input class="mdl-textfield__input" type="text" id="name" name="name">
	    <label class="mdl-textfield__label" for="sample3">名前　　　　　　</label>
	  </div>
	  <input class="mdl-button mdl-js-button mdl-js-ripple-effect button" type="submit" value="新規登録" id="submit-button">
	</form>
	<script>
		const submitButton = document.getElementById("submit-button");
		const progressBar = document.getElementById("progress-bar");
		submitButton.addEventListener('click', event => {
			progressBar.style.cssText = "display: block;";
		});
	</script>
</body>
</html>