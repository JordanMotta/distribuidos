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
import java.util.*;
import java.util.logging.*;

class Persona extends Thread {
    protected Socket sk;
    protected DataOutputStream dos;
    protected DataInputStream dis;
    int bytesRead;
    OutputStream fos = null;
    BufferedOutputStream bos = null;
    private int id;
    
    public Persona(int id) {
        this.id = id;
    }
    /**
    * Puerto
    * */
    private final static int PORT = 50010;
    /**
    * Host
    * */
    private final static String SERVER = "localhost";

    
    @Override
    public void run() {
        
        boolean exit=false;//bandera para controlar ciclo del programa
        Socket socket;//Socket para la comunicacion cliente servidor 
        try {
            System.out.println("Client> Welcome to DGit");  
            while( !exit ){//ciclo repetitivo                                
                socket = new Socket(SERVER, PORT);//abre socket                
                //Para leer lo que envie el servidor      
                BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));                
                //para imprimir datos del servidor
                PrintStream output = new PrintStream(socket.getOutputStream());                
                //Para leer lo que escriba el usuario            
                BufferedReader brRequest = new BufferedReader(new InputStreamReader(System.in));            
                System.out.println("Client> Type a command "); 
                //captura comando escrito por el usuario
                String request = brRequest.readLine();   
                String [] arreglo = request.split(" ");
                if (arreglo[0] == "dgit" & arreglo[1] == "pull")
                {
                    //para verificar si es cliente, esto no lo he usado aqui
                    int id = 50;
                    //manda peticion al servidor
                    output.println(request);
                    //captura respuesta e imprime
                    //String st = input.readLine();
                    try {
                //recibo archivo
                //Creamos array de bytes
                byte [] mybytearray  = new byte [66666];
                //Creamos objeto InputStream que abre la cadena de entrada para lectura del fichero que mande servidor
                InputStream cadenaReceptor = socket.getInputStream();
                fos = new FileOutputStream("Donde llega el archivo la ruta");
                bos = new BufferedOutputStream(fos);  //lee donde va a escribir

                bytesRead = cadenaReceptor.read(mybytearray,0,mybytearray.length);

                if(bytesRead == 1){   //1 valor que toma como minimo un archivo Outputstream, significa archivo no encontrado o no buscado 
                    System.out.println("Archivo no encontrado o comando erroneo");
                } else if(bytesRead == -1){ System.out.println("Sesion finalizada"); } //-1 valor que toma al no recibir ningun archivo erroneo ni correcto, solo con BYE

                else { bos.write(mybytearray, 0 , bytesRead);  //buffer escribe en el archivo asignado
                    bos.flush();
                    System.out.println("File " + "ruta completa del archivo "
                        + " downloaded (" + bytesRead + " bytes read)");  }

                }catch(IOException e){ System.out.println("Error de conexion");
                    if (fos != null) fos.close();
                    if (bos != null) bos.close();
                    if (socket != null) socket.close(); }
                    
                }
                else if (arreglo[0] == "dgit" & arreglo[1] == "push" & arreglo[3] == "-m")
                {
                    String archivo = arreglo[4].replace("[","").replace("]","");
                    String comentario = arreglo[4].replace("[","").replace("]","").replace("'","");
                    FileInputStream archivo2 = new FileInputStream(archivo);
                    //INCOMPLETO PARA QUE NO SE JODA CORRIENDO 

                }
                
                //FileInputStream fileinputstream = new FileInputStream(file);

                String st = input.readLine();
                if( st != null ) System.out.println("Server> " + st );    
                if(request.equals("exit")){//terminar aplicacion
                    exit=true;                  
                    System.out.println("Client> Your session has ended");    
                }  
                socket.close();
            }//end while                                    
            
        } catch (IOException ex) {
            System.err.println("Client> " + ex.getMessage());   
            Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
public class Client {

    public static void main(String[] args) {
        ArrayList<Thread> clients = new ArrayList<Thread>();
        for (int i = 0; i < 1; i++) {
            clients.add(new Persona(i));
        }
        for (Thread thread : clients) {
            thread.start();
        }
    }
}
