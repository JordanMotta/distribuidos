import java.lang.Thread;
import java.io.IOException;
import java.lang.Runnable;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.io.InputStream;

class Server
{
    public final static byte MAIN_SERVER = 1;
    public final static byte SECONDARY_SERVER = 2;
    public final static byte JOIN = 120;
    public final static byte JOIN_RING = 119;
    public final static byte SAVE_FILE = 50;

    private Connection conn;
    private Thread receiveMessageThread;
    private Thread acceptRequestTheard;

    private BasedeDatos db = new BasedeDatos ();

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
                        Socket newConnection = conn.acceptRequest();
                        InputStream in = newConnection.getInputStream();
                        while (in.available () == 0 );

                        List<Byte> msg = Connection.retrieveMessage (in);
                        if (msg.get(0) == SAVE_FILE)
                        {
                            System.out.println ("Salvando un archivo");
                            saveFile (msg);
                            continue;
                        }
                        else if (msg.get(0) == JOIN)
                        {
                            System.out.println ("Un servidor se quiere unir");
                            conn.sendToBackServer(msg);
                        }
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
                            System.out.println ("Back message available");
                            processMessage (conn.receive());
                        }
                        if (conn.isFrontMessageAvailable())
                        {
                            System.out.println ("Front message available");
                            processMessage(conn.frontReceive());
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        receiveMessageThread.start ();
    }

    private void saveFile (List<Byte> msg)
    {
        System.out.println ("msg length: " + msg.size());

        System.out.println ("File Title length: " + msg.get(1).intValue());
        int fileTitleLength = msg.get(1).intValue();

        System.out.println ("File length: " + msg.get(2).intValue());
        int fileLength = msg.get (2).intValue();
        List<Byte> fileTitle = msg.subList(3, 3 + fileTitleLength);
        int fileContentBase = 3 + fileTitleLength;
        List<Byte> fileContent = msg.subList(fileContentBase, fileContentBase + fileLength);
        // int commentBase = fileContentBase + fileLength + 1;
        // List<Byte> comment = msg.subList(commentBase, commentBase + msg.size() - 1);
        // String fileContentText = "";
        // for (Byte fc : fileContent)
        // {
        //     fileContentText += fc.toString ();
        // }
        // System.out.println ("fileContent: " + fileContentText);

        // String fileTitleText = "";

        

        // for (Byte fc : fileTitle)
        // {
        //     fileTitleText += fc.toString();
        // }
        // System.out.println ("fileTitle: " + fileTitleText);

        byte[] fileTitlebytes = new byte[fileTitleLength];
        for (int i = 0; i < fileTitleLength; ++i)
        {
            fileTitlebytes[i] = fileTitle.get(i);
        }

        String fileTitleText = new String (fileTitlebytes, StandardCharsets.UTF_8);
        System.out.println ("fileTitle: " + fileTitleText);


        byte[] fileBytes = new byte[fileLength];
        for (int i = 0; i < fileLength; ++i)
        {
            fileBytes[i] = fileContent.get(i);
        }

        String fileContentText = new String (fileBytes, StandardCharsets.UTF_8);
        System.out.println ("fileTitle: " + fileContentText);
        

        db.Creararchivo("Dios", "same", fileTitleText, fileContentText);
    }

    private void processMessage (final List<Byte> msg)
    {
        System.out.println ("Message received!");
        if (msg.get(0) == Server.JOIN)
        {
            System.out.println ("A server wants to join");
            System.out.println ("Port length: " + (int)msg.get (1));
            System.out.println ("Port: " + Main.bytesToString(msg.subList(2, msg.size())));
        }
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