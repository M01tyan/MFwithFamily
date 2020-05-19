package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Family;
import model.Financial;
import model.Household;
import model.User;

/**
 * Servlet implementation class List
 */
public class HouseholdController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * 訪問時
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// セッションからユーザ情報と家族情報を取得
		HttpSession session = request.getSession();
		Family family = (Family) session.getAttribute("family");
		User user = (User) session.getAttribute("user");
		try {
			// 家族全体の口座を取得
			List<Financial> financialList = getFinancial(family.getId(), user.getId());
			// 家族全体の家計簿を取得
			List<Household> householdList = getHousehold(family.getId(), user.getId());
			// セッションに口座情報と家計簿を保存
			session.setAttribute("financialList", financialList);
			session.setAttribute("householdList", householdList);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		request.getRequestDispatcher(request.getContextPath()+"/household.jsp")
		.forward(request, response);
	}

	/**
	 * 新しく家計簿をつけた際に呼ばれる
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 入力フォームから新しい家計簿の情報を取得
		request.setCharacterEncoding("utf-8");
		String date = request.getParameter("date");
		int price = Integer.parseInt(request.getParameter("price"));
		int financialId = Integer.parseInt(request.getParameter("financialId"));
		boolean isTransfer = Boolean.valueOf(request.getParameter("isTransfer"));
		int transferId = Integer.parseInt(request.getParameter("transferId"));
		String content = request.getParameter("content");
		String largeItem = request.getParameter("largeItem");
		String middleItem = request.getParameter("middleItem");
		String memo = request.getParameter("memo");
		int sourceUid = Integer.parseInt(request.getParameter("sourceUid"));
		int transferUid = Integer.parseInt(request.getParameter("transferUid"));
		try {
			if (!isTransfer) {
				// 支出の場合
				recordExpense(date, content, -price, financialId, largeItem, middleItem, memo, isTransfer, sourceUid);
			} else {
				// 振替の場合
				recordTransfer(date, content, price, financialId, transferId, memo, isTransfer, sourceUid, transferUid);
			}
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("ERROR: " + e);
		}
		// MySQLへの保存完了後再レンダリング
		response.sendRedirect(request.getContextPath()+"/household");
	}

	/**
	 * 支出の記録するメソッド
	 * MySQLのhouseholdテーブルへ新しい支出を登録&口座残高の更新をトランザクション処理する
	 * @param date 日付
	 * @param content 内容
	 * @param price 値段
	 * @param financialId 口座ID
	 * @param largeItem 大項目
	 * @param middleItem 中項目
	 * @param memo メモ
	 * @param isTransfer 振替フラグ
	 * @param uid 支払者のuid
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private void recordExpense(String date, String content, int price, int financialId, String largeItem, String middleItem, String memo, boolean isTransfer, int uid) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		String householdId = "";
		// UNIQUEな家計簿IDの生成
		do {
			householdId = createHouseholdId();
		} while (!checkUniqueHouseholdId(householdId));
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			// 家計簿を記録
			PreparedStatement ps1 = conn.prepareStatement("INSERT INTO household "
							+ "(`date`, `content`, `price`, `financial_id`, `large_item`, `middle_item`, `memo`, `transfer`, `id`, `user_id`) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			// 口座残高の更新
			PreparedStatement ps2 = conn.prepareStatement("UPDATE financial SET balance = balance + ? WHERE id = ?;");
		) {
			conn.setAutoCommit(false);

			// 家計簿
			ps1.setString(1, date);
			ps1.setString(2, content);
			ps1.setInt(3, price);
			ps1.setInt(4, financialId);
			ps1.setString(5, largeItem);
			ps1.setString(6, middleItem);
			ps1.setString(7, memo);
			ps1.setBoolean(8, isTransfer);
			ps1.setString(9, householdId);
			ps1.setInt(10, uid);
			ps1.executeUpdate();

			//口座
			ps2.setInt(1, price);
			ps2.setInt(2, financialId);
			ps2.executeUpdate();

			try {
		        //データベースに登録
		        conn.commit();
		        System.out.println("登録成功");
		    } catch (SQLException e) {
		        //ロールバック処理ですべての変更を取り消し
		        conn.rollback();
		        System.out.println("登録失敗：ロールバック実行");
		        e.printStackTrace();
		    }
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
	}

	/**
	 * 振替を記録するメソッド
	 * MySQLのhouseholdテーブルに支出と収入の２回記録&口座残高の更新をトランザクション処理する
	 * @param date 日付
	 * @param content 内容
	 * @param price 値段
	 * @param financialId 振替元の口座ID
	 * @param transferId 振替先の口座ID
	 * @param memo メモ
	 * @param isTransfer 振替フラグ
	 * @param sourceUid 振替元のユーザID
	 * @param transferUid 振替先のユーザID
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private void recordTransfer(String date, String content, int price, int financialId, int transferId, String memo, boolean isTransfer, int sourceUid, int transferUid) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		String householdId1 = "";
		String householdId2 = "";
		// UNIQUEな家計簿IDを２つ生成
		do {
			householdId1 = createHouseholdId();
			householdId2 = createHouseholdId();
		} while (!checkUniqueHouseholdId(householdId1) && !checkUniqueHouseholdId(householdId2));
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps1 = conn.prepareStatement("INSERT INTO household "
							+ "(`date`, `content`, `price`, `financial_id`, `large_item`, `middle_item`, `memo`, `transfer`, `id`, `user_id`) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			PreparedStatement ps2 = conn.prepareStatement("UPDATE financial SET balance = balance + ? WHERE id = ?;")
		) {
			conn.setAutoCommit(false);

			// 振替元
			ps1.setString(1, date);
			ps1.setString(2, content);
			ps1.setInt(3, -price);
			ps1.setInt(4, financialId);
			ps1.setString(5, "");
			ps1.setString(6, "");
			ps1.setString(7, memo);
			ps1.setBoolean(8, isTransfer);
			ps1.setString(9, householdId1);
			ps1.setInt(10, sourceUid);
			ps1.executeUpdate();

			ps2.setInt(1, -price);
			ps2.setInt(2, financialId);
			ps2.executeUpdate();

			// 振替先
			ps1.setString(1, date);
			ps1.setString(2, content);
			ps1.setInt(3, price);
			ps1.setInt(4, transferId);
			ps1.setString(5, "");
			ps1.setString(6, "");
			ps1.setString(7, memo);
			ps1.setBoolean(8, isTransfer);
			ps1.setString(9, householdId2);
			ps1.setInt(10, transferUid);
			ps1.executeUpdate();

			ps2.setInt(1, price);
			ps2.setInt(2, transferId);
			ps2.executeUpdate();

			try {
		        //データベースに登録
		        conn.commit();
		        System.out.println("登録成功");
		    } catch (SQLException e) {
		        //ロールバック処理ですべての変更を取り消し
		        conn.rollback();
		        System.out.println("登録失敗：ロールバック実行");
		        e.printStackTrace();
		    }
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
	}

	/**
	 * 家族全員の口座情報の取得メソッド
	 * @param familyId 家族ID
	 * @param uid ユーザID
	 * @return 家族全員の口座情報
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private List<Financial> getFinancial(int familyId, int uid) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		List<Financial> financial = new ArrayList<Financial>();
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT financial.id AS id, users.id AS user_id, users.name AS user_name, financial.name AS financial_name, balance, publish " +
					"FROM users " +
					"INNER JOIN financial ON users.id = financial.user_id " +
					"WHERE users.family_id = ? AND financial.target = true;");
		) {
			ps.setInt(1, familyId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String user_name = rs.getInt("user_id") == uid ? "あなた" : rs.getString("user_name");
				financial.add(new Financial(rs.getInt("id"), rs.getString("financial_name"), user_name, rs.getInt("user_id"), rs.getInt("balance"), rs.getBoolean("publish")));
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return financial;
	}

	/**
	 * 家族IDの生成メソッド
	 * 22文字の半角英数字のランダムな文字列を生成
	 * @return 家族ID
	 */
	private String createHouseholdId() {
		Random r = new Random();
		String alphabet = "abcdefghijklmnopqrstuv-_wxyzABCDEFGHIJKLMNOPQRSTUVWXY-_Z01234567890123456789";

		int shareCodeLength = 22;
		String id = "";

		for (int i=0; i<shareCodeLength; i++) {
			id += alphabet.charAt(r.nextInt(alphabet.length()));
		}
		return id;
	}

	/**
	 * 家族IDがUNIQUEかを判定するメソッド
	 * 引数の家計簿IDがMySQL上のhouseholdテーブルのidと重複していないかをチェックする
	 * @param id createHouseholdId()によって生成された家計簿ID
	 * @return 判定結果(true: UNIQUE, false; DISTINCT)
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private boolean checkUniqueHouseholdId(String id) throws ClassNotFoundException, SQLException {
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT * FROM household WHERE id = ?");
		) {
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println("NOT UNIQUE HOUSEHOLD ID!!!");
				return false;
			} else {
				System.out.println("UNIQUE HOUSEHOLD ID!");
				return true;
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return false;
	}

	/**
	 * 家計簿を取得メソッド
	 * MySQL上のhouseholdテーブルからfamily_idが同じ家計簿を取得
	 * @param familyId 家族ID
	 * @param uid アクセスしているユーザID
	 * @return 家計簿一覧
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private List<Household> getHousehold(int familyId, int uid) throws ClassNotFoundException, SQLException {
		List<Household> list = new ArrayList<Household>();
		// DB接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT "
					+ "date, "
					+ "content, "
					+ "price, "
					+ "financial.name, "
					+ "large_item, "
					+ "middle_item, "
					+ "memo, "
					+ "transfer, "
					+ "household.id, "
					+ "users.name AS user_name,"
					+ "household.user_id "
					+ "FROM household "
					+ "JOIN users ON users.id = household.user_id "
					+ "JOIN financial ON financial.id = household.financial_id "
					+ "WHERE users.family_id = ? "
					+ "ORDER BY date DESC;");
		) {
			ps.setInt(1, familyId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Household household = new Household();
				household.setDate(rs.getString("date"));
				household.setContent(rs.getString("content"));
				household.setPrice(rs.getInt("price"));
				household.setFinancial(rs.getString("name"));
				household.setLargeItem(rs.getString("large_item"));
				household.setMiddleItem(rs.getString("middle_item"));
				household.setMemo(rs.getString("memo"));
				household.setTransfer(rs.getBoolean("transfer"));
				household.setId(rs.getString("id"));
				String userName = rs.getInt("user_id") == uid ? "あなた" : rs.getString("user_name");
				household.setUserName(userName);
				list.add(household);
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return list;
	}
}
