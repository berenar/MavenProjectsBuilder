package Principal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

class Main extends JFrame {

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final ArrayList<ProjectPanel> selected_projects = new ArrayList<ProjectPanel>();

    private final int panel_width = (screenSize.width / 3) + 50;
    private int panel_height = 150;

    private Container contentPane;
    private ProjectPanel project_panel;
    private JButton add_project;
    private JButton compile;

    private final int add_project_size = 30;
    private final int compile_width = 200;
    private final int compile_height = 30;

    private final String compileCommand = "mvn clean install";
    private boolean success = true;
    private Timer t;
    private JOptionPane pane;
    private JDialog dialog;

    private JTextArea console;
    private JScrollPane scrollPane;
    private final int output_height = 200;
    private boolean output_visible = false;

    private Main() {
        initUI();
    }

    private void initUI() {
        contentPane = getContentPane();
        contentPane.setLayout(null);

        //TITLE
        JLabel title_app = new JLabel("Select projects to be compiled");
        title_app.setBounds(((panel_width / 2) - 90), 0, panel_width, 50);
        contentPane.add(title_app);

        //PROJECT PANEL
        nouProjectPan();

        //ADD PROJECT BUTTON
        nouAddProject();

        //COMPILE BUTTON
        nouCompile();

        //CONFIGURE JFRAME
        this.setSize(panel_width, panel_height);
        this.setResizable(false);
        this.setLocationRelativeTo(null);//null: centers window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("mvnCompiler 1.1");
        try {
            this.setIconImage(ImageIO.read(getClass().getClassLoader().getResource("mvn_logo_2.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            System.out.println("Error reading image: mvn_logo_2.png");
            System.exit(1);
        }

    }

    private void nouProjectPan() {
        project_panel = new ProjectPanel();
        project_panel.configureProjectPan(selected_projects.size() + 1);
        project_panel.addProjectPan(contentPane);
        selected_projects.add(project_panel);
        panel_height += project_panel.getJl_path_height() + 20;
        this.setSize(panel_width, panel_height);
    }

    private void nouAddProject() {
        add_project = new JButton("+");
        add_project.setMargin(new Insets(0, 0, 0, 0));
        add_project.setFont(new Font("Arial", Font.PLAIN, 20));
        add_project.setBounds(50, panel_height - 100, add_project_size, add_project_size);
        add_project.setBackground(new java.awt.Color(186, 195, 211));
        contentPane.add(add_project);

        add_project.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nouProjectPan();
                removeOutput();
                reSetBounds();
            }
        });
    }

    private void nouCompile() {
        compile = new JButton("Compile all");
        compile.setFont(new Font("Arial", Font.PLAIN, 20));
        compile.setBounds(160 + add_project_size, panel_height - 100, compile_width, compile_height);
        compile.setBackground(new java.awt.Color(186, 195, 211));
        contentPane.add(compile);

        compile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!output_visible) {
                    addOutput();
                }
                //reset success value
                success = true;
                compileChosen();
            }
        });
    }

    private void addOutput() {
        output_visible = true;

        int previous_panel_height = panel_height;
        panel_height = panel_height + output_height;
        this.setSize(panel_width, panel_height);

        console = new JTextArea(10, 50);
        console.setEditable(false);
        console.setBackground(Color.BLACK);
        console.setFont(new Font("Arial", Font.PLAIN, 12));
        console.setForeground(Color.WHITE);

        scrollPane = new JScrollPane(console);
        scrollPane.setBounds(0, previous_panel_height - 12, panel_width - 5, output_height - 15);
        contentPane.add(scrollPane);
        //TODO: llevar
        console.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus ultrices feugiat venenatis. Donec laoreet ligula eu tortor viverra gravida ac at neque. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Cras porttitor dignissim dictum. Maecenas finibus tempus maximus. Suspendisse quis condimentum justo.\n" +
                "Nunc faucibus felis feugiat lectus pretium tempor. Donec molestie dapibus gravida. Suspendisse ut erat at magna dictum dignissim. Vestibulum consectetur nibh non massa vehicula placerat. Vestibulum faucibus quam at enim suscipit fringilla. Integer quis fringilla est, at pulvinar erat. Phasellus aliquam leo non aliquam scelerisque. Nunc condimentum ipsum nec eros accumsan porta. Nam eu sapien nec ante rutrum rutrum vel in nulla. In hac habitasse platea dictumst. Praesent ipsum sem, condimentum a lorem nec, consequat scelerisque diam. Maecenas pharetra consequat nunc suscipit placerat.\n" +
                "Nunc feugiat mattis nibh, sit amet faucibus nisl venenatis eget. Curabitur sapien nunc, euismod sed consectetur ac, convallis in nunc. Nulla ac mollis sapien, a molestie tellus. Suspendisse ut libero sit amet neque imperdiet cursus vitae quis dolor. Nulla semper turpis felis, scelerisque ornare turpis hendrerit dapibus. Nulla et varius ex, ac tincidunt nunc. Nullam nec arcu dictum, placerat ipsum eget, ullamcorper odio. Cras at odio a elit porta interdum. Proin nec convallis tortor, at pretium mi. Curabitur varius sapien sed pretium volutpat. Aliquam ut est sed mauris faucibus semper. Suspendisse varius laoreet nunc. Aenean placerat elit a faucibus fermentum. Nam venenatis tristique turpis non lobortis.\n" +
                "Nulla vel rhoncus tellus. Sed eleifend elit nec quam tempus blandit. Vivamus iaculis augue quis neque dapibus, vel aliquam nisl tristique. Duis tempus sed odio ac imperdiet. Cras vulputate arcu sed iaculis bibendum. Aenean aliquet consectetur diam ut aliquam. In id fermentum tellus. Sed aliquam magna ut tellus sodales, a commodo ex vehicula. Sed mattis elit mollis, euismod felis sed, euismod justo. Sed dignissim mi in pulvinar sodales. Morbi sit amet risus elementum, convallis mauris eu, pulvinar nunc. Mauris hendrerit ante enim, vitae porttitor dui ultrices ut. Pellentesque vulputate sollicitudin scelerisque. Pellentesque id lorem et diam congue pellentesque. Cras justo magna, fringilla ac arcu id, porta dignissim tortor. Suspendisse ac libero eu sapien convallis sagittis ut quis justo.\n" +
                "Aenean a orci porttitor nunc tincidunt condimentum. In eget maximus ante. Morbi laoreet cursus mi, auctor sollicitudin urna dictum vitae. Nullam cursus, tellus in condimentum consequat, leo mi luctus quam, vitae ultricies ipsum urna facilisis tortor. Vivamus in turpis sollicitudin, ultrices mauris eu, interdum lorem. Nam maximus ipsum ut nisl volutpat consectetur. Donec dignissim, sem quis porta mattis, orci augue iaculis nibh, id posuere nisi nisi ac arcu. Aliquam pellentesque nisl in luctus vulputate. Phasellus ex augue, dignissim vitae erat a, dictum auctor metus. In a purus a velit ultrices luctus. Morbi a libero diam.");
    }

    private void removeOutput() {
        if (output_visible) {
            output_visible = false;
            contentPane.remove(scrollPane);
            panel_height = panel_height - output_height;
            setSize(panel_width, panel_height);
        }
    }

    private void compileChosen() {
        ProcessBuilder pb = new ProcessBuilder();
        for (int i = 0; i < selected_projects.size(); i++) {
            if (selected_projects.get(i).getFc().isChosen()) {
                String path = selected_projects.get(i).getFc().getPath();
                try {
                    pb.executeCommand("cd " + "\"" + path + "\"" + " && " + compileCommand, getContentPane());
                } catch (Exception e) {
                    contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    JOptionPane.showMessageDialog(getContentPane(),
                            "Error compiling project number " + selected_projects.get(i).getId() + ".",
                            "Compilation halted",
                            JOptionPane.ERROR_MESSAGE);
                    success = false;
                    break;
                }
                //stick is set to visible but isn't painted until stopToPaintTick()
                selected_projects.get(i).getTickLabel().setVisible(true);
                stopToPaintTick();
            }
        }
        //Last message if all went well
        if (success && anySelectedProjects()) {
            JOptionPane.showMessageDialog(getContentPane(),
                    "All projects have been successfully compiled.",
                    "Success!",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * JDialogs are modal so we can use them to be able to paint sticks meanwhile current execution
     */
    private void stopToPaintTick() {
        t = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                t.stop();
                dialog.dispose();
            }
        });
        t.start();
        pane = new JOptionPane("");
        dialog = pane.createDialog("");
        dialog.setVisible(true);
    }

    private boolean anySelectedProjects() {
        boolean oneOrMore = false;
        for (int i = 0; i < selected_projects.size(); i++) {
            if (selected_projects.get(i).getFc().isChosen()) {
                oneOrMore = true;
            }
        }
        return oneOrMore;
    }

    private void reSetBounds() {
        add_project.setBounds(50, panel_height - 100, add_project_size, add_project_size);
        compile.setBounds(160 + add_project_size, panel_height - 100, compile_width, compile_height);
    }


    public static void main(String[] args) {
        Main ex = new Main();
        ex.setVisible(true);
    }
}