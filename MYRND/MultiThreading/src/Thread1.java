
public class Thread1 implements Runnable
{
	Thread t1 = null;
	public Thread1()
	{
		t1 = new Thread(this,"Thread One");
		t1.start();
	}
	public void run() 
	{
		for( int i = 0 ; i < 5 ; i++ )
		{
			System.out.println(t1.getName()+" running"+" sequence value-->"+i);
		}
	}
}
