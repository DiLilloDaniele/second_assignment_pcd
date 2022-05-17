package ass02;

import ass02.implementation.ProjectAnalyzerImpl;
import ass02.implementation.ReactiveAnalyzerImpl;
import ass02.view.AnalyzerView;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

class MethodNameCollector extends VoidVisitorAdapter<List<String>> {
  public void visit(MethodDeclaration md, List<String> collector) {
	  super.visit(md, collector);
	  collector.add(md.getNameAsString());
  }
}

class FullCollector extends VoidVisitorAdapter<Void> {

	public void visit(PackageDeclaration fd, Void collector) {
		super.visit(fd, collector);
		System.out.println("PACKAGE");
		System.out.println(fd);
	}

	public void visit(ClassOrInterfaceDeclaration cd, Void collector) {
		super.visit(cd, collector);
		/**full name della classe o interfaccia
		 * sono le informazioni sulla classe o interfaccia
		 */
		System.out.println("CLASS OR INTERFACE");
		System.out.println(cd.getNameAsString());
	}
	
	public void visit(FieldDeclaration fd, Void collector) {
		super.visit(fd, collector);
		System.out.println("FIELD");
		fd.getVariables().forEach(i -> {
			System.out.println(i);
		});
		System.out.println("FIELD-MODIFIERS");
		System.out.println(fd.getModifiers());
		System.out.println("FIELD-TYPE");
		System.out.println(fd.getElementType());
	}

	public void visit(MethodDeclaration md, Void collector) {
		super.visit(md, collector);
		System.out.println("METHOD");
		System.out.println(md.getName());
	}
}


public class TestJavaParser {

	public static void main(String[] args) throws Exception {
		new AnalyzerView();
	}
}
