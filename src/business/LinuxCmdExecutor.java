package business;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.LazyMysqlOP;

public class LinuxCmdExecutor extends Thread{
	public void run()
	{
		while(true){
			execute();
			try {
				sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	public void execute()
	{
		LazyMysqlOP mo=new LazyMysqlOP();
		String sql="select*from commandlist where computerID='"+Localization.getID()+"'";
		try {
			ResultSet rs=mo.executeQuery(sql);
			while(rs.next())
			{
				String command=rs.getString("cmd")+" "+rs.getString("par");
				Runtime.getRuntime().exec(command);
			}
			mo.execute("delete from commandlist where computerID='"+Localization.getID()+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mo.close();
	}
}
