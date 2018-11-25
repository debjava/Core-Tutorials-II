import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader
{
	public static void downloadFile( String urlPath ) throws Exception
	{
		String Address = "http://www.bof.fi/Stats/default.aspx?r=%2ftilastot%2fvaluuttakurssit%2fvaluuttakurssit_today_en";
		URL MyURL = new URL(Address);
		URLConnection TheConnection = MyURL.openConnection();
		InputStream IS = TheConnection.getInputStream();
		BufferedInputStream BIS = new BufferedInputStream(IS);
		String FileName = "daily.html";
		FileOutputStream FOS = new FileOutputStream(FileName);
		BufferedOutputStream BOS = new BufferedOutputStream(FOS);
		int I = 0;
		while ((I = BIS.read()) != -1) {
			BOS.write(I);
		}
		BOS.flush();
		BOS.close();
	}
	public static void main(String[] args) 
	{
		try
		{
			downloadFile(null);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
