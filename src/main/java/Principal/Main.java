package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends JFrame {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    ArrayList<Project_pan> selected_projs = new ArrayList<Project_pan>();

    //initial sizes
    private int panell_width = (screenSize.width / 3);
    private int panell_height = 100;

    public Main() {
        initUI();
    }

    public void initUI() {

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //TITLE
        JLabel titol_app = new JLabel("Select projects to be compiled");
        titol_app.setBounds(((panell_width / 2) - 100), 0, 200, 50);
        contentPane.add(titol_app);

        //PROJECTS
        nou_project_pan(contentPane);

        //WINDOW CONFIGURATION
        setSize(panell_width, panell_height);
        setLocationRelativeTo(null);//null: al centre de la pantalla
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("mvnCompiler 1.0");
        try {
            setIconImage(ImageIO.read(new File(System.getProperty("user.dir") + "/media/mvn_logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nou_project_pan(Container contentPane) {
        Project_pan proj = new Project_pan();
        selected_projs.add(proj);
        proj.configurar_project_pan(selected_projs.size());
        proj.afegir_project_pan(contentPane);
        recalcular_window(proj);
    }

    private void recalcular_window(Project_pan proj) {
        panell_height = panell_height + 2 * proj.getJl_path_height();
    }

    public static void main(String[] args) {
        Main ex = new Main();
        ex.setVisible(true);
    }

}