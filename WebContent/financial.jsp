<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="model.Financial" import="model.User"
	import="java.util.*" import="Component.SendMail"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet"
	href="https://code.getmdl.io/1.3.0/material.orange-pink.min.css" />
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<link rel="stylesheet" type="text/css" href="dist/dialog-polyfill.css" />
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

.input-household {
	width: 80%;
	height: 230px;
	background-color: white;
	margin: auto;
}

table {
	margin: auto;
}

.mdl-data-table th {
	text-align: center;
}

button:focus {
	outline: 0;
}

.financial-container {
	width: 81%;
	text-align: center;
}

.financial {
	background-color: #6AB8FF;
	border-radius: 10px;
	position: relative;
	min-height: 100px;
	max-width: 322px;
}

.financial__balance {
	color: #FFFFFF;
	font-size: 35px;
	position: absolute;
	bottom: 0px;
	right: 30px;
}

.financial__name {
	color: #FFFFFF;
	font-size: 25px;
	position: absolute;
	top: 15px;
	left: 20px;
}

.financial__user {
	position: absolute;
	bottom: 0px;
	left: 20px;
	color: #FFFFFF;
}

.delete-button {
	position: absolute;
	right: 10px;
	top: 5px;
	font-size: 15px;
	background: grey;
	color: white;
	transform: rotate(-45deg);
}

.delete-button:hover {
	background: red;
}

.financial-form {
	width: 322px;
	height: 200px;
	background: white;
	margin: auto;
	border: solid 1px;
	border-radius: 10px;
	padding: 20px;
}
</style>
</head>
<body>
	<!-- プログレスバー -->
	<div id="progress-bar" class="mdl-progress mdl-js-progress mdl-progress__indeterminate"></div>
	<!-- Header -->
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
					<a class="mdl-navigation__link mdl-typography--text-uppercase" href="${pageContext.request.contextPath}/balance">残高</a>
					<a class="mdl-navigation__link mdl-typography--text-uppercase" href="${pageContext.request.contextPath}/share">家族追加</a>
					<a class="mdl-navigation__link mdl-typography--text-uppercase" href="${pageContext.request.contextPath}/household">家計簿</a>
					<a class="mdl-navigation__link mdl-typography--text-uppercase" href="${pageContext.request.contextPath}/balance?mode=logout">ログアウト</a>
				</nav>
			</div>
		</div>
	</div>
	<!-- 戻るボタン&Title -->
	<a href="${pageContext.request.contextPath}/balance"
		style="position: absolute; left: 50px; top: 80px;" id="back-button">&lt;
		戻る</a>
	<h3 style="color: orange;">連携口座</h3>
	<%
	List<Financial> financialList = (List<Financial>) session.getAttribute("financialList");
	User user = (User) session.getAttribute("user");
	List<User> userList = (List<User>) session.getAttribute("userList");
	int id = Integer.parseInt(request.getParameter("id"));
	%>
	<!-- 公開口座一覧 -->
	<%
	String userName = financialList.get(0).getUserName();
	List<Financial> personFinancial = new ArrayList<Financial>();
	for (Financial financial : financialList) {
		if (financial.getPublish()) {
	%>
	<%
			if (!userName.equals(financial.getUserName())) {
	%>
	<p style="width: 80%; text-align: left; margin: 0 auto; font-size: 20px;"><%= userName %></p>
	<div class="mdl-grid financial-container">
		<%
		for (Financial v : personFinancial) {
		%>
		<div class="mdl-cell mdl-cell--4-col financial">
			<p class="financial__balance">
				¥<%=SendMail.comma(v.getBalance())%></p>
			<p class="financial__name"><%=v.getFinancialName()%></p>
			<%
				if (user.getId() == v.getUid()) {
			%>
			<button
				class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab delete-button">
				<i class="material-icons">add</i>
			</button>
			<% } %>
			<p class="financial__user"><%=v.getUserName()%></p>
		</div>
		<% } %>
	</div>
	<%
		personFinancial.clear();
		personFinancial.add(financial);
	} else {
		personFinancial.add(financial);
	}
	%>
	<% 	userName = financial.getUserName();
		}
	}%>
	<% if (personFinancial.size() != 0) { %>
	<p style="width: 80%; text-align: left; margin: 0 auto; font-size: 20px;"><%= userName %></p>
	<div class="mdl-grid financial-container">
	<% } %>
	<% for (Financial v : personFinancial) { %>
		<div class="mdl-cell mdl-cell--4-col financial">
			<p class="financial__balance">
				¥<%=SendMail.comma(v.getBalance())%></p>
			<p class="financial__name"><%=v.getFinancialName()%></p>
			<%
				if (user.getId() == v.getUid()) {
			%>
			<button
				class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab delete-button">
				<i class="material-icons">add</i>
			</button>
			<% } %>
			<p class="financial__user"><%=v.getUserName()%></p>
		</div>
	<% } %>
	<% if (personFinancial.size() != 0) { %>
	</div>
	<% } %>
	<!-- へそくり口座一覧 -->
	<% if (id == 0 || userList.get(id).getId() == user.getId()) { %>
	<p style="width: 80%; text-align: left; margin: 0 auto; font-size: 20px;">非公開口座</p>
	<% } %>
	<div class="mdl-grid financial-container">
		<%
		for (Financial financial : financialList) {
			if (!financial.getPublish() && financial.getUid() == user.getId()) {
		%>
		<div class="mdl-cell mdl-cell--4-col financial" style="background: #FE748D">
			<p class="financial__balance">
				¥<%=financial.getBalance()%></p>
			<p class="financial__name"><%=financial.getFinancialName()%></p>
			<%
				if (user.getId() == financial.getUid()) {
			%>
			<button
				class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab delete-button">
				<i class="material-icons">add</i>
			</button>
			<%
				}
			%>
			<p class="financial__user"><%=financial.getUserName()%></p>
		</div>
		<%	}
		}
		%>
	</div>
	<%
		if (user.getId() == financialList.get(0).getUid() || id == 0) {
	%>
	<!-- 口座追加フォーム -->
	<div class="financial-form">
		<form action="financial" method="post" id="form">
			<input type="hidden" name="id"
				value="<%=request.getParameter("id")%>">
			<div
				class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
				<input class="mdl-textfield__input" type="text" id="name"
					name="name" required> <label class="mdl-textfield__label" for="name">口座名</label>
			</div>
			<div
				class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
				<div style="display: flex; flex-direction: row;">
					<p style="font-size: 30px; margin: auto;">¥</p>
					<input class="mdl-textfield__input" type="number" id="balance"
						name="balance" style="text-align: right;" maxlength='10' required> <label
						class="mdl-textfield__label" for="balance"
						style="padding-left: 20px; width: 95%;">残高</label>
				</div>
			</div>
			<label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect" for="secret" style="width: auto; margin-bottom: 10px;">
			<input type="checkbox" id="secret" class="mdl-checkbox__input" name="publish" value="true" checked>
			<span class="mdl-checkbox__label">家族全員に公開します</span>
			</label><BR> <input
				class="mdl-button mdl-js-button mdl-js-ripple-effect button"
				type="submit" value="送信" id="submit-button" style="margin: auto;">
		</form>
	</div>
	<%
		}
	%>

	<!-- 削除ダイアログ -->
	<dialog class="mdl-dialog" style="width: 50%;">
	<h4 class="mdl-dialog__title">この口座を削除しますか？</h4>
	<div class="mdl-dialog__content">
		<p>一度削除すると復元することはできません。</p>
	</div>
	<div class="mdl-dialog__actions">
		<button type="button" class="mdl-button" id="yes-button">はい</button>
		<button type="button" class="mdl-button close">いいえ</button>
	</div>
	</dialog>

	<script>
		//戻るボタンを押した時にローディング表示
		const navigationButton = document.querySelectorAll("a");
		const progressBar = document.getElementById("progress-bar");
		navigationButton.forEach(button => {
			button.addEventListener('click', event => {
				progressBar.style.cssText = "display: block;";
			});
		});

		//submit時のローディング表示
		const form = document.getElementById("form");
		form.addEventListener('submit', () => {
			const name = document.getElementById("name").value;
			const balance = document.getElementById("balance").value;
			if (balance.length > 10) {
				event.preventDefault();
			    alert("残高の金額が大きすぎます。残高は10億円以内に設定してください。");
			} else if (name.length > 20) {
				event.preventDefault();
				alert("口座の名前が長すぎます。口座名は20文字以内で設定してください。");
			} else {
				progressBar.style.cssText = "display: block;";
			}
		});

		//公開チャックボックスを押した時の処理
		const checkBox = document.getElementById("secret");
		let publish = true;
		checkBox.addEventListener('click', event => {
			publish = !publish;
			checkBox.value = publish;
		});

		//ダイアログ
		const dialog = document.querySelector('dialog');
	    const deleteButtons = document.getElementsByClassName("delete-button");
	    if (!dialog.showModal) {
	      dialogPolyfill.registerDialog(dialog);
	    }
	    Array.from(deleteButtons).forEach(button => {
	    	button.addEventListener('click', () => {
	    		index = [].slice.call(deleteButtons).indexOf(button);
		      	dialog.showModal();
		      	//はいボタンを押した時
		      	document.getElementById("yes-button").addEventListener('click', event => {
		      		progressBar.style.cssText = "display: block;";
		      		const path = window.location.href.split("?");
		      		$.ajax({
						type    : "DELETE",
					    url     : path[0]+'?index='+index,
					    async   : true,
					    success : function(data) {
					    	console.log("SUCCESS!");
					    	alert("送信に成功しました！");
					    	location.reload();
					    	progressBar.style.cssText = "display: block;";
					    },
					    error : function(XMLHttpRequest, textStatus, errorThrown) {
					      alert("リクエスト時になんらかのエラーが発生しました：" + textStatus +":\n" + errorThrown);
					    }
					});
		      	});
		    });
	    });
	    dialog.querySelector('.close').addEventListener('click', () => {
	      dialog.close();
	    });
	</script>
</body>
</html>