package net.codjo.ontology.common.xml;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 *
 */
public class OntologyConfiguration {
    private List<Alias> aliases = new ArrayList<Alias>();
    private String xsdFilename;

    public class Alias {
        private String alias;
        private Class aClass;


        public Alias(String alias, Class aClass) {
            this.alias = alias;
            this.aClass = aClass;
        }


        public String getAlias() {
            return alias;
        }


        public Class getBean() {
            return aClass;
        }
    }


    public void addAlias(Class aClass) {
        String simpleClassName = aClass.getSimpleName();
        String alias = simpleClassName.substring(0, 1).toLowerCase() + simpleClassName.substring(1);
        addAlias(alias, aClass);
    }


    public void addAlias(String alias, Class aClass) {
        aliases.add(new Alias(alias, aClass));
    }


    public void setXsdFile(String xsdFilename) {
        this.xsdFilename = xsdFilename;
    }


    public String getXsdFilename() {
        return xsdFilename;
    }


    public Iterator<Alias> getAliases() {
        return aliases.iterator();
    }
}
