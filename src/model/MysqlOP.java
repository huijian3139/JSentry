package model;

import java.sql.*;


public class MysqlOP {
	String driver = "com.mysql.jdbc.Driver";
	String url;
	Connection conn = null;
	Statement statement;

	public MysqlOP(String ip, String port, String db, String user, String password)
			throws SQLException {
		url = "jdbc:mysql://" + ip + ":" + port + "/" + db;
		//System.out.println(url);
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		conn = (Connection) DriverManager.getConnection(url, user, password);

		try {
			if (!conn.isClosed())
				//System.out.println("Succeeded connecting to the Database!");
				;
			else
				System.out.println("Failed connecting to the Database!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			statement = (Statement) conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		ResultSet rs = null;

		rs = statement.executeQuery(sql);

		return rs;
	}

	public boolean execute(String sql) throws SQLException {
		boolean rs = false;

		rs = statement.execute(sql);

		return rs;
	}

	public void close() {
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void finalize() {
		try {
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
