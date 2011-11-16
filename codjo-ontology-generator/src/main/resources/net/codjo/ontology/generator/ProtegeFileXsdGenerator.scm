(define (protege-type->xsd-type type-name)
    (case type-name
        ('INTEGER  "xs:int")
        ('STRING   "xs:string")
        ('BOOLEAN  "xs:boolean")
    )
)

(define-macro (type type-name)
    {type='[(protege-type->xsd-type type-name)]'}
)

(define-macro (allowed-classes class-name)
    {type='[class-name]Type'}
)

(define (make-enumeration-value val)
    {<xs:enumeration value='[val]'/>}
)

(define-macro (allowed-values . other)
    {<xs:simpleType>
         <xs:restriction base='xs:string'>
             [(apply string-append (map make-enumeration-value other))]
         </xs:restriction>
     </xs:simpleType>
    }
)

(define-macro (cardinality  min-occurs max-occurs)
    {minOccurs='[min-occurs]' maxOccurs='[max-occurs]'}
)

(define-macro (single-slot-default slotname type cardinality . other)
    {<xs:element [(eval cardinality)] name='[slotname]' [(eval type)] />}
)

(define-macro (single-slot-restricted slotname allowed-values cardinality . other)
    {<xs:element [(eval cardinality)] name='[slotname]'>
         [(eval allowed-values)]
     </xs:element> }
)

(define-macro (single-slot . other)
     (cond
         ((member 'allowed-classes (third other))
               `(single-slot-default ,(first other) ,(third other) ,(fourth other)  ,(list-tail other 4)))
         ((equal? '(allowed-values FALSE TRUE) (third other))
               `(single-slot-default ,(first other) (type BOOLEAN) ,(fourth other)  ,(list-tail other 4)))
         ((member 'allowed-values (third other))
               `(single-slot-restricted ,(first other) ,(third other) ,(fourth other)  ,(list-tail other 4)))
         (else `(single-slot-default ,(first other) ,(second other) ,(third other)  ,(list-tail other 3)))
     )
)

(define-macro (multislot slotname type allowed-classes . other )
    {<xs:element name="[slotname]" minOccurs="0">
         <xs:complexType>
             <xs:sequence minOccurs="0" maxOccurs="unbounded">
                 <xs:element name="[(uncapitalize (symbol->string (second allowed-classes)))]" type="[(second allowed-classes)]Type"/>
             </xs:sequence>
         </xs:complexType>
     </xs:element>}
)

(define (uncapitalize s)
  (string-append (char-downcase (string-ref s 0)) (substring s 1 (string-length s)))
)

(define-macro (defclass classname . other)
	(if (.contains (symbol->string classname) "TOP_LEVEL_SLOT_CLASS")
	{}
	{<xs:complexType name='[classname]Type'>
	    <xs:sequence>
		  [(apply string-append (map eval other))]
		</xs:sequence>
	 </xs:complexType>
	})
)

(define-macro (generateXsd . other)
	(apply string-append (map eval other))
)

; --------------------------------------
(define-macro (version . other) "")
(define-macro (build . other) "")
(define-macro (is-a . other) "")
(define-macro (role . other) "")
(define-macro (create-accessor . other) "")

