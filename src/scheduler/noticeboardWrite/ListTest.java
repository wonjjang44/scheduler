package scheduler.noticeboardWrite;

import java.util.ArrayList;
import java.util.List;

public class ListTest {
	String[] item_list= {"����","�޸հ��ü","�ü�","�������"};
	List list=new ArrayList();
	public ListTest() {
		for (int i = 0; i < item_list.length; i++) {
			String obj = item_list[i];
			list.add(obj);
		}
		System.out.println(list);
	}
	public static void main(String[] args) {
		new ListTest();
	}
}
