<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="model.Analytics"
    import="java.util.ArrayList"
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	ArrayList<Analytics> analyticsList = (ArrayList<Analytics>)session.getAttribute("analyticsList");
	int id = (int)session.getAttribute("id");
	%>
	<a href="${pageContext.request.contextPath}/balance">戻る</a>
	<h1>収支内訳</h1>
	<a href="${pageContext.request.contextPath}/household?id=<%= id %>">家計簿</a><BR>
	<h4>支出</h4>
	<table border="1">
	<%
	for (Analytics analytics: analyticsList) {
		if (id == 0) {
			if (analytics.getPayer().equals("合計")) {
				out.println("<tr><th>" + analytics.getLargeItem() + "</th><th>" + analytics.getPrice() + "</tr>");
			} else {
				out.println("<tr><td>" + analytics.getPayer() + "</td><td>" + analytics.getPrice() + "</td></tr>");
			}
		} else {
			out.println("<tr><th>" + analytics.getLargeItem() + "</th><td>" + analytics.getPrice() + "</tr>");
		}
	}
	%>
	</table>
</body>
</html>