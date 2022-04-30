package ass02;

import java.util.List;

public interface PackageReport {

	public List<ClassReport> getAllClasses();
	public List<InterfaceReport> getAllInterfaces();
	public List<PackageReport> getAllPackages();
	public String getFullPackageName();
	
}
