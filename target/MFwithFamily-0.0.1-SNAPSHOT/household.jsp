<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="model.Household"
	import="java.util.*"
	import="java.util.stream.*"
	import="java.text.*"
	import="model.Financial"
	import="model.Household"
	import="model.User"
	import="Component.SendMail"
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

.input-household {
	width: 80%;
	height: 230px;
	background-color: white;
	margin: auto;
	margin-top: 73px;
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

.is-transfer {
	color: #d3d3d3;
}

</style>
</head>
<body>
	<%
	List<String> largeItemList = new ArrayList<String>(Arrays.asList("食費", "日用品", "趣味・娯楽", "交際費", "交通費", "衣服・美容", "健康・医療",
			"自動車", "教養・教育", "特別な支出", "現金・カード", "水道・光熱費", "通信費", "住宅", "税・社会保障", "保険", "その他", "未分類"));
	List<Financial> financialList = (List<Financial>) session.getAttribute("financialList");
	List<Household> householdList = (List<Household>) session.getAttribute("householdList");
	List<User> userList = (List<User>) session.getAttribute("userList");
	User user = (User) session.getAttribute("user");
	%>
	<form name="financial_list">
		<% for (Financial financial : financialList) { %>
		<% if (financial.getPublish() || financial.getUid() == user.getId() ) { %>
		<input type="hidden" name="id" value="<%= financial.getId() %>">
		<input type="hidden" name="uid" value="<%= financial.getUid() %>">
		<input type="hidden" name="name" value="<%= financial.getFinancialName() %>">
		<input type="hidden" name="userName" value="<%= financial.getUserName() %>">
		<input type="hidden" name="balance" value="<%= financial.getBalance() %>">
		<input type="hidden" name="publish" value="<%= financial.getPublish() %>">
		<% } }%>
	</form>
	<form name="user">
		<input type="hidden" name="id" value="<%= user.getId() %>">
	</form>
	<form name="household_list">
		<% for (Household household : householdList) { %>
		<input type="hidden" name="date" value="<%= household.getDate() %>">
		<input type="hidden" name="content" value="<%= household.getContent() %>">
		<input type="hidden" name="price" value="<%= household.getPrice() %>">
		<input type="hidden" name="financial" value="<%= household.getFinancial() %>">
		<input type="hidden" name="largeItem" value="<%= household.getLargeItem() %>">
		<input type="hidden" name="middleItem" value="<%= household.getMiddleItem() %>">
		<input type="hidden" name="memo" value="<%= household.getMemo() %>">
		<input type="hidden" name="transfer" value="<%= household.getTransfer() %>">
		<input type="hidden" name="id" value="<%= household.getId() %>">
		<input type="hidden" name="userName" value="<%= household.getUserName() %>">
		<% } %>
	</form>
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
	<h3 style="color: orange;">家計簿入力</h3>
	<div class="mdl-tabs mdl-js-tabs mdl-js-ripple-effect">
		<div class="mdl-tabs__tab-bar" style="border: none; position: absolute; left: 154px;">
			<% for (User u : userList) { %>
			<a href="#a" class="mdl-tabs__tab <%= u.getName() == "合計" ? "is-active" : "" %>">
				<% if (u.getName() == "合計") { %>
				全体
				<% } else if (u.getId() == user.getId()) { %>
				あなた
				<% } else {%>
				<%= u.getName() %>
				<% } %>
			</a>
			<% } %>
		</div>
		<div class="mdl-tabs__panel is-active" id="starks-panel">
			<ul></ul>
		</div>
	</div>
	<div class="input-household">
		<form action="household" method="post"
			style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
			<div class="mdl-grid" style="width: 95%; margin: auto;">
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--2-col">
					<input class="mdl-textfield__input" type="date" id="date" style="height: 35px;" name="date" required>
					<label class="mdl-textfield__label" for="sample3">日付</label>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--3-col" style="display: flex; flex-direction: row; align-items: end; justify-content: center;">
					<input class="mdl-textfield__input" type="number" id="price" style="height: 35px; text-align: right;" name="price" required>
					<label class="mdl-textfield__label" for="sample3">金額</label>
					<p style="margin: auto;">円</p>
				</div>
				<div
					class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--3-col">
					<input type="hidden" id="financial-uid" value="<%= financialList.get(0).getUid() %>">
					<input type="hidden" id="financial-id" value="<%= financialList.get(0).getId() %>">
					<input type="button" class="mdl-textfield__input" id="financial"
						style="height: 44px;" value="あなた　<%=financialList.get(0).getFinancialName()%>">
					<label class="mdl-textfield__label" for="financial" id="financial-lable">口座</label>

					<ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect" for="financial" id="financial-list">
						<%
						for (int i=0; i<financialList.size(); i++) {
							if (financialList.get(i).getPublish()|| user.getId() == financialList.get(i).getUid()) {
								if (i == 0) {
						%>
							<li disabled class="mdl-menu__item financial"><%= financialList.get(i).getUserName() %> <%=financialList.get(i).getFinancialName()%></li>
							<% 	} else { %>
							<li class="mdl-menu__item financial"><%= financialList.get(i).getUserName() %> <%=financialList.get(i).getFinancialName()%></li>
							<%  }
							}
						}%>
					</ul>
				</div>
				<div
					class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--1-col"
					style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 35px; margin-top: 20px;">
					<p style="margin: 0; font-size: 12px; color: orange;">振替</p>
					<button type="button" id="transfer-button"
						style="border: solid 1px; border-radius: 10px; padding-top: 5px;"
						name="transfer-button" value=false>
						<i class="material-icons">compare_arrows</i>
					</button>
				</div>
				<div
					class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--3-col"
					id="transfer-menu" style="display: none;">
					<input type="hidden" id="transfer-uid" value="<%= financialList.get(0).getUid() %>">
					<input type="hidden" id="transfer-id" value="<%= financialList.get(0).getId() %>">
					<input type="button" class="mdl-textfield__input" id="transfer"
						name="transfer" style="height: 44px;"
						value="あなた　<%=financialList.get(0).getFinancialName()%>"> <label
						class="mdl-textfield__label" for="sample3">振替先</label>

					<ul
						class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect"
						for="transfer" id="transfer-list">
						<%
						for (int i = 0; i < financialList.size(); i++) {
							if (financialList.get(i).getPublish() || user.getId() == financialList.get(i).getUid()) {
								if (i == 0) {
						%>
							<li disabled class="mdl-menu__item transfer"><%= financialList.get(i).getUserName() %> <%=financialList.get(i).getFinancialName()%></li>
							 <% } else { %>
							<li class="mdl-menu__item transfer"><%= financialList.get(i).getUserName() %> <%=financialList.get(i).getFinancialName()%></li>
						<%      }
							}
						}
						%>
					</ul>
				</div>
			</div>
			<div class="mdl-grid" style="width: 95%; margin: auto;">
				<div
					class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--4-col">
					<input class="mdl-textfield__input" type="text" id="content"
						style="height: 35px;"> <label class="mdl-textfield__label"
						for="sample3">内容</label>
				</div>
				<div
					class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--2-col">
					<input type="button" class="mdl-textfield__input" id="large-item"
						name="large-item" style="height: 44px;" value="未分類"> <label
						class="mdl-textfield__label" for="sample3">大項目</label>

					<ul
						class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect"
						for="large-item" style="height: 200px; overflow-y: scroll;">
						<%
							for (String item : largeItemList) {
						%>
						<li class="mdl-menu__item large-item"><%=item%></li>
						<%
							}
						%>
					</ul>
				</div>
				<div
					class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--2-col">
					<input type="button" class="mdl-textfield__input" id="middle-item"
						name="middle-item" style="height: 44px;" value="未分類"> <label
						class="mdl-textfield__label" for="sample3">中項目</label>

					<ul
						class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect"
						for="middle-item" id="midle-item__list"
						style="height: 200px; overflow-y: scroll;">
					</ul>
				</div>
				<div
					class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--3-col">
					<input class="mdl-textfield__input" type="text" id="memo"
						name="memo" style="height: 35px;"> <label
						class="mdl-textfield__label" for="sample3">メモ</label>
				</div>
				<div
					class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--1-col"
					style="text-align: left;">
					<input class="mdl-button mdl-js-button mdl-js-ripple-effect button"
						type="button" value="送信" id="submit-button" style="margin: auto;">
				</div>
			</div>
		</form>
	</div>
	<h3 style="color: orange;">家計簿</h3>
	<div
		style="display: flex; flex-direction: row; justify-content: center; align-items: center;">
		<button class="mdl-button mdl-js-button mdl-button--icon" id="left-button">
			<i class="material-icons">keyboard_arrow_left</i>
		</button>
		<h3 id="household-date"></h3>
		<button class="mdl-button mdl-js-button mdl-button--icon" id="right-button">
			<i class="material-icons">keyboard_arrow_right</i>
		</button>
	</div>
	<%
	Calendar cl = Calendar.getInstance();
	SimpleDateFormat nowMonthFormat = new SimpleDateFormat("yyyy-MM");
    String nowMonth = nowMonthFormat.format(cl.getTime());
	%>
	<table
		class="mdl-data-table mdl-js-data-table mdl-shadow--2dp "
		style="margin-bottom: 50px;">
		<thead>
			<tr>
				<th class="mdl-data-table__header--sorted-ascending">日付</th>
				<th>内容</th>
				<th>金額(円)</th>
				<th>口座</th>
				<th>大項目</th>
				<th>中項目</th>
				<th>メモ</th>
				<!-- <th>振替先</th> -->
				<th>口座持ち主</th>
			</tr>
		</thead>
		<tbody id="household_item">
			<%
			List<Household> filter = householdList.stream()
												  .filter(v -> v.getDate().matches(nowMonth+"-.{2}"))
												  .collect(Collectors.toList());
			for (Household household : filter) {
			%>

			<tr class="<%= household.getTransfer() ? "is-transfer" : ""%>">
				<td class="mdl-data-table__cell--non-numeric"><%=household.getDate()%></td>
				<td class="mdl-data-table__cell--non-numeric"><%=household.getContent()%></td>
				<td><%=SendMail.comma(household.getPrice())%></td>
				<td class="mdl-data-table__cell--non-numeric"><%=household.getFinancial()%></td>
				<td class="mdl-data-table__cell--non-numeric"><%=household.getLargeItem()%></td>
				<td class="mdl-data-table__cell--non-numeric"><%=household.getMiddleItem()%></td>
				<td class="mdl-data-table__cell--non-numeric"><%=household.getMemo()%></td>
				<%-- <td class="mdl-data-table__cell--non-numeric"><%=household.getTransfer()%></td> --%>
				<td class="mdl-data-table__cell--non-numeric"><%=household.getUserName()%></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
	<script>
		//jspからデータを取得
		let financialList = [];
		for (let i=0; i<document.financial_list.id.length; i++ ){
			financialList.push({
				id: document.financial_list.id[i].value,
				financialName: document.financial_list.name[i].value,
				userName: document.financial_list.userName[i].value,
				uid: document.financial_list.uid[i].value,
				balance: document.financial_list.balance[i].value,
				publish: document.financial_list.publish[i].value
			});
		}
		let householdList = [];
		if (document.household_list.date !== undefined) {
			for (let i=0; i<document.household_list.date.length; i++) {
				householdList.push({
					date: document.household_list.date[i].value,
					content: document.household_list.content[i].value,
					price: document.household_list.price[i].value,
					financial: document.household_list.financial[i].value,
					largeItem: document.household_list.largeItem[i].value,
					middleItem: document.household_list.middleItem[i].value,
					memo: document.household_list.memo[i].value,
					transfer: document.household_list.transfer[i].value,
					id: document.household_list.id[i].value,
					userName: document.household_list.userName[i].value,
				});
			}
		}
		console.log(householdList);
		const uid = document.user.id.value;
		let financialFilter = financialList;

		//本日の日付を初期値
		let today = new Date();
		today.setDate(today.getDate());
		const yyyy = today.getFullYear();
		const mm = ("0"+(today.getMonth()+1)).slice(-2);
		const dd = ("0"+today.getDate()).slice(-2);
		const householdDate = document.getElementById("household-date");
		document.getElementById("date").value=yyyy+'-'+mm+'-'+dd;
		householdDate.innerText=yyyy+"年"+mm+"月";

		//振替ボタンを押した際の振替先の表示切り替え
		var isTransfer = false;
		const transferButton = document.getElementById("transfer-button");
		const transferLabel = document.getElementById("financial-lable");
		transferButton.addEventListener('click', button => {
			const transfer = document.getElementById("transfer-menu");
			if ((isTransfer = !isTransfer)) {
				transfer.style.cssText = "display: block;";
				transferButton.value = true;
				transferLabel.innerText = "振込元";
			} else {
				transfer.style.cssText = "display: none;";
				transferButton.value = false;
				transferLabel.innerText = "口座";
			}
		});

		//大項目選択時の表示切り替え
		const middleItemMap = {
			"食費": ["食費", "食料品", "外食", "朝ごはん", "昼ごはん", "夜ごはん", "カフェ", "その他食費"],
			"日用品": ["日用品", "子育て用品", "ドラッグストア", "おこづかい", "ペット用品", "タバコ", "その他日用品"],
			"趣味・娯楽": ["外食", "パソコン機器", "アウトドア", "ゴルフ", "スポーツ", "映画・音楽・ゲーム", "本", "旅行", "秘密の趣味", "その他趣味・娯楽"],
			"交際費": ["交際費", "飲み会", "プレゼント代", "冠婚葬祭", "その他交際費"],
			"交通費": ["交通費", "電車", "バス", "タクシー", "飛行機", "その他交通費"],
			"衣服・美容": ["衣服", "クリーニング", "理容院・理髪", "化粧品", "アクセサリー", "その他衣服・美容"],
			"健康・医療": ["フィットネス", "ボディケア", "医療費",  "薬", "その他健康・医療"],
			"自動車": ["自動車ローン", "道路料金", "ガソリン", "駐車場", "車両", "車検・整備", "自動車保険", "その他自動車"],
			"教養・教育": ["書籍", "新聞・雑誌", "習いごと", "学費", "塾", "その他教養・教育"],
			"特別な支出": ["引っ越し", "家具・家電", "住宅・リフォーム", "その他特別な支出"],
			"現金・カード": ["ATM引き出し", "カード引き落とし", "電子マネー", "使途不明金", "その他現金・カード"],
			"水道・光熱費": ["光熱費", "電気代", "ガス・灯油代", "水道代", "その他水道・光熱費"],
			"通信費": ["携帯電話", "固定電話", "インターネット", "放送視聴料", "情報サービス", "宅配便・運送", "その他通信費"],
			"住宅": ["住宅", "家賃・地代", "ローン返済", "管理費・積立金", "地震・火災保険", "その他住宅"],
			"税・社会保障": ["所得税・住民税", "年金保険料", "健康保険", "その他税・社会保障"],
			"保険": ["生命保険", "医療保険", "その他保険"],
			"その他": ["仕送り", "事業経費", "事業原価", "事業投資", "寄付金", "雑費"],
			"未分類": ["未分類"]
		};
		const largeItems = document.getElementsByClassName("large-item");
		const largeItemText = document.getElementById("large-item");
		const middleItemList = document.getElementById('midle-item__list');
		Array.from(largeItems).forEach(item => {
			item.addEventListener('click', event => {
				const largeItem = event.currentTarget.innerText;
				largeItemText.value = largeItem;
				while (middleItemList.firstChild) {
					middleItemList.removeChild(middleItemList.firstChild);
				}
				middleItemMap[largeItem].map(middleItem => {
					const li = document.createElement('li');
					li.classList.add("mdl-menu__item")
					li.classList.add("middle-item");
					li.innerText = middleItem;
					middleItemList.appendChild(li);
				});
				const middleItems = document.getElementsByClassName("middle-item");
				const middleItemText = document.getElementById("middle-item");
				Array.from(middleItems).forEach(item => {
					item.addEventListener('click', event => {
						middleItemText.value = event.currentTarget.innerText;
					});
				});
			});
		});

		//口座選択時の切り替え
		const financials = document.getElementsByClassName("financial");
		const financialText = document.getElementById("financial");
		const financialUid = document.getElementById("financial-uid");
		const financialId = document.getElementById("financial-id");
		const transfers = document.getElementsByClassName("transfer");
		Array.from(financials).forEach(financial => {
			financial.addEventListener('click', event => {
				if (!event.currentTarget.getAttribute("disabled")) {
					Array.from(financials).forEach(financial => {
						financial.removeAttribute("disabled");
					});
					const index = [].slice.call(financials).indexOf(financial);
					financials[index].setAttribute("disabled", "disabled");
					financialText.value = event.currentTarget.innerText;
					financialUid.value = financialFilter[index].uid;
					financialId.value = financialFilter[index].id;
					Array.from(transfers).forEach(transfer => {
						transfer.removeAttribute("disabled");
					});
					transfers[index].setAttribute("disabled", "disabled");
				}
			});
		});

		//振替先選択時の切り替え
		const transferText = document.getElementById("transfer");
		const transferUid = document.getElementById("transfer-uid");
		const transferId = document.getElementById("transfer-id");
		Array.from(transfers).forEach(transfer => {
			transfer.addEventListener('click', event => {
				const index = [].slice.call(transfers).indexOf(transfer);
				if (!event.currentTarget.getAttribute("disabled")) {
					transferText.value = event.currentTarget.innerText;
					transferUid.value = financialFilter[index].uid;
					transferId.value = financialFilter[index].id;
				}
			});
		});

		//戻るボタンを押したときのローディング表示
		const backButton = document.getElementById("back-button");
		const progressBar = document.getElementById("progress-bar");
		backButton.addEventListener('click', event => {
			progressBar.style.cssText = "display: block;";
		});

		//送信ボタンを押した時
		const submitButton = document.getElementById("submit-button");
		submitButton.addEventListener('click', event => {
			const now = new Date();
			const date = document.getElementById("date").value+" "+now.getHours()+":"+now.getMinutes()+":"+now.getSeconds();
			const price = document.getElementById("price");
			const financialId = document.getElementById("financial-id");
			const isTransfer = document.getElementById("transfer-button");
			const transferId = document.getElementById("transfer-id");
			const content = document.getElementById("content");
			const largeItem = document.getElementById("large-item");
			const middleItem = document.getElementById("middle-item");
			const memo = document.getElementById("memo");
			const sourceUid = document.getElementById("financial-uid");
			const transferUid = document.getElementById("transfer-uid");
			console.log(date);
			const data = {
				date: date,
				price: price.value,
				financialId: financialId.value,
				isTransfer: isTransfer.value,
				transferId: transferId.value,
				content: content.value,
				largeItem: largeItem.value,
				middleItem: middleItem.value,
				memo: memo.value,
				sourceUid: sourceUid.value,
				transferUid: transferUid.value
		    };
			if (data.isTransfer == true && data.financial == data.transfer) {
				alert("振込元の口座と振込先の口座が同じです");
			} else if (data.price.length == 0) {
				alert("金額を入力してください");
			} else {
				progressBar.style.cssText = "display: block;";
				$.ajax({
					type    : "POST",
				    url     : location.href,
				    async   : true,
				    data : data,
				    success : function(data) {
				    	console.log("SUCCESS!");
				    	alert("送信に成功しました！");
				    	location.reload();
				    	progressBar.style.cssText = "display: block;";
				    },
				    error : function(XMLHttpRequest, textStatus, errorThrown) {
				    	progressBar.style.cssText = "display: none;";
				      	alert("リクエスト時になんらかのエラーが発生しました：" + textStatus +":\n" + errorThrown);
				    }
				});
			}
		});

		//タブの切り替え
		const tabs = document.getElementsByClassName("mdl-tabs__tab");
		const householdItems = document.getElementById("household_item");
		const financialLists = document.getElementById("financial-list");
		const transferLists = document.getElementById("transfer-list");
		let targetTabName = "全体";
		Array.from(tabs).forEach(tab => {
			tab.addEventListener('click', event => {
				targetTabName = event.currentTarget.innerText;
				const format = today.getFullYear()+"-"+('0'+(today.getMonth()+1)).slice(-2)+"-.{2}";
				const householdFilter = targetTabName == "全体"
					? householdList.filter(household => household.date.match(new RegExp(format)))
					: householdList.filter(household => household.userName.toUpperCase() == targetTabName && household.date.match(new RegExp(format)));
				while (householdItems.firstChild) {
					householdItems.removeChild(householdItems.firstChild);
				}
				householdFilter.map(household => {
					const tr = document.createElement('tr');
					for (let [key, value] of Object.entries(household)) {
						if(key == "transfer" && value == "true") tr.classList.add("is-transfer");
						if(key == "id" || key == "transfer") continue;
						const td = document.createElement('td');
						if (key != "price") td.classList.add("mdl-data-table__cell--non-numeric");
						if (key == "price") td.innerText = value.replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
						else td.innerText = value;
						tr.appendChild(td);
					}
					householdItems.appendChild(tr);
				});
				//口座の切り替え
				while (financialLists.firstChild) {
					financialLists.removeChild(financialLists.firstChild);
					transferLists.removeChild(transferLists.firstChild);
				}
				financialFilter = targetTabName == "全体"
										? financialList
										: financialList.filter(financial => financial.userName.toUpperCase() == targetTabName );
				financialFilter.map((financial, index) => {
					if (financial.publish == "true" || uid == financial.uid) {
						const financial_li = document.createElement('li');
						const transfer_li = document.createElement('li');
						financial_li.classList.add("mdl-menu__item");
						financial_li.classList.add("financial");
						transfer_li.classList.add("mdl-menu__item");
						transfer_li.classList.add("transfer");
						if (index == 0) {
							financial_li.setAttribute("disabled", "disabled");
							financialText.value = financial.userName + " " + financial.financialName;
							financialUid.value = financial.uid;
							financialId.value = financial.id;
							transferText.value = financial.userName + " " + financial.financialName;
							transferUid.value = financial.uid;
							transferId.value = financial.id;
						}
						financial_li.innerText = financial.userName + " " + financial.financialName;
						transfer_li.innerText = financial.userName + " " + financial.financialName;
						financialLists.appendChild(financial_li);
						transferLists.appendChild(transfer_li);
					}
				});
				Array.from(document.getElementsByClassName("financial")).forEach(financial => {
					financial.addEventListener('click', event => {
						if (!event.currentTarget.getAttribute("disabled")) {
							Array.from(document.getElementsByClassName("financial")).forEach(financial => {
								financial.removeAttribute("disabled");
							});
							const index = [].slice.call(document.getElementsByClassName("financial")).indexOf(financial);
							document.getElementsByClassName("financial")[index].setAttribute("disabled", "disabled");
							financialText.value = event.currentTarget.innerText;
							financialUid.value = financialList[index].uid;
							financialId.value = financialList[index].id;
							Array.from(document.getElementsByClassName("transfer")).forEach(transfer => {
								transfer.removeAttribute("disabled");
							});
							document.getElementsByClassName("transfer")[index].setAttribute("disabled", "disabled");
						}
					});
				});
				Array.from(document.getElementsByClassName("transfer")).forEach(transfer => {
					transfer.addEventListener('click', event => {
						const index = [].slice.call(document.getElementsByClassName("transfer")).indexOf(transfer);
						if (!event.currentTarget.getAttribute("disabled")) {
							transferText.value = event.currentTarget.innerText;
							transferUid.value = financialList[index].uid;
							transferId.value = financialList[index].id;
						}
					});
				});
			});
		});

		//月別フィルター
		//先月
		const leftButton = document.getElementById("left-button");
		const rightButton = document.getElementById("right-button");
		leftButton.addEventListener('click', event => {
			today = new Date(today.getFullYear()+"-"+today.getMonth());
			householdDate.innerText = today.getFullYear()+"年"+('0'+(today.getMonth()+1)).slice(-2)+"月";
			const format = today.getFullYear()+"-"+('0'+(today.getMonth()+1)).slice(-2)+"-.{2}";
			const result = targetTabName == "全体"
				? householdList.filter(household => household.date.match(new RegExp(format)))
				: householdList.filter(household => household.userName.toUpperCase() == targetTabName && household.date.match(new RegExp(format)));
			while (householdItems.firstChild) {
				householdItems.removeChild(householdItems.firstChild);
			}
			console.log(householdList);
			console.log(result);
			result.map(household => {
				const tr = document.createElement('tr');
				for (let [key, value] of Object.entries(household)) {
					if(key == "transfer" && value == "true") tr.classList.add("is-transfer");
					if(key == "id" || key == "transfer") continue;
					const td = document.createElement('td');
					if (key != "price") td.classList.add("mdl-data-table__cell--non-numeric");
					if (key == "price") td.innerText = value.replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					else td.innerText = value;
					tr.appendChild(td);
				}
				householdItems.appendChild(tr);
			});
		});
		//翌月
		rightButton.addEventListener('click', event => {
			today = new Date(today.getFullYear()+"-"+(today.getMonth()+2));
			householdDate.innerText = today.getFullYear()+"年"+('0'+(today.getMonth()+1)).slice(-2)+"月";
			const format = today.getFullYear()+"-"+('0'+(today.getMonth()+1)).slice(-2)+"-.{2}";
			const result = targetTabName == "全体"
				? householdList.filter(household => household.date.match(new RegExp(format)))
				: householdList.filter(household => household.userName.toUpperCase() == targetTabName && household.date.match(new RegExp(format)));
			while (householdItems.firstChild) {
				householdItems.removeChild(householdItems.firstChild);
			}
			result.map(household => {
				const tr = document.createElement('tr');
				for (let [key, value] of Object.entries(household)) {
					if(key == "transfer" && value == "true") tr.classList.add("is-transfer");
					if(key == "id" || key == "transfer") continue;
					const td = document.createElement('td');
					if (key != "price") td.classList.add("mdl-data-table__cell--non-numeric");
					if (key == "price") td.innerText = value.replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
					else td.innerText = value;
					tr.appendChild(td);
				}
				householdItems.appendChild(tr);
			});
		});
	</script>
</body>
</html>