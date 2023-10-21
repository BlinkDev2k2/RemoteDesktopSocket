import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1906);
            System.out.println("Connect to server successfully");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("Type command remote: ");
                writer.println(sc.nextLine());
                sc.nextLine();
                writer.flush();

                int size = Integer.valueOf(reader.readLine());
                byte[] bytes = new byte[size];
                int byteRead = socket.getInputStream().read(bytes);
                if (byteRead > 0) {
                    System.out.print("Type name image: ");
                    Path path = Paths.get(sc.nextLine() + ".png");
                    Files.write(path, bytes);
                    System.out.println("Screenshot done");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
