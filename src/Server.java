import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1906);
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected with ip address: " + socket.getInetAddress().getHostAddress());
                new Thread(
                        () -> {
                            handlerClient(socket);
                        }
                ).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handlerClient(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            while (true) {
                String request = reader.readLine();
                if (request.equals("shutdown")) {
                    //Runtime.getRuntime().exec("shutdown -s -t 0");
                    System.out.println("Shutted down computer");
                } else if (request.equals("restart")) {
                    //Runtime.getRuntime().exec("shutdown -r -t 0");
                    System.out.println("Restarted computer");
                } else if (request.equals("screenshot")) {
                    System.out.println("Shotting computer");
                    BufferedImage image = new Robot().createScreenCapture(
                            new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())
                    );

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();

                    writer.println(bytes.length);
                    writer.flush();
                    socket.getOutputStream().write(bytes);
                    socket.getOutputStream().flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
