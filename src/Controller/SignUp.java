package Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Component.SendMail;
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SendMail sendMail = new SendMail();
		sendMail.send("kanta01m.tyan@gmail.com");
		try {
			doIt(request, response);
		} catch (ServletException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String message = "";
		String email = (String)request.getParameter("email");
		String password = (String)request.getParameter("password");
		String confirmation = (String)request.getParameter("confirmation");
		if (!email.contains("@")) message += "メールアドレスを入力してください<BR>";
		if (!password.equals(confirmation)) message += "パスワードが一致しません<BR>";
		if (message.isEmpty()) {
			request.getRequestDispatcher("/balance").forward(request, response);
		} else {
			request.setAttribute("message", message);
			request.getRequestDispatcher("/signUp.jsp").forward(request, response);
		}
	}

	private void doIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/signUp.jsp")
			.forward(request, response);
	}
}
