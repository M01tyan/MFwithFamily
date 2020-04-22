<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="model.Household"
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
	<a href="/MFwithFamily/balance">戻る</a>
	<h1>家計簿</h1>
	<a href="/MFwithFamily/breakdown">収支内訳</a>
	<div style="display: flex; flex-direction: column">

	<% ArrayList<Household> householdList = (ArrayList<Household>)session.getAttribute("householdList"); %>
	</div>
		<table border="1">
			<tr><th>日付</th><th>内容</th><th>値段</th><th>口座</th><th>大項目</th><th>中項目</th><th>メモ</th><th>支払者</th><th>続柄</th></tr>
			<% for(Household household : householdList) { %>
			<tr><td><%= household.getDate() %></td><td><%= household.getContent() %></td><td><%= household.getPrice() %></td><td><%= household.getFinancial() %></td><td><%= household.getLargeItem() %></td><td><%= household.getMiddleItem() %></td><td><%= household.getMemo() %></td><td><%= household.getUserName() %></td><td><%= household.getRelationshipName() %></td>
			<% } %>
		</table>
	</div>
</body>
</html>