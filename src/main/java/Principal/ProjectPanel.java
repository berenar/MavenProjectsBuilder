package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

class ProjectPanel extends JPanel {

    //Swing components
    private final JLabel jl_order;
    private final JTextField jtf_path;
    private final JButton jb_fc;
    private final JButton jb_git;
    private BufferedImage tick;
    private final JLabel tickLabel;

    //Border and colors for components
    private final Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
    private final Color color_jb;
    private final Color color_jl = new Color(204, 230, 255);

    //File chooser for the jb_fc button
    private final FileChooser fc = new FileChooser();

    private final String cloneCommand = "git clone";

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
     *
     * @param color_jb color of the buttons
     */
    public ProjectPanel(Color color_jb) {
        this.color_jb = color_jb;
        this.jl_order = new JLabel("", SwingConstants.CENTER);
        this.jtf_path = new JTextField();
        this.jtf_path.setHorizontalAlignment(JTextField.CENTER);
        fc.setProjectName(jtf_path);
        this.jb_fc = fc.getGo();
        this.jb_fc.setBackground(Color.WHITE);
        this.jb_git = new JButton("Git");
        this.jb_git.setBackground(Color.WHITE);
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
     * Sets jb_git action
     *
     * @param n id
     */
    public void configureProjectPan(int n) {
        this.id = n;
        this.jl_order.setText(String.valueOf(id));
        this.jl_order.setBounds(x_initial, y_initial * n, jl_order_size, jl_order_size);
        this.jl_order.setBorder(border);
        this.jtf_path.setBounds(jl_order.getBounds().x + jl_order_size + x_margin,
                y_initial * n, jl_path_width, jl_path_height);
        this.jtf_path.setBorder(border);
        this.jb_fc.setBounds(jtf_path.getBounds().x + jl_path_width + x_margin,
                y_initial * n, jbs_width, jbs_height);
        this.jb_git.setBounds(jb_fc.getBounds().x + jbs_width + x_margin, y_initial * n, jbs_width, jbs_height);
        this.jb_git.setBorder(border);
        jb_git.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepareProject();
            }
        });
        this.tickLabel.setBounds(jb_git.getBounds().x + jbs_width + x_margin,
                y_initial * n - 5, tickLabel_size, tickLabel_size);
        this.tickLabel.setVisible(false);
    }

    /**
     * Clones the repository in a new folder
     * Sets the local path of the newly cloned repository
     * Sets the project as chosen
     */
    private void prepareProject() {
        String git_url = jtf_path.getText();
        //Get substring of the repository name
        String nom_repo = git_url.substring(git_url.lastIndexOf("/") + 1, git_url.indexOf(".git"));
        //Create destination directory
        String dest_path = System.getProperty("user.dir") + "\\git_temp\\" + nom_repo;
        ProcessBuilder pb = new ProcessBuilder();
        String com = cloneCommand + " " + git_url + " " + dest_path;
        try {
            pb.executeCommand(com);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error cloning git repository");
        }
        this.jtf_path.setText(dest_path);
        this.fc.setChosen(true);
    }


    /**
     * Adds project panel components to the JFrame
     *
     * @param contentPane JFrame
     */
    public void addProjectPan(Container contentPane) {
        contentPane.add(this.jl_order);
        contentPane.add(this.jtf_path);
        contentPane.add(this.jb_fc);
        contentPane.add(this.jb_git);
        contentPane.add(this.tickLabel);
    }

    /**
     * Sets Project panel components background color
     */
    public void colorizeProjectPane() {
        this.jl_order.setOpaque(true);
        this.jl_order.setBackground(color_jl);

        this.jtf_path.setOpaque(true);
        this.jtf_path.setBackground(color_jl);

        this.jb_fc.setOpaque(true);
        this.jb_fc.setBackground(this.color_jb);

        this.jb_git.setOpaque(true);
        this.jb_git.setBackground(this.color_jb);
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