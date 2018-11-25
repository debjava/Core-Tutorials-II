package com.ope.patu.util.test;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ope.patu.utility.PBEncryptionUtility;

public class TestPBEncryptionUtility 
{
	public static void writeToFile( Map map ) throws Exception
	{
		FileWriter writer = new FileWriter(System.getProperty("user.dir")
				+ File.separator + "namepwd.txt");
		Iterator itr = map.entrySet().iterator();
		StringBuilder sb = new StringBuilder();
		
		while( itr.hasNext() )
		{
			Map.Entry<String, String> me = ( Map.Entry<String, String> )itr.next();
			sb.append(me.getKey()).append("-----[").append(me.getValue()).append("]");
			sb.append("\n");
		}
		writer.write(sb.toString());
		writer.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		PBEncryptionUtility pbutility = new PBEncryptionUtility();
		List<String> namesList = new ArrayList<String>();
		namesList.add("deba");
		namesList.add("charles");
		namesList.add("manju");
		namesList.add("anantraj");
		namesList.add("vishnu");
		namesList.add("qa");
		namesList.add("admin");
		namesList.add("raja");
		namesList.add("satya");
		namesList.add("sakthi");
		Map<String, String> enMap = new HashMap<String, String>();
		for( int i = 0 ; i < namesList.size() ; i++ )
		{
			String toEncrypt = namesList.get( i );
			String encryptedString = pbutility.encrypt(toEncrypt);
			enMap.put(toEncrypt, encryptedString);
		}
		
		System.out.println(enMap);
		writeToFile(enMap);
		
//		String toEncrypt = "deba";
//		String encryptedString = pbutility.encrypt(toEncrypt);
//		System.out.println("En String--->>>"+encryptedString);
		
		//JnUTBPRNT+52X0BodLXz7qNL0CZhfMgg
		
//		String originalString = pbutility.decrypt(encryptedString);
//		System.out.println("Original Sound--->>>"+originalString);
	}

}
