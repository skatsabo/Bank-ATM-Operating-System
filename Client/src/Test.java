
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test implements Runnable { 

    private String departmentNumber;
    private static ArrayList<String> load;
    private static PrintWriter writer;
    private static BufferedReader reader;

    public Test(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    @Override
    public void run() {
        load = new ArrayList<>();

        new LoadHandler("Load" + departmentNumber + ".txt", departmentNumber).populateLoadFromFile(load);

        Socket socket;

        try {
            socket = new Socket("localhost", 8911);

            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new DataSender(load, writer)).start();
            new Thread(new DataReceiver(reader, departmentNumber)).start();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
