package ass02.utility;

import ass02.ClassReport;
import ass02.MethodInfo;
import ass02.ProjectAnalyzer;
import ass02.implementation.*;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

public class ClassCollector extends VoidVisitorAdapter<ClassReport> {

    private boolean innerSearch = false;

    public void visit(PackageDeclaration fd, ClassReport collector) {
        super.visit(fd, collector);
        collector.setFullClassName(fd.getNameAsString());
    }

    public void visit(ClassOrInterfaceDeclaration cd, ClassReport collector) {
        if(cd.isInnerClass() && !innerSearch) {
            try {
                ClassCollector analyzer = new ClassCollector();
                ClassReport innerClass = new ClassReportImpl();
                analyzer.setAsInnerSearch();
                analyzer.visit(cd, innerClass);
                collector.addInnerClass(innerClass);
            }catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            super.visit(cd, collector);
            collector.setFullClassName(cd.getNameAsString());
            if(cd.getFullyQualifiedName().isPresent())
                collector.setSrcFullFileName(cd.getFullyQualifiedName().get());
        }
    }

    public void visit(MethodDeclaration md, ClassReport collector) {
        super.visit(md, collector);

        String name = md.getNameAsString();
        Optional<Position> begin = md.getBegin();
        Optional<Position> end = md.getEnd();
        MethodInfoImpl methodInfo = new MethodInfoImpl(name, begin.orElse(new Position(0,0)).line,
                end.orElse(new Position(0,0)).line, collector);
        collector.addMethodInfo(methodInfo);
    }

    public void visit(FieldDeclaration fd, ClassReport collector) {
        super.visit(fd, collector);
        String name = fd.getVariable(0).getNameAsString();
        String type = fd.getElementType().toString();
        FieldInfoImpl fieldInfo = new FieldInfoImpl(name, type, collector);
        collector.addFieldInfo(fieldInfo);
    }

    public void visit(EnumDeclaration ed, ClassReport collector) {
        super.visit(ed, collector);
        collector.addEnum(ed.getNameAsString());
    }

    public void setAsInnerSearch() {
        this.innerSearch = true;
    }

}
