package business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;



public class RedhatSWMonitor  extends LinuxMonitor {

	class MySoftware
	{
		String name;
		String version;
		String action;
	}
	class MyProcessComparator implements Comparator<MySoftware>
	{
		@Override
		public int compare(MySoftware o1, MySoftware o2) {
			if(o1.name.compareTo(o2.name)==0)
				return o1.version.compareTo(o2.version);
			return o1.name.compareTo(o2.name);
		}
	}
	public static String cmd="rpm";
	public static String par=" -qa";
	LinkedList<MySoftware> oldSoftware=null;
	LinkedList<MySoftware> diffSoftware=null;
	public LinkedList<MyEvent> getEvent()
	{
		LinkedList<MyEvent> elist=new LinkedList<MyEvent>();
		//LinkedList<MySoftware> tmp=getDiff();
		for(int i=0;i<diffSoftware.size();i++)
		{
			MySoftware m=diffSoftware.get(i);
			MyEvent me=new MyEvent();
			me.eventID=m.name+" "+m.version;
			me.description=m.action;
			elist.add(me);
		}
		return elist;
	}
	public static void main(String[]s)
	{
		RedhatSWMonitor m=new RedhatSWMonitor();
		while(true){
			LinkedList<MySoftware> t=m.getDiff();
			for(int i=0;i<t.size();i++)
			{
				System.out.println(t.get(i).name+" "+t.get(i).version+" "+t.get(i).action);
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public LinkedList<MySoftware> getDiff()
	{
		if(oldSoftware==null)
		{
			oldSoftware=getProcess();
		}
		LinkedList<MySoftware> nowSoftware=getProcess();
		LinkedList<MySoftware> diffL=processDiff(oldSoftware,nowSoftware);
		oldSoftware=nowSoftware;
		return diffL;
	}
	public LinkedList<MySoftware> processDiff(LinkedList<MySoftware> l1,LinkedList<MySoftware> l2)
	{
		LinkedList<MySoftware> diffL=new LinkedList<MySoftware>();
		TreeSet<MySoftware> ts=new TreeSet<MySoftware>(new MyProcessComparator());
		for(int i=0;i<l1.size();i++)
		{
			ts.add(l1.get(i));
		}
		for(int i=0;i<l2.size();i++)
		{
			boolean re=ts.contains(l2.get(i));
			if(!re)//not in l1, new: delete,add to diff 
			{
				l2.get(i).action="install";
				diffL.add(l2.get(i));
			}
			//delete this item
			ts.remove(l2.get(i));
		}
		Iterator<MySoftware> it=ts.iterator();
		while(it.hasNext())
		{
			MySoftware m=it.next();
			m.action="uninstall";
			diffL.add(m);
		}
		return diffL;
	}
	public LinkedList<MySoftware> getProcess()
	{
		LinkedList<MySoftware> llsw=new LinkedList<MySoftware>();
		OutterCmd oc=new OutterCmd(cmd+" "+par);
		String str;
		while ((str = oc.readLine()) != null) {
			MySoftware m = analysis(str);
			llsw.add(m);
		}
        return llsw;
	}
	
	public MySoftware analysis(String in)
	{
		MySoftware m=new MySoftware();
		int i=0;
		for(String t : in.split(" "))
		{
			if(t.length()==0)
				continue; 
			if(i==0)
			{	m.name=t;
			
				m.version=t;}
			i++;
		}
		m.action="install";
		return m;
	}
	@Override
	protected void sendList() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void update() {
		diffSoftware=this.getDiff();
	}
}
