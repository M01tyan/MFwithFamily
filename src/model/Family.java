package model;

import java.util.List;

public class Family {
	private int id;
	private String shareCode;
	private List<User> userList;

	public Family() {}

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}

	public void setShareCode(String authCode) {
		this.shareCode = authCode;
	}
	public String getShareCode() {
		return this.shareCode;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public List<User> getUserList() {
		return this.userList;
	}
}
