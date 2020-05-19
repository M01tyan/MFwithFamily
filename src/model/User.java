package model;

/**
 * ユーザモデル
 * uid ユーザID
 * name ユーザ名
 * familyId 家族ID
 * emailCertificate メール認証フラグ
 * balance 残高
 * @author maeda.kanta
 *
 */
public class User {
	private int uid;
	private String name;
	private int familyId;
	private boolean emailCertificate;
	private int balance;

	public User() {
		this.uid = -1;
		this.familyId = -1;
		this.emailCertificate = false;
		this.balance = 0;
	}

	public User(int uid, String name, int familyId, boolean emailCertificate, int balance) {
		this.uid = uid;
		this.name = name;
		this.familyId = familyId;
		this.emailCertificate = emailCertificate;
		this.balance = balance;
	}

	public void setId(int id) {
		this.uid = id;
	}
	public int getId() {
		return this.uid;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}
	public int getFamilyId() {
		return this.familyId;
	}

	public void setEmailCertificate(boolean emailCertificate) {
		this.emailCertificate = emailCertificate;
	}
	public boolean getEmailCertificate() {
		return this.emailCertificate;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getBalance() {
		return this.balance;
	}
}
