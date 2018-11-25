package com.dnb.ope.security.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.sun.crypto.provider.SunJCE;

public class PATUCrypter 
{
	private static final String ALGORITHM = "RSA";
	private final String ALGORITHM_PADDING = "RSA/ECB/PKCS1Padding";
	private static final int KEY_SIZE = 1024;

	private PublicKey publicKey = null;
	private PrivateKey privateKey = null;

	public PATUCrypter()
	{
		super();
		Security.addProvider( new SunJCE() );
	}

	public void generateKey() throws NoSuchAlgorithmException
	{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		keyGen.initialize( KEY_SIZE );
		KeyPair keyPair = keyGen.generateKeyPair();
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
	}

	private String getKeyAsString( Key key )
	{
		// Get the bytes of the key
		byte[] keyBytes = key.getEncoded();
		// Convert key to BASE64 encoded string
		BASE64Encoder b64 = new BASE64Encoder();
		return b64.encode(keyBytes);
	}

	public String getPublicKeyString( PublicKey publicKey )
	{
		return getKeyAsString( publicKey );
	}

	public String getPublicKeyString()
	{
		return getKeyAsString( publicKey );
	}

	public String getPrivateKeyString( PrivateKey privateKey )
	{
		return getKeyAsString( privateKey );
	}

	public String getPrivateKeyString()
	{
		return getKeyAsString( privateKey );
	}

	public PrivateKey getPrivateKeyFromString( String privateKeyString ) throws Exception
	{
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		BASE64Decoder b64 = new BASE64Decoder();
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(b64.decodeBuffer(privateKeyString));
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		return privateKey;
	}

	public PublicKey getPublicKeyFromString( String publicKeyString ) throws Exception
	{
		BASE64Decoder b64 = new BASE64Decoder();
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(b64.decodeBuffer(publicKeyString));
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		return publicKey;
	}

	public void encryptFile(String srcFileName, String destFileName, PublicKey publicKey) throws Exception
	{
		encryptDecryptFile(srcFileName,destFileName, publicKey, Cipher.ENCRYPT_MODE);
	}

	public void decryptFile(String srcFileName, String destFileName, PrivateKey privateKey) throws Exception
	{
		encryptDecryptFile(srcFileName,destFileName, privateKey, Cipher.DECRYPT_MODE);
	}

	private void encryptDecryptFile(String srcFileName, String destFileName,
			Key key, int cipherMode) throws Exception
			{
		OutputStream outputWriter = null;
		InputStream inputReader = null;
		try
		{
			Cipher cipher = Cipher.getInstance(ALGORITHM_PADDING);
			byte[] buf = cipherMode == Cipher.ENCRYPT_MODE? new byte[100] : new byte[128];
			int bufl;
			// init the Cipher object for Encryption...
			cipher.init(cipherMode, key);

			// start FileIO
			outputWriter = new FileOutputStream(destFileName);
			inputReader = new FileInputStream(srcFileName);
			while ( (bufl = inputReader.read(buf)) != -1)
			{
				byte[] encText = null;
				if (cipherMode == Cipher.ENCRYPT_MODE)
				{
					encText = encrypt(copyBytes(buf,bufl),(PublicKey)key);
				}
				else
				{
					encText = decrypt(copyBytes(buf,bufl),(PrivateKey)key);
				}
				outputWriter.write(encText);
			}
			outputWriter.flush();

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if (outputWriter != null)
				{
					outputWriter.close();
				}
				if (inputReader != null)
				{
					inputReader.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace(); 
			} 
		}
			}

	private byte[] copyBytes(byte[] arr, int length)
	{
		byte[] newArr = null;
		if (arr.length == length)
		{
			newArr = arr;
		}
		else
		{
			newArr = new byte[length];
			for (int i = 0; i < length; i++)
			{
				newArr[i] = (byte) arr[i];
			}
		}
		return newArr;
	}

	private byte[] encrypt(byte[] text, PublicKey key) throws Exception
	{
		byte[] cipherText = null;
		try
		{
			Cipher cipher = Cipher.getInstance(ALGORITHM_PADDING);
			// encrypt the plaintext using the public key
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text);
		}
		catch (Exception e)
		{
			throw e;
		}
		return cipherText;
	}

	private byte[] decrypt(byte[] text, PrivateKey key) throws Exception
	{
		byte[] dectyptedText = null;
		try
		{
			Cipher cipher = Cipher.getInstance(ALGORITHM_PADDING);
			cipher.init(Cipher.DECRYPT_MODE, key);
			dectyptedText = cipher.doFinal(text);
		}
		catch (Exception e)
		{
			throw e;
		}
		return dectyptedText;

	}

	public PublicKey getPublicKey() 
	{
		return publicKey;
	}

	public PrivateKey getPrivateKey() 
	{
		return privateKey;
	}
}
