
public class TestDeadLock 
{
	public static void main(String[] args) 
	{
		Worker worker1 = new Worker( "W1" );
		Worker worker2 = new Worker( "W2" );
		
		Thread t1 = new Thread( new DeadThread1(worker1,worker2) );
		Thread t2 = new Thread( new DeadThread1(worker2,worker1) );
		
		t1.start();
		t2.start();
	}
}
