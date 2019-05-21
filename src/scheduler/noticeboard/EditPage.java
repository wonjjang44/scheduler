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
	
	// bt_search= �˻���ư bt_write=�۾��� ��ư
	JButton bt_search, bt_write;
	JTable table;
	EditModel editModel;
	JScrollPane scroll;// ���̺��� ���̱� ���� ��ũ��
	JScrollPane scroll2;// ���� ���� ��ũ��

	JPanel p_north, p_south;
	// ���� MainŬ������ ����ҰŴ� �׷��⿡ ��������� �޴´�.
	Main main;
	// NoticeBoardMain noticeBoardMain;
	// Connection con;
	NoticeBoardWrite noticeBoardWrite;

	//search ����
	int search;
	
	int no;
	String all;
	
	boolean flag=true;
	
	Connection con;
	public EditPage(Main main) {
		this.main = main;


		
		bt_search = new JButton("�˻�");
		bt_write = new JButton("�۾���");



		editModel = new EditModel(this);
		table = new JTable();
		scroll = new JScrollPane(table);
		// scroll2=new JScrollPane(area);
		p_north = new JPanel();
		p_south = new JPanel();
		p_south.setLayout(new BorderLayout());
		// south�� center�� ���� �гξȿ��� south�г��� �ѹ��� �ѷ� ��������(p_s_north,p_s_south)

		//noticeBoardWrite = new NoticeBoardWrite(this);//

		this.add(p_north);// ��ü JPanel�� p_center�г� ����
		p_north.setBackground(new Color(255, 255, 255));

		p_north.add(scroll);


		// p_s_south ��!!
		bt_search.setBackground(new Color(000, 204, 000));
		bt_search.setPreferredSize(new Dimension(100,30));
		bt_write.setBackground(new Color(255, 255, 255));
		bt_write.setFont(new Font("����", Font.PLAIN, 15));
		bt_write.setPreferredSize(new Dimension(100, 30));
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

		// ���̺�� ���콺������ ����!!
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int col = 0;
				int choice =  (int) table.getValueAt(row, col);
				System.out.println("���� ������ choice�� "+choice);
				/*
				 * getValueAt�� Object�� ��ȯ�ϹǷ� int���� choice�� �����ʴ´�!! ���� Object�� getValueAt()��
				 * int�� ����ȯ���ش�!!
				 */
				
				showTable() ;
				ClickedPage cp=(ClickedPage)(main.mainFrame.getPages(6));
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

		/*bt_del.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				//boardMain.showPage(4);
			}
		});*/
		

		


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

		String sql = "select board_no ,title ,writer,create_date,hits from board";
		// select date_sub("1998-01-02", interval 31 day);

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

			editModel.columnName = columnName;

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
				//data[i][4] = rs.getString("modi_date");
				data[i][4] = rs.getInt("hits");
				//data[i][7] = rs.getInt("font");
			} // ���⸦ �ٽ� Ȯ���غ���!!

			editModel.data = data;
			table.setModel(editModel);

			table.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// ���������� ����
		main.closeDB(pstmt, rs);
	}

}
