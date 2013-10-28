package business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


public class Localization {
	 public static String getOSName()
	 {
		String name = System.getProperty("os.name");
		if (name.equals("Linux"))
		{
			Runtime rt = Runtime.getRuntime();
			String str=null;
	        try {
				Process p = rt.exec("cat /etc/issue");
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				str= in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return str;
		}
		else
		{
			return name;
		}
	 }
	 public static String getOSArch()
	 {
		 return System.getProperty("os.arch");
	 }
	 public static String getOSVersion()
	 {
		 return System.getProperty("os.version");
	 }
	 public static String getUserName()
	 {
		 return System.getProperty("user.name");
	 }
	 protected static String getIP()
	 {
		String ip=null;
		try {
			InetAddress localAdd=null;
			localAdd = InetAddress.getLocalHost();
			ip=localAdd.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	 }
	 public static String getIP2()
	 {
		String ip=null;
		NetworkInterface face=null;
		try 
		{
			String netPort=Localization.getNetPort();
			face = NetworkInterface.getByName(netPort);
			if(face!=null)
			{
				Enumeration<?> e2 = face.getInetAddresses();
				while (e2.hasMoreElements()) 
				{
					InetAddress ia = (InetAddress) e2.nextElement();
					if (ia instanceof Inet6Address)
						continue;
					ip = ia.getHostAddress();
					break;
				}
			}
			else{
				ip = "not detected";
			}
			
		} catch (SocketException e) {
			System.out.println(e.getMessage());
		}
		return ip;
	 }
	 protected static String getMAC()
	 {
		 String mac=null;
		 try {
			InetAddress localAdd=null;
			localAdd = InetAddress.getLocalHost();
			NetworkInterface face=NetworkInterface.getByInetAddress(localAdd);
			byte[]bb=face.getHardwareAddress();
			System.out.println("bb.length: "+bb.length);
			mac=byte2str(bb);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return mac;
	 }
	 public static String getMAC2()
	 {
		String mac=null;
		NetworkInterface face=null;
		try {
			String netPort=Localization.getNetPort();
			face = NetworkInterface.getByName(netPort);
			if(face!=null)
			{
				byte[]bb=face.getHardwareAddress();
				mac=byte2str(bb);
			}
			else{
				mac="not detected";
			}
				
			} catch (SocketException e) {
				System.out.println(e.getMessage());
			}
		
		return mac;
	 }
	 public static String getID()
	 {
		 return getMAC2();
	 }
	 private static String byte2str(byte[] bb)
		{
			String s;
			StringBuffer sb = new StringBuffer();;
			for(int i=0;i<bb.length;i++)
			{
				s=Integer.toHexString(bb[i]&0xff);
				if(s.length()==0)
					sb.append("00");
				else if(s.length()==1)
					sb.append("0");
				sb.append(s);
			}
			return sb.toString().toUpperCase();
		}
	 public static String getNetPort()
	 {
		 String osName=getOSName();
		 if(osName.startsWith("Fedora"))
		 {
			 return "em1";
		 }
		 else if(osName.startsWith("Ubuntu"))
		 {
			 return "eth0";
		 }
		 return "eth0";
	 }
	 public static LinuxMonitor getSWMonitor()
	 {
		 String osName=getOSName();
		 if(osName.startsWith("Fedora"))
		 {
			 return new RedhatSWMonitor();
		 }
		 else if(osName.startsWith("Ubuntu"))
		 {
			 return new DebianSWMonitor();
		 }
		 return null;
	 }
}
