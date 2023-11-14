import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GUIServidor {
    private JPanel panelServidor;
    private JTextArea servidorTextArea;
    private JScrollPane ScrollArea;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private JTextArea serverTextArea;



    public GUIServidor(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public JPanel getPanelServidor() {
        return panelServidor;
    }

    public void start() {
        servidorTextArea.append("Servidor en línea. Esperando clientes...\n");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                serverTextArea.append("Nuevo cliente conectado: " + clientSocket + "\n");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = (String) inputStream.readObject();
                    serverTextArea.append("Mensaje recibido de " + socket + ": " + message + "\n");

                    // Enviar el mensaje a todos los clientes conectados
                    for (ClientHandler client : clients) {
                        if (client != this) {
                            client.sendMessage(message);
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
