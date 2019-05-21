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

	// bt_search= 검색버튼 bt_write=글쓰기 버튼
	JButton bt_search, bt_write;
	// JButton bt_edit;
	JTable table;
	JScrollPane scroll;// 테이블을 붙이기 위한 스크롤
	JScrollPane scroll2;// 에어리어를 붙일 스크롤
	ObjectModel objectModel;

	JPanel p_north, p_south;
	JPanel p_s_north, p_s_south, p_s_south2;// 이 패널들은 버튼과 검색란!!
	JTextField t_input;// 검색할 수 있는 공간
	String[] ch2_list = { /* "제목+내용", "제목만", "글작성자", "댓글내용", "댓글작성자" */
			"선택하세요", "제목", "글번호" };
	Choice ch2 = new Choice();
	// 나는 Main클래스를 사용할거다 그렇기에 멤버변수로 받는다.
	Main main;
	// NoticeBoardMain noticeBoardMain;
	// Connection con;
	// search 관련
	int search;

	int no;
	String all;

	boolean flag = true;
	Connection con;
	ClickedPage cp;

	public NoticeBoard(Main main) {
		this.main = main;

		bt_search = new JButton("검색");
		bt_write = new JButton("글쓰기");
		// bt_edit=new JButton("게시글관리");

		objectModel = new ObjectModel(this);
		table = new JTable();
		scroll = new JScrollPane(table);
		// scroll2=new JScrollPane(area);
		p_north = new JPanel();
		p_south = new JPanel();
		p_south.setLayout(new BorderLayout());
		// south와 center로 나눈 패널안에서 south패널을 한번더 둘로 나누었다(p_s_north,p_s_south)
		t_input = new JTextField(35);
		t_input.setPreferredSize(new Dimension(120, 30));
		p_s_north = new JPanel();
		p_s_south = new JPanel();
		p_s_south2 = new JPanel();

		this.add(p_north);// 전체 JPanel에 p_center패널 부착
		p_north.setBackground(new Color(255, 255, 255));
		p_south.add(p_s_north, BorderLayout.NORTH);// p_south패널에 p_s_north패널을 p_south기준 밑에 부착

		for (int i = 0; i < ch2_list.length; i++) {// 얘도 마찬가지
			ch2.add(ch2_list[i]);
		}
		p_north.add(scroll);

		// p_south패널기준 아래쪽에 부착된p_s_south패널에 초이스,Field,searchButton부착
		p_s_south.add(ch2);
		p_s_south.add(t_input);
		p_s_south.add(bt_search);

		// p_s_south.add(bt_again, BorderLayout.EAST);
		// writeButton을 p_s_north에 부착
		p_s_south.add(bt_write);
		// p_s_north.setBackground(Color.red);
		// p_s_north.add(bt_del, BorderLayout.WEST);
		// bt_del.setPreferredSize(new Dimension(105, 30));
		// bt_del.setBackground(new Color(255, 255, 255));
		// bt_del.setFont(new Font("굴림", Font.PLAIN, 15));

		// p_s_south를 p_south에 부착
		// p_south.setLayout(new BorderLayout());
		p_south.add(p_s_south, BorderLayout.NORTH);
		// p_south.add(p_s_south2);
		// p_s_south2.setBackground(Color.red);///============================
		// p_s_south2.add(bt_edit);

		// p_s_south 꺼!!
		bt_search.setBackground(new Color(000, 204, 000));
		bt_search.setPreferredSize(new Dimension(100, 30));
		bt_write.setBackground(new Color(255, 255, 255));
		bt_write.setFont(new Font("굴림", Font.PLAIN, 15));
		bt_write.setPreferredSize(new Dimension(100, 30));
//		bt_edit.setPreferredSize(new Dimension(200,30));
//		bt_edit.setFont(new Font("굴림", Font.PLAIN, 15));
		ch2.setPreferredSize(new Dimension(125, 40));
		// 스크롤 사이즈결정!!
		scroll.setPreferredSize(new Dimension(750, 440));
		// table 사이즈결정!!
		table.setPreferredSize(new Dimension(700, 447));
		// 부모Panel의 사이즈결정!!
		this.setPreferredSize(new Dimension(800, 600));
		// 자식Panel의 사이즈결정!!
		p_south.setPreferredSize(new Dimension(800, 150));
		p_north.setPreferredSize(new Dimension(800, 450));

		// 최종적으로 this 즉, JPanel에 전부 add하자!!
		this.add(p_north, BorderLayout.NORTH);
		this.add(p_south, BorderLayout.SOUTH);
		// p_north.setBackground(Color.red);
		// this.setBackground(Color.BLUE);
//		 cp=(ClickedPage)main.mainFrame.getPages(6);

		// 테이블과 마우스리스너 연결!!
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int col = 0;
				String select = (String) (table.getValueAt(row, 2));
				int choice = (int) table.getValueAt(row, col);
				System.out.println("내가 선택한 choice는 " + choice);

				String writer = main.userInfo.getId();
				if (writer.equals(select)) {// String과 int와 비교할때는 절때==쓰지말고 equals쓸것..==쓰면 안먹음...
					cp = (ClickedPage) (main.mainFrame.getPages(6));
					cp.userInterface();
				} else {		
					cp = (ClickedPage) (main.mainFrame.getPages(6));
					cp.p_south.remove(cp.bt_edit);
					cp.p_south.repaint();

				}

				System.out.println("selected_writer는 ?" + select);
				// System.out.println("나의 cp" + cp);
				// 여기서 구한 int값을 cp.selected_writer에서 사용가능하게 한다
				/*
				 * getValueAt이 Object를 반환하므로 int형인 choice와 맞지않는다!! 따라서 Object인 getValueAt()을
				 * int로 형변환해준다!!
				 */
				hitsCount(choice);
				showTable();

				main.mainFrame.showPage(6);

				cp.choiceBoard(choice);
				table.updateUI();

			}
		});

		// 버튼과 리스너 연결(글쓰기 버튼)
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
		// 선택한 ch2의 내용을 알아야지!!
		ch2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				search = ch2.getSelectedIndex();
				System.out.println("내가 선택한 초이스는? " + search);
			}
		});

		// "검색"버튼에 이벤트 주자!!
		bt_search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("넘어온 초이스는?" + search);

				if (search == 0) {
					search();
				} else if (search == 1) {
					search();// 이름으로 검색할때
				} else if (search == 2) {
					search();// 제목으로 검색할때
				}
				t_input.setText("");

			}
		});

		// bt_again에 리스너부여
		/*
		 * bt_again.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { showTable(); } });
		 */

		showTable();

		// 셀의 너비를 조정하고, 글자 정렬!!
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

		// 가운데로 조정
		table.getColumnModel().getColumn(0).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(1).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(2).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(3).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(4).setCellRenderer(cellCenter);

		// 가시효과 on
		table.updateUI();
		this.setVisible(false);

	}

	// 시작과 동시에 테이블을 띄우자!!(게시판목록)
	public void showTable() {
		Connection con = main.getCon();
		// System.out.println("showTable"+con);
		// boardMain에서 관리하고 있으므로!!
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// String sql = "select board_no ,title ,writer,create_date,hits from board";
		String sql = "select board_no ,title ,writer,create_date,hits from board";
		// select date_sub("1998-01-02", interval 31 day);
		// SELECT CONVERT(date,GETDATE())

		try {
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			// System.out.println("showTable rs는"+rs);
			// 칼럼조사해야하므로 Meta까지 올라가자
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
			/* String이 올지 int가 올지 모르기에 Object로 받는다!! */

			// 커서를 맨밑으로 내렸으니 다시 처음으로 올리자!!
			rs.beforeFirst();

			// 채워넣자!!
			for (int i = 0; i < total; i++) {
				rs.next();
				data[i][0] = rs.getInt("board_no");
				data[i][1] = rs.getString("title");
				data[i][2] = rs.getString("writer");
				data[i][3] = rs.getString("create_date");

				data[i][4] = rs.getInt("hits");

			} // 여기를 다시 확인해볼것!!

			objectModel.data = data;
			table.setModel(objectModel);

			table.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 마지막으로 종료
		main.closeDB(pstmt, rs);
	}

	// 검색기능!!
	public void search() {
		con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		if (t_input.getText().isEmpty()) {
			sql = "select board_no,title,writer,create_date,hits from board";
		}

		else if (search == 1) {
			all = t_input.getText() + "%%"; // 제목으로 검색
			sql = "select board_no,title,writer,create_date,hits from board where title like '" + all + "'";
		} else if (search == 2) {
			no = Integer.parseInt(t_input.getText());
			sql = "select board_no,title,writer,create_date,hits from board where board_no='" + no + "'";
		}

		try {
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			// 컬럼을 알기위해서 Meta까지 올리자!!
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
				} else {// false면 전체 다나오도록...
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

	// 조회수 증가 함수!!
	public void hitsCount(int hit) {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		System.out.println("조회수" + hit);
		// String sql = "update board set hits=NVL(hits,0)+1 where board_no=" +
		// hit;//이거는 오라클 쿼리문
		String sql = "UPDATE  board set hits =hits+1 where board_no=" + hit;// mysql쿼리

		try {
			pstmt = con.prepareStatement(sql);
			int result = pstmt.executeUpdate();

			if (result == 0) {
				System.out.println("조회수증가 실패..");

			} else {
				System.out.println("조회수 1 증가!!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
