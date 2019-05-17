package Principal;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

import static Principal.Constants.*;

class Main extends JFrame {
    //Array of ProjectPanel objects
    private final ArrayList<ProjectPanel> selectedProjects = new ArrayList<>();

    //JFrame size
    private final int panelWidth = (Toolkit.getDefaultToolkit().getScreenSize().width / 3) + 150;
    private int panelHeight = 150;

    //Swing components
    private Container contentPane;
    private ProjectPanel projectPanel;
    private JButton addProject;
    private JButton compile;
    private JButton reset;
    private JButton save;

    //to know if there's any project being compiled
    private boolean compiling = false;

    //to know if all projects compiled
    private boolean success = true;

    private int retryCompile;

    /*-------------------------------------------------------------------------------------------*/
    /*--------------------------------------- GUI METHODS ---------------------------------------*/
    /*-------------------------------------------------------------------------------------------*/

    /**
     * Main GUI method, sets the basic layout of the app.
     */
    private void initUI() {
        contentPane = getContentPane();
        contentPane.setLayout(null);

        //TITLE
        JLabel titleApp = new JLabel("Introduce a project path manually (and click Local) or click " +
                "one of the two buttons to select a project");
        titleApp.setBounds(100, 0, panelWidth, 50);
        contentPane.add(titleApp);

        //PROJECT PANEL
        nouProjectPan();

        //ADD PROJECT BUTTON
        nouAddProject();

        //COMPILE BUTTON
        nouCompile();

        //SAVE BUTTON
        nouSave();

        //RESET BUTTON
        nouReset();

        //CONFIGURE JFRAME
        updFrameSize();
        contentPane.setBackground(Color.WHITE);
        //this.setResizable(false);
        this.setLocationRelativeTo(null);//null: centers window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("mvnCompiler 1.4");
        try {
            //noinspection ConstantConditions
            this.setIconImage(ImageIO.read(getClass().getClassLoader().getResource("mvn_logo_2.png")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Error reading image: mvn_logo_2.png");
            System.exit(1);
        }
    }

    /**
     * Adds a new Project panel.
     */
    private void nouProjectPan() {
        projectPanel = new ProjectPanel(compiling, contentPane);
        projectPanel.configureProjectPan(selectedProjects.size() + 1);
        projectPanel.addProjectPan(contentPane);
        selectedProjects.add(projectPanel);
        panelHeight += projectPanel.getJlPathHeight() + 20;
        updFrameSize();
    }

    /**
     * Adds a new Add Project button (+).
     */
    private void nouAddProject() {
        addProject = new JButton("+");
        addProject.setBorderPainted(false);
        addProject.setMargin(new Insets(0, 0, 0, 0));
        addProject.setFont(new Font("Arial", Font.PLAIN, 20));
        addProject.setBounds(50, panelHeight - 100, squareComponent, squareComponent);
        addProject.setBackground(moreBlue);
        contentPane.add(addProject);

        addProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!compiling) {
                    boolean temporaryRemoved = false;
                    nouProjectPan();
                    if (out.isOutputVisible()) {
                        temporaryRemoved = true;
                        panelHeight = out.removeOutput(contentPane, panelHeight);
                        updFrameSize();
                    }
                    reSetBounds();
                    if (temporaryRemoved) {
                        panelHeight = out.addOutput(contentPane, panelHeight, panelWidth);
                        updFrameSize();
                    }
                }
            }
        });
    }

    /**
     * Adds a new compile button (Compile all).
     */
    private void nouCompile() {
        compile = new JButton("Compile all");
        compile.setBorderPainted(false);
        compile.setFont(new Font("Arial", Font.PLAIN, 20));
        compile.setBounds(160 + squareComponent, panelHeight - 100, compileWidth, componentHeight);
        compile.setBackground(moreBlue);
        contentPane.add(compile);

        compile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!compiling) {
                    if (anyProjectIsCloning()) {
                        int reply = JOptionPane.showConfirmDialog(contentPane,
                                "There are projects being cloned, are you sure you want to start compiling?",
                                "Hey",
                                JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                            preCompile();
                        }
                    } else {
                        preCompile();
                    }
                }
            }
        });
    }

    /**
     * Creates the Save button
     * It's action is to save what the user introduced in the app.
     */
    private void nouSave() {
        save = new JButton("Save");
        save.setBorderPainted(false);
        save.setMargin(new Insets(0, 0, 0, 0));
        save.setBounds(500, panelHeight - 100, buttonWidth, componentHeight);
        save.setBackground(moreBlue);
        contentPane.add(save);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane.showInputDialog(contentPane, "Name the file");
                File f = new File(fileName + ".txt");

                if (f.exists() && !f.isDirectory()) {
                    int reply = JOptionPane.showConfirmDialog(contentPane,
                            "File already exists, do you want to overwrite it?",
                            "Warning", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        writeFile(fileName);
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Nothing saved");
                    }
                } else {
                    writeFile(fileName);
                }
            }
        });
    }

    /**
     * Writes a file with all project paths.
     *
     * @param fileName name of the file
     */
    private void writeFile(String fileName) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName + ".txt", "UTF-8");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < selectedProjects.size(); i++) {
            writer.println(selectedProjects.get(i).getJtfPath().getText());
        }
        writer.close();
    }

    /**
     * Creates the Clear projects button.
     * It's action is to reset the program.
     */
    private void nouReset() {
        reset = new JButton("Reset");
        reset.setBorderPainted(false);
        reset.setMargin(new Insets(0, 0, 0, 0));
        reset.setBounds(save.getBounds().x + buttonWidth + 10, panelHeight - 100, buttonWidth, componentHeight);
        reset.setBackground(dangerRed);
        contentPane.add(reset);

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!compiling && !anyProjectIsCloning()) {
                    int reply1 = JOptionPane.showConfirmDialog(contentPane,
                            "You are trying to reset the app to it's initial state, are you sure?",
                            "Be careful!",
                            JOptionPane.YES_NO_OPTION);
                    if (reply1 == JOptionPane.YES_OPTION) {
                        int reply2 = JOptionPane.showConfirmDialog(contentPane,
                                "There is no coming back, ARE YOU SURE?",
                                "BE CAREFUL!",
                                JOptionPane.YES_NO_OPTION);
                        if (reply2 == JOptionPane.YES_OPTION) {
                            cleanEverything();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane,
                            "You can't reset the program while cloning or compiling a project",
                            "Forbidden",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void cleanEverything() {
        //delete temporal directory
        dispose();
        String tempPath = System.getProperty("user.dir") + tempDir;
        if (deleteDirectory(new File(tempPath))) {
            JOptionPane.showMessageDialog(contentPane,
                    "The folder with temporal files may have " +
                            "not been deleted, you might want to delete it yourself",
                    "Hey",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        Main ex = new Main();
        ex.setVisible(true);
    }

    /**
     * Recursively deletes a directory and it's contents
     *
     * @param directoryToBeDeleted the desired directory
     * @return if it could be deleted
     */
    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    /**
     * Updates addProject and compile buttons bounds to match a new screen size.
     */
    private void reSetBounds() {
        addProject.setBounds(50, panelHeight - 100, squareComponent, squareComponent);
        compile.setBounds(160 + squareComponent, panelHeight - 100, compileWidth, componentHeight);
        save.setBounds(500, panelHeight - 100, buttonWidth, componentHeight);
        reset.setBounds(save.getBounds().x + buttonWidth + 10, panelHeight - 100, buttonWidth, componentHeight);
    }

    /**
     * Sets all project ticks to false for a new compilation.
     */
    private void allTicksToFalse() {
        for (int i = 0; i < selectedProjects.size(); i++) {
            selectedProjects.get(i).getTickLabel().setVisible(false);
        }
    }

    /**
     * Paint selected projects to be compiled in a darker color.
     */
    private void colorizeSelected() {
        for (int i = 0; i < selectedProjects.size(); i++) {
            if (selectedProjects.get(i).getFc().isChosen()) {
                selectedProjects.get(i).colorizeProjectPane();
            }
        }
    }

    /**
     * Updates the JFrame size with the current values for the width and height.
     */
    private void updFrameSize() {
        this.setSize(panelWidth, panelHeight);
    }



    /*-------------------------------------------------------------------------------------------*/
    /*----------------------------------- COMPILATION METHODS -----------------------------------*/
    /*-------------------------------------------------------------------------------------------*/

    /**
     * Checks a couple of things before compilation.
     */
    private void preCompile() {
        retryCompile++;
        if (retryCompile >= 3) {
            projectPanel.getFc().userFeedback("compile");
            retryCompile = 0;
        } else if (!allEmptyProjects()) {
            //if the output is not visible, set it to visible
            if (!out.isOutputVisible()) {

                //Thread for the output
                Thread tOut = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        panelHeight = out.addOutput(contentPane, panelHeight, panelWidth);
                        updFrameSize();
                    }
                });
                tOut.start();
            }

            //Thread for the compilation
            Thread tCompile = new Thread(new Runnable() {
                @Override
                public void run() {
                    //reset success value
                    success = true;
                    selectProjects();
                    allTicksToFalse();
                    colorizeSelected();
                    compileChosen();
                }
            });
            tCompile.start();
        }
    }

    /**
     * Executes the compile command and shows up a success message.
     */
    private void compileChosen() {
        compiling = true;
        compile.setText("Compiling...");
        ProcessBuilder pb = new ProcessBuilder();
        for (int i = 0; i < selectedProjects.size(); i++) {
            if (selectedProjects.get(i).getFc().isChosen()) {
                String path = selectedProjects.get(i).getFc().getPath();
                try {
                    pb.executeCommand(out, "cd " + "\"" + path + "\"" + " && " + compileCommand,
                            getContentPane(), selectedProjects.get(i).getId());
                } catch (Exception e) {
                    contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    JOptionPane.showMessageDialog(contentPane,
                            "Error compiling project number " + selectedProjects.get(i).getId(),
                            "Compilation halted",
                            JOptionPane.ERROR_MESSAGE);
                    success = false;
                    break;
                }
                selectedProjects.get(i).getTickLabel().setVisible(true);
            }
        }

        //Last message if all went well
        if (success && anySelectedProjects()) {

            compile.setText("Finished Compiling");
            JOptionPane.showMessageDialog(contentPane,
                    "All projects have been successfully compiled",
                    "Success!",
                    JOptionPane.PLAIN_MESSAGE);
        }

        waitAndResetCompileText();
        compiling = false;
    }

    /**
     * Wait 5 seconds to set the Compile button back to it's original text.
     */
    private void waitAndResetCompileText() {
        Thread tResetCompileText = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                compile.setText("Compile all");
            }
        });

        tResetCompileText.start();
    }

    /*-------------------------------------------------------------------------------------------*/
    /*------------------------------------ CHECKING METHODS -------------------------------------*/
    /*-------------------------------------------------------------------------------------------*/

    /**
     * Checks if all projects paths are empty.
     *
     * @return true if all are empty.
     */
    private boolean allEmptyProjects() {
        boolean allEmpty = true;
        String path;
        for (int i = 0; i < selectedProjects.size(); i++) {
            path = selectedProjects.get(i).getJtfPath().getText();
            if (!path.isEmpty() && !path.contains("git")) {
                allEmpty = false;
            }
        }
        return allEmpty;
    }

    /**
     * If the user has entered the path manually, the path and the chosen variables have to be set.
     */
    private void selectProjects() {

        for (int i = 0; i < selectedProjects.size(); i++) {
            ProjectPanel project = selectedProjects.get(i);
            if (!project.getJtfPath().getText().isEmpty() && !project.isCloned()) {
                //The path of the project is not empty
                project.getFc().setPath(project.getJtfPath().getText());
                project.getFc().getProjectName().setText(project.getFc().getPath());
                project.getFc().setChosen(true);
            }
        }
    }

    /**
     * Checks if there is any project being cloned.
     *
     * @return true if at least one is being cloned.
     */
    private boolean anyProjectIsCloning() {
        boolean atLeastOne = false;
        for (int i = 0; i < selectedProjects.size(); i++) {
            if (selectedProjects.get(i).isCloning()) {
                atLeastOne = true;
            }
        }
        return atLeastOne;
    }

    /**
     * @return = true if at least one project is selected in the file chooser.
     */
    private boolean anySelectedProjects() {
        boolean oneOrMore = false;
        for (int i = 0; i < selectedProjects.size(); i++) {
            if (selectedProjects.get(i).getFc().isChosen()) {
                oneOrMore = true;
            }
        }
        return oneOrMore;
    }

    /*-------------------------------------------------------------------------------------------*/
    /*------------------------------------------ MAIN -------------------------------------------*/
    /*-------------------------------------------------------------------------------------------*/

    public static void main(String[] args) {
        Main ex = new Main();
        ex.setVisible(true);
    }

    private Main() {
        initUI();
    }

}