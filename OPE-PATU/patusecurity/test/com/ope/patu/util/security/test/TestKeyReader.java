package com.ope.patu.util.security.test;

import com.ope.patu.util.security.KeyReader;

public class TestKeyReader 
{
	public static void main(String[] args) 
	{
		String publicKeyString = KeyReader.getPrivateKeyString();
		System.out.println("Public Key String---->>>"+publicKeyString);
		String privateKeyString = KeyReader.getPrivateKeyString();
		System.out.println("Private Key String---->>>"+privateKeyString);
	}
}
