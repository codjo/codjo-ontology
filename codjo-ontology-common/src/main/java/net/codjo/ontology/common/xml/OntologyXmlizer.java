package net.codjo.ontology.common.xml;
import com.thoughtworks.xstream.XStream;
import java.util.Iterator;

public class OntologyXmlizer {

    private OntologyXmlizer() {
    }


    public static String toXml(Object bean, OntologyConfiguration ontologyConfiguration) {
        XStream xStream = createXStream(ontologyConfiguration);
        return replaceXsdHeader(xStream.toXML(bean), ontologyConfiguration);
    }


    public static Object fromXml(String xml, OntologyConfiguration ontologyConfiguration) {
        XStream xStream = createXStream(ontologyConfiguration);
        return xStream.fromXML(xml);
    }


    private static String replaceXsdHeader(String xml, OntologyConfiguration ontologyConfiguration) {
        if (ontologyConfiguration.getXsdFilename() == null) {
            return xml;
        }
        return xml.replaceFirst("<(.*)>",
                                "<$1 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\""
                                + ontologyConfiguration.getXsdFilename()
                                + "\">");
    }


    private static XStream createXStream(OntologyConfiguration ontologyConfiguration) {
        XStream xStream = new XStream();

        for (Iterator iterator = ontologyConfiguration.getAliases(); iterator.hasNext();) {
            OntologyConfiguration.Alias alias = (OntologyConfiguration.Alias)iterator.next();
            xStream.alias(alias.getAlias(), alias.getBean());
        }

        return xStream;
    }
}
