package org.example.client;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        ClientsRep rep = new ClientsRep(new String[] {
                "Bobby\nHelpFAQ\nHi, guys! Anybody knows how it works?\n/back\n/exit\nend\n",
                "John_Smith\nHelpFAQ\nHello, Bob. No ideas yet, sorry.\n/online\n/back\n/exit\nend\n"
        });
        rep.addClient(null);

        int cli = 0;
        for (Client cl : rep.getClients()) {
            Thread.sleep(3000);
            new Thread(null, cl::start, "client").start();
            cli++;
            System.out.println("Client " + cli + " started");
        }
    }

}
