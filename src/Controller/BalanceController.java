package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Balance;
import model.User;
/**
 * Servlet implementation class Balance
 */
public class BalanceController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doIt(request, response);
		} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			doIt(request, response);
		} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void doIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		String mode = (String)request.getParameter("mode");
		if (mode == null) {
			Balance balance = new Balance();
			User user = fetchUserInfo();
			balance = fetchTotalBalance(balance);
	//		balance = fetchEachBalance(balance);
			HttpSession session = request.getSession();
			session.setAttribute("balance", balance);
			session.setAttribute("user", user);
			request.getRequestDispatcher(request.getContextPath()+"/balance.jsp")
					.forward(request, response);
		} else {
			//セッションの全削除
			HttpSession session = request.getSession();
			Enumeration<String> en = session.getAttributeNames();
			while(en.hasMoreElements()){
			  String attributeName = (String)en.nextElement();
			  session.removeAttribute(attributeName);
			}
			//アプリケーションスコープの全削除
			ServletContext sc = getServletContext();
			en = sc.getAttributeNames();
			while(en.hasMoreElements()) {
				String attributeName = (String)en.nextElement();
				sc.removeAttribute(attributeName);
			}
			response.sendRedirect(request.getContextPath() + "/");
		}
	}

	private Balance fetchTotalBalance(Balance balance) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		ServletContext sc = getServletContext();
		int uid = (int)sc.getAttribute("uid");
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT SUM(price) AS total FROM household "
					+ "INNER JOIN users ON household.user_id = users.id "
					+ "WHERE users.id = " + uid
//					+ "INNER JOIN family ON users.family_id = family.id"
					+ ";");
		) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int total = rs.getInt("total");
				balance.setTotalBalance(total);
			} else {
				return balance;
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return balance;
	}

	private Balance fetchEachBalance(Balance balance) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT SUM(price) AS total, users.name AS name FROM household "
					+ "INNER JOIN users ON household.user_id = users.id "
					+ "INNER JOIN relationship ON users.relationship_id = relationship.id "
					+ "GROUP BY users.id;");
		) {
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				balance.addEachBalance(rs.getInt("total"), rs.getString("name"));
			}
			return balance;
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return balance;
	}

	private User fetchUserInfo() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String dbUser = System.getenv("HEROKU_DB_USER");
		String dbPassword = System.getenv("HEROKU_DB_PASSWORD");
		ServletContext sc = getServletContext();
		User user = null;
		int uid = (int)sc.getAttribute("uid");
		try (
			Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
		) {
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String name = rs.getString("name");
				int relationshipId = rs.getInt("relationship_id");
				int familyId = rs.getInt("family_id");
				boolean emailCertificate = rs.getBoolean("email_certificate");
				user = new User(uid, name, relationshipId, familyId, emailCertificate);
			}
			return user;
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return user;
	}
}
