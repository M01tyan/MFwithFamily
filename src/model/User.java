package model;

public class User {
	private int uid;
	private String name;
	private int relationshipId;
	private int familyId;
	private boolean emailCertificate;
	private int balance;

	public User() {}

	public User(int uid, String name, int relationshipId, int familyId, boolean emailCertificate, int balance) {
		this.uid = uid;
		this.name = name;
		this.relationshipId = relationshipId;
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

	public void setRelationshipId(int relationshipId) {
		this.relationshipId = relationshipId;
	}
	public int getRelationshipId() {
		return this.relationshipId;
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
