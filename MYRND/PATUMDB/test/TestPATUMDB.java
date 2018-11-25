import java.util.HashMap;
import java.util.Map;

import com.ope.patu.beans.EmpBean;


public class TestPATUMDB 
{
	public static void main(String[] args) 
	{
//		String stringMsg = "HI techies";
//		new MessagePublisher().publish(stringMsg);
		
//		Map dataMap = new HashMap();
//		dataMap.put("temp", "AAAAA");
//		new MessagePublisher().publish(dataMap);
		
		EmpBean emp = new EmpBean();
		emp.setAdrs("India");
		emp.setAge(23);
		emp.setName("John");
		emp.setSalary(12345);
		new MessagePublisher().publish(emp);
		
		
	}
}
