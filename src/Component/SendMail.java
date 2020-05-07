package Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SendMail {
	public String send(String email) throws UnirestException {
		String code = createAuthCode();
		HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + "sandboxcc344b42ea404e4a9d8007c97e1c9ce6.mailgun.org" + "/messages")
	            .basicAuth("api", "fe47e49c9540e7d868271d607381751c-0afbfc6c-4685dd4d")
	            .field("from", "Excited User <USER@YOURDOMAIN.COM>")
	            .field("to", email)
	            .field("subject", "Authentication code")
	            .field("text", code)
	            .asJson();

		System.out.println(request.getBody());
//		Email from = new Email("maeda.kanta@moneyforward.co.jp");
//	    String mailSubject = "Authentication Email";
//	    Email to = new Email(email);
//	    Content mailContent = new Content("text/plain",
//	    		"認証コードをお送りします。\n\n"
//	    		+ code + "\n\n"
//	    		+ "アプリの方で認証コードを入力し、\n"
//	    		+ "アカウントを有効にしてください。"
//	    		+ "なお30分以内に認証しない場合は失効します");
//	    Mail mail = new Mail(from, mailSubject, to, mailContent);
//	    String sendGridAPI = System.getenv("SENDGRID_API_KEY");
//	    System.out.println(sendGridAPI);
//	    SendGrid sg = new SendGrid(sendGridAPI);
//	    Request request = new Request();
//	    try {
//	      request.setMethod(Method.POST);
//	      request.setEndpoint("mail/send");
//	      request.setBody(mail.build());
//	      Response response = sg.api(request);
//	      System.out.println(response.getStatusCode());
//	      System.out.println(response.getBody());
//	      System.out.println(response.getHeaders());
//	    } catch (IOException ex) {
//	      throw ex;
//	    }
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
