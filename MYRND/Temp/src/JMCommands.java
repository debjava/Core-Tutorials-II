//import java.util.LinkedList; 
//import java.util.StringTokenizer; 
//public class JMCommands
//{
//	private LinkedList<String> com; 
//	private int index; 
//	private CommandListener commandListener;
//	private commandEvent event;
//	String command; 
//	LinkedList<String> parameters; 
//	String parsedInstruction; 
//	public JMCommands() 
//	{
//		com=new LinkedList<String>();
//		com.add("OPEN");
//		com.add("LS");
//		com.add("GET"); 
//		com.add("DELETE");
//		com.add("PUT");
//		com.add("CD");
//		com.add("QUIT");
//	}
//	public void addCommand(String c) 
//	{
//		com.add(c);
//	}
//	public LinkedList<String> getCommands()
//	{
//		return com; 
//	}
//	public void setCom(LinkedList<String> com)
//	{
//		this.com = com;
//	}
//	void addCommandListener(CommandListener cl) 
//	{
//		this.commandListener=cl;
//	}
//	public void sendCommand(String c) throws NoCommandException, NoValidCommandException 
//	{
//		parseInstruction(c); 
//		commandEvent ce=new commandEvent();
//		ce.setCommand(command);
//		ce.setParametros(parameters);
//		ce.setParsedInstruction(parsedInstruction); 
//		commandListener.commandEntered(ce); 
//	} 
//	public void receiveCommand(String c) throws NoCommandException, NoValidCommandException
//	{
//		parseInstruction(c); 
//		commandEvent ce=new commandEvent();
//		ce.setCommand(command); 
//		ce.setParametros(parameters); 
//		ce.setParsedInstruction(parsedInstruction); 
//		commandListener.commandReceived(ce); 
//	}
//	public void parseInstruction(String i) throws NoCommandException, NoValidCommandException 
//	{ 
//		parameters=new LinkedList<String>(); 
//		StringTokenizer st=new StringTokenizer(i); 
//		if(st.countTokens() <= 0) 
//		{ 
//			throw new NoCommandException();
//		} 
//		command=st.nextToken().toUpperCase(); 
//		System.out.println(command); 
//		parsedInstruction=command; while(st.hasMoreTokens()) 
//		{ 
//			String t=st.nextToken();
//			parsedInstruction=parsedInstruction+" "+t; parameters.add(t);
//		} 
//		if(!com.contains(command)) 
//		{ 
//			throw new NoValidCommandException(); 
//		}
//	} 
//} 
//
//
//import java.util.LinkedList; 
///** * * @author MeMo */ 
// class commandEvent 
//{
//	private String parsedInstruction; 
//	private String command;
//	private LinkedList<String> parameters; 
//	public String getCommand()
//	{
//		return command;
//	}
//	public void setCommand(String command)
//	{
//		this.command = command;
//	}
//	public LinkedList<String> getParametros() 
//	{
//		return parameters;
//	}
//	public void setParametros(LinkedList<String> parametro)
//	{
//		this.parameters = parametro;
//	} 
//	public void setParsedInstruction(String p) 
//	{ 
//		this.parsedInstruction=p;
//	}
//	public String getParsedInstruction() 
//	{
//		return parsedInstruction;
//	}
//}
//class NoCommandException extends Exception
//{ 
//}
//class NoValidCommandException extends Exception
//{
//} 