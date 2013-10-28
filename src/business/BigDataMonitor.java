package business;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeMap;

import model.LazyMysqlOP;


public class BigDataMonitor extends Thread{
	int threshold=10000000;//10MB
	//TreeMap<> tm
	public static void main(String[]args)
	{
		BigDataMonitor b=new BigDataMonitor();
		b.monitor();
	//	MyPackage m1=b.new MyPackage();
	//	MyPackage m2=b.new MyPackage();
		
//		TreeMap<MyPackage,MyPackage> tsmp=new TreeMap<MyPackage,MyPackage>(b.new MyPackageComparator());
	//	System.out.print(m1.equals(m2));
	}

	public void run()
	{
		monitor();
	}
	public void monitor()
	{
		String device=Localization.getNetPort();
		String ip=Localization.getIP2();
		String cmd="tcpdump -i "+device+" ip host "+ip+" -fnqSt";
		System.out.println(cmd);
		OutterCmd oc = new OutterCmd(cmd);
		String in;
		
		TreeMap<MyPackage,MyPackage> tsmp=new TreeMap<MyPackage,MyPackage>(new MyPackageComparator());
		//tsmp.
		while((in=oc.readLine())!=null)
		{
			System.out.println(in);
			if(in.startsWith("IP"))
			{
				MyPackage m=new MyPackage(in);
				if(tsmp.containsKey(m))
				{
					tsmp.get(m).size+=m.size;
				}
				else
				{
					tsmp.put(m, m);
				}
				if(tsmp.get(m).size>this.threshold)
				{
					sendAlarm(tsmp.get(m));
					tsmp.get(m).size=0;
				}
			}
		}
	}
	private void sendAlarm(MyPackage m) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=df.format(new Date());
		String sql="insert into bigdata values('"+time+"','"+m.source+"','"+m.destination+"','"+m.size+"','"+m.type+"')";
		LazyMysqlOP mo=new LazyMysqlOP();
		try {
			mo.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		mo.close();
	}
	class  MyPackage
	{
		public String type=null;
		public String source=null;
		public String destination=null;
		public int size=0;
		public MyPackage(String in)
		{
			int c=0;
			for(String i:in.split(" "))
			{
				if(i.length()==0)
					continue;
				c++;
				if(c==2)
					source=i;
				else if(c==4)
					destination=i;
				else if(c==5)
					type=i;
				else if(c==6&&type.startsWith("tcp"))
				{
					size=Integer.parseInt(i);
				}
				else if(c==7&&type.startsWith("UDP"))
				{
					size=Integer.parseInt(i);
				}
			}
		}

	}
	class MyPackageComparator implements Comparator<MyPackage>
	{

		@Override
		public int compare(MyPackage o1, MyPackage o2) {
			int re1=o1.type.compareTo(o2.type);
			int re2=o1.source.compareTo(o2.source);
			int re3=o1.destination.compareTo(o2.destination);

			if(re1==0)
				if(re2==0)
					return re3;
				else
					return re2;
			else 
				return re1;
		}
	}
}
