package business;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.LazyMysqlOP;

public class MyEvent {
	public String computerID;
	public String eventID;
	public String time;
	public String description;
	public String exception;
	public MyEvent()
	{
		computerID=Localization.getID();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time=df.format(new Date());
		
	}
	public void eventSend()
	{
		exception=exceptionDetect()?"true":"false";
		LazyMysqlOP mo=new LazyMysqlOP();
		String sql="insert into event values('"+computerID+"','"+eventID+"','"+time+"','"+description+"','"+exception+"')";
		try {
			mo.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	private boolean exceptionDetect()
	{

		if (computerID == null || computerID.equals("not detected")
				|| eventID == null || eventID.length() == 0 || time == null
				| time.length() == 0 || description == null
				|| description.length() == 0) {
			return true;
		}
		if(description.contains("plugin in"))
			return true;
		
		return false;
	}
	public static void main(String [] args)
	{
	}
}
