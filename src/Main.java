import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        String serverAddress = "localhost";


        JFrame frame = new JFrame("Panel principal");
        frame.setContentPane((new GUIMenuPrincipal(port, serverAddress)).getPanel1());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}