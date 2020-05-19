package Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	/**
	 * バリデーションメソッド
	 * @param email メールアドレス
	 * @param password パスワード
	 * @param confirmation 確認用パスワード
	 * @param name 空でないか
	 * @return エラーメッセージ
	*/
	public static String validation(String email, String password, String confirmation, String name) {
		String message = "";
		if (!emailValidation(email)) message += "正しいメールアドレスを入力してください<BR>";
		if (!passwordValidation(password)) message += "パスワードは半角英数字で入力してください<BR>";
		if (!password.equals(confirmation)) message += "パスワードが一致しません<BR>";
		if (name.isEmpty()) message += "ユーザ名を入力してください<BR>";
		return message;
	}

	/**
	 * メールアドレスのバリデーションメソッド
	 * メールフォーマットにあっているかをチェック
	 * @param email メールアドレス
	 * @return 判定結果(true: OK, false: NO)
	 */
	public static boolean emailValidation(String email) {
		String pattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	    Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(email);
	    return m.find();
	}

	/**
	 * パスワードのバリデーションメソッド
	 * パスワードが6~20文字の半角英数字かをチェック
	 * @param password
	 * @return 判定結果(true: OK, false: NO)
	 */
	public static boolean passwordValidation(String password) {
		String pattern = "^[0-9a-zA-Z.-_@!&#$%]{6,20}";
	    Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(password);
	    return m.find();
	}
}
