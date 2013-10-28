package view;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


import model.LazyMysqlOP;

public class TablePane {
	public static void showtabledlg(String sql)
	{
		JFrame prodlg=new JFrame();
		JTable table=new JTable();
		JScrollPane jsp=new JScrollPane();
		TablePane.showtable(sql,table);
		jsp.setViewportView(table);
		prodlg.add(jsp);
		prodlg.pack();
		prodlg.setVisible(true);
	}
	public static void showtable(String sql,JTable table)
	{
		LazyMysqlOP mo = new LazyMysqlOP();
		try {
			DefaultTableModel dt = new DefaultTableModel(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 3830730382645663651L;

				public boolean isCellEditable(int row, int column) {
					return false;
				};
			};
			ResultSet rs = mo.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();
			int b = 1;
			while (b <= numberOfColumns) {
				dt.addColumn(rsmd.getColumnName(b));
				b++;
			}
			Vector<String> newRow = null;
			while (rs.next()) {
				newRow = new Vector<String>();
				int c = 1;
				while (c <= numberOfColumns) {
					newRow.addElement(rs.getString(c));
					c++;
				}
				dt.addRow(newRow);
			}
			table.setModel(dt);
			table.setGridColor(new Color(240,240,240));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		mo.close();
	}
}
