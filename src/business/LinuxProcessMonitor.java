package business;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;


import model.LazyMysqlOP;


//time id process start/kill
public class LinuxProcessMonitor extends LinuxMonitor {
	public static String cmd="ps";
	public static String par="aux";
	LinkedList<MyProcess> oldProcess=null;
	LinkedList<MyProcess> diffProcess=null;
	class MyProcess
	{
		String pid;
		String user;
		String name;
		String action;
	}
	class MyProcessComparator implements Comparator<MyProcess>
	{
		@Override
		public int compare(MyProcess o1, MyProcess o2) {
			if(o1.pid.compareTo(o2.pid)==0)
				return o1.name.compareTo(o2.name);
			return o1.pid.compareTo(o2.pid);
		}
	}
	public LinuxProcessMonitor()
	{
		oldProcess=this.getProcess();
		String sql="delete from processlist where computerID='"+Localization.getID()+"'";
		LazyMysqlOP mo=new LazyMysqlOP();
		try {
			mo.execute(sql);
			for(int i=0;i<oldProcess.size();i++)
			{
				MyProcess t=oldProcess.get(i);
				sql="insert into processlist values('"+Localization.getID()+"','"+t.pid+"','"+t.name+"')";
				mo.execute(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mo.close();
	}

	public void update()
	{
		diffProcess=getDiff();
	}
	public LinkedList<MyEvent> getEvent()
	{
		LinkedList<MyEvent> elist=new LinkedList<MyEvent>();
		for(int i=0;i<diffProcess.size();i++)
		{
			MyProcess m=diffProcess.get(i);
			MyEvent me=new MyEvent();
			me.eventID=m.pid;
			me.description=m.name+" "+m.user+" "+m.action;
			elist.add(me);
		}
		return elist;
	}
	public static void main(String[]s)
	{
		LinuxProcessMonitor m=new LinuxProcessMonitor();
		while(true){
			LinkedList<MyProcess> t=m.getDiff();
			for(int i=0;i<t.size();i++)
			{
				System.out.println(t.get(i).pid+" "+t.get(i).name+" "+t.get(i).action);
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public LinkedList<MyProcess> getDiff()
	{
		if(oldProcess==null)
		{
			oldProcess=getProcess();
		}
		LinkedList<MyProcess> nowProcess=getProcess();
		LinkedList<MyProcess> diffL=processDiff(oldProcess,nowProcess);
		oldProcess=nowProcess;
		return diffL;
	}
	public LinkedList<MyProcess> processDiff(LinkedList<MyProcess> l1,LinkedList<MyProcess> l2)
	{
		LinkedList<MyProcess> diffL=new LinkedList<MyProcess>();
		TreeSet<MyProcess> ts=new TreeSet<MyProcess>(new MyProcessComparator());
		for(int i=0;i<l1.size();i++)
		{
			ts.add(l1.get(i));
		}
		for(int i=0;i<l2.size();i++)
		{
			boolean re=ts.contains(l2.get(i));
			if(!re)//not in l1, new: delete,add to diff 
			{
				l2.get(i).action="start";
				diffL.add(l2.get(i));
			}
			//delete this item
			ts.remove(l2.get(i));
		}
		Iterator<MyProcess> it=ts.iterator();
		while(it.hasNext())
		{
			MyProcess m=it.next();
			m.action="quit";
			diffL.add(m);
		}
		return diffL;
	}
	public LinkedList<MyProcess> getProcess()
	{
		LinkedList<MyProcess> llmp=new LinkedList<MyProcess>();
		OutterCmd oc=new OutterCmd(cmd+" "+par);
			
		String str;
		while ((str = oc.readLine()) != null) {
			MyProcess m = analysis(str);
			if (m.user.equals("root")
					|| m.name.equals("[" + LinuxNetMonitor.cmd + "]")
					|| m.name.equals(LinuxNetMonitor.cmd)
					|| m.name.equals(LinuxPortMonitor.cmd)
					|| m.name.equals(LinuxProcessMonitor.cmd)
					|| m.name.equals(DebianSWMonitor.cmd)
					|| m.name.equals(LinuxProcessControl.cmd)
					|| m.name.equals(RedhatSWMonitor.cmd))
				continue;
			// System.out.println(m.pid+" "+m.user+" "+m.name);
			llmp.add(m);
		}

        return llmp;
	}
	public MyProcess analysis(String in)
	{
		MyProcess m=new MyProcess();
		int i=0;
		for(String t : in.split(" "))
		{
			if(t.length()==0)
				continue; 
			if(i==1)
				m.pid=t;
			else if(i==0)
				m.user=t;
			else if(i==10)
				m.name=t;
			i++;
		}
		m.action="start";
		return m;
	}
	@Override
	protected void sendList() {
		String sql=null;
		LazyMysqlOP mo=new LazyMysqlOP();
		for(int i=0;i<diffProcess.size();i++)
		{
			MyProcess m=diffProcess.get(i);
			if(m.action.equals("start"))
			{
				sql="insert into processlist values('"+Localization.getID()+"','"+m.pid+"','"+m.name+"')";
			}
			else if(m.action.equals("quit"))
			{
				sql="delete from processlist where computerID='"+Localization.getID()+"' and pid='"+m.pid+"'";
			}
			try {
				mo.execute(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		mo.close();
	}
}
