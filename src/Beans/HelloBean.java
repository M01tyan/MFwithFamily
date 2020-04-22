package Beans;

public class HelloBean {
	private String id;

    //(3)プロパティidのsetメソッドを宣言しています。
    public void setId(String id) { this.id = id; }

    //(4)プロパティidのgetメソッドを宣言しています。
    public String getId() { return this.id; }
}
