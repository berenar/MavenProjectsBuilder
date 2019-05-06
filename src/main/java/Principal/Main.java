package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class Main extends JFrame {

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final ArrayList<ProjectPanel> selected_projects = new ArrayList<ProjectPanel>();

    private final int panel_width = (screenSize.width / 3);
    private int panel_height = 150;

    private Container contentPane;
    private ProjectPanel project_panel;
    private JButton add_project;
    private JButton compile;

    private final int add_project_size = 30;
    private final int compile_width = 200;
    private final int compile_height = 30;

    private final String compileCommand = "mvn clean install";

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
        setSize(panel_width, panel_height);
        setResizable(false);
        setLocationRelativeTo(null);//null: centers window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("mvnCompiler 1.0");
        try {
            setIconImage(ImageIO.read(new File(System.getProperty("user.dir")
                    + "/media/mvn_logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void nouProjectPan() {
        project_panel = new ProjectPanel();
        project_panel.configureProjectPan(selected_projects.size() + 1);
        project_panel.addProjectPan(contentPane);
        selected_projects.add(project_panel);
        panel_height += project_panel.getJl_path_height() + 20;
        setSize(panel_width, panel_height);
        repaint();
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
                reSetBounds();
                contentPane.repaint();
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
                compileChosen();
            }
        });
    }

    private void compileChosen() {
        ProcessBuilder pb = new ProcessBuilder();
        for (int i = 0; i < selected_projects.size(); i++) {
            if (selected_projects.get(i).getFc().isChosen()) {
                String path = selected_projects.get(i).getFc().getFc_jl_path().getText();
                try {
                    pb.executeCommand("cd " + "\"" + path + "\"" + " && " + compileCommand);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(getContentPane(),
                            "Error compiling project number" + selected_projects.get(i).getId(),
                            "Error compiling",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
        }
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