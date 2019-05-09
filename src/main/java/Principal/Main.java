package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

class Main extends JFrame {

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final ArrayList<ProjectPanel> selected_projects = new ArrayList<ProjectPanel>();

    private final int panel_width = (screenSize.width / 3) + 50;
    private int panel_height = 150;

    private Container contentPane;
    private ProjectPanel project_panel;
    private JButton add_project;
    private JButton compile;

    private final int add_project_size = 30;
    private final int compile_width = 200;
    private final int compile_height = 30;

    private final String compileCommand = "mvn clean install";
    private boolean success = true;
    private Timer t;
    private JOptionPane pane;
    private JDialog dialog;

    private Output out = new Output();

    private Main() {
        initUI();
    }

    private void initUI() {
        contentPane = getContentPane();
        contentPane.setLayout(null);

        //TITLE
        JLabel title_app = new JLabel("Select projects to be compiled");
        title_app.setBounds(((panel_width / 2) - 90), 0, panel_width, 50);
        contentPane.add(title_app);

        //PROJECT PANEL
        nouProjectPan();

        //ADD PROJECT BUTTON
        nouAddProject();

        //COMPILE BUTTON
        nouCompile();

        //CONFIGURE JFRAME
        this.setSize(panel_width, panel_height);
//        this.setResizable(false);
        this.setLocationRelativeTo(null);//null: centers window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("mvnCompiler 1.1");
        try {
            this.setIconImage(ImageIO.read(getClass().getClassLoader().getResource("mvn_logo_2.png")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Error reading image: mvn_logo_2.png");
            System.exit(1);
        }

    }

    private void nouProjectPan() {
        project_panel = new ProjectPanel();
        project_panel.configureProjectPan(selected_projects.size() + 1);
        project_panel.addProjectPan(contentPane);
        selected_projects.add(project_panel);
        panel_height += project_panel.getJl_path_height() + 20;
        this.setSize(panel_width, panel_height);
    }

    private void nouAddProject() {
        add_project = new JButton("+");
        add_project.setMargin(new Insets(0, 0, 0, 0));
        add_project.setFont(new Font("Arial", Font.PLAIN, 20));
        add_project.setBounds(50, panel_height - 100, add_project_size, add_project_size);
        add_project.setBackground(new java.awt.Color(186, 195, 211));
        contentPane.add(add_project);

        add_project.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nouProjectPan();
                out.initOutput(contentPane,panel_height, panel_width);
                out.removeOutput();
                reSetBounds();
            }
        });
    }

    private void nouCompile() {
        compile = new JButton("Compile all");
        compile.setFont(new Font("Arial", Font.PLAIN, 20));
        compile.setBounds(160 + add_project_size, panel_height - 100, compile_width, compile_height);
        compile.setBackground(new java.awt.Color(186, 195, 211));
        contentPane.add(compile);

        compile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Create new thread to be able to paint ticks meanwhile compilation
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!out.isOutput_visible()) {
                            out.initOutput(contentPane, panel_height, panel_width);
                            out.addOutput();
                        }
                        //reset success value
                        success = true;
                        allTicksToFalse();
                        compileChosen();
                    }
                });
                t1.start();


            }
        });
    }


    private void compileChosen() {
        ProcessBuilder pb = new ProcessBuilder();
        for (int i = 0; i < selected_projects.size(); i++) {
            if (selected_projects.get(i).getFc().isChosen()) {
                String path = selected_projects.get(i).getFc().getPath();
                try {
                    pb.executeCommand(out,"cd " + "\"" + path + "\"" + " && " + compileCommand, getContentPane());
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
            JOptionPane.showMessageDialog(getContentPane(),
                    "All projects have been successfully compiled.",
                    "Success!",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void allTicksToFalse() {
        for (int i = 0; i < selected_projects.size(); i++) {
            selected_projects.get(i).getTickLabel().setVisible(false);
        }
        repaint();
    }

    private boolean anySelectedProjects() {
        boolean oneOrMore = false;
        for (int i = 0; i < selected_projects.size(); i++) {
            if (selected_projects.get(i).getFc().isChosen()) {
                oneOrMore = true;
            }
        }
        return oneOrMore;
    }

    private void reSetBounds() {
        add_project.setBounds(50, panel_height - 100, add_project_size, add_project_size);
        compile.setBounds(160 + add_project_size, panel_height - 100, compile_width, compile_height);
    }


    public static void main(String[] args) {
        Main ex = new Main();
        ex.setVisible(true);
    }
}