<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="model.Household"
    import="model.Analytics"
    import="java.util.*"
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.orange-pink.min.css" />
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
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
.material-icons {
  font-family: 'Material Icons';
  font-weight: normal;
  font-style: normal;
  font-size: 24px;  /* Preferred icon size */
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
</style>
</head>
<body>
	<% List<String> largeItemList = new ArrayList<String>(Arrays.asList("食費", "日用品", "趣味・娯楽", "交際費", "交通費", "衣服・美容", "健康・医療", "自動車", "教養・教育", "特別な支出", "現金・カード", "水道・光熱費", "通信費", "住宅", "税・社会保障", "保険", "その他", "未分類")); %>
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
		           	<a class="mdl-navigation__link mdl-typography--text-uppercase" href="">連携口座</a>
		          	<a class="mdl-navigation__link mdl-typography--text-uppercase" href="">マイメニュー</a>
		        	<a class="mdl-navigation__link mdl-typography--text-uppercase" href="${pageContext.request.contextPath}/balance?mode=logout">ログアウト</a>
            	</nav>
          	</div>
		</div>
	</div>
	<a href="${pageContext.request.contextPath}/balance" style="position: absolute; left: 50px; top: 80px;" id="back-button">&lt; 戻る</a>
	<h3 style="color: orange;">家計簿入力</h3>
	<div class="input-household">
		<form action="#" style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
			<div class="mdl-grid" style="width: 95%; margin: auto;">
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--2-col">
					<input class="mdl-textfield__input" type="date" id="date" style="height: 35px;" required>
					<label class="mdl-textfield__label" for="sample3">日付</label>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--3-col" style="display: flex; flex-direction: row; align-items: end; justify-content: center;">
				    <input class="mdl-textfield__input" type="number" id="price" style="height: 35px; text-align: right;" required>
				    <label class="mdl-textfield__label" for="sample3">金額</label>
				    <p style="margin: auto;">円</p>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--3-col">
				    <input type="button" class="mdl-textfield__input" id="financial" style="height: 44px;" value="お財布">
				    <label class="mdl-textfield__label" for="sample3">口座</label>

					<ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect" for="financial">
					  <li class="mdl-menu__item">Some Action</li>
					  <li class="mdl-menu__item">Another Action</li>
					  <li disabled class="mdl-menu__item">Disabled Action</li>
					  <li class="mdl-menu__item">Yet Another Action</li>
					</ul>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--1-col" style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 35px; margin-top: 20px;">
					<p style="margin: 0; font-size: 12px; color: orange;">振替</p>
					<button id="transfer-button" style="border: solid 1px; border-radius: 10px; padding-top: 5px;">
						<i class="material-icons">compare_arrows</i>
					</button>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--3-col" id="transfer-menu" style="display: none;">
				    <input type="button" class="mdl-textfield__input" id="transfer" style="height: 44px;" value="お財布">
				    <label class="mdl-textfield__label" for="sample3">振替先</label>

					<ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect" for="transfer">
					  <li class="mdl-menu__item">Some Action</li>
					  <li class="mdl-menu__item">Another Action</li>
					  <li disabled class="mdl-menu__item">Disabled Action</li>
					  <li class="mdl-menu__item">Yet Another Action</li>
					</ul>
				</div>
			</div>
			<div class="mdl-grid" style="width: 95%; margin: auto;">
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--4-col">
					<input class="mdl-textfield__input" type="text" id="content" style="height: 35px;">
					<label class="mdl-textfield__label" for="sample3">内容</label>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--2-col">
				    <input type="button" class="mdl-textfield__input" id="large-item" style="height: 44px;" value="未分類">
				    <label class="mdl-textfield__label" for="sample3">大項目</label>

					<ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect" for="large-item">
						<% for (String item : largeItemList) { %>
							<li class="mdl-menu__item large-item" ><%= item %></li>
						<% } %>
					</ul>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--2-col">
				    <input type="button" class="mdl-textfield__input" id="middle-item" style="height: 44px;" value="未分類">
				    <label class="mdl-textfield__label" for="sample3">中項目</label>

					<ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect" for="middle-item">
					  <li class="mdl-menu__item">Some Action</li>
					  <li class="mdl-menu__item">Another Action</li>
					  <li disabled class="mdl-menu__item">Disabled Action</li>
					  <li class="mdl-menu__item">Yet Another Action</li>
					</ul>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--3-col">
				    <input class="mdl-textfield__input" type="text" id="memo" style="height: 35px;">
				    <label class="mdl-textfield__label" for="sample3">メモ</label>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--1-col" style="text-align: left;">
				    <input class="mdl-button mdl-js-button mdl-js-ripple-effect button" type="submit" value="送信" id="submit-button" style="margin: auto;">
				</div>
			</div>
		</form>
	</div>
	<h3 style="color: orange;">家計簿</h3>
	<div style="display: flex; flex-direction: row; justify-content: center; align-items: center;">
		<p style="margin: auto; margin-right: 10px;">&lt;</p>
		<h3>2020年05月</h3>
		<p style="margin: auto; margin-left: 10px;">&gt;</p>
	</div>
	<%  List<Household> householdList = new ArrayList<Household>();
		householdList.add(new Household("2020/05/12", "購入", -30000, "Kyash", "食費", "その他教養・教育", "", "", "8kaoirguw093", "あああ"));
		for(int i=0; i<20; i++) {
			householdList.add(new Household("2020/05/12", "購入 PREMIUMSELECTIONRAKUTE", -3000, "Kyash", "水道・光熱費", "その他教養・教育", "", "", "8kaoirguw093", "あああ"));
		}
	%>
	<table class="mdl-data-table mdl-js-data-table mdl-data-table--selectable mdl-shadow--2dp " style="margin-bottom: 50px;">
		<thead>
			<tr>
				<th class="mdl-data-table__header--sorted-ascending">日付</th>
				<th>内容</th>
				<th>金額(円)</th>
				<th>口座</th>
				<th>大項目</th>
				<th>中項目</th>
				<th>メモ</th>
				<th>振替先</th>
				<th>支払者</th>
			</tr>
	  	</thead>
	  	<tbody>
	  		<% for(Household household : householdList) { %>
	    	<tr>
	      		<td class="mdl-data-table__cell--non-numeric"><%= household.getDate() %></td>
	      		<td class="mdl-data-table__cell--non-numeric"><%= household.getContent() %></td>
	      		<td><%= household.getPrice() %></td>
	      		<td class="mdl-data-table__cell--non-numeric"><%= household.getFinancial() %></td>
	      		<td class="mdl-data-table__cell--non-numeric"><%= household.getLargeItem() %></td>
	      		<td class="mdl-data-table__cell--non-numeric"><%= household.getMiddleItem() %></td>
	      		<td class="mdl-data-table__cell--non-numeric"><%= household.getMemo() %></td>
	      		<td class="mdl-data-table__cell--non-numeric"><%= household.getTransfer() %></td>
	      		<td class="mdl-data-table__cell--non-numeric"><%= household.getUserName() %></td>
	    	</tr>
	    	<% } %>
	  	</tbody>
	</table>
	<script>
		const today = new Date();
		today.setDate(today.getDate());
		const yyyy = today.getFullYear();
		const mm = ("0"+(today.getMonth()+1)).slice(-2);
		const dd = ("0"+today.getDate()).slice(-2);
		document.getElementById("date").value=yyyy+'-'+mm+'-'+dd;

		var isTransfer = false;
		const transferButton = document.getElementById("transfer-button");
		transferButton.addEventListener('click', button => {
			const transfer = document.getElementById("transfer-menu");
			if ((isTransfer = !isTransfer)) {
				transfer.style.cssText = "display: block;";
			} else {
				transfer.style.cssText = "display: none;";
			}
		});

		const largeItems = document.getElementsByClassName("large-item");
		Array.from(largeItems).forEach(item => {
			item.addEventListener('click', event => {
				console.log(event.currentTarget);
			});
		});

		const backButton = document.getElementById("back-button");
		const progressBar = document.getElementById("progress-bar");
		backButton.addEventListener('click', event => {
			progressBar.style.cssText = "display: block;";
		});
	</script>
</body>
</html>