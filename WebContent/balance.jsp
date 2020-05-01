<%@ page language="java"
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="model.User"
    import="java.util.*"
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
	<%
		List<User> userList = (List<User>)request.getAttribute("userList");
		/* User user = (User)application.getAttribute("user"); */
	%>
	<a href="${pageContext.request.contextPath}/balance?mode=logout">ログアウト</a>
	<h1>残高</h1>
	<%
		for (User user : userList) {
			String name = user.getName();
			if (name == null) name = "ユーザー";
			out.println(name + " : " + user.getBalance() + "<BR>");
		}
	%>
</body>
</html>