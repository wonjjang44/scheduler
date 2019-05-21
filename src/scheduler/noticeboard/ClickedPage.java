package scheduler.noticeboard;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import scheduler.main.Main;

public class ClickedPage extends JPanel {

	Main main;
	NoticeBoard nb;

	JLabel label;
	JTextArea area;
	JScrollPane scroll;
	JButton bt_edit;
	JButton bt_back;// 뒤로가기 버튼
	JTextField field;
	Choice ch,choice_size;
	String[] item_list = { "맑은 고딕", "돋움", "돋움체", "굴림", "굴림체", "바탕", "바탕체", "궁서", "Hy엽서M", "HY수평선B", "휴먼명조", "함초롱바탕",
			"HY그래픽" };
	JPanel p_north, p_south;
	

	int choice;// 내가 고른 테이블
	
	
	// DataBase
	Connection con;

	String select;

	public ClickedPage(Main main) {
		this.main = main;
		label = new JLabel("제목");
		area = new JTextArea();
		scroll = new JScrollPane(area);

		bt_edit = new JButton("수정하기");
		bt_back = new JButton("뒤로가기");
		field = new JTextField(50);
		ch = new Choice();
		p_north = new JPanel();
		p_south = new JPanel();
		choice_size = new Choice();
		for (int i = 0; i < item_list.length; i++) {
			ch.add(item_list[i]);
		}
		for(int i = 10; i<40; i++) {
			
			choice_size.add(Integer.toString(i));
		}
		
		
		
		ch.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				String font_name = ch.getSelectedItem();
				System.out.println("넘어온 글꼴은?" + font_name);
				Font font = new Font(font_name, Font.PLAIN, 25);// 기본값 PLAIN
				area.setFont(font);
			}
		});
		
		// p_north에 부착시키자
		p_north.add(label, BorderLayout.WEST);
		p_north.setPreferredSize(new Dimension(800,100));
		label.setFont(new Font("굴림", Font.BOLD, 17));
		p_north.add(field);

		this.setLayout(new BorderLayout());
		// p_south에 버튼 부착하자!!
		// p_south.add(save);

		p_north.add(bt_back, BorderLayout.WEST);
		p_north.add(ch);
		p_north.add(choice_size);
		// Button edit
		bt_edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editing();

				JOptionPane.showMessageDialog(null, "변경된 사항을 성공적으로 등록하였습니다.");
				NoticeBoard nb = (NoticeBoard) main.mainFrame.getPages(2);
				nb.showTable();
				main.mainFrame.showPage(2);
				area.setText("");
			}
		});
		// Button back
		bt_back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ClickedPage.this.setVisible(false);
				main.mainFrame.showPage(2);
				area.setText("");

			}
		});
		// area.setText("");
		// this 즉, JPanel에 부착시키자!!
		this.add(p_north, BorderLayout.NORTH);
		this.add(p_south, BorderLayout.SOUTH);
		this.add(scroll);
	
		this.setPreferredSize(new Dimension(782, 540));
		this.setVisible(false);
	}

	// 내가 클릭한 게시물
	public void choiceBoard(int choice) {
		this.choice = choice;
		con = main.getCon();
		NoticeBoard nb = (NoticeBoard) main.mainFrame.getPages(2);
		nb.showTable();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		System.out.println("선택된 board_id?" + choice);

		String sql = "select title,content from board where board_no='" + choice + "'";

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				field.setText(rs.getString("title"));
				area.append(rs.getString("content"));

			}
			field.repaint();
			area.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		main.closeDB(pstmt, rs);
	}

	public void editing() {

		con = main.getCon();
		PreparedStatement pstmt = null;
		String title = field.getText();
		String content = area.getText();

		StringBuffer sb = new StringBuffer();
		System.out.println("내가 선택한 choice" + choice);
		sb.append("update board set content=?,title=?,create_date=now() where board_no=" + choice + "");
		try {
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, content);
			pstmt.setString(2, title);
			
			int result = pstmt.executeUpdate();
			if (result != 0) {
				System.out.println("수정성공");

			} else {
				System.out.println("수정실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		main.closeDB(pstmt);
	}

	public void userBoard() {
		con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT board_no, title, NAME, id, create_date, hits from member m, board b WHERE m.member_no = b.writer";

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next() == true) {

			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		main.closeDB(pstmt, rs);
	}
	
	
	public void userInterface() {
		p_south.add(bt_edit);
		repaint();
	}

}
