import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;


public class TestFile 
{
	private static Date getDate( String dt )
	{
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		try
		{
			date = format.parse(dt);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return date;
	}
	public static void show()
	{
		Date dt = getDate("14-12-2008");
		String path = "C:/backup";
		File files = new File( path );
		File[] dirs = files.listFiles();
		for( int i = 0 ; i < dirs.length ; i++ )
		{
			System.out.println("File Path--------"+dirs[i].getAbsolutePath());
			System.out.println(FileUtils.isFileOlder(dirs[i], dt));
		}
	}
	public static void main(String[] args) throws Exception
	{
		show();
		
//		long time = System.currentTimeMillis();
//		String dt = "2008-12-22";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date dt1 = sdf.parse(dt);
//		File file = new File("temp/checkwar/WEB-INF");
//		System.out.println(FileUtils.isFileOlder(file, dt1));
	}	
}