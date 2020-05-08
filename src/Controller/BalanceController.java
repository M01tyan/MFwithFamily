package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Family;
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
		//ログアウト判定
		if (mode == null) {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			Family family = (Family) session.getAttribute("family");
			System.out.println("<HOME画面>\nuid: " + user.getId() + " name: " + user.getName() + " familyId: " + family.getId());
			if (user.getId() == -1) {
				//ログイン画面へ遷移
				response.sendRedirect(request.getContextPath() + "/");
			} else {
//				user = getUser(user);
				ArrayList<User> userList = new ArrayList<User>();
				if (family.getId() == -1) {
					userList.add(getPersonalBalance(user));
				} else {
					userList.addAll(getWholeFamilyBalance(family.getId()));
				}
				int totalBalance = 0;
				for (User v : userList) {
					totalBalance += v.getBalance();
				}
				userList.add(new User(-1, "合計", -1, family.getId(), false, totalBalance));
				for (User v : userList) {
					System.out.println(v.getName() + " : " + v.getBalance());
				}
				session.setAttribute("user", user);
				session.setAttribute("userList", userList);
				request.getRequestDispatcher(request.getContextPath()+"/balance.jsp")
						.forward(request, response);
			}
		} else {
			logout(request, response);
		}
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		//ログイン画面へ遷移
		response.sendRedirect(request.getContextPath() + "/");
	}

	private User getUser(User user) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String dbUser = System.getenv("HEROKU_DB_USER");
		String dbPassword = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
			PreparedStatement ps = conn.prepareStatement("SELECT name, family_id, relationship_id FROM users WHERE id = ?");
		) {
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user.setName(rs.getString("name"));
				user.setRelationshipId(rs.getInt("relationship_id"));
				user.setFamilyId(rs.getInt("family_id"));
			}
			return user;
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return user;
	}

	private User getPersonalBalance(User user) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String dbUser = System.getenv("HEROKU_DB_USER");
		String dbPassword = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
			PreparedStatement ps = conn.prepareStatement("SELECT SUM(price) AS balance FROM household WHERE user_id = ?");
		) {
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user.setBalance(rs.getInt("balance"));
			}
			return user;
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return user;
	}

	private List<User> getWholeFamilyBalance(int familyId) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String dbUser = System.getenv("HEROKU_DB_USER");
		String dbPassword = System.getenv("HEROKU_DB_PASSWORD");
		List<User> balanceList = new ArrayList<User>();
		try (
			Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
			PreparedStatement ps = conn.prepareStatement("SELECT COALESCE(SUM(price), 0) AS balance, users.id AS id, name "
					+ "FROM household "
					+ "RIGHT JOIN users ON users.id = household.user_id "
					+ "WHERE users.family_id = ? "
					+ "GROUP BY user_id;");
		) {
			ps.setLong(1, familyId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setBalance(rs.getInt("balance"));
				user.setName(rs.getString("name"));
				balanceList.add(user);
			}
			return balanceList;
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return balanceList;
	}
}
