<%@ page language="java"
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="model.Balance"
%>
<!DOCTYPE html>
<html>
<head>
<style>
body {
	text-align: center;
	margin-top: 150px;
}
h1 {
	color: orange;
}
.individual-balance {
	display: flex;
	flex-direction: row;
	justify-content: space-around;
	margin-top: 50px;
}
</style>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<% Balance balance = (Balance)session.getAttribute("balance"); %>
	<h1>残高</h1>
	<p>家庭内全体</p><a href="${pageContext.request.contextPath}/household?id=0"><%= balance.getTotalBalance() %>円</a><BR>
	<div class="individual-balance">
		<div><p style="color: blue">夫</p><a href="">0円</a></div>
		<div><p style="color: red">妻</p><a href="">0円</a></div>
	</div>
	<%-- <%= balance.getEachBalancePrice(0) %>
	<a href="${pageContext.request.contextPath}/household?id=1"><%= balance.getEachBalanceName(0) %></a><BR>
	<%= balance.getEachBalancePrice(1) %>
	<a href="${pageContext.request.contextPath}/household?id=2"><%= balance.getEachBalanceName(1)%></a><BR> --%>
</body>
</html>