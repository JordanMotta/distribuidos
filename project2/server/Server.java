import java.lang.Thread;
import java.io.IOException;
import java.lang.Runnable;
import java.net.ServerSocket;
import java.net.Socket;

class Server
{
    public final static byte MAIN_SERVER = 1;
    public final static byte SECONDARY_SERVER = 2;
    public final static byte JOIN = 120;

    private Connection conn;
    private Thread receiveMessageThread;
    private Thread acceptRequestTheard;

    public Server ()
    {

    }

    public Server (final String cType, final String[] range)
    {
        if (cType == Main.P_PARAM)
        {
            System.out.println ("Server with ports");
            conn = new CPort (range);
        }
        else if (cType == Main.I_PARAM)
        {
            conn = new CIP (range);
        }

        conn.connect();
        run ();
    }

    
    public void run ()
    {
        acceptRequestTheard = new Thread(new Runnable(){
        
            @Override
            public void run() {
                while (!Thread.interrupted())
                {
                    try
                    {
                        conn.acceptRequest();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        acceptRequestTheard.start ();

        receiveMessageThread = new Thread(new Runnable(){
        
            @Override
            public void run() {
                while (!Thread.interrupted())
                {
                    try
                    {
                        if (conn.isMessageAvailable())
                        {
                            processMessage ();
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void processMessage ()
    {

    }

    public void close ()
    {
        System.out.println ("Closing DGit server");
        conn.close ();
    }



    // public static class State
    // {
    //     public State ()
    //     {

    //     }

    //     public void process ()
    //     {

    //     }

    //     private void join ()
    //     {

    //     }

        
    // }


}