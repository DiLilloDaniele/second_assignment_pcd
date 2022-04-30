package ass02.implementation;

import ass02.ClassReport;
import ass02.FieldInfo;

public class FieldInfoImpl implements FieldInfo {

    private String name;
    private String fieldTypeFullName;
    private ClassReport parent;

    public FieldInfoImpl(String name, String fieldTypeFullName, ClassReport parent) {
        this.name = name;
        this.fieldTypeFullName = fieldTypeFullName;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFieldTypeFullName() {
        return fieldTypeFullName;
    }

    @Override
    public ClassReport getParent() {
        return parent;
    }
}
