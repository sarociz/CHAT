import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIMenuPrincipal {
    private JPanel panel1;
    private JButton EntrarButton;
    private JButton cancelarButton;
    private JButton servidorButton;

    public JPanel getPanel1() {
        return panel1;
    }

    public GUIMenuPrincipal(int port, String serverAddress) {
        EntrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("Enter your username:");
                if (username != null && !username.trim().isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        new GUICliente(serverAddress, port);
                    });
                    JFrame frame = new JFrame("Panel cliente");
                    frame.setContentPane((new GUICliente(serverAddress, port)).getPanelCliente());
                    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username. Please try again.");
                }
            }
        });
        servidorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    new GUIServidor(port).start();
                });
                JFrame frame = new JFrame("Panel servidor");
                frame.setContentPane((new GUIServidor(port)).getPanelServidor());
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });


    }
}
