package scheduler.member;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import scheduler.main.Main;

public class RegistMember extends Frame {// 회원가입
	Main main;

	// 디자인 명 멤버 변수 주석 전까지
	JPanel p_east;
	JPanel p_west;
	JPanel p_center;
	JPanel p_south;
	JLabel la_id;
	JLabel la_name;
	JLabel la_password;
	JLabel la_email;
	JLabel la_phone;
	JTextField t_id;
	JTextField t_name;
	JTextField t_password;
	JTextField t_email;
	JTextField t_phone;
	JButton bt_check; // 중복체크
	JButton bt_add; // 등록

	// 멤버 변수 DB연결 전까지
	boolean idChecked = false; // id 체크여부
	boolean flag = true;

	public RegistMember(Main main) {
		this.main = main;

		this.setLayout(new BorderLayout());

		p_east = new JPanel(); // 동
		p_west = new JPanel(); // 서
		p_center = new JPanel(); // 센터
		p_south = new JPanel(); // 남
		la_id = new JLabel("아이디");
		la_name = new JLabel("이름");
		la_password = new JLabel("비밀번호");
		la_email = new JLabel("이메일");
		la_phone = new JLabel("전화번호");
		t_id = new JTextField(15);
		t_name = new JTextField(15);
		t_password = new JTextField(15);
		t_email = new JTextField(15);
		t_phone = new JTextField(15);
		bt_check = new JButton("중복체크");
		bt_add = new JButton("등록");
		// 라벨 사이즈
		Dimension la_dm = new Dimension(100, 25);
		la_id.setPreferredSize(la_dm);
		la_name.setPreferredSize(la_dm);
		la_password.setPreferredSize(la_dm);
		la_email.setPreferredSize(la_dm);
		la_phone.setPreferredSize(la_dm);
		p_west.setPreferredSize(new Dimension(100, 300));
		// 센터 부착
		p_west.add(la_id);
		p_center.add(t_id);
		p_east.add(bt_check); // 중복체크 버튼
		p_west.add(la_name);
		p_center.add(t_name);
		p_west.add(la_password);
		p_center.add(t_password);
		p_west.add(la_email);
		p_center.add(t_email);
		p_west.add(la_phone);
		p_center.add(t_phone);
		// 남쪽 등록버튼
		p_south.add(bt_add);
		// p_center.setLayout(new GridLayout(5,2));
		add(p_east, BorderLayout.EAST);
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_south, BorderLayout.SOUTH);

		// 등록
		bt_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regist();
			}
		});

		// 등록에서 X버튼 누를시 창 닫는 이벤트
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

		// 중복체크
		bt_check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkId();
			}
		});

		setLocationRelativeTo(null);
		setSize(400, 300);
		setVisible(flag);

	}

	// 등록
	public void regist() {
		// 중복확인을 눌러야 가입가능
		if (idChecked == false) { // 중복 체크
			JOptionPane.showMessageDialog(this, "아이디 중복체크 하지않음");
			return;
		}

		String id = t_id.getText();
		String name = t_name.getText();
		String password = t_password.getText();
		String email = t_email.getText();
		String phone = t_phone.getText();

		if (id.length() == 0) {
			JOptionPane.showMessageDialog(this, "아이디를 입력하세요");
			t_id.requestFocus();// 커서 올리기
			return;
		}

		if (name.length() == 0) {
			JOptionPane.showMessageDialog(this, "이름을 입력하세요");
			t_name.requestFocus();// 커서 올리기
			return;
		}

		if (password.length() == 0) {
			JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요");
			t_password.requestFocus();// 커서 올리기
			return;
		}

		if (email.length() == 0) {
			JOptionPane.showMessageDialog(this, "이메일을 입력하세요");
			t_email.requestFocus();// 커서 올리기
			return;
		}

		if (phone.length() == 0) {
			JOptionPane.showMessageDialog(this, "전화번호를 입력하세요");
			t_phone.requestFocus();// 커서 올리기
			return;
		}
		// flag=false; //등록에 성공하면 visible을 false로

		String sql = "insert into member(id,name,password,email,phone)";
		sql += " values('" + id + "','" + name + "','" + password + "','" + email + "','" + phone + "')";

		System.out.println(sql);
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			int result = pstmt.executeUpdate();
			if (result == 0) {
				JOptionPane.showMessageDialog(this, "가입실패");
			} else {
				JOptionPane.showMessageDialog(this, "가입성공");
				flag = false;
				setVisible(flag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 중복체크
	public void checkId() {
		String id = t_id.getText();
		String sql = "select * from member where id='" + id + "'";
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {// true면 가입 불가
				JOptionPane.showMessageDialog(this, "이미 사용중인 아이디입니다");
			} else {
				idChecked = true;
				JOptionPane.showMessageDialog(this, "사용 가능한 아이디입니다");
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
