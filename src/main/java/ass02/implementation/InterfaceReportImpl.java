package ass02.implementation;

import ass02.InterfaceReport;
import ass02.MethodInfo;

import java.util.ArrayList;
import java.util.List;

public class InterfaceReportImpl implements InterfaceReport {

    private String interfaceName;
    private String fullPath;
    private List<String> methods;

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public void setMethodsNames(final List<String> methods) {
        this.methods = methods;
    }

    public void addMethod(final String method) {
        this.methods.add(method);
    }

    public InterfaceReportImpl() {
        this.methods = new ArrayList<>();
    }

    @Override
    public String getInterfaceName() {
        return this.interfaceName;
    }

    @Override
    public String getFullPath() {
        return this.fullPath;
    }

    @Override
    public List<String> getMethods() {
        return this.methods;
    }
}
