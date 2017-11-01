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

    public String getCreateregistry() {
        return getElement("createregistry").getTextContent();
    }

    public void setCreateregistry(String value) {
        getElement("createregistry").setTextContent(value);
    }

    public String getRegistryaddress() {
        return getElement("registryaddress").getTextContent();
    }

    public void setRegistryaddress(String value) {
        getElement("registryaddress").setTextContent(value);
    }

    public int getRegistryport() {
        return Integer.parseInt(getElement("registryport").getTextContent());
    }

    public void setRegistryport(int value) {
        getElement("registryport").setTextContent(String.valueOf(value));
    }

    public String getPolicypath() {
        return getElement("policypath").getTextContent();
    }

    public void setPolicypath(String value) {
        getElement("policypath").setTextContent(value);
    }

    public String getUsecodebaseonly() {
        return getElement("usecodebaseonly").getTextContent();
    }

    public void setUsecodebaseonly(String value) {
        getElement("usecodebaseonly").setTextContent(value);
    }


    public String getClassprovider() {
        return getElement("classprovider").getTextContent();
    }

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
