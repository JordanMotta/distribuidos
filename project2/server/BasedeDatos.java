import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author frank
 */
public class BasedeDatos {
    String url = ".\\";
    /* maestro = nombre de la persona que creo ese archivo 
       nombre = nombre que esta actualizando el archivo 
       archivo = nombre del archivo 
       texto = es la informacion que tiene ese archivo
    */
        /*
    metodo que sirve para crear archivos este tiene un usuario maestro que es responsable de crear por primera vez el archivo 
    el nombre es la persona que esta actualizando o creando el archivo , el archivo es el nombre del archivo y el texto es el
    contenido de este.
    */
    public String Creararchivo(String Maestro , String nombre, String archivo , String texto){
        
        String respuesta = "hola";
        archivos archivos =new archivos();
        Xml xml =new Xml();
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String hora = hourdateFormat.format(date);
        // verifica si hay un maestro con el mismo nombre si existe entonces este toma el hash que se le dio si no existe 
        // entonces este se le asigna un hash 
        String hash = xml.Verificar("Maestro", Maestro);
        // si es null entonces significa que no existe el maestro en el archivo proyecto.xml
        if ("null".equals(hash)){
            // funcion que crear el archivo y las carpeta con sus hash respectivamente y guarda el archivo con su contenido
            funcion(hora,archivo,xml,Maestro,archivos,nombre,texto);
            // si este exite entonces se busca si existe el archivo en el archivo proyecto.xml con el maestro 
        }else {
            String tem = xml.Verificar("archivo", archivo);
            if ("null".equals(tem) ){
                // si no existe el archivo se procede a crear la carpeta con su hash y guardar el archivo con su contenido
                funcion(hora,archivo,xml,Maestro,archivos,nombre,texto);
            }else {
                // si este existe entonces se porcede a buscar la carpeta que contiene el archivo que se va modificar 
                // se actualiza el archivo hash.xml (actualizacion) y se guarda el archivo con su contenido 
                String ruta =url+"\\"+tem;
                XmlHash xmlhash = new XmlHash(ruta);
                hash= xmlhash.Crear(hourdateFormat.format(date), nombre);
                archivos.CrearArchivo(ruta+"\\"+archivo+"_"+hash, texto);
            }

        }
        return respuesta;
    }
     //  esta funcion es el proceso de almacenar el archivo con su contenido este crea  o actualiza el proyecto.xml 
    // luego crea el fichero con el hash para luego este cree el archivo con su contenido y lo guarde en el fichero
    // ya  antes mencionado 
    private void funcion (String hora , String archivo, Xml xml , String Maestro , archivos archivos , String nombre ,String texto){
                String hash= xml.Crear( archivo, hora,Maestro);
                archivos.CrearFichero(url+"\\"+hash);
                String ruta =url+"\\"+hash;
                XmlHash xmlhash = new XmlHash(ruta);
                hash= xmlhash.Crear(hora, nombre);
                archivos.CrearArchivo(ruta+"\\"+archivo+"_"+hash , texto);
            }
    /*
      es una mostrar el nombre del archivo con el maestro y  las personas que modificaron dicho archivo 
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
