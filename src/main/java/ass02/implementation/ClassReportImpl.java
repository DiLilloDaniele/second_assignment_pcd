package ass02.implementation;

import ass02.ClassReport;
import ass02.FieldInfo;
import ass02.MethodInfo;

import java.util.ArrayList;
import java.util.List;

public class ClassReportImpl implements ClassReport {

    private String fullClassName;
    private String srcFullFileName;
    private List<MethodInfo> methodInfos;
    private List<FieldInfo> fieldInfos;
    private String fullPath;
    private List<ClassReport> innerClasses;
    private List<String> enumInfos;

    public ClassReportImpl() {
        this.methodInfos = new ArrayList<>();
        this.fieldInfos = new ArrayList<>();
        this.innerClasses = new ArrayList<>();
        this.enumInfos = new ArrayList<>();
    }

    @Override
    public String getFullClassName() {
        return fullClassName;
    }

    @Override
    public String getSrcFullFileName() {
        return srcFullFileName;
    }

    @Override
    public List<MethodInfo> getMethodsInfo() {
        return methodInfos;
    }

    @Override
    public List<FieldInfo> getFieldsInfo() {
        return fieldInfos;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public List<MethodInfo> getMethodInfos() {
        return methodInfos;
    }

    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public void addMethodInfo(MethodInfo methodInfo) {
        this.methodInfos.add(methodInfo);
    }

    public void addFieldInfo(FieldInfo methodInfo) {
        this.fieldInfos.add(methodInfo);
    }

    public void setSrcFullFileName(String srcFullFileName) {
        this.srcFullFileName = srcFullFileName;
    }

    public void setMethodInfos(List<MethodInfo> methodInfos) {
        this.methodInfos = methodInfos;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    public void addInnerClass(ClassReport innerClass) {
        this.innerClasses.add(innerClass);
    }

    public List<ClassReport> getInnerClasses() {
        return innerClasses;
    }

    public void addEnum(String name) {
        this.enumInfos.add(name);
    }

    public List<String> getEnumInfos() {
        return enumInfos;
    }
}
