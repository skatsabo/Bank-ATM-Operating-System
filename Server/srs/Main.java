
import java.util.ArrayList;

public class Main {

    private static final ArrayList<Record> records = new ArrayList<>();
    private static DatabaseHandler dh;

    public static void main(String[] args) {
        dh = new DatabaseHandler("Records.txt");
        dh.populateRecordsFromFile(records);

        new Thread(new MultithreadingServer(8911, records)).start();
    }

    public DatabaseHandler getDatabaseHandler() {
        return dh;
    }
}
