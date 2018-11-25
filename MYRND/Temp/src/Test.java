import java.io.*; 
import java.net.*;
public class Test 
{
	public static void main(String[] args) throws Exception
	{
		//http://search.yahoo.com/search?p=java 
		String webSite = "http://search.yahoo.com/"; 
		String searchText = URLEncoder.encode("java", "UTF-8");
		String key = "p"; 
		String text = "search?";
		URL yahoo = new URL(webSite + text + key + "=" + searchText);
		URLConnection yc = yahoo.openConnection();
		BufferedReader in = new BufferedReader( new InputStreamReader( yc.getInputStream()));
		String inputLine; 
		while ((inputLine = in.readLine()) != null)
		{
			System.out.println(inputLine); } in.close();
	} 
} 

