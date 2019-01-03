import java.net.Socket;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.InputStream;

abstract class Connection
{

    public final static int CONNECT_TO = 100;

    protected Socket socket;
    protected ServerSocket serverSocket;
    protected String[] range;
    protected ArrayList<Socket> servers;
    protected Thread acceptRequestTheard;
    protected Thread messagesListenerTheard;
    protected boolean firstConnection = false;
    protected Server server;

    /**
     * Ring model.
     */
    protected Socket frontServer;
    protected Socket backServer;

    public Connection (final String[] range)
    {
        this.range = range;
        servers = new ArrayList<>();
        try
        {
            // this.socket = new Socket ();
            this.serverSocket = new ServerSocket();
        }
        catch (IOException e)
        {
            
        }
    }

    public Connection (final String[] range, Server server)
    {
        this(range);
        this.server = server;
    }

    /**
     * The server will try to connect to another server with the given range.
     * If there is no server available, that means this is the first server and
     * it will be the main one.
     */
    abstract public void connect ();

    abstract public void send (final byte[] message);
    abstract public byte[] receive ();

    public void close ()
    {
        try
        {
            serverSocket.close ();
            socket.close();
        }
        catch (Exception e)
        {

        }
    }

    public boolean isFirstConnection ()
    {
        return firstConnection;
    }

    public void acceptRequest () throws IOException
    {
        Socket anotherServer = serverSocket.accept ();

        //Wait until the server expose his reasons.
        InputStream in = anotherServer.getInputStream();
        while (in.available() == 0);

        
    }

    public boolean isMessageAvailable () throws IOException
    {
        return backServer.getInputStream().available() != 0;
    }

    protected void runFirstServer ()
    {
        
    }

    protected void printConnection (final Socket s)
    {
        System.out.println ("--------------------Connection request from---------------");
        System.out.println ("ip: " + s.getInetAddress().getHostAddress());
        System.out.println ("port: " + s.getLocalPort());
    }

    protected void printConnections ()
    {
        System.out.println ("Front server ip: " + frontServer.getInetAddress().getHostAddress());
        System.out.println ("Front server port: " + frontServer.getPort());
        System.out.println ("Back server ip: " + backServer.getInetAddress().getHostAddress());
        System.out.println ("Back server port: " + backServer.getLocalPort());
    }
}


