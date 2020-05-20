<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="model.User"
    import="model.Family"
    import="java.util.*"
    import="Component.Color"
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet"
	href="https://code.getmdl.io/1.3.0/material.orange-pink.min.css" />
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
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

.material-icons {
	font-family: 'Material Icons';
	font-weight: normal;
	font-style: normal;
	font-size: 24px; /* Preferred icon size */
	display: inline-block;
	line-height: 1;
	text-transform: none;
	letter-spacing: normal;
	word-wrap: normal;
	white-space: nowrap;
	direction: ltr;
	/* Support for all WebKit browsers. */
	-webkit-font-smoothing: antialiased;
	/* Support for Safari and Chrome. */
	text-rendering: optimizeLegibility;
	/* Support for Firefox. */
	-moz-osx-font-smoothing: grayscale;
	/* Support for IE. */
	font-feature-settings: 'liga';
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
	transition: opacity 0.2s cubic-bezier(0.4, 0, 0.2, 1), width 0.2s
		cubic-bezier(0.4, 0, 0.2, 1);
}

.android-navigation {
	/* ... and now make sure the content is actually LTR */
	direction: ltr;
	-webkit-justify-content: flex-end;
	-ms-flex-pack: end;
	justify-content: flex-end;
	width: 800px;
}

.android-search-box.is-focused+.android-navigation-container {
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

.share-code__input {
	display: flex;
	flex-direction: row;
	justify-content: center;
	padding-left: 90px;
}

.user-chip {
	position: absolute;
    top: 200px;
}

.family-list {
	display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    transform: scale(2, 2);
    margin: auto;
    width: auto;
    height: 100px;
}
</style>
</head>
<body>
	<div id="progress-bar"
		class="mdl-progress mdl-js-progress mdl-progress__indeterminate"></div>
	<div
		class="android-header mdl-layout__header--waterfall">
		<div class="mdl-layout__header-row" style="padding: 0 0 0 5px;">
			<span class="android-title mdl-layout-title">
				<p style="font-size: 24px; font-weight: bold; margin-top: 20px">
					Money Forward <span style="color: orange;">with Family</span>
				</p>
			</span>
			<!-- Add spacer, to align navigation to the right in desktop -->
			<div class="android-header-spacer mdl-layout-spacer"></div>
			<!-- Navigation -->
			<div class="android-navigation-container">
				<nav class="android-navigation mdl-navigation">
					<a class="mdl-navigation__link mdl-typography--text-uppercase"
						href="${pageContext.request.contextPath}/balance">残高</a> <a
						class="mdl-navigation__link mdl-typography--text-uppercase"
						href="${pageContext.request.contextPath}/share">家族追加</a> <a
						class="mdl-navigation__link mdl-typography--text-uppercase"
						href="${pageContext.request.contextPath}/household">家計簿</a> <a
						class="mdl-navigation__link mdl-typography--text-uppercase"
						href="${pageContext.request.contextPath}/balance?mode=logout">ログアウト</a>
				</nav>
			</div>
		</div>
	</div>
	<a href="${pageContext.request.contextPath}/balance"
		style="position: absolute; left: 50px; top: 80px;" id="back-button">&lt;
		戻る</a>
	<h3 style="color: orange;">家族追加</h3>
	<%
	User user = (User) session.getAttribute("user");
	Family family = (Family) session.getAttribute("family");
	%>

	<% if (family.getId() == -1) { %>
	<form name="createShareCode" method="get" action="share" style="margin-top: 72px;">
		<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--1-col" style="text-align: left;">
			<input class="mdl-button mdl-js-button mdl-js-ripple-effect button"
				type="submit" value="おうちを作る" id="submit-button" name="createShareCode" style="margin: auto;">
		</div>
	</form>
	<% String message = (String)request.getAttribute("message"); %>
	<span style="color: red"><%= message == null ? "" : message %></span>
	<p style="font-size: 20px; color: orange; margin-top: 60px;">おうちに入る</p>
	<form name="inputShareCode" action="share" method="post" class="share-code__input" id="form">
		<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
	    	<input class="mdl-textfield__input" type="text" id="share-code" name="inputShareCode" style="padding-top: 20px;" pattern="[a-zA-Z0-9]{8}">
	    	<label class="mdl-textfield__label" for="share-code">家族コードの入力</label>
		</div>
		<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--1-col" style="text-align: left; margin-top: 27px;">
			<input class="mdl-button mdl-js-button mdl-js-ripple-effect button"
				type="submit" value="送信" id="submit-button" style="margin: auto;">
		</div>
	</form>
	<% } else { %>
	<h2 style="margin-top: 77px; ">家族コード</h2>
	<p style="font-size: 40px; color: orange;"><%= family.getShareCode() %></p>
	<% } %>
	<% if (!family.getShareCode().equals("DEFAULT")) {%>
	<div style="position: relative;">
		<svg width="800" height="529" viewBox="0 0 1053 529" fill="none" xmlns="http://www.w3.org/2000/svg" style="position: relative;">
			<path d="M524.174 1.42698C525.672 0.852992 527.328 0.852991 528.826 1.42698L946.964 161.681C953.696 164.261 951.848 174.25 944.638 174.25H108.362C101.152 174.25 99.3037 164.261 106.036 161.68L524.174 1.42698Z" fill="white" stroke="black"/>
			<path d="M134.5 176C134.5 170.753 138.753 166.5 144 166.5H910C915.247 166.5 919.5 170.753 919.5 176V519C919.5 524.247 915.247 528.5 910 528.5H144C138.753 528.5 134.5 524.247 134.5 519V176Z" fill="white" stroke="black"/>
			<rect x="134" y="156" width="786" height="18" fill="white"/>
		</svg>
		<div class="family-list">
			<%
			List<User> userList = family.getUserList();
			List<Color> colors = new ArrayList(Arrays.asList(Color.values()));
			Collections.shuffle(colors);
			%>
			<% for (int i=0; i<userList.size(); i++) {
				String userName = userList.get(i).getId() == user.getId() ? "あなた" : userList.get(i).getName();
			%>
			<span class="mdl-chip mdl-chip--contact mdl-chip--deletable">
				<span class="mdl-chip__contact mdl-color-text--white" style="background: <%= colors.get(i).getDarkColor() %>;"><%= userList.get(i).getName().substring(0,1) %></span>
			    <span class="mdl-chip__text"><%= userName %></span>
			    <button id="<%= userList.get(i).getId() %>" class="mdl-chip__action"><i class="material-icons">cancel</i></button>
			</span>
			<% } %>
		</div>
	</div>
	<% } %>
	<script>
		//戻るボタンを押したときのローディング表示
		const backButton = document.getElementById("back-button");
		const progressBar = document.getElementById("progress-bar");
		backButton.addEventListener('click', event => {
			progressBar.style.cssText = "display: block;";
		});

		//submit時のローディング表示
		const form = document.getElementById("form");
		form.addEventListener('submit', () => {
			const shareCode = document.getElementById("share-code").value;
			if (share.length !== 10) {
				event.preventDefault();
			    alert("家族コードの形式が違います。");
			} else {
				progressBar.style.cssText = "display: block;";
			}
		});

		//家族を削除する
		var buttons = document.querySelectorAll('button');
		buttons.forEach(button => {
			button.addEventListener('click', event => {
				progressBar.style.cssText = "display: block;";
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