package net.codjo.ontology.generator;
import java.io.File;
/**
 *
 */
@SuppressWarnings({"UnusedDeclaration"})
public class OntologyConfiguration {
    private String name;
    private File protegeFile;
    private RelationshipManager relationships;
    private Generation generation;


    public File getProtegeFile() {
        return protegeFile;
    }


    public String getName() {
        return name;
    }


    public RelationshipManager getRelationships() {
        return relationships;
    }


    public Generation getGeneration() {
        return generation;
    }


    public class Generation {
        private File outputDirectory;
        private String packageName;
        private String xsdRootNode;


        public File getOutputDirectory() {
            return outputDirectory;
        }


        public String getPackageName() {
            return packageName;
        }


        public String getXsdRootNode() {
            return xsdRootNode;
        }
    }
}