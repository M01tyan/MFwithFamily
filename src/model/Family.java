package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 家族モデル
 * id 家族ID
 * shareCode 家族コード
 * userList 家族の個別のユーザ情報
 * @author maeda.kanta
 *
 */
public class Family {
	private int id;
	private String shareCode;
	private List<User> userList;

	public Family() {
		this.id = -1;
		this.userList = new ArrayList<User>();
		this.shareCode = null;
	}

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
