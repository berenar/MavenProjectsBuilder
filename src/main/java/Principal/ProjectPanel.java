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
    private final JLabel jl_path;
    private final JButton jb_fc;
    private final JButton jb_git;
    private BufferedImage tick;
    private final JLabel tickLabel;
    private final Border border;

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
    private final int jbs_width = 90;
    private final int jbs_height = 30;
    private final int tickLabel_size = 35;

    //id of the project
    private int id;

    /**
     * Initializes project panel components
     */
    public ProjectPanel() {
        this.jl_order = new JLabel("", SwingConstants.CENTER);
        this.border = BorderFactory.createLineBorder(Color.GRAY, 1);
        this.jl_path = new JLabel(". . .", SwingConstants.CENTER);
        fc.setProjectName(jl_path);
        this.jb_fc = fc.getGo();
        this.jb_git = new JButton("Git");
        this.jb_git.setBackground(new java.awt.Color(186, 195, 211));
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
        this.jl_order.setText(String.valueOf(id));
        this.jl_order.setBounds(x_initial, y_initial * n, jl_order_size, jl_order_size);
        this.jl_order.setBorder(border);
        this.jl_path.setBounds(jl_order.getBounds().x + jl_order_size + x_margin,
                y_initial * n, jl_path_width, jl_path_height);
        this.jl_path.setBorder(border);
        this.jb_fc.setBounds(jl_path.getBounds().x + jl_path_width + x_margin,
                y_initial * n, jbs_width, jbs_height);
        this.jb_git.setBounds(jb_fc.getBounds().x + jbs_width + x_margin, y_initial * n, jbs_width, jbs_height);
        this.jb_git.setBorder(border);
        this.tickLabel.setBounds(jb_git.getBounds().x + jbs_width + x_margin,
                y_initial * n - 5, tickLabel_size, tickLabel_size);
        //this.tickLabel.setVisible(false);
    }

    /**
     * Adds project panel components to the JFrame
     *
     * @param contentPane JFrame
     */
    public void addProjectPan(Container contentPane) {
        contentPane.add(this.jl_order);
        contentPane.add(this.jl_path);
        contentPane.add(this.jb_fc);
        contentPane.add(this.jb_git);
        contentPane.add(this.tickLabel);
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