package com.dnb.ope.security.test;

import com.dnb.ope.security.core.KeyGenerator;

public class TestKeyGenerator 
{
	public static void main(String[] args) 
	{
		KeyGenerator.generatePrivateKey();
		KeyGenerator.generatePublicKey();
	}

}
