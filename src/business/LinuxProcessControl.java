package business;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import model.LazyMysqlOP;

import business.LinuxProcessMonitor.MyProcess;

public class LinuxProcessControl extends LinuxControl{
	class Black
	{
		String name;
		String description;
	}
	public static String cmd="kill";
	public void monitor()
	{
		LinuxProcessMonitor lpm=new LinuxProcessMonitor();
		
		while(true)
		{
			LinkedList<Black> llb= getBlacklist();
			LinkedList<MyProcess> lm=lpm.getProcess();
			controlProcess(llb,lm);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//lm.get(index)
	}
	//public static 
	public void controlProcess(LinkedList<Black> llb,LinkedList<MyProcess> lm)
	{
		for(int i=0;i<lm.size();i++)
		{
			for(int j=0;j<llb.size();j++)
			{
				if(lm.get(i).name.contains(llb.get(j).name))
				{
					Runtime rt = Runtime.getRuntime();
					try {
						rt.exec(cmd+" "+lm.get(i).pid);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}
	public LinkedList<Black> getBlacklist()
	{
		LinkedList<Black> llb=new LinkedList<Black>();
		LazyMysqlOP mo=new LazyMysqlOP();
		String sql="select name from processblacklist";
		ResultSet rs;
		try {
			rs = mo.executeQuery(sql);
			while(rs.next())
			{
				Black b=new Black();
				b.name=rs.getString(1);
//				System.out.println(b.name+" ");
				llb.add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return llb;

	}
}
