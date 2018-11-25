import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;


public class TestPdfPassword {

	public static void main(String[] args)
	{
		try 
		{
			PdfReader reader = new PdfReader(new FileInputStream("testdata/appendix_e.pdf"));
			PdfEncryptor.encrypt(
				      reader,  // A reader for the original file
				      new FileOutputStream("testdata/sample-encrypted.pdf"), // The output file 
				      true,  //true -> 128 bit, false -> 40 bit
				       "deba",  // User password
				       "test",   // Owner password
				       PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY );// User permissions);

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
