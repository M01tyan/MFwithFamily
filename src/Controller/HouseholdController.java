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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		int id = Integer.parseInt(request.getParameter("id"));
		HttpSession session = request.getSession();
		Family family = (Family) session.getAttribute("family");
		User user = (User) session.getAttribute("user");
		try {
			List<Financial> financialList = getFinancial(family.getId(), user.getId());
			List<Household> householdList = getHousehold(family.getId(), user.getId());
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		System.out.println(date + " " + price + " " + financialId + " " + isTransfer + " " + transferId + " " + content + " " + largeItem + " " + middleItem + " " + memo + " " + sourceUid + " " + transferUid);
		try {
			if (!isTransfer) {
				recordExpense(date, content, -price, financialId, largeItem, middleItem, memo, isTransfer, sourceUid);
			} else {
				recordTransfer(date, content, price, financialId, transferId, memo, isTransfer, sourceUid, transferUid);
			}
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("ERROR: " + e);
		}
		response.sendRedirect(request.getContextPath()+"/household");
	}

	private void recordExpense(String date, String content, int price, int financialId, String largeItem, String middleItem, String memo, boolean isTransfer, int uid) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		String householdId = "";
		do {
			householdId = createHouseholdId();
		} while (!checkUniqueHouseholdId(householdId));
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps1 = conn.prepareStatement("INSERT INTO household "
							+ "(`date`, `content`, `price`, `financial_id`, `large_item`, `middle_item`, `memo`, `transfer`, `id`, `user_id`) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			PreparedStatement ps2 = conn.prepareStatement("UPDATE financial SET balance = balance + ? WHERE id = ?;");
		) {
			conn.setAutoCommit(false);

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

	private void recordTransfer(String date, String content, int price, int financialId, int transferId, String memo, boolean isTransfer, int sourceUid, int transferUid) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		String householdId1 = "";
		String householdId2 = "";
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

	private List<Financial> getFinancial(int familyId, int uid) throws ClassNotFoundException, SQLException {
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

	public int getFinancialIdFromFinancialName(List<Financial> financialList, String financialName) {
		for (Financial financial : financialList) {
			if (financial.getFinancialName().equals(financialName)) return financial.getId();
		}
		return -1;
	}

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

	private boolean checkUniqueHouseholdId(String id) throws ClassNotFoundException, SQLException {
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

	private List<Household> getHousehold(int familyId, int uid) throws ClassNotFoundException, SQLException {
		List<Household> list = new ArrayList<Household>();
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
