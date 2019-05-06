package Principal;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

class ProjectPanel extends JPanel {


    private final int x_initial = 50;
    private final int y_initial = 50;

    private final int x_margin = 10;

    private final int jl_order_size = 30;

    private final int jl_path_width = 400;

    private final int jl_path_height = 30;

    private final int jb_fc_width = 90;
    private final int jb_fc_height = 30;

    private final JLabel jl_order;
    private final Border jl_order_border;
    private final JLabel jl_path;
    private final JButton jb_fc;
    private final Border fc_border;

    private int id;

    private final FileChooser fc = new FileChooser();

    public ProjectPanel() {
        this.jl_order = new JLabel("", SwingConstants.CENTER);
        this.jl_order_border = BorderFactory.createLineBorder(Color.GRAY, 1);
        this.jl_path = new JLabel(". . .", SwingConstants.CENTER);
        fc.setProjName(jl_path);
        this.jb_fc = fc.getGo();
        this.fc_border = BorderFactory.createLineBorder(Color.GRAY, 1);
    }

    public void configureProjectPan(int n) {
        this.id = n;
        jl_order.setText(String.valueOf(id));
        jl_order.setBounds(x_initial, y_initial * n, jl_order_size, jl_order_size);
        jl_order.setBorder(jl_order_border);
        jl_path.setBounds(x_initial + jl_order_size + x_margin,
                y_initial * n, jl_path_width, jl_path_height);
        jl_path.setBorder(fc_border);
        jb_fc.setBounds(x_initial + jl_order_size + jl_path_width + x_margin * 2,
                y_initial * n, jb_fc_width, jb_fc_height);
    }

    public void addProjectPan(Container contentPane) {
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

    public int getId() {
        return id;
    }

}
