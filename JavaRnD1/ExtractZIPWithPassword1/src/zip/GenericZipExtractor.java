package zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

public class GenericZipExtractor 
{
	public static void unzip(String filePath, String password, final String outputPath)
	{
		RandomAccessFile randomAccessFile = null;
		ISevenZipInArchive inArchive = null;
		try 
		{
			randomAccessFile = new RandomAccessFile(filePath, "r");
			inArchive = SevenZip.openInArchive(null, // autodetect archive type
					new RandomAccessFileInStream(randomAccessFile));

			// Getting simple interface of the archive inArchive
			ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

			System.out.println("   Hash   | Filename");
			System.out.println("----------+---------");

			for (final ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) 
			{

				final int[] hash = new int[] { 0 };
				if (!item.isFolder()) 
				{
					ExtractOperationResult result;
					result = item.extractSlow(new ISequentialOutStream() 
					{
						public int write(final byte[] data) throws SevenZipException 
						{
							try
							{
								if(item.getPath().indexOf(File.separator)>0)
								{
									String path = outputPath+File.separator+item.getPath().substring(0,item.getPath().lastIndexOf(File.separator));

									File folderExisting = new File(path);  
									if (!folderExisting.exists())
										new File(path).mkdirs();
								}
								OutputStream out = new FileOutputStream(outputPath+File.separator+item.getPath());
								out.write(data);
								out.close();
							}
							catch( Exception e )
							{
								e.printStackTrace();
							}
							hash[0] |= Arrays.hashCode(data);
							return data.length; // Return amount of proceed data
						}
					},password); /// password.
					if (result == ExtractOperationResult.OK) 
					{
						System.out.println(String.format("%9X | %s", // 
								hash[0], item.getPath()));
					}
					else 
					{
						System.err.println("Error extracting item: " + result);
					}
				}

			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if (inArchive != null)
			{
				try 
				{
					inArchive.close();
				}
				catch (SevenZipException e)
				{
					System.err.println("Error closing archive: " + e);
					e.printStackTrace();
				}
			}
			if (randomAccessFile != null) 
			{
				try 
				{
					randomAccessFile.close();
				}
				catch (IOException e) 
				{
					System.err.println("Error closing file: " + e);
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) 
	{
//		unzip("temp/test.zip", "pikupiku", "temp/");
		unzip("temp/encrypted.zip", "pikupiku", "temp/");
	}

}
