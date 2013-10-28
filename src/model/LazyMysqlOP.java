package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;


public class LazyMysqlOP {
	MysqlOP mo;
	public LazyMysqlOP()
	{
		FileReader reader=null;
		try {
			String workingDir = System.getProperty("user.dir");
			reader = new FileReader(workingDir + File.separator+"server.txt");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "configure file not found");
			System.exit(0);
		}
		BufferedReader br = new BufferedReader(reader);
		String s1 = null;
		try {
			s1 = br.readLine();
			//System.out.println(s1);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "configure file error");
			System.exit(0);
		}
		try {
			br.close();
			reader.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		try {
			mo =new MysqlOP(s1, "3306", "reportdb", "reportu", "123");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, "Cann't connect to server!");
			return;
		}
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		return mo.executeQuery(sql);
	}

	public boolean execute(String sql) throws SQLException {
		return mo.execute(sql);
	}

	public void close() {
		mo.close();
	}

	protected void finalize() {
		mo.close();
	}
}
