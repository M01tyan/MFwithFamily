package model;

import java.util.ArrayList;
import java.util.List;

public class Balance {
	private int totalBalance;
	private List<Integer> eachBalancePrice = new ArrayList<Integer>();
	private List<String> eachBalanceName = new ArrayList<String>();

	public void setTotalBalance(int totalBalance) {
		this.totalBalance = totalBalance;
	}
	public int getTotalBalance() {
		return this.totalBalance;
	}

	public void addEachBalance(int price, String name) {
		this.eachBalancePrice.add(price);
		this.eachBalanceName.add(name);
	}
	public int getEachBalancePrice(int index) {
		return this.eachBalancePrice.get(index);
	}
	public String getEachBalanceName(int index) {
		return this.eachBalanceName.get(index);
	}
}
