//dgit debe aceptar los parametros de push y pull con sus
//respectivos parametros
//pull: dgit pull DONE
//push: dgit push [archivos] -m '[comentario]' Falta

import java.io.*;
import java.net.Socket;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.*;

public class Client
{
    public static void main(String[] args) throws IOException {

        int bytesRead;
        OutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket socketCliente = null;

        PrintWriter salida = null;

        String hostName = InetAddress.getLocalHost().getHostName();
        //socket.setSoTimeout( 2000 );
        //socket.setKeepAlive( true );
         
        // Creamos un socket en el lado cliente
        //Se conecta a partir del puerto 3000

        try{ socketCliente = new Socket(hostName, 50010);
            System.out.println("servidor conectado:" + hostName);

            //Obtenemos el canal de salida
            salida = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socketCliente.getOutputStream())),true);
        }catch(IOException e){
            System.err.println("No puede establecer conexion");
            System.exit(-1); }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String linea = "";

        // Para el caso del pull, se debe colocar dgit pull 
        while(!linea.equalsIgnoreCase("bye")){   

            do{
                System.out.println("Ingresa comando Dgit:");
                //Leo la entrada del usuario
                linea = stdIn.readLine();
            }while (!linea.matches("[a-z][a-z][a-z] \".*\"") && !linea.equalsIgnoreCase("bye"));
            //La envia al servidor
            salida.println(linea); 

            try {
                //recibo archivo
                //Creamos array de bytes
                byte [] mybytearray  = new byte [66666];
                //Creamos objeto InputStream que abre la cadena de entrada para lectura del fichero que mande servidor
                InputStream cadenaReceptor = socketCliente.getInputStream();
                fos = new FileOutputStream("Donde llega el archivo la ruta");
                bos = new BufferedOutputStream(fos);  //lee donde va a escribir

                bytesRead = cadenaReceptor.read(mybytearray,0,mybytearray.length);


                if(bytesRead == 1){   //1 valor que toma como mínimo un archivo Outputstream, significa archivo no encontrado o no buscado 
                    System.out.println("Archivo no encontrado o comando erroneo");
                } else if(bytesRead == -1){ System.out.println("Sesion finalizada"); } //-1 valor que toma al no recibir ningun archivo erroneo ni correcto, solo con BYE

                else { bos.write(mybytearray, 0 , bytesRead);  //buffer escribe en el archivo asignado
                    bos.flush();
                    System.out.println("File " + "ruta completa del archivo "
                        + " downloaded (" + bytesRead + " bytes read)");  }

            }catch(IOException e){ System.out.println("Error de conexion");
                if (fos != null) fos.close();
                if (bos != null) bos.close();
                if (socketCliente != null) socketCliente.close(); }
        }

        if (fos != null) fos.close();
        if (bos != null) bos.close();
        if (socketCliente != null) socketCliente.close(); 

    }
}