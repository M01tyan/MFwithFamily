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
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			if (user.getId() != -1) {
				try {
					Family family = getFamily(user);
					session.setAttribute("family", family);
				} catch (ClassNotFoundException | SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		} else {
			String shareCode = "";
			try {
				HttpSession session = request.getSession();
				User user = (User) session.getAttribute("user");
				do {
					shareCode = createShareCode();
				} while (!checkUniqueShareCode(shareCode));
				Family family = createFamily(shareCode);
				setFamilyId(family.getId(), user.getId());
				session.setAttribute("family", family);
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
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Family family = (Family) session.getAttribute("family");
		try {
			family = checkShareCode(inputShareCode, user);
			if (family.getUserList() != null) {
				setFamilyId(family.getId(), user.getId());
			} else {
				String message = "共有コードが違います";
				request.setAttribute("message", message);
			}
			session.setAttribute("family", family);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		request.getRequestDispatcher(request.getContextPath()+"/share.jsp")
		   .forward(request, response);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int index = Integer.parseInt((String)request.getParameter("id"));
		HttpSession session = request.getSession();
		Family family = (Family) session.getAttribute("family");
		List<User> userList = family.getUserList();
		try {
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

	private Family createFamily(String shareCode) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
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
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
	}

	private Family checkShareCode(String shareCode, User user) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		List<User> userList = new ArrayList<User>();
		Family family = new Family();
		try (
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

	private Family getFamily(User user) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		List<User> userList = new ArrayList<User>();
		Family family = new Family();
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("(SELECT @family_id := `family_id` AS family_id, users.id AS id, name, auth_code "
							+ "FROM users "
							+ "INNER JOIN family ON users.family_id = family.id "
							+ "WHERE users.id = ?) "
							+ "UNION "
							+ "(SELECT family_id, users.id AS id, name, auth_code "
							+ "FROM users "
							+ "INNER JOIN family ON users.family_id = family.id "
							+ "WHERE family_id = @family_id);");
		) {
			ps.setInt(1, user.getId());
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

	private void releaseFamily(int uid) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
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
