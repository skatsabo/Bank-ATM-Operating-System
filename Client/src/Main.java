
public class Main {

    public static void main(String[] args) {

        new Thread(new Test("1")).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        new Thread(new Test("2")).start();
    }
}
