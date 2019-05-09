package Principal;

import javax.swing.*;
import java.awt.*;

class Output {
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
