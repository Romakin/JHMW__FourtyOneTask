public class Main {

    public static void main(String[] args) {
        Client client = new Client();
        new Thread(null, client::start, "client").start();
    }

}
