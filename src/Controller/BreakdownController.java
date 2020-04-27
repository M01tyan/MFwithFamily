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

import model.Analytics;

/**
 * Servlet implementation class Breakdown
 */
public class BreakdownController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		HttpSession session = request.getSession();
		int id = (int)session.getAttribute("id");
		ArrayList<Analytics> analyticsList = id == 0 ? fetchAllBreakdown() : fetchEachBreakdown(id);
		session.setAttribute("analyticsList", analyticsList);
		request.getRequestDispatcher(request.getContextPath()+"/breakdown.jsp")
			.forward(request, response);
	}

	private ArrayList<Analytics> fetchAllBreakdown() throws ClassNotFoundException, SQLException {
		ArrayList<Analytics> list = new ArrayList<Analytics>();
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("(SELECT SUM(price) AS price, large_item, \"合計\" AS payer "
					+ "FROM household INNER JOIN users ON users.id = household.user_id "
					+ "INNER JOIN relationship ON users.relationship_id = relationship.id "
					+ "GROUP BY large_item HAVING price < 0) "
					+ "UNION ALL "
					+ "(SELECT SUM(price) AS price, large_item, users.name AS payer FROM household "
					+ "INNER JOIN users ON users.id = household.user_id "
					+ "INNER JOIN relationship ON users.relationship_id = relationship.id "
					+ "GROUP BY large_item, users.id HAVING price < 0) "
					+ "ORDER BY large_item DESC, price ASC");
		) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Analytics analytics = new Analytics();
				analytics.setLargeItem(rs.getString("large_item"));
				analytics.setPrice(rs.getInt("price"));
				analytics.setPayer(rs.getString("payer"));
				list.add(analytics);
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return list;
	}

	private ArrayList<Analytics> fetchEachBreakdown(int id) throws ClassNotFoundException, SQLException {
		ArrayList<Analytics> list = new ArrayList<Analytics>();
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL");
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		try (
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps =
			conn.prepareStatement("SELECT SUM(price) AS price, large_item, users.name AS payer FROM household "
					+ "INNER JOIN users ON users.id = household.user_id "
					+ "INNER JOIN relationship ON users.relationship_id = relationship.id "
					+ "WHERE users.id = " + id + " "
					+ "GROUP BY large_item, users.id HAVING price < 0 "
					+ "ORDER BY large_item DESC, price ASC");
		) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Analytics analytics = new Analytics();
				analytics.setLargeItem(rs.getString("large_item"));
				analytics.setPrice(rs.getInt("price"));
				analytics.setPayer(rs.getString("payer"));
				list.add(analytics);
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return list;
	}
}
