package ass02;

import ass02.implementation.ProjectAnalyzerImpl;
import ass02.implementation.ReactiveAnalyzerImpl;
import ass02.passiveComponents.CountersMonitor;
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
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class TestJavaParser {
	public static void main(String[] args) throws Exception {
		new AnalyzerView();
	}
}
