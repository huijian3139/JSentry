package business;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;



//time id port
public class LinuxPortMonitor  extends LinuxMonitor{
	class MyPort
	{
		String bus;
		String id;
		String description;
		String action;
	}
	class MyProcessComparator implements Comparator<MyPort>
	{
		@Override
		public int compare(MyPort o1, MyPort o2) {
			if(o1.id.compareTo(o2.id)==0)
				return o1.bus.compareTo(o2.bus);
			return o1.id.compareTo(o2.id);
		}
	}
	public static String cmd="lsusb";
	LinkedList<MyPort> oldPort=null;
	LinkedList<MyPort> diffPort=null;
	public LinkedList<MyEvent> getEvent()
	{
		LinkedList<MyEvent> elist=new LinkedList<MyEvent>();
		//LinkedList<MyPort> tmp=getDiff();
		for(int i=0;i<diffPort.size();i++)
		{
			MyPort m=diffPort.get(i);
			MyEvent me=new MyEvent();
			me.eventID=m.id+" "+m.bus;
			me.description=m.description+" "+m.action;
			elist.add(me);
		}
		return elist;
	}
	public static void main(String[]s)
	{
		LinuxPortMonitor m=new LinuxPortMonitor();
		while(true){
			LinkedList<MyPort> t=m.getDiff();
			for(int i=0;i<t.size();i++)
			{
				System.out.println(t.get(i).bus+" "+t.get(i).id+" "+t.get(i).description+" "+t.get(i).action);
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public LinkedList<MyPort> getDiff()
	{
		if(oldPort==null)
		{
			oldPort=getProcess();
		}
		LinkedList<MyPort> nowPort=getProcess();
		LinkedList<MyPort> diffL=processDiff(oldPort,nowPort);
		oldPort=nowPort;
		return diffL;
	}
	public LinkedList<MyPort> processDiff(LinkedList<MyPort> l1,LinkedList<MyPort> l2)
	{
		LinkedList<MyPort> diffL=new LinkedList<MyPort>();
		TreeSet<MyPort> ts=new TreeSet<MyPort>(new MyProcessComparator());
		for(int i=0;i<l1.size();i++)
		{
			ts.add(l1.get(i));
		}
		for(int i=0;i<l2.size();i++)
		{
			boolean re=ts.contains(l2.get(i));
			if(!re)//not in l1, new: delete,add to diff 
			{
				l2.get(i).action="plug in";
				diffL.add(l2.get(i));
			}
			//delete this item
			ts.remove(l2.get(i));
		}
		Iterator<MyPort> it=ts.iterator();
		while(it.hasNext())
		{
			MyPort m=it.next();
			m.action="plug out";
			diffL.add(m);
		}
		return diffL;
	}
	public LinkedList<MyPort> getProcess()
	{
		LinkedList<MyPort> llsw=new LinkedList<MyPort>();
		OutterCmd oc=new OutterCmd(cmd);
			String str;
			while ((str = oc.readLine()) != null) {  
				MyPort m=analysis(str);
				llsw.add(m);
			}
			
        return llsw;
	}
	public MyPort analysis(String in)
	{
		MyPort m=new MyPort();
		int i=0;
		for(String t : in.split(" "))
		{
			if(t.length()==0)
				continue; 
			if(i==1)
				m.bus=t;
			else if(i==5)
				m.id=t;
			else if(i==6)
				m.description=t;
			else if(i>6)
				m.description+=" "+t;
			i++;
		}
		m.action="plug in";
		return m;
	}
	@Override
	protected void sendList() {
		
	}
	@Override
	protected void update() {
		diffPort=this.getDiff();
	}
}
