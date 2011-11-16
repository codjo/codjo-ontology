package net.codjo.ontology.generator;
import static net.codjo.ontology.generator.RelationshipManager.that;
import net.codjo.test.common.Directory;
import net.codjo.test.common.PathUtil;
import net.codjo.test.common.XmlUtil;
import net.codjo.test.common.fixture.DirectoryFixture;
import net.codjo.util.file.FileUtil;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
/**
 *
 */
public class ProtegeGeneratorTest extends TestCase {
    private DirectoryFixture generationDirectory = DirectoryFixture.newTemporaryDirectoryFixture();
    private static final String PACKAGE_DIRECTORY = "src/java/net/codjo/test/ontology";


    @Override
    protected void setUp() throws Exception {
        generationDirectory.doSetUp();
    }


    @Override
    protected void tearDown() throws Exception {
        generationDirectory.doTearDown();
    }


    public void test_java_agentAction() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + ")";
        String expected = "package net.codjo.scribe.ontology;"
                          + " import net.codjo.ontology.common.api.AgentAction;"
                          + " public class GetProductAction implements AgentAction {"
                          + " }";

        generateJava(input, "net.codjo.scribe.ontology");

        assertJavaFile(expected, "GetProductAction.java");
    }


    public void test_java_concept() throws Exception {
        String input = "(defclass MyConcept"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + ")";
        String expected = "package net.codjo.fubar.ontology;"
                          + " import net.codjo.ontology.common.api.Concept;"
                          + " public class MyConcept implements Concept {"
                          + " }";

        generateJava(input, "net.codjo.fubar.ontology");

        assertJavaFile(expected, "MyConcept.java");
    }


    public void test_java_singleSlot_String() throws Exception {
        String input = "(defclass MyConcept"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (single-slot date"
                       + "    (type STRING)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + ")";
        String expected = "package net.codjo.fubar.ontology;"
                          + " import net.codjo.ontology.common.api.Concept;"
                          + " public class MyConcept implements Concept {"
                          + "   private String date;"
                          + "   public void setDate(String value) { this.date = value; }"
                          + "   public String getDate() { return this.date; } "
                          + " }";

        generateJava(input, "net.codjo.fubar.ontology");

        assertJavaFile(expected, "MyConcept.java");
    }


    public void test_java_singleSlot_int() throws Exception {
        String input = "(defclass MyConcept"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (single-slot id"
                       + "    (type INTEGER)"
                       + "    (cardinality 0 1)"
                       + "    (create-accessor read-write))"
                       + ")";
        String expected = "package net.codjo.fubar.ontology;"
                          + " import net.codjo.ontology.common.api.Concept;"
                          + " public class MyConcept implements Concept {"
                          + "   private int id;"
                          + "   public void setId(int value) { this.id = value; }"
                          + "   public int getId() { return this.id; } "
                          + " }";

        generateJava(input, "net.codjo.fubar.ontology");

        assertJavaFile(expected, "MyConcept.java");
    }


    public void test_java_singleSlot_boolean() throws Exception {
        String input = "(defclass MyConcept"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (single-slot isAllowed"
                       + "    (type SYMBOL)"
                       + "    (allowed-values FALSE TRUE)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + ")";
        String expected = "package net.codjo.fubar.ontology;"
                          + " import net.codjo.ontology.common.api.Concept;"
                          + " public class MyConcept implements Concept {"
                          + "   private boolean isAllowed;"
                          + "   public void setIsAllowed(boolean value) { this.isAllowed = value; }"
                          + "   public boolean getIsAllowed() { return this.isAllowed; } "
                          + " }";

        generateJava(input, "net.codjo.fubar.ontology");

        assertJavaFile(expected, "MyConcept.java");
    }


    public void test_java_singleSlot_instanceType() throws Exception {
        String input = "(defclass MyConcept"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (single-slot status"
                       + "    (type INSTANCE)"
                       + "    (allowed-classes Status)"
                       + "    (cardinality 0 1)"
                       + "    (create-accessor read-write))"
                       + ")";
        String expected = "package net.codjo.fubar.ontology;"
                          + " import net.codjo.ontology.common.api.Concept;"
                          + " public class MyConcept implements Concept {"
                          + "   private Status status;"
                          + "   public void setStatus(Status value) { this.status = value; }"
                          + "   public Status getStatus() { return this.status; } "
                          + " }";

        generateJava(input, "net.codjo.fubar.ontology");

        assertJavaFile(expected, "MyConcept.java");
    }


    public void test_java_multislot_instanceType() throws Exception {
        String input = "(defclass MyConcept"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (multislot classifierList"
                       + "     (type INSTANCE)"
                       + "     (allowed-classes Classifier)"
                       + "     (create-accessor read-write))"
                       + ") ";

        String expected = "package net.codjo.fubar.ontology;"
                          + " import net.codjo.ontology.common.api.Concept;"
                          + " public class MyConcept implements Concept {"
                          + "   private java.util.List<Classifier> classifierList;"
                          + "   public void setClassifierList(java.util.List<Classifier> value) { this.classifierList = value; }"
                          + "   public java.util.List<Classifier> getClassifierList() { return this.classifierList; } "
                          + " }";

        generateJava(input, "net.codjo.fubar.ontology");

        assertJavaFile(expected, "MyConcept.java");
    }


    public void test_java_multipleDefClass() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + ")"
                       + "(defclass GetProductResponse"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + ")";

        generateJava(input, "net.codjo.fubar.ontology");

        assertJavaFile("package net.codjo.fubar.ontology;"
                       + " import net.codjo.ontology.common.api.AgentAction;"
                       + " public class GetProductAction implements AgentAction {"
                       + " }",
                       "GetProductAction.java");

        assertJavaFile("package net.codjo.fubar.ontology;"
                       + " import net.codjo.ontology.common.api.AgentAction;"
                       + " public class GetProductResponse implements AgentAction {"
                       + " }",
                       "GetProductResponse.java");
    }


    public void test_java_global() throws Exception {
        generateJavaFiles(generationDirectory, "net.codjo.fubar.ontology");
    }


    public void test_ontologyClass_agentAction() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + ")";

        String expectedBody =
              " AgentActionSchema GetProductActionSchema = new AgentActionSchema('GetProductAction');"
              + " add(GetProductActionSchema, GetProductAction.class);";

        assertOntologyClass(expectedBody, input);
    }


    public void test_ontologyClass_concept() throws Exception {
        String input = "(defclass Status"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + ")";
        String expectedBody = " ConceptSchema StatusSchema = new ConceptSchema('Status');"
                              + " add(StatusSchema, Status.class);";

        assertOntologyClass(expectedBody, input);
    }


    public void test_ontologyClass_singleSlot_String() throws Exception {
        String input = "(defclass Status"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (single-slot id"
                       + "    (type STRING)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + "  (single-slot label"
                       + "    (type STRING)"
                       + "    (cardinality 0 1)"
                       + "    (create-accessor read-write))"
                       + ")";
        String expectedBody = " ConceptSchema StatusSchema = new ConceptSchema('Status');"
                              + " add(StatusSchema, Status.class);"
                              + ""
                              + " StatusSchema.add('id', "
                              + "           (TermSchema)getSchema(BasicOntology.STRING), "
                              + "           ObjectSchema.MANDATORY);"
                              + " StatusSchema.add('label', "
                              + "           (TermSchema)getSchema(BasicOntology.STRING), "
                              + "           ObjectSchema.OPTIONAL);";

        assertOntologyClass(expectedBody, input);
    }


    public void test_ontologyClass_singleSlot_int() throws Exception {
        String input = "(defclass Status"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (single-slot id"
                       + "    (type INTEGER)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + ")";
        String expectedBody = " ConceptSchema StatusSchema = new ConceptSchema('Status');"
                              + " add(StatusSchema, Status.class);"
                              + ""
                              + " StatusSchema.add('id', "
                              + "           (TermSchema)getSchema(BasicOntology.INTEGER), "
                              + "           ObjectSchema.MANDATORY);";

        assertOntologyClass(expectedBody, input);
    }


    public void test_ontologyClass_singleSlot_boolean() throws Exception {
        String input = "(defclass Status"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (single-slot isAllowed"
                       + "    (type SYMBOL)"
                       + "    (allowed-values FALSE TRUE)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + ")";
        String expectedBody = " ConceptSchema StatusSchema = new ConceptSchema('Status');"
                              + " add(StatusSchema, Status.class);"
                              + ""
                              + " StatusSchema.add('isAllowed', "
                              + "           (TermSchema)getSchema(BasicOntology.BOOLEAN), "
                              + "           ObjectSchema.MANDATORY);";

        assertOntologyClass(expectedBody, input);
    }


    public void test_ontologyClass_singleSlot_instanceType() throws Exception {
        String input = "(defclass Status"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (single-slot actor"
                       + "    (type INSTANCE)"
                       + "    (allowed-classes ActorSegment)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + ")";
        String expectedBody = " ConceptSchema StatusSchema = new ConceptSchema('Status');"
                              + " add(StatusSchema, Status.class);"
                              + ""
                              + " StatusSchema.add('actor', "
                              + "           ActorSegmentSchema, "
                              + "           ObjectSchema.MANDATORY);";

        assertOntologyClass(expectedBody, input);
    }


    public void test_ontologyClass_multiSlot_instanceType() throws Exception {
        String input = "(defclass Segment"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (multislot classifierList"
                       + "     (type INSTANCE)"
                       + "     (allowed-classes Classifier)"
                       + "     (create-accessor read-write))"
                       + ")";
        String expectedBody = " ConceptSchema SegmentSchema = new ConceptSchema('Segment');"
                              + " add(SegmentSchema, Segment.class);"
                              + ""
                              + " SegmentSchema.add('classifierList', ClassifierSchema, 0, ObjectSchema.UNLIMITED);";

        assertOntologyClass(expectedBody, input);
    }


    public void test_ontologyClass_multipleDefClass() throws Exception {
        String input = "(defclass Segment"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (multislot classifierList"
                       + "     (type INSTANCE)"
                       + "     (allowed-classes Classifier)"
                       + "     (create-accessor read-write))"
                       + ")"
                       + "(defclass Classifier"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + "  (multislot partList"
                       + "     (type INSTANCE)"
                       + "     (allowed-classes Part)"
                       + "     (create-accessor read-write))"
                       + ")";
        String expectedBody = " ConceptSchema SegmentSchema = new ConceptSchema('Segment');"
                              + " add(SegmentSchema, Segment.class);"
                              + " ConceptSchema ClassifierSchema = new ConceptSchema('Classifier');"
                              + " add(ClassifierSchema, Classifier.class);"
                              + ""
                              + " SegmentSchema.add('classifierList', ClassifierSchema, 0, ObjectSchema.UNLIMITED);"
                              + " ClassifierSchema.add('partList', PartSchema, 0, ObjectSchema.UNLIMITED);";

        assertOntologyClass(expectedBody, input);
    }


    public void test_ontologyClass_result() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + ")"
                       + "(defclass GetProductResponse"
                       + "  (is-a Concept)"
                       + "  (role concrete)"
                       + ")";

        String expectedBody =
              " AgentActionSchema GetProductActionSchema = new AgentActionSchema('GetProductAction');"
              + " add(GetProductActionSchema, GetProductAction.class);"
              + ""
              + " ConceptSchema GetProductResponseSchema = new ConceptSchema('GetProductResponse');"
              + " add(GetProductResponseSchema, GetProductResponse.class);"
              + ""
              + " GetProductActionSchema.setResult(GetProductResponseSchema,1,1);";

        assertOntologyClass(expectedBody, input,
                            RelationshipManager.create()
                                  .define(that("GetProductAction").returnsOne("GetProductResponse")));
    }


    public void test_xsd_singleSlot_cardinality() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + "  (single-slot date"
                       + "    (type STRING)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + "  (single-slot code"
                       + "    (type STRING)"
                       + "    (cardinality 1 2)"
                       + "    (create-accessor read-write))"
                       + ")";

        String expected = "<xs:complexType name='GetProductActionType'>"
                          + " <xs:sequence>"
                          + "   <xs:element minOccurs='1' maxOccurs='1' name='date' type='xs:string'/>"
                          + "   <xs:element minOccurs='1' maxOccurs='2' name='code' type='xs:string'/>"
                          + " </xs:sequence>"
                          + "</xs:complexType>";
        assertResult(expected, input);
    }


    public void test_xsd_singleSlot_defaultType() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + "  (single-slot date"
                       + "    (type INTEGER)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + "  (single-slot label"
                       + "    (type STRING)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + ") ";

        String expected = "<xs:complexType name='GetProductActionType'>"
                          + " <xs:sequence>"
                          + "   <xs:element minOccurs='1' maxOccurs='1' name='date' type='xs:int'/>"
                          + "   <xs:element minOccurs='1' maxOccurs='1' name='label' type='xs:string'/>"
                          + " </xs:sequence>"
                          + "</xs:complexType>";
        assertResult(expected, input);
    }


    public void test_xsd_singleSlot_booleanType() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + "  (single-slot isAllowed"
                       + "    (type SYMBOL)"
                       + "    (allowed-values FALSE TRUE)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + ") ";

        String expected = "<xs:complexType name='GetProductActionType'>"
                          + " <xs:sequence>"
                          + "   <xs:element minOccurs='1' maxOccurs='1' name='isAllowed' type='xs:boolean'/>"
                          + " </xs:sequence>"
                          + "</xs:complexType>";
        assertResult(expected, input);
    }


    public void test_xsd_singleSlot_restrictedType() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + "  (single-slot fieldStatus"
                       + "    (type SYMBOL)"
                       + "    (allowed-values Applicable NotApplicable)"
                       + "    (cardinality 0 1)"
                       + "    (create-accessor read-write))"
                       + ") ";

        String expected = "<xs:complexType name='GetProductActionType'>"
                          + " <xs:sequence>"
                          + "   <xs:element minOccurs='0' maxOccurs='1' name='fieldStatus'>"
                          + "     <xs:simpleType>"
                          + "         <xs:restriction base='xs:string'>"
                          + "             <xs:enumeration value='Applicable'/>"
                          + "             <xs:enumeration value='NotApplicable'/>"
                          + "         </xs:restriction>"
                          + "     </xs:simpleType>"
                          + "   </xs:element>"
                          + " </xs:sequence>"
                          + "</xs:complexType>";
        assertResult(expected, input);
    }


    public void test_xsd_singleSlot_instanceType() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + "  (single-slot status"
                       + "    (type INSTANCE)"
                       + "    (allowed-classes Status)"
                       + "    (cardinality 0 1)"
                       + "    (create-accessor read-write))"
                       + ") ";

        String expected = "<xs:complexType name='GetProductActionType'>"
                          + " <xs:sequence>"
                          + "   <xs:element minOccurs='0' maxOccurs='1' name='status' type='StatusType'/>"
                          + " </xs:sequence>"
                          + "</xs:complexType>";
        assertResult(expected, input);
    }


    public void test_xsd_multislot_instanceType() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + "  (multislot classifierList"
                       + "     (type INSTANCE)"
                       + "     (allowed-classes Classifier)"
                       + "     (create-accessor read-write))"
                       + ") ";

        String expected = "<xs:complexType name='GetProductActionType'>"
                          + " <xs:sequence>"
                          + "   <xs:element name='classifierList' minOccurs='0'>"
                          + "       <xs:complexType>"
                          + "           <xs:sequence minOccurs='0' maxOccurs='unbounded'>"
                          + "               <xs:element name='classifier' type='ClassifierType'/>"
                          + "           </xs:sequence>"
                          + "       </xs:complexType>"
                          + "   </xs:element>"
                          + " </xs:sequence>"
                          + "</xs:complexType>";
        assertResult(expected, input);
    }


    public void test_xsd_multipleDefClass() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + "  (single-slot label"
                       + "    (type STRING)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + ")"
                       + "(defclass GetProductResponse"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + "  (single-slot result"
                       + "    (type STRING)"
                       + "    (cardinality 1 1)"
                       + "    (create-accessor read-write))"
                       + ")";

        String expected = "<xs:complexType name='GetProductActionType'>"
                          + " <xs:sequence>"
                          + "   <xs:element minOccurs='1' maxOccurs='1' name='label' type='xs:string'/>"
                          + " </xs:sequence>"
                          + "</xs:complexType>"
                          + "<xs:complexType name='GetProductResponseType'>"
                          + " <xs:sequence>"
                          + "   <xs:element minOccurs='1' maxOccurs='1' name='result' type='xs:string'/>"
                          + " </xs:sequence>"
                          + "</xs:complexType>";
        assertResult(expected, input);
    }


    public void test_xsd_uncomment() throws Exception {
        String input = "(defclass GetProductAction"
                       + "  (is-a AgentAction)"
                       + "  (role concrete)"
                       + "  (single-slot label"
                       + ";+    (type STRING)\n"
                       + ";+    (cardinality 1 1)\n"
                       + "    (create-accessor read-write))"
                       + ")";

        String expected = "<xs:complexType name='GetProductActionType'>"
                          + " <xs:sequence>"
                          + "   <xs:element minOccurs='1' maxOccurs='1' name='label' type='xs:string'/>"
                          + " </xs:sequence>"
                          + "</xs:complexType>";
        assertResult(expected, input);
    }


    public void test_xsd_removeFakeClass() throws Exception {
        String input = "(defclass %3ACLIPS_TOP_LEVEL_SLOT_CLASS)";

        String expected = "";
        assertResult(expected, input);
    }


    public void test_xsd_global() throws Exception {
        ProtegeGenerator.generateSchema(getSamplePontFile(), "Status");
    }


    private void assertOntologyClass(String expectedBody, String input) throws IOException {
        assertOntologyClass(expectedBody, input, RelationshipManager.create());
    }


    private void assertOntologyClass(String expectedBody, String input,
                                     RelationshipManager relationshipManager) throws IOException {
        File output = new File(generationDirectory, PACKAGE_DIRECTORY);
        new ProtegeGenerator(input).generateOntologyFile("net.codjo.fubar.ontology",
                                                         "Product",
                                                         relationshipManager,
                                                         output);

        compare(flatten("package net.codjo.fubar.ontology;"
                        + " import jade.content.onto.*;"
                        + " import jade.content.schema.*;"
                        + " public class JadeProductOntology extends Ontology {"
                        + ""
                        + "    public static final String ONTOLOGY_NAME = \"Product\";"
                        + "    private static final Ontology theInstance = new JadeProductOntology();"
                        + "    public static Ontology getInstance() { return theInstance; }"
                        + ""
                        + "    private JadeProductOntology() {"
                        + "        super(ONTOLOGY_NAME, BasicOntology.getInstance(), new net.codjo.ontology.common.jade.JavaCollectionIntrospector());"
                        + "        try {"
                        + expectedBody.replaceAll("'", "\"")
                        + "        }"
                        + "        catch (Exception e) {"
                        + "            e.printStackTrace();"
                        + "        }"
                        + "    }"
                        + " }"),
                flatten(FileUtil.loadContent(new File(output, "JadeProductOntology.java"))));
    }


    private void assertResult(String expected, String input) throws Exception {
        XmlUtil.assertEquivalent(ProtegeGenerator.HEADER
                                 + expected
                                 + "<xs:element name='getProductAction' type='GetProductActionType'/>"
                                 + ProtegeGenerator.FOOTER,
                                 new ProtegeGenerator(input).generateXsd("GetProductAction"));
    }


    private static void generateJavaFiles(File output, String packageName) throws Exception {
        String input = FileUtil.loadContent(getSamplePontFile());
        new ProtegeGenerator(input).generateJavaFiles(packageName, output);

        new ProtegeGenerator(input)
              .generateOntologyFile(packageName, "Product",
                                    RelationshipManager.create()
                                          .define(that("GetProductAction").returnsOne("GetProductResponse")),
                                    output);
    }


    private void assertJavaFile(String expected, String fileName) throws IOException {
        File outputb = new File(generationDirectory, PACKAGE_DIRECTORY);
        compare(flatten(expected), flatten(FileUtil.loadContent(new File(outputb, fileName))));
    }


    private void generateJava(String pont, String packageName) throws IOException {
        File output = new File(generationDirectory, PACKAGE_DIRECTORY);
        new ProtegeGenerator(pont).generateJavaFiles(packageName, output);
    }


    public static void compare(String expected, String actual) {
        if (expected.equals(actual)) {
            return;
        }
        for (int i = 0; i <= expected.length(); i++) {
            if (!actual.startsWith(expected.substring(0, i))) {
                int min = Math.max(0, i - 30);
                String expect = "..." + expected.substring(min, Math.min(i + 30, expected.length())) + "...";
                String result = "..." + actual.substring(min, Math.min(i + 30, actual.length())) + "...";
                throw new AssertionFailedError("Comparaison"
                                               + "\n\texpected = " + expect
                                               + "\n\tactual   = " + result);
            }
        }
    }


    private static URL getSamplePontFile() throws MalformedURLException {
        Directory basedir = PathUtil.findBaseDirectory(ProtegeGeneratorTest.class);
        return new File(basedir, "src/test/sample/sample.pont").toURL();
    }


    /**
     * Mise à plat de la chaîne de charactère (sans saut de ligne).
     */
    public static String flatten(String str) {
        StringBuilder buffer = new StringBuilder();
        boolean previousWhite = true;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '\r' || ch == '\n') {
                ; // à ignorer
            }
            else if (Character.isWhitespace(ch) || Character.isSpaceChar(ch)) {
                if (!previousWhite) {
                    buffer.append(" ");
                }
                previousWhite = true;
            }
            else {
                buffer.append(ch);
                previousWhite = false;
            }
        }

        return buffer.toString();
    }
}
