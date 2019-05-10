package Principal;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.IOException;

class ProjectPanel extends JPanel {

    //Swing components
    private final JLabel jl_order;
    private final Border jl_order_border;
    private final JLabel jl_path;
    private final JButton jb_fc;
    private final Border fc_border;
    private BufferedImage tick;
    private final JLabel tickLabel;

    //File chooser for the jb_fc button
    private final FileChooser fc = new FileChooser();

    //Where to start painting components
    private final int x_initial = 50;
    private final int y_initial = 50;

    //JFrame margin
    private final int x_margin = 10;

    //Component sizes
    private final int jl_order_size = 30;
    private final int jl_path_width = 400;
    private final int jl_path_height = 30;
    private final int jb_fc_width = 90;
    private final int jb_fc_height = 30;
    private final int tickLabel_size = 35;

    //id of the project
    private int id;

    /**
     * Initializes project panel components
     */
    public ProjectPanel() {
        this.jl_order = new JLabel("", SwingConstants.CENTER);
        this.jl_order_border = BorderFactory.createLineBorder(Color.GRAY, 1);
        this.jl_path = new JLabel(". . .", SwingConstants.CENTER);
        fc.setProjectName(jl_path);
        this.jb_fc = fc.getGo();
        this.fc_border = BorderFactory.createLineBorder(Color.GRAY, 1);
        try {
            //noinspection ConstantConditions
            this.tick = ImageIO.read(getClass().getClassLoader().getResource("tick.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Error reading image: tick.png");
            System.exit(1);
        }
        this.tickLabel = new JLabel(new ImageIcon(this.tick));
    }

    /**
     * Sets id for the project
     * Sets project panel components bounds
     * Sets the tickLabel to false
     *
     * @param n id
     */
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
        tickLabel.setBounds(x_initial + jl_order_size + jl_path_width + tickLabel_size + x_margin * 9,
                y_initial * n - 5, tickLabel_size, tickLabel_size);
        tickLabel.setVisible(false);
    }

    /**
     * Adds project panel components to the JFrame
     *
     * @param contentPane JFrame
     */
    public void addProjectPan(Container contentPane) {
        contentPane.add(jl_order);
        contentPane.add(jl_path);
        contentPane.add(jb_fc);
        contentPane.add(tickLabel);
    }

    /**
     * Getter for fc File Chooser
     *
     * @return file chooser
     */
    public FileChooser getFc() {
        return fc;
    }

    /**
     * Getter for the JLabel path
     *
     * @return JLabel path
     */
    public int getJl_path_height() {
        return jl_path_height;
    }

    /**
     * Getter for the project id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the tickLabel
     *
     * @return tickLabel
     */
    public JLabel getTickLabel() {
        return tickLabel;
    }

}