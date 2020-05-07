package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Family;
import model.User;

/**
 * Servlet implementation class ShareController
 */
public class ShareController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShareController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean clickCreateButton = request.getParameter("createShareCode") != null ? true : false;
		if (!clickCreateButton) {

		} else {
			String shareCode = "";
			try {
				ServletContext application = getServletContext();
				User user = (User) application.getAttribute("user");
				do {
					shareCode = createShareCode();
				} while (!checkUniqueShareCode(shareCode));
				int familyId = createFamily(shareCode);
				setFamilyId(familyId, user.getId());
				request.getRequestDispatcher("/share.jsp").forward(request, response);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				String message = "共有コードの発行に失敗しました。\nもう一度お願いします";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/share.jsp").forward(request, response);
			}
		}
		request.getRequestDispatcher(request.getContextPath()+"/share.jsp")
		   .forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String inputShareCode = (String) request.getParameter("inputShareCode");
		Family family = new Family();
		try {
			List<User> userList = checkShareCode(inputShareCode);
			if (userList != null) {
				family.setUserList(userList);
				family.setShareCode(inputShareCode);
				for (User user : userList) {
					System.out.println(user.getId());
				}
			} else {

			}
			ServletContext application = getServletContext();
			application.setAttribute("family", family);
			application.setAttribute("shareCode", inputShareCode);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		request.getRequestDispatcher(request.getContextPath()+"/share.jsp")
		   .forward(request, response);
	}


	private String createShareCode() {
		Random r = new Random();
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890123456789";

		int shareCodeLength = 8;
		String shareCode = "";

		for (int i=0; i<shareCodeLength; i++) {
			shareCode += alphabet.charAt(r.nextInt(alphabet.length()));
		}
		System.out.println(shareCode);
		return shareCode;
	}

	private boolean checkUniqueShareCode(String shareCode) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
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

	private int createFamily(String shareCode) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		int familyId = -1;
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("INSERT INTO family "
							+ "(`auth_code`) "
							+ "VALUES (?);", Statement.RETURN_GENERATED_KEYS);
		) {
			ps.setString(1, shareCode);
			ps.executeUpdate();
			ResultSet res = ps.getGeneratedKeys();
			if(res.next()) {
				familyId = res.getInt(1);
				ServletContext application = getServletContext();
				application.setAttribute("shareCode", shareCode);
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return familyId;
	}

	private void setFamilyId(int familyId, int uid) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
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
			ServletContext application = getServletContext();
			application.setAttribute("familyId", familyId);
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
	}

	private List<User> checkShareCode(String shareCode) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		List<User> userList = new ArrayList<User>();
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("SELECT users.id AS id, name, relationship_id, family_id, email_certificate "
							+ "FROM users "
							+ "INNER JOIN family ON users.family_id = family.id "
							+ "WHERE family.auth_code = ?;", Statement.RETURN_GENERATED_KEYS);
		) {
			ps.setString(1, shareCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id = (int) rs.getInt("id");
				String name = (String) rs.getString("name");
				int relationshipId = (int) rs.getInt("relationship_id");
				int familyId = (int) rs.getInt("family_id");
				boolean emailCertificate = (Boolean) rs.getBoolean("email_certificate");
				User user = new User(id, name, relationshipId, familyId, emailCertificate, 0);
				System.out.println(id);
				userList.add(user);
				while (rs.next()) {
					id = (int) rs.getInt("id");
					name = (String) rs.getString("name");
					relationshipId = (int) rs.getInt("relationship_id");
					familyId = (int) rs.getInt("family_id");
					emailCertificate = (Boolean) rs.getBoolean("email_certificate");
					user = new User(id, name, relationshipId, familyId, emailCertificate, 0);
					System.out.println(id);
					userList.add(user);
				}
			}
			return userList;
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return userList;
	}
}
