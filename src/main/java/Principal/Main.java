package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class Main extends JFrame implements ActionListener {

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final ArrayList<ProjectPanel> selected_projects = new ArrayList<ProjectPanel>();

    private final int panel_width = (screenSize.width / 3);
    private int panel_height = 150;

    private Container contentPane;
    private JButton add_project;
    private final int add_project_size = 30;

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
        nouProjectPan();
        nouProjectPan();

        //PROJECT BUTTON
        nouAddProject();

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
        ProjectPanel project_panel = new ProjectPanel();
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
        add_project.addActionListener(this);
    }

    private void repaintAddProject() {
        add_project.setBounds(50, panel_height - 90, add_project_size, add_project_size);
    }

    public void actionPerformed(ActionEvent e) {
        nouProjectPan();
        System.out.println(panel_height);
        repaintAddProject();
        contentPane.repaint();
    }

// --Commented out by Inspection START (03/05/2019 13:03):
//    private void printPaths() {
//        for (ProjectPanel selected_project : selected_projects) {
//            System.out.println(selected_project.getFc().getFc_jl_path().getText());
//        }
//    }
// --Commented out by Inspection STOP (03/05/2019 13:03)

    public static void main(String[] args) {
        Main ex = new Main();
        ex.setVisible(true);
    }
}