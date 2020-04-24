package Component;

import java.io.IOException;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

public class SendMail {

	public String send(String email) throws IOException {
		String code = createAuthCode();
		Email from = new Email("maeda.kanta@moneyforward.co.jp");
	    String mailSubject = "Authentication Email";
	    Email to = new Email(email);
	    Content mailContent = new Content("text/plain",
	    		"認証コードをお送りします。\n\n"
	    		+ code + "\n\n"
	    		+ "アプリの方で認証コードを入力し、\n"
	    		+ "アカウントを有効にしてください。"
	    		+ "なお30分以内に認証しない場合は失効します");
	    Mail mail = new Mail(from, mailSubject, to, mailContent);
	    String sendGridAPI = System.getenv("SENDGRID_API_KEY");

	    SendGrid sg = new SendGrid(sendGridAPI);
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

	private String createAuthCode() {
		int FINAL_NUMBER = 4;
		String code = "";
		for (int i=0; i<FINAL_NUMBER; i++) {
			code += (int)(Math.random() * 9) + 1;
		}
		return code;
	}
}
