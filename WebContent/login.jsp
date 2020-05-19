<%@ page language="java"
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
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
h2 {

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
	<!-- <div style="width: 100%;">
		<svg width="188" height="189" viewBox="0 0 188 189" fill="none" xmlns="http://www.w3.org/2000/svg">
			<ellipse cx="94" cy="94.5" rx="94" ry="94.5" fill="#E88224"/>
			<path d="M75.7282 29.2531L85.8381 23.4162C92.7733 19.4121 101.641 21.7883 105.645 28.7236L142.988 93.403C146.992 100.338 144.616 109.206 137.681 113.21L127.571 119.047C120.636 123.051 111.768 120.675 107.763 113.74L70.4208 49.0605C66.4167 42.1253 68.7929 33.2572 75.7282 29.2531Z" fill="white" stroke="black"/>
			<path d="M26.3026 114.645L21.9331 107.077C17.929 100.142 20.3052 91.2739 27.2404 87.2698L98.9686 45.8575C105.904 41.8535 114.772 44.2297 118.776 51.1649L123.146 58.7332C127.15 65.6685 124.773 74.5365 117.838 78.5406L46.11 119.953C39.1748 123.957 30.3067 121.581 26.3026 114.645Z" fill="white" stroke="black"/>
			<path d="M50.6232 73.7698L61.0155 67.7698C67.9507 63.7657 76.8188 66.1419 80.8229 73.0772L118.823 138.895C122.827 145.83 120.451 154.698 113.515 158.702L103.123 164.702C96.188 168.707 87.3199 166.33 83.3158 159.395L45.3158 93.5772C41.3118 86.6419 43.688 77.7739 50.6232 73.7698Z" fill="white" stroke="black"/>
			<path d="M69.891 136.511L65.5215 128.943C61.5174 122.008 63.8936 113.14 70.8288 109.136L142.557 67.7235C149.492 63.7194 158.36 66.0956 162.364 73.0308L166.734 80.5991C170.738 87.5344 168.362 96.4024 161.427 100.406L89.6984 141.819C82.7632 145.823 73.8951 143.447 69.891 136.511Z" fill="white" stroke="black"/>
			<path d="M130.995 72.9778L143.495 94.6284C147.499 101.564 145.123 110.432 138.187 114.436L128.228 120.186C121.293 124.19 112.425 121.814 108.421 114.878L95.8732 93.1453C95.7614 92.9517 95.8277 92.7042 96.0213 92.5924L130.312 72.7948C130.551 72.6567 130.857 72.7387 130.995 72.9778Z" fill="white" stroke="black"/>
			<rect x="95.7563" y="91.9297" width="39.4742" height="2.81682" transform="rotate(-30 95.7563 91.9297)" fill="white"/>
		</svg>
	</div> -->
	<h2 style="color: orange;">ログイン</h2>
	<span style="color: red"><%= message == null ? "" : message %></span>
	<form action="login" method="post">
	  <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
	    <input class="mdl-textfield__input" type="text" id="email" name="email">
	    <label class="mdl-textfield__label" for="sample3">メールアドレス</label>
	  </div>
	  <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
	    <input class="mdl-textfield__input" type="password" id="password" name="password">
	    <label class="mdl-textfield__label" for="sample3">パスワード  </label>
	  </div>
	  <input id="submit-button" class="mdl-button mdl-js-button mdl-js-ripple-effect button" type="submit" value="ログイン">
	</form>
	<a href="${pageContext.request.contextPath}/signUp" class="new-accunt">新規登録</a>
	<script>
		const submitButton = document.getElementById("submit-button");
		const progressBar = document.getElementById("progress-bar");
		submitButton.addEventListener('click', event => {
			progressBar.style.cssText = "display: block;";
		});
	</script>
</body>
</html>