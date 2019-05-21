package scheduler.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrameMouseAdapter extends MouseAdapter {
	MainFrame mainFrame;

	public FrameMouseAdapter(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource(); // 선택된 이벤트 받아옴

		if (obj == mainFrame.menu[0]) {// 달력
			mainFrame.showPage(MainFrame.CALENDAR);
		} else if (obj == mainFrame.menu[1]) { // 일정등록
			mainFrame.showPage(MainFrame.SCHEDULE);
		} else if (obj == mainFrame.menu[2]) { // 게시판
			mainFrame.showPage(MainFrame.NOTICEBOARD);
		} else if (obj == mainFrame.menu[3]) { // 정보수정
			mainFrame.showPage(MainFrame.MEMBERMODIFIED);
		} else if (obj == mainFrame.menu[4] && mainFrame.admin) { // 회원관리
			mainFrame.showPage(MainFrame.MEMBERMANAGER);
		}
	}
	

}
