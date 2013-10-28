package view;

import javax.swing.*;

public class ReportUI {
	public static void main(String[]args)
	{
		startCS();
	}
	public static void startCS()
	{
		JFrame frame=new JFrame();
		JTabbedPane tab = new JTabbedPane();
		
		RegView r1=new RegView(frame);
		ShowView r2=new ShowView(frame);
		
		frame.add(tab);
		tab.add("Guest",r1);
		tab.add("Admin",r2);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
	}
	public static void startClient()
	{
		JFrame frame=new JFrame();
		RegView r=new RegView(frame);
		frame.add(r);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
	}
	public static void startServer()
	{
		JFrame frame=new JFrame();
		ShowView r=new ShowView(frame);
		frame.add(r);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
