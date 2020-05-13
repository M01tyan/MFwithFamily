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
			List<String> financialList = getFinancial(userList.get(id).getId());
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
		// TODO Auto-generated method stub
//		doGet(request, response);
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

	private List<String> getFinancial(int uid) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		List<String> financial = new ArrayList<String>();
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT users.id AS user_id, users.name AS user_name, financial.name AS financial_name "
					+ "FROM users "
					+ "INNER JOIN financial ON users.id = financial.user_id "
					+ "WHERE users.id = ?;");
		) {
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String user_name = rs.getInt("user_id") == uid ? "あなた" : rs.getString("user_name");
				financial.add(rs.getString("financial_name"));
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return financial;
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
