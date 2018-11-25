public class TwoThreads extends Thread 
{
	public TwoThreads()
	{
		super();
	}
	public void run()
	{
		for ( int i = 0; i < 5; i++ ) 
		{
			System.out.println("New thread----> "+i);
		}
	}

	public static void main(String[] args) {
		TwoThreads tt = new TwoThreads();
		tt.start();
				

		System.out.println("New Thread will be invoked");
		for ( int i = 0; i < 5; i++ ) 
		{
			System.out.println("Main thread---> "+i);
		}
	}
}
