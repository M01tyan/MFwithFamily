package model;

public class Household {
	private int target;
	private String date;
	private String content;
	private int price;
	private String financial;
	private String largeItem;
	private String middleItem;
	private String memo;
	private int transfer;
	private String id;
	private String userName;
	private String relationshipName;

	public void setTarget(int target) {
		this.target = target;
	}
	public int getTarget() {
		return this.target;
	}

	public void setDate(String date) {
//		System.out.println(date);
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

	public void setTransfer(int transfer) {
		this.transfer = transfer;
	}
	public int getTransfer() {
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

	public void setRelationshipName(String relationshipName) {
		this.relationshipName = relationshipName;
	}
	public String getRelationshipName() {
		return this.relationshipName;
	}
}
