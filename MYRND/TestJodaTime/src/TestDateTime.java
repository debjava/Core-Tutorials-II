import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class TestDateTime 
{
	public static void main(String[] args) 
	{
		DateTime dt = new DateTime();
		System.out.println(dt);
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		System.out.println(dt.toString(fmt));
		
		String dts = "21/01/2009";
		DateTimeFormatter fmt1 = DateTimeFormat.forPattern("dd/MM/yyyy");
		
		DateTime dt2 = fmt1.parseDateTime(dts);
		System.out.println(dt2);
		
		
		
//		DateTime dt1 = fmt.parseDateTime("21/01/2009");
//		System.err.println("Parsed Data---->>>"+dt1);
		

	}
}
