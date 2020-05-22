package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Component.SendMail;
import Component.Util;
import model.Family;
import model.User;
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/signUp.jsp")
			.forward(request, response);
	}

	/**
	 * 新規登録フォームからの入力を受け取った際に実行
	 *
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		//Formから入力された文字列を受け取る
		String email = (String)request.getParameter("email");
		String password = (String)request.getParameter("password");
		String confirmation = (String)request.getParameter("confirmation");
		String name = (String) request.getParameter("name");
		//バリデーション
		String message = Util.validation(email, password, confirmation, name);
		if (message.isEmpty()) {
			//フォームが正しく入力されていれば以下実行
			SendMail sendMail = new SendMail();
			String authCode = "";
			try {
				//新規ユーザの作成
				User user = createUser(email, password, name);
				if (user.getId() == -1) {
					//メールアドレスから既存ユーザの場合エラーメッセージを表示
					message += "もうすでに登録されたメールアドレスです";
					request.setAttribute("message", message);
					request.getRequestDispatcher(request.getContextPath()+"/signUp.jsp").forward(request, response);
				} else {
					//メール送信
					StringBuffer path = request.getRequestURL();
					String[] url = path.toString().split("/");
					authCode = sendMail.send(email, url[2]);
					int id = -1;
					//初期口座(お財布)の作成
					do {
						id = createFinancial(user.getId());
					} while(id == -1);
					//セッションにユーザ情報, 認証コード, 家族情報を保存
					HttpSession session = request.getSession();
					session.setAttribute("user", user);
					session.setAttribute("sessionAuthCode", authCode);
					session.setAttribute("family", new Family());
					System.out.println("認証コード: " + authCode);
					//認証画面へリダイレクト
					response.sendRedirect(request.getContextPath()+"/auth");
				}
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			}
		} else {
			//フォームに誤りがあった場合はエラーメッセージを表示する
			request.setAttribute("message", message);
			request.getRequestDispatcher(request.getContextPath()+"/signUp.jsp").forward(request, response);
		}
	}

	/**
	 * DBへの新規ユーザ登録
	 * MySQLのusersテーブルにemail, password, nameを追加
	 * @param email 生のメールアドレス
	 * @param password 生のパスワード
	 * @param name ユーザ名
	 * @return 生成されたUserクラス
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private User createUser(String email, String password, String name) throws SQLException, ClassNotFoundException {
		System.out.println(email + " : " + password);
		//MySQLへの接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		//暗号鍵の取得
		String secretKey = System.getenv("SECRET_KEY");
		//新しいユーザの作成
		User user = new User();
		try (
			Connection conn = DriverManager.getConnection(url, DBUser, DBPassword);
			//暗号化されたメールアドレス、パスワードと生のユーザ名をusersテーブルに追加
			//追加時にAUTO＿INCREMENTされているuidを取得
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

	/**
	 * 初期口座（お財布）の追加メソッド
	 * MySQLのfinancialテーブルに初期口座(お財布)の追加
	 * @param uid ユーザID
	 * @return　個別の口座ID
	 * @throws SQLException 正しくSQLが実行されなかった場合
	 * @throws ClassNotFoundException jdbcドライバが存在しない場合
	 */
	private int createFinancial(int uid) throws SQLException, ClassNotFoundException {
		//MySQLへの接続
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:" + System.getenv("HEROKU_DB_URL") + "?reconnect=true&verifyServerCertificate=false&useSSL=true&characterEncoding=utf8";
		String DBUser = System.getenv("HEROKU_DB_USER");
		String DBPassword = System.getenv("HEROKU_DB_PASSWORD");
		int id = -1;
		try (
			//新しいユーザの初期口座(名前: お財布, 残高: 0円)の追加
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
				//AUTO_INCREMENTされたfinancial_idを取得
				id = res.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("SQL ERROR: " + e);
		}
		return id;
	}
}
