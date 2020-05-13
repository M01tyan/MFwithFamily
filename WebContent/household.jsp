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
	<%
	List<String> largeItemList = new ArrayList<String>(Arrays.asList("食費", "日用品", "趣味・娯楽", "交際費", "交通費", "衣服・美容", "健康・医療", "自動車", "教養・教育", "特別な支出", "現金・カード", "水道・光熱費", "通信費", "住宅", "税・社会保障", "保険", "その他", "未分類"));
	List<String> financialList = (List<String>) session.getAttribute("financialList");
	%>
	<form name="financial_form">
		<% for (String financial : financialList) { %>
			<input type="hidden" name="financial" value="<%= financial %>">
		<% } %>
	</form>
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
				    <input type="button" class="mdl-textfield__input" id="financial" style="height: 44px;" value="<%= financialList.get(0) %>">
				    <label class="mdl-textfield__label" for="sample3">口座</label>

					<ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect" for="financial">
						<% for (String financial : financialList) { %>
						<li class="mdl-menu__item financial"><%= financial %></li>
						<% } %>
					</ul>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--1-col" style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 35px; margin-top: 20px;">
					<p style="margin: 0; font-size: 12px; color: orange;">振替</p>
					<button id="transfer-button" style="border: solid 1px; border-radius: 10px; padding-top: 5px;">
						<i class="material-icons">compare_arrows</i>
					</button>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--3-col" id="transfer-menu" style="display: none;">
				    <input type="button" class="mdl-textfield__input" id="transfer" style="height: 44px;" value="<%= financialList.get(0) %>">
				    <label class="mdl-textfield__label" for="sample3">振替先</label>

					<ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect" for="transfer" id="transfer-liset__parent">
					<%  for (int i=0; i<financialList.size(); i++) {
						   if (i==0) { %>
						<li disabled class="mdl-menu__item transfer"><%= financialList.get(i) %></li>
						<% } else { %>
						<li class="mdl-menu__item transfer"><%= financialList.get(i) %></li>
						<% }
						}%>
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

					<ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect" for="large-item" style="height: 200px; overflow-y: scroll;">
						<% for (String item : largeItemList) { %>
							<li class="mdl-menu__item large-item" ><%= item %></li>
						<% } %>
					</ul>
				</div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--2-col">
				    <input type="button" class="mdl-textfield__input" id="middle-item" style="height: 44px;" value="未分類">
				    <label class="mdl-textfield__label" for="sample3">中項目</label>

					<ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect" for="middle-item" id="midle-item__list" style="height: 200px; overflow-y: scroll;">
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
		//本日の日付を初期値
		const today = new Date();
		today.setDate(today.getDate());
		const yyyy = today.getFullYear();
		const mm = ("0"+(today.getMonth()+1)).slice(-2);
		const dd = ("0"+today.getDate()).slice(-2);
		document.getElementById("date").value=yyyy+'-'+mm+'-'+dd;

		//振替ボタンを押した際の振替先の表示切り替え
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

		//項目選択時の表示切り替え
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
		const transfers = document.getElementsByClassName("transfer");
		Array.from(financials).forEach(financial => {
			financial.addEventListener('click', event => {
				const index = [].slice.call(financials).indexOf(financial);
				financialText.value = event.currentTarget.innerText;
				Array.from(transfers).forEach(transfer => {
					transfer.removeAttribute("disabled");
				});
				transfers[index].setAttribute("disabled", "disabled");
			});
		});

		//振替先選択時の切り替え

		const transferText = document.getElementById("transfer");
		Array.from(transfers).forEach(transfer => {
			transfer.addEventListener('click', event => {
				if (!event.currentTarget.getAttribute("disabled")) {
					transferText.value = event.currentTarget.innerText;
				}
			});
		});

		//戻るボタンを押したときのローディング表示
		const backButton = document.getElementById("back-button");
		const progressBar = document.getElementById("progress-bar");
		backButton.addEventListener('click', event => {
			progressBar.style.cssText = "display: block;";
		});


	</script>
</body>
</html>