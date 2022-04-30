package ass02.utility;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ClassOrInterfaceChecker extends VoidVisitorAdapter<FileTypeImpl> {

    public void visit(ClassOrInterfaceDeclaration md, FileTypeImpl collector) {
        super.visit(md, collector);
        if(md.isInterface()) {
            collector.setAsInterface();
        } else {
            if(md.getNameAsString() == "main")
                collector.setAsMainClass();
            collector.setAsClass();
        }
    }

}
