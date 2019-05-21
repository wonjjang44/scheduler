/*
 * ó�� ����Ǵ� main ȭ��.
 * ��� Ŭ�������� ���Ǵ� �޼���/���� ����
 * */
package scheduler.main;

import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import scheduler.main.user.UserInfo;
import scheduler.member.Login;
import scheduler.noticeboard.NoticeBoard;


public class Main {
	private DBManager dbManager; // DB���� ����
	public UserInfo userInfo; // ���� ���� ����
	public MainFrame mainFrame;
	private final Image iconImg = Toolkit.getDefaultToolkit().getImage("res/icon.png");
	private final String appTitle = "Scheduler";
	private final String key = "securityUtil";	// ��ȣȭ �� ����ϴ� Ű��


	public Main() {
		dbManager = new DBManager();
		
		userInfo = new UserInfo();
	}

	// ȭ�� ������ �߻��ϴ� �޼���
	public void frameClose() {
		dbManager.closeDB();
		System.exit(0);
	}

	// mainframe �����ϴ� �޼���
	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	// ������ ������ִ� �޼���
	public Image getIcon() {
		return iconImg;
	}

	// Ÿ��Ʋ ������ִ� �޼���
	public String getTitle() {
		return appTitle;
	}
	
	public String getKey() {
		return key;
	}

	// DB���� �޼��� ����
	public Connection getCon() { // ���ؼ� ��ü ��ȭ�ϴ� �޼���
		return dbManager.getCon();
	}

	public void closeDB(PreparedStatement pstmt, ResultSet rs) {
		dbManager.closeDB(pstmt, rs);
	}

	public void closeDB(PreparedStatement pstmt) {
		dbManager.closeDB(pstmt);
	}
	// DB ���� �޼��� ��

	public static void main(String[] args) {
		Main main = new Main();
		new Login(main);

	}

}
