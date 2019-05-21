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

public class RegistMember extends Frame {// ȸ������
	Main main;

	// ������ �� ��� ���� �ּ� ������
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
	JButton bt_check; // �ߺ�üũ
	JButton bt_add; // ���

	// ��� ���� DB���� ������
	boolean idChecked = false; // id üũ����
	boolean flag = true;

	public RegistMember(Main main) {
		this.main = main;

		this.setLayout(new BorderLayout());

		p_east = new JPanel(); // ��
		p_west = new JPanel(); // ��
		p_center = new JPanel(); // ����
		p_south = new JPanel(); // ��
		la_id = new JLabel("���̵�");
		la_name = new JLabel("�̸�");
		la_password = new JLabel("��й�ȣ");
		la_email = new JLabel("�̸���");
		la_phone = new JLabel("��ȭ��ȣ");
		t_id = new JTextField(15);
		t_name = new JTextField(15);
		t_password = new JTextField(15);
		t_email = new JTextField(15);
		t_phone = new JTextField(15);
		bt_check = new JButton("�ߺ�üũ");
		bt_add = new JButton("���");
		// �� ������
		Dimension la_dm = new Dimension(100, 25);
		la_id.setPreferredSize(la_dm);
		la_name.setPreferredSize(la_dm);
		la_password.setPreferredSize(la_dm);
		la_email.setPreferredSize(la_dm);
		la_phone.setPreferredSize(la_dm);
		p_west.setPreferredSize(new Dimension(100, 300));
		// ���� ����
		p_west.add(la_id);
		p_center.add(t_id);
		p_east.add(bt_check); // �ߺ�üũ ��ư
		p_west.add(la_name);
		p_center.add(t_name);
		p_west.add(la_password);
		p_center.add(t_password);
		p_west.add(la_email);
		p_center.add(t_email);
		p_west.add(la_phone);
		p_center.add(t_phone);
		// ���� ��Ϲ�ư
		p_south.add(bt_add);
		// p_center.setLayout(new GridLayout(5,2));
		add(p_east, BorderLayout.EAST);
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_south, BorderLayout.SOUTH);

		// ���
		bt_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regist();
			}
		});

		// ��Ͽ��� X��ư ������ â �ݴ� �̺�Ʈ
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

		// �ߺ�üũ
		bt_check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkId();
			}
		});

		setLocationRelativeTo(null);
		setSize(400, 300);
		setVisible(flag);

	}

	// ���
	public void regist() {
		// �ߺ�Ȯ���� ������ ���԰���
		if (idChecked == false) { // �ߺ� üũ
			JOptionPane.showMessageDialog(this, "���̵� �ߺ�üũ ��������");
			return;
		}

		String id = t_id.getText();
		String name = t_name.getText();
		String password = t_password.getText();
		String email = t_email.getText();
		String phone = t_phone.getText();

		if (id.length() == 0) {
			JOptionPane.showMessageDialog(this, "���̵� �Է��ϼ���");
			t_id.requestFocus();// Ŀ�� �ø���
			return;
		}

		if (name.length() == 0) {
			JOptionPane.showMessageDialog(this, "�̸��� �Է��ϼ���");
			t_name.requestFocus();// Ŀ�� �ø���
			return;
		}

		if (password.length() == 0) {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� �Է��ϼ���");
			t_password.requestFocus();// Ŀ�� �ø���
			return;
		}

		if (email.length() == 0) {
			JOptionPane.showMessageDialog(this, "�̸����� �Է��ϼ���");
			t_email.requestFocus();// Ŀ�� �ø���
			return;
		}

		if (phone.length() == 0) {
			JOptionPane.showMessageDialog(this, "��ȭ��ȣ�� �Է��ϼ���");
			t_phone.requestFocus();// Ŀ�� �ø���
			return;
		}
		// flag=false; //��Ͽ� �����ϸ� visible�� false��

		String sql = "insert into member(id,name,password,email,phone)";
		sql += " values('" + id + "','" + name + "','" + password + "','" + email + "','" + phone + "')";

		System.out.println(sql);
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			int result = pstmt.executeUpdate();
			if (result == 0) {
				JOptionPane.showMessageDialog(this, "���Խ���");
			} else {
				JOptionPane.showMessageDialog(this, "���Լ���");
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

	// �ߺ�üũ
	public void checkId() {
		String id = t_id.getText();
		String sql = "select * from member where id='" + id + "'";
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {// true�� ���� �Ұ�
				JOptionPane.showMessageDialog(this, "�̹� ������� ���̵��Դϴ�");
			} else {
				idChecked = true;
				JOptionPane.showMessageDialog(this, "��� ������ ���̵��Դϴ�");
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
