package zip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

public class ZipExtractor {
	private static Map map = new HashMap();
	private static int count =0;
	//byte[] fileData = null;
	private String password = null;
	private String filePath =null;
	private String fileOutputPath = null;
	
	
	public ZipExtractor() 
	{
		super();
	}
	
	public ZipExtractor(String password, String filePath, String fileOutputPath) {
		super();
		this.password = password;
		this.filePath = filePath;
		this.fileOutputPath = fileOutputPath;
	}
	
	public static void main(String[] args){
//		String ch = null;
		String filePath = "temp/test.zip";
		String outputPath = "temp";
		String password = "pikupiku";
		
		ZipExtractor extract = new ZipExtractor();
		extract.extractZipFile(filePath, password, outputPath);
		
		
			
	}
	
	public void extractZipFile()
	{
		extractZipFile(this.filePath, this.password, this.fileOutputPath);
	}
	
	public void extractZipFile(String filePath, String password, final String outputPath){
		boolean flag = false;
	       
        RandomAccessFile randomAccessFile = null;
        ISevenZipInArchive inArchive = null;
        try {
            randomAccessFile = new RandomAccessFile(filePath, "r");
            inArchive = SevenZip.openInArchive(null, // autodetect archive type
                    new RandomAccessFileInStream(randomAccessFile));

            // Getting simple interface of the archive inArchive
            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
            if(checkPassword(filePath, password))
            {
            System.out.println("   Hash   | Filename");
            System.out.println("----------+---------");
            
            for (final ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) 
            {
                final int[] hash = new int[] { 0 };
                if (!item.isFolder()) 
                {
                    ExtractOperationResult result;
//                    byte[] fileData = null;
                    result = item.extractSlow(new ISequentialOutStream() 
                    {
                        public int write(final byte[] data) throws SevenZipException 
                        {
                        	try
                        	{
//                        		if(item.getPath().indexOf('\\')>0)
                        		if(item.getPath().indexOf(File.separator)>0)
                        		{
//                        			String path = outputPath+File.separator+item.getPath().substring(0,item.getPath().lastIndexOf('\\'));
                        			String path = outputPath+File.separator+item.getPath().substring(0,item.getPath().lastIndexOf(File.separator));
                        		
                        		File folderExisting = new File(path);  
                        		boolean success = false;
                        		if (!folderExisting.exists()){
//                        			 success = (new File(path)).mkdir();
                        			success = (new File(path)).mkdirs();
                        			 System.out.println(success);
                        		}
                        		}
//                        		if(success){
                        		//System.out.println(outputPath+File.separator+item.getPath().substring(0,item.getPath().lastIndexOf('\\')));
                        		OutputStream out = new FileOutputStream(outputPath+File.separator+item.getPath());
                        		out.write(data);
                        		out.close();
//                        		}
                        	}
                        	catch( Exception e )
                        	{
                        		e.printStackTrace();
                        	}
//                        	System.out.println(new String(data));
                            hash[0] |= Arrays.hashCode(data);
                            return data.length; // Return amount of proceed data
                        }
                    },password); /// password.
                    if (result == ExtractOperationResult.OK) 
                    {
                        System.out.println(String.format("%9X | %s", // 
                                hash[0], item.getPath()));
                        map.put(count++, item.getPath());
//                        JOptionPane.showMessageDialog(null,item.getPath());
                        flag= true;
                       
                    }
                    else 
                    {
                        System.err.println("Error extracting item: " + result);
                    }
                }
            }
            }
    		else
    		{
    			JOptionPane.showMessageDialog(null, "Incorrect Password");
    		}
        }
        catch(FileNotFoundException fnfe)
        {
        	JOptionPane.showMessageDialog(null, "File Not found Check the file Path");
        }
        
        catch (Exception e) 
        {
            System.out.println("Error occurs: " + e);
            e.printStackTrace(System.out);
            System.exit(1);
        } finally 
        {
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (SevenZipException e) {
                    System.err.println("Error closing archive: " + e);
                    e.printStackTrace();
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    System.err.println("Error closing file: " + e);
                    e.printStackTrace();
                }
            }
            if(flag){
            	JOptionPane.showMessageDialog(null, "Success");
            }
            
        }
		
    
	}
	
	public boolean checkPassword(String name, String password) {
	    boolean passwordIsOk = false;
	    
        RandomAccessFile randomAccessFile = null;
        ISevenZipInArchive inArchive = null;
	   

        try {

        	ExtractOperationResult r;
        	randomAccessFile = new RandomAccessFile(name, "r");
        	inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));

        	ISequentialOutStream wirerx = new ISequentialOutStream() {

        		int count = 0;
        		public int write(byte[] arg0) throws PasswordOkException {
        			count += arg0.length;
        			if (count > 65536) {
        				// Password looks good
        				throw new PasswordOkException();
        			}
        			return arg0.length;
        		}
        	};

        	r = inArchive.extractSlow(0, wirerx, password);
        	if (r == ExtractOperationResult.OK) {
        		System.out.println("The extracted file must be empty. Can't check password on empty files anyway...");
        		
        		passwordIsOk = true; // ?? Can be password considered correct here ??
        	}
        } catch (PasswordOkException passwordOkException) {
	        passwordIsOk = true;
	    } catch (Exception e) {
	        // Exception will occur here, if password is incorrect
	        //System.out.println("Error occurs: " + e);
	        //System.exit(1);
	    } finally {
	        if (inArchive != null) {
	            try {
	                inArchive.close();
	            } catch (SevenZipException e) {
	                System.out.println("nError closing archive: " + e);
	            }
	        }
	        if (randomAccessFile != null) {
	            try {
	                randomAccessFile.close();
	            } catch (IOException e) {
	                System.out.println("Error closing file: " + e);
	            }
	        }
	    }
	    if (passwordIsOk) {
	        System.out.println("Password is Ok!");
	    } else {
	        System.out.println("Password is NOT ok!");
	    }
	    return passwordIsOk;
	}

	static class PasswordOkException extends SevenZipException {

	}
	

}
