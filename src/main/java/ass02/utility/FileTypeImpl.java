package ass02.utility;

public class FileTypeImpl {
    private enum FileType {
        None,
        Class,
        Interface
    }
    public FileType fileType = FileType.None;
    private boolean isMainClass = false;

    public void setAsClass() {
        this.fileType = FileType.Class;
    }

    public void setAsInterface() {
        this.fileType = FileType.Interface;
    }

    public String toString() {
        return this.fileType.toString();
    }

    public boolean isInterface() {
        return this.fileType == FileType.Interface;
    }

    public void setAsMainClass() {
        this.isMainClass = true;
    }

    public boolean isMainClass() {
        return this.isMainClass;
    }
}
