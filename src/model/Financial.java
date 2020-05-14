package model;

public class Financial {
	private int id;
	private String financialName;
	private String userName;
	private int uid;
	private int balance;

	public Financial() {}
	public Financial(int id, String financialName, String userName, int uid, int balance) {
		this.id = id;
		this.financialName = financialName;
		this.userName = userName;
		this.uid = uid;
		this.balance = balance;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	public void setFinancialName(String financialName) {
		this.financialName = financialName;
	}
	public String getFinancialName() {
		return this.financialName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return this.userName;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getUid() {
		return this.uid;
	}
	public void setBlance(int balance) {
		this.balance = balance;
	}
	public int getBalance() {
		return this.balance;
	}
}
