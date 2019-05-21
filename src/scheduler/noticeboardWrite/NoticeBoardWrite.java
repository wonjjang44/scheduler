/*이 클래스는 글쓰기 버튼은 눌렀을시에 사용되는 클래스 입니다*/
package scheduler.noticeboardWrite;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import scheduler.main.Main;
import scheduler.noticeboard.ClickedPage;
import scheduler.noticeboard.NoticeBoard;

public class NoticeBoardWrite extends JPanel /* implements ActionListener */ {
	Main main;

	JLabel label;
	JTextArea area;
	JScrollPane scroll;
	JButton bt_register;
	JButton bt_back;// 뒤로가기 버튼
	JTextField field;
	String[] font_list = { "맑은 고딕", "돋움", "돋움체", "굴림", "굴림체", "바탕", "바탕체", "궁서", "Hy엽서M", "HY수평선B", "휴먼명조", "함초롱바탕",
			"HY그래픽" };
	String[] font_s = { "10", "11", "12", "13", "14", "15", "16" };
	Choice choice, choice_size;
	JPanel p_north, p_south;

	String font_name;
	int size;
	// DataBase
	Connection con;

	public NoticeBoardWrite(Main main) {
		this.main = main;

		label = new JLabel("제목");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		// setFont(new Font("돋움", Font.PLAIN, 25));
		bt_register = new JButton("등록");
		bt_back = new JButton("뒤로가기");
		field = new JTextField(40);
		p_north = new JPanel();
		p_south = new JPanel();
		choice = new Choice();
		// font_list=getLocalFontNameList();
		System.out.println("내가 선택한 font" + font_list);
		// font_list[]=choice.getFontName();
		choice_size = new Choice();
		
		for (int i = 0; i < font_list.length; i++) {

			choice.add(font_list[i]);

		}
		for (int i = 10; i < 40; i++) {
			choice_size.add(Integer.toString(i));
		}

		Font font = new Font("돋움", Font.PLAIN, 20);// 기본값 PLAIN
		area.setFont(font);
		choice.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				String font_name = choice.getSelectedItem();
				System.out.println("내가 선택한 글꼴은?" + font_name);
				Font font = new Font(font_name, Font.PLAIN, size);// 기본값 PLAIN
				area.setFont(font);
			}
		});

		choice_size.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				int size = choice_size.getSelectedIndex();
				System.out.println("내가 선택한 폰트사이즈는??" + size);
				Font font = new Font(font_name, Font.PLAIN, size);// 기본값 PLAIN
				area.setFont(font);
			}
		});
		
		// p_north에 부착시키자
		p_north.add(label, BorderLayout.NORTH);
		label.setFont(new Font("굴림", Font.BOLD, 17));
		p_north.add(field);
		p_north.add(choice);
		p_north.add(choice_size);

		this.setLayout(new BorderLayout());

		// p_south에 버튼 부착하자!!
		p_south.add(bt_register);
		p_south.add(bt_back);

		// bt_register에 액션이벤트 부여!!
		bt_register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// NOTICEBOARD=>게시판 페이지
				// 열때사용하는 상수

				registerNoticeBoard();
				NoticeBoard nb = (NoticeBoard) (main.mainFrame.getPages(2));
				nb.showTable();
			
				// main.mainFrame.showPage(main.mainFrame.NOTICEBOARD);
				main.mainFrame.showPage(2);

			}
		});

		// bt_back에 액션이벤트 부여!!
		bt_back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NoticeBoardWrite.this.setVisible(false);
				// (NoticeBoard)main.mainFrame.getPages(2);
				NoticeBoard nb = (NoticeBoard) main.mainFrame.getPages(2);
				nb.showTable();
				main.mainFrame.showPage(2);
			}
		});

	
		area.setRequestFocusEnabled(true);
		// this 즉, JPanel에 부착시키자!!
		this.add(p_north, BorderLayout.NORTH);
		this.add(p_south, BorderLayout.SOUTH);
		this.add(scroll);
		this.setPreferredSize(new Dimension(782, 540));
		this.setVisible(false);
	}
	/*
	 * @Override public void actionPerformed(ActionEvent e) {
	 * //main.mainFrame.showPage(main.mainFrame.NOTICEBOARD);//NOTICEBOARD=>게시판 페이지
	 * 열때사용하는 상수
	 * 
	 * //뒤로가기!! NoticeBoardWrite.this.setVisible(false); boardMain.showPage(0); }
	 */

	// 등록버튼을 눌렀을때의 함수
	public void registerNoticeBoard() {
		con = main.getCon();

		PreparedStatement pstmt = null;
		String title = field.getText();// 제목란에 입력받은 Text를 받아오겠다
		String content = area.getText();// 내용적는란에 입력받은 Text를 받아오겠다
		// ★현재 write컬럼이 int형이라서
		int writer = Integer.parseInt(main.userInfo.getId());

		String sql = "insert into board(title,content,writer,create_date)";
		// ★
		sql += " values(?,?,?,now())";
		// SELECT CONVERT(date,GETDATE())

		try {

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, writer);

			int result = pstmt.executeUpdate();
			System.out.println(result);
			if (result == 0) {
				System.out.println("등록실패..");
			} else {
				System.out.println("등록성공!!");
				NoticeBoardWrite.this.setVisible(false);
				// boardMain.showPage(0);
				JOptionPane.showMessageDialog(this, "성공적으로 등록되었습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		main.closeDB(pstmt);
	}

}
