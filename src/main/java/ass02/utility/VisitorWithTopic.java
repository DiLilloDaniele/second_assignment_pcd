package ass02.utility;

import ass02.ProjectElem;
import ass02.implementation.ProjectElemImpl;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.vertx.core.eventbus.EventBus;

import java.util.function.Consumer;

public class VisitorWithTopic extends VoidVisitorAdapter<String> {

    private EventBus eventBus;

    public VisitorWithTopic(final EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void visit(ClassOrInterfaceDeclaration cd, String topic) {
        super.visit(cd, topic);
        String type = "";
        if(cd.isInterface()) {
            type = "Interface-" + cd.getNameAsString();
        } else {
            type = "Class-" + cd.getNameAsString();
        }
        this.eventBus.publish(topic, type);
    }

    public void visit(MethodDeclaration md, String topic) {
        super.visit(md, topic);
        this.eventBus.publish(topic, "Method-" + md.getNameAsString());
    }

    public void visit(FieldDeclaration fd, String topic) {
        super.visit(fd, topic);
        this.eventBus.publish(topic, "Field-" + fd.getVariables().get(0).getNameAsString());
    }

    public void visit(EnumDeclaration ed, String topic) {
        super.visit(ed, topic);
        this.eventBus.publish(topic, "Enum-" + ed.getNameAsString());
    }

}
