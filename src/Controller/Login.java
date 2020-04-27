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

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(request.getContextPath()+"/login.jsp")
			.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=UTF-8");
		String message = "";
		String email = (String)request.getParameter("email");
		String password = (String)request.getParameter("password");
		boolean loginSuccess = false;
		try {
			loginSuccess = authentication(email, password);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		if (loginSuccess) {
			response.sendRedirect("/MFwithFamily/balance");
		} else {
			message += "ログインできませんでした";
			request.setAttribute("message", message);
			request.getRequestDispatcher(request.getContextPath()+"/login.jsp").forward(request, response);
		}
	}

	private boolean authentication(String email, String password) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBuser = System.getenv("HEROKU_DB_USER");
		String DBpassword = System.getenv("HEROKU_DB_PASSWORD");
		String secretKey = System.getenv("SECRET_KEY");
		try (
			Connection conn = DriverManager.getConnection(url, DBuser, DBpassword);
			PreparedStatement ps =
			conn.prepareStatement("SELECT id, AES_DECRYPT(`email`, ?) AS email, AES_DECRYPT(`password`, ?) AS password FROM users WHERE email=AES_ENCRYPT(?, ?) AND password=AES_ENCRYPT(?, ?);");
		) {
			ps.setString(1, secretKey);
			ps.setString(2, secretKey);
			ps.setString(3, email);
			ps.setString(4, secretKey);
			ps.setString(5, password);
			ps.setString(6, secretKey);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				return true;
			} else {
				System.out.println("NOT USER");
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return false;
	}
}
