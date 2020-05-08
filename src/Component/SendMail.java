package Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SendMail {
	public String send(String email) throws UnirestException {
		String code = createAuthCode();
		HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + System.getenv("MAILGUN_DOMAIN") + "/messages")
	            .basicAuth("api", System.getenv("MAILGUN_API_KEY"))
	            .field("from", System.getenv("MAILGUN_FROM_MAIL"))
	            .field("to", email)
	            .field("subject", "認証コード")
	            .field("text", "認証コードをお送りします。\n\n"
	            		     + code +"\n\n"
	            		     + "アプリの方で認証コードを入力し、アカウントを有効にしてください。\n"
	            		     + "なお30分以内に認証しない場合は失効されます。")
	            .asJson();

		System.out.println(request.getBody());
	    return code;
	}

	public static String createAuthCode() {
		int FINAL_NUMBER = 4;
		String code = "";
		for (int i=0; i<FINAL_NUMBER; i++) {
			code += (int)(Math.random() * 9) + 1;
		}
		return code;
	}
}
