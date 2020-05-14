<%@ page language="java"
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="model.User"
    import="java.util.*"
    import="Component.Color"
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
	background-color: #F3F3F3;
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
::-moz-selection {
  background-color: #6ab344;
  color: #fff;
}
::selection {
  background-color: #6ab344;
  color: #fff;
}
.android-search-box .mdl-textfield__input {
  color: rgba(0, 0, 0, 0.87);
}
.android-header .mdl-menu__container {
  z-index: 50;
  margin: 0 !important;
}
.android-header {
  overflow: visible;
  background-color: white;
}
.android-header .material-icons {
    color: #767777 !important;
}
.android-header .mdl-layout__drawer-button {
    background: transparent;
    color: #767777;
}
.android-header .mdl-navigation__link {
    color: #757575;
    font-weight: 700;
    font-size: 14px;
}
.android-navigation-container {
    /* Simple hack to make the overflow happen to the left instead... */
    direction: rtl;
    -webkit-order: 1;
        -ms-flex-order: 1;
            order: 1;
    width: 500px;
    transition: opacity 0.2s cubic-bezier(0.4, 0, 0.2, 1),
        width 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}
.android-navigation {
    /* ... and now make sure the content is actually LTR */
    direction: ltr;
    -webkit-justify-content: flex-end;
        -ms-flex-pack: end;
            justify-content: flex-end;
    width: 800px;
}
.android-search-box.is-focused + .android-navigation-container {
    opacity: 0;
    width: 100px;
}
.android-navigation .mdl-navigation__link {
    display: inline-block;
    height: 60px;
    line-height: 68px;
    background-color: transparent !important;
    border-bottom: 4px solid transparent;
}
.android-navigation .mdl-navigation__link:hover {
      border-bottom: 4px solid orange;
}
.android-search-box {
    -webkit-order: 2;
        -ms-flex-order: 2;
            order: 2;
    margin-left: 16px;
    margin-right: 16px;
}
.android-more-button {
    -webkit-order: 3;
        -ms-flex-order: 3;
            order: 3;
}
.android-drawer {
  border-right: none;
}
.android-drawer-separator {
    height: 1px;
    background-color: #dcdcdc;
    margin: 8px 0;
}
.android-drawer .mdl-navigation__link.mdl-navigation__link {
    font-size: 14px;
    color: #757575;
}
.android-drawer span.mdl-navigation__link.mdl-navigation__link {
    color: #8bc34a;
}
.android-drawer .mdl-layout-title {
    position: relative;
    background: #6ab344;
    height: 160px;
}
.android-drawer .android-logo-image {
      position: absolute;
      bottom: 16px;
}
.android-be-together-section {
  position: relative;
  height: 800px;
  width: auto;
  background-color: #f3f3f3;
  background: url('images/slide01.jpg') center 30% no-repeat;
  background-size: cover;
}
#progress-bar {
	width: 100%;
	display: none;
}
.wallet {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}
.wallet-price {
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translateY(-50%) translateX(-50%);
	-webkit- transform: translateY(-50%) translateX(-50%);
	margin: auto;
	font-size: 20px;
}
</style>
</head>
<body>
	<div id="progress-bar" class="mdl-progress mdl-js-progress mdl-progress__indeterminate"></div>
	<div class="android-header mdl-layout__header mdl-layout__header--waterfall">
		<div class="mdl-layout__header-row">
			<span class="android-title mdl-layout-title">
				<p style="font-size: 24px; font-weight: bold; margin-top: 20px">Money Forward <span style="color: orange;">with Family</span></p>
          	</span>
          	<!-- Add spacer, to align navigation to the right in desktop -->
          	<div class="android-header-spacer mdl-layout-spacer"></div>
          	<!-- Navigation -->
          	<div class="android-navigation-container">
            	<nav class="android-navigation mdl-navigation">
              		<a class="mdl-navigation__link mdl-typography--text-uppercase" href="${pageContext.request.contextPath}/balance">残高</a>
              		<a class="mdl-navigation__link mdl-typography--text-uppercase" href="${pageContext.request.contextPath}/share">家族追加</a>
		           	<a class="mdl-navigation__link mdl-typography--text-uppercase" href="${pageContext.request.contextPath}/financial?id=1">連携口座</a>
		          	<a class="mdl-navigation__link mdl-typography--text-uppercase" href="">マイメニュー</a>
		        	<a class="mdl-navigation__link mdl-typography--text-uppercase" href="${pageContext.request.contextPath}/balance?mode=logout">ログアウト</a>
            	</nav>
          	</div>
		</div>
	</div>
	<%
		List<User> userList = (List<User>)session.getAttribute("userList");
		User user = (User)session.getAttribute("user");
		List<Color> colors = new ArrayList(Arrays.asList(Color.values()));
		Collections.shuffle(colors);
	%>
	<h2 style="color: orange; margin-top: 80px;">残高</h2>
	<div class="wallet">
		<a href="${pageContext.request.contextPath}/financial?id=0">
			<div style="position: relative;">
				<svg width="335" height="295" viewBox="0 0 335 295" fill="none" xmlns="http://www.w3.org/2000/svg">
					<path d="M329.212 164.5C358.368 254 260.287 295 169.712 295C79.1378 295 -25.7875 245.5 5.71244 164.5C33.7124 92.5 79.1378 34 169.712 34C260.287 34 307.712 98.5 329.212 164.5Z" fill="#FAB97D"/>
					<circle cx="149.712" cy="18" r="18" fill="#FAB97D"/>
					<circle cx="185.712" cy="18" r="18" fill="#FAB97D"/>
				</svg>
				<p class="wallet-price" style="font-size: 40px; color: white">¥<%= userList.get(0).getBalance() %></p>
			</div>
			<h3>全体</h3>
		</a>
		<div class="mdl-grid">
			<%
				for (int i=1; i<userList.size(); i++) {
					String name = userList.get(i).getName();
					if (name.equals(user.getName())) name = "あなた";
					String color = colors.get(i).getThinColor();
			%>
			<a href="${pageContext.request.contextPath}/financial?id=<%= i %>" class="mdl-cell mdl-cell--4-col" style="position: relative; width: 154px;">
			  	<svg width="154" height="136" viewBox="0 0 154 136" fill="none" xmlns="http://www.w3.org/2000/svg">
					<path d="M151.59 75.8373C165.015 117.098 119.852 136 78.1462 136C36.4399 136 -11.8742 113.18 2.63036 75.8373C15.5233 42.6441 36.4399 15.6746 78.1462 15.6746C119.852 15.6746 141.69 45.4102 151.59 75.8373Z" fill="<%=color%>"/>
					<ellipse cx="68.9369" cy="8.29831" rx="8.28832" ry="8.29831" fill="<%=color%>"/>
					<ellipse cx="85.5135" cy="8.29831" rx="8.28832" ry="8.29831" fill="<%=color%>"/>
				</svg>
				<p class="wallet-price" id="price">¥<%= userList.get(i).getBalance() %></p>
				<p><%= name %></p>
			</a>
			<% } %>
		</div>
	</div>
	<script>
		const navigationButton = document.querySelectorAll("a");
		const progressBar = document.getElementById("progress-bar");
		navigationButton.forEach(button => {
			button.addEventListener('click', event => {
				progressBar.style.cssText = "display: block;";
			});
		});
	</script>
</body>
</html>