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
	 * 訪問時とログアウトクリック時に呼ばれる
	 * 訪問時にはMySQLから残高情報を取得する
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mode = (String)request.getParameter("mode");
		//ログアウト判定
		if (mode == null) {
			// 訪問時
			// セッションからユーザ情報と家族情報を取得する
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			Family family = (Family) session.getAttribute("family");
			if (user.getId() == -1) {
				// ユーザIDが存在しない場合はログイン画面へ遷移
				response.sendRedirect(request.getContextPath() + "/");
			} else {
				ArrayList<User> userList = new ArrayList<User>();
				try {
					if (family.getId() == -1) {
						// 家族と連携していない場合はアクセスしているユーザのみの残高を取得
						userList.add(getPersonalBalance(user));
					} else {
						// 家族と連携中の場合は家族全員分の残高を取得
						userList.addAll(getWholeFamilyBalance(family.getId()));
					}
				} catch (ClassNotFoundException | SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				// 合計残高の計算
				int totalBalance = 0;
				for (User v : userList) {
					totalBalance += v.getBalance();
				}
				userList.add(0, new User(-1, "合計", family.getId(), false, totalBalance));
				// セッションに残高情報を保存
				session.setAttribute("userList", userList);
				request.getRequestDispatcher(request.getContextPath()+"/balance.jsp")
						.forward(request, response);
			}
		} else {
			// ログアウトクリック
			logout(request, response);
		}
	}

	/**
	 * ログアウトするメソッド
	 * 全セッション情報を削除し、ログイン画面へ遷移する
	 * @param request
	 * @param response
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 */
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//セッションの全削除
		HttpSession session = request.getSession();
		Enumeration<String> en = session.getAttributeNames();
		while(en.hasMoreElements()){
		  String attributeName = (String)en.nextElement();
		  session.removeAttribute(attributeName);
		}
		//ログイン画面へ遷移
		response.sendRedirect(request.getContextPath() + "/");
	}

	/**
	 * 個人の残高を取得するメソッド
	 * @param user 取得したいユーザ情報
	 * @return 取得したユーザ情報
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 */
	private User getPersonalBalance(User user) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String dbUser = System.getenv("HEROKU_DB_USER");
		String dbPassword = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
			PreparedStatement ps = conn.prepareStatement("SELECT SUM(balance) AS balance FROM financial WHERE user_id = ? AND target = true AND publish = true;");
		) {
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				// ユーザに残高情報を追加
				user.setBalance(rs.getInt("balance"));
			}
			return user;
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return user;
	}

	/**
	 * 家族全員の残高を取得するメソッド
	 * @param user 取得したい家族ID
	 * @return 取得したユーザ情報をリスト
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 */
	private List<User> getWholeFamilyBalance(int familyId) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String dbUser = System.getenv("HEROKU_DB_USER");
		String dbPassword = System.getenv("HEROKU_DB_PASSWORD");
		List<User> balanceList = new ArrayList<User>();
		try (
			Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
			PreparedStatement ps = conn.prepareStatement("SELECT SUM(balance) AS balance, users.id, users.name "
					+ "FROM financial "
					+ "RIGHT JOIN users ON users.id = financial.user_id "
					+ "WHERE users.family_id = ? AND target = true AND publish = true "
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
