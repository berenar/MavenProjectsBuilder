package Principal;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Project_pan extends JPanel {


    private int x_inicial = 50;
    private int y_inicial = 50;

    private int x_margin = 10;

    private int jl_order_size = 30;

    private int jl_path_width = 400;

    private int jl_path_height = 30;

    private int jb_fc_width = 90;
    private int jb_fc_height = 30;

    private JLabel jl_order;
    private Border jl_order_border;
    private JLabel jl_path;
    private JButton jb_fc;
    private Border fc_border;
    private Boolean triat;
    private int id;

    private FileChooser fc = new FileChooser();

    public Project_pan() {
        this.jl_order = new JLabel("", SwingConstants.CENTER);
        this.jl_order_border = BorderFactory.createLineBorder(Color.GRAY, 1);
        this.jl_path = new JLabel("...", SwingConstants.CENTER);
        fc.setFc_jl_path(jl_path);
        this.jb_fc = fc.getGo();
        this.fc_border = BorderFactory.createLineBorder(Color.GRAY, 1);
        this.triat = false;
    }

    public void configurarProjectPan(int n) {
        this.id = n;
        jl_order.setText(String.valueOf(id));
        jl_order.setBounds(x_inicial, y_inicial * n, jl_order_size, jl_order_size);
        jl_order.setBorder(jl_order_border);
        jl_path.setBounds(x_inicial + jl_order_size + x_margin,
                y_inicial * n, jl_path_width, jl_path_height);
        jl_path.setBorder(fc_border);
        jb_fc.setBounds(x_inicial + jl_order_size + jl_path_width + x_margin * 2,
                y_inicial * n, jb_fc_width, jb_fc_height);
    }

    public void afegirProjectPan(Container contentPane) {
        contentPane.add(jl_order);
        contentPane.add(jl_path);
        contentPane.add(jb_fc);
    }

    public FileChooser getFc() {
        return fc;
    }

    public int getJl_path_height() {
        return jl_path_height;
    }


}
