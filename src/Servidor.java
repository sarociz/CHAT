import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Servidor {
    private List<PrintWriter> clientes = new ArrayList<>();
    private static final int PORT = 12345;
    private static Set<PrintWriter> writers = new HashSet<>();

    public static void main(String[] args) {
        new Servidor().iniciarServidor();
    }

    public void iniciarServidor() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor en línea. Esperando conexiones...");

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                new Thread(new ClientHandler(clienteSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                Scanner in = new Scanner(clientSocket.getInputStream());
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                writers.add(writer);

                while (true) {
                    String message = in.nextLine();
                    if (message.equals("/quit")) {
                        break;
                    }

                    broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    writers.remove(writer);
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcast(String message) {
            for (PrintWriter writer : writers) {
                writer.println(message);
            }
        }
    }

    private void enviarMensajeATodos(String mensaje) {
        System.out.println("Mensaje recibido: " + mensaje);
        for (PrintWriter cliente : clientes) {
            cliente.println(mensaje);
        }
    }
}
