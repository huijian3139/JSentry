package view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

import model.LazyMysqlOP;

class CmdAbstractTableModel extends AbstractTableModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2127152185893694916L;
	String[] head={"c","computerID","state"};
	Class[] typeArray={Boolean.class,Object.class,Object.class};
	LinkedList<Object[]> data=new LinkedList<Object[]>();
	public CmdAbstractTableModel()
	{
		updatetable();
	}
	public boolean updatetable()  
	{
		boolean b=false;
		LazyMysqlOP mo = new LazyMysqlOP();
		String sql="select a.mac,b.cmd from computer as a left join commandlist as b on a.mac=b.computerID";
		ResultSet rs;
		try {
			data.clear();
			rs = mo.executeQuery(sql);
			while (rs.next()) {
				Object [] o={new Boolean(false),rs.getString(1),rs.getString(2)};
				data.add(o);
			}
			sql="select*from commandlist";
			rs=mo.executeQuery(sql);
			b=rs.next();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		

		mo.close();
		if(b)
			return true;
		else 
			return false;
	}

	 public int getColumnCount(){
	  return head.length;
	 }

	public int getRowCount(){
	  return data.size();
	 }

	 @Override
	 public String getColumnName(int column){
	  return head[column];
	 }

	 public Object getValueAt(int rowIndex,int columnIndex){
	  return data.get(rowIndex)[columnIndex];
	 }

	  @Override
	 public boolean isCellEditable(int rowIndex,int columnIndex){
	  return true;
	 }

	  @Override
	 public void setValueAt(Object aValue,int rowIndex,int columnIndex){
		  	data.get(rowIndex)[columnIndex]=aValue;
		  		fireTableCellUpdated(rowIndex,columnIndex);
	 }

	 public Class getColumnClass(int columnIndex){
	  return typeArray[columnIndex];
	 }
}