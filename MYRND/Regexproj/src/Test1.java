import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test1 
{
	public static void show()
	{
		 String[] x = 
		      Pattern.compile("ian").split(
		        "the darwinian devonian explodian chicken");
		    for (int i=0; i<x.length; i++) {
		      System.out.println(i + " \"" + x[i] + "\"");
		    }

	}
	
	public static void show1()
	{
		String pattern = "^Q[^u]\\d+\\.";
	    String input = "QA777. is the next flight. It is on time.";

	    Pattern p = Pattern.compile(pattern);

	    boolean found = p.matcher(input).lookingAt();
	    System.out.println(found);

//	    System.out.println("'" + pattern + "'"
//	        + (found ? " matches '" : " doesn't match '") + input + "'");

	}
	
	public static void show2()
	{
		Pattern myRE = Pattern.compile("d.*ian");
	    Matcher matcher = myRE
	        .matcher("darwinian pterodactyls soared over the devonian space");
	    matcher.lookingAt();
	    String result = matcher.group(0);
	    System.out.println(result);

	}
	
	public static void show3()
	{
		Pattern patt = Pattern.compile("(\\w+)\\s(\\d+)");
	    Matcher matcher = patt.matcher("Bananas 123");
	    matcher.lookingAt();
	    System.out.println("Name: " + matcher.group(1));
	    System.out.println("Number: " + matcher.group(2));
	  
	}
	public static void main(String[] args) 
	{
//		show();
//		show1();
//		show2();
		show3();
//		String ss = "the darwinian devonian explodian chicken";
//		Pattern pat = Pattern.compile("ian");
//		String[] st = ss.split("ian");
//		for( int i = 0 ; i < st.length ; i++ )
//			System.out.println(st[i]);
//		System.out.println("**************************************");
//		String[] s1 = pat.split(ss);
//		for( int j = 0 ; j < s1.length ; j++ )
//			System.out.println(s1[j]);
		
		
	}
}
