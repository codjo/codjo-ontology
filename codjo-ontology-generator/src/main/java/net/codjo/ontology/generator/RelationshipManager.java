package net.codjo.ontology.generator;
import java.util.ArrayList;
import java.util.List;
/**
 *
 */
public class RelationshipManager {
    private List<Relationship> relationships = new ArrayList<Relationship>();


    public static RelationshipManager create() {
        return new RelationshipManager();
    }


    public RelationshipManager define(Relationship relation) {
        relationships.add(relation);
        return this;
    }


    public static Relationship that(String agentAction) {
        return new Relationship(agentAction);
    }


    public String generate() {
        if (relationships.size() == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (Relationship relationship : relationships) {
            result.append(relationship.getAction())
                  .append("Schema.setResult(")
                  .append(relationship.getResult())
                  .append("Schema,1,1);");
        }
        return result.toString();
    }


    public static class Relationship {
        private final String action;
        private String result;


        private Relationship(String agentAction) {
            this.action = agentAction;
        }


        public Relationship returnsOne(String resultTerm) {
            this.result = resultTerm;
            return this;
        }


        String getAction() {
            return action;
        }


        String getResult() {
            return result;
        }
    }
}
