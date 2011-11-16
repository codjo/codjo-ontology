(define (capitalize s)
  (string-append (char-upcase (string-ref s 0)) (substring s 1 (string-length s)))
)

(define (toJavaType type-name)
    (case type-name
        ('INTEGER  "int")
        ('STRING   "String")
        ('BOOLEAN  "boolean")
        (else type-name)
    )
)

(define-macro (single-slot-default slotname type . other)
    {
    private [(toJavaType (second type))] [slotname];
    public void set[(capitalize (symbol->string slotname))]([(toJavaType (second type))] value) \{ this.[slotname] = value; \}
    public [(toJavaType (second type))] get[(capitalize (symbol->string slotname))]() \{ return this.[slotname]; \}
    }
)

(define-macro (single-slot . other)
     (cond
         ((member 'allowed-classes (third other))
               `(single-slot-default ,(first other) ,(third other) ,(fourth other)  ,(list-tail other 4)))
         ((equal? '(allowed-values FALSE TRUE) (third other))
               `(single-slot-default ,(first other) (type BOOLEAN) ,(list-tail other 3)))
         (else `(single-slot-default ,(first other) ,(second other) ,(list-tail other 2)))
     )
)

(define-macro (multislot slotname type allowed-classes . other )
    {
    private java.util.List<[(toJavaType (second allowed-classes))]> [slotname];
    public void set[(capitalize (symbol->string slotname))](java.util.List<[(toJavaType (second allowed-classes))]> value) \{ this.[slotname] = value; \}
    public java.util.List<[(toJavaType (second allowed-classes))]> get[(capitalize (symbol->string slotname))]() \{ return this.[slotname]; \}
    }
)

(define-macro (toJava classname isA . other )
    {package [(.getPackageName SchemeUtil)];
    import net.codjo.ontology.common.api.[(second isA)];
    public class [classname] implements [(second isA)] \{
          [(apply string-append (map eval other))]
    \}}
)

(define-macro (defclass classname isA . other)
	(if (.contains (symbol->string classname) "TOP_LEVEL_SLOT_CLASS")
	{}
	(.generateFile SchemeUtil (symbol->string classname) 
	                          {
	                          package [(.getPackageName SchemeUtil)];
	                          import net.codjo.ontology.common.api.[(second isA)];
	                          public class [classname] implements [(second isA)] \{
                                [(apply string-append (map eval other))]
                              \}
                              }
     )
	)
)

(define-macro (generateAll . other)
	(map eval other)
	"ok"
)

; --------------------------------------
(define-macro (version . other) "")
(define-macro (build . other) "")
(define-macro (role . other) "")
