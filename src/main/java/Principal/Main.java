package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

class Main extends JFrame {

    private final ArrayList<ProjectPanel> selected_projects = new ArrayList<ProjectPanel>();

    //JFrame size
    private final int panel_width = (Toolkit.getDefaultToolkit().getScreenSize().width / 3) + 150;
    private int panel_height = 150;

    //Swing components
    private Container contentPane;
    private ProjectPanel project_panel;
    private JButton add_project;
    private JButton compile;
    private final Output out = new Output();

    //JButtons color
    private final Color color_selected = new Color(128, 191, 255);

    //component sizes
    private final int add_project_size = 30;
    private final int compile_width = 200;
    private final int compile_height = 30;

    //commands to execute
    private final String compileCommand = "mvn clean install";

    private boolean compiling = false;

    //to know if all projects compiled
    private boolean success = true;

    private int retry_Local;

    private Main() {
        initUI();
    }

    private void initUI() {
        contentPane = getContentPane();
        contentPane.setLayout(null);

        //TITLE
        JLabel title_app = new JLabel("Introduce a project path manually (and click Local) or click " +
                "one of the two buttons to select a project");
        title_app.setBounds(100, 0, panel_width, 50);
        contentPane.add(title_app);

        //PROJECT PANEL
        nouProjectPan();

        //ADD PROJECT BUTTON
        nouAddProject();

        //COMPILE BUTTON
        nouCompile();

        //CONFIGURE JFRAME
        upd_frame_size();
        contentPane.setBackground(Color.WHITE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);//null: centers window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("mvnCompiler 1.2");
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
     * Adds a new Project panel
     */
    private void nouProjectPan() {
        project_panel = new ProjectPanel(color_selected,compiling);
        project_panel.configureProjectPan(selected_projects.size() + 1);
        project_panel.addProjectPan(contentPane);
        selected_projects.add(project_panel);
        panel_height += project_panel.getJl_path_height() + 20;
        upd_frame_size();
    }

    /**
     * Adds a new Add Project button (+)
     */
    private void nouAddProject() {
        add_project = new JButton("+");
        add_project.setBorderPainted(false);
        add_project.setMargin(new Insets(0, 0, 0, 0));
        add_project.setFont(new Font("Arial", Font.PLAIN, 20));
        add_project.setBounds(50, panel_height - 100, add_project_size, add_project_size);
        add_project.setBackground(color_selected);
        contentPane.add(add_project);

        add_project.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!compiling) {
                    boolean temporary_removed = false;
                    nouProjectPan();
                    if (out.isOutput_visible()) {
                        temporary_removed = true;
                        panel_height = out.removeOutput(contentPane, panel_height);
                        upd_frame_size();
                    }
                    reSetBounds();
                    if (temporary_removed) {
                        panel_height = out.addOutput(contentPane, panel_height, panel_width);
                        upd_frame_size();
                    }
                }
            }
        });
    }

    /**
     * Adds a new compile button (Compile all)
     */
    private void nouCompile() {
        compile = new JButton("Compile all");
        compile.setBorderPainted(false);
        compile.setFont(new Font("Arial", Font.PLAIN, 20));
        compile.setBounds(160 + add_project_size, panel_height - 100, compile_width, compile_height);
        compile.setBackground(color_selected);
        contentPane.add(compile);

        compile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!compiling) {
                    if (anyProjectIsCloning()) {
                        int reply = JOptionPane.showConfirmDialog(null,
                                "There are projects being cloned, are you sure you want to start compiling?",
                                "Hey",
                                JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                            actuallyCompile();
                        }
                    } else {
                        actuallyCompile();
                    }
                }
            }
        });
    }

    private void actuallyCompile() {
        retry_Local++;
        if (retry_Local >= 3) {
            project_panel.getFc().userFeedback("compile");
            retry_Local = 0;
        } else if (!out.isOutput_visible()) {
            //Thread for the output
            Thread t_out = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (anySelectedProjects()) {
                        panel_height = out.addOutput(contentPane, panel_height, panel_width);
                        upd_frame_size();
                    }
                }
            });
            t_out.start();
        }

        //Thread for the compilation
        Thread t_compile = new Thread(new Runnable() {
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
        t_compile.start();
    }

    /**
     * If the user has entered the path manually, the path and the chosen variables have to be set
     */
    private void selectProjects() {
        System.out.println("manually entered");
        for (int i = 0; i < selected_projects.size(); i++) {
            ProjectPanel project = selected_projects.get(i);
            project.getFc().setPath(project.getJtf_path().getText());
            project.getFc().getProjectName().setText(project.getFc().getPath());
            project.getFc().setChosen(true);
        }
    }

    /**
     * Checks if there is any project being cloned
     *
     * @return true if at least one is being cloned
     */
    private boolean anyProjectIsCloning() {
        boolean atLeastOne = false;
        for (int i = 0; i < selected_projects.size(); i++) {
            if (selected_projects.get(i).isCloning()) {
                atLeastOne = true;
            }
        }
        System.out.println("cloning?" + atLeastOne);
        return atLeastOne;

    }

    private void colorizeSelected() {
        for (int i = 0; i < selected_projects.size(); i++) {
            if (selected_projects.get(i).getFc().isChosen()) {
                selected_projects.get(i).colorizeProjectPane();
            }
        }
    }

    /**
     * Updates the JFrame size with the current values for the width and height
     */
    private void upd_frame_size() {
        this.setSize(panel_width, panel_height);
    }


    /**
     * Compile chosen projects
     */
    private void compileChosen() {
        compiling = true;
        compile.setText("Compiling...");
        ProcessBuilder pb = new ProcessBuilder();
        for (int i = 0; i < selected_projects.size(); i++) {
            if (selected_projects.get(i).getFc().isChosen()) {
                String path = selected_projects.get(i).getFc().getPath();
                try {
                    pb.executeCommand(out, "cd " + "\"" + path + "\"" + " && " + compileCommand,
                            getContentPane(), selected_projects.get(i).getId());
                } catch (Exception e) {
                    contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    JOptionPane.showMessageDialog(getContentPane(),
                            "Error compiling project number " + selected_projects.get(i).getId() + ".",
                            "Compilation halted",
                            JOptionPane.ERROR_MESSAGE);
                    success = false;
                    break;
                }
                selected_projects.get(i).getTickLabel().setVisible(true);
            }
        }

        //Last message if all went well
        if (success && anySelectedProjects()) {

            compile.setText("Finished Compiling");
            JOptionPane.showMessageDialog(getContentPane(),
                    "All projects have been successfully compiled.",
                    "Success!",
                    JOptionPane.PLAIN_MESSAGE);
        }

        waitAndResetCompileText();
        compiling = false;
    }

    /**
     * Wait 5 seconds to set the Compile button back to it's original text
     */
    private void waitAndResetCompileText() {
        Thread t_resetCompileText = new Thread(new Runnable() {
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

        t_resetCompileText.start();
    }

    /**
     * Sets all project ticks to false for a new compilation
     */
    private void allTicksToFalse() {
        for (int i = 0; i < selected_projects.size(); i++) {
            selected_projects.get(i).getTickLabel().setVisible(false);
        }
    }

    /**
     * @return = true if at least one project is color_selected in the file chooser
     */
    private boolean anySelectedProjects() {
        boolean oneOrMore = false;
        for (int i = 0; i < selected_projects.size(); i++) {
            if (selected_projects.get(i).getFc().isChosen()) {
                oneOrMore = true;
            }
        }
        return oneOrMore;
    }

    /**
     * Updates add_project and compile buttons bounds to match a new screen size
     */
    private void reSetBounds() {
        add_project.setBounds(50, panel_height - 100, add_project_size, add_project_size);
        compile.setBounds(160 + add_project_size, panel_height - 100, compile_width, compile_height);
    }

    public static void main(String[] args) {
        Main ex = new Main();
        ex.setVisible(true);
    }
}