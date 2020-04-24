package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Balance;
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
		Balance balance = new Balance();
		balance = fetchTotalBalance(balance);
		balance = fetchEachBalance(balance);
		HttpSession session = request.getSession();
		session.setAttribute("balance", balance);
		request.getRequestDispatcher("/balance.jsp")
				.forward(request, response);
	}

	private Balance fetchTotalBalance(Balance balance) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		Connection conn = DriverManager.getConnection(url, user, password);
		try {
			PreparedStatement ps =
			conn.prepareStatement("SELECT SUM(price) AS total FROM household "
					+ "INNER JOIN users ON household.user_id = users.id "
					+ "INNER JOIN relationship ON users.relationship_id = relationship.id;");
			try {
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					balance.setTotalBalance(rs.getInt("total"));
				} else {
					return balance;
				}
			} catch (SQLException e) {
				System.out.println("SQL ERROR: " + e);
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						System.out.println("PreparedStatementのクローズに失敗しました。");
					}
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("MySQLのクローズに失敗しました。");
				}
			}
		}
		return balance;
	}

	private Balance fetchEachBalance(Balance balance) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL");
		String user = System.getenv("HEROKU_DB_USER");
		String password = System.getenv("HEROKU_DB_PASSWORD");
		Connection conn = DriverManager.getConnection(url, user, password);
		try {
			PreparedStatement ps =
			conn.prepareStatement("SELECT SUM(price) AS total, users.name AS name FROM household "
					+ "INNER JOIN users ON household.user_id = users.id "
					+ "INNER JOIN relationship ON users.relationship_id = relationship.id "
					+ "GROUP BY users.id;");
			try {
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					balance.addEachBalance(rs.getInt("total"), rs.getString("name"));
				}
				return balance;
			} catch (SQLException e) {
				System.out.println("SQL ERROR: " + e);
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						System.out.println("PreparedStatementのクローズに失敗しました。");
					}
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("MySQLのクローズに失敗しました。");
				}
			}
		}
		return balance;
	}

}
