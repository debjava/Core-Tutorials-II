import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class Client 
{
	private Socket clientSocket = null;
	
	public Client()
	{
		super();
		connectToServer();
	}
	
	private void connectToServer()
	{
		try 
		{
			clientSocket = new Socket("localhost",1234);
		}
		catch( IOException ie )
		{
			ie.printStackTrace();
		}
	}
	
	public void sendMessage()
	{
		try
		{
			OutputStream os = clientSocket.getOutputStream();
			os.write("Hello World dvsdd".getBytes());
			
//			PrintWriter out = new PrintWriter
//			  (clientSocket.getOutputStream(), true);
//			out.print("scsafasas");
//			out.flush();
//			out.close();
			clientSocket.close();
			
		}
		catch( NullPointerException npe )
		{
			npe.printStackTrace();
		}
		catch (IOException ie) 
		{
			ie.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		Client client = new Client();
		client.sendMessage();
	}
}
