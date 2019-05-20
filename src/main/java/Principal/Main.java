package Principal;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

import static Principal.Constants.*;

class Main extends JFrame {
    //Array of ProjectPanel objects
    private final ArrayList<ProjectPanel> selectedProjects = new ArrayList<>();

    //JFrame size
    private final int panelWidth = 780;
    private int panelHeight = 150;

    //Swing components
    private Container contentPane;
    private ProjectPanel projectPanel;
    private JButton addProject;
    private JButton compile;
    private JButton reset;
    private JButton export;
    private JButton importt;

    //to know if there's any project being compiled
    private boolean compiling = false;

    //to know if all projects compiled
    private boolean success = true;

    private int retryCompile;

    //Console for the output
    private final Output out = new Output();

    //To execute commands
    private final ProcessBuilder pb = new ProcessBuilder();

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

        //ADD BUTTON
        nouAddProject();

        //COMPILE BUTTON
        nouCompile();

        //IMPORT BUTTON
        nouImport();

        //Export BUTTON
        nouExport();

        //RESET BUTTON
        nouReset();

        //CONFIGURE JFRAME
        updFrameSize();
        contentPane.setBackground(Color.WHITE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);//null: centers window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("mvnCompiler 1.5");
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
        projectPanel = new ProjectPanel(compiling, contentPane, pb);
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
        setBounds("addProject");
        addProject.setBackground(moreBlue);
        contentPane.add(addProject);

        addProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProjectAction();

            }
        });
    }

    private void addProjectAction() {
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

    /**
     * Adds a new compile button (Compile all).
     */
    private void nouCompile() {
        compile = new JButton("Compile all");
        compile.setBorderPainted(false);
        compile.setFont(new Font("Arial", Font.PLAIN, 20));
        setBounds("compile");
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
     * Creates the Importt button
     * It's action is to import a file
     */
    private void nouImport() {

        final FileChooser fcImport = new FileChooser(contentPane, "Import", true);
        importt = fcImport.getGo();
        importt.setBorderPainted(false);
        importt.setMargin(new Insets(0, 0, 0, 0));
        setBounds("importt");
        importt.setBackground(moreBlue);
        contentPane.add(importt);
        importt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!compiling && !anyProjectIsCloning()) {
                    if (!allEmptyProjects()) {
                        int reply = JOptionPane.showConfirmDialog(contentPane,
                                "Current projects on the app will be overwritten. Proceed?",
                                "Warning", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                            proceed();
                        } else {
                            JOptionPane.showMessageDialog(contentPane, "Nothing imported");
                        }
                    } else {
                        proceed();
                    }
                }
            }

            private void proceed() {
                boolean chosen = fcImport.chooserAction();
                if (chosen) {
                    colorizeSelected(false); //remove color
                    allTicksToFalse();
                    allClonedToFalse();
                    fillProjects(fcImport.getPath());
                }
            }
        });

    }

    /**
     * Reads a line of the file in the path filePath
     * Creates a new projectPanel and fills it with the read line
     * (One ProjectPanel per read line)
     *
     * @param filePath path of the file to be imported
     */
    private void fillProjects(String filePath) {
        File file = new File(filePath);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "UTF-8"))) {
            if (!file.getName().contains(".txt")) {
                //File isn't a .txt file
                throw new InvalidFileExtension();
            }
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i + 1 > maxProjects) { //+1 because i starts at 0
                    //File has more than 10 lines
                    throw new MaxLinesException();
                } else {
                    if (i == selectedProjects.size()) {
                        //Add a new project panel only if needed
                        addProjectAction();
                    }
                    selectedProjects.get(i).getFc().setPath(line);
                    selectedProjects.get(i).getFc().getProjectName().setText(line);
                    i++;
                }
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(contentPane,
                    "Select a valid file path",
                    "File not found",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(contentPane,
                    "Error reading file",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (MaxLinesException e) {
            JOptionPane.showMessageDialog(contentPane,
                    "Only " + maxProjects + " projects were imported",
                    "Too many projects in the file",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InvalidFileExtension invalidFileExtension) {
            JOptionPane.showMessageDialog(contentPane,
                    "File must be a text file",
                    "Invalid file extension",
                    JOptionPane.ERROR_MESSAGE);

        }
    }

    /**
     * Creates the Export button
     * It's action is to export what the user introduced in the app.
     */
    private void nouExport() {
        export = new JButton("Export");
        export.setBorderPainted(false);
        export.setMargin(new Insets(0, 0, 0, 0));
        setBounds("export");
        export.setBackground(moreBlue);
        contentPane.add(export);

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!allEmptyProjects()) {
                    String fileName = JOptionPane.showInputDialog(contentPane, "Name the file");
                    if (fileName != null && !fileName.trim().isEmpty()) {
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
                    } else {
                        //File name is null or blank or canceled showInputDialog
                        JOptionPane.showMessageDialog(contentPane,
                                "No file created",
                                "Hey",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    //No project imported
                    JOptionPane.showMessageDialog(contentPane,
                            "There's any project in the app",
                            "Error creating file",
                            JOptionPane.ERROR_MESSAGE);
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
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < selectedProjects.size(); i++) {
            Objects.requireNonNull(writer).println(selectedProjects.get(i).getJtfPath().getText());
        }
        Objects.requireNonNull(writer).close();
    }

    /**
     * Creates the Clear projects button.
     * It's action is to reset the program.
     */
    private void nouReset() {
        reset = new JButton("Reset");
        reset.setBorderPainted(false);
        reset.setMargin(new Insets(0, 0, 0, 0));
        reset.setBounds(export.getBounds().x + buttonWidth + 10, panelHeight - 100, buttonWidth, componentHeight);
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
        //Delete temporal directory
        String tempPath = System.getProperty("user.dir") + tempDir;
        if (deleteDirectory(new File(tempPath))) {
            JOptionPane.showMessageDialog(contentPane,
                    "The folder with temporal files may have " +
                            "not been deleted, you might want to delete it yourself",
                    "Hey",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        //Delete current window and create a new instance of Main
        this.dispose();
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
/*        addProject.setBounds(50, panelHeight - 100, squareComponent, squareComponent);
        compile.setBounds(160 + squareComponent, panelHeight - 100, compileWidth, componentHeight);
        export.setBounds(500, panelHeight - 100, buttonWidth, componentHeight);
        reset.setBounds(export.getBounds().x + buttonWidth + 10, panelHeight - 100, buttonWidth, componentHeight);*/

        setBounds("addProject");
        setBounds("compile");
        setBounds("importt");
        setBounds("export");
        setBounds("reset");
    }

    private void setBounds(String component) {
        switch (component) {
            case "addProject":
                addProject.setBounds(50, panelHeight - 100, squareComponent, squareComponent);
                break;
            case "compile":
                compile.setBounds(addProject.getX() + squareComponent + xMargin, panelHeight - 100, compileWidth, componentHeight);
                break;
            case "stop":
                //TODO: bounds del boto stop (compiling)
                break;
            case "importt":
                importt.setBounds(compile.getX() + compileWidth + xMargin, panelHeight - 100, buttonWidth, componentHeight);
                break;
            case "export":
                export.setBounds(importt.getX() + buttonWidth + xMargin, panelHeight - 100, buttonWidth, componentHeight);
                break;
            case "reset":
                reset.setBounds(export.getX() + buttonWidth + 10, panelHeight - 100, buttonWidth,
                        componentHeight);
                break;
        }
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
     * Sets all projects to cloned = false
     */
    private void allClonedToFalse() {
        for (int i = 0; i < selectedProjects.size(); i++) {
            selectedProjects.get(i).setCloned(false);
            selectedProjects.get(i).getJbClone().setText("Clone");
        }
    }

    /**
     * Paint selected projects to be compiled in a darker color.
     */
    private void colorizeSelected(boolean de) {
        for (int i = 0; i < selectedProjects.size(); i++) {
            if (selectedProjects.get(i).getFc().isChosen()) {
                if (de) {
                    selectedProjects.get(i).colorizeProjectPane();
                } else {
                    selectedProjects.get(i).discolorProjectPane();
                }
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
                    manualProjects();
                    allTicksToFalse();
                    colorizeSelected(true);
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
//        ProcessBuilder pb = new ProcessBuilder();
        for (int i = 0; i < selectedProjects.size(); i++) {
            if (selectedProjects.get(i).getFc().isChosen()) {
                String path = selectedProjects.get(i).getFc().getPath();
                try {
                    pb.executeCommandAndWait(out, "cd " + "\"" + path + "\"" + " && " + compileCommand,
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
    private void manualProjects() {

        for (int i = 0; i < selectedProjects.size(); i++) {
            ProjectPanel project = selectedProjects.get(i);
            if (!project.getJtfPath().getText().isEmpty()) {
                //The path of the project is not empty
                project.getFc().setPath(project.getJtfPath().getText());
                project.getFc().getProjectName().setText(project.getFc().getPath());
                if (project.getFc().getPath().contains(".git")) {
                    if (project.isCloned()) {
                        project.getFc().setChosen(true);
                    } else {
                        project.getFc().setChosen(false);
                    }
                } else {
                    project.getFc().setChosen(true);
                }
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
    /*--------------------------------------- EXCEPTIONS ----------------------------------------*/
    /*-------------------------------------------------------------------------------------------*/


    private class MaxLinesException extends Throwable {
    }

    private class InvalidFileExtension extends Throwable {
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