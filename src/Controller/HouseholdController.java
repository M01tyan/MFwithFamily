package Controller;

import java.io.IOException;
import java.io.PrintWriter;
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
		int id = Integer.parseInt(request.getParameter("id"));
		HttpSession session = request.getSession();
		List<User> userList = (List<User>) session.getAttribute("userList");
		try {
			List<Financial> financialList = getFinancial(userList.get(id).getId());
			session.setAttribute("financialList", financialList);
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
		String financial = request.getParameter("financial");
		boolean isTransfer = Boolean.valueOf(request.getParameter("isTransfer"));
		String transfer = request.getParameter("transfer");
		String content = request.getParameter("content");
		String largeItem = request.getParameter("largeItem");
		String middleItem = request.getParameter("middleItem");
		String memo = request.getParameter("memo");
		int id = Integer.parseInt(request.getParameter("id"));
		HttpSession session = request.getSession();
		List<Financial> financialList = (List<Financial>) session.getAttribute("financialList");
		User user = (User) session.getAttribute("user");

		String householdId = "";
		try {
			do {
				householdId = createId();
			} while (!checkUniqueShareCode(householdId));
			addHousehold(date, content, price, financialList.get(id).getId(), largeItem, middleItem, memo, isTransfer, householdId, user.getId());
			String message = "success";
			String responseJson = "{\"message\":\"" + message + "\"}";
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.write(responseJson);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

//	private void doIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
////		int id = Integer.parseInt(request.getParameter("id"));
////		ArrayList<Household> householdList = id == 0 ? fetchAllHousehold() : fetchEachHousehold(id);
////		HttpSession session = request.getSession();
////		session.setAttribute("id", id);
////		session.setAttribute("householdList", householdList);
//		request.getRequestDispatcher(request.getContextPath()+"/household.jsp")
//			.forward(request, response);
//	}

	private List<Financial> getFinancial(int uid) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		List<Financial> financial = new ArrayList<Financial>();
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT financial.id AS id, users.id AS user_id, users.name AS user_name, financial.name AS financial_name, balance "
					+ "FROM users "
					+ "INNER JOIN financial ON users.id = financial.user_id "
					+ "WHERE users.id = ?;");
		) {
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String user_name = rs.getInt("user_id") == uid ? "あなた" : rs.getString("user_name");
				financial.add(new Financial(rs.getInt("id"), rs.getString("financial_name"), user_name, rs.getInt("user_id"), rs.getInt("balance")));
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return financial;
	}

	private void addHousehold(String date, String content, int price, int financialId, String largeItem, String middleItem, String memo, boolean isTransfer, String id, int uid) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
					conn.prepareStatement("INSERT INTO household "
							+ "(`date`, `content`, `price`, `financial_id`, `large_item`, `middle_item`, `memo`, `transfer`, `id`, `user_id`) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		) {
			ps.setString(1, date);
			ps.setString(2, content);
			ps.setInt(3, price);
			ps.setInt(4, financialId);
			ps.setString(5, largeItem);
			ps.setString(6, middleItem);
			ps.setString(7, memo);
			ps.setBoolean(8, isTransfer);
			ps.setString(9, id);
			ps.setInt(10, uid);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
	}

	private String createId() {
		Random r = new Random();
		String alphabet = "abcdefghijklmnopqrstuv-_wxyzABCDEFGHIJKLMNOPQRSTUVWXY-_Z01234567890123456789";

		int shareCodeLength = 22;
		String id = "";

		for (int i=0; i<shareCodeLength; i++) {
			id += alphabet.charAt(r.nextInt(alphabet.length()));
		}
		return id;
	}

	private boolean checkUniqueShareCode(String id) throws ClassNotFoundException, SQLException {
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

	private ArrayList<Household> fetchAllHousehold() throws ClassNotFoundException, SQLException {
		ArrayList<Household> list = new ArrayList<Household>();
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
					+ "financial, "
					+ "large_item, "
					+ "middle_item, "
					+ "memo, "
					+ "transfer, "
					+ "household.id AS id, "
					+ "users.name AS user_name "
					+ "from household "
					+ "INNER JOIN users ON household.user_id = " + 131 + " "
					+ "ORDER BY date DESC"
					+ ";");
		) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Household household = new Household();
				household.setDate(rs.getString("date"));
				household.setContent(rs.getString("content"));
				household.setPrice(rs.getInt("price"));
				household.setFinancial(rs.getString("financial"));
				household.setLargeItem(rs.getString("large_item"));
				household.setMiddleItem(rs.getString("middle_item"));
				household.setMemo(rs.getString("memo"));
				household.setTransfer(rs.getString("transfer"));
				household.setId(rs.getString("id"));
				household.setUserName(rs.getString("user_name"));
				list.add(household);
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return list;
	}

	private ArrayList<Household> fetchEachHousehold(int id) throws ClassNotFoundException, SQLException {
		ArrayList<Household> list = new ArrayList<Household>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL");
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT "
					+ "date, "
					+ "content, "
					+ "price, "
					+ "financial, "
					+ "large_item, "
					+ "middle_item, "
					+ "memo, "
					+ "transfer, "
					+ "household.id AS id, "
					+ "users.name AS user_name, "
					+ "from household "
					+ "INNER JOIN users ON users.id = household.user_id "
					+ "WHERE users.id = " + id + ";");
		) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Household household = new Household();
				household.setDate(rs.getString("date"));
				household.setContent(rs.getString("content"));
				household.setPrice(rs.getInt("price"));
				household.setFinancial(rs.getString("financial"));
				household.setLargeItem(rs.getString("large_item"));
				household.setMiddleItem(rs.getString("middle_item"));
				household.setMemo(rs.getString("memo"));
				household.setTransfer(rs.getString("transfer"));
				household.setId(rs.getString("id"));
				household.setUserName(rs.getString("user_name"));
				list.add(household);
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return list;
	}

}
