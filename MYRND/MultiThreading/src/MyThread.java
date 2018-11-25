
public class MyThread implements Runnable 
{
	Thread t = null;
	public MyThread()
	{
		t = new Thread(this,"MyThread");
		t.start();
	}
	
	public void run() 
	{
		for( int i = 0 ; i < 5 ; i++ )
			System.out.println("Child Thread Running");
	}
}
