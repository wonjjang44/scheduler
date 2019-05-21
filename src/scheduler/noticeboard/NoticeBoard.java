package scheduler.noticeboard;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import scheduler.main.Main;
import scheduler.noticeboardWrite.NoticeBoardWrite;

public class NoticeBoard extends JPanel {

	// bt_search= �˻���ư bt_write=�۾��� ��ư
	JButton bt_search, bt_write;
	// JButton bt_edit;
	JTable table;
	JScrollPane scroll;// ���̺��� ���̱� ���� ��ũ��
	JScrollPane scroll2;// ���� ���� ��ũ��
	ObjectModel objectModel;

	JPanel p_north, p_south;
	JPanel p_s_north, p_s_south, p_s_south2;// �� �гε��� ��ư�� �˻���!!
	JTextField t_input;// �˻��� �� �ִ� ����
	String[] ch2_list = { /* "����+����", "����", "���ۼ���", "��۳���", "����ۼ���" */
			"�����ϼ���", "����", "�۹�ȣ" };
	Choice ch2 = new Choice();
	// ���� MainŬ������ ����ҰŴ� �׷��⿡ ��������� �޴´�.
	Main main;
	// NoticeBoardMain noticeBoardMain;
	// Connection con;
	// search ����
	int search;

	int no;
	String all;

	boolean flag = true;
	Connection con;
	ClickedPage cp;

	public NoticeBoard(Main main) {
		this.main = main;

		bt_search = new JButton("�˻�");
		bt_write = new JButton("�۾���");
		// bt_edit=new JButton("�Խñ۰���");

		objectModel = new ObjectModel(this);
		table = new JTable();
		scroll = new JScrollPane(table);
		// scroll2=new JScrollPane(area);
		p_north = new JPanel();
		p_south = new JPanel();
		p_south.setLayout(new BorderLayout());
		// south�� center�� ���� �гξȿ��� south�г��� �ѹ��� �ѷ� ��������(p_s_north,p_s_south)
		t_input = new JTextField(35);
		t_input.setPreferredSize(new Dimension(120, 30));
		p_s_north = new JPanel();
		p_s_south = new JPanel();
		p_s_south2 = new JPanel();

		this.add(p_north);// ��ü JPanel�� p_center�г� ����
		p_north.setBackground(new Color(255, 255, 255));
		p_south.add(p_s_north, BorderLayout.NORTH);// p_south�гο� p_s_north�г��� p_south���� �ؿ� ����

		for (int i = 0; i < ch2_list.length; i++) {// �굵 ��������
			ch2.add(ch2_list[i]);
		}
		p_north.add(scroll);

		// p_south�гα��� �Ʒ��ʿ� ������p_s_south�гο� ���̽�,Field,searchButton����
		p_s_south.add(ch2);
		p_s_south.add(t_input);
		p_s_south.add(bt_search);

		// p_s_south.add(bt_again, BorderLayout.EAST);
		// writeButton�� p_s_north�� ����
		p_s_south.add(bt_write);
		// p_s_north.setBackground(Color.red);
		// p_s_north.add(bt_del, BorderLayout.WEST);
		// bt_del.setPreferredSize(new Dimension(105, 30));
		// bt_del.setBackground(new Color(255, 255, 255));
		// bt_del.setFont(new Font("����", Font.PLAIN, 15));

		// p_s_south�� p_south�� ����
		// p_south.setLayout(new BorderLayout());
		p_south.add(p_s_south, BorderLayout.NORTH);
		// p_south.add(p_s_south2);
		// p_s_south2.setBackground(Color.red);///============================
		// p_s_south2.add(bt_edit);

		// p_s_south ��!!
		bt_search.setBackground(new Color(000, 204, 000));
		bt_search.setPreferredSize(new Dimension(100, 30));
		bt_write.setBackground(new Color(255, 255, 255));
		bt_write.setFont(new Font("����", Font.PLAIN, 15));
		bt_write.setPreferredSize(new Dimension(100, 30));
//		bt_edit.setPreferredSize(new Dimension(200,30));
//		bt_edit.setFont(new Font("����", Font.PLAIN, 15));
		ch2.setPreferredSize(new Dimension(125, 40));
		// ��ũ�� ���������!!
		scroll.setPreferredSize(new Dimension(750, 440));
		// table ���������!!
		table.setPreferredSize(new Dimension(700, 447));
		// �θ�Panel�� ���������!!
		this.setPreferredSize(new Dimension(800, 600));
		// �ڽ�Panel�� ���������!!
		p_south.setPreferredSize(new Dimension(800, 150));
		p_north.setPreferredSize(new Dimension(800, 450));

		// ���������� this ��, JPanel�� ���� add����!!
		this.add(p_north, BorderLayout.NORTH);
		this.add(p_south, BorderLayout.SOUTH);
		// p_north.setBackground(Color.red);
		// this.setBackground(Color.BLUE);
//		 cp=(ClickedPage)main.mainFrame.getPages(6);

		// ���̺�� ���콺������ ����!!
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int col = 0;
				String select = (String) (table.getValueAt(row, 2));
				int choice = (int) table.getValueAt(row, col);
				System.out.println("���� ������ choice�� " + choice);

				String writer = main.userInfo.getId();
				if (writer.equals(select)) {// String�� int�� ���Ҷ��� ����==�������� equals����..==���� �ȸ���...
					cp = (ClickedPage) (main.mainFrame.getPages(6));
					cp.userInterface();
				} else {		
					cp = (ClickedPage) (main.mainFrame.getPages(6));
					cp.p_south.remove(cp.bt_edit);
					cp.p_south.repaint();

				}

				System.out.println("selected_writer�� ?" + select);
				// System.out.println("���� cp" + cp);
				// ���⼭ ���� int���� cp.selected_writer���� ��밡���ϰ� �Ѵ�
				/*
				 * getValueAt�� Object�� ��ȯ�ϹǷ� int���� choice�� �����ʴ´�!! ���� Object�� getValueAt()��
				 * int�� ����ȯ���ش�!!
				 */
				hitsCount(choice);
				showTable();

				main.mainFrame.showPage(6);

				cp.choiceBoard(choice);
				table.updateUI();

			}
		});

		// ��ư�� ������ ����(�۾��� ��ư)
		bt_write.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.mainFrame.showPage(main.mainFrame.BOARDWRITE);
			}
		});

		/*
		 * bt_del.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent arg0) {
		 * 
		 * //boardMain.showPage(4); } });
		 */
		// ������ ch2�� ������ �˾ƾ���!!
		ch2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				search = ch2.getSelectedIndex();
				System.out.println("���� ������ ���̽���? " + search);
			}
		});

		// "�˻�"��ư�� �̺�Ʈ ����!!
		bt_search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("�Ѿ�� ���̽���?" + search);

				if (search == 0) {
					search();
				} else if (search == 1) {
					search();// �̸����� �˻��Ҷ�
				} else if (search == 2) {
					search();// �������� �˻��Ҷ�
				}
				t_input.setText("");

			}
		});

		// bt_again�� �����ʺο�
		/*
		 * bt_again.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { showTable(); } });
		 */

		showTable();

		// ���� �ʺ� �����ϰ�, ���� ����!!
		DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
		cellCenter.setHorizontalAlignment(JLabel.CENTER);
		DefaultTableCellRenderer cellRight = new DefaultTableCellRenderer();
		cellRight.setHorizontalAlignment(JLabel.RIGHT);

		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setPreferredWidth(110);
		table.getColumnModel().getColumn(3).setPreferredWidth(130);
		table.getColumnModel().getColumn(4).setPreferredWidth(40);
		table.setRowHeight(35);

		// ����� ����
		table.getColumnModel().getColumn(0).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(1).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(2).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(3).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(4).setCellRenderer(cellCenter);

		// ����ȿ�� on
		table.updateUI();
		this.setVisible(false);

	}

	// ���۰� ���ÿ� ���̺��� �����!!(�Խ��Ǹ��)
	public void showTable() {
		Connection con = main.getCon();
		// System.out.println("showTable"+con);
		// boardMain���� �����ϰ� �����Ƿ�!!
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// String sql = "select board_no ,title ,writer,create_date,hits from board";
		String sql = "select board_no ,title ,writer,create_date,hits from board";
		// select date_sub("1998-01-02", interval 31 day);
		// SELECT CONVERT(date,GETDATE())

		try {
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			// System.out.println("showTable rs��"+rs);
			// Į�������ؾ��ϹǷ� Meta���� �ö���
			ResultSetMetaData meta = rs.getMetaData();
			int columnCount = meta.getColumnCount();

			String[] columnName = new String[columnCount];

			for (int i = 0; i < columnCount; i++) {
				columnName[i] = meta.getColumnName(i + 1);
			}

			objectModel.columnName = columnName;

			rs.last();
			int total = rs.getRow();

			Object[][] data = new Object[total][columnCount];
			/* String�� ���� int�� ���� �𸣱⿡ Object�� �޴´�!! */

			// Ŀ���� �ǹ����� �������� �ٽ� ó������ �ø���!!
			rs.beforeFirst();

			// ä������!!
			for (int i = 0; i < total; i++) {
				rs.next();
				data[i][0] = rs.getInt("board_no");
				data[i][1] = rs.getString("title");
				data[i][2] = rs.getString("writer");
				data[i][3] = rs.getString("create_date");

				data[i][4] = rs.getInt("hits");

			} // ���⸦ �ٽ� Ȯ���غ���!!

			objectModel.data = data;
			table.setModel(objectModel);

			table.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// ���������� ����
		main.closeDB(pstmt, rs);
	}

	// �˻����!!
	public void search() {
		con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		if (t_input.getText().isEmpty()) {
			sql = "select board_no,title,writer,create_date,hits from board";
		}

		else if (search == 1) {
			all = t_input.getText() + "%%"; // �������� �˻�
			sql = "select board_no,title,writer,create_date,hits from board where title like '" + all + "'";
		} else if (search == 2) {
			no = Integer.parseInt(t_input.getText());
			sql = "select board_no,title,writer,create_date,hits from board where board_no='" + no + "'";
		}

		try {
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			// �÷��� �˱����ؼ� Meta���� �ø���!!
			ResultSetMetaData meta = rs.getMetaData();
			int columnCount = meta.getColumnCount();

			String[] columnName = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				columnName[i] = meta.getColumnName(i + 1);
			}

			rs.last();
			int total = rs.getRow();

			Object[][] data = new Object[total][columnCount];

			rs.beforeFirst();

			for (int i = 0; i < total; i++) {
				if (rs.next() == flag) {
					data[i][0] = rs.getInt("board_no");
					data[i][1] = rs.getString("title");
					data[i][2] = rs.getString("writer");
					data[i][3] = rs.getString("create_date");
					// data[i][4] = rs.getString("modi_date");
					data[i][4] = rs.getInt("hits");
				} else {// false�� ��ü �ٳ�������...
					showTable();
				}
			}
			objectModel.columnName = columnName;
			objectModel.data = data;
			table.updateUI();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// boardMain.connectionHelper.disCon(pstmt, rs);

	}

	// ��ȸ�� ���� �Լ�!!
	public void hitsCount(int hit) {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		System.out.println("��ȸ��" + hit);
		// String sql = "update board set hits=NVL(hits,0)+1 where board_no=" +
		// hit;//�̰Ŵ� ����Ŭ ������
		String sql = "UPDATE  board set hits =hits+1 where board_no=" + hit;// mysql����

		try {
			pstmt = con.prepareStatement(sql);
			int result = pstmt.executeUpdate();

			if (result == 0) {
				System.out.println("��ȸ������ ����..");

			} else {
				System.out.println("��ȸ�� 1 ����!!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
