package Principal;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class FileChooser extends JPanel implements ActionListener {

    private final JButton go;
    private JFileChooser chooser;
    private final String chooser_title = "Select a project directory";

    private JLabel projectName; //pointed by ProjectPanel.jl_path

    String path;

    public boolean chosen;

    public FileChooser() {
        go = new JButton("Choose");
        go.setBackground(new java.awt.Color(186, 195, 211));
        go.addActionListener(this);
        add(go);
        chosen =  false;
    }

    public void actionPerformed(ActionEvent e) {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
        chooser.setDialogTitle(chooser_title);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            projectName.setText(chooser.getSelectedFile().getName());
            path = chooser.getSelectedFile().getPath();
            chosen = true;
        } else {
            System.out.println("No Selection");
        }
    }


    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    public JButton getGo() {
        return this.go;
    }

    public void setProjectName(JLabel projectName) {
        this.projectName = projectName;
    }

    public String getPath() {
        return path;
    }

    public boolean isChosen() {
        return chosen;
    }
}