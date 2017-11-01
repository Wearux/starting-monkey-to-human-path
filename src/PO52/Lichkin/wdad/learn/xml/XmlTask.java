package PO52.Lichkin.wdad.learn.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlTask {
    private String filePath;
    private Document document;

    public XmlTask(String filePath)  {
        this.filePath = filePath;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(filePath);
            checkTotalcost();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    //проверка тега <totalcost>
    private void checkTotalcost() {
        int totalcost=0;
        NodeList orders = document.getElementsByTagName("order");
        Node order;
        NodeList items;
        Node item;
        boolean isTotalcost=false;
        for (int i = 0; i < orders.getLength(); i++) {
            order = orders.item(i);
            items=order.getChildNodes();
            for (int j = 0; j < items.getLength(); j++) {
                item=items.item(j);
                if(item.getNodeName().equals("item")){
                    totalcost+=Integer.parseInt(item.getAttributes().getNamedItem("cost").getTextContent());
                }
                if(item.getNodeName().equals("totalcost")){
                    isTotalcost=true;
                    if(!item.getTextContent().equals(totalcost)) item.setTextContent(String.valueOf(totalcost));
                }
            }
            if(!isTotalcost){
                item = document.createElement("totalcost");
                item.setTextContent(String.valueOf(totalcost));
                order.appendChild(item);
            }
            isTotalcost=false;
            totalcost=0;
        }
        writeDocument();
    }

    //возвращающий суммарную выручку заданного официанта в заданный день
    public int earningsTotal(String officiantSecondName, Calendar calendar){
        int earningsTotal=0;
        NodeList dates = document.getElementsByTagName("date");
        Node date;
        Calendar dateOrders;
        NodeList orders;
        NodeList items;
        boolean foundOfficiant=false;
        for (int i = 0; i < dates.getLength(); i++) {
            date=dates.item(i);
            dateOrders= new GregorianCalendar(Integer.parseInt(date.getAttributes().getNamedItem("year").getTextContent()),
                    Integer.parseInt(date.getAttributes().getNamedItem("month").getTextContent()),
                    Integer.parseInt(date.getAttributes().getNamedItem("day").getTextContent()));
            if(dateOrders.equals(calendar)){
                orders=date.getChildNodes();
                for (int j = 0; j < orders.getLength(); j++) {
                    foundOfficiant=false;
                    items=orders.item(j).getChildNodes();
                    for (int k = 0; k < items.getLength(); k++) {
                        if(items.item(k).getNodeName().equals("officiant") &&
                                items.item(k).getAttributes().getNamedItem("secondname").getTextContent().equals(officiantSecondName))
                            foundOfficiant=true;
                        if(foundOfficiant &&  items.item(k).getNodeName().equals("totalcost"))
                            earningsTotal+=Integer.parseInt(items.item(k).getTextContent());
                    }
                }
            }
        }
        return earningsTotal;
    }

    //удаляет информацию по заданному дню
    public void removeDay(Calendar calendar){
        Node root = document.getDocumentElement();
        NodeList dates = document.getElementsByTagName("date");
        Node date;
        Calendar dateOrders;
        for (int i = 0; i < dates.getLength(); i++) {
            date=dates.item(i);
            dateOrders= new GregorianCalendar(Integer.parseInt(date.getAttributes().getNamedItem("year").getTextContent()),
                    Integer.parseInt(date.getAttributes().getNamedItem("month").getTextContent()),
                    Integer.parseInt(date.getAttributes().getNamedItem("day").getTextContent()));
            if(dateOrders.equals(calendar)){
                root.removeChild(date);
                writeDocument();
                break;
            }
        }
    }

    //изменяющий имя и фамилию официанта во всех днях
    public void changeOfficiantName(String oldFirstName, String oldSecondName, String newFirstName, String newSecondName){
        NodeList officiants = document.getElementsByTagName("officiant");
        Node officiant;
        for (int i = 0; i < officiants.getLength(); i++) {
            officiant=officiants.item(i);
            if(officiant.getAttributes().getNamedItem("firstname").getTextContent().equals(oldFirstName) &&
                    officiant.getAttributes().getNamedItem("secondname").getTextContent().equals(oldSecondName)){
                officiant.getAttributes().getNamedItem("firstname").setTextContent(newFirstName);
                officiant.getAttributes().getNamedItem("secondname").setTextContent(newSecondName);
            }
        }
        writeDocument();
    }

    private void writeDocument() throws TransformerFactoryConfigurationError {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            tr.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace(System.out);
        }
    }
}
