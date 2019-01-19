import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
public class Xml {
    private Document document;
    String ruta="..\\proyecto.xml";
    File file= new File (ruta);
    int longitud = 100;
    String [] listahash = new  String [longitud];
    int  contador =1 ; 
        // metodo que crear el archivo proyecto.xml y si en tal caso existe este toma la informacion de dicho archivo
    public Xml(){
       try {

          if (file.exists ()){
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(file);
       
          }
          else {
                DocumentBuilderFactory factoria =DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factoria.newDocumentBuilder();
                DOMImplementation implementation = builder.getDOMImplementation();
                document = implementation.createDocument(null, "xml", null);
                document.setXmlVersion("1.0");
          } 
       }catch(Exception e){
            e.printStackTrace();
       }
    }
            //formato de xml primero un codigo que seria el hash que se le asigno 
    // el nombre del archivo
    // el maestro que  creo el archivo 
    // y la fecha del momento que se creo 
    public String  Crear (String Nombre , String Fecha , String Maestro){
        if (contador <= longitud ){
        String Indice_Arreglo= funcion_hash();
        Element Hash =document.createElement("Hash");
        Hash.setAttribute("codigo", Indice_Arreglo); 
 
        Element nombre_arc =document.createElement("archivo");
        Text texto =document.createTextNode(Nombre);  
        nombre_arc.appendChild(texto);
        
        Element maestro = document.createElement("Maestro");
        texto =document.createTextNode(Maestro); 
        maestro.appendChild(texto);
        
        Element fecha_cre = document.createElement("fecha");
        texto =document.createTextNode(Fecha); 
        fecha_cre.appendChild(texto);
        
        Hash.appendChild(maestro);       
        Hash.appendChild(nombre_arc);
        Hash.appendChild(fecha_cre);
        
        document.getDocumentElement().appendChild(Hash);
        
        generar_documento();

        return Indice_Arreglo;
        }else {
        System.out.println( "llego a la max capacidad de hash ");
        return "null";
        }
    }
            // metodo que guarda la informacion de archivo hash.xml
    public void generar_documento (){
        try {
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer();
                Source souce =new DOMSource(document);;
                FileWriter fw = new FileWriter(new File (ruta));
                PrintWriter pw= new PrintWriter(fw);
                Result result = new StreamResult (pw);
                transformer.transform(souce, result);
        }catch(Exception e){
            e.printStackTrace();


       }
    }
    public Node  Buscar(String Tag , String Elemento){
        Node resultado;
        resultado = null;
        NodeList nodoRaiz = document.getDocumentElement().getElementsByTagName("Hash");
          for (int temp = 0; temp < nodoRaiz.getLength(); temp++) {
                Node nodo = nodoRaiz.item(temp);
                        if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) nodo;
                            if (Elemento.equals(element.getElementsByTagName(Tag).item(0).getTextContent())){
                                resultado=nodo;
                            }
                        }
          }
          return resultado;
       }
           // busca en el archivo proyecto.xml si existe el elemento  expecificamente el nombre del maestro o 
    //el nombre del archivo
    public String Verificar (String Tag , String Elemento){
        String resultado ;
        Node nodo =Buscar (Tag,Elemento);
        if (nodo== null){
            resultado ="null";
        }
        else {
            Element element = (Element) nodo;
            resultado = element.getAttribute("codigo");
        }
        return resultado;
        
    }
    
        // funcion hash que hace es asignar un hash unico
    public String funcion_hash(){
        int Indice_Arreglo=0;
        Boolean bo=false;
        Obtener();
        if (file.exists()){
        int  numero = (int) (Math.random() * 96) ;
        Indice_Arreglo =97%numero;
        while (bo==false){
             for (int temp = 1; temp < contador ; temp ++) {
                 String resultado= listahash[temp];
                if ( resultado.equals(Integer.toString(Indice_Arreglo)))
                    {
                        bo=true;
                    }
            }
             if (bo == true) {
                 bo= false ;
                 Indice_Arreglo++;
                 System.out.print("aumentando contador de hash de xml");
             
             } else {
                 bo=true;
             }
        }
        } else {
             Indice_Arreglo = 0;
        }
        return  Integer.toString(Indice_Arreglo) ;
    }
        // obtiene todos los hash que se asignaron para que este no se repita 
    public void Obtener (){
        
      NodeList nodoRaiz = document.getDocumentElement().getElementsByTagName("Hash");
        contador =0;
        for (int temp = 0; temp < nodoRaiz.getLength(); temp++) {
            Node nodo = nodoRaiz.item(temp);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodo;
                listahash[temp]= element.getAttribute("codigo");  
            }
            contador ++;
        }
    }
    public String [] Mostrar(String Codigo, String Tag){
        int con=0;
        NodeList nodoRaiz = document.getDocumentElement().getElementsByTagName("Hash");
        for (int temp = 0; temp < nodoRaiz.getLength(); temp++) {
         Node nodo = nodoRaiz.item(temp);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodo;
                if (Codigo.equals(element.getElementsByTagName(Tag).item(0).getTextContent())){
                    con++;
                }
            }
        }
        String [] resultado= new  String [4*con];
        con=0;
        for (int temp = 0; temp < nodoRaiz.getLength(); temp++) {
        Node nodo = nodoRaiz.item(temp);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodo;
                if (Codigo.equals(element.getElementsByTagName(Tag).item(0).getTextContent())){
                     resultado[con]=element.getAttribute("codigo");
                     resultado[con+1]=element.getElementsByTagName("Maestro").item(0).getTextContent();
                     resultado[con+2]=element.getElementsByTagName("archivo").item(0).getTextContent();
                     resultado[con+3]=element.getElementsByTagName("fecha").item(0).getTextContent();
                     con=con+4;
                    }

            }
            contador ++;
        }
        return resultado;
    }
    
}
