package com.ope.patu.unittest;

import javax.crypto.SecretKey;
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
	
	public static String getcustomerCalcAuth( String strToAuth )
	{
		String macString = null;
		try
		{
			/*
			 * Get the AUK from database
			 */
			//KEK Part1=340E7EDC49EA0D5A
			//KEK Part2=BF2733586FDCFCD7
			//KEK=8A294C852637F18C
			//KVV=ED0C95
			//AUK=29543191DF469125
			//5d02cd85c88c97e0//Auk String
			
			/*
			 * AUK value calculated based upon the
			 * data sent by Shripad
			 * AUK=AEBAE983D6406D07
			 */
//			String hexString = "29543191DF469125";//"5d02cd85c88c97e0";
			String hexString = "AEBAE983D6406D07";//"5d02cd85c88c97e0";
			byte[] hexBytes = CommonUtil.hexToBytes(hexString);
			MACGenerator macGenerator = new MACGenerator();
			macGenerator.generateMAC( strToAuth , hexBytes );
			macString = macGenerator.getMacString();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return macString;
	}
	
//	public static String getcustomerCalcAuth( String strToAuth )
//	{
//		String macString = null;
//		try
//		{
//			/*
//			 * Get the AUK from database
//			 */
//			//5d02cd85c88c97e0//Auk String
//			String hexString = "5d02cd85c88c97e0";
//			byte[] hexBytes = CommonUtil.hexToBytes(hexString);
//			MACGenerator macGenerator = new MACGenerator();
//			macGenerator.generateMAC( strToAuth , hexBytes );
//			macString = macGenerator.getMacString();
//		}
//		catch( Exception e )
//		{
//			e.printStackTrace();
//		}
//		return macString;
//	}
	
	public static String getPatuCalcAuth( String strToAuth )
	{
		String macString = null;
		try
		{
			/*
			 * Get the AUK from database
			 */
			//5d02cd85c88c97e0//Auk String
			String hexString = "5d02cd85c88c97e0";
			byte[] hexBytes = CommonUtil.hexToBytes(hexString);
			MACGenerator macGenerator = new MACGenerator();
			macGenerator.generateMAC( strToAuth , hexBytes );
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
//		String strToAuth = ">>ESI161120     BANKSOFT    4.10SMH00370844052922227        WMDATA20101302204        11080827124248123                                          ";
		String strToAuth = ">>ESI161110 0000BasWare Bank740 SMH0037084405292227         99910000011111111        01080829141540000                                          ";
		/*
		 * customer side calculation
		 */
		strToAuth = strToAuth.trim();
		String custAuthenticatedVal = getcustomerCalcAuth(strToAuth);
		System.out.println("Authenticated Value by customer-------"+custAuthenticatedVal);
//		/*
//		 * Server side calculation
//		 */
//		String patuAuthenticatedVal = getPatuCalcAuth(strToAuth);
//		System.out.println("Authenticated value by PATU server------"+patuAuthenticatedVal);
//		System.out.println("Check condition----"+custAuthenticatedVal.equals(patuAuthenticatedVal));
	}

}
