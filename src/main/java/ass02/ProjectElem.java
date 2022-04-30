package ass02;

import ass02.implementation.ProjectElemImpl.Type;

public interface ProjectElem {

    String getNameAsString();

    Type getTypeAsString();

    void setName(String name);

    void setType(Type type);

}
