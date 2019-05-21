//등록페이지
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
	JTextField t_title; // 제목
	JTextField t_name; // 작성자
	JTextArea t_main; // 내용

	// JTextField t_start; // 시작일
	JTextField t_end; // 종료일
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
		// 패널
		p_north = new JPanel();
		p_center = new JPanel();
		p_souht = new JPanel();
		// 선택박스
		c_phase = new Choice();
		c_phase.add("미진행");
		c_phase.add("진행중");
		c_phase.add("완료");
		// 텍스트필드 ,에리어
		t_title = new JTextField(68); // 제목
		t_name = new JTextField(name, 8); // 작성자

		t_main = new JTextArea(50, 65); // 일정 내용

		bt_regist = new JButton("    등 록    ");

		p_north.setPreferredSize(new Dimension(800, 60));
		p_center.setPreferredSize(new Dimension(800, 400));
		p_souht.setPreferredSize(new Dimension(800, 100));

		t_name.setEnabled(false);
		t_name.setBackground(new Color(189, 189, 189));

		Dimension d = new Dimension(350, 25);
		t_title.setPreferredSize(d);

		p_north.add(new JLabel("제목")); // 타이틀 라벨
		p_north.add(t_title);
		p_north.add(new JLabel("시작일")); // 시작일 라벨

		p_north.add(st_picker);
		p_north.add(new JLabel("종료일")); // 종료일 라벨
		p_north.add(end_picker);
		p_north.add(new JLabel("작성자"));
		p_north.add(t_name);
		p_north.add(new JLabel("진행정도"));
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

		bt_regist.addActionListener(new ActionListener() {// 일정 등록하기

			@Override
			public void actionPerformed(ActionEvent e) {
				regist();

				// cal.selectAll();
			}
		});

		setSize(800, 600);
		setVisible(false);

	} // 생성자 end

	public void regist() {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;

		/*
		 * calendar_no int primary key 등록순서 title varchar(50) not null 제목 start_date
		 * TIMESTAMP 시작일 end_date TIMESTAMP 종료일 writer_no int member참조 작성자 stop_no int
		 * 진행정도 content text 내용
		 */
		// int writer_no = t_name.getText();
		String title = t_title.getText();

		Date st_date = st_picker.getDate();
		Date ed_date = end_picker.getDate();

		Timestamp start_date = new Timestamp(st_date.getTime());
		Timestamp end_date = new Timestamp(ed_date.getTime());
		int writer_no = 1; // 나중에 바꿔야함 멤버의 프라이머리키가 들어갈 것 작성자
		int step_no = c_phase.getItemCount();
		String content = t_main.getText();

		System.out.println(start_date);
		// yyyy-mm-dd hh:mm:ss
		// 로직~~~~

		String sql = "insert into schedule(title,start_date,end_date,writer_no,step_no,content)";
		sql += " values('" + title + "','" + start_date + "','" + end_date + "'," + writer_no + "," + step_no + ",'"
				+ content + "')";
		System.out.println(sql);

		try {

			pstmt = con.prepareStatement(sql);
			int result = pstmt.executeUpdate();
			if (result == 0) {
				JOptionPane.showMessageDialog(this, "등록실패");
			} else {
				insertCal(st_date, ed_date);
				JOptionPane.showMessageDialog(this, "등록성공");

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

	public void insertCal(Date st_date, Date ed_date) {// 달력에 보여줄 날자를 등록 calendar테이블에함
		/*
		 * 반복문 시작일 ~ 끝나는 날 ex) 1/1 ~ 1/5 .... getLast 5 (5, 1/1), (5, 1/2), ... (5, 1/5)
		 */
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		String insert_schedule = getLast();// 마지막에 인서트 스케쥴 스케쥴에 일정을 등록하고나서 셀에 보여줄것을 등록!
		long diff = ed_date.getTime() - st_date.getTime();
		long diffdays = diff / (24 * 60 * 60 * 1000);
		System.out.println(diffdays);
		// date시작일
		for (int i = 0; i <= diffdays; i++) {// 무슨스케쥴인지 알아야함 스케쥴에 등록한 일정의 키값을 얻기위해 getlast는 마지막날 키값임
			// now_date = 시작일 +i일
			// db에인서트
			// 지금날짜랑 스케쥴에 등록한 키값 db에 인서트
			// insert into (scid,date) value (insert_스케쥴,now데이트)
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

	public String getLast() { // 마지막에 등록된 스케쥴 id값을 찾아줌 캘린더에 붙일 스케쥴을 알아내기 위해 똑같은 일정을 누를때 같은스케쥴이 뜨게하기위해
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
