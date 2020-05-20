package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Family;
import model.User;

/**
 * Servlet implementation class ShareController
 */
public class ShareController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 訪問時と家族コードの生成時に実行
	 * 訪問時 家族情報の取得＆セッションに保存
	 * 家族コードの生成時 家族コードの生成&MySQLへの家族情報の追加
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean clickCreateButton = request.getParameter("createShareCode") != null ? true : false;
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user.getId() != -1) {
			//ユーザ情報がある場合
			if (!clickCreateButton) {
				//訪問時
				try {
					//家族情報の取得＆セッションに保存
					Family family = getFamily(user);
					session.setAttribute("family", family);
				} catch (ClassNotFoundException | SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				request.getRequestDispatcher(request.getContextPath()+"/share.jsp")
				   .forward(request, response);
			} else {
				//家族コード生成ボタンクリックした場合
				String shareCode = "";
				try {
					//家族コードの生成&UNIQUEなKEYかチェック
					do {
						shareCode = createShareCode();
					} while (!checkUniqueShareCode(shareCode));
					//MySQLに新しい家族コードの追加&セッション保存
					Family family = createFamily(shareCode);
					setFamilyId(family.getId(), user.getId());
					family.setUserList(new ArrayList<User>(Arrays.asList(user)));
					session.setAttribute("family", family);
					response.sendRedirect(request.getContextPath() + "/share");
				} catch (ClassNotFoundException | SQLException e) {
					// エラーコードの表示
					e.printStackTrace();
					String message = "共有コードの発行に失敗しました。\nもう一度お願いします";
					request.setAttribute("message", message);
					request.getRequestDispatcher("/share.jsp").forward(request, response);
				}
			}
		} else {
			//ユーザ情報がない場合ログイン画面へ遷移
			response.sendRedirect(request.getContextPath()+"/");
		}
	}

	/**
	 * 家族コード入力時に呼ばれる
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 入力された家族コードを受けとる
		String inputShareCode = (String) request.getParameter("inputShareCode");
		// セッションからユーザ情報、家族情報の取得
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Family family = (Family) session.getAttribute("family");
		try {
			// 入力された家族コードから家族情報を取得
			family = checkShareCode(inputShareCode, user);
			if (family.getUserList() != null) {
				// 家族コードが正しければMySQL上のusersテーブルのfamily_idを変更
				setFamilyId(family.getId(), user.getId());
			} else {
				// 家族コードが正しくない場合はエラーを表示
				String message = "共有コードが違います";
				request.setAttribute("message", message);
			}
			// セッションに家族情報を保存
			session.setAttribute("family", family);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		response.sendRedirect(request.getContextPath() + "/share");
	}

	/**
	 * 家族メンバーを削除する際に呼ばれる
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 削除するユーザのパラメータを受け取る
		int index = Integer.parseInt((String)request.getParameter("id"));
		// セッションから家族情報と家族の個別情報をリストで取得
		HttpSession session = request.getSession();
		Family family = (Family) session.getAttribute("family");
		List<User> userList = family.getUserList();
		try {
			// MySQLで家族連携を解除
			releaseFamily(userList.get(index).getId());
			userList.remove(index);
			family.setUserList(userList);
			session.setAttribute("family", family);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		String message = "success";
		String responseJson = "{\"message\":\"" + message + "\"}";
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(responseJson);
	}

	/**
	 * 家族コードの生成メソッド
	 * 8文字のランダムな半角英数字を作成
	 * @return 生成されたランダムな8文字英数字
	 */
	private String createShareCode() {
		Random r = new Random();
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890123456789";

		int shareCodeLength = 8;
		String shareCode = "";

		for (int i=0; i<shareCodeLength; i++) {
			shareCode += alphabet.charAt(r.nextInt(alphabet.length()));
		}
		return shareCode;
	}

	/**
	 * 家族コードがUNIQUEかをチェックするメソッド
	 * MySQL上に保存されている家族コードと重複しないかを判定
	 * @param shareCode 家族コード
	 * @return 判定結果 (true: 重複なし, false: 重複あり)
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private boolean checkUniqueShareCode(String shareCode) throws ClassNotFoundException, SQLException {
		// DBへの接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT * FROM family WHERE auth_code = ?");
		) {
			ps.setString(1, shareCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println("NOT UNIQUE SHARE CODE!!!");
				return false;
			} else {
				System.out.println("UNIQUE SHARE CODE!");
				return true;
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return false;
	}

	/**
	 * 新しい家族を生成するメソッド
	 * MySQL上のfamilyテーブルに新しい家族を生成
	 * @param shareCode 家族コード
	 * @return 新しく生成された家族クラス
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private Family createFamily(String shareCode) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		Family family = new Family();
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("INSERT INTO family "
							+ "(`auth_code`) "
							+ "VALUES (?);", Statement.RETURN_GENERATED_KEYS);
		) {
			ps.setString(1, shareCode);
			ps.executeUpdate();
			//AUTO_INCREMENTで生成された家族IDを取得
			ResultSet res = ps.getGeneratedKeys();
			if(res.next()) {
				family.setId(res.getInt(1));
				family.setShareCode(shareCode);
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return family;
	}

	/**
	 * MySQL上のusersテーブルに家族コードを追加するメソッド
	 * @param familyId
	 * @param uid
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void setFamilyId(int familyId, int uid) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("UPDATE users SET family_id = ? WHERE id = ?;");
		) {
			ps.setInt(1, familyId);
			ps.setInt(2, uid);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
	}

	/**
	 * 入力された家族コードが正しいかを判定するメソッド
	 * MySQL上のfamilyテーブルに保存されている家族コードを確認し、家族コードが同じユーザの情報を取得
	 * @param shareCode 入力された家族コード
	 * @param user 入力したユーザ情報
	 * @return 家族コードが同じ家族の情報
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private Family checkShareCode(String shareCode, User user) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		// ユーザリスト(家族の個別ユーザ情報)の初期化
		List<User> userList = new ArrayList<User>();
		// 家族クラスの初期化
		Family family = new Family();
		try (
			// 家族コードが同じユーザの情報とfamily_idを取得
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("SELECT users.id AS id, name, family_id, email_certificate "
							+ "FROM users "
							+ "INNER JOIN family ON users.family_id = family.id "
							+ "WHERE family.auth_code = ?;", Statement.RETURN_GENERATED_KEYS);
		) {
			ps.setString(1, shareCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id = (int) rs.getInt("id");
				String name = (String) rs.getString("name");
				int familyId = (int) rs.getInt("family_id");
				boolean emailCertificate = (Boolean) rs.getBoolean("email_certificate");
				User u = new User(id, name, familyId, emailCertificate, 0);
				// family_idの保存
				family.setId(familyId);
				userList.add(u);
				while (rs.next()) {
					id = (int) rs.getInt("id");
					name = (String) rs.getString("name");
					familyId = (int) rs.getInt("family_id");
					emailCertificate = (Boolean) rs.getBoolean("email_certificate");
					u = new User(id, name, familyId, emailCertificate, 0);
					userList.add(u);
				}
				//入力者の情報を追加
				userList.add(user);
				family.setShareCode(shareCode);
			}
			family.setUserList(userList);
			return family;
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return family;
	}

	/**
	 * 訪問時に家族情報を取得するメソッド
	 * @param user ユーザ情報
	 * @return 家族情報
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private Family getFamily(User user) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		List<User> userList = new ArrayList<User>();
		Family family = new Family();
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			// MySQL上のuidからfamily_idが同じユーザの情報を取得
			PreparedStatement ps =
					conn.prepareStatement("(SELECT @family_id := `family_id` AS family_id, users.id AS id, name, auth_code "
							+ "FROM users "
							+ "INNER JOIN family ON users.family_id = family.id "
							+ "WHERE users.id = ?) "
							+ "UNION "
							+ "(SELECT family_id, users.id AS id, name, auth_code "
							+ "FROM users "
							+ "INNER JOIN family ON users.family_id = family.id "
							+ "WHERE CASE @family_id "
							+ "WHEN -1 THEN users.id = ? "
							+ "ELSE family_id = @family_id END);");
		) {
			ps.setInt(1, user.getId());
			ps.setInt(2, user.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id = (int) rs.getInt("id");
				String name = (String) rs.getString("name");
				int familyId = (int) rs.getInt("family_id");
				String shareCode = (String) rs.getString("auth_code");
				User u = new User(id, name, familyId, true, 0);
				family.setId(familyId);
				family.setShareCode(shareCode);
				userList.add(u);
				while (rs.next()) {
					id = (int) rs.getInt("id");
					name = (String) rs.getString("name");
					familyId = (int) rs.getInt("family_id");
					u = new User(id, name, familyId, true, 0);
					userList.add(u);
				}
			}
			family.setUserList(userList);
			return family;
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return family;
	}

	/**
	 * 家族連携の解除メソッド
	 * MySQL上のuidのユーザを家族から解除する
	 * @param uid 解除するユーザID
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private void releaseFamily(int uid) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("UPDATE users SET family_id = -1 WHERE id = ?;");
		) {
			ps.setInt(1, uid);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
	}
}
