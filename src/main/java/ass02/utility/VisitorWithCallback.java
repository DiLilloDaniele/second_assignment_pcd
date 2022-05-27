package ass02.utility;

import ass02.ProjectElem;
import ass02.implementation.ClassReportImpl;
import ass02.implementation.FieldInfoImpl;
import ass02.implementation.MethodInfoImpl;
import ass02.implementation.ProjectElemImpl;
import ass02.implementation.ProjectElemImpl.Type;
import com.github.javaparser.Position;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;
import java.util.function.Consumer;

public class VisitorWithCallback extends VoidVisitorAdapter<Consumer<ProjectElem>> {

    public void visit(ClassOrInterfaceDeclaration cd, Consumer<ProjectElem> callback) {
        super.visit(cd, callback);
        ProjectElem projectElem;
        Type type = Type.None;
        if(cd.isInterface()) {
            type = Type.Interface;
        } else {
            type = Type.Class;
        }
        projectElem = new ProjectElemImpl(cd.getNameAsString(), type);
        callback.accept(projectElem);
    }

    public void visit(MethodDeclaration md, Consumer<ProjectElem> callback) {
        super.visit(md, callback);
        ProjectElem projectElem = new ProjectElemImpl(md.getNameAsString(), Type.Method);
        callback.accept(projectElem);
    }

    public void visit(FieldDeclaration fd, Consumer<ProjectElem> callback) {
        super.visit(fd, callback);
        ProjectElem projectElem = new ProjectElemImpl(fd.getVariable(0).getNameAsString(), Type.Field);
        callback.accept(projectElem);
    }

    public void visit(EnumDeclaration ed, Consumer<ProjectElem> callback) {
        super.visit(ed, callback);
        ProjectElem projectElem = new ProjectElemImpl(ed.getNameAsString(), Type.Enum);
        callback.accept(projectElem);
    }

}
