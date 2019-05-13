package Principal;

        import javax.swing.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;

public class repoChooser extends JPanel implements ActionListener {
    private JTextField repo;
    private JButton go;

    public repoChooser(){
        go = new JButton("Git");
        go.addActionListener(this);
        add(go);

        repo = new JTextField();
        repo.setBounds(128, 28, 86, 20);
        repo.setColumns(10);
        add(repo);

        JLabel lblName = new JLabel("Name");
        lblName.setBounds(65, 31, 46, 14);
        add(lblName);


    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public JButton getGo() {
        return go;
    }
}
