package business;

import java.util.LinkedList;

public abstract class LinuxMonitor extends Thread{
	public void run() 
	{
		while(true)
		{
			update();
			sendEvent();
			sendList();
			try {
				sleep(10000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	protected abstract void update();
	private void sendEvent()
	{
		LinkedList<MyEvent> lm = getEvent();
		for(MyEvent e:lm)
		{
			e.eventSend();
		}
	}
	protected abstract void sendList();
	protected abstract LinkedList<MyEvent> getEvent();
}
