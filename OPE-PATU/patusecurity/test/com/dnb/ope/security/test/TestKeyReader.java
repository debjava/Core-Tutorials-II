package com.dnb.ope.security.test;

import com.dnb.ope.security.core.KeyReader;

public class TestKeyReader 
{
	public static void main(String[] args) 
	{
		String publicString = KeyReader.getPublicKeyString();
		System.out.println("Public String--->>>"+publicString);
		String privateString = KeyReader.getPrivateKeyString();
		System.out.println("Private String--->>>"+privateString);
	}

}
