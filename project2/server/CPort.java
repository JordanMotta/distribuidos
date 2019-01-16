import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import java.net.SocketException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

class CPort extends Connection
{
    private ArrayList<String> usedPorts = new ArrayList<>();
    /**
     * ports -> 3000 - 3100
     */
    public CPort (final String[] range)
    {
        super(range);
    }

    @Override
    public void connect ()
    {

        InetSocketAddress sa;
        List<String> availablePorts = findPorts();

        if (availablePorts.size() == range.length) // There is no server yet.
        {
            System.out.println ("I am the first server!");
            firstConnection = true;
            join (availablePorts.get(Math.abs(new Random().nextInt() % availablePorts.size())));
        }
        else if (availablePorts.size() == 0) // There is no space for another server.
        {
            System.out.println ("I cannot join to the party");
        }
        else // There are some ports to use.
        {
            System.out.println ("I am not the first server :(");
            join (availablePorts.get(Math.abs(new Random().nextInt()) % availablePorts.size()));
            
        }
    }

    /**
     * Return available port for this server.
     * 
     * @return List<String>
     */
    private List<String> findPorts ()
    {
        ArrayList<String> ports = new ArrayList<>();
        for (String p : range)
        {
            try
            {
                ServerSocket s = new ServerSocket (Integer.parseInt(p));
                s.close();
                ports.add(p);
            }
            catch (IOException e)
            {
                this.usedPorts.add (p);
                continue;
            }
        }

        return ports;
    }

    private void join (final String port)
    {
        final InetSocketAddress sa = new InetSocketAddress ("localhost", Integer.parseInt(port));
        try
        {
            //Create the server.
            serverSocket.bind(sa);
            if (firstConnection)
            {
                new Thread(new Runnable(){
                
                    @Override
                    public void run() {
                        try
                        {
                            backServer = serverSocket.accept();
                            printConnection(backServer);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start ();

                frontServer = new Socket ("localhost", Integer.parseInt (port));
                printConnections();

            }
            else
            {
                //Ask to join to one server.
                System.out.println ("Joining to server " + port);
                frontServer = new Socket ("localhost", Integer.parseInt (
                    usedPorts.get( 
                        Math.abs (
                            new Random().nextInt()) % usedPorts.size ())));

                System.out.println ("Joined to server " + port);
                
                // ArrayList<Byte> message = new ArrayList<>();
                // message.add(new Byte(Server.JOIN));
                // message.addAll(
                //     Arrays.asList(
                //         BigInteger.valueOf(
                //             socket.getLocalPort()).toByteArray()));
                

                // String strMessage = String.valueOf(Server.JOIN) + String.valueOf(socket.getLocalPort());
                
                
                // OutputStream out = socket.getOutputStream();
                // out.write(strMessage.getBytes());
                // out.flush();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace ();
        }
    }

    // private void proccessData (Socket s, InputStream in)
    // {

    // }

    @Override
    public void send (final byte[] message)
    {

    }

    @Override
    public List<Byte> receive () throws IOException
    {
        try
        {
            return retrieveMessage(backServer.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close ()
    {
        super.close();
        messagesListenerTheard.interrupt();
        acceptRequestTheard.interrupt();
    }
}