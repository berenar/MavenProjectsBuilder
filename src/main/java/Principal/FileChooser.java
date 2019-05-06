package Principal;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class FileChooser extends JPanel implements ActionListener {

    private final JButton go;
    private JFileChooser chooser;
    private final String chooser_title = "Select a project directory";
    private JLabel fc_jl_path;

    private boolean chosen = false;

    public FileChooser() {
        go = new JButton("Choose");
        go.setBackground(new java.awt.Color(186, 195, 211));
        go.addActionListener(this);
        add(go);
    }

    public void actionPerformed(ActionEvent e) {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
        chooser.setDialogTitle(chooser_title);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            setTextFit(fc_jl_path, chooser.getSelectedFile().getPath());
            chosen = true;
        } else {
            System.out.println("No Selection");
        }
    }

    void setTextFit(JLabel label, String text) {
        // Get the original Font from client properties
        Font originalFont = (Font) label.getClientProperty("originalfont");
        if (originalFont == null) { // First time we call it: add it
            originalFont = label.getFont();
            label.putClientProperty("originalfont", originalFont);
        }

        int stringWidth = label.getFontMetrics(originalFont).stringWidth(text);
        int componentWidth = label.getWidth();

        if (stringWidth > componentWidth) { // Resize only if needed
            // Find out how much the font can shrink in width.
            double widthRatio = (double) componentWidth / (double) stringWidth;

            // Keep the minimum size
            int newFontSize = (int) Math.floor(originalFont.getSize() * widthRatio);

            // Set the label's font size to the newly determined size.
            label.setFont(new Font(originalFont.getName(), originalFont.getStyle(), newFontSize));
        } else
            label.setFont(originalFont); // Text fits, do not change font size

        label.setText(text);
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    public JButton getGo() {
        return this.go;
    }

    public void setFc_jl_path(JLabel fc_jl_path) {
        this.fc_jl_path = fc_jl_path;
    }

    public JLabel getFc_jl_path() {
        return fc_jl_path;
    }

    public boolean isChosen() {
        return chosen;
    }
}