import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.codec.language.Soundex;

public class Test 
{
	public static void main(String[] args) throws Exception
	{
		String s1 = "the sky";//"ice cream";//"carat";//"Nelson";
		String s2 = "this guy";//"I scream";//"caret";//"Neilson";
		
		Metaphone mp = new Metaphone();
		System.out.println("Encoded value---->"+mp.encode(s1));
		System.out.println("Equal ?---->"+mp.isMetaphoneEqual(s1, s2));
		System.out.println("Metaphone Value----->"+mp.metaphone(s1));
		
		Soundex snd = new Soundex();
		System.out.println("Soundex Encoded value---->"+snd.encode(s1));
		System.out.println("Soundex Value----->"+snd.soundex(s1));
		
		if( snd.soundex(s1).equals(snd.soundex(s2)))
			System.out.println("Equal");
		
	}

}
