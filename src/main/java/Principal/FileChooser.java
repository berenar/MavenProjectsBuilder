package Principal;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileChooser extends JPanel implements ActionListener {

    //FileChooser components
    private final JButton go;
    private JFileChooser chooser;
    private JLabel projectName; //pointed by ProjectPanel.jl_path

    //FileChooser Title and path
    private final String chooser_title = "Select a project directory";
    private String path;

    //true if the project has been chosen
    private boolean chosen;

    /**
     * Constructor for the FileChooser class
     */
    public FileChooser() {
        go = new JButton("Local");
        go.addActionListener(this);
        add(go);
        chosen = false;
    }

    /**
     * Action of the File chooser
     *
     * @param e event
     */
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

    /**
     * Preferred size of the File Chooser
     *
     * @return size
     */
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    /**
     * Getter for the go button (Choose)
     *
     * @return go button
     */
    public JButton getGo() {
        return this.go;
    }

    /**
     * Setter for the project name
     *
     * @param projectName project name
     */
    public void setProjectName(JLabel projectName) {
        this.projectName = projectName;
    }

    /**
     * Getter for the project path
     *
     * @return project path
     */
    public String getPath() {
        return path;
    }

    /**
     * Getter for chosen
     *
     * @return true if the project has been chosen
     */
    public boolean isChosen() {
        return chosen;
    }
}