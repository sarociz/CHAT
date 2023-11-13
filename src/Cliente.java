import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    public Cliente(Socket clientSocket) {
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            System.out.println("Conectado al servidor de chat");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            System.out.print("Ingrese su nombre de usuario: ");
            String username = new BufferedReader(new InputStreamReader(System.in)).readLine();
            writer.println(username);

            Thread inputThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            inputThread.start();

            System.out.println("¡Bienvenido al chat, " + username + "!");
            System.out.println("Comience a escribir mensajes...");

            String userInput;
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while ((userInput = consoleReader.readLine()) != null) {
                writer.println(userInput);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
