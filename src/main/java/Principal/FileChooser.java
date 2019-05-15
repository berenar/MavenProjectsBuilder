package Principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileChooser extends JPanel implements ActionListener {

    //FileChooser components
    private final JButton go;
    private JFileChooser chooser;



    private JTextField projectName; //pointed by ProjectPanel.jtf_path

    //FileChooser Title and path
    private final String chooser_title = "Select a project directory";

    private String path;

    //true if the project has been chosen
    private boolean chosen;

    private int retry;

    private Container parentContentPane;


    /**
     * Constructor for the FileChooser class
     */
    public FileChooser(Container parentContentPane) {
        go = new JButton("Local");
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
        retry++;
        if (retry >= 3) {
            //User has failed 3 attempts to select a project
            userFeedback("local");
        } else if (!projectName.getText().isEmpty()) {
            //User has manually introduced an URL
            path = projectName.getText();
            chosen = true;
        } else {
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
            chooser.setDialogTitle(chooser_title);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                path = chooser.getSelectedFile().getPath();
                projectName.setText(path);
                chosen = true;
                retry = 0;
            } else {
                System.out.println("No Selection");
            }
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
                retry = 0;
                break;
            case "compile":
                //Invoked using Compile all button
                JOptionPane.showMessageDialog(parentContentPane,
                        "Click Local or Git buttons to submit the URL",
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
    public void setPath(String path) {
        this.path = path;
    }

    public JTextField getProjectName() {
        return projectName;
    }

}