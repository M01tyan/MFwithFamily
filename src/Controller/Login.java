package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Component.SendMail;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher(request.getContextPath() + "/login.jsp")
				.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=UTF-8");
		String message = "";
		String email = (String) request.getParameter("email");
		String password = (String) request.getParameter("password");
		try {
			ServletContext application = getServletContext();
			login(email, password);
			int uid = (int) application.getAttribute("uid");
			Boolean emailCertificate = (Boolean) application.getAttribute("emailCertificate");
			if (uid != -1) {
				//メール認証済みかチェック
				System.out.println("ログインしました！\nuid: " + uid);
				if (emailCertificate != false) {
					response.sendRedirect(request.getContextPath() + "/balance");
				} else {
					//認証コードの生成＆メール送信 -> 認証画面へ遷移
					HttpSession session = request.getSession();
					String sessionAuthCode = (String)session.getAttribute("code");
					if (sessionAuthCode == null) {
						SendMail sendMail = new SendMail();
						sessionAuthCode = sendMail.send(email);
						System.out.println("認証コード: " + sessionAuthCode);
						session.setAttribute("sessionAuthCode", sessionAuthCode);
					}
					response.sendRedirect(request.getContextPath() + "/auth");
				}
			} else {
				message += "ログインできませんでした";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/login.jsp").forward(request, response);
//				response.sendRedirect(request.getContextPath() + "/");
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void login(String email, String password) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL")
				+ "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBuser = System.getenv("HEROKU_DB_USER");
		String DBpassword = System.getenv("HEROKU_DB_PASSWORD");
		String secretKey = System.getenv("SECRET_KEY");
		try (
			Connection conn = DriverManager.getConnection(url, DBuser, DBpassword);
			PreparedStatement ps = conn.prepareStatement("SELECT "
					+ "id, "
					+ "email_certificate, "
					+ "CONVERT(AES_DECRYPT(email, ?) USING utf8) AS email, "
					+ "CONVERT(AES_DECRYPT(password, ?) USING utf8) AS password "
					+ "FROM users "
					+ "WHERE email=HEX(AES_ENCRYPT(?, ?)) AND password=HEX(AES_ENCRYPT(?, ?));");
		) {
			ps.setString(1, secretKey);
			ps.setString(2, secretKey);
			ps.setString(3, email);
			ps.setString(4, secretKey);
			ps.setString(5, password);
			ps.setString(6, secretKey);
			ResultSet rs = ps.executeQuery();
			ServletContext application = getServletContext();
			if (rs.next()) {
				application.setAttribute("uid", (int)rs.getInt("id"));
				application.setAttribute("emailCertificate", (boolean)rs.getBoolean("email_certificate"));
			} else {
				application.setAttribute("uid", -1);
				application.setAttribute("emailCertificate", false);
				System.out.println("NOT USER");
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
	}
}
