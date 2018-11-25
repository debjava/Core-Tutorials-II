
public class DeadThread1 implements Runnable 
{
	private Worker worker1 = null;
	private Worker worker2 = null;
	
	public DeadThread1( Worker worker1 , Worker worker2 )
	{
		super();
		this.worker1 = worker1;
		this.worker2 = worker2;
	}
	public void run() 
	{
		this.worker1.doWork(worker2);
	}
}
