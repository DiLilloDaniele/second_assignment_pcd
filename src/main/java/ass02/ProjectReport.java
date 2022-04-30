package ass02;

import java.util.List;

public interface ProjectReport {

	ClassReport getMainClass();

	void setMainClass(ClassReport classReport);
	
	List<ClassReport> getAllClasses();

	void addClassReport(ClassReport classReport);

	List<PackageReport> getAllPackages();

	void addPackageReport(PackageReport packageReport);
	
	ClassReport getClassReport(String fullClassName);

}
