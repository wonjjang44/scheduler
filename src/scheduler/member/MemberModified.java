package scheduler.member;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import scheduler.main.Main;
import scheduler.main.MainFrame;

public class MemberModified extends JPanel{ //정보수정
	Main main;
	Connection con;
	MainFrame mainFrame;

	// 디자인 명
	JPanel p_east;
	JPanel p_west;
	JPanel p_center;
	JPanel p_south;
	JLabel la_id;
	JLabel la_name;
	JLabel la_password;
	JLabel la_email;
	JLabel la_phone;
	JTextField t_id;
	JTextField t_name;
	JTextField t_password;
	JTextField t_email;
	JTextField t_phone;
	JButton bt_modified;   // 수정버튼
	
	String memberId; //현제 접속중인 유저
	String memberName;
	String memberPassword;
	String memberEmail;
	String memberPhone;
	
		

	public MemberModified(Main main) {
		this.main = main;
		
		con = main.getCon();
		
		this.setLayout(new BorderLayout());

		p_east = new JPanel(); // 동
		p_west = new JPanel(); // 서
		p_center = new JPanel(); // 센터
		p_south = new JPanel(); // 남
		la_id = new JLabel("아이디");
		la_name = new JLabel("이름");
		la_password = new JLabel("비밀번호");
		la_email = new JLabel("이메일");
		la_phone = new JLabel("전화번호");
		t_id = new JTextField(15);
		t_name = new JTextField(15);
		t_password = new JTextField(15);
		t_email = new JTextField(15);
		t_phone = new JTextField(15);
		bt_modified = new JButton("수정");
		
		// 라벨 사이즈
		Dimension la_dm = new Dimension(100, 22);
		la_id.setPreferredSize(la_dm);
		la_name.setPreferredSize(la_dm);
		la_password.setPreferredSize(la_dm);
		la_email.setPreferredSize(la_dm);
		la_phone.setPreferredSize(la_dm);
		p_west.setPreferredSize(new Dimension(100, 300));
		
		// 센터 부착
		p_west.add(la_id);
		p_center.add(t_id).setEnabled(false);
		p_west.add(la_name);
		p_center.add(t_name).setEnabled(false);
		p_west.add(la_password);
		p_center.add(t_password);
		p_west.add(la_email);
		p_center.add(t_email);
		p_west.add(la_phone);
		p_center.add(t_phone);
		
		// 남쪽 등록버튼
		p_south.add(bt_modified);
		// p_center.setLayout(new GridLayout(5,2));
		add(p_east, BorderLayout.EAST);
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_south, BorderLayout.SOUTH);
		
		memberId=main.userInfo.getId();
		memberName=main.userInfo.getName();
		memberEmail=main.userInfo.getEmail();
		memberPhone=main.userInfo.getPhone();
		
		t_id.setText(memberId);
		t_name.setText(memberName);
		t_email.setText(memberEmail);
		//패스워드
		t_password.setText(memberPassword);
		t_email.setText(memberEmail);
		t_phone.setText(memberPhone);
//		main.userInfo.getName();
//		main.userInfo.set

		// 수정
		bt_modified.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modified();
			}
		});
		this.setPreferredSize(new Dimension(300,200));
	}
	
	
	public void modified() { //update 쿼리
		String sql = "update member set id=";
	}
}
