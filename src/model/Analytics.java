package model;

public class Analytics {
	private int price;
	private String largeItem;
	private String payer;

	public void setPrice(int price) {
		this.price = price;
	}
	public int getPrice() {
		return this.price;
	}

	public void setLargeItem(String largeItem) {
		this.largeItem = largeItem;
	}
	public String getLargeItem() {
		return this.largeItem;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPayer() {
		return this.payer;
	}
}
