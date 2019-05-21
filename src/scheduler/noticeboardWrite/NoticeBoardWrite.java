/*�� Ŭ������ �۾��� ��ư�� �������ÿ� ���Ǵ� Ŭ���� �Դϴ�*/
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
	JButton bt_back;// �ڷΰ��� ��ư
	JTextField field;
	String[] font_list = { "���� ���", "����", "����ü", "����", "����ü", "����", "����ü", "�ü�", "Hy����M", "HY����B", "�޸ո���", "���ʷչ���",
			"HY�׷���" };
	String[] font_s = { "10", "11", "12", "13", "14", "15", "16" };
	Choice choice, choice_size;
	JPanel p_north, p_south;

	String font_name;
	int size;
	// DataBase
	Connection con;

	public NoticeBoardWrite(Main main) {
		this.main = main;

		label = new JLabel("����");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		// setFont(new Font("����", Font.PLAIN, 25));
		bt_register = new JButton("���");
		bt_back = new JButton("�ڷΰ���");
		field = new JTextField(40);
		p_north = new JPanel();
		p_south = new JPanel();
		choice = new Choice();
		// font_list=getLocalFontNameList();
		System.out.println("���� ������ font" + font_list);
		// font_list[]=choice.getFontName();
		choice_size = new Choice();
		
		for (int i = 0; i < font_list.length; i++) {

			choice.add(font_list[i]);

		}
		for (int i = 10; i < 40; i++) {
			choice_size.add(Integer.toString(i));
		}

		Font font = new Font("����", Font.PLAIN, 20);// �⺻�� PLAIN
		area.setFont(font);
		choice.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				String font_name = choice.getSelectedItem();
				System.out.println("���� ������ �۲���?" + font_name);
				Font font = new Font(font_name, Font.PLAIN, size);// �⺻�� PLAIN
				area.setFont(font);
			}
		});

		choice_size.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				int size = choice_size.getSelectedIndex();
				System.out.println("���� ������ ��Ʈ�������??" + size);
				Font font = new Font(font_name, Font.PLAIN, size);// �⺻�� PLAIN
				area.setFont(font);
			}
		});
		
		// p_north�� ������Ű��
		p_north.add(label, BorderLayout.NORTH);
		label.setFont(new Font("����", Font.BOLD, 17));
		p_north.add(field);
		p_north.add(choice);
		p_north.add(choice_size);

		this.setLayout(new BorderLayout());

		// p_south�� ��ư ��������!!
		p_south.add(bt_register);
		p_south.add(bt_back);

		// bt_register�� �׼��̺�Ʈ �ο�!!
		bt_register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// NOTICEBOARD=>�Խ��� ������
				// ��������ϴ� ���

				registerNoticeBoard();
				NoticeBoard nb = (NoticeBoard) (main.mainFrame.getPages(2));
				nb.showTable();
			
				// main.mainFrame.showPage(main.mainFrame.NOTICEBOARD);
				main.mainFrame.showPage(2);

			}
		});

		// bt_back�� �׼��̺�Ʈ �ο�!!
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
		// this ��, JPanel�� ������Ű��!!
		this.add(p_north, BorderLayout.NORTH);
		this.add(p_south, BorderLayout.SOUTH);
		this.add(scroll);
		this.setPreferredSize(new Dimension(782, 540));
		this.setVisible(false);
	}
	/*
	 * @Override public void actionPerformed(ActionEvent e) {
	 * //main.mainFrame.showPage(main.mainFrame.NOTICEBOARD);//NOTICEBOARD=>�Խ��� ������
	 * ��������ϴ� ���
	 * 
	 * //�ڷΰ���!! NoticeBoardWrite.this.setVisible(false); boardMain.showPage(0); }
	 */

	// ��Ϲ�ư�� ���������� �Լ�
	public void registerNoticeBoard() {
		con = main.getCon();

		PreparedStatement pstmt = null;
		String title = field.getText();// ������� �Է¹��� Text�� �޾ƿ��ڴ�
		String content = area.getText();// �������¶��� �Է¹��� Text�� �޾ƿ��ڴ�
		// ������ write�÷��� int���̶�
		int writer = Integer.parseInt(main.userInfo.getId());

		String sql = "insert into board(title,content,writer,create_date)";
		// ��
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
				System.out.println("��Ͻ���..");
			} else {
				System.out.println("��ϼ���!!");
				NoticeBoardWrite.this.setVisible(false);
				// boardMain.showPage(0);
				JOptionPane.showMessageDialog(this, "���������� ��ϵǾ����ϴ�.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		main.closeDB(pstmt);
	}

}
