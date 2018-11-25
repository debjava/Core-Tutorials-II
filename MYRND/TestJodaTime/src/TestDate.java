import java.text.SimpleDateFormat;
import java.util.Date;


public class TestDate 
{
	public static void main(String[] args) 
	{
		String dt1 = "090119120227000";
		String dt2 = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		try
		{
			Date dt = sdf.parse(dt1);
			System.out.println(dt);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

	}

}
