package net.codjo.ontology.common.xml;
import net.codjo.test.common.XmlUtil;
import jade.content.Concept;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
/**
 *
 */
public class OntologyXmlizerTest extends TestCase {

    public void test_toXml() throws Exception {
        Todo first = new Todo();
        first.setTask("finir Prodoc");

        Todo second = new Todo();
        second.setTask("fêter ça");

        List<Todo> todos = new ArrayList<Todo>();
        todos.add(first);
        todos.add(second);

        TodoManager todoManager = new TodoManager();
        todoManager.setTodos(todos);

        OntologyConfiguration ontologyConfiguration = new OntologyConfiguration();
        ontologyConfiguration.addAlias("todoManager", TodoManager.class);
        ontologyConfiguration.addAlias("todo", Todo.class);
        ontologyConfiguration.setXsdFile("todo.xsd");

        XmlUtil.assertEquivalent(
              "<todoManager xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='todo.xsd'>"
              + "  <todos>"
              + "    <todo>"
              + "      <task>"
              + "        finir Prodoc"
              + "      </task>"
              + "    </todo>"
              + "    <todo>"
              + "      <task>"
              + "        fêter ça"
              + "      </task>"
              + "    </todo>"
              + "  </todos>"
              + "</todoManager>",
              OntologyXmlizer.toXml(todoManager, ontologyConfiguration));
    }


    public static class Todo implements Concept {
        private String task;


        public String getTask() {
            return task;
        }


        public void setTask(String task) {
            this.task = task;
        }
    }

    public static class TodoManager implements Concept {
        private List todos = new ArrayList();


        public List getTodos() {
            return todos;
        }


        public void setTodos(List todos) {
            this.todos = todos;
        }
    }
}
