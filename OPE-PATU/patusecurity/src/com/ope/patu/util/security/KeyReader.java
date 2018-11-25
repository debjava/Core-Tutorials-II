package com.ope.patu.util.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class KeyReader 
{
	private KeyReader()
	{
		super();
	}
	
	public static String getPublicKeyString()
	{
		String publicKeyString = null;
		try
		{
			Properties publicProp = new Properties();
			InputStream in = new FileInputStream(System.getProperty("user.dir")
					+ File.separator + "public.properties");
			publicProp.load(in);
			publicKeyString = publicProp.getProperty("publickey");
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return publicKeyString;
	}
	
	public static String getPrivateKeyString()
	{
		String privateKeyString = null;
		try
		{
			Properties privateProp = new Properties();
			InputStream in = new FileInputStream(System.getProperty("user.dir")
					+ File.separator + "private.properties");
			privateProp.load(in);
			privateKeyString = privateProp.getProperty("privatekey");
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return privateKeyString;
	}
}
