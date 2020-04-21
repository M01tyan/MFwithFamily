package model;

public class User {
	private int id;
	private String name;
	private int relationshipId;
	private int familyId;

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
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
}
