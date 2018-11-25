package com.dnb.ope.security.test;

import java.security.PrivateKey;
import java.security.PublicKey;

import com.dnb.ope.security.core.KeyReader;
import com.dnb.ope.security.core.PATUCrypter;

public class TestFileCrypto 
{
	public static void encryptFile( String srcFile , String destnFile )
	{
		try
		{
			PATUCrypter patuCrypter = new PATUCrypter();
			PublicKey publicKey = patuCrypter.getPublicKeyFromString(KeyReader
					.getPublicKeyString());
			patuCrypter.encryptFile(srcFile, destnFile, publicKey);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	public static void decryptFile( String srcFile , String destnFile )
	{
		try
		{
			PATUCrypter patuCrypter = new PATUCrypter();
			PrivateKey privateKey = patuCrypter.getPrivateKeyFromString(KeyReader
					.getPrivateKeyString());
			patuCrypter.decryptFile(srcFile, destnFile, privateKey);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) 
	{
//		String srcFile = "C:/k1.txt";
//		String destnFile = "C:/k2.txt";
//		encryptFile(srcFile, destnFile);
		
		String srcFile = "C:/k2.txt";
		String destnFile = "C:/k3.txt";
		decryptFile(srcFile, destnFile);
		
		
		
		
	}
}
