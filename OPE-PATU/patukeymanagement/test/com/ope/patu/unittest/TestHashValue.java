package com.ope.patu.unittest;

import java.io.File;
import java.util.Scanner;

import javax.crypto.SecretKey;

import com.ope.patu.keys.MACGenerator;
import com.ope.patu.keyutil.CommonUtil;

public class TestHashValue 
{
	public static String getLineByLineFileContents( String filePath )
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			File file = new File( filePath );
			Scanner scanner = new Scanner( file );
			while( scanner.hasNext() )
			{
				String msg = scanner.nextLine();
				sb.append(msg).append("\n");
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static String getFileContents()
	{
		String filePath = System.getProperty("user.dir") + File.separator
		+ "testdata" + File.separator + "billpaymentdata11.txt";
		String contents = null;
		contents = getLineByLineFileContents(filePath);
		return contents;
	}
	
	public static String getCustomerHashValue( String hskString )
	{
		String custHashValue = null;
		try
		{
			byte[] hskByte = CommonUtil.hexToBytes( hskString );
			String fileContents = getFileContents();
			/*
			 * Remove all the line feeds
			 */
			String strNoLineFeed = fileContents.replaceAll("\n", "");
			MACGenerator macGen = new MACGenerator();
			macGen.generateMAC(strNoLineFeed,hskByte);
			custHashValue = macGen.getMacString();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return custHashValue;
	}
	
	public static String getServerHashValue( String hskString )
	{
		String computedHashValue = null;
		try
		{
			byte[] hskByte = CommonUtil.hexToBytes( hskString );
			String fileContents = getFileContents();
			/*
			 * Remove all the line feeds
			 */
			String strNoLineFeed = fileContents.replaceAll("\n", "");
			MACGenerator macGen = new MACGenerator();
			macGen.generateMAC(strNoLineFeed,hskByte);
			computedHashValue = macGen.getMacString();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return computedHashValue;
	}
	
	public static void main(String[] args) 
	{
		/*
		 * The following is the HSK which is available
		 * at SUO and VAR message
		 */
		//2790FF84A73D4C93//HSK value present in the SUO and VAR message
		String hskStr = "2790FF84A73D4C93";
		String hashValFromSUOMsg = getCustomerHashValue(hskStr);
		System.out.println("Hash value from SUO message-----"+hashValFromSUOMsg);
		
//		Hash Value present in SUO and VAR message
//		B9CDA4DB1DA5CCE7
		String computedHashVal = getServerHashValue(hskStr);
		System.out.println("Computed Hash Value------"+computedHashVal);
		if( hashValFromSUOMsg.equals( computedHashVal ))
			System.out.println("Hash value checking------"+true);
	}

}
