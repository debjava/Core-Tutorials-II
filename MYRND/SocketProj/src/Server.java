import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Server 
{
	private int serverPort = 1234;
	private ServerSocket serverSocket = null;
	private Socket listenerSocket = null;
	private boolean listenerFlag = false;

	public Server()
	{
		super();
		createServer();
	}

	private void createServer()
	{
		try
		{
			serverSocket = new ServerSocket( serverPort );
			listenerSocket = serverSocket.accept();
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

	public void listen()
	{
		try
		{
			BufferedReader br = new BufferedReader( new InputStreamReader(listenerSocket.getInputStream()));
			while( !listenerFlag )
			{
				String line = br.readLine();
//				System.out.println(line);
				if( line != null )
				{
					System.out.println(line);
//					listenerFlag = true;
//					break;
				}
				else
				{
					
				}
					
			}
//			listenerSocket.close();
//			serverSocket.close();
		}
		catch( IOException ie )
		{
			ie.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) 
	{
		Server server = new Server();
		server.listen();
		
	}

}
