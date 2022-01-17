import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String fileName2 = "data.xml";
        List<Employee> list = parseCSV(columnMapping, fileName);
        listToJson(list);
        //parseXML(fileName2);
        //List<Employee> list2 = parseXML(fileName2);
        //writeString(list);
    }

    //Читаем csv файл
    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = new ArrayList<>();
        fileName = "data.csv";
        try (CSVReader csvr = new CSVReader(new FileReader(fileName))) {

            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csvtb = new CsvToBeanBuilder<Employee>(csvr)
                    .withMappingStrategy(strategy)
                    .build();
            list = csvtb.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list);
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static void parseXML(String fileName2) throws ParserConfigurationException,
//            IOException, SAXException {
//        List<Employee> list2 = new ArrayList<>();
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse(new File("data.xml"));
//        NodeList nodeList = doc.getElementsByTagName("employee");
//        //System.out.println(nodeList);
//        for (int i = 0; i < nodeList.getLength(); i++) {
//            Node node = nodeList.item(i);
//            if (Node.ELEMENT_NODE == node.getNodeType()) {
//                Element element = (Element) node;
//                if (Node.ELEMENT_NODE == node.getNodeType()) {
//                    Element element = (Element) node;
//                    element.getTextContent(id);
//                    }}}}

                    private static void read (Node node){
                        NodeList nodeList = node.getChildNodes();
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            Node node_ = nodeList.item(i);
                            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                                System.out.println("Текущий узел: " + node_.getNodeName());
                                Element element = (Element) node_;
                                NamedNodeMap map = element.getAttributes();
                                for (int a = 0; a < map.getLength(); a++) {
                                    String attrName = map.item(a).getNodeName();
                                    String attrValue = map.item(a).getNodeValue();
                                    System.out.println("Атрибут: " + attrName + "; значение: " + attrValue);
                                }
                                read(node_);
                            }
                        }
                    }
                }