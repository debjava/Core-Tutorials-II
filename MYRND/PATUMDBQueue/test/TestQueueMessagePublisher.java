import java.util.HashMap;
import java.util.Map;

import com.ope.patu.beans.EmpBean;


public class TestQueueMessagePublisher 
{
	public static void main(String[] args) 
	{
//		String stringMsg = "HI techies";
//		new QueueMessagePublisher().publish(stringMsg);
		
		Map dataMap = new HashMap();
		dataMap.put("temp", "AAAAA");
		new QueueMessagePublisher().publish(dataMap);
		
		EmpBean emp = new EmpBean();
		emp.setAdrs("India");
		emp.setAge(23);
		emp.setName("John");
		emp.setSalary(12345);
		new QueueMessagePublisher().publish(emp);
		
		
	}
}
