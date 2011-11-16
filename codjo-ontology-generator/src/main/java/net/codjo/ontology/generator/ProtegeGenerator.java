package net.codjo.ontology.generator;
import net.codjo.util.file.FileUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import jscheme.JScheme;

public class ProtegeGenerator {
    static final String HEADER
          = "<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema' elementFormDefault='qualified'>";
    static final String FOOTER = "</xs:schema>";
    private final String pontContent;


    public static String generateSchema(URL pontFile, String rootNode) throws IOException {
        String input = FileUtil.loadContent(pontFile);
        return new ProtegeGenerator(input).generateXsd(rootNode);
    }


    public ProtegeGenerator(String pontContent) throws IOException {
        this.pontContent = uncommentAll(pontContent);
    }


    public void generateJavaFiles(String packageName, File outputDirectory) throws IOException {
        outputDirectory.mkdirs();
        JScheme jScheme = createScheme("ProtegeFileJavaGenerator.scm");
        jScheme.setGlobalValue("SchemeUtil", new SchemeUtil(packageName, outputDirectory));
        jScheme.eval("(generateAll " + pontContent + ")");
    }


    public void generateOntologyFile(String packageName,
                                     String ontologyName,
                                     RelationshipManager relationshipManager,
                                     File outputDirectory) throws IOException {
        outputDirectory.mkdirs();
        JScheme jScheme = createScheme("ProtegeFileOntologyGenerator.scm");
        jScheme.setGlobalValue("SchemeUtil", new SchemeUtil(packageName, outputDirectory));
        jScheme.setGlobalValue("RelationshipManager", relationshipManager);
        jScheme.eval("(generateOntology " + ontologyName + " " + pontContent + ")");
    }


    public void generateXsd(String rootNode, File outputFile) throws IOException {
        outputFile.getParentFile().mkdirs();
        FileUtil.saveContent(outputFile, generateXsd(rootNode));
    }


    public String generateXsd(String rootNode) throws IOException {
        JScheme jScheme = createScheme("ProtegeFileXsdGenerator.scm");

        return HEADER + jScheme.eval("(generateXsd " + pontContent + ")")
               + createRootNodeElement(rootNode)
               + FOOTER;
    }


    private String createRootNodeElement(String name) {
        return "<xs:element name='"
               + name.substring(0, 1).toLowerCase()
               + name.substring(1)
               + "' type='" + name + "Type'/>";
    }


    private JScheme createScheme(String generatorName) throws IOException {
        JScheme jScheme = new JScheme();
        jScheme.load(FileUtil.loadContent(getClass().getResource(generatorName)));
        return jScheme;
    }


    private String uncommentAll(String content) {
        return content.replaceAll(";\\+", "");
    }


    public static class SchemeUtil {

        private final String packageName;
        private final File outputDirectory;


        public SchemeUtil(String packageName, File outputDirectory) {
            this.packageName = packageName;
            this.outputDirectory = outputDirectory;
        }


        public String getPackageName() {
            return packageName;
        }


        public void generateFile(String className, String body) throws IOException {
            File file = new File(outputDirectory, className + ".java");
            //noinspection UseOfSystemOutOrSystemErr
            System.out.println(file + " (generated)");
            FileUtil.saveContent(file, body);
        }
    }
}
