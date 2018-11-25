package com.ope.patu.util.security;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.WordUtils;

public class FileCrypto 
{
	private static PATUCrypter patuCrypter = new PATUCrypter();
	
	static
	{
		try
		{
			String publicKeyString = KeyReader.getPublicKeyString();
			patuCrypter.setEncodedPublicKey( publicKeyString );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}
	private FileCrypto()
	{
		super();
	}

	private static String encryptString( String str )
	{
		String encryptedString = null;
		try
		{
			encryptedString = new sun.misc.BASE64Encoder()
			.encode(patuCrypter.encrypt(str
					.getBytes()));
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return encryptedString;
	}
	
	private static String decryptString( String str )
	{
		String deString = null;
		try
		{
			deString = new sun.misc.BASE64Encoder()
			.encode(patuCrypter.decrypt(str
					.getBytes()));
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return deString;
	}
	
	public static void formatFile( String sourceFileName , String destnFileName )
	{
		try
		{
			File srcFile = new File( sourceFileName );
			Scanner fileScanner = new Scanner( srcFile );
			StringBuffer sb = new StringBuffer();
			while( fileScanner.hasNextLine() )
			{
				String lineString = fileScanner.nextLine();
				if( lineString.length() > 80 )
				{
					String wrappedString = WordUtils.wrap(lineString, 20, "\n", true);
					sb.append(wrappedString);
				}
				else
				{
					sb.append("\n");
					sb.append(lineString);
				}
			}
			FileWriter writer = new FileWriter( destnFileName );
			writer.write(sb.toString());
			writer.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public static void encryptFile( String srcFilePath , String destnFilePath )
	{
		try
		{
			File srcFile = new File( srcFilePath );
			Scanner fileScanner = new Scanner( srcFile );
			StringBuffer sb = new StringBuffer();
			while( fileScanner.hasNextLine() )
			{
				String lineString = fileScanner.nextLine();
				String enString = encryptString(lineString);
				System.out.println(enString.indexOf("\n"));
//				enString = enString.replaceAll("\n", "");
				sb.append( enString );
				
				sb.append("\n");
				sb.append("------------------------------------------------------------");
				
			}
			FileWriter writer = new FileWriter( destnFilePath );
			writer.write(sb.toString());
			writer.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public static void decryptFile( String srcFilePath , String destnFilePath )
	{
		String str1 = "------------------------------------------------------------";
		try
		{
			File srcFile = new File( srcFilePath );
			Scanner fileScanner = new Scanner( srcFile );
			StringBuffer sb = new StringBuffer();
			List l1 = new LinkedList(); 
			while( fileScanner.hasNextLine() )
			{
				String lineString = fileScanner.nextLine();
				if( lineString.equals(str1 ))
				{
					continue;
				}
				else
				{
					String de = decryptString(lineString);
					System.out.println(de);
				}
						
//				if( lineString.equals("\n") || lineString.length()== 0 )
//				{
//					/*
//					 * stop here and form the actual string
//					 */
//					System.out.println(sb.toString());
//					System.out.println("**************************************");
//					l1.add(sb);
//					sb = null;
//					sb = new StringBuffer();
//				}
//				else
//				{
//					sb.append(lineString);
//				}
				
			}
//			FileWriter writer = new FileWriter( destnFilePath );
//			writer.write(sb.toString());
//			writer.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception 
	{
		FileCrypto.formatFile("C:/t3.txt", "C:/t4.txt");
		FileCrypto.encryptFile("C:/t4.txt", "C:/t5.txt");
//		FileCrypto.decryptFile("C:/t5.txt", "C:/t5.txt");
		
		
		
	}
}
