
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultithreadingServer implements Runnable {

    private final int port;
    ArrayList<Record> records;

    public MultithreadingServer(int port, ArrayList<Record> records) {
        this.port = port;
        this.records = records;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
       
            System.out.println("Server >> Server started...");

            while (true) {
                Socket departmentSocket = serverSocket.accept();

                new Thread(new DepartmentHandler(departmentSocket, records)).start();
            }
        } catch (Exception ex) {
            System.out.println("Server >> Error making a connection...\n");
        }
    }
}
