import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class GUICliente {
    private JPanel panelCliente;
    private JButton salirButton;
    private JTextArea chatTextArea;
    private JTextField mensajeTF;
    private JButton enviarButton;
    private JScrollPane chatScroll;
    private Cliente cliente;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    public JPanel getPanelCliente() {
        return panelCliente;
    }

    public GUICliente(String serverAddress, int port) {
        cliente = new Cliente();
        try {
            socket = new Socket(serverAddress, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mensajeTF.addActionListener(e -> sendMessage());
        enviarButton.addActionListener(e -> sendMessage());
        // Iniciar un hilo para recibir mensajes del servidor
        Thread thread = new Thread(this::receiveMessages);
        thread.start();

    }
    private void receiveMessages() {
        try {
            while (true) {
                String message = (String) inputStream.readObject();
                chatTextArea.append("Mensaje recibido: " + message + "\n");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = mensajeTF.getText();
        if (!message.isEmpty()) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
                mensajeTF.setText(""); // Limpiar el campo de mensaje después de enviar
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {

    }

}
