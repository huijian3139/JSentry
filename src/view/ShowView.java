package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.LazyMysqlOP;

public class ShowView extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1279942369199923217L;
	JFrame frame;
	JTable tableReg = new JTable();
	JTable tableEvent = new JTable();
	JTable tableBlack = new JTable();
	JTable tableCmd = new JTable();
	JTable tableBigData = new JTable();
	
	JTabbedPane tab = new JTabbedPane();
	JPanel panelReg = new JPanel( );
	JPanel panelEvent = new JPanel( );
	JPanel panelBlack = new JPanel();
	JPanel panelBlackButton = new JPanel();
	JPanel panelCmd = new JPanel();
	JPanel panelCmdButton = new JPanel();
	JPanel panelBigData = new JPanel();
	
	JScrollPane jspReg=new JScrollPane();
	JScrollPane jspEvent=new JScrollPane();
	JScrollPane jspBlack=new JScrollPane();
	JScrollPane jspCmd=new JScrollPane();
	JScrollPane jspBigData=new JScrollPane();
	
	JButton addblack=new JButton("Add");
	JTextField blackname=new JTextField(15);
	JTextField blackdes=new JTextField(15);
	
	JButton addcmd=new JButton("Add");
	JButton selectall=new JButton("all");
	JButton reverse=new JButton("reverse");
	JTextField cmd=new JTextField(15);
	JTextField par=new JTextField(15);
	
	ShowView(JFrame f) {
		super(new BorderLayout());
		this.frame=f;
		
		jspReg.setViewportView(tableReg);
		jspEvent.setViewportView(tableEvent);
		jspBlack.setViewportView(tableBlack);
		jspCmd.setViewportView(tableCmd);
		jspBigData.setViewportView(tableBigData);
		
		panelBlackButton.add(blackname);
		panelBlackButton.add(blackdes);
		panelBlackButton.add(addblack);
		
		panelCmdButton.add(cmd);
		panelCmdButton.add(par);
		panelCmdButton.add(addcmd);
		panelCmdButton.add(selectall);
		panelCmdButton.add(reverse);
		
		panelReg.add(jspReg);
		panelEvent.add(jspEvent);
		panelBlack.add(panelBlackButton);
		panelBlack.add(jspBlack);
		panelCmd.add(panelCmdButton);
		panelCmd.add(jspCmd);
		panelBigData.add(jspBigData);
		
		tab.add("Registed",panelReg);
		tab.add("Event",panelEvent);
		tab.add("Blacklist",panelBlack);
		tab.add("Command",panelCmd);
		tab.add("Bigdata",panelBigData);
		this.add(tab);
		
		tableEvent.setDefaultRenderer(Object.class, new EventTableRenderer());
		tableCmd.setModel(new CmdAbstractTableModel());
		TableColumn aColumn = tableCmd.getColumnModel().getColumn(0);  
		JCheckBox ckb=new JCheckBox();
		aColumn.setCellEditor(new DefaultCellEditor(ckb));
		
		selectall.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				CmdAbstractTableModel tmodel=(CmdAbstractTableModel) tableCmd.getModel();
				for(int i=0;i<tmodel.getRowCount();i++)
				{
					tmodel.setValueAt(true, i, 0);
				}
			}
		});
		reverse.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				CmdAbstractTableModel tmodel=(CmdAbstractTableModel) tableCmd.getModel();
				for(int i=0;i<tmodel.getRowCount();i++)
				{
					if((boolean)tmodel.getValueAt(i, 0))
						tmodel.setValueAt(false, i, 0);
					else
						tmodel.setValueAt(true, i, 0);
				}
			}
		});
	
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setSize(new Dimension(500, 320));
		
		tableReg.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = tableReg.getSelectedRow();
					String preId = tableReg.getValueAt(row,5).toString();
					String sql="select*from processlist where computerID='"+preId+"'";
					TablePane.showtabledlg(sql);
				}
			}
		});

		addblack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String sql="insert into processblacklist values('"+blackname.getText()+"','"+blackdes.getText()+"')";
				LazyMysqlOP mo=new LazyMysqlOP();
				try {
					mo.execute(sql);
				} catch (SQLException e1) {
					System.out.println(e1.getMessage());
				}
				mo.close();
				sql = "select * from processblacklist";
				TablePane.showtable(sql,tableBlack);
			}
		});
		
		addcmd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				LazyMysqlOP mo=new LazyMysqlOP();
				for(int i=0;i<tableCmd.getRowCount();i++)
				{
					if((boolean) tableCmd.getValueAt(i, 0))
					{
						String sql="insert into commandlist values('"+cmd.getText()+"','"+par.getText()+"','"+tableCmd.getValueAt(i, 1)+"')";
						try {
							mo.execute(sql);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
				mo.close();

				new Thread(){
					public void run()
					{
						CmdAbstractTableModel tablemodel=(CmdAbstractTableModel)tableCmd.getModel();
						boolean b=true;
						while(b)
						{
							b=tablemodel.updatetable();
							tableCmd.updateUI();
							try{
								Thread.sleep(1000);
							}
							catch(InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}
				}.start();
			}
		});
		keepUpdate();
		
	}
	public void keepUpdate()
	{
		new Thread(){
			public void run()
			{
				while(true)
				{
					updateInfo();
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
	}
	
	public void updateInfo() {
		
		String sql = null;
		sql="select * from computer";
		TablePane.showtable(sql,tableReg);
		sql = "select * from event";
		TablePane.showtable(sql,tableEvent);
		sql = "select * from processblacklist";
		TablePane.showtable(sql,tableBlack);
		sql = "select * from bigdata";
		TablePane.showtable(sql,tableBigData);

	}
	
	
}
