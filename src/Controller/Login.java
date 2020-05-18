package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Component.SendMail;
import model.Family;
import model.User;
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
			HttpSession session = request.getSession();
			User user = login(email, password, request);
			if (user.getId() != -1) {
				//メール認証済みかチェック
				System.out.println("ログインしました！\nuid: " + user.getId() + " name: " + user.getName());
				session.setAttribute("user", user);
				if (user.getEmailCertificate() != false) {
					response.sendRedirect(request.getContextPath() + "/balance");
				} else {
					//認証コードの生成＆メール送信 -> 認証画面へ遷移
					String sessionAuthCode = (String) session.getAttribute("code");
					if (sessionAuthCode == null) {
						StringBuffer path = request.getRequestURL();
						String[] url = path.toString().split("/");
						SendMail sendMail = new SendMail();
						sessionAuthCode = sendMail.send(email, url[2]);
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
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private User login(String email, String password, HttpServletRequest request)
			throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL")
				+ "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBuser = System.getenv("HEROKU_DB_USER");
		String DBpassword = System.getenv("HEROKU_DB_PASSWORD");
		String secretKey = System.getenv("SECRET_KEY");
		User user = new User();
		Family family = new Family();
		try (
				Connection conn = DriverManager.getConnection(url, DBuser, DBpassword);
				PreparedStatement ps = conn.prepareStatement("SELECT "
						+ "users.id AS id, "
						+ "name, "
						+ "email_certificate, "
						+ "family_id, "
						+ "family.auth_code AS share_code, "
						+ "CONVERT(AES_DECRYPT(email, ?) USING utf8) AS email, "
						+ "CONVERT(AES_DECRYPT(password, ?) USING utf8) AS password "
						+ "FROM users "
						+ "INNER JOIN family ON users.family_id = family.id "
						+ "WHERE email=HEX(AES_ENCRYPT(?, ?)) AND password=HEX(AES_ENCRYPT(?, ?));");) {
			ps.setString(1, secretKey);
			ps.setString(2, secretKey);
			ps.setString(3, email);
			ps.setString(4, secretKey);
			ps.setString(5, password);
			ps.setString(6, secretKey);
			ResultSet rs = ps.executeQuery();
			HttpSession session = request.getSession();
			if (rs.next()) {
				user.setId((int) rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setEmailCertificate((boolean) rs.getBoolean("email_certificate"));
				family.setId((int) rs.getInt("family_id"));
				family.setShareCode((String) rs.getString("share_code"));
				session.setAttribute("family", family);
			} else {
				System.out.println("NOT USER");
			}
			return user;
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return user;
	}
}
