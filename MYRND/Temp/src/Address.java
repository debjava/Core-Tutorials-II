
public class Address 
{
		
	private String street;

	public String getStreet() 
	{
		return street;
	}
	
	public void setStreet(String newStreet) 
	{

	}
	
	public static void main(String[] args) 
	{
		Address a = new Address();
		a.setStreet("sdsdfsdfsdfdsdd");
		System.out.println(a.getStreet());
	}
	
}
