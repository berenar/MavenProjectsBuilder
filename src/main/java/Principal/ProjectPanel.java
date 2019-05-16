package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    private final JLabel jlOrder;
    private final JTextField jtfPath;
    private final JButton jbFc;
    private final JButton jbClone;
    private BufferedImage tick;
    private final JLabel tickLabel;

    //File chooser for the jbFc button
    private final FileChooser fc;

    //id of the project
    private int id;

    //pointer to Main.compiling
    private final boolean compiling;

    //true if the project is being cloned
    private boolean cloning = false;

    //true if project has finished cloning
    private boolean cloned = false;

    private String gitUrl;
    private String nomRepo;
    private String destPath;
    private String com;

    //failed user attempts to clone
    private int retryClone;

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
        this.jlOrder = new JLabel("", SwingConstants.CENTER);
        this.jtfPath = new JTextField();
        this.jtfPath.setHorizontalAlignment(JTextField.CENTER);
        fc.setProjectName(jtfPath);
        this.jbFc = fc.getGo();
        this.jbClone = new JButton("Clone");
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
     * Sets jbClone action
     *
     * @param n id
     */
    public void configureProjectPan(int n) {
        id = n;
        jlOrder.setText(String.valueOf(id));
        jlOrder.setBounds(initial, initial * id, componentHeight, componentHeight);
        jlOrder.setBackground(lessWhite);

        jtfPath.setBounds(jlOrder.getBounds().x + componentHeight + xMargin,
                initial * id, jlPathWidth, componentHeight);
        jtfPath.setBorder(emptyBorder);
        jtfPath.setBackground(lessWhite);

        jbFc.setBounds(jtfPath.getBounds().x + jlPathWidth + xMargin,
                initial * id, buttonWidth, componentHeight);
        jbFc.setBorderPainted(false);
        jbFc.setBackground(lessWhite);

        jbClone.setBounds(jbFc.getBounds().x + buttonWidth + xMargin, initial * id, buttonWidth, componentHeight);
        jbClone.setBorderPainted(false);
        jbClone.setBackground(lessWhite);

        jbClone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retryClone++;
                String path = jtfPath.getText();
                if (retryClone > 3) {
                    //reset retryClone
                    retryClone = 0;
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
                    //reset retryClone
                    retryClone = 0;
                    prepareProject();
                }
            }
        });

        tickLabel.setBounds(jbClone.getBounds().x + buttonWidth + xMargin,
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
        Thread tClone = new Thread(new Runnable() {
            @Override
            public void run() {
                cloning = true;
                jbClone.setText("Cloning...");
                gitUrl = jtfPath.getText();
                //Get substring of the repository name
                nomRepo = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.indexOf(".git"));
                //Create destination directory
                destPath = System.getProperty("user.dir") + "\\.mvnCompiler_temp\\" + nomRepo;
                ProcessBuilder pb = new ProcessBuilder();
                com = cloneCommand + " " + gitUrl + " " + destPath;
                try {
                    pb.executeCommand(com);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                if (checkClonedDirectory(destPath)) {
                    cloned = true;
                    fc.setPath(destPath);
                    fc.setChosen(true);
                    jtfPath.setText(nomRepo);
                    jbClone.setText("Cloned");
                } else {
                    cloned = false;
                    JOptionPane.showMessageDialog(parentContentPane,
                            "Error cloning the project " + id,
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    jbClone.setText("Error");
                }
                cloning = false;
            }
        });

        tClone.start();
    }

    private boolean checkClonedDirectory(String destPath) {
        return Files.exists(Paths.get(destPath));
    }

    /**
     * Adds project panel components to the JFrame
     *
     * @param contentPane JFrame
     */
    public void addProjectPan(Container contentPane) {
        contentPane.add(this.jlOrder);
        contentPane.add(this.jtfPath);
        contentPane.add(this.jbFc);
        contentPane.add(this.jbClone);
        contentPane.add(this.tickLabel);
    }

    /**
     * Sets Project panel components background color
     */
    public void colorizeProjectPane() {
        jtfPath.setOpaque(true);
        jtfPath.setBackground(moreBlue);
        jtfPath.setBorder(emptyBorder);

        jbFc.setOpaque(true);
        jbFc.setBackground(moreBlue);
        jbFc.setBorderPainted(false);

        jbClone.setOpaque(true);
        jbClone.setBackground(moreBlue);
        jbFc.setBorderPainted(false);
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
     * Getter for jtfPath
     *
     * @return jtfPath
     */
    public JTextField getJtfPath() {
        return jtfPath;
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