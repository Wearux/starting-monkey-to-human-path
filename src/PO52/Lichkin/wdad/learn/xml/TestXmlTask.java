package PO52.Lichkin.wdad.learn.xml;

import java.util.GregorianCalendar;

public class TestXmlTask {
    public static void main(String[] args)  {
        XmlTask xml = new XmlTask ("src\\PO52\\Lichkin\\wdad\\learn\\xml\\GoodXml.xml");
        System.out.println(xml.earningsTotal("ivanov",new GregorianCalendar(2017, 11, 12)));
        //xml.removeDay(new GregorianCalendar(2017, 11, 13));
        xml.changeOfficiantName("Andrey","petrov","Name","sidorov");
    }
}
