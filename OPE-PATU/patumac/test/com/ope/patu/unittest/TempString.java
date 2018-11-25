package com.ope.patu.unittest;

public class TempString 
{
	public static String[] split(String str , int maxLen ) 
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

	public static void main(String[] args) 
	{
		String s = "12345678012345678";
		String[] strArr = split(s, 8);
		for( String s1 : strArr )
			System.out.println("---->"+s1);
		
	}
}
