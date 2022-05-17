package ass02;

import java.util.List;

public interface ClassReport {

	String getFullClassName();
	
	String getSrcFullFileName();

	List<MethodInfo> getMethodsInfo();

	List<FieldInfo> getFieldsInfo();

	List<ClassReport> getInnerClasses();

	List<String> getEnumInfos();

	void setFullPath(String fullPath);

	void setFullClassName(String fullClassName);

	void addMethodInfo(MethodInfo methodInfo);

	void addFieldInfo(FieldInfo methodInfo);

	void setSrcFullFileName(String srcFullFileName);

	void setMethodInfos(List<MethodInfo> methodInfos);

	void setFieldInfos(List<FieldInfo> fieldInfos);

	void addInnerClass(ClassReport innerClass);

	void addEnum(String name);
	
}
