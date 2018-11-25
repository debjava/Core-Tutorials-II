import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipExtracter 
{
	private static void writeFile( File file , ZipInputStream zipIn)
	{
		try 
		{
			OutputStream outStream = new FileOutputStream(file);
			byte[] buff = new byte[1024];
			int len;
			while ((len = zipIn.read(buff)) > 0) 
			{
				outStream.write(buff, 0, len);
			}
			outStream.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public static void extract(File file, File destination) throws IOException
	{
		ZipInputStream zipIn = null;
		try 
		{
			zipIn = new ZipInputStream(new FileInputStream(file));
			ZipEntry entry = null;

			while ((entry = zipIn.getNextEntry()) != null) 
			{
				String outFilename = entry.getName();

				if( !new File(destination, outFilename).getParentFile().exists() )
					new File(destination, outFilename).getParentFile().mkdirs();
				
				if( !entry.isDirectory() )
					writeFile(new File(destination,outFilename), zipIn);
			}
			System.out.println("Zip file extracted successfully...");
		} 
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally 
		{
			if (zipIn != null) 
			{
				zipIn.close();
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		extract(new File("temp/test.zip"), new File("temp"));
	}
}
