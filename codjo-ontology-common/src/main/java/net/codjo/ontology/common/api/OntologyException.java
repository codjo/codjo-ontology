package net.codjo.ontology.common.api;
/**
 *
 */
public class OntologyException extends Exception {
    public OntologyException(String message) {
        super(message);
    }


    public OntologyException(Exception cause) {
        super(cause);
    }
}
