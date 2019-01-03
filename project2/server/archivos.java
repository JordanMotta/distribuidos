package proyecto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class archivos {
    public void CrearFichero(String carpeta){
        File folder = new File(carpeta);
        folder.mkdirs();
    }
    public void CrearArchivo(String ruta , String texto){
         ruta = ruta+".txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        try {
        if(archivo.exists()) {
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(texto);
        } else {
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(texto);
        }
        bw.close();
        }catch(IOException e){
            System.out.print("error en crear el archivo");
        }    
    }
    public void LeerArchivo(String ruta ){
        try {
            String cadena;
            FileReader f = new FileReader(ruta+".txt");
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                System.out.println(cadena);
        }
        b.close();
        }catch(IOException e){
            System.out.print("no se encontro en el documento ");
        }  
    
    }
}
