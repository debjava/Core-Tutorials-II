package com.ope.patu.utility;

import java.io.File;
import java.io.FileWriter;

import javax.crypto.SecretKey;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class SymmetricKeyGenerator 
{
	private static final String symmetricAlgorithm = "AES";
	//128, 192 or 256 key size
	private static final int symmetricKeySize = 256;

	private static String generateSecretKey()
	{
		String generatedKey = null;
		SecretKey secretKey = null;
		try
		{
			javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator
			.getInstance(symmetricAlgorithm);
			keyGenerator.init(symmetricKeySize);
			secretKey = keyGenerator.generateKey();
			byte[] keyBytes = secretKey.getEncoded();
			generatedKey = Base64.encode(keyBytes);
		}
		catch( Exception e )
		{
			System.out.println("Error in generating file");
			e.printStackTrace();
		}
		return generatedKey;
	}
	
	private static void generateSecretFile()
	{
		String fileName = System.getProperty("user.dir")+File.separator+"key.txt";
		try
		{
			FileWriter fileWriter = new FileWriter( fileName );
			fileWriter.write(generateSecretKey());
			fileWriter.close();
		}
		catch( Exception e )
		{
			System.out.println("Error in generating file");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		generateSecretFile();
	}
}
