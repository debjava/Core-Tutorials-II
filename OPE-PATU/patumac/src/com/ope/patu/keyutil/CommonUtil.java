package com.ope.patu.keyutil;

import java.util.HashMap;
import java.util.Map;

public class CommonUtil 
{
	public static Map<Integer, String> getFileSequence()
	{
		Map<Integer, String> sequenceMap = new HashMap<Integer, String>();
		sequenceMap.put(1, "ESIp.txt");
		sequenceMap.put(2, "billpmt.akn");
		sequenceMap.put(3, "billpmt.fbk");
		return sequenceMap;
	}

	public static String getFileName( int value )
	{
		return getFileSequence().get(value);
	}

	public static String getInternalCodeString( String str )
	{
		String tempStr = str.toUpperCase();
		/*
		 * Remove the special chars with blank space
		 * The special characters may be $, # or
		 * Scandinavian characters
		 */
		tempStr = removeSpecialChars(tempStr);
		StringBuilder internalBuilder = new StringBuilder();
		for( int i = 0 , n = tempStr.length() ; i < n ; i++ )
		{
			char ch = tempStr.charAt(i);
			int decimalVal = ( int )ch;
			String hexString = Integer.toHexString( decimalVal );
			internalBuilder.append( hexString );
		}
		return internalBuilder.toString().toUpperCase();
	}
	
	public static String[] getPartitionString( String str , int val )
	{
		System.out.println("Totoal String length---------"+str.length());
		int no_of_zeros_to_add = 0;
		int remainder = str.length() % val;
		System.out.println("Remainder----"+remainder);
		if( remainder != 0 )
		{
			no_of_zeros_to_add = val - remainder ;
		}
		System.out.println("No of 0s to add--------"+no_of_zeros_to_add);
		StringBuilder sb = new StringBuilder( str );
		for( int i = 0 ; i < no_of_zeros_to_add ; i++ )
		{
			sb.append("0");
		}
		str = sb.toString();
		int loopVal = str.length() / val ;
		String[] arrStr = new String[ loopVal ];
		for( int i = 0 ; i < loopVal ; i++ )
			arrStr[i] = str.substring( (i*val),(i+1)*val);
		return arrStr ;
	}
	
	public static String[] getPartitionString1( String str , int maxLen )
	{
		int stringLength = str.length();
		// calculate the number of substrings we'll need to make
		int nofOfStrs = stringLength/maxLen;
		if (stringLength % maxLen > 0)
			nofOfStrs += 1;

		// initialize the result array
		String[] splittedArr = new String[nofOfStrs];

		for (int i = 0; i < nofOfStrs; i++) 
		{
			// the substring starts here
			int startPos = i * maxLen;

			// the substring ends here
			int endPos = startPos + maxLen;

			// make sure we don't cause an IndexOutOfBoundsException
			if (endPos > stringLength)
				endPos = stringLength;

			// make the substring
			String substr = str.substring(startPos, endPos);

			// stick it in the result array
			splittedArr[i] = substr;
		}

		// return the result array
		return splittedArr;
	}

//	public static String[] getPartitionString( String str , int val )
//	{
//		int remainder = str.length() % val;
//		int no_of_zeros_to_add = val - remainder ;
//		StringBuilder sb = new StringBuilder( str );
//		for( int i = 0 ; i < no_of_zeros_to_add ; i++ )
//		{
//			sb.append("0");
//		}
//		str = sb.toString();
//		int loopVal = str.length() / val ;
//		String[] arrStr = new String[ loopVal ];
//		for( int i = 0 ; i < loopVal ; i++ )
//			arrStr[i] = str.substring( (i*val),(i+1)*val);
//		return arrStr ;
//	}

	/**
	 * Converts byte array into String
	 * @param data
	 * @return string
	 */
	public static String bytesToHex(byte[] data) 
	{
		if (data==null) 
		{
			return null;
		}
		else 
		{
			int len = data.length;
			String str = "";
			for (int i=0; i<len; i++) 
			{
				if ((data[i]&0xFF)<16) str = str + "0" 
				+ java.lang.Integer.toHexString(data[i]&0xFF);
				else str = str
				+ java.lang.Integer.toHexString(data[i]&0xFF);
			}
			return str.toUpperCase();
		}
	}

	/**
	 * Converts String into byte array
	 * @param str
	 * @return byte array
	 */
	public static byte[] hexToBytes(String str) 
	{
		if (str==null) 
		{
			return null;
		}
		else if (str.length() < 2) 
		{
			return null;
		}
		else 
		{
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i=0; i<len; i++)
			{
				buffer[i] = (byte) Integer.parseInt(
						str.substring(i*2,i*2+2),16);
			}
			return buffer;
		}
	}
	
	public static String removeSpecialChars( String str )
	{
		String removedStr = null;
		String pattern = "[^a-zA-Z0-9%()*+,-./:;<=>]";
		removedStr = str.replaceAll(pattern, " ");
		return removedStr;
	}



}
