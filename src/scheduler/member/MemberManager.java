package scheduler.member;

import java.awt.BorderLayout;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import scheduler.main.Main;
import scheduler.main.MainFrame;

public class MemberManager extends JPanel { // 관리자용 페이지
	Main main;
	Connection con;
	MainFrame mainFrame;

	JTable table;
	JScrollPane scroll;
	AbstractTableModel model;
	JButton bt_del;
	JButton bt_reset;
	JButton bt_search;
	JLabel la_search;
	JTextField t_search;
	JPanel p_north;
	JPanel p_south;
	JPanel p_east;

	String[][] data;
	String[] columnTitle;

	public MemberManager(Main main) {
		this.main = main;
		table = new JTable();
		scroll = new JScrollPane(table);
		// 버튼
		bt_del = new JButton("회원삭제");
		bt_reset = new JButton("비밀번호 초기화");
		// 남쪽
		bt_search = new JButton("검 색");
		la_search = new JLabel("검 색");
		t_search = new JTextField(25);

		p_north = new JPanel();
		p_east = new JPanel();
		// p_center=new JPanel();
		p_south = new JPanel();
		p_east.add(bt_del);
		p_east.add(bt_reset);

		this.setLayout(new BorderLayout());
		add(scroll);
		p_south.add(la_search);
		p_south.add(t_search);
		p_south.add(bt_search);

	}
}
