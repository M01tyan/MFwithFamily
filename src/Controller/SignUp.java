package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Component.SendMail;
import model.Family;
import model.User;
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doIt(request, response);
		} catch (ServletException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String message = "";
		String email = (String)request.getParameter("email");
		String password = (String)request.getParameter("password");
		String confirmation = (String)request.getParameter("confirmation");
		String name = (String) request.getParameter("name");
		if (!emailValidation(email)) message += "正しいメールアドレスを入力してください<BR>";
		if (!passwordValidation(password)) message += "パスワードは半角英数字で入力してください<BR>";
		if (!password.equals(confirmation)) message += "パスワードが一致しません<BR>";
		if (name.isEmpty()) message += "ユーザ名を入力してください<BR>";
		if (message.isEmpty()) {
			SendMail sendMail = new SendMail();
			String authCode = "";
			try {
				StringBuffer path = request.getRequestURL();
				String[] url = path.toString().split("/");
				authCode = sendMail.send(email, url[2]);
				User user = createUser(email, password, name);
				if (user.getId() == -1) {
					message += "もうすでに登録されたメールアドレスです";
					request.setAttribute("message", message);
					request.getRequestDispatcher(request.getContextPath()+"/signUp.jsp").forward(request, response);
				} else {
					int id = -1;
					do {
						id = createFinancial(user.getId());
					} while(id == -1);
					HttpSession session = request.getSession();
					session.setAttribute("user", user);
					session.setAttribute("sessionAuthCode", authCode);
					session.setAttribute("family", new Family());
					System.out.println("認証コード: " + authCode);
					response.sendRedirect(request.getContextPath()+"/auth");
				}
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			}

		} else {
			request.setAttribute("message", message);
			request.getRequestDispatcher(request.getContextPath()+"/signUp.jsp").forward(request, response);
		}
	}

	private void doIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/signUp.jsp")
			.forward(request, response);
	}

	private boolean emailValidation(String email) {
		String pattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	    Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(email);
	    return m.find();
	}

	private boolean passwordValidation(String password) {
		String pattern = "^[0-9a-zA-Z.-_@!&#$%]{6,20}";
	    Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(password);
	    return m.find();
	}

	private User createUser(String email, String password, String name) throws SQLException, ClassNotFoundException {
		System.out.println(email + " : " + password);
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		String secretKey = System.getenv("SECRET_KEY");
		User user = new User();
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("INSERT INTO users "
							+ "(`email`, `password`, `name`) "
							+ "VALUES (HEX(AES_ENCRYPT(?, ?)), HEX(AES_ENCRYPT(?, ?)), ?);", Statement.RETURN_GENERATED_KEYS);
		) {
			ps.setString(1, email);
			ps.setString(2, secretKey);
			ps.setString(3, password);
			ps.setString(4, secretKey);
			ps.setString(5, name);
			ps.executeUpdate();
			ResultSet res = ps.getGeneratedKeys();
			if(res.next()) {
				user.setId(res.getInt(1));
				user.setName(name);
				return user;
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return user;
	}

	private int createFinancial(int uid) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		int id = -1;
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			PreparedStatement ps =
					conn.prepareStatement("INSERT INTO financial "
							+ "(`name`, `user_id`) "
							+ "VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
		) {
			ps.setString(1, "お財布");
			ps.setInt(2, uid);
			ps.executeUpdate();
			ResultSet res = ps.getGeneratedKeys();
			if(res.next()) {
				id = res.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return id;
	}
}
