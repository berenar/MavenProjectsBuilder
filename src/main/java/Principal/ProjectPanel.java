package Principal;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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

    //Repository branches and the selected one
    ArrayList<String> branches = new ArrayList<>();
    String selected;

    //Variables needed to clone a repository
    private String gitUrl;
    private String nomRepo;
    private String destPath;
    private String com;

    //true if the project is being cloned
    private boolean cloning = false;

    //true if project has finished cloning
    private boolean cloned = false;

    //failed user attempts to clone
    private int retryClone;

    private final Container parentContentPane;

    private ProcessBuilder pb;

    /**
     * Initializes project panel components
     *
     * @param compiling pointer to
     * @param pb
     */
    public ProjectPanel(boolean compiling, Container parentContentPane, ProcessBuilder pb) {
        this.pb = pb;
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
                    chooseBranch();
                    if (selected != null){
                        //User has selected a branch
                        prepareProject();
                    }
                }
            }
        });

        tickLabel.setBounds(jbClone.getBounds().x + buttonWidth + xMargin,
                initial * n - 5, tickLabelSize, tickLabelSize);
        tickLabel.setVisible(false);
    }

    /**
     *
     */
    private void chooseBranch() {
        gitUrl = jtfPath.getText();
        String toExecute = branchesCommand + gitUrl;
        try {
            //execute command to get branch names
            pb.executeCommand(toExecute, branches, parentContentPane);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String preSelected;
        if (branches.contains("master")) {
            preSelected = "master";
        } else {
            preSelected = branches.get(0);
        }

        selected = (String) JOptionPane.showInputDialog(parentContentPane,
                "The selected branch will be cloned", "Choose a branch",
                JOptionPane.QUESTION_MESSAGE, null, branches.toArray(), preSelected);
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
                //Get substring of the repository name
                nomRepo = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.indexOf(".git"));
                //Create destination directory
                destPath = System.getProperty("user.dir") + tempDir + nomRepo;
                com = cloneCommand + selected + " " + gitUrl + " " + destPath;
                try {
                    pb.executeCommand(com);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                if (checkClonedDirectory(destPath)) {
                    cloned = true;
                    fc.setPath(destPath);
                    fc.setChosen(true);
                    jtfPath.setText(destPath);
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
     * Getter for jtfPath
     *
     * @return jtfPath
     */
    public JTextField getJtfPath() {
        return jtfPath;
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
     * Getter for Cloned
     *
     * @return if the project has been cloned or not
     */
    public boolean isCloned() {
        return cloned;
    }
}