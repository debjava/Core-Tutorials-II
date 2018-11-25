
public class Worker 
{
	private String name = null;
	
	public String getName() {
		return name;
	}

	public Worker( String name )
	{
		super();
		this.name = name;
	}
	
	public synchronized void doWork( Worker worker )
	{
		System.out.println("doWork Worker Name------>"+worker.getName());
		worker.doInternalWork(worker);
	}
	
	public synchronized void doInternalWork( Worker worker )
	{
		System.out.println(" doInternalWork Worker Name------>"+this.name);
	}
}
