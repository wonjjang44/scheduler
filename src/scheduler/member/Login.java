
package scheduler.member;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import scheduler.main.Main;
import scheduler.main.MainFrame;

public class Login extends JFrame { // 로그인
	Main main;
	JLabel la_id; // 아이디
	JLabel la_pw; // 비밀번호
	JTextField t_id; // 아이디 텍스트 필드
	JTextField t_pw; // 비밀번호 텍스트 필드
	JButton bt_add; // 등록
	JButton bt_login; // 로그인
	JPanel p_north; // 남쪽
	JPanel p_center; // 센터패널
	JPanel p_south; // 남쪽 패널
	JPanel[] pages = new JPanel[2];
	String admin = "Y";

	public Login(Main main) {
		this.main = main;

		// container = new JPanel();
		la_id = new JLabel("아이디"); // 아이디
		la_pw = new JLabel("비밀번호"); // 비밀번호
		t_id = new JTextField(10);
		t_pw = new JTextField(10);
		bt_add = new JButton("등록");// 등록버튼
		bt_login = new JButton("로그인");// 로그인버튼

		p_north = new JPanel();// 북
		p_center = new JPanel();// 센터 패널
		p_south = new JPanel(); // 남쪽 패널

		// 라벨 사이즈
		Dimension dm = new Dimension(60, 20);
		la_id.setPreferredSize(dm);
		la_pw.setPreferredSize(dm);

		p_north.add(la_id); // 아이디 라벨
		p_north.add(t_id); // 아이디 텍스트필드
		p_center.add(la_pw); // 비밀번호 라벨
		p_center.add(t_pw); // 비밀번호 텍스트필드

		p_south.add(bt_add); // 등록 버튼
		p_south.add(bt_login); // 로그인 버튼

		add(p_north, BorderLayout.NORTH);
		add(p_center);
		add(p_south, BorderLayout.SOUTH);

		bt_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regist();
			}
		});

		bt_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		t_id.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});

		t_pw.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

		setLocationRelativeTo(null);
		setSize(400, 150);
		this.setVisible(true);
	}

	public void regist() { // 등록
		new RegistMember(main);
	}

	public void login() { // 로그인
		String id = t_id.getText();
		String password = t_pw.getText();
		String sql = "select * from member where id='" + id + "'and password='" + password + "'";
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			System.out.println(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int user_num = rs.getInt("member_no");
				String sql2 = "update member set last_login_date=now() where member_no='" + user_num + "'";// 마지막 로그인시간
				pstmt = con.prepareStatement(sql2);
				int result = pstmt.executeUpdate();
				JOptionPane.showMessageDialog(this, "로그인 성공");

				main.userInfo.setData(rs.getInt("member_no"), rs.getString("id"), rs.getString("name"),
						rs.getString("email"), rs.getString("phone"), rs.getBoolean("admin"));

				main.setMainFrame(new MainFrame(main));
				this.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 확인해주세요");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
