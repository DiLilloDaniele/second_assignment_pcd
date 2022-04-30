package ass02;

import ass02.implementation.ProjectAnalyzerImpl;
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
		//CompilationUnit cu = StaticJavaParser.parse(new File("src/main/java/ass02/MyClass.java"));
		/*
		var methodNames = new ArrayList<String>();
		var methodNameCollector = new MethodNameCollector();
		methodNameCollector.visit(cu,methodNames);
		methodNames.forEach(n -> System.out.println("MethodNameCollected:" + n));
		 */
		//var fullc = new FullCollector();
		//fullc.visit(cu, null);


		/*ProjectAnalyzer analyzer = new ProjectAnalyzerImpl();
		Future<PackageReport> fut = analyzer.getPackageReport("src/main/java/ass02/");
		fut.onComplete((AsyncResult<PackageReport> res) -> {
			if(res == null)
				System.out.println("res è null");
			else {
				if(res.result() == null) {
					System.out.println("res.result() è null");
				}
			}
			System.out.println("FINITOOOOOOOOO: " + res.result().getAllClasses().size() + " - " + res.result().getAllInterfaces().size() + " - " + res.result().getAllPackages().size());
			res.result().getAllClasses().forEach(i -> {
				System.out.println("Classe: " + i.getFullClassName());
				System.out.println("--> " + i.getMethodsInfo().stream().map(s -> s.getName()).collect(Collectors.toList()));
				System.out.println("--> " + i.getFieldsInfo().stream().map(s -> s.getName()).collect(Collectors.toList()));
			});
			res.result().getAllInterfaces().forEach(i -> {
				System.out.println("Interfacce: " + i.getInterfaceName());
				System.out.println("--> " + i.getMethods());
			});
			res.result().getAllPackages().forEach(i -> {
				System.out.println("Package: " + i.getFullPackageName());
			});
		});*/


/*
		ProjectAnalyzer analyzer = new ProjectAnalyzerImpl();
		Future<ClassReport> fut = analyzer.getClassReport("src/main/java/ass02/MyClass.java");
		fut.onComplete((AsyncResult<ClassReport> res) -> {
			if(res == null)
				System.out.println("res è null");
			else {
				if(res.result() == null) {
					System.out.println("res.result() è null");
				}
			}
			System.out.println("FullClassName: " + res.result().getFullClassName());
			System.out.println("FullFileName: " + res.result().getSrcFullFileName());
			System.out.println("Fields: " + res.result().getFieldsInfo().stream().map(i -> i.getName()).collect(Collectors.toList()));
			System.out.println("Methods: " + res.result().getMethodsInfo().stream().map(i -> i.getName()).collect(Collectors.toList()));
			System.out.println("InnerClasses: " + res.result().getInnerClasses().stream().map(i -> i.getFullClassName()).collect(Collectors.toList()));
			System.out.println("Enums: " + res.result().getEnumInfos());
		});


/*
		ProjectAnalyzer analyzer = new ProjectAnalyzerImpl();
		analyzer.analyzeProject("src/main/java/ass02", (i) -> {
			System.out.println("Trovato elemento: (" + i.getNameAsString() + ", " + i.getTypeAsString() + ")");
		});
*/
	}
}
