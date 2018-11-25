package com.ope.patu.util.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;


public class KeyGenerator 
{
	private static PATUCrypter patuCrypter = null;
	static
	{
		try
		{
			patuCrypter = new PATUCrypter();
			patuCrypter.generateKeyPair();
		}
		catch( Exception e )
		{
			
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
			String privateKeyString = patuCrypter.getEncodedPrivateKey();
			Properties privateProp = new Properties();
			OutputStream out = new FileOutputStream(System.getProperty("user.dir")
					+ File.separator + "private.properties");
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
			OutputStream out = new FileOutputStream(System.getProperty("user.dir")
					+ File.separator + "public.properties");
			String publicKeyString = patuCrypter.getEncodedPublicKey();
			publicProp.put("publickey", publicKeyString);
			publicProp.store(out, "Public Key File");
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
