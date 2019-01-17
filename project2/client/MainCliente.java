//dgit debe aceptar los parametros de push y pull con sus
//respectivos parametros
//pull: dgit pull DONE
//push: dgit push [archivos] -m '[comentario]'
package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.io.*;

class MainCliente {
    
    private static Socket sk;
    private static DataOutputStream dos;
    private static DataInputStream dis;
    private static BufferedInputStream bis = null;
    private static final BufferedOutputStream bos = null;
    private static InputStream is;
    int bytesRead;
    private static OutputStream fos = null;
    int id;
    private final static int PORT = 50010;
    private final static String SERVER = "192.168.2.2";

 public static void main(String[] args) throws InterruptedException {
        String server = "localhost";
        int port = PORT;

        if (args.length >= 1) {
            server = args[0];
        }
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        }

        new MainCliente(server, port);
    }

    public MainCliente(String server, int port)
    {
        
        boolean exit=false;//bandera para controlar ciclo del programa
        try {
            System.out.println("Client> Welcome to DGit");  
            System.out.println("Client> help for Help Menu");
            while( !exit ){//ciclo repetitivo                                
                sk= new Socket(server, port);//abre socket                
                //Para leer lo que envie el servidor      
                BufferedReader input = new BufferedReader( new InputStreamReader(sk.getInputStream()));                
                //para imprimir datos del servidor
                PrintStream output = new PrintStream(sk.getOutputStream());                
                //Para leer lo que escriba el usuario            
                BufferedReader brRequest = new BufferedReader(new InputStreamReader(System.in));            
                System.out.println("Client> Type a command "); 
                //captura comando escrito por el usuario
                String request = brRequest.readLine();   
                String [] arreglo = request.split(" ");
                if (request.equals("Help"))
                {
                    System.out.println("Client> Dgit Help Menu");
                    System.out.println("Client> for pull request: dgit pull");
                    System.out.println("Client> for push request: dgit push [archivos] -m '[comentario]'");
                     System.out.println("Client> for close conecction: exit");
                }
                
                if ("dgit".equals(arreglo[0]) & "pull".equals(arreglo[1]))
                    {
                        try 
                        {
                            File file = new File(request.substring(4));
                            is = sk.getInputStream();
                            fos = new FileOutputStream(file);

                            byte[] buffer = new byte[sk.getReceiveBufferSize()];
                            int bytesReceived = 0;
                            while ((bytesReceived = is.read(buffer)) >=0) 
                            {
                                fos.write(buffer, 0, bytesReceived);
                            }
                            request = "";
                            fos.close();
                            is.close();
                        }catch (IOException ex) 
                        {
                            System.err.println("Client> " + ex.getMessage());   
                            //Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
                         }       
                    
                    }
                else if ("dgit".equals(arreglo[0]) & "push".equals(arreglo[1]) & "-m".equals(arreglo[3]))
                    {
                        try 
                        {
                        String archivo = arreglo[4].replace("[","").replace("]","");
                        String comentario = arreglo[4].replace("[","").replace("]","").replace("'","");
                        //FileInputStream archivo2 = new FileInputStream(archivo);
                        
                        //Send file
                        File myFile = new File(archivo);
                        byte[] mybytearray = new byte[(int) myFile.length()];
         
                        FileInputStream fis = new FileInputStream(myFile);
                        bis = new BufferedInputStream(fis);
                        //bis.read(mybytearray, 0, mybytearray.length);
                        dis = new DataInputStream(bis); 
                        dis.readFully(mybytearray, 0, mybytearray.length);
         
                        OutputStream os = sk.getOutputStream();
         
                        //Sending data to the server
                        dos.write(50); // envio identificador numero 50 
                        dos.writeLong(myFile.getName().length()); //envio tamano del archivo
                        dos.writeLong(mybytearray.length);   //envio longitud del archivo en bytes 
                        dos.writeUTF(myFile.getName()); //envio nombre del archivo
                        dos.write(mybytearray, 0, mybytearray.length); // envio el archivo completo desde el byte 0 hasta el final
                        dos.writeChars(comentario); //envio comentario
                       
                        System.out.println("Client> Commit Accepted");
                        output.println(request);
                        dos.flush();
                        dos.close();
                        os.close();  
                        sk.close();
                        }catch (IOException ex) 
                                {
                                 System.err.println("Client> " + ex.getMessage());   
                                }

                    }
                
                        //FileInputStream fileinputstream = new FileInputStream(file);

                String st = input.readLine();
                if( st != null ) System.out.println("Server> " + st );    
                if(request.equals("exit")){//terminar aplicacion
                    exit=true;                  
                    System.out.println("Client> Your session has ended");    
                }  
                sk.close();
                dos.flush();
                dos.close();  
                sk.close();
            }//end while                                    
            
        } catch (IOException ex) {
            System.err.println("Client> " + ex.getMessage());   
            //Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


