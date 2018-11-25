import java.util.Locale;


public class TestLanguage {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		Locale[] l = Locale.getAvailableLocales();
		for( Locale ll : l )
		{
			System.out.println(ll.getLanguage()+"  "+ll.getDisplayLanguage());
		}
		
	}

}
