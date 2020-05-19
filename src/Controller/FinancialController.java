package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Family;
import model.Financial;
import model.User;

public class FinancialController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 訪問時に実行されるメソッド
	 * セッションからユーザ情報、家族情報、家族の個別リストを受け取る
	 * パラメータからクリックしたidを受け取る
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 情報の取得
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Family family = (Family) session.getAttribute("family");
		List<User> userList = (List<User>) session.getAttribute("userList");
		int id = Integer.parseInt(request.getParameter("id"));

		try {
			// 家族全員のor個別の口座情報を取得
			List<Financial> financialList = id == 0 ? getFamilyFinancial(family.getId(), user.getId()) : getPersonalFinancial(userList.get(id).getId());
			// セッションに口座情報を保存
			session.setAttribute("financialList", financialList);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		// 受け取ったidをそのままリクエストに保存
		request.setAttribute("id", id);
		request.getRequestDispatcher(request.getContextPath()+"/financial.jsp")
			.forward(request, response);

	}

	/**
	 * 新しい口座情報を追加するときに呼ばれるメソッド
	 * 口座フォームから口座名、残高、公開フラグを受け取り、MySQLへ追加
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 口座フォームから情報を取得
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		// 追加できるのは本人のみなのでセッションからアクセスしているユーザ情報を取得
		User user = (User) session.getAttribute("user");
		// 口座フォームから、口座名、残高、公開フラグを取得
		String name = request.getParameter("name");
		int balance = Integer.parseInt(request.getParameter("balance"));
		boolean publish = Boolean.valueOf(request.getParameter("publish"));
		// 再リダイレクトのためのidを取得
		String id = request.getParameter("id");
		try {
			// MySQLへ口座情報を追加
			addFinancial(name, balance, user.getId(), publish);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		// 再リダイレクト
		response.sendRedirect(request.getContextPath()+"/financial?id="+id);
	}

	/**
	 * 口座情報を削除するときに呼ばれるメソッド
	 * 削除する口座のidを取得し、MySQLから口座IDが同じものを削除
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// セッションからユーザの口座情報を取得
		HttpSession session = request.getSession();
		List<Financial> financialList = (List<Financial>) session.getAttribute("financialList");
		// パラメータからクリックしたidを取得
		int id = Integer.parseInt(request.getParameter("index"));
		try {
			// 口座情報から口座IDを取得し、MySQL上の同じ口座IDのものを削除
			int financialId = financialList.get(id).getId();
			deleteFinancial(financialId);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 * 個別のユーザの口座情報を取得するメソッド
	 * MySQLからuidの口座情報を取得する
	 * @param uid ユーザID
	 * @return MySQLから取得したユーザの口座情報
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 */
	private List<Financial> getPersonalFinancial(int uid) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		List<Financial> financial = new ArrayList<Financial>();
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT financial.id AS id, users.id AS user_id, users.name AS user_name, financial.name AS financial_name, balance, publish "
					+ "FROM users "
					+ "INNER JOIN financial ON users.id = financial.user_id "
					+ "WHERE users.id = ? AND financial.target = true;");
		) {
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// 取得した口座がアクセスしているユーザのものなら"あなた"に変更
				String user_name = rs.getInt("user_id") == uid ? "あなた" : rs.getString("user_name");
				financial.add(new Financial(rs.getInt("id"), rs.getString("financial_name"), user_name, rs.getInt("user_id"), rs.getInt("balance"), rs.getBoolean("publish")));
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return financial;
	}

	/**
	 * 家族全員の口座情報を取得
	 * @param familyId 家族ID
	 * @param uid ユーザID
	 * @return MySQLから取得した家族全員の口座情報
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 */
	private List<Financial> getFamilyFinancial(int familyId, int uid) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		List<Financial> financial = new ArrayList<Financial>();
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT financial.id AS id, users.id AS user_id, users.name AS user_name, financial.name AS financial_name, balance, publish "
					+ "FROM users "
					+ "INNER JOIN financial ON users.id = financial.user_id "
					+ "WHERE CASE ? WHEN -1 THEN users.id = ? ELSE family_id = ? END "
					+ "AND financial.target = true;");
		) {
			ps.setInt(1, familyId);
			ps.setInt(2, uid);
			ps.setInt(3, familyId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// 取得した口座がアクセスしているユーザのものなら"あなた"に変更
				String user_name = rs.getInt("user_id") == uid ? "あなた" : rs.getString("user_name");
				financial.add(new Financial(rs.getInt("id"), rs.getString("financial_name"), user_name, rs.getInt("user_id"), rs.getInt("balance"), rs.getBoolean("publish")));
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return financial;
	}

	/**
	 * 新しい口座情報をMySQLに追加するメソッド
	 * 口座フォームから受け取った値をMySQLのfinancialテーブルに追加する
	 * なお、自分の口座しか追加することはできない
	 * @param name 口座名
	 * @param balance 残高
	 * @param uid 追加するユーザID
	 * @param publish 公開フラグ
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 */
	private void addFinancial(String name, int balance, int uid, boolean publish) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps = conn.prepareStatement("INSERT INTO financial "
							+ "(`name`, `balance`, `user_id`, `publish`) "
							+ "VALUES (?, ?, ?, ?);");
		) {
			ps.setString(1, name);
			ps.setInt(2, balance);
			ps.setInt(3, uid);
			ps.setBoolean(4, publish);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
	}

	/**
	 * 口座を削除するメソッド
	 * クリックした口座IDからMySQL上のfinancialテーブルのtarget要素をfalseに変更
	 * @param financialId 削除する口座ID
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 */
	private void deleteFinancial(int financialId) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps = conn.prepareStatement("UPDATE financial SET target = false WHERE id = ?;");
		) {
			ps.setInt(1, financialId);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
	}
}
