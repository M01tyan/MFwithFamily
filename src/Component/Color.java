package Component;

/**
 * カラーenum
 * 6色のカラーを事前に作成
 * 薄い色と濃い色の２種類用意
 * 薄い色は残高画面の財布の色に使用
 * 濃い色は家族追加画面のユーザごとの色
 * @author maeda.kanta
 */
public enum Color {
	Green("#B3FE85", "#51C20C"),
	Red("#FF9898", "#ED2828"),
	Blue("#9FD1FF", "#1773C9"),
	Yellow("#FFF59F", "#F5DE08"),
	Purple("#B3FE85", "#7830EF"),
	Mint("#9FFFEE", "#2CD6CA"),
	Pink("#FF9FD3", "#F92D9B");

	private String thin;
	private String dark;

	private Color(String thin, String dark) {
		this.thin = thin;
		this.dark = dark;
	}

	public String getThinColor() {
		return this.thin;
	}

	public String getDarkColor() {
		return this.dark;
	}
}
