
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private ClientSettings settings;
    private InputStream inStream;
    private PrintStream outStream;

    public Client(String[] args) {
        if (args.length == 2)
            try {
                this.settings = new ClientSettings(Integer.parseInt(args[0]), args[1]);
            } catch (Exception e) {}
        if (this.settings == null)
            this.settings = new ClientSettings();
        inStream = System.in;
        outStream = System.out;
    }

    public Client setInStream(InputStream clientStream) {
        this.inStream = clientStream;
        return this;
    }

    public Client setOutStream(PrintStream outStream) {
        this.outStream = outStream;
        return this;
    }

    public void start() {
        try (Socket socket = new Socket("localhost", settings.getPort())) {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    Scanner scanner = new Scanner(inStream)
            ) {
                while (true)
                    if (in.ready()) {
                        readInput(in);
                        if (printOutput(out, scanner)) break;
                    }
            }
        } catch (IOException ex) {
            this.outStream.println("Can not connect to this port");
        }
    }

    private boolean printOutput(PrintWriter out, Scanner scanner) {
        String msg;
        msg = scanner.nextLine();
        if ("end".equals(msg)) {
            this.outStream.println("client shutdown");
            return true;
        }
        settings.log("<request> " + msg);
        out.println(msg);
        out.flush();
        return false;
    }

    private void readInput(BufferedReader in) throws IOException {
        String line, response = "";
        while ((line = in.readLine()) != null) {
            if ("".equals(line.trim())) break;
            response += line + "\n";
        }
        this.outStream.println(response.replaceAll("\\{\\n\\}", "\n"));
        settings.log("<response> " + response);
    }

}