<%@ page language="java"
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="model.Balance"
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		Balance balance = (Balance)session.getAttribute("balance");
	%>
	<%= balance.getTotalBalance() + " total" + "<BR>" %>
	<%= balance.getEachBalancePrice(0) %>
	<%= balance.getEachBalanceName(0) + "<BR>"%>
	<%= balance.getEachBalancePrice(1) %>
	<%= balance.getEachBalanceName(1) + "<BR>"%>
	<a href="/MFwithFamily/list">家計簿</a>
</body>
</html>