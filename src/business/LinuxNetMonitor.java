package business;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;



// time id ip:port - ip:port
public class LinuxNetMonitor extends LinuxMonitor{
	class MyNet
	{
		String name;
		String description;
		String device;
		String action;
	}
	class MyProcessComparator implements Comparator<MyNet>
	{
		@Override
		public int compare(MyNet o1, MyNet o2) {
			if(o1.name.compareTo(o2.name)==0)
				return o1.device.compareTo(o2.device);
			return o1.name.compareTo(o2.name);
		}
	}

	public static String cmd="lsof";
	public static String par=" -Pnl +M -i4";
	LinkedList<MyNet> oldNet=null;
	public LinkedList<MyNet> diffNet=null;
	public LinkedList<MyEvent> getEvent()
	{
		LinkedList<MyEvent> elist=new LinkedList<MyEvent>();
		//LinkedList<MyNet> tmp=getDiff();
		for(int i=0;i<diffNet.size();i++)
		{
			MyNet m=diffNet.get(i);
			MyEvent me=new MyEvent();
			me.eventID=m.name+" "+m.device;
			me.description=m.description+" "+m.action;
			elist.add(me);
		}
		return elist;
	}
	public static void main(String[]s)
	{
		LinuxNetMonitor m=new LinuxNetMonitor();
		while(true){
			LinkedList<MyNet> t=m.getDiff();
			for(int i=0;i<t.size();i++)
			{
				System.out.println(t.get(i).name+" "+t.get(i).device+" "+t.get(i).description+" "+t.get(i).action);
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public LinkedList<MyNet> getDiff()
	{
		if(oldNet==null)
		{
			oldNet=getProcess();
		}
		LinkedList<MyNet> nowNet=getProcess();
		LinkedList<MyNet> diffL=processDiff(oldNet,nowNet);
		oldNet=nowNet;
		return diffL;
	}
	public LinkedList<MyNet> processDiff(LinkedList<MyNet> l1,LinkedList<MyNet> l2)
	{
		LinkedList<MyNet> diffL=new LinkedList<MyNet>();
		TreeSet<MyNet> ts=new TreeSet<MyNet>(new MyProcessComparator());
		for(int i=0;i<l1.size();i++)
		{
			ts.add(l1.get(i));
		}
		for(int i=0;i<l2.size();i++)
		{
			boolean re=ts.contains(l2.get(i));
			if(!re)//not in l1, new: delete,add to diff 
			{
				l2.get(i).action="connect";
				diffL.add(l2.get(i));
			}
			//delete this item
			ts.remove(l2.get(i));
		}
		Iterator<MyNet> it=ts.iterator();
		while(it.hasNext())
		{
			MyNet m=it.next();
			m.action="disconnect";
			diffL.add(m);
		}
		return diffL;
	}
	public LinkedList<MyNet> getProcess()
	{
		LinkedList<MyNet> llsw=new LinkedList<MyNet>();
		String str;
		OutterCmd oc=new OutterCmd(cmd+" "+par);
		while ((str = oc.readLine()) != null) {  
			MyNet m=analysis(str);
			llsw.add(m);
		}
        return llsw;
	}
	public MyNet analysis(String in)
	{
		MyNet m=new MyNet();
		int i=0;
		for(String t : in.split(" "))
		{
			if(t.length()==0)
				continue; 
			if(i==0)
				m.name=t;
			else if(i==5)
				m.device=t;
			else if(i==8)
				m.description=t;
			i++;
		}
		m.action="connect";
		return m;
	}
	@Override
	protected void sendList() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void update() {
		diffNet=this.getDiff();
	}
}
