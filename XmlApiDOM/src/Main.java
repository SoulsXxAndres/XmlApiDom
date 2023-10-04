//Hecho por Andrés López Corrales
import org.w3c.dom.Document; //API DOM
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        File archivo = new File("src\\sales.xml"); //archivo XML Sales

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivo); //Dentro de API DOM - para utilizar el archivo Sales

            Scanner scanner = new Scanner(System.in); // para scanner percentage
            Scanner scanner2 = new Scanner(System.in); //para scanner departamento

            boolean seguir = false;// para do-while de scanner percentage

            Document nuevoDocNewSales = dBuilder.newDocument(); //nuevo documento new_sales.xml

            do {
                System.out.print("Escribe un valor de porcentaje entre el 5 y el 15: "); //scanner percentage
                double percentageScanner = scanner.nextDouble();

                if (percentageScanner < 5 || percentageScanner > 15) { //validación percentage entre 5 y 15
                    System.out.println("Escribe un numero entre 5 y 15");
                    seguir = true;
                }else {
                    seguir=false;

                    System.out.print("Escriba el departamento al que le quiera agregar el porcentaje a las ventas: ");
                    String depa = scanner2.nextLine(); //scanner departamento

                    double percentage = percentageScanner * (.01); //multiplicar scannerPercentage * .01

                    NodeList nodeList = doc.getElementsByTagName("sale_record"); //Nodo de sale_record

                    Element elementoRaiz = nuevoDocNewSales.createElement("sales_records");
                    nuevoDocNewSales.appendChild(elementoRaiz);

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node nodo = nodeList.item(i);

                        if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                            Element elemento = (Element) nodo;
                            String departmentValue = elemento.getElementsByTagName("department").item(0).getTextContent();
                            Node nuevoNodo = nuevoDocNewSales.importNode(nodo, true); //copiar todo a new_sales.xml
                            elementoRaiz.appendChild(nuevoNodo);

                            // Compara el valor del elemento "department" con depa
                            if (departmentValue.equals(depa)) {
                                String salesValue = elemento.getElementsByTagName("sales").item(0).getTextContent();
                                double salesValuesDouble = Double.parseDouble(salesValue);
                                double valoresSalesConPercentage = ((salesValuesDouble) * percentage);
                                double valoresFinalesYaAgregados = valoresSalesConPercentage +salesValuesDouble;
                                System.out.println("Sales de " + depa + " " + (i + 1) + " con porcentaje agregado: " + valoresFinalesYaAgregados);
                                //Actualizar la información del departamento señalado en new_sales.xml
                                Element elementoNuevasVentas = (Element) nuevoNodo;
                                NodeList listaNodoNuevasVentas = elementoNuevasVentas.getElementsByTagName("sales");
                                if (listaNodoNuevasVentas.getLength() > 0) {
                                    Node nodoNuevasVentas = listaNodoNuevasVentas.item(0);
                                    nodoNuevasVentas.setTextContent(String.valueOf(valoresFinalesYaAgregados)); //actualizar nomas lo que contiene la etiqueta ventas
                                }
                            }
                        }
                    }
                }

                // Guardar y poner toda la info actualizada y vieja en el new_sales.xml
                if (!seguir){
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                    DOMSource source = new DOMSource(nuevoDocNewSales);
                    StreamResult result = new StreamResult(new File("src\\new_sales.xml"));
                    transformer.transform(source, result);
                    System.out.println("new_sales.xml actualizado");
                }

            }while (seguir) ;

        } catch(Exception e) {
            e.printStackTrace();
        }

    }//end main
}