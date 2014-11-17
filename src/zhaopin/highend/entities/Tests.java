package zhaopin.highend.entities;

import java.util.List;

public class Tests {
	
	public static void main(String[] args)
	{
		//SqlAccess access=configAdapter.getAccessByDBKey("ZhaoPin.HighEnd.SeekerDB");
		
		//test();
		
		test();

	}
	
	private static void test()
	{
		String where=" ID>? and SeekerUserID>?";
		
		Object[] objs=new Object[2];
		
		objs[0]=5;
		
		objs[1]=1;
		
		zhaopin.highend.entities.PagerParameter parameter=new PagerParameter(1,10,0);
		
		List<EmailList> list=new EmailList().queryPageList(parameter,where,"EmailAddress desc",objs);
		
		System.out.println(parameter.ItemCount);
		
		System.out.println(list.size());
	}
	


}
