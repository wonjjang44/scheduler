/*
 * 패널들 추가되는 메인 프레임
 * */

// 달력 불러올때 새로고침 되는거 추가 해야됨

package scheduler.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import scheduler.calendar.MyCalendar;
import scheduler.main.traymessage.MessageThread;
import scheduler.member.MemberManager;
import scheduler.member.MemberModified;
import scheduler.noticeboard.ClickedPage;
import scheduler.noticeboard.NoticeBoard;
import scheduler.noticeboardWrite.NoticeBoardWrite;
import scheduler.schedule.Schedule;

public class MainFrame extends JFrame {
	public static final int CALENDAR = 0; // 캘린더 페이지 저장된 인덱스 번호
	public static final int SCHEDULE = 1; // 스케줄 페이지 저장된 인덱스 번호
	public static final int NOTICEBOARD = 2; // 게시판 페이지 저장된 인덱스 번호
	public static final int MEMBERMODIFIED = 3; // 정보 수정 페이지 저장된 인덱스 번호
	public static final int MEMBERMANAGER = 4; // 회원 관리 페이지 저장된 인덱스 번호
	public static final int BOARDWRITE = 5; // 게시판 글쓰게 페이지 저장된 인덱스 번호
	public static final int CLICKEDPAGE = 6; // 게시판 클릭시 게시판을 수정할 수 있는 인덱스 번호
	
	private static final int NONVIEWPAGECOUNT = 2; // 메뉴에 추가하지 않지만 사용하는 페이지 갯수

	Main main;
	Boolean admin = false; // 어드민 여부
	int user_num;

	MessageThread msgThread;

	JPanel container; // 화면 교체 시 컨테이너 될 패널
	JMenuBar bar;
	String[] menuTitle = { "달력", "일정등록", "게시판", "정보수정", "회원관리" }; // 출력되는 메뉴
	JMenu[] menu = new JMenu[menuTitle.length]; // 바에 저장할 메뉴 목록
	JPanel[] pages = new JPanel[menuTitle.length + NONVIEWPAGECOUNT]; // 화면에 보여줄 페이지 목록

	FrameMouseAdapter frameMouseAdapter;

	public MainFrame(Main main) {
		// 새로운 일정 등록하면 메시지 띄어주는 쓰레드
		msgThread = new MessageThread(main);
		msgThread.start();

		this.main = main;
		admin = main.userInfo.isAdmin(); // 어드민 계정인지 확인
		setIconImage(main.getIcon()); // 아이콘 설정
		setTitle(main.getTitle()); // 타이틀 설정

		frameMouseAdapter = new FrameMouseAdapter(this);

		container = new JPanel();
		bar = new JMenuBar();

		for (int i = 0; i < menuTitle.length; i++) { // 바에 메뉴 추가
			if (i == MEMBERMANAGER) {
				if (admin) { // 어드민 계정일 정우에만 맨 마지막(회원관리)추가
					menu[i] = new JMenu(menuTitle[i]);
					bar.add(menu[i]);
				}
			} else {
				menu[i] = new JMenu(menuTitle[i]);
				bar.add(menu[i]);
			}
		}

		// 바 추가
		setJMenuBar(bar);

		// 페이지 생성
		createPages();

		// 종료 이벤트
		addWindowListener(new WindowAdapter() {
			// 닫기버튼 누르면 동작
			public void windowClosing(WindowEvent e) {
				main.frameClose();
			}

			// 최소화 버튼 누르면 트레이 창으로 감
			public void windowIconified(WindowEvent e) {
				setVisible(false);
			}

		});

		// 내용 출력하는 패널 부착
		add(container);

		// 마우스 이벤트 등록
		for (int i = 0; i < menuTitle.length; i++) {
			if (i == MEMBERMANAGER) {
				if (admin) {
					menu[i].addMouseListener(frameMouseAdapter);
				}
			} else {
				menu[i].addMouseListener(frameMouseAdapter);
			}
		}

		// 화면 구성
		setSize(810, 650);
		centerWindow(this);
		setVisible(true);
	}

	// pages배열에 panel 추가하는 메서드
	private void createPages() {
		pages[CALENDAR] = new MyCalendar(main); // 달력
		pages[SCHEDULE] = new Schedule(main); // 일정등록
		pages[NOTICEBOARD] = new NoticeBoard(main); // 게시판
		pages[MEMBERMODIFIED] = new MemberModified(main); // 정보수정
		pages[MEMBERMANAGER] = new MemberManager(main); // 회원관리
		pages[BOARDWRITE] = new NoticeBoardWrite(main);
		pages[CLICKEDPAGE]=new ClickedPage(main);
		for (int i = 0; i < menuTitle.length + NONVIEWPAGECOUNT; i++) {
			container.add(pages[i]);
		}

	}

	public void showPage(int page) {
		if (page == CALENDAR) { // 캘린더 페이지 열 때 조건 추가
			MyCalendar tmpCal = (MyCalendar) pages[CALENDAR];
			tmpCal.setCal();
		}

		for (int i = 0; i < pages.length; i++) {
			if (pages[i] != null) {
				if (i == page) {
					pages[i].setVisible(true);

				} else {
					pages[i].setVisible(false);
				}
			}
		}
	}

	// 모니터 중간에 프레임 출력
	public static void centerWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	// 페이지 얻어오는 메서드
	public JPanel getPages(int index) {
		return pages[index];
	}
	

}
