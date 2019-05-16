package Principal;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import static Principal.Constants.*;

class Output {

    //Swing Components
    private JTextArea console;
    private JScrollPane scrollPane;

    //Console height


    //True if the console is visible
    private boolean outputVisible = false;

    /**
     * Adds the console to the bottom of the JFrame and sets outputVisible to true
     *
     * @param contentPane  JFrame contents
     * @param panelHeight JFrame height
     * @param panelWidth  JFrame width
     * @return updated panelHeight
     */
    public int addOutput(Container contentPane, int panelHeight, int panelWidth) {
        int previousPanelHeight = panelHeight;
        panelHeight = panelHeight + outputHeight;
        console = new JTextArea(10, 50);
        console.setEditable(false);
        console.setBackground(mostBlue);
        console.setFont(new Font("Arial", Font.PLAIN, 12));
        console.setForeground(Color.WHITE);

        scrollPane = new JScrollPane(console);
        scrollPane.setBounds(0, previousPanelHeight - 13, panelWidth - 5, outputHeight - 15);
        contentPane.add(scrollPane);

        //auto scroll to the last line
        console.setCaretPosition(console.getDocument().getLength());

        outputVisible = true;
        return panelHeight;
    }

    /**
     * Removes the console of the JFrame and sets outputVisible to false
     *
     * @param contentPane  JFrame contents
     * @param panelHeight JFrame height
     * @return updated panelHeight
     */
    public int removeOutput(Container contentPane, int panelHeight) {
        contentPane.remove(scrollPane);
        panelHeight = panelHeight - outputHeight;

        outputVisible = false;
        return panelHeight;
    }

    /**
     * Getter for outputVisible
     *
     * @return true if outputVisible is true
     */
    public boolean isOutputVisible() {
        return outputVisible;
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

