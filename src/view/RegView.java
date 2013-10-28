package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;

import model.LazyMysqlOP;

import business.Localization;

public class RegView extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4810991001781932899L;
	JFrame frame;
	JButton btnreg = new JButton("Submit");
	JButton btnstart = new JButton("start");
	JButton btncancel = new JButton("Quit");
	JTextArea ta = new JTextArea(8, 20);
	JPanel inputInfo = new JPanel(new GridLayout(5, 2));
	JPanel buttonPane = new JPanel();

	JLabel labelname = new JLabel("name");
	JLabel labelage = new JLabel("age");
	JLabel labelposition = new JLabel("position");
	JLabel labeldepartment = new JLabel("department");
	JLabel labelgender = new JLabel("gender");
	JTextField name = new JTextField(20);
	JTextField age = new JTextField(20);
	JTextField pos = new JTextField(20);
	JTextField depart = new JTextField(20);
	JTextField gender = new JTextField(20);

	RegView(JFrame f) {
		super(new BorderLayout());
		this.frame=f;
		this.add(buttonPane, BorderLayout.PAGE_END);
		this.add(inputInfo, BorderLayout.PAGE_START);

		buttonPane.add(btnstart);
		buttonPane.add(btnreg);
		buttonPane.add(btncancel);

		inputInfo.add(labelname);
		inputInfo.add(name);
		inputInfo.add(labelage);
		inputInfo.add(age);
		inputInfo.add(labelposition);
		inputInfo.add(pos);
		inputInfo.add(labeldepartment);
		inputInfo.add(depart);
		inputInfo.add(labelgender);
		inputInfo.add(gender);

	//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	frame.pack();
	//	frame.setVisible(true);

		btnstart.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				int re=JOptionPane.showConfirmDialog(null, "Start?");
				if(re==0)
				{
					frame.setVisible(false);
					Client.sentry();
				}
				else
					return;
			}

		});
		btnreg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendInfo();
			}
		});
		btncancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int re = JOptionPane.showConfirmDialog(null,
						"Do you want to quit?");
				if (re == 0)
					System.exit(0);
				else
					;
			}
		});
		
		gatherInfo();
	}
	
	public void gatherInfo() {


		Object[][] tableData = { 
				new Object[] { "os", Localization.getOSName() },
				new Object[] { "arch", Localization.getOSArch()},
				new Object[] { "version",Localization.getOSVersion() },
				new Object[] { "username", Localization.getUserName()} ,
				new Object[] { "ip", Localization.getIP2()} ,
				new Object[] { "mac", Localization.getMAC2() } 
				};

		Object[] columnTitle = { "general", "detail" };
		JTable table=new JTable(tableData , columnTitle);
		this.add(table,BorderLayout.CENTER);
		frame.pack();
		
		String tmp = "OS:" + Localization.getOSName() + "\n" + "Arch:"
				+ Localization.getOSArch() + "\n" + "Version:"
				+ Localization.getOSVersion() + "\n" + "Username:"
				+ Localization.getUserName() + "\n" + "IP:" + Localization.getIP2()
				+ "\n" + "MAC:" + Localization.getMAC2() + "\n";
		ta.setText(tmp);
	}
	
	protected void updateInfo() {

		LazyMysqlOP mo = null;
		mo = new LazyMysqlOP();
		String sql = "delete from computer where mac='"
				+ Localization.getMAC2() + "'";
		try {
			mo.execute(sql);
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
			mo.close();
			return;
		}

		sql = "insert into computer values('" + Localization.getOSName()
				+ "','" + Localization.getOSArch() + "','"
				+ Localization.getOSVersion() + "','" + Localization.getUserName()
				+ "','" + Localization.getIP2() + "','" + Localization.getMAC2()
				+ "','" + name.getText() + "','" + age.getText() + "','"
				+ pos.getText() + "','" + depart.getText() + "','"
				+ gender.getText() + "')";
		System.out.println(sql);
		try {
			mo.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			mo.close();
			return;
		}
		JOptionPane.showMessageDialog(null, "Successfully updated!");

	}



	public void sendInfo() {
		LazyMysqlOP mo = null;
		mo = new LazyMysqlOP();
		String sql = "insert into computer values('"
				+ Localization.getOSName() + "','" + Localization.getOSArch()
				+ "','" + Localization.getOSVersion() + "','"
				+ Localization.getUserName() + "','" + Localization.getIP2() + "','"
				+ Localization.getMAC2() + "','" + name.getText() + "','"
				+ age.getText() + "','" + pos.getText() + "','"
				+ depart.getText() + "','" + gender.getText() + "')";
		System.out.println(sql);
		try {
			mo.execute(sql);
		} catch (SQLException e) {
			mo.close();
			System.out.println(e.getMessage());
			int re = JOptionPane.showConfirmDialog(null,
					"You have already registered before! Do you want to update information?");
			if (re == 0)
				updateInfo();
			else if (re == 1)
				JOptionPane.showMessageDialog(null, "Nothing changed!");
			else
				;

			return;
		}
		JOptionPane.showMessageDialog(null, "Successfully registered!");
	}
}
