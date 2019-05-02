package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    ArrayList<Project_pan> selected_projs = new ArrayList<Project_pan>();

    private int panell_width = (screenSize.width / 3);
    private int panell_height = 500;

    public Main() {
        initUI();
    }

    public void initUI() {

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //TITOL
        JLabel titol_app = new JLabel("Select projects to be compiled");
        titol_app.setBounds(((panell_width / 2) - 100), 0, 200, 50);
        contentPane.add(titol_app);

        //PANELL PROJECTE
        nou_project_pan(contentPane);

        //ADD PROJECT BUTTON
        JButton add_proj = new JButton("+");
        add_proj.setBounds(0,0,20,20);
        contentPane.add(add_proj);

/*        JScrollPane a = new JScrollPane(new DragDropList());
        a.setBounds(50,100,100,100);
        contentPane.add(a);

        JScrollPane b = new JScrollPane(new DragDropList());
        b.setBounds(200,100,100,100);
        contentPane.add(b);*/

        List<Project_pan> lll= new ArrayList<Project_pan>();
        lll.add(new Project_pan());

/*        listModel = new DefaultListModel<>();
        Project_pan proj = new Project_pan();
        listModel.addElement(proj);
        listModel.addElement(new Project_pan());
        listModel.addElement(new Project_pan());*/

        // Create the list and put it in a scroll pane.
        JList list;
        list = new JList((ListModel) lll);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        list.setBounds(200,200,500,100);
        contentPane.add(list);


        //CONFIGURAR FINESTRA
        setSize(panell_width, panell_height);
        setResizable(false);
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
        panell_height += proj.getJl_path_height()+20;
    }

    public static void main(String[] args) {
        Main ex = new Main();
        ex.setVisible(true);
    }

}