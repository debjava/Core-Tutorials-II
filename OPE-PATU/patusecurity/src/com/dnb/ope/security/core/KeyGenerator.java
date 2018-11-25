package com.dnb.ope.security.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;


public class KeyGenerator 
{
	private static PATUCrypter patuCrypter = null;
	private static String HOMEDIR = System.getProperty("user.dir");
	private static String KEYDIR = HOMEDIR+File.separator+"keys";
	static
	{
		try
		{
			File dirFile = new File( KEYDIR );
			if( !dirFile.exists() )
				dirFile.mkdirs();
			
			patuCrypter = new PATUCrypter();
			patuCrypter.generateKey();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	private KeyGenerator()
	{
		super();
	}
	
	public static void generatePrivateKey()
	{
		try
		{
			String privateKeyString = patuCrypter.getPrivateKeyString();
			Properties privateProp = new Properties();
			OutputStream out = new FileOutputStream(KEYDIR + File.separator
					+ "private.key");
			privateProp.put("privatekey", privateKeyString);
			privateProp.store(out, "Private Key File");
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public static void generatePublicKey()
	{
		try
		{
			Properties publicProp = new Properties();
			OutputStream out = new FileOutputStream(KEYDIR + File.separator
					+ "public.key");
			String publicKeyString = patuCrypter.getPublicKeyString();
			publicProp.put("publickey", publicKeyString);
			publicProp.store(out, "Public Key File");
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
