package com.ope.patu.util.security.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.PublicKey;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import com.ope.patu.util.security.KeyReader;
import com.ope.patu.util.security.PATUCrypter;

public class FileSecurity 
{
	public String getFileContents( String filePath )
	{
		byte[] fileBytes = null;
		try
		{
			RandomAccessFile raf = new RandomAccessFile( filePath , "r" );
			int fileLength = ( int )raf.length();
			fileBytes = new byte[ fileLength ];
			while( fileLength > 0 )
			{
				fileLength -= raf.read( fileBytes,fileBytes.length - fileLength , fileLength );
			}
			raf.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return new String( fileBytes );
	}
	
	public byte[] getFileBytes( String filePath )
	{
		byte[] data = null;
		try
		{
			InputStream in = new FileInputStream( filePath );
			data = new byte[in.available()];
			in.read(data);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		System.out.println("data length------"+data.length);
		return data;
	}
	
	public void getFileString( String filePath )throws Exception
	{
		String publicKeyString = KeyReader.getPublicKeyString();
		PATUCrypter patuCrypter = new PATUCrypter();
		patuCrypter.setEncodedPublicKey( publicKeyString );
		
		PublicKey pubKey = patuCrypter.getPublicKey();
		
		Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String ss = "fdgjdfgjdfgjdf";
        byte[] bb = cipher.doFinal(ss.getBytes());
        String sss = new sun.misc.BASE64Encoder().encode(bb);
        System.out.println(sss);
		
		
		
		
		
		
		
		
		
		
//		Scanner fileScanner = new Scanner( new File( filePath ));
//		StringBuilder sb = new StringBuilder();
//		while( fileScanner.hasNextLine() )
//		{
//			String ss = fileScanner.nextLine();
//			sb.append( new sun.misc.BASE64Encoder().encode( patuCrypter.encrypt( ss.getBytes()) ));
//			sb.append("\n");
//		}
//		System.out.println(sb.toString());
	}
	
	public void en( byte[] fileBytes ) throws Exception
	{
		String publicKeyString = KeyReader.getPublicKeyString();
		PATUCrypter patuCrypter = new PATUCrypter();
		patuCrypter.setEncodedPublicKey( publicKeyString );
		byte[] datas = patuCrypter.encrypt( fileBytes );
		String ss = new sun.misc.BASE64Encoder().encode( datas );
		System.out.println(ss);
	}
	
	public static void main(String[] args)  
	{
		try
		{
			new FileSecurity().getFileString(null);
//			FileSecurity fs = new FileSecurity();
////			byte[] datas = fs.getFileBytes("C:/emp.xml");
//			InputStream in = new FileInputStream("C:/emp.xml");
//			byte[] b = new byte[in.available()];
//			System.out.println(b.length);
//			PATUCrypter patuCrypter = new PATUCrypter();
//			String publicKeyString = KeyReader.getPublicKeyString();
////			System.out.println(publicKeyString);
//			patuCrypter.setEncodedPublicKey( publicKeyString );
//			Cipher cipher = patuCrypter.createEncryptCipher();
//			CipherInputStream cis = new CipherInputStream( in , cipher );
//			OutputStream out = new FileOutputStream("C:/emp_en.xml");
//			int i = cis.read( b );
//			//(bufl = inputReader.read(buf)) != -1
//			System.out.println(i);
//			while( i != -1 )
//			{
//				out.write(b,0,i);
//				i = cis.read(b);
//				System.out.println(i);
//			}
//			in.close();
//			cis.close();
			
//			PATUCrypter patuCrypter = new PATUCrypter();
//			String publicKeyString = KeyReader.getPublicKeyString();
//			patuCrypter.setEncodedPublicKey( publicKeyString );
//			Cipher cipher = patuCrypter.createEncryptCipher();
//			
//			File inFile = new File("C:/t1.txt");
//			File outFile = new File("C:/e1.txt");
//			FileInputStream is = new FileInputStream(inFile);
//			CipherOutputStream os = new CipherOutputStream(
//					new FileOutputStream(outFile), cipher);
//			
//			int i;
//			byte[] b = new byte[1024];
//			while((i=is.read(b))!=-1) 
//			{
//				os.write(b, 0, i);
//			}

			
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
//		
		
		
		
		
		
		
//		new FileSecurity().getFileString("C:/emp.xml");
//		String str = new FileSecurity().getFileContents("C:/emp.xml");
	}
}
