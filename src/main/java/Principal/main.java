package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class main extends JFrame {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int panell_width = screenSize.width/3;
    private int panell_heigh = screenSize.height/3;

    //TODO: fer dinamic
    private String[] path_names=new String[20];

    public main() {
        initUI();
    }

    public final void initUI() {

        FileChooser fc = new FileChooser();

        //crear components
        JLabel titol_app = new JLabel("Elegeix els projectes a compilar");
        JLabel path_1 = new JLabel("...");
        JButton button_fc = fc.go;

        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);

        //definir components
        titol_app.setBounds(((panell_width/2)-100), 0, 200, 50);
        path_1.setBounds(50, 60, 200, 30);
        path_1.setBorder(border);
        button_fc.setBounds(180, 60, 100, 30);

        getContentPane().setLayout(null);

        //afegir components
        getContentPane().add(titol_app);
        getContentPane().add(path_1);
        getContentPane().add(button_fc);


        //configurar finestra
        setSize(panell_width, panell_heigh);
        setLocationRelativeTo(null);//null: al centre de la pantalla
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("mvnCompiler 1.0");
        try {
            setIconImage(ImageIO.read(new File(System.getProperty("user.dir")+"/media/mvn_logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error obtenint logo de la app");
        }
    }

    public void setPath_names(String[] path_names) {
        this.path_names = path_names;
    }

    public static void main(String[] args) {
        main ex = new main();
        ex.setVisible(true);
    }
}