import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author frank
 */
public class BasedeDatos {
    String url = "..\\";
    /* maestro = nombre de la persona que creo ese archivo 
       nombre = nombre que esta actualizando el archivo 
       archivo = nombre del archivo 
       texto = es la informacion que tiene ese archivo
    */
    public String Creararchivo(String Maestro , String nombre, String archivo , String texto){
        
        String respuesta = "hola";
        archivos archivos =new archivos();
        Xml xml =new Xml();
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String hora = hourdateFormat.format(date);
        String hash = xml.Verificar("Maestro", Maestro);
        if ("null".equals(hash)){
            funcion(hora,archivo,xml,Maestro,archivos,nombre,texto);
        }else {
            String tem = xml.Verificar("archivo", archivo);
            if ("null".equals(tem) ){
                funcion(hora,archivo,xml,Maestro,archivos,nombre,texto);
            }else {
                String ruta =url+"\\"+tem;
                XmlHash xmlhash = new XmlHash(ruta);
                hash= xmlhash.Crear(hourdateFormat.format(date), nombre);
                archivos.CrearArchivo(ruta+"\\"+archivo+"_"+hash, texto);
            }

        }
        return respuesta;
    }
    private void funcion (String hora , String archivo, Xml xml , String Maestro , archivos archivos , String nombre ,String texto){
                String hash= xml.Crear( archivo, hora,Maestro);
                archivos.CrearFichero(url+"\\"+hash);
                String ruta =url+"\\"+hash;
                XmlHash xmlhash = new XmlHash(ruta);
                hash= xmlhash.Crear(hora, nombre);
                archivos.CrearArchivo(ruta+"\\"+archivo+"_"+hash , texto);
            }
    /*
      
    */
    public void Mostrar_por_nombre (String Maestro, String nombre ){
         Xml xml =new Xml();
        String[] temporal = xml.Mostrar(Maestro, "Maestro");
        int i=temporal.length ;
        int tem=0;
        for (int f=0 ; f <i/4;f++){
        System.out.println(temporal[tem + 1 ]+ " maestro"  );
        System.out.println(temporal[tem + 2 ]+ " archivo"  );
        System.out.println("------------------------------------------------------------");
        XmlHash xmlhash= new XmlHash(url+ temporal[tem]);
        tem=f+4;
        String[] resultado= xmlhash.Mostrar(nombre, "Nombre");
        for (String resultado1 : resultado) {
            System.out.println(resultado1);
        
        }
        }
    }
}
