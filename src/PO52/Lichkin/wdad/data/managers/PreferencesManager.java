package PO52.Lichkin.wdad.data.managers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Enumeration;
import java.util.Properties;

import PO52.Lichkin.wdad.utils.PreferencesConstantManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PreferencesManager {
    private static volatile PreferencesManager instance;
    private static final String FILE_PATH="src\\PO52\\Lichkin\\wdad\\resources\\configuration\\appconfig.xml";
    private Document document;

    private PreferencesManager() throws Exception {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        document = documentBuilder.parse(FILE_PATH);
    }

    public static PreferencesManager getInstance() throws Exception {
        if (instance == null)
            synchronized (PreferencesManager.class) {
                if (instance == null)
                    instance = new PreferencesManager();
            }
        return instance;
    }

    private Element getElement(String tagName) {
        NodeList elements = document.getElementsByTagName(tagName);
        return (Element) elements.item(0);
    }

    public void setProperty(String key, String value){
        getElement(key).setTextContent(value);
    }

    public String getProperty(String key){
        return getElement(key).getTextContent();
    }

    public void setProperties(Properties prop){
        Enumeration elements = prop.elements();
        Enumeration keys = prop.keys();
        while (elements.hasMoreElements()) {
            getElement((String) elements.nextElement()).setTextContent((String) keys.nextElement());
        }
    }

    public Properties getProperties(){
        Properties prop = new Properties();
        String[] keys = new String[]{
                PreferencesConstantManager.CREATEREGISTRY,
                PreferencesConstantManager.REGISTRYADDRESS,
                PreferencesConstantManager.REGISTRYPORT,
                PreferencesConstantManager.POLICYPATH,
                PreferencesConstantManager.USECODEBASEONLY,
                PreferencesConstantManager.CLASSPROVIDER
        };
        for (String key : keys) {
            prop.setProperty(key, getElement(key).getTextContent());
        }
        return prop;
    }

    public void addBindedObject(String name, String className){
        Element element= document.createElement("bindedobject");
        element.setAttribute("class",className);
        element.setAttribute("name",name);
        getElement("rmi").appendChild(element);
    }

    public void removeBindedObject(String name){
        NodeList nodeList = document.getElementsByTagName("bindedobject");
        Element element;
        for (int i = 0; i < nodeList.getLength(); i++) {
            element = (Element) nodeList.item(i);
            if (element.getAttribute("name").equals(name))
            {
                getElement("rmi").removeChild(element);
            }
        }
    }

    @Deprecated
    public String getCreateregistry() {
        return getElement("createregistry").getTextContent();
    }

    @Deprecated
    public void setCreateregistry(String value) {
        getElement("createregistry").setTextContent(value);
    }

    @Deprecated
    public String getRegistryaddress() {
        return getElement("registryaddress").getTextContent();
    }

    @Deprecated
    public void setRegistryaddress(String value) {
        getElement("registryaddress").setTextContent(value);
    }

    @Deprecated
    public int getRegistryport() {
        return Integer.parseInt(getElement("registryport").getTextContent());
    }

    @Deprecated
    public void setRegistryport(int value) {
        getElement("registryport").setTextContent(String.valueOf(value));
    }

    @Deprecated
    public String getPolicypath() {
        return getElement("policypath").getTextContent();
    }

    @Deprecated
    public void setPolicypath(String value) {
        getElement("policypath").setTextContent(value);
    }

    @Deprecated
    public String getUsecodebaseonly() {
        return getElement("usecodebaseonly").getTextContent();
    }

    @Deprecated
    public void setUsecodebaseonly(String value) {
        getElement("usecodebaseonly").setTextContent(value);
    }

    @Deprecated
    public String getClassprovider() {
        return getElement("classprovider").getTextContent();
    }

    @Deprecated
    public void setClassprovider(String value) {
        getElement("classprovider").setTextContent(value);
    }


    private void writeDocument() throws TransformerFactoryConfigurationError {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(FILE_PATH));
            tr.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace(System.out);
        }
    }
}
