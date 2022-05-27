package ass02.utility;

import ass02.ClassReport;
import ass02.implementation.MethodInfoImpl;
import com.github.javaparser.Position;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class ClassOrInterfaceChecker extends VoidVisitorAdapter<FileTypeImpl> {

    public void visit(ClassOrInterfaceDeclaration md, FileTypeImpl collector) {
        super.visit(md, collector);
        if(md.isInterface()) {
            collector.setAsInterface();
        } else {
            collector.setAsClass();
        }
    }

    public void visit(MethodDeclaration md, FileTypeImpl collector) {
        super.visit(md, collector);

        String name = md.getNameAsString();
        if(name.equals("main")) {
            collector.setAsMainClass();
        }
    }

}
