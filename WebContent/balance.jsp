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
	<% Balance balance = (Balance)session.getAttribute("balance"); %>
	<%= balance.getTotalBalance() %> <a href="/MFwithFamily/household?id=0">total</a><BR>
	<%-- <%= balance.getEachBalancePrice(0) %>
	<a href="/MFwithFamily/household?id=1"><%= balance.getEachBalanceName(0) %></a><BR>
	<%= balance.getEachBalancePrice(1) %>
	<a href="/MFwithFamily/household?id=2"><%= balance.getEachBalanceName(1)%></a><BR> --%>
</body>
</html>