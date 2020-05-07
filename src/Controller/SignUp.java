package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mashape.unirest.http.exceptions.UnirestException;

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
			String authCode = "";
			try {
				authCode = sendMail.send(email);
			} catch (UnirestException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			try {
				int uid = createUser(email, password);
				if (uid == -1) {
					message += "もうすでに登録されたメールアドレスです";
					request.setAttribute("message", message);
					request.getRequestDispatcher(request.getContextPath()+"/signUp.jsp").forward(request, response);
				} else {
					ServletContext sc = getServletContext();
					sc.setAttribute("uid", uid);
					session.setAttribute("sessionAuthCode", authCode);
					System.out.println("認証コード: " + authCode);
					response.sendRedirect(request.getContextPath()+"/auth");
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

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

	private int createUser(String email, String password) throws SQLException, ClassNotFoundException {
		System.out.println(email + " : " + password);
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		String secretKey = System.getenv("SECRET_KEY");
		int uid = -1;
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("INSERT INTO users "
							+ "(`email`, `password`) "
							+ "VALUES (HEX(AES_ENCRYPT(?, ?)), HEX(AES_ENCRYPT(?, ?)));", Statement.RETURN_GENERATED_KEYS);
		) {
			ps.setString(1, email);
			ps.setString(2, secretKey);
			ps.setString(3, password);
			ps.setString(4, secretKey);
			ps.executeUpdate();
			ResultSet res = ps.getGeneratedKeys();
			if(res.next()) {
				uid = res.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return uid;
	}
}
