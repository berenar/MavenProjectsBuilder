package Principal;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;

public class FileChooser extends JPanel implements ActionListener {
    JButton go;
    JFileChooser chooser;
    String choosertitle;
    JPanel panell;

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int panell_width = screenSize.width/3;
    private int panell_heigh = screenSize.height/3;

    public static void main(String[] s) {
        JFrame frame = new JFrame("");
        FileChooser panel = new FileChooser();
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
        frame.getContentPane().add(panel, "Center");
        frame.setSize(panel.getPreferredSize());
        frame.setVisible(true);
    }

    public FileChooser() {
        //titol
        JLabel titol_app = new JLabel("Elegeix els projectes a compilar");
        titol_app.setBounds(((panell_width/2)-100), 0, 200, 50);

        go = new JButton("Elegeix");
        go.addActionListener(this);
        add(go);


    }

    public void actionPerformed(ActionEvent e) {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(choosertitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): "
                    + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : "
                    + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }
}