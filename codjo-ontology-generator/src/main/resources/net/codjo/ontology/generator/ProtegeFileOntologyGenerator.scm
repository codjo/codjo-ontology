(define (capitalize s)
  (string-append (char-upcase (string-ref s 0)) (substring s 1 (string-length s)))
)

(define (toOntologyType type-name)
    (case type-name
        ('INTEGER  "BasicOntology.INTEGER")
        ('STRING   "BasicOntology.STRING")
        ('BOOLEAN  "BasicOntology.BOOLEAN")
        (else type-name)
    )
)
(define (toOntologyCardinality cardinality)
    (case (second cardinality)
        ('0  "ObjectSchema.OPTIONAL")
        (else "ObjectSchema.MANDATORY")
    )
)

(define-macro (declareConcept defclass)
    (if (.equals (symbol->string (first defclass)) "defclass")
      { [(declareConceptImpl (second defclass) (third defclass))] }
      {}
      )
)
(define-macro (declareConceptImpl classname isA)
	(if (.contains (symbol->string classname) "TOP_LEVEL_SLOT_CLASS")
	{}
	(cond
        ((equal? '(is-a AgentAction) isA)
                {AgentActionSchema [classname]Schema = new AgentActionSchema("[classname]");
                 add([classname]Schema, [classname].class);
                 }
        )
        ((equal? '(is-a Concept) isA)
                {ConceptSchema [classname]Schema = new ConceptSchema("[classname]");
                 add([classname]Schema, [classname].class);
                 }
        )
        (else {Type inconnu [(second isA)]})
	)
	)
)

(define-macro (defclass classname isA role . other)
	(if (.contains (symbol->string classname) "TOP_LEVEL_SLOT_CLASS")
	{}
    {[(apply string-append (map (lambda (x) { [classname]Schema.[(eval x)]})  other))] }
    )
)

(define-macro (single-slot-default slotname type cardinality . other)
    {add("[slotname]",
                    (TermSchema)getSchema([(toOntologyType (second type))]),
                    [(toOntologyCardinality cardinality)]);
    }
)

(define-macro (single-slot-instance slotname type cardinality . other)
    {add("[slotname]",
                    [(second type)]Schema,
                    [(toOntologyCardinality cardinality)]);
    }
)
(define-macro (multislot slotname type allowed-classes . other )
    {add("[slotname]", [(second allowed-classes)]Schema, 0, ObjectSchema.UNLIMITED);
    }
)

(define-macro (single-slot . other)
     (cond
         ((member 'allowed-classes (third other))
               `(single-slot-instance ,(first other) ,(third other) ,(fourth other)  ,(list-tail other 4)))
         ((equal? '(allowed-values FALSE TRUE) (third other))
               `(single-slot-default ,(first other) (type BOOLEAN) ,(list-tail other 3)))
         (else `(single-slot-default ,(first other) ,(second other) , (third other) ,(list-tail other 2)))
     )
)

(define-macro (generateOntology ontologyName . other)
    (define capitalizedOntologyName (capitalize (symbol->string ontologyName)))

	(.generateFile SchemeUtil  {Jade[capitalizedOntologyName]Ontology}
	                           #{package #[(.getPackageName SchemeUtil)]#;
                                import jade.content.onto.*;
                                import jade.content.schema.*;

                                public class Jade#[capitalizedOntologyName]#Ontology extends Ontology {
                                    public static final String ONTOLOGY_NAME = "#[ontologyName]#";
                                    private static final Ontology theInstance = new Jade#[capitalizedOntologyName]#Ontology();

                                    public static Ontology getInstance() {
                                       return theInstance;
                                    }

                                    private Jade#[capitalizedOntologyName]#Ontology() {
                                        super(ONTOLOGY_NAME, BasicOntology.getInstance(), new net.codjo.ontology.common.jade.JavaCollectionIntrospector());
                                        try {
                                            #[(apply string-append (map declareConcept other))]#
                                            #[(apply string-append (map eval other))]#
                                            #[(.generate RelationshipManager)]#
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                            }}#
     )
	"ok"
)

; --------------------------------------
(define-macro (version . other) "")
(define-macro (build . other) "")
(define-macro (is-a . other) "")
(define-macro (role . other) "")
(define-macro (create-accessor . other) "")
