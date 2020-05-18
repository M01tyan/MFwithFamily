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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Family;
import model.Financial;
import model.User;

/**
 * Servlet implementation class FinancialController
 */
@WebServlet("/FinancialController")
public class FinancialController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FinancialController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Family family = (Family) session.getAttribute("family");
		List<User> userList = (List<User>) session.getAttribute("userList");
		int id = Integer.parseInt(request.getParameter("id"));

		try {
			List<Financial> financialList = id == 0 ? getFamilyFinancial(family.getId(), user.getId()) : getPersonalFinancial(userList.get(id).getId());
			session.setAttribute("financialList", financialList);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		request.setAttribute("id", id);
		request.getRequestDispatcher(request.getContextPath()+"/financial.jsp")
			.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		int balance = Integer.parseInt(request.getParameter("balance"));
		boolean publish = Boolean.valueOf(request.getParameter("publish"));
		System.out.println(name + " " + balance + " " + publish);
		try {
			addFinancial(name, balance, user.getId(), publish);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		response.sendRedirect(request.getContextPath()+"/financial?id="+id);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		List<Financial> financialList = (List<Financial>) session.getAttribute("financialList");
		int id = Integer.parseInt(request.getParameter("index"));
		try {
			int financialId = financialList.get(id).getId();
			deleteFinancial(financialId);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}


	private List<Financial> getPersonalFinancial(int uid) throws ClassNotFoundException, SQLException {
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
				String user_name = rs.getInt("user_id") == uid ? "あなた" : rs.getString("user_name");
				financial.add(new Financial(rs.getInt("id"), rs.getString("financial_name"), user_name, rs.getInt("user_id"), rs.getInt("balance"), rs.getBoolean("publish")));
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return financial;
	}

	private List<Financial> getFamilyFinancial(int familyId, int uid) throws ClassNotFoundException, SQLException {
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
					+ "WHERE users.family_id = ? AND financial.target = true;");
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

	private void addFinancial(String name, int balance, int uid, boolean publish) throws ClassNotFoundException, SQLException {
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
