package scheduler.main.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnecter {
	public Connection con = null;

	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://younggu1545.cafe24.com:3306/younggu1545";
	private String user = "younggu1545";
	private String password = "Itbank510!@";

	public DBConnecter() {
		connect();
	}

	public Connection getCon() {
		if (con == null) {
			connect();
			if (con == null) {// db접속 못하면 재시도
				getCon();
			}
		}

		return con;
	}

	public void connect() {
		if (con == null) {
			try {
				Class.forName(driver);
				this.con = DriverManager.getConnection(url, user, password);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (this.con == null) {
				System.out.println("DB 연결 실패");
			} else {
				System.out.println("DB 연결 성공");
			}
		}

	}

	public void close() {
		if (this.con != null) {
			try {
				this.con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
