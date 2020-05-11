<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="model.User"
    import="model.Family"
    import="java.util.*"
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<title>Money Forward with Family</title>
</head>
<body>
	<%
	User user = (User) session.getAttribute("user");
	Family family = (Family) session.getAttribute("family");
	%>
	<a href="${pageContext.request.contextPath}/balance">戻る</a>

	<h1>家族連携</h1>
	<% if (family.getId() == -1) { %>
	<form name="createShareCode" method="get" action="share">
		<input type="submit" value="createShareCode" name="createShareCode"><BR>
	</form>
	<% } else { %>
	<p>共有コード</p>
	<%= family.getShareCode() %>
	<% } %>

	<p>共有コードの入力</p>
	<% String message = (String)request.getAttribute("message"); %>
	<span style="color: red"><%= message == null ? "" : message %></span>
	<form name="inputShareCode" method="post" action="share">
		<input type="text" name="inputShareCode" />
		<input type="submit" value="送信" />
	</form>

	<p>家族一覧</p>
	<% List<User> userList = family.getUserList(); %>
	<% for (int i=0; i<userList.size(); i++) { %>
	<div style="display: flex; flex-direction: row">
		<% if (userList.get(i).getId()==user.getId()) { %>
		<p>あなた</p>
		<% } %>
		<p><%= userList.get(i).getName() %></p>
		<button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" id="${userList.get(i).getId()}">
	  		連携解除
	  	</button>

	</div>
	<% } %>
	<script>
		var buttons = document.querySelectorAll('button');
		buttons.forEach(button => {
			button.addEventListener('click', event => {
				index = [].slice.call(buttons).indexOf(button);
				$.ajax({
					type    : "DELETE",
				    url     : location.href + "?id="+index,
				    async   : true,
				    success : function(data) {
				    	console.log("SUCCESS!");
				    	alert("削除に成功しました！");
				    	location.reload();
				    },
				    error : function(XMLHttpRequest, textStatus, errorThrown) {
				      alert("リクエスト時になんらかのエラーが発生しました：" + textStatus +":\n" + errorThrown);
				    }
				  });
			});
		});
	</script>
</body>
</html>