package business;

import java.io.IOException;

public class LinuxNetControl extends LinuxControl {

	@Override
	protected void monitor() {
		Runtime rt = Runtime.getRuntime();
		while(true)
		{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String ip=Localization.getIP2();
			String netPort=Localization.getNetPort();
			String cmd="ifconfig "+netPort+" "+ip;
			try {
				rt.exec(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
