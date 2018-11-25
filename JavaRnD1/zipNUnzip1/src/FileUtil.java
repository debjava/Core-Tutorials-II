import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class FileUtil 
{
    /**
     * Zip up a directory
     * @param directory
     * @param zipName
     * @throws IOException
     */
    public static void zipDir(String directory, String zipName) throws IOException 
    {
    	System.out.println(directory);
    	if( zipName == null )
    	{
    		String tempPath = new File(directory).getAbsoluteFile().getParent()+File.separator+new File(directory).getName()+".zip";
    		System.out.println( tempPath );
    		
    	}
    	
    	
//        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipName));
//        zipDir(directory, zos, new File(directory).getName()+File.separator);
//        zos.close();
    }

    /**
     * Zip up a directory path
     * @param directory
     * @param zos
     * @param path
     * @throws IOException
     */
    public static void zipDir(String directory, ZipOutputStream zos, String path) throws IOException {
        File zipDir = new File(directory);
        // get a listing of the directory content
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        // loop through dirList, and zip the files
        for (int i = 0; i < dirList.length; i++) 
        {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) 
            {
                String filePath = f.getPath();
                System.out.println(path + f.getName());
                zipDir(filePath, zos, path + f.getName() + "/");
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            try 
            {
                ZipEntry anEntry = new ZipEntry(path + f.getName());
                zos.putNextEntry(anEntry);
                bytesIn = fis.read(readBuffer);
                while (bytesIn != -1)
                {
                    zos.write(readBuffer, 0, bytesIn);
                    bytesIn = fis.read(readBuffer);
                }
            }
            catch( Exception e )
            {
            	e.printStackTrace();
            }
            finally 
            {
                fis.close();
            }
        }
    }
    
    public static void main(String[] args) throws Exception
    {
//    	zipDir("testdata/SWT", "temp/test.zip");
    	zipDir("testdata/SWT", null);
	}
    
}

