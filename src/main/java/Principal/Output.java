package Principal;

import javax.swing.*;
import java.awt.*;

public class Output {
    private Container contentPane;

    private JTextArea console;
    private JScrollPane scrollPane;
    private final int output_height = 200;
    private int panel_height;
    private int panel_width;
    private boolean output_visible = false;

    public Output() {

    }



    public void initOutput(Container contentPane, int panel_height, int panel_width) {
        this.contentPane = contentPane;
        this.panel_height = panel_height;
        this.panel_width = panel_width;
    }

    public void addOutput() {
        output_visible = true;

        int previous_panel_height = panel_height;
        panel_height = panel_height + output_height;
        contentPane.setSize(panel_width, panel_height);

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

    public void removeOutput() {
        if (output_visible) {
            output_visible = false;
            contentPane.remove(scrollPane);
            panel_height = panel_height - output_height;
            contentPane.setSize(panel_width, panel_height);
        }
    }

    public boolean isOutput_visible() {
        return output_visible;
    }

    public JTextArea getConsole() {
        return console;
    }
}
