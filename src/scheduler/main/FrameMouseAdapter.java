package scheduler.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrameMouseAdapter extends MouseAdapter {
	MainFrame mainFrame;

	public FrameMouseAdapter(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource(); // ���õ� �̺�Ʈ �޾ƿ�

		if (obj == mainFrame.menu[0]) {// �޷�
			mainFrame.showPage(MainFrame.CALENDAR);
		} else if (obj == mainFrame.menu[1]) { // �������
			mainFrame.showPage(MainFrame.SCHEDULE);
		} else if (obj == mainFrame.menu[2]) { // �Խ���
			mainFrame.showPage(MainFrame.NOTICEBOARD);
		} else if (obj == mainFrame.menu[3]) { // ��������
			mainFrame.showPage(MainFrame.MEMBERMODIFIED);
		} else if (obj == mainFrame.menu[4] && mainFrame.admin) { // ȸ������
			mainFrame.showPage(MainFrame.MEMBERMANAGER);
		}
	}
	

}
