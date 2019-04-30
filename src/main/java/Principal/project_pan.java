package Principal;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class project_pan extends JPanel {
    private FileChooser fc = new FileChooser();

    private JLabel jl_path;
    private JButton jb_fc;
    private Border fc_border;
    private Boolean triat;

    public void project_pan() {
        this.jl_path = new JLabel("...");
        this.jb_fc = fc.go;
        this.fc_border = BorderFactory.createLineBorder(Color.GRAY, 1);
        this.triat = false;
    }
    public void configurar_project_pan(){
        jl_path.setBounds(50, 60, 200, 30);
        jl_path.setBorder(fc_border);
        jb_fc.setBounds(180, 60, 100, 30);
    }

    public void afegir_project_pan(Container contentPane) {
        contentPane.add(jl_path);
        contentPane.add(jb_fc);
    }
}
