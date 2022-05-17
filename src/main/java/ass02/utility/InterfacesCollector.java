package ass02.utility;

import ass02.InterfaceReport;
import ass02.MethodInfo;
import ass02.implementation.InterfaceReportImpl;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class InterfacesCollector extends VoidVisitorAdapter<InterfaceReport> {

    public void visit(PackageDeclaration fd, InterfaceReport collector) {
        super.visit(fd, collector);
        //collector.add(fd.getNameAsString());
    }

    public void visit(ClassOrInterfaceDeclaration cd, InterfaceReport collector) {
        super.visit(cd, collector);
        collector.setInterfaceName(cd.getNameAsString());
        Optional<String> opt = cd.getFullyQualifiedName();
        /*if(opt.isPresent())
            System.out.println("FULLY QUALIFIED NAME: " + opt.get());*/
    }

    public void visit(MethodDeclaration md, InterfaceReport collector) {
        super.visit(md, collector);
        //getBegin e getEnd per la position

        //MethodInfo methodInfo;
        collector.addMethod(md.getNameAsString());
    }
}