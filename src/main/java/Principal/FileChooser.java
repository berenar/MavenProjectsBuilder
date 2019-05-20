package Principal;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileChooser extends JPanel implements ActionListener {

    //FileChooser components
    private final JButton go;
    private JFileChooser chooser;

    //True if no other action needs to be performed
    private final boolean justChooseFile;

    //Project name
    private JTextField projectName; //pointed by ProjectPanel.jtfPath

    //Path project
    private String path;

    //true if the project has been chosen
    private boolean chosen;

    //For the user error control
    private int retryLocal;

    //Main panel
    private final Container parentContentPane;

    /**
     * Constructor for the FileChooser class
     */
    public FileChooser(Container parentContentPane, String buttonName, boolean justChooseFile) {
        go = new JButton(buttonName);
        this.justChooseFile = justChooseFile;
        go.addActionListener(this);
        add(go);
        chosen = false;
        this.parentContentPane = parentContentPane;
    }

    /**
     * Action of the File chooser
     *
     * @param e event
     */
    public void actionPerformed(ActionEvent e) {
        if (!justChooseFile) {
            retryLocal++;
            if (retryLocal >= 3) {
                //User has failed 3 attempts to select a project
                userFeedback("local");
            } else if (!projectName.getText().isEmpty()) {
                //User has manually introduced an URL
                path = projectName.getText();
                chosen = true;
            } else {
                chooserAction();
            }
        }
    }

    public boolean chooserAction() {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
        chooser.setDialogTitle("Select a directory");
        if (!justChooseFile) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getPath();
            chosen = true;
            if (!justChooseFile) {
                projectName.setText(path);
                retryLocal = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    public void userFeedback(String button) {
        switch (button) {
            case "local":
                //Invoked clicking Local button
                if (projectName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(parentContentPane,
                            "You have to select a project or input its path",
                            "Quick tip",
                            JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(parentContentPane,
                            "You have already selected the project: \n" + path,
                            "Quick tip",
                            JOptionPane.PLAIN_MESSAGE);
                }
                retryLocal = 0;
                break;
            case "compile":
                //Invoked using Compile all button
                JOptionPane.showMessageDialog(parentContentPane,
                        "Click Local or Clone buttons to submit the URL",
                        "Quick tip",
                        JOptionPane.PLAIN_MESSAGE);
                break;
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
     * Getter for the project name
     *
     * @return the project name
     */
    public JTextField getProjectName() {
        return projectName;
    }

    /**
     * Setter for the project name
     *
     * @param projectName project name
     */
    public void setProjectName(JTextField projectName) {
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
     * Setter for path
     *
     * @param path new path value
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Getter for chosen
     *
     * @return true if the project has been chosen
     */
    public boolean isChosen() {
        return chosen;
    }

    /**
     * Setter for chosen
     *
     * @param chosen the new value of chosen
     */
    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

}