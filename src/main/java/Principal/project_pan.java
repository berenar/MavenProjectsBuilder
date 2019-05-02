package Principal;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class project_pan extends JPanel {
    private FileChooser fc = new FileChooser();
    private int x_inicial = 50;
    private int y_inicial = 50;

    private int jl_order_size = 30;

    private int jl_path_width = 400;
    private int jl_path_height = 30;

    private int jb_fc_width = 100;
    private int jb_fc_height = 30;

    private JLabel jl_order;
    private Border jl_order_border;
    private JLabel jl_path;
    private JButton jb_fc;
    private Border fc_border;
    private Boolean triat;

    public project_pan() {
        this.jl_order = new JLabel("a");
        this.jl_order_border = BorderFactory.createLineBorder(Color.GRAY, 1);
        this.jl_path = new JLabel(" ...");
        this.jb_fc = fc.go;
        this.fc_border = BorderFactory.createLineBorder(Color.GRAY, 1);
        this.triat = false;

    }

    public void configurar_project_pan(int n) {
        jl_order.setBounds(x_inicial, y_inicial * n, jl_order_size, jl_order_size);
        jl_order.setBorder(jl_order_border);
        jl_path.setBounds(x_inicial + jl_order_size, y_inicial * n, jl_path_width, jl_path_height);
        jl_path.setBorder(fc_border);
        jb_fc.setBounds(x_inicial + jl_path_width + 40, y_inicial * n, jb_fc_width, jb_fc_height);
    }

    public void afegir_project_pan(Container contentPane) {
        contentPane.add(jl_path);
        contentPane.add(jb_fc);
    }
}
