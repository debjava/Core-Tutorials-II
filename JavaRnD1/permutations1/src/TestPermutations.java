import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestPermutations 
{
	    public static <T> List<T> permute(List<T> list, int r) { 
	  
	        int n= list.size(); 
	        int f= fac(n); 
	        List<T> perm= new ArrayList<T>(); 
	  
	        list= new ArrayList<T>(list); 
	  
	        for (list= new ArrayList<T>(list); n > 0; n--, r%= f) { 
	  
	            f/= n; 
	           perm.add(list.remove(r/f)); 
	        } 
	        return perm; 
	    } 
	    
	    public static int fac(int n) { 
	    	  
	        int f= 1; 
	  
	        for (; n > 0; f*= n--); 
	  
	        return f; 
	    } 

	    
	    public static void main(String[] args) { 
	    	  
//	        List<String> list= Arrays.asList("A", "B", "C");
	    	 List<String> list= Arrays.asList("X", "T", "B"); 
	  
	        for (int i= 0, n= 3, f= fac(n); i < f; i++) 
	            System.out.println(i+": "+permute(list, i)); 
	    } 


}
