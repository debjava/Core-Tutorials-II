
public class Thread2 implements Runnable
{
	Thread t2 = null;
	public Thread2()
	{
		t2 = new Thread(this,"Thread Two");
		t2.start();
	}
	public void run() 
	{
		for( int i = 0 ; i < 5 ; i++ )
		{
			System.out.println(t2.getName()+" running"+" sequence value-->"+i);
		}
	}
}
