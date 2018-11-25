
public class Test 
{
	public static void main(String[] args)
    {
        try
        {
            Thread t1 = new Thread(new Test1());
            t1.start();
            t1.join();
            Thread t2 = new Thread(new Test2());
            t2.start();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



}
