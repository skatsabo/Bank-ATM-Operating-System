
import java.io.PrintWriter;
import java.util.ArrayList;

public class DataSender implements Runnable {

    private final ArrayList<String> load;
    private final PrintWriter writer;

    public DataSender(ArrayList<String> load, PrintWriter writer) {
        this.load = load;
        this.writer = writer;
    }

    @Override
    public void run() {
        System.out.println("****************************************************");
        for (int i = 0; i < load.size(); i++) {
      
            writer.println(load.get(i));
            writer.flush();
        }
    }
}
