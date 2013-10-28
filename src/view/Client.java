package view;

import business.BigDataMonitor;
import business.LinuxNetControl;
import business.Localization;
import business.LinuxCmdExecutor;
import business.LinuxNetMonitor;
import business.LinuxPortMonitor;
import business.LinuxProcessControl;
import business.LinuxProcessMonitor;

public class Client {
	public static void sentry()
	{
		Localization.getSWMonitor().start();
		new LinuxNetMonitor().start();
		new LinuxPortMonitor().start();
		new LinuxProcessMonitor().start();
		new LinuxProcessControl().start();
		new LinuxCmdExecutor().start();
		new BigDataMonitor().start();
		new LinuxNetControl().start();
	}
}

