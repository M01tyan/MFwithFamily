package model;

/**
 * 家計簿モデル
 * date 日付
 * content 内容
 * price 値段
 * financial 口座名
 * largeItem 大項目
 * middleItem 中項目
 * memo メモ
 * transfer 振替フラグ
 * id 家計簿ID
 * userName 記入者のユーザID
 * @author maeda.kanta
 *
 */
public class Household {
	private String date;
	private String content;
	private int price;
	private String financial;
	private String largeItem;
	private String middleItem;
	private String memo;
	private boolean transfer;
	private String id;
	private String userName;

	public Household() {}

	public Household(String date, String content, int price, String financial, String largeItem, String middleItem, String memo, boolean transfer, String id, String userName) {
		this.date = date;
		this.content = content;
		this.price = price;
		this.financial = financial;
		this.largeItem = largeItem;
		this.middleItem = middleItem;
		this.memo = memo;
		this.transfer = transfer;
		this.id = id;
		this.userName = userName;
	}

	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return this.date;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return this.content;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	public int getPrice() {
		return this.price;
	}

	public void setFinancial(String financial) {
		this.financial = financial;
	}
	public String getFinancial() {
		return this.financial;
	}

	public void setLargeItem(String largeItem) {
		this.largeItem = largeItem;
	}
	public String getLargeItem() {
		return this.largeItem;
	}

	public void setMiddleItem(String middleItem) {
		this.middleItem = middleItem;
	}
	public String getMiddleItem() {
		return this.middleItem;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMemo() {
		return this.memo;
	}

	public void setTransfer(boolean transfer) {
		this.transfer = transfer;
	}
	public boolean getTransfer() {
		return this.transfer;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return this.userName;
	}

}
