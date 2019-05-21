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

public class EditPage extends JPanel {
	
	// bt_search= 검색버튼 bt_write=글쓰기 버튼
	JButton bt_search, bt_write;
	JTable table;
	EditModel editModel;
	JScrollPane scroll;// 테이블을 붙이기 위한 스크롤
	JScrollPane scroll2;// 에어리어를 붙일 스크롤

	JPanel p_north, p_south;
	// 나는 Main클래스를 사용할거다 그렇기에 멤버변수로 받는다.
	Main main;
	// NoticeBoardMain noticeBoardMain;
	// Connection con;
	NoticeBoardWrite noticeBoardWrite;

	//search 관련
	int search;
	
	int no;
	String all;
	
	boolean flag=true;
	
	Connection con;
	public EditPage(Main main) {
		this.main = main;


		
		bt_search = new JButton("검색");
		bt_write = new JButton("글쓰기");



		editModel = new EditModel(this);
		table = new JTable();
		scroll = new JScrollPane(table);
		// scroll2=new JScrollPane(area);
		p_north = new JPanel();
		p_south = new JPanel();
		p_south.setLayout(new BorderLayout());
		// south와 center로 나눈 패널안에서 south패널을 한번더 둘로 나누었다(p_s_north,p_s_south)

		//noticeBoardWrite = new NoticeBoardWrite(this);//

		this.add(p_north);// 전체 JPanel에 p_center패널 부착
		p_north.setBackground(new Color(255, 255, 255));

		p_north.add(scroll);


		// p_s_south 꺼!!
		bt_search.setBackground(new Color(000, 204, 000));
		bt_search.setPreferredSize(new Dimension(100,30));
		bt_write.setBackground(new Color(255, 255, 255));
		bt_write.setFont(new Font("굴림", Font.PLAIN, 15));
		bt_write.setPreferredSize(new Dimension(100, 30));
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

		// 테이블과 마우스리스너 연결!!
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int col = 0;
				int choice =  (int) table.getValueAt(row, col);
				System.out.println("내가 선택한 choice는 "+choice);
				/*
				 * getValueAt이 Object를 반환하므로 int형인 choice와 맞지않는다!! 따라서 Object인 getValueAt()을
				 * int로 형변환해준다!!
				 */
				
				showTable() ;
				ClickedPage cp=(ClickedPage)(main.mainFrame.getPages(6));
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

		/*bt_del.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				//boardMain.showPage(4);
			}
		});*/
		

		


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

		String sql = "select board_no ,title ,writer,create_date,hits from board";
		// select date_sub("1998-01-02", interval 31 day);

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

			editModel.columnName = columnName;

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
				//data[i][4] = rs.getString("modi_date");
				data[i][4] = rs.getInt("hits");
				//data[i][7] = rs.getInt("font");
			} // 여기를 다시 확인해볼것!!

			editModel.data = data;
			table.setModel(editModel);

			table.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 마지막으로 종료
		main.closeDB(pstmt, rs);
	}

}
