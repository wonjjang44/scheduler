//���� ������ ����ִ� ��
package scheduler.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scheduler.main.Main;

public class Cell extends JPanel {
	Main main;
	int year;
	int month;
	int days;
	JLabel la_day;
	String printDay;

	public Cell(Main main) {
		this.main = main;
		la_day = new JLabel();
		setLayout(new BorderLayout());
		add(la_day, BorderLayout.NORTH);
		setPreferredSize(new Dimension(60, 60));
		setBorder(BorderFactory.createLineBorder(Color.black));

	}

	public void setCellDate(int year, int month, int days) {
		this.year = year;
		this.month = month;
		this.days = days;

		if (year > 0) {// 1 Ķ���� ���̺��� ������ȸ - �����Ͱ� ������ ���� �߰��� + �̺�Ʈ�߰� �󺧿��� �ۼ��� + ���������� - �̺�Ʈ���� ��������ȭ������
						// �̵��ؼ� ������ ���γ��� ���
			//setSchedule();// ������������ ���ڸ� ��ȸ������ schedule_no�� �޾ƿͼ� �̰Ÿ� ������ schedule���� ��ȸ�ؼ� ������ ������ �������� �ּ�ó��
			
		}

	}

	// Ķ���� ���̺��� ���ó��ڿ� ������ ������ ȭ�鿡 �߰��ϴ� �޼���
	private void setSchedule() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Date this_date = new Date(year, month, days);
		java.sql.Date now_date = new java.sql.Date(this_date.getTime());
		try {
			con = main.getCon();
			String sql = "SELECT title, writer_no, calendar_date "
					+ "FROM schedule s, calendar c WHERE s.schedule_no=c.schedule_no,calendar_date=?";
			pstmt.setDate(1, now_date);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println(now_date);
			while (rs.next()) {
				int schedule_no = rs.getInt("schedule_no");
				Date calendar_date = rs.getDate("calendar_date");
				int write_no = rs.getInt("write_no");
				String title = rs.getString("title");

				JLabel tmpLabel = new JLabel();
				this.add(tmpLabel);
				tmpLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						String schedule = Integer.toString(schedule_no);
						tmpLabel.setText(write_no + "," + title);
					}
				});

			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	public void setCellColor(Color color) {
		this.setBackground(color);
	}

	public void setOtherMonthDay() {
		setCellColor(Color.GRAY);
		la_day.setText("");
	}

	public void setMonthDay() {
		setCellColor(null);
		la_day.setText(year + "." + (month + 1) + "." + days);

	}

}
