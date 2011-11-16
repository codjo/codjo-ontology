package net.codjo.ontology.generator;
import net.codjo.util.file.FileUtil;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class Main {
    private final OntologyConfiguration configuration;
    private final File rootDir;


    Main(File configurationFile) throws IOException {
        configuration = load(configurationFile);

        rootDir = configurationFile.getParentFile().getAbsoluteFile();
    }


    @SuppressWarnings({"MethodNamesDifferingOnlyByCase"})
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: "
                                               + "java net.codjo.ontology.generator.Main <configuration file>");
        }

        File configurationFile = new File(args[0]);
        if (!configurationFile.exists()) {
            throw new IllegalArgumentException("Fichier de configuration introuvable : " + configurationFile);
        }

        new Main(configurationFile).generateAll();
    }


    private void generateAll() throws IOException {
        ProtegeGenerator generator = new ProtegeGenerator(
              FileUtil.loadContent(toFile(configuration.getProtegeFile())));

        generator.generateXsd(configuration.getGeneration().getXsdRootNode(),
                              toFile(configuration.getGeneration().getOutputDirectory() + "/xsd/"
                                     + configuration.getName()
                                     + ".xsd"));
        generator.generateJavaFiles(configuration.getGeneration().getPackageName(),
                                    getJavaOutputDir());
        generator.generateOntologyFile(configuration.getGeneration().getPackageName(),
                                       configuration.getName(),
                                       configuration.getRelationships(),
                                       getJavaOutputDir());
    }


    private File getJavaOutputDir() throws IOException {
        File outputDir = toFile(configuration.getGeneration().getOutputDirectory() + "/java");
        outputDir = new File(outputDir,
                             configuration.getGeneration().getPackageName().replaceAll("\\.", "/"));
        return outputDir;
    }


    public static OntologyConfiguration load(File configuration) throws IOException {
        XStream xstream = createXstream();

        FileReader reader = new FileReader(configuration);
        try {
            return (OntologyConfiguration)xstream.fromXML(reader);
        }
        finally {
            reader.close();
        }
    }


    static XStream createXstream() {
        XStream xstream = new XStream();
        xstream.alias("ontology", OntologyConfiguration.class);
        xstream.alias("relationships", RelationshipManager.class);
        xstream.alias("relationship", RelationshipManager.Relationship.class);
        xstream.addImplicitCollection(RelationshipManager.class, "relationships");
        return xstream;
    }


    private File toFile(File relativeFilePath) throws IOException {
        return toFile(relativeFilePath.getPath());
    }


    private File toFile(String relativeFilePath) throws IOException {
        return new File(rootDir, relativeFilePath).getCanonicalFile();
    }
}
