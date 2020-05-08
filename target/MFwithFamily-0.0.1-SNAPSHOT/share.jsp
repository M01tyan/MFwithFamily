<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="model.User"
    import="model.Family"
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	User user = (User) application.getAttribute("user");
	Family family = (Family) application.getAttribute("family");
	%>
	<a href="${pageContext.request.contextPath}/balance">戻る</a>
	<h1>家族連携</h1>
	<%-- <% if (familyId == -1) { %> --%>
	<form name="createShareCode" method="get" action="share">
		<input type="submit" value="createShareCode" name="createShareCode"><BR>
	</form>
	<%= family.getShareCode() %>
	<%-- <% } else { %>
		<p><% out.println(shareCode != null ? shareCode : "まだ共有コードを発行していません");%></p>
	<% } %> --%>
	<form name="inputShareCode" method="post" action="share">
		<input type="text" name="inputShareCode" />
		<input type="submit" value="送信" />
	</form>
</body>
</html>