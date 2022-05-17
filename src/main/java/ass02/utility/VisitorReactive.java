package ass02.utility;

import ass02.ProjectElem;
import ass02.implementation.ProjectElemImpl;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.function.Consumer;

public class VisitorReactive extends VoidVisitorAdapter<PublishSubject<ProjectElem>>{

    public void visit(ClassOrInterfaceDeclaration cd, PublishSubject<ProjectElem> source) {
        super.visit(cd, source);
        ProjectElem projectElem;
        ProjectElemImpl.Type type = ProjectElemImpl.Type.None;
        if(cd.isInterface()) {
            type = ProjectElemImpl.Type.Interface;
        } else {
            type = ProjectElemImpl.Type.Class;
        }
        projectElem = new ProjectElemImpl(cd.getNameAsString(), type);
        source.onNext(projectElem);
    }

    public void visit(MethodDeclaration md, PublishSubject<ProjectElem> source) {
        super.visit(md, source);
        ProjectElem projectElem = new ProjectElemImpl(md.getNameAsString(), ProjectElemImpl.Type.Method);
        source.onNext(projectElem);
    }

    public void visit(FieldDeclaration fd, PublishSubject<ProjectElem> source) {
        super.visit(fd, source);
        ProjectElem projectElem = new ProjectElemImpl(fd.getVariable(0).getNameAsString(), ProjectElemImpl.Type.Field);
        source.onNext(projectElem);
    }

    public void visit(EnumDeclaration ed, PublishSubject<ProjectElem> source) {
        super.visit(ed, source);
        ProjectElem projectElem = new ProjectElemImpl(ed.getNameAsString(), ProjectElemImpl.Type.Enum);
        source.onNext(projectElem);
    }


}
