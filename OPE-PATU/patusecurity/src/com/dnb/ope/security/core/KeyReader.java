package com.dnb.ope.security.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class KeyReader 
{
	private static String HOMEDIR = System.getProperty("user.dir");
	private static String KEYDIR = HOMEDIR+File.separator+"keys";
	
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
			InputStream in = new FileInputStream(KEYDIR + File.separator
					+ "public.key");
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
			InputStream in = new FileInputStream(KEYDIR + File.separator
					+ "private.key");
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
