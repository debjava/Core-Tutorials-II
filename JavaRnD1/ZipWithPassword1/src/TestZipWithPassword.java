import java.io.File;
import java.io.IOException;

import de.idyl.crypto.zip.AesZipFileEncrypter;


public class TestZipWithPassword
{
	public static void main(String[] args) 
	{
		//First create a Zip file.
		//Then encrypt the zip file.
		try 
		{
			AesZipFileEncrypter enc = new AesZipFileEncrypter("testdata/encrypted.zip");
//			enc.addEncrypted( new File("testdata/SWT.zip"), "pikupiku");
			enc.addEncrypted( new File("testdata/tk.zip"), "pikupiku");
			
			File file = new File("testdata/tk.zip");
			file.delete();
			
			file = new File("testdata/encrypted.zip");
			file.renameTo( new File("testdata/tk.zip"));
			
			
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

}
