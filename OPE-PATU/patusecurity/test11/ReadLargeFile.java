import java.io.File;
import java.util.Scanner;


public class ReadLargeFile 
{
	public static void main(String[] args) 
	{
//		try
//		{
//			File inFile = new File("C:/t1.txt");
//			Scanner scan = new Scanner( inFile );
//			StringBuffer sb = new StringBuffer();
//			while( scan.hasNextLine() )
//			{
//				String longLine = scan.nextLine();
//				if( longLine.length() > 100 )
//				{
//					for( int i = 0 ; i < longLine.length(); i+=100 )
//					{
//						System.out.println(longLine.substring(i,100));
//					}
//					
//				}
//				else
//					System.out.println(longLine);
//				
//			}
//		}
//		catch( Exception e )
//		{
//			e.printStackTrace();
//		}
		
		String ss = "AAAABBBCCC";
		//0,3 , 4,7 , 8, 11
		String s1 = ss.substring(6,9);
		System.out.println(s1);
			
	}
}
