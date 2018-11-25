import java.io.*;
import java.net.*; 
public class GetGooglePage
{
	final static String url = "/search?hl=en&q=";
	final static String googleHost = "www.google.com";
	final static int BUFFER_SIZE=2048;
	public String fetchGooglePage( String keyword )
	{
		String content=null;
		try

		{
			InetAddress inetaddress = InetAddress.getByName( googleHost );
			Socket socket = new Socket(inetaddress, 80);
			BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			bufferedwriter.write("GET " + url + URLEncoder.encode(keyword, "UTF-8") + " HTTP/1.0\r\n");
			bufferedwriter.write("Accept-Language: en-us\r\n" );
			bufferedwriter.write("User-Agent: Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 4.0)\r\n" );
			bufferedwriter.write("Host: "+ googleHost + "\r\n" );
			bufferedwriter.write("Connection: Keep-Alive\r\n" ); 
			bufferedwriter.write("\r\n"); bufferedwriter.write("\r\n");
			bufferedwriter.flush();
			InputStream is=socket.getInputStream();
			byte [] buffer=new byte[BUFFER_SIZE];
			StringBuffer allBuffer=new StringBuffer();
			int count=1;
			while ( count>0 )
			{
				count = is.read( buffer, 0, BUFFER_SIZE ); 
				if ( count>0 )
				{
					allBuffer.append( new String( buffer, 0, count, "UTF-8" ) );
				}
				else break;
			}
			content = allBuffer.toString();
		}
		catch ( Exception e ) 
		{
			e.printStackTrace();
		}
		return content; 
	}
	// end fetchGooglepage()
	public static void main( String arg[] ) 
	{ 
		GetGooglePage getter = new GetGooglePage(); 
		String content = getter.fetchGooglePage( "sun" ); 
		System.out.println( "Content="+content );
	}
}