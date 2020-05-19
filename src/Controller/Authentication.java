package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
/**
 * Servlet implementation class Authentication
 */
public class Authentication extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 訪問時に呼ばれるメソッド
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(request.getContextPath()+"/auth.jsp")
			.forward(request, response);
	}

	/**
	 * 認証コードを入力した時に呼ばれるメソッド
	 * セッションい保存された認証コードと入力された認証コードが正しいかをチェックし、正しい場合は新規登録を完了させる
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// DB接続
		response.setContentType("text/html; charset=UTF-8");
		HttpSession session = request.getSession();
		String inputAuthCode = (String)request.getParameter("authCode");
		String sessionAuthCode = (String)session.getAttribute("sessionAuthCode");
		String message = "";
		if (sessionAuthCode.equals(inputAuthCode)) {
			// 認証コードが正しい場合MySQLのメール認証フラグをtrueに変更し、残高画面へ遷移
			System.out.println("Authentication Success!!");
			try {
				User user = (User) session.getAttribute("user");
				user = updateEmailCertificate(user);
				session.setAttribute("user", user);
				response.sendRedirect(request.getContextPath()+"/balance");
			} catch (ClassNotFoundException | SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} else {
			// 認証コードが異なる場合、エラーメッセージを表示
			message += "認証コードが違います";
			request.setAttribute("message", message);
			request.getRequestDispatcher("/auth.jsp").forward(request, response);
		}
	}

	/**
	 * MySQLのメール認証フラグをtrueにするメソッド
	 * @param user アクセスしているユーザ情報
	 * @return 更新されたユーザ情報
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 */
	private User updateEmailCertificate(User user) throws SQLException, ClassNotFoundException{
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("UPDATE users SET email_certificate=true WHERE id = ?;");
		) {
			ps.setInt(1, user.getId());
			ps.executeUpdate();
			user.setEmailCertificate(true);
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return user;
	}
}
