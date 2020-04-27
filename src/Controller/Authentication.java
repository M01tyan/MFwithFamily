package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
		request.getRequestDispatcher(request.getContextPath()+"/auth.jsp")
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
		String email = (String)session.getAttribute("email");
		String password = (String)session.getAttribute("password");
		String message = "";
		if (code.equals(inputCode)) {
			System.out.println("Authentication Success!!");
			try {
				createUser(email, password);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			response.sendRedirect(request.getContextPath()+"/balance");
		} else {
			message += "認証コードが違います";
			request.setAttribute("message", message);
			request.getRequestDispatcher("/auth.jsp").forward(request, response);
		}
	}

	private void createUser(String email, String password) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		String secretKey = System.getenv("SECRET_KEY");
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("INSERT INTO users "
							+ "(`email`, `password`) "
							+ "VALUES (AES_ENCRYPT(?, ?), AES_ENCRYPT(?, ?));");
		) {
			ps.setString(1, email);
			ps.setString(2, secretKey);
			ps.setString(3, password);
			ps.setString(4, secretKey);
			int nums = ps.executeUpdate();
			System.out.println(nums);
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
	}
}
