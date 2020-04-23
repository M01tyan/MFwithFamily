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

	public void send(String email) throws IOException {
		Email from = new Email("maeda.kanta@moneyforward.co.jp");
	    String mailSubject = "Sending with Twilio SendGrid is Fun";
	    Email to = new Email(email);
	    Content mailContent = new Content("text/plain", "and easy to do anywhere, even with Java");
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
	}
}
