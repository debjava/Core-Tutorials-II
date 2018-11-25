import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;


public class FileCreater 
{
	private static String fileContents = null;
	static
	{
		//Read the file and store the contents in a String
		fileContents = getContents("temp.txt");
		//System.out.println(fileContents);
	}
	
	protected static String getContents( String filePath )
	{
		String contents = null;
		try
		{
			FileReader fr = new FileReader( filePath );
			BufferedReader br = new BufferedReader( fr );
			String temp = null;
			StringBuilder builder = new StringBuilder();
			while( (temp = br.readLine()) != null )
			{
				builder.append(temp);
				builder.append("\n");
			}
			contents = builder.toString();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return contents;
	}
	/*
	 * For 10 dirs with 3 nesting, files creation --- 31
	 * For 1000 dirs with 3 nesting, files creation --- 1375 
	 * For 10000 dirs with 3 nesting, files creation --- 14483
	 */
	protected static void createFiles( String filePath , int no )
	{
		String fileName = "test.txt";
		try
		{
			FileWriter writer = new FileWriter( filePath+File.separator+fileName);
			writer.write(fileContents);
			writer.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	protected static void createDirs()
	{
		for( int i = 0 ; i < 400 ; i++ )
		{
			String inner1 = "temp"+i;
			String inner2 = inner1+File.separator+"temp-"+i;
			String inner3 = inner2+File.separator+"temp_"+i;
			String inner4 = inner3+File.separator+"ope";
			String inner5 = inner4+File.separator+"patu";
			String inner6 = inner5+File.separator+"data";
			File dirs = new File("C:/deleteNtemp"+File.separator+inner6);
			dirs.mkdirs();
			createFiles(dirs.getAbsolutePath(), i);
		}
	}
	
	protected static List getFileLists()
	{
		/*
		 * Getting the list of file from 10000 dirs with 3 nested dirs time is 9031
		 */
		String rootPath = "C:/deleteNtemp";
		List fileLists = new LinkedList();
		fileLists = (List)FileUtils.listFiles(new File(rootPath), null, true);
		System.out.println(fileLists);
		return fileLists;
	}
	
	protected static void merge( List fileLists )
	{
		for( int i = 0 ; i < fileLists.size() ; i++ )
		{
			String filePath = ((File)fileLists.get(i)).getAbsolutePath();
			String fileContents = getContents(filePath);
			appendToFile(fileContents);
		}
	}
	
	private static void appendToFile( String contents )
	{
		String appndedFileName = "APP_M.txt";
		try
		{
			FileWriter writer = new FileWriter( appndedFileName , true );
			BufferedWriter bfwriter = new BufferedWriter( writer );
			bfwriter.write(contents);
			bfwriter.close();
			writer.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args) 
	{
		long startTime = System.currentTimeMillis();
		
//		createDirs();
//		getFileLists();
		merge(getFileLists());
		long endTime = System.currentTimeMillis();
		System.out.println("Estimated time-----"+(endTime-startTime));
	}

}
