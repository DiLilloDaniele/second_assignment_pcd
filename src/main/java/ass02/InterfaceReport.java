package ass02;

import java.util.ArrayList;
import java.util.List;

public interface InterfaceReport {

    String getInterfaceName();

    String getFullPath();

    List<String> getMethods();

    public void setInterfaceName(String interfaceName);

    public void setFullPath(String fullPath);

    public void setMethodsNames(final List<String> methods);

    public void addMethod(final String method);

}
