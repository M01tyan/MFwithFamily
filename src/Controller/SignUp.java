package Controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Component.SendMail;
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		HttpSession session = request.getSession();
		if (!emailValidation(email)) message += "正しいメールアドレスを入力してください<BR>";
		if (!passwordValidation(password)) message += "パスワードは半角英数字で入力してください<BR>";
		if (!password.equals(confirmation)) message += "パスワードが一致しません<BR>";
		if (message.isEmpty()) {
			SendMail sendMail = new SendMail();
			String code = sendMail.send(email);
			session.setAttribute("email", email);
			session.setAttribute("password", password);
			session.setAttribute("code", code);
			System.out.println(code);
			response.sendRedirect(request.getContextPath()+"/auth");
		} else {
			request.setAttribute("message", message);
			request.getRequestDispatcher(request.getContextPath()+"/signUp.jsp").forward(request, response);
		}
	}

	private void doIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/signUp.jsp")
			.forward(request, response);
	}

	private boolean emailValidation(String email) {
		String pattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	    Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(email);
	    return m.find();
	}

	private boolean passwordValidation(String password) {
		String pattern = "^[0-9a-zA-Z.-_@!&#$%]{6,20}";
	    Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(password);
	    return m.find();
	}
}
