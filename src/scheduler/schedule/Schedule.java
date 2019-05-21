//���������
package scheduler.schedule;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import scheduler.calendar.Cell;
import scheduler.calendar.MyCalendar;
import scheduler.main.Main;
import scheduler.main.db.DBConnecter;

public class Schedule extends JPanel {
	Main main;

	Cell cell;
	MyCalendar cal;
	JTextField t_title; // ����
	JTextField t_name; // �ۼ���
	JTextArea t_main; // ����

	// JTextField t_start; // ������
	JTextField t_end; // ������
	JXDatePicker st_picker = new JXDatePicker();
	JXDatePicker end_picker = new JXDatePicker();
	Choice c_phase;

	JPanel p_north;
	JPanel p_center;
	JPanel p_souht;

	String name;
	String file_loc;
	JButton bt_regist;

	public Schedule(Main mian) {
		this.main = main;
		setLayout(new BorderLayout());
		// �г�
		p_north = new JPanel();
		p_center = new JPanel();
		p_souht = new JPanel();
		// ���ùڽ�
		c_phase = new Choice();
		c_phase.add("������");
		c_phase.add("������");
		c_phase.add("�Ϸ�");
		// �ؽ�Ʈ�ʵ� ,������
		t_title = new JTextField(68); // ����
		t_name = new JTextField(name, 8); // �ۼ���

		t_main = new JTextArea(50, 65); // ���� ����

		bt_regist = new JButton("    �� ��    ");

		p_north.setPreferredSize(new Dimension(800, 60));
		p_center.setPreferredSize(new Dimension(800, 400));
		p_souht.setPreferredSize(new Dimension(800, 100));

		t_name.setEnabled(false);
		t_name.setBackground(new Color(189, 189, 189));

		Dimension d = new Dimension(350, 25);
		t_title.setPreferredSize(d);

		p_north.add(new JLabel("����")); // Ÿ��Ʋ ��
		p_north.add(t_title);
		p_north.add(new JLabel("������")); // ������ ��

		p_north.add(st_picker);
		p_north.add(new JLabel("������")); // ������ ��
		p_north.add(end_picker);
		p_north.add(new JLabel("�ۼ���"));
		p_north.add(t_name);
		p_north.add(new JLabel("��������"));
		p_north.add(c_phase);

		p_center.add(t_main);
		p_souht.add(bt_regist);

		st_picker.setDate(Calendar.getInstance().getTime());
		st_picker.setBounds(50, 130, 100, 25);

		end_picker.setDate(Calendar.getInstance().getTime());
		end_picker.setBounds(50, 130, 100, 25);
		setLayout(new BorderLayout());

		add(p_north, BorderLayout.NORTH);
		add(p_center);
		add(p_souht, BorderLayout.SOUTH);

		bt_regist.addActionListener(new ActionListener() {// ���� ����ϱ�

			@Override
			public void actionPerformed(ActionEvent e) {
				regist();

				// cal.selectAll();
			}
		});

		setSize(800, 600);
		setVisible(false);

	} // ������ end

	public void regist() {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;

		/*
		 * calendar_no int primary key ��ϼ��� title varchar(50) not null ���� start_date
		 * TIMESTAMP ������ end_date TIMESTAMP ������ writer_no int member���� �ۼ��� stop_no int
		 * �������� content text ����
		 */
		// int writer_no = t_name.getText();
		String title = t_title.getText();

		Date st_date = st_picker.getDate();
		Date ed_date = end_picker.getDate();

		Timestamp start_date = new Timestamp(st_date.getTime());
		Timestamp end_date = new Timestamp(ed_date.getTime());
		int writer_no = 1; // ���߿� �ٲ���� ����� �����̸Ӹ�Ű�� �� �� �ۼ���
		int step_no = c_phase.getItemCount();
		String content = t_main.getText();

		System.out.println(start_date);
		// yyyy-mm-dd hh:mm:ss
		// ����~~~~

		String sql = "insert into schedule(title,start_date,end_date,writer_no,step_no,content)";
		sql += " values('" + title + "','" + start_date + "','" + end_date + "'," + writer_no + "," + step_no + ",'"
				+ content + "')";
		System.out.println(sql);

		try {

			pstmt = con.prepareStatement(sql);
			int result = pstmt.executeUpdate();
			if (result == 0) {
				JOptionPane.showMessageDialog(this, "��Ͻ���");
			} else {
				insertCal(st_date, ed_date);
				JOptionPane.showMessageDialog(this, "��ϼ���");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public void insertCal(Date st_date, Date ed_date) {// �޷¿� ������ ���ڸ� ��� calendar���̺���
		/*
		 * �ݺ��� ������ ~ ������ �� ex) 1/1 ~ 1/5 .... getLast 5 (5, 1/1), (5, 1/2), ... (5, 1/5)
		 */
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		String insert_schedule = getLast();// �������� �μ�Ʈ ������ �����쿡 ������ ����ϰ��� ���� �����ٰ��� ���!
		long diff = ed_date.getTime() - st_date.getTime();
		long diffdays = diff / (24 * 60 * 60 * 1000);
		System.out.println(diffdays);
		// date������
		for (int i = 0; i <= diffdays; i++) {// �������������� �˾ƾ��� �����쿡 ����� ������ Ű���� ������� getlast�� �������� Ű����
			// now_date = ������ +i��
			// db���μ�Ʈ
			// ���ݳ�¥�� �����쿡 ����� Ű�� db�� �μ�Ʈ
			// insert into (scid,date) value (insert_������,now����Ʈ)
			Date n_date = new Date(st_date.getYear(), st_date.getMonth(), st_date.getDate() + i);
			java.sql.Date now_date = new java.sql.Date(n_date.getTime());
			String sql = "insert into calendar(schedule_no,calendar_date)";
			sql += " values(?,?)";
			System.out.println(sql);
			System.out.println(now_date);
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, insert_schedule);
				pstmt.setDate(2, now_date);
				int result = pstmt.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

			}
		}

		// getLast();
	}

	public String getLast() { // �������� ��ϵ� ������ id���� ã���� Ķ������ ���� �������� �˾Ƴ��� ���� �Ȱ��� ������ ������ ������������ �߰��ϱ�����
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select last_insert_id()";
		String key = null;
		System.out.println(con);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			// System.out.println(rs.getString(1));
			key = rs.getString(1);

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
		return key;
	}

}
