package Principal;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class repoChooser extends JFrame {

    private final JLabel lblName;
    private final JTextField repo;
    //private final JButton choose;

    private final int rc_width;
    private final int rc_height;


    public repoChooser() {
        lblName = new JLabel("URL");
        lblName.setBounds(65, 31, 46, 14);
        add(lblName);

        repo = new JTextField();
        repo.setBounds(128, 28, 86, 20);
        repo.setColumns(10);
        add(repo);

/*        choose = new JButton("Done");
        choose.setBounds(0, 0, 50, 50);
        add(choose);*/

        rc_width = 500;
        rc_height= 200;

    }

    public void open() {
        this.setSize(rc_width,rc_height);
        setBackground(Color.WHITE);
        //this.setResizable(false);
        this.setLocationRelativeTo(null);//null: centers window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Choose a repository");
        setVisible(true);
    }

    private void createTempDir() {
        String path = ".\\temp_git";
        new File(path).mkdir();
    }
}
