package business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OutterCmd {
	private BufferedReader in;
	public OutterCmd(String cmd)
	{
		Runtime rt = Runtime.getRuntime();
		Process p;
		try {
			p = rt.exec(cmd);
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String readLine()
	{
		try {
			return in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
