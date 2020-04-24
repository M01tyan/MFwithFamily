package Controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Component.AESCipher;
/**
 * Servlet implementation class Authentication
 */
@WebServlet("/Authentication")
public class Authentication extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Authentication() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/auth.jsp")
			.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=UTF-8");
		HttpSession session = request.getSession();
		String inputCode = (String)request.getParameter("authCode");
		String code = (String)session.getAttribute("code");
//		String email = (String)session.getAttribute("email");
//		String password = (String)session.getAttribute("password");
		String email = "kanta01m.tyan@gmail.com";
		String password = "No.1runner";
		String message = "";
//		if (code.equals(inputCode)) {
			System.out.println("Authentication Success!!");
			try {
				createUser(email, password);
			} catch (ClassNotFoundException | SQLException | GeneralSecurityException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
//			response.sendRedirect("/MFwithFamily/balance");
//		} else {
//			message += "認証コードが違います";
//			request.setAttribute("message", message);
//			request.getRequestDispatcher("/auth.jsp").forward(request, response);
//		}
	}

	private void createUser(String email, String password) throws SQLException, ClassNotFoundException, GeneralSecurityException {
		try {
			AESCipher cipher = new AESCipher();
			SecretKey secretKey = cipher.decodeSecretKey(System.getenv("SECRET_KEY"));
			IvParameterSpec iv = cipher.decodeIvParameter(System.getenv("IV_PARAMETER"));
			byte[] encryptEmail = cipher.encrypto(email, secretKey, iv);
			byte[] encryptPassword = cipher.encrypto(password, secretKey, iv);
			String decryptEmail = cipher.decrypto(encryptEmail, secretKey, iv);
			String decryptPassword = cipher.decrypto(encryptPassword, secretKey, iv);
		} catch(GeneralSecurityException e) {
			System.out.println(e);
		}
//
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?verifyServerCertificate=false&useSSL=true";
//		String user = System.getenv("HEROKU_DB_USER");
//		String password = System.getenv("HEROKU_DB_PASSWORD");
//		Connection conn = DriverManager.getConnection(url, user, password);
//		try {
//			PreparedStatement ps =
//					conn.prepareStatement("INSERT INO");
//			try {
//				ResultSet rs = ps.executeQuery();
//				if (rs.next()) {
//
//				} else {
//
//				}
//			} catch (SQLException e) {
//				System.out.println("SQL ERROR: " + e);
//			} finally {
//				if (ps != null) {
//					try {
//						ps.close();
//					} catch (SQLException e) {
//						System.out.println("PreparedStatementのクローズに失敗しました。");
//					}
//				}
//			}
//		} finally {
//			if (conn != null) {
//				try {
//					conn.close();
//				} catch (SQLException e) {
//					System.out.println("MySQLのクローズに失敗しました。");
//				}
//			}
//		}
	}
}
