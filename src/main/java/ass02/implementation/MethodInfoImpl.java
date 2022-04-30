package ass02.implementation;

import ass02.ClassReport;
import ass02.MethodInfo;

public class MethodInfoImpl implements MethodInfo {

    private String name;
    private int beginLine;
    private int endLine;
    private ClassReport classReport;

    public MethodInfoImpl(final String name, final int beginLine, final int endLine, final ClassReport classReport) {
        this.name = name;
        this.beginLine = beginLine;
        this.endLine = endLine;
        this.classReport = classReport;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBeginLine(int beginLine) {
        this.beginLine = beginLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public void setClassReport(ClassReport classReport) {
        this.classReport = classReport;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSrcBeginLine() {
        return beginLine;
    }

    @Override
    public int getEndBeginLine() {
        return endLine;
    }

    @Override
    public ClassReport getParent() {
        return classReport;
    }
}
