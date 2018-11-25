import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class ReportError 
{
	public ReportError() 
	{
		Thread.setDefaultUncaughtExceptionHandler( new UncaughtExceptionHandler() 
		{
			public void uncaughtException(Thread t, Throwable e)
			{
				JOptionPane.showConfirmDialog(null, new JTextArea (getMessage(e))); 
				}
			});
		}
	private String getMessage(Throwable e) 
	{
		String msg = "Mail it to developer, Or Something you want. \n\n";
		msg += e.toString() + "\n";
		StackTraceElement[] elems = e.getStackTrace();
		for (int i = 0; i < elems.length; i++) 
		{
			msg += elems[i].toString() + " \n";
			}
		return msg; 
		}
	public static void main(String[] args)
	{
		new ReportError(); generateException(); 
		}
	public static void generateException() 
	{
		String s = null; 
		//Generating exception 
		System.out.println(" " + s.toLowerCase()); 
	}
}