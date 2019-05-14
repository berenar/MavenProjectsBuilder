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
    private final Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    private Color selected;
    private Color notWhite = new Color(230, 247, 255);

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

    //true if the project is being cloned
    private boolean cloning = false;

    //true if project has finished cloning
    private boolean cloned = false;

    String git_url;
    String nom_repo;
    String dest_path;
    String com;

    /**
     * Initializes project panel components
     *
     * @param selected color of the buttons
     */
    public ProjectPanel(Color selected) {
        this.selected = selected;
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
        id = n;
        jl_order.setText(String.valueOf(id));
        jl_order.setBounds(x_initial, y_initial * id, jl_order_size, jl_order_size);
        jl_order.setBackground(notWhite);

        jtf_path.setBounds(jl_order.getBounds().x + jl_order_size + x_margin,
                y_initial * id, jl_path_width, jl_path_height);
        jtf_path.setBorder(emptyBorder);
        jtf_path.setBackground(notWhite);

        jb_fc.setBounds(jtf_path.getBounds().x + jl_path_width + x_margin,
                y_initial * id, jbs_width, jbs_height);
        jb_fc.setBorderPainted(false);
        jb_fc.setBackground(notWhite);

        jb_git.setBounds(jb_fc.getBounds().x + jbs_width + x_margin, y_initial * id, jbs_width, jbs_height);
        jb_git.setBorderPainted(false);
        jb_git.setBackground(notWhite);
        jb_git.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!cloned && !jtf_path.getText().isEmpty()) {
                    prepareProject();
                }
            }
        });

        tickLabel.setBounds(jb_git.getBounds().x + jbs_width + x_margin,
                y_initial * n - 5, tickLabel_size, tickLabel_size);
        tickLabel.setVisible(false);
    }

    /**
     * Clones the repository in a new folder
     * Sets the local path of the newly cloned repository
     * Sets the project as chosen
     */
    private void prepareProject() {
        //Thread for the cloning process
        Thread t_clone = new Thread(new Runnable() {
            @Override
            public void run() {
                cloning = true;
                jb_git.setText("Cloning...");
                git_url = jtf_path.getText();
                //Get substring of the repository name
                nom_repo = git_url.substring(git_url.lastIndexOf("/") + 1, git_url.indexOf(".git"));
                //Create destination directory
                dest_path = System.getProperty("user.dir") + "\\.git_temp\\" + nom_repo;
                ProcessBuilder pb = new ProcessBuilder();
                com = cloneCommand + " " + git_url + " " + dest_path;
                try {
                    cloned = pb.executeCommand(com);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                fc.setChosen(true);
                jtf_path.setText(nom_repo);
                fc.setPath(dest_path);
                cloning = false;
                jb_git.setText("Cloned");
            }
        });

        t_clone.start();
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
        jtf_path.setOpaque(true);
        jtf_path.setBackground(selected);
        jtf_path.setBorder(emptyBorder);

        jb_fc.setOpaque(true);
        jb_fc.setBackground(this.selected);
        jb_fc.setBorderPainted(false);

        jb_git.setOpaque(true);
        jb_git.setBackground(this.selected);
        jb_fc.setBorderPainted(false);
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

    public boolean isCloning() {
        return cloning;
    }
}