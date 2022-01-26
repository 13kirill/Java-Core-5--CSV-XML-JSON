import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.*;
import com.opencsv.bean.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileNameCSV = "data.csv";
        String fileNameXML = "data.xml";
        String fileNameJSON1 = "data1.json";
        String fileNameJSON2 = "data2.json";

        listToJson(parseCSV(columnMapping, fileNameCSV), fileNameJSON1);
        listToJson(parseXML(fileNameXML), fileNameJSON2);

        readString("data1.json");
    }

    //Читаем csv файл
    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {

        List<Employee> list = new ArrayList<>();

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

    private static void listToJson(List<Employee> list, String fileName) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list);
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    private static void read(Node node) {
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
    }*/

    private static List<Employee> parseXML(String fileName) {
        List<Employee> result = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileInputStream);
            NodeList nodes = document.getDocumentElement().getChildNodes();
            Node item;
            for (int i = 0; i < nodes.getLength(); i++) {
                item = nodes.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    result.add(new Employee(Long.parseLong(getValueFromElement(item, "id")),
                            getValueFromElement(item, "firstName"),
                            getValueFromElement(item, "lastName"),
                            getValueFromElement(item, "country"),
                            Integer.parseInt(getValueFromElement(item, "age"))));
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getValueFromElement(Node element, String name) {
        return ((Element) element).getElementsByTagName(name).item(0).getTextContent();
    }
/*  private static void fillFieldFromNode(Employee employee, Node node){
        switch (node.getNodeName()){
            case "id":
                employee.id = Long.parseLong(node.getTextContent());
                break;
            case "firstName":
                employee.firstName = node.getTextContent();
                break;
            case "lastName":
                employee.lastName = node.getTextContent();
                break;
            case "country":
                employee.country = node.getTextContent();
                break;
            case "age":
                employee.age = Integer.parseInt(node.getTextContent());
                break;
        }
    }*/

    private static void readString(String fileName) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fileReader = new FileReader((fileName));
        Type type = new TypeToken<List<Employee>>(){}.getType();
        List<Employee> employees = gson.fromJson(fileReader, type);
        System.out.println(employees);;
    }
}