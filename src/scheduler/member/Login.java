
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

public class Login extends JFrame { // �α���
	Main main;
	JLabel la_id; // ���̵�
	JLabel la_pw; // ��й�ȣ
	JTextField t_id; // ���̵� �ؽ�Ʈ �ʵ�
	JTextField t_pw; // ��й�ȣ �ؽ�Ʈ �ʵ�
	JButton bt_add; // ���
	JButton bt_login; // �α���
	JPanel p_north; // ����
	JPanel p_center; // �����г�
	JPanel p_south; // ���� �г�
	JPanel[] pages = new JPanel[2];
	String admin = "Y";

	public Login(Main main) {
		this.main = main;

		// container = new JPanel();
		la_id = new JLabel("���̵�"); // ���̵�
		la_pw = new JLabel("��й�ȣ"); // ��й�ȣ
		t_id = new JTextField(10);
		t_pw = new JTextField(10);
		bt_add = new JButton("���");// ��Ϲ�ư
		bt_login = new JButton("�α���");// �α��ι�ư

		p_north = new JPanel();// ��
		p_center = new JPanel();// ���� �г�
		p_south = new JPanel(); // ���� �г�

		// �� ������
		Dimension dm = new Dimension(60, 20);
		la_id.setPreferredSize(dm);
		la_pw.setPreferredSize(dm);

		p_north.add(la_id); // ���̵� ��
		p_north.add(t_id); // ���̵� �ؽ�Ʈ�ʵ�
		p_center.add(la_pw); // ��й�ȣ ��
		p_center.add(t_pw); // ��й�ȣ �ؽ�Ʈ�ʵ�

		p_south.add(bt_add); // ��� ��ư
		p_south.add(bt_login); // �α��� ��ư

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

	public void regist() { // ���
		new RegistMember(main);
	}

	public void login() { // �α���
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
				String sql2 = "update member set last_login_date=now() where member_no='" + user_num + "'";// ������ �α��νð�
				pstmt = con.prepareStatement(sql2);
				int result = pstmt.executeUpdate();
				JOptionPane.showMessageDialog(this, "�α��� ����");

				main.userInfo.setData(rs.getInt("member_no"), rs.getString("id"), rs.getString("name"),
						rs.getString("email"), rs.getString("phone"), rs.getBoolean("admin"));

				main.setMainFrame(new MainFrame(main));
				this.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(this, "���̵�� ��й�ȣ�� Ȯ�����ּ���");
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
