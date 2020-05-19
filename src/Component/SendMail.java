package Component;

import java.io.IOException;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

/**
 * メール送信クラス
 * @author maeda.kanta
 */
public class SendMail {
	/**
	 * メール送信メソッド
	 * ランダムな6文字の数字を生成し、SendGrid APIを使用してメールへ送信
	 * @param email 生のメールアドレス
	 * @param path 認証画面へのパス
	 * @return 生成された任用コード
	 * @throws IOException
	 */
	public String send(String email, String path) throws IOException {
		String code = createAuthCode();
		Email from = new Email("maeda.kanta@moneyforward.co.jp");
	    String subject = "【Money Forward with Family】認証コードをお送りします。会員登録を完了させてください。";
	    Email to = new Email(email);
	    Content content = new Content("text/html", "この度はご登録いただきありがとうございます。<BR>2段階認証のための認証コードをお送りいたしますので、ご確認の上、下記サイトよりご入力のほどお願いいたします。<BR><BR>"
	    		+ "<span style=\"font-size: 25px; color: orange\">" + code + "</span><BR>"
	    		+ "http://" + path + "/auth" + "<BR><BR>"
	    		+ "※このコードはメール送信から30分間の有効期限のため、お早めにご入力のほどお願いいたします。");
	    Mail mail = new Mail(from, subject, to, content);

	    // 環境変数からSendGridのAPIキーを取得
	    SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	      System.out.println(response.getStatusCode());
	      System.out.println(response.getBody());
	      System.out.println(response.getHeaders());
	    } catch (IOException ex) {
	      throw ex;
	    }
	    return code;
	}

	/**
	 * 認証コードを生成するメソッド
	 * ランダムな6文字の数字を生成する
	 * @return 生成された6文字の認証コード
	 */
	public String createAuthCode() {
		int FINAL_NUMBER = 6;
		String code = "";
		for (int i=0; i<FINAL_NUMBER; i++) {
			code += (int)(Math.random() * 9) + 1;
		}
		return code;
	}
}
