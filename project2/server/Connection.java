import java.net.Socket;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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
    protected Socket client;

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
    abstract public List<Byte> receive () throws IOException;

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

    public String createMessageToJoin ()
    {
        String msg;
        String port = String.valueOf(frontServer.getLocalPort());
        msg = String.valueOf(Server.JOIN) + port.length() + port;
        return msg;
    }

    public void sendToBackServer (List<Byte> msg)
    {

    }

    public boolean isFirstConnection ()
    {
        return firstConnection;
    }

    public Socket acceptRequest () throws IOException
    {
        System.out.println ("Listening...");
        Socket newConnection = serverSocket.accept ();
        System.out.println ("A new connection");

        return newConnection;
    }

    private void newServerConnection (Socket newServer)
    {
        /**
         * 
         */
        String message = 
            String.valueOf(Server.JOIN) + 
            String.valueOf(newServer.getLocalPort()).length() + 
            String.valueOf(newServer.getLocalPort());

        System.out.println ("Sending message: " + message);
        
        OutputStream out;

        try 
        {
            out = backServer.getOutputStream();
            out.write(message.getBytes());
            out.flush();

            backServer.close();
            backServer = newServer;
        } 
        catch (Exception e) 
        {
        }
        
    }

    public boolean isMessageAvailable () throws IOException
    {
        return backServer.getInputStream().available() != 0;
    }

    protected void runFirstServer ()
    {
        
    }

    public boolean isFrontMessageAvailable () throws IOException
    {
        return frontServer.getInputStream ().available () != 0;
    }

    public List<Byte> frontReceive () throws IOException
    {
        return retrieveMessage(frontServer.getInputStream());
    }

    protected static List<Byte> retrieveMessage (final InputStream in) throws IOException
    {
        ArrayList<Byte> msg = new ArrayList<>();
        while (true)
        {
            int n = in.read ();
            if (n == -1) break;
            msg.add (new Byte((byte)n));
        }
        return msg;
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


