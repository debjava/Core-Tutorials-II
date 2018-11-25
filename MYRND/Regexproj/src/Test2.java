import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test2 
{
	public static void test1()
	{
		String pattern = "^\\d";
		
//		pattern = "^[0-9]";
		String name = "12222";
		
		Pattern pat = Pattern.compile(pattern);
		Matcher mat = pat.matcher(name);
		
		System.out.println(mat.matches());
		System.out.println(mat.find());
		
		
	}
	
	public static void main(String[] args) 
	{
		test1();
	}
}
