
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataReceiver implements Runnable {

    private BufferedReader reader;
    private String departmentNumber;

    public DataReceiver(BufferedReader reader, String departmentNumber) {
        this.reader = reader;
        this.departmentNumber = departmentNumber;
    }

    @Override
    public void run() {
        String stream;

        try {
            while ((stream = reader.readLine()) != null) {
                System.out.println("Client " + departmentNumber + " << " + stream);
            }
        } catch (IOException ex) {
            Logger.getLogger(DataReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
