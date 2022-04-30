package ass02.utility;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class MethodNameCollector extends VoidVisitorAdapter<List<String>> {
    public void visit(MethodDeclaration md, List<String> collector) {
        super.visit(md, collector);
        collector.add(md.getNameAsString());
    }
}
