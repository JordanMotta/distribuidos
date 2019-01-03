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

public final class XmlHash {
       Document document;
       String ruta="\\Hash.xml";
       File file= new File (ruta );
       int longitud = 100;
       String [] listahash = new  String [longitud];
       int  contador =1 ; 
       
    public XmlHash(String ruta){
       try {
          this.ruta = ruta+ this.ruta;
          if ( new File (this.ruta).exists ()){
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(new File (this.ruta));
          }
          else {
                DocumentBuilderFactory factoria =DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factoria.newDocumentBuilder();
                DOMImplementation implementation = builder.getDOMImplementation();
                document = implementation.createDocument(null, "xml", null);
                document.setXmlVersion("1.0");
          }  
          Obtener();
        } catch(Exception e){
            e.printStackTrace();}  
       } 
    public String Crear (  String vFecha,String vUsuario){
        if (contador <= longitud ){
            String Indice_Arreglo= funcion_hash();
            Element Codigo =document.createElement("Codigo");
            Codigo.setAttribute("codigo", Indice_Arreglo);
            
            Element Nombre =document.createElement("Nombre");
            Text texto =document.createTextNode(vUsuario);  
            Nombre.appendChild(texto);
            
            Element Fecha = document.createElement("Fecha"); 
            texto =document.createTextNode(vFecha);
            Fecha.appendChild(texto);

            Codigo.appendChild(Nombre);
            Codigo.appendChild(Fecha);

            document.getDocumentElement().appendChild(Codigo);

            generar_documento();
            Obtener();

        return Indice_Arreglo;
        }else {
        System.out.println( "llego a la max capacidad de hash ");
        return "null";
        }
    }
    private void generar_documento (){
        try {
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer();
                Source souce =new DOMSource(document);
                FileWriter fw = new FileWriter(new File (this.ruta));
                
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
        NodeList nodoRaiz = document.getDocumentElement().getElementsByTagName("Codigo");
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
    public String funcion_hash(){
  int Indice_Arreglo=0;
        Boolean bo=false;
        Obtener();
        if (new File (this.ruta).exists()){
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
    
             
           } else {
                 bo=true;
             }
        }
        } else {
             Indice_Arreglo = 0;
                            
        }
        return  Integer.toString(Indice_Arreglo) ;
    }
    public void Obtener (){
      NodeList nodoRaiz = document.getDocumentElement().getElementsByTagName("Codigo");
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
        NodeList nodoRaiz = document.getDocumentElement().getElementsByTagName("Codigo");
        for (int temp = 0; temp < nodoRaiz.getLength(); temp++) {
         Node nodo = nodoRaiz.item(temp);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodo;
                if (Codigo.equals(element.getElementsByTagName(Tag).item(0).getTextContent())){
                    con++;
                }
            }
        }
        String [] resultado= new  String [3*con];
        con=0;
        for (int temp = 0; temp < nodoRaiz.getLength(); temp++) {
        Node nodo = nodoRaiz.item(temp);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodo;
                if (Codigo.equals(element.getElementsByTagName(Tag).item(0).getTextContent())){
                     resultado[con]=element.getAttribute("codigo");
                     resultado[con+1]=element.getElementsByTagName("Nombre").item(0).getTextContent();
                     resultado[con+2]=element.getElementsByTagName("Fecha").item(0).getTextContent();
                     con=con+3;
                    }

            }
            contador ++;
        }
        return resultado;
    }
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
}
