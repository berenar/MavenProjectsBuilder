package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class main extends JFrame {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int panell_width = screenSize.width / 3;
    private int panell_heigh = screenSize.height / 3;

    private int project_pan_number = 0;

    //TODO: fer dinamic
    private String path_name;

    public main() {
        initUI();
    }

    public void initUI() {

        getContentPane().setLayout(null);

        //TITOL
        JLabel titol_app = new JLabel("Select projects");
        titol_app.setBounds(((panell_width / 2) - 100), 0, 200, 50);
        getContentPane().add(titol_app);

        //PANELL PROJECTE
        nou_project_pan();

        //CONFIGURAR FINESTRA
        setSize(panell_width, panell_heigh);
        setLocationRelativeTo(null);//null: al centre de la pantalla
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("mvnCompiler 1.0");
        try {
            setIconImage(ImageIO.read(new File(System.getProperty("user.dir") + "/media/mvn_logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error obtenint logo de la app");
        }
    }

    private void nou_project_pan() {
        project_pan_number++;
        project_pan proj = new project_pan();
        proj.project_pan();
        proj.configurar_project_pan(project_pan_number);
        proj.afegir_project_pan(getContentPane());
    }

    public static void main(String[] args) {
        main ex = new main();
        ex.setVisible(true);
    }

}