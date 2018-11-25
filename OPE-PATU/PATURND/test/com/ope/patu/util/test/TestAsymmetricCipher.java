package com.ope.patu.util.test;

import com.ope.patu.utility.AsymmetricCipher;

public class TestAsymmetricCipher
{
	public static void main(String[] args) throws Exception 
	{
		AsymmetricCipher as = new AsymmetricCipher();
		as.generateKeyPair();
		System.out.println(as.getEncodedPublicKey());
	}
}
