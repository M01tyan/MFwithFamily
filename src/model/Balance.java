package model;

public class Balance {
	private String name;
	private int price;

	public Balance() {}

	public Balance(String name, int price) {
		this.name = name;
		this.price = price;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	public int getPrice() {
		return this.price;
	}
}
