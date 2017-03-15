
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DatabaseHandler {

    private final String filename;
    private final int recordsSize = 1000;

    private File file;
    private PrintWriter out;
    private Scanner in;

    public DatabaseHandler(String filename) {
        this.filename = filename;

        file = new File(filename);
    }

    public void populateRecordsFromFile(ArrayList<Record> records) {
        if (!file.exists()) {
            createFile();
        }

        loadFromFile(records);
    }

    private void createFile() {
        int departmentNumber;

        try {
            out = new PrintWriter(file);

            out.write("accountNumber-departmentNumber-amount\n");

            for (int i = 1; i <= recordsSize; i++) {
                if (i <= recordsSize / 2) {
                    departmentNumber = 1;
                    //STEILE MIA EGGRAFH STO TOPIKO ANTIGRAFO 1 KAI STO ARXEIO EGGRAFVN
                    out.append(i + "-" + departmentNumber + "-" + getRandAmount() + "\n");

                } else {
                    departmentNumber = 2;
                    //STEILE MIA EGGRAFH STO TOPIKO ANTIGRAFO 2 KAI STO ARXEIO EGGRAFVN
                    out.append(i + "-" + departmentNumber + "-" + getRandAmount() + "\n");
                }
            }

            out.close();

            System.out.println("Server >> Create file completed...");
        } catch (FileNotFoundException ex) {
            System.out.println("Server >> Error on create...");
        }
    }

    //*****STOYS Servers PREPEI NA KANV LOAD TO ARXEIO GIA NA MPORV NA TO DIAXEIRISTV OPVW EDV*******
    private void loadFromFile(ArrayList<Record> records) {
        StringTokenizer st;

        try {
            in = new Scanner(file);

            if (in.hasNext()) {
                in.nextLine();
            }

            while (in.hasNext()) {
                st = new StringTokenizer(in.nextLine(), "-");

                records.add(new Record(Integer.parseInt(st.nextToken()), st.nextToken().charAt(0), Float.parseFloat(st.nextToken())));
            }

            in.close();

            System.out.println("Server >> Load file completed...");
        } catch (FileNotFoundException ex) {
            System.out.println("Server >> Error on load...");
        }
    }

    public void updateFile(ArrayList<Record> records) {

        file.delete();

        file = new File(filename);

        try {
            out = new PrintWriter(file);

            out.write("accountNumber-departmentNumber-amount\n");

            for (int i = 0; i < records.size(); i++) {
                out.append(records.get(i).getAccountNumber() + "-" + records.get(i).getDepartmentNumber() + "-" + records.get(i).getAmount() + "\n");
            }

            out.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Server >> Error on update...");
        }
    }

    private float getRandAmount() {
        Random r = new Random();

        return Math.abs(r.nextFloat()) * 10000;
    }
}
