import java.io.File;

public class RenameUtil 
{
	private static int seqenceNo = 0;
//	private static String seq = "_NO_";
	private static String seq = "DK_";

	private static String getNewFileName( String srcFileName , String sequence )
	{
		String firstPart = srcFileName.substring(0,srcFileName.indexOf("."));
		String lastPart = srcFileName.substring(srcFileName.indexOf("."));
		String newFileName = firstPart+sequence+lastPart;
		return newFileName;
	}

	public static void renameFilesInDir( String srcDirPath )
	{
		try
		{
			File photoDirs = new File(srcDirPath);
			File[] photoFiles = photoDirs.listFiles();

			for( int i = 0 ; i < photoFiles.length ; i++ )
			{
//				String newFileName = getNewFileName(photoFiles[i].getName(), seq+(seqenceNo++));
//				String newPath = photoDirs.getAbsolutePath()+File.separator+newFileName;
				String newPath = photoDirs.getAbsolutePath()+File.separator+seq+(seqenceNo++)+".JPG";
				System.out.println(newPath);
				photoFiles[i].renameTo( new File( newPath ));
			}
			System.out.println("Sequence No :::"+seqenceNo);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
