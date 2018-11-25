package com.ope.patu.keys;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import javax.crypto.SecretKey;

import com.ope.patu.exception.MACException;
import com.ope.patu.keyutil.CommonUtil;
import com.ope.patu.keyutil.DESUtil;
import com.ope.patu.keyutil.ParityUtil;

public class MACGenerator 
{
	private static String macString;
	private static byte[] macBytes;
	
	public MACGenerator()
	{
		super();
	}
	
	private static byte[] getByteArray( String str )
	{
		byte[] byteData = null;
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream( bos );
			dos.writeBytes( str );
			byteData = bos.toByteArray();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return byteData;
	}
	
	private static void doMAC2( String[] strs , SecretKey skey )
	{
		byte[] Ti = null;
		byte[] Si = null;
		for( int i = 0 ; i < strs.length ; i++ )
		{
			try
			{
				String firstdata = strs[i];
				String nextData = strs[i+1];
				if( i == 0 )
				{
					Si = firstdata.getBytes();
//					Si = getByteArray( firstdata );
					
//					Si = CommonUtil.hexToBytes(firstdata);
					
					
				}
				Ti = DESUtil.getEncryptedBytes(Si, skey);
				Si = ParityUtil.xorArray( Ti , nextData.getBytes());//S
//				Si = ParityUtil.xorArray( Ti , getByteArray( nextData ) );//S
//				Si = ParityUtil.xorArray( Ti , CommonUtil.hexToBytes( nextData ) );//S
			}
			catch( IndexOutOfBoundsException ibe )
			{
				macBytes = Ti;
				macString = CommonUtil.bytesToHex(macBytes);
				System.out.println("In Caught Exception macBytes------"+macString);
//				ibe.printStackTrace();
				break;
			}
			catch( Exception e )
			{
				System.out.println("Other Exception thrown");
//				e.printStackTrace();
			}
		}
		macBytes = Ti;
		macString = CommonUtil.bytesToHex(macBytes);
//		System.out.println("Finally macBytes------"+macString);
	}
	
//	private static void doMAC1( String[] strs , SecretKey skey )
//	{
//		byte[] Tbytes = null;
//		byte[] sXorBytes = null;//S
//		try
//		{
//			for( int i = 0 ; i < strs.length ; i++ )
//			{
//				String firstdata = strs[i];
//				String nextData = strs[i+1];
//				if( Tbytes == null )
//				{
//					Tbytes = DESUtil.getEncryptedBytes(firstdata.getBytes(), skey);
//					sXorBytes = ParityUtil.xorArray( Tbytes , nextData.getBytes());//S
//				}
//				else
//				{
//					Tbytes = DESUtil.getEncryptedBytes(sXorBytes, skey);
//					sXorBytes = ParityUtil.xorArray( Tbytes , nextData.getBytes());//S
//				}
//			}
//			macBytes = Tbytes;
//		}
//		catch( Exception e )
//		{
//			//e.printStackTrace();
//			macBytes = Tbytes;
//		}
//		macString = CommonUtil.bytesToHex(macBytes);
//	}

	
//	private static void doMAC( String[] strs , SecretKey skey )
//	{
//		byte[] Tbytes = null;
//		byte[] xorBytes = null;//S
//		try
//		{
//			String data = strs[0];
//			Tbytes = DESUtil.getEncryptedBytes(data.getBytes(), skey);
//			for( int i = 1 ; i < strs.length ; i++ )
//			{
//				if( Tbytes.length < 8 )
//				{
//					System.out.println("Tbytes length-------"+Tbytes.length);
//					break;
//				}
//				byte[] strByte = strs[i].getBytes();
//				if( strByte.length < 8 )
//				{
//					System.out.println("str byte length-------"+strByte.length);
//					break;
//				}
//				xorBytes = ParityUtil.xorArray( Tbytes , strs[i].getBytes());//S
//				if( xorBytes.length < 8 )
//				{
//					System.out.println("xorBytes length-------"+xorBytes.length);
//					break;
//				}
//				Tbytes = DESUtil.getEncryptedBytes(xorBytes, skey);
//			}
//			macBytes = Tbytes;
//			macString = CommonUtil.bytesToHex(macBytes);
//		}
//		catch( Exception e )
//		{
//			System.out.println("Tbytes length-------"+Tbytes.length);
//			System.out.println("xorbytes length-------"+xorBytes.length);
//			System.out.println("------------DANGER ZONE---------------");
//			e.printStackTrace();
//		}
//		
//	}
	
	public void generateMAC1( Object ...objects ) throws MACException
	{
		String preAuthnString = ( String )objects[0] ;
		/*
		 * This can be HSK for Hash Value or AUK for Authentication Value
		 */
		byte[] keyBytes = ( byte[] )objects[1];
		String internalCodeString = CommonUtil.getInternalCodeString( preAuthnString );
		System.out.println(internalCodeString);
		System.out.println("After modification------"+internalCodeString.length());
		SecretKey kSecretKey = DESUtil.getsecretKey( keyBytes );
		String[] str = CommonUtil.getPartitionString1(internalCodeString, 8);
		System.out.println("******************* String Printing **************");
		show(str);
		System.out.println("******************* String Printing **************");
		doMAC2(str, kSecretKey);
	}
	
	public void show( String[] ss )
	{
		for( String s : ss )
			System.out.println(s);
	}
	
	public void generateMAC( Object ...objects ) throws MACException
	{
		/*
		 * For Hash Value computation
		 * 
		 * 1. Pass the actual data String without the
		 * 	  line feeds.
		 * For Hash value computation
		 * 2.Pass the HSK as the key input for the encryption
		 * 
		 * For Authentication value computation
		 * 
		 * 1. Pass the data as a String
		 * 2. Pass the AUK as the key input for the encryption 
		 */
		String preAuthnString = ( String )objects[0] ;
		byte[] keyBytes = ( byte[] )objects[1];//This can be HSK for Hash Value or AUK for Authentication Value
		String internalCodeString = CommonUtil.getInternalCodeString( preAuthnString );
		System.out.println(internalCodeString);
		System.out.println("After modification------"+internalCodeString.length());
		SecretKey kSecretKey = DESUtil.getsecretKey( keyBytes );
		String[] str = CommonUtil.getPartitionString(internalCodeString, 8);
		doMAC2(str, kSecretKey);
	}

	public String getMacString() throws MACException 
	{
		if( macString == null )
			throw new MACException();
		return macString;
	}

	public byte[] getMacBytes() throws MACException 
	{
		if( macBytes == null )
			throw new MACException();
		return macBytes;
	}
}
