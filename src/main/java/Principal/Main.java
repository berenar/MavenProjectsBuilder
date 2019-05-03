package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends JFrame implements ActionListener {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    ArrayList<Project_pan> selected_projs = new ArrayList<Project_pan>();

    private int panell_width = (screenSize.width / 3);
    private int panell_height = 150;

    Container contentPane;
    JButton add_proj;
    int add_proj_size = 30;

    Font f2 = new Font(Font.SANS_SERIF, Font.PLAIN, 20);

    public Main() {
        initUI();
    }

    public void initUI() {

        contentPane = getContentPane();
        contentPane.setLayout(null);

        //TITLE
        JLabel titol_app = new JLabel("Select projects to be compiled");
        titol_app.setFont(f2);
        titol_app.setBounds(((panell_width / 2) - 150), 0, panell_width, 50);
        contentPane.add(titol_app);

        //PROJECT PANEL
        nouProjectPan();

        //ADD PROJECT BUTTON
        nouAddProj();

        //CONFIGURE JFRAME
        setSize(panell_width, panell_height);
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

    public void nouProjectPan() {
        Project_pan proj = new Project_pan();
        proj.configurarProjectPan(selected_projs.size() + 1);
        proj.afegirProjectPan(contentPane);
        selected_projs.add(proj);
        panell_height += proj.getJl_path_height() + 20;
    }

    private void nouAddProj() {
        add_proj = new JButton("+");
        add_proj.setFont(new Font("Arial", Font.PLAIN, 20));
        add_proj.setMargin(new Insets(0, 0, 0, 0));
        add_proj.setBounds(50, panell_height - 90, add_proj_size, add_proj_size);
        contentPane.add(add_proj);
        add_proj.addActionListener(this);
    }

    private void repaintAddProj() {
        add_proj.setBounds(50, panell_height - 90, add_proj_size, add_proj_size);
    }

    public void actionPerformed(ActionEvent e) {
        nouProjectPan();
        System.out.println(panell_height);
        repaintAddProj();
        contentPane.repaint();
    }

    private void printPaths() {
        for (int i = 0; i < selected_projs.size(); i++) {
            System.out.println(selected_projs.get(i).getFc().getFc_jl_path().getText());
        }
    }

    public static void main(String[] args) {
        Main ex = new Main();
        ex.setVisible(true);
    }
}