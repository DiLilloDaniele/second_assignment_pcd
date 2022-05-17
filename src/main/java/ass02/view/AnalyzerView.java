package ass02.view;
import ass02.ProjectElem;
import ass02.ReactiveAnalyzer;
import ass02.implementation.ProjectElemImpl;
import ass02.implementation.ReactiveAnalyzerImpl;
import ass02.implementation.ReactiveAnalyzerPubSub;
import ass02.implementation.ReactiveAnalyzerWithEmitter;
import ass02.passiveComponents.CountersMonitor;
import ass02.verticle.AnalyzerVerticle;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AnalyzerView {

    private JFrame f;
    private CountersMonitor countersMonitor;
    private Vertx vertx;
    JLabel classLabel;
    JLabel methodLabel;
    JLabel fieldLabel;
    JLabel packageLabel;
    JLabel interfaceLabel;
    JLabel enumLabel;

    DefaultListModel<String> listClasses;
    DefaultListModel<String> listMethods;
    DefaultListModel<String> listFields;
    DefaultListModel<String> listEnums;
    DefaultListModel<String> listInterfaces;
    DefaultListModel<String> listPackages;

    AnalyzerVerticle verticle;

    public AnalyzerView() {
        countersMonitor = new CountersMonitor();
        f = new JFrame();//creating instance of JFrame
        f.getContentPane().setLayout(
            new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS)
        );

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosed(e);
                System.out.println("Mi sto chiudendo");
                if(vertx != null)
                    vertx.close();
                System.exit(0);
            }
        });

        listClasses = new DefaultListModel<>();
        listClasses.addElement("CLASSES");
        listClasses.addElement(" ");
        listEnums = new DefaultListModel<>();
        listEnums.addElement("ENUMS");
        listEnums.addElement(" ");
        listFields = new DefaultListModel<>();
        listFields.addElement("FIELDS");
        listFields.addElement(" ");
        listInterfaces = new DefaultListModel<>();
        listInterfaces.addElement("INTERFACES");
        listInterfaces.addElement(" ");
        listMethods = new DefaultListModel<>();
        listMethods.addElement("METHODS");
        listMethods.addElement(" ");
        listPackages = new DefaultListModel<>();
        listPackages.addElement("PACKAGES");
        listPackages.addElement(" ");

        classLabel = new JLabel("# classes: " + countersMonitor.getNumClasses());
        methodLabel = new JLabel("# methods: " + countersMonitor.getNumMethods());
        fieldLabel = new JLabel("# fields: " + countersMonitor.getNumFields());
        packageLabel = new JLabel("# packages: " + countersMonitor.getNumPackages());
        interfaceLabel = new JLabel("# interfaces: " + countersMonitor.getNumInterfaces());
        enumLabel = new JLabel("# enums: " + countersMonitor.getNumEnums());
        JButton b = new JButton("Select folder");
        JButton stop = new JButton("Stop");
        b.addActionListener((e) -> {
            JFileChooser fc = new JFileChooser();
            File workingDirectory = new File(System.getProperty("user.dir"));
            fc.setCurrentDirectory(workingDirectory);

            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.showSaveDialog(null);
            start(fc.getSelectedFile().getPath());
            //rxStart(fc.getSelectedFile().getPath());
        });
        stop.addActionListener((e) -> {
            if(vertx != null) {
                try {
                    countersMonitor.close();
                    verticle.stop();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.X_AXIS));
        listPane.setAlignmentY(JPanel.TOP_ALIGNMENT);

        JList j_listClass = new JList(listClasses);
        listPane.add(new JScrollPane(j_listClass));
        j_listClass.setBorder(BorderFactory.createEmptyBorder(0, 0,0,20));
        JList j_listMethod = new JList(listMethods);
        listPane.add(new JScrollPane(j_listMethod));
        j_listMethod.setBorder(BorderFactory.createEmptyBorder(0, 0,0,20));
        JList j_listField = new JList(listFields);
        listPane.add(new JScrollPane(j_listField));
        j_listField.setBorder(BorderFactory.createEmptyBorder(0, 0,0,20));
        JList j_listPackage = new JList(listPackages);
        listPane.add(new JScrollPane(j_listPackage));
        j_listPackage.setBorder(BorderFactory.createEmptyBorder(0, 0,0,20));
        JList j_listInterface = new JList(listInterfaces);
        listPane.add(new JScrollPane(j_listInterface));
        j_listInterface.setBorder(BorderFactory.createEmptyBorder(0, 0,0,20));
        JList j_listEnum = new JList(listEnums);
        listPane.add(new JScrollPane(j_listEnum));
        j_listEnum.setBorder(BorderFactory.createEmptyBorder(0, 0,0,20));


        b.setBounds(130, 100, 100, 40);

        f.getContentPane().add(classLabel);
        f.getContentPane().add(methodLabel);
        f.getContentPane().add(fieldLabel);
        f.getContentPane().add(packageLabel);
        f.getContentPane().add(interfaceLabel);
        f.getContentPane().add(enumLabel);

        f.getContentPane().add(b);
        f.getContentPane().add(stop);
        f.getContentPane().add(listPane);
        f.setSize(400, 500);//400 width and 500 height
        f.setVisible(true);//making the frame visible
    }

    private void display() {
        classLabel.setText("# classes: " + countersMonitor.getNumClasses());
        methodLabel.setText("# methods: " + countersMonitor.getNumMethods());
        fieldLabel.setText("# fields: " + countersMonitor.getNumFields());
        packageLabel.setText("# packages: " + countersMonitor.getNumPackages());
        interfaceLabel.setText("# interfaces: " + countersMonitor.getNumInterfaces());
        enumLabel.setText("# enums: " + countersMonitor.getNumEnums());
    }

    private void start(String path){
        vertx = Vertx.vertx();
        EventBus eb = vertx.eventBus();
        Consumer<ProjectElem> callback = (i) -> {
            eb.publish("update-gui", i.getTypeAsString() + "." + i.getNameAsString());
        };
        eb.consumer("update-gui", message -> {
            String temp = message.body().toString();
            String[] elementsParsed = temp.split("-");
            DefaultListModel<String> model = null;
            //System.out.println("Size: " + elementsParsed.length);
            switch(elementsParsed[0]) {
                case "Class":
                    countersMonitor.incClasses();
                    model = listClasses;
                    break;
                case "Package":
                    countersMonitor.incPackages();
                    model = listPackages;
                    break;
                case "Field":
                    countersMonitor.incFields();
                    model = listFields;
                    break;
                case "Interface":
                    countersMonitor.incInterfaces();
                    model = listInterfaces;
                    break;
                case "Method":
                    countersMonitor.incMethods();
                    model = listMethods;
                    break;
                case "Enum":
                    countersMonitor.incEnums();
                    model = listEnums;
                    break;
            }
            DefaultListModel<String> finalModel = model;
            SwingUtilities.invokeLater(() -> {
                if(finalModel != null)
                    finalModel.addElement(elementsParsed[1]);
                display();
            });
        });
        verticle = new AnalyzerVerticle(path, callback, countersMonitor);
        vertx.deployVerticle(verticle);
    }

    private void rxStart(String path){
        //TODO INDIVIDUARE COSE IN COMUNE CON start(...)
        //TODO stop?
        PublishSubject<ProjectElem> source = PublishSubject.<ProjectElem>create();
        source.subscribe((s) -> {
            //System.out.println(Thread.currentThread().getName() + ": "+ s.getNameAsString());
            DefaultListModel<String> model = null;
            String type = s.getTypeAsString();
            String name = s.getNameAsString();
            switch(type) {
                case "Class":
                    countersMonitor.incClasses();
                    model = listClasses;
                    break;
                case "Package":
                    countersMonitor.incPackages();
                    model = listPackages;
                    break;
                case "Field":
                    countersMonitor.incFields();
                    model = listFields;
                    break;
                case "Interface":
                    countersMonitor.incInterfaces();
                    model = listInterfaces;
                    break;
                case "Method":
                    countersMonitor.incMethods();
                    model = listMethods;
                    break;
                case "Enum":
                    countersMonitor.incEnums();
                    model = listEnums;
                    break;
            }

            DefaultListModel<String> finalModel = model;
            SwingUtilities.invokeLater(() -> {
                if(finalModel != null)
                    finalModel.addElement(name);
                display();
            });
        }, Throwable::printStackTrace);

        ReactiveAnalyzer analyzer = new ReactiveAnalyzerPubSub();
        analyzer.analyzeProject(path, source);
    }

}
