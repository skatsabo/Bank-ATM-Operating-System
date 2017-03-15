
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DepartmentHandler implements Runnable {

    private BufferedReader reader;
    private ArrayList<Record> records;

    private Socket departmentSocket;
    private ThreadPoolExecutor queriesThreadPool; 

    public DepartmentHandler(Socket departmentSocket, ArrayList<Record> records) {
        this.departmentSocket = departmentSocket;
        this.records = records;

        queriesThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1000); 

        try {
            reader = new BufferedReader(new InputStreamReader(departmentSocket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Unexpected error...\n");
        }
    }

    @Override
    public void run() {
        String line;
        String[] data;

        try {
            while ((line = reader.readLine()) != null) {
                data = line.split("-");

                WorkerThread worker = new WorkerThread(data, departmentSocket, records);

                queriesThreadPool.execute(worker); 
                
            }
        } catch (IOException ex) {
            // Remove department from departments (ArrayList)
        }
        finally{
            queriesThreadPool.shutdown();
        }
    }
}
