package Principal;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class FileChooser extends JPanel implements ActionListener {

    private JButton go;
    private JFileChooser chooser;
    private String choosertitle;

    private JLabel fc_jl_path;


    public FileChooser() {
        go = new JButton("Choose");
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
            fc_jl_path.setText(chooser.getSelectedFile().getPath());
        } else {
            System.out.println("No Selection ");
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    public JButton getGo() {
        return this.go;
    }

    public void setFc_jl_path(JLabel fc_jl_path) {
        this.fc_jl_path = fc_jl_path;
    }

    public JLabel getFc_jl_path() {
        return fc_jl_path;
    }


}