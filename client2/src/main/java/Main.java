import java.io.ByteArrayInputStream;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        String comands1 = "Bobby\nHelpFAQ\nHi, guys! Anybody knows how it works?\n/back\n/exit\nend\n",
                comands2 = "John_Smith\nHelpFAQ\nHello, Bob. No ideas yet, sorry.\n/online\n/back\n/exit\nend\n";

        Client[] clients = new Client[] {
                new Client(new String[]{"8081", "./logs/clients/client1.log"})
                        .setInStream(new ByteArrayInputStream(comands1.getBytes())),
                new Client(new String[]{"8081", "./logs/clients/client3.log"})
                        .setInStream(new ByteArrayInputStream(comands2.getBytes())),
                new Client(new String[]{})
        };
        int cli = 0;
        for (Client cl : clients) {
            Thread.sleep(3000);
            new Thread(null, cl::start, "client").start();
            cli++;
            System.out.println("Client " + cli + " started");
        }
    }

}
