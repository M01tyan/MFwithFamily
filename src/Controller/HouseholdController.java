package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Household;

/**
 * Servlet implementation class List
 */
//@WebServlet("/List")
public class HouseholdController extends HttpServlet {
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
		doGet(request, response);
	}

	private void doIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		int id = Integer.parseInt(request.getParameter("id"));
		ArrayList<Household> householdList = id == 0 ? fetchAllHousehold() : fetchEachHousehold(id);
		HttpSession session = request.getSession();
		session.setAttribute("id", id);
		session.setAttribute("householdList", householdList);
		request.getRequestDispatcher("/household.jsp")
			.forward(request, response);
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
					+ "target, "
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
					+ "relationship.name AS relationship_name "
					+ "from household "
					+ "INNER JOIN users ON users.id = household.user_id "
					+ "INNER JOIN relationship ON users.relationship_id = relationship.id;");
		) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Household household = new Household();
				household.setTarget(rs.getInt("target"));
				household.setDate(rs.getString("date"));
				household.setContent(rs.getString("content"));
				household.setPrice(rs.getInt("price"));
				household.setFinancial(rs.getString("financial"));
				household.setLargeItem(rs.getString("large_item"));
				household.setMiddleItem(rs.getString("middle_item"));
				household.setMemo(rs.getString("memo"));
				household.setTransfer(rs.getInt("transfer"));
				household.setId(rs.getString("id"));
				household.setUserName(rs.getString("user_name"));
				household.setRelationshipName(rs.getString("relationship_name"));
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
					+ "target, "
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
					+ "relationship.name AS relationship_name "
					+ "from household "
					+ "INNER JOIN users ON users.id = household.user_id "
					+ "INNER JOIN relationship ON users.relationship_id = relationship.id "
					+ "WHERE users.id = " + id + ";");
		) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Household household = new Household();
				household.setTarget(rs.getInt("target"));
				household.setDate(rs.getString("date"));
				household.setContent(rs.getString("content"));
				household.setPrice(rs.getInt("price"));
				household.setFinancial(rs.getString("financial"));
				household.setLargeItem(rs.getString("large_item"));
				household.setMiddleItem(rs.getString("middle_item"));
				household.setMemo(rs.getString("memo"));
				household.setTransfer(rs.getInt("transfer"));
				household.setId(rs.getString("id"));
				household.setUserName(rs.getString("user_name"));
				household.setRelationshipName(rs.getString("relationship_name"));
				list.add(household);
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return list;
	}

}
