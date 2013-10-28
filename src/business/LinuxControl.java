package business;

public abstract class LinuxControl extends Thread {
	public void run()
	{
		monitor();
	}
	protected abstract void monitor();
}
