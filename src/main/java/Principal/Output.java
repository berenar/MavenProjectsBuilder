package Principal;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

class Output {

    //Swing Components
    private JTextArea console;
    private JScrollPane scrollPane;

    //Console height
    private final int output_height;

    //True if the console is visible
    private boolean output_visible;

    Color color_out;

    /**
     * Constructor of the class
     */
    public Output() {
        output_height = 200;
        output_visible = false;
        color_out = new Color(0, 53, 102);
    }

    /**
     * Adds the console to the bottom of the JFrame and sets output_visible to true
     *
     * @param contentPane  JFrame contents
     * @param panel_height JFrame height
     * @param panel_width  JFrame width
     * @return updated panel_height
     */
    public int addOutput(Container contentPane, int panel_height, int panel_width) {
        int previous_panel_height = panel_height;
        panel_height = panel_height + output_height;
        console = new JTextArea(10, 50);
        console.setEditable(false);
        console.setBackground(color_out);
        console.setFont(new Font("Arial", Font.PLAIN, 12));
        console.setForeground(Color.WHITE);

        scrollPane = new JScrollPane(console);
        scrollPane.setBounds(0, previous_panel_height - 13, panel_width - 5, output_height - 15);
        contentPane.add(scrollPane);

        //auto scroll to the last line
        console.setCaretPosition(console.getDocument().getLength());

        output_visible = true;
        return panel_height;
    }

    /**
     * Removes the console of the JFrame and sets output_visible to false
     *
     * @param contentPane  JFrame contents
     * @param panel_height JFrame height
     * @return updated panel_height
     */
    public int removeOutput(Container contentPane, int panel_height) {
        contentPane.remove(scrollPane);
        panel_height = panel_height - output_height;

        output_visible = false;
        return panel_height;
    }

    /**
     * Getter for output_visible
     *
     * @return true if output_visible is true
     */
    public boolean isOutput_visible() {
        return output_visible;
    }

    /**
     * Getter for console
     *
     * @return console
     */
    public JTextArea getConsole() {
        return console;
    }
}

