package com.ddlabs.core.zip.test;

import com.ddlabs.core.zip.ZipUtil;

/**This is a test harness class to test all the functionalities defined
 * in the ZipUtil class.
 * @author Debadatta Mishra(PIKU)
 *
 */
public class TestZipUtil 
{
	public static void main(String[] args) 
	{
		//Normal Zip
		//ZipUtil.zipDir("testdata/SWT", "testdata/temp.zip");
		//ZipUtil.zipDir("testdata/SWT", null);

		//Zip with Password
		ZipUtil.zipDirWithPassword("testdata/SWT", "testdata/temp.zip","abcd1234");
		//ZipUtil.zipDirWithPassword("testdata/SWT", null,"abcd1234");

		//Normal Unzip
		//ZipUtil.unzipDir("testdata/temp.zip", "tempdata");
		ZipUtil.unzipDirWithPassword("testdata/temp.zip", "tempdata", "abcd1234");

	}

}
