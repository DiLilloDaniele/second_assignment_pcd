package ass02.view;
import ass02.ProjectAnalyzer;
import ass02.implementation.ProjectAnalyzerImpl;
import ass02.implementation.ProjectElemImpl;
import ass02.implementation.ProjectElemImpl.Type;
import ass02.passiveComponents.CountersMonitor;
import ass02.verticle.VerticleView;
import io.vertx.core.Vertx;

import javax.swing.*;

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

    public AnalyzerView() {
        countersMonitor = new CountersMonitor();
        f = new JFrame();//creating instance of JFrame
        f.getContentPane().setLayout(
            new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS)
        );
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
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.showSaveDialog(null);
            start(fc.getSelectedFile().getPath());
        });
        stop.addActionListener((e) -> {
            vertx.close();
        });
        b.setBounds(130, 100, 100, 40);

        f.getContentPane().add(classLabel);
        f.getContentPane().add(methodLabel);
        f.getContentPane().add(fieldLabel);
        f.getContentPane().add(packageLabel);
        f.getContentPane().add(interfaceLabel);
        f.getContentPane().add(enumLabel);
        f.getContentPane().add(b);
        f.getContentPane().add(stop);
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

    private void display_old() {
        f.getContentPane().removeAll();
        JLabel classLabel = new JLabel("# classes: " + countersMonitor.getNumClasses());
        JLabel methodLabel = new JLabel("# methods: " + countersMonitor.getNumMethods());
        JLabel fieldLabel = new JLabel("# fields: " + countersMonitor.getNumFields());
        JLabel packageLabel = new JLabel("# packages: " + countersMonitor.getNumPackages());
        JLabel interfaceLabel = new JLabel("# interfaces: " + countersMonitor.getNumInterfaces());
        JLabel enumLabel = new JLabel("# enums: " + countersMonitor.getNumEnums());
        JButton b = new JButton("Select folder");
        JButton stop = new JButton("Stop");
        b.addActionListener((e) -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.showSaveDialog(null);
            start(fc.getSelectedFile().getPath());
        });
        stop.addActionListener((e) -> {
            vertx.close();
        });
        b.setBounds(130, 100, 100, 40);

        f.getContentPane().add(classLabel);
        f.getContentPane().add(methodLabel);
        f.getContentPane().add(fieldLabel);
        f.getContentPane().add(packageLabel);
        f.getContentPane().add(interfaceLabel);
        f.getContentPane().add(enumLabel);
        f.getContentPane().add(b);
        f.getContentPane().add(stop);
        f.revalidate();
        f.repaint();
    }

    private void start(String path){
        vertx = Vertx.vertx();
        vertx.deployVerticle(new VerticleView(path, (i) -> {
            System.out.println("CALLBACK");
            switch(i.getTypeAsString()) {
                case Class:
                    countersMonitor.incClasses();
                    break;
                case Package:
                    countersMonitor.incPackages();
                    break;
                case Field:
                    countersMonitor.incFields();
                    break;
                case Interface:
                    countersMonitor.incInterfaces();
                    break;
                case Method:
                    countersMonitor.incMethods();
                    break;
                case Enum:
                    countersMonitor.incEnums();
                    break;
            }
            SwingUtilities.invokeLater(() -> {
                display();
            });
        }));
    }
}
