package com.ope.patu.util.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class PATUCrypter 
{
	static final String asymmetricAlgorithm = "RSA";
	static final String asymmetricAlgorithmModePadding = "RSA/ECB/PKCS1Padding";
	
	static final int keySize = 1024;
	static final int blockCapacity = 117;

	private KeyPair keyPair;
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public PATUCrypter()
	{
		super();
	}

	public void generateKeyPair() throws NoSuchAlgorithmException 
	{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(asymmetricAlgorithm);
		keyPairGenerator.initialize(keySize);
		keyPair = keyPairGenerator.generateKeyPair();
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
	}

	public String getEncodedPublicKey() 
	{
		byte[] encodedKey = publicKey.getEncoded();
		return new sun.misc.BASE64Encoder().encode(encodedKey);
	}

	public String getEncodedPrivateKey()
	{
		byte[] encodedKey = privateKey.getEncoded();
		return new sun.misc.BASE64Encoder().encode(encodedKey);
	}

	public void setEncodedPublicKey(String key)
	throws NoSuchAlgorithmException, InvalidKeySpecException, IOException 
	{
		byte[] encodedKey = new sun.misc.BASE64Decoder().decodeBuffer(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
		publicKey = KeyFactory.getInstance(asymmetricAlgorithm).generatePublic(keySpec);
	}

	public void setEncodedPrivateKey(String key)
	throws NoSuchAlgorithmException, InvalidKeySpecException, IOException 
	{
		byte[] encodedKey = new sun.misc.BASE64Decoder().decodeBuffer(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
		privateKey = KeyFactory.getInstance(asymmetricAlgorithm).generatePrivate(keySpec);
	}

	public Cipher createEncryptCipher() 
	throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException  
	{
		Cipher encryptCipher = Cipher.getInstance(asymmetricAlgorithmModePadding);
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return encryptCipher;
	}

	public Cipher createDecryptCipher() 
	throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException  
	{
		Cipher decryptCipher = Cipher.getInstance(asymmetricAlgorithmModePadding);
		decryptCipher.init(Cipher.DECRYPT_MODE, privateKey );
		return decryptCipher;
	}

	public byte[] encrypt(byte[] bytes) throws Exception 
	{
		return createEncryptCipher().doFinal(bytes);
	}

	public byte[] decrypt(byte[] bytes) throws Exception 
	{
		return createDecryptCipher().doFinal(bytes);
	}    

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}


}
