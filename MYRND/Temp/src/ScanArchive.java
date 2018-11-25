import java.io.*; 
import java.util.*; 
import java.util.jar.*; 
/** * @author pradeep.k * */
public class ScanArchive 
{
	/** * @param file * @param args * @return void * @throws FileNotFoundException * @throws IllegalArgumentException * @throws IOException */ 
	public void searchArchive(File file,String args) throws FileNotFoundException,IllegalArgumentException,IOException 
	{
		//validate the input directory 'file' 
		if(!file.exists()) 
			throw new FileNotFoundException("specified directory does not exist");
		if(!file.isDirectory()) 
			throw new IllegalArgumentException("directory need to be specified.");
		File[] lstFile = file.listFiles(); 
		if(lstFile == null) return; 
		//iterate throw the list of directories & files 
		for(int j=0;j<lstFile.length;j++)
		{ if(lstFile[j].isDirectory()) 
		{ 
			//recursive call for each directory 
			searchArchive(lstFile[j],args);
		}
		else
		{ 
			String filename=""; filename=lstFile[j].toString();
			//check whether file .jar || .zip 
			if(lstFile[j].canRead() & isFileSearchable(filename)) 
			{ 
				JarFile jf = null; 
				try

				{
					//create new jar file 
					jf=new JarFile(lstFile[j]);
				}
				catch(IOException ioe)
				{
					throw new IOException("unable to process '"+lstFile[j]+"'");
				}
				if (jf!=null)
				{
					Enumeration jarenum=jf.entries();
					//iterate throw each and every file under .zip or .jar file 
					while(jarenum.hasMoreElements()) 
					{
						String zipfilename="";
						try
						{
							//if unable to read contents of the archive then show message 
							zipfilename=jarenum.nextElement().toString(); 
							System.out.println("Zip file Name >>>"+zipfilename);
						}
						catch
						(java.lang.InternalError e)
						{
							System.out.println("\n\n\tUnable to scan '"+lstFile[j]+"'");
							break;
						}
						String str; 
						String input;
						str=zipfilename.substring(zipfilename.lastIndexOf("/")+1).toLowerCase();
						System.out.println(str);
						input=args.toLowerCase();
						if((str.indexOf(input)) >=0)
						{
							System.out.println("\n\n\tFound file > "+ zipfilename.substring(zipfilename.lastIndexOf("/")+1)+ "\n\tUnder directory > "+lstFile[j].toString()); 
						}
						// if close 
					}
					// while close 
				}//if close 
			}
			//if close 
		}//if else close 
		}//for close 
	}
	private boolean isFileSearchable(String fileName)
	{
		fileName = fileName.substring(fileName.lastIndexOf('.')+1);
		if(fileName.equalsIgnoreCase("jar") || fileName.equalsIgnoreCase("zip"))
		{
			return true; 
		}
		return false; 
	}
	public static void main(String[] args) 
	{
		File file; 
//		if(args.length==0) 
//		{
//			System.out.println("Please enter Folder & File Name"); 
//			System.exit(1);
//		}
//		else
		{
			try {
				file=new File("C:/dev/MYRND/Temp/lib"); 
				String text="DateUtil"; 
				ScanArchive scanjar = new ScanArchive();
				scanjar.searchArchive(file,text); 
			}
			catch(FileNotFoundException e)
			{ 
				System.out.println(e.getMessage()); 
			}
			catch(IllegalArgumentException e)
			{
				System.out.println(e.getMessage());
			}
			catch(IOException e) 
			{ 
				System.out.println(e.getMessage()); 
			}
		}
	}
}
