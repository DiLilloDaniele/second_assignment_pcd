package ass02.implementation;

import ass02.ProjectElem;

public class ProjectElemImpl implements ProjectElem {

    public enum Type {
        None,
        Class,
        Interface,
        Package,
        Method,
        Field,
        Enum
    }

    private String name;
    private Type type;

    public ProjectElemImpl(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getNameAsString() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getTypeAsString() {
        return type.toString();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }
}
