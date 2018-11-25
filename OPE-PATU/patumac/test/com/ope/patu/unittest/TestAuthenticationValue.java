package com.ope.patu.unittest;

import javax.crypto.SecretKey;

import sun.io.Converters;

import com.ope.patu.keys.MACGenerator;
import com.ope.patu.keyutil.CommonUtil;
import com.ope.patu.keyutil.DESUtil;


public class TestAuthenticationValue 
{
	public static String getHexString(byte[] b) throws Exception {
		String result = "";
		for (int i=0; i < b.length; i++) {
			result +=
				Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
	
	public static String getAUKHexString()
	{
		String aukString = null;
		SecretKey key = DESUtil.getsecretKey();
		aukString = CommonUtil.bytesToHex( key.getEncoded() );
		return aukString;
	}
	
	public static String getAuthBySKE( String strToAuth , String aukString )
	{
		String macString = null;
		try
		{
			byte[] aukHexBytes = CommonUtil.hexToBytes( aukString );
			MACGenerator macGenerator = new MACGenerator();
			macGenerator.generateMAC1( strToAuth , aukHexBytes );
			macString = macGenerator.getMacString();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return macString;
	}
	
	public static String getAuthBySKH( String strToAuth , String aukString )
	{
		String macString = null;
		try
		{
			byte[] hexBytes = CommonUtil.hexToBytes( aukString );
			MACGenerator macGenerator = new MACGenerator();
			macGenerator.generateMAC( strToAuth.trim() , hexBytes );
			macString = macGenerator.getMacString();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return macString;
	}
	
	public static void main(String[] args) 
	{
		String strToAuth = ">>ESI161110 0000BasWare Bank740 SMH0037084405292227         99910000011111111        01080829141540000                                          ";
		//Result should be 57B098BA7B4754220
		System.out.println("Length--------"+strToAuth.length());
		/*
		 * customer side calculation
		 */
		String aukHexString = "AEBAE983D6406D07" ;
		String custAuthenticatedVal = getAuthBySKE(strToAuth , aukHexString );
		System.out.println("Authenticated Value by customer-------"+custAuthenticatedVal);
		
	}

}
