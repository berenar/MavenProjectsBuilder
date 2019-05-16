package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static Principal.Constants.*;

class ProjectPanel extends JPanel {

    //Swing components
    private final JLabel jl_order;
    private final JTextField jtf_path;
    private final JButton jb_fc;
    private final JButton jb_clone;
    private BufferedImage tick;
    private final JLabel tickLabel;

    //File chooser for the jb_fc button
    private final FileChooser fc;

    //id of the project
    private int id;

    //pointer to Main.compiling
    private final boolean compiling;

    //true if the project is being cloned
    private boolean cloning = false;

    //true if project has finished cloning
    private boolean cloned = false;

    private String git_url;
    private String nom_repo;
    private String dest_path;
    private String com;

    //failed user attempts to clone
    private int retry_clone;

    private final Container parentContentPane;

    /**
     * Initializes project panel components
     *
     * @param compiling pointer to
     */
    public ProjectPanel(boolean compiling, Container parentContentPane) {
        this.fc = new FileChooser(parentContentPane);
        this.compiling = compiling;
        this.parentContentPane = parentContentPane;
        this.jl_order = new JLabel("", SwingConstants.CENTER);
        this.jtf_path = new JTextField();
        this.jtf_path.setHorizontalAlignment(JTextField.CENTER);
        fc.setProjectName(jtf_path);
        this.jb_fc = fc.getGo();
        this.jb_fc.setBackground(Color.WHITE);
        this.jb_clone = new JButton("Clone");
        this.jb_clone.setBackground(Color.WHITE);
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
     * Sets jb_clone action
     *
     * @param n id
     */
    public void configureProjectPan(int n) {
        id = n;
        jl_order.setText(String.valueOf(id));
        jl_order.setBounds(initial, initial * id, componentHeight, componentHeight);
        jl_order.setBackground(notWhite);

        jtf_path.setBounds(jl_order.getBounds().x + componentHeight + xMargin,
                initial * id, jlPathWidth, componentHeight);
        jtf_path.setBorder(emptyBorder);
        jtf_path.setBackground(notWhite);

        jb_fc.setBounds(jtf_path.getBounds().x + jlPathWidth + xMargin,
                initial * id, buttonWidth, componentHeight);
        jb_fc.setBorderPainted(false);
        jb_fc.setBackground(notWhite);

        jb_clone.setBounds(jb_fc.getBounds().x + buttonWidth + xMargin, initial * id, buttonWidth, componentHeight);
        jb_clone.setBorderPainted(false);
        jb_clone.setBackground(notWhite);

        jb_clone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retry_clone++;
                String path = jtf_path.getText();
                if (retry_clone > 3) {
                    //reset retry_cloned
                    retry_clone = 0;
                    if (cloned) {
                        JOptionPane.showMessageDialog(parentContentPane,
                                "Already cloned",
                                "Quick tip",
                                JOptionPane.PLAIN_MESSAGE);
                    } else if (path.isEmpty()) {
                        JOptionPane.showMessageDialog(parentContentPane,
                                "Path is empty",
                                "Quick tip",
                                JOptionPane.PLAIN_MESSAGE);
                    } else if (compiling) {
                        JOptionPane.showMessageDialog(parentContentPane,
                                "There is at least one project compiling",
                                "Quick tip",
                                JOptionPane.PLAIN_MESSAGE);
                    } else if (!path.contains(".git")) {
                        JOptionPane.showMessageDialog(parentContentPane,
                                "Not a valid git repository",
                                "Quick tip",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                } else if (!cloned && !path.isEmpty() && !compiling && path.contains(".git")) {
                    //reset retry_clone
                    retry_clone = 0;
                    prepareProject();
                }
            }
        });

        tickLabel.setBounds(jb_clone.getBounds().x + buttonWidth + xMargin,
                initial * n - 5, tickLabelSize, tickLabelSize);
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
                jb_clone.setText("Cloning...");
                git_url = jtf_path.getText();
                //Get substring of the repository name
                nom_repo = git_url.substring(git_url.lastIndexOf("/") + 1, git_url.indexOf(".git"));
                //Create destination directory
                dest_path = System.getProperty("user.dir") + "\\.mvnCompiler_temp\\" + nom_repo;
                ProcessBuilder pb = new ProcessBuilder();
                com = cloneCommand + " " + git_url + " " + dest_path;
                try {
                    pb.executeCommand(com);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                if (checkClonedDirectory(dest_path)) {
                    cloned = true;
                    fc.setPath(dest_path);
                    fc.setChosen(true);
                    jtf_path.setText(nom_repo);
                    jb_clone.setText("Cloned");
                } else {
                    cloned = false;
                    JOptionPane.showMessageDialog(parentContentPane,
                            "Error cloning the project " + id,
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    jb_clone.setText("Error");
                }
                cloning = false;
            }
        });

        t_clone.start();
    }

    private boolean checkClonedDirectory(String dest_path) {
        return Files.exists(Paths.get(dest_path));
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
        contentPane.add(this.jb_clone);
        contentPane.add(this.tickLabel);
    }

    /**
     * Sets Project panel components background color
     */
    public void colorizeProjectPane() {
        jtf_path.setOpaque(true);
        jtf_path.setBackground(moreBlue);
        jtf_path.setBorder(emptyBorder);

        jb_fc.setOpaque(true);
        jb_fc.setBackground(moreBlue);
        jb_fc.setBorderPainted(false);

        jb_clone.setOpaque(true);
        jb_clone.setBackground(moreBlue);
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
    public int getJlPathHeight() {
        return componentHeight;
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

    /**
     * Getter for Cloning
     *
     * @return if the project is cloning or not
     */
    public boolean isCloning() {
        return cloning;
    }

    /**
     * Getter for jtf_path
     *
     * @return jtf_path
     */
    public JTextField getJtfPath() {
        return jtf_path;
    }

    /**
     * Getter for Cloned
     *
     * @return if the project has been cloned or not
     */
    public boolean isCloned() {
        return cloned;
    }
}