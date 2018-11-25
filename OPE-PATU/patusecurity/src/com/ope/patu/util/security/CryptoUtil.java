package com.ope.patu.util.security;

public class CryptoUtil 
{
	public static void testEncryptedString() throws Exception
	{
		String toEncrypt = "The man in THE 20th centurY.AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV";
		byte[] strBytes = toEncrypt.getBytes();
		System.out.println(strBytes.length);
		String publicKeyString = KeyReader.getPublicKeyString();
		PATUCrypter patuCrypter = new PATUCrypter();
		patuCrypter.setEncodedPublicKey(publicKeyString);
		byte[] enBytes = patuCrypter.encrypt(strBytes);
		String enString = new sun.misc.BASE64Encoder().encode(enBytes);
		System.out.println("Encrypted String---->>>"+enString);

	}
	
	public static void testDecryptedString() throws Exception
	{
		String enString1 = "LWQaqiTQI5Ox+ggkEHX6Jl/hMlYwU3OoKoR9o1wDHi4QTR8g/h/Q08Jtco0Sud+1H8phUzucqoJO"
			+"irgxzipKMXhCsCl9R+Asl1JGppujWOEpNhRx7wc7BaPT9QwdZhnqPMdDHusxooizxsMdFTumf6Gt"
			+"ImgQLqo3F5OD4/kvG4o=";

		String privateKeyString = KeyReader.getPrivateKeyString();
		PATUCrypter patuCrypter = new PATUCrypter();
		patuCrypter.setEncodedPrivateKey(privateKeyString);

		byte[] enBytes = new sun.misc.BASE64Decoder().decodeBuffer(enString1);
		byte[] deBytes = patuCrypter.decrypt( enBytes );

		String deString = new String( deBytes );
		System.out.println(deString);

	}
	public static void main(String[] args) throws Exception
	{
		testEncryptedString();
//		testDecryptedString();
	}
}
