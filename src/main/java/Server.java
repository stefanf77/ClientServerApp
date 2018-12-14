import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Server {
    private PrintWriter out;
    private final String PATH_TO_RESOURCES = "/src/main/resources/";
    private final String CREDENTIALS = "Credentials";
    private final String STATUS_200_FILENAME = "Status200Response";
    private final String STATUS_201_FILENAME = "Status201Response";
    private final String STATUS_401_FILENAME = "Status401Response";
    private final String STATUS_404_FILENAME = "Status404Response";
    private static final Integer PORT_NUMBER = 6670;


    public static void main(String[] args) {
        Server server = new Server();
        server.start(PORT_NUMBER);
    }

    public void start(int port) {
        ServerSocket serverSocket;
        Socket clientSocket;
        BufferedReader in;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String header = in.readLine();
                StringTokenizer st = new StringTokenizer(header);
                String method = st.nextToken();
                String path = st.nextToken();

                if (!buildCredentialsSet().contains(getEncodedCredentials(st))) {
                    unauthorized();
                    clientSocket.close();
                }

                switch (method) {
                    case "GET": {
                        parseGetRequest(path);
                        clientSocket.close();
                        break;
                    }
                    case "POST": {
                        parsePostRequest(path);
                        clientSocket.close();
                        break;
                    }
                    default: {
                        pageNotFound();
                        clientSocket.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseGetRequest(String path) {
        switch (path) {
            case "/index": {
                getIndexPage();
                break;
            }
            case "/version": {
                getVersion();
                break;
            }
            default: {
                pageNotFound();
                break;
            }
        }
    }

    private void parsePostRequest(String path) {
        switch (path) {
            case "/version": {
                createVersion();
                break;
            }
            case "/timeline": {
                createTimeline();
                break;
            }
            default: {
                pageNotFound();
                break;
            }
        }
    }

    private void getIndexPage() {
        writeFromFileToOutBuffer(STATUS_200_FILENAME);
        out.print("Hello World\r\n");
        out.flush();
    }

    private void getVersion() {
        writeFromFileToOutBuffer(STATUS_200_FILENAME);
        out.print("Current version is: 2.1\r\n");
        out.flush();
    }

    private void pageNotFound() {
        writeFromFileToOutBuffer(STATUS_404_FILENAME);
        out.print("Date: " + new Date() + "\r\n");
        out.flush();
    }

    private void unauthorized() {
        writeFromFileToOutBuffer(STATUS_401_FILENAME);
        out.print("Date: " + new Date() + "\r\n");
        out.flush();
    }

    private void createVersion() {
        writeFromFileToOutBuffer(STATUS_201_FILENAME);
        out.print("Version Created\r\n");
        out.flush();
    }

    private void createTimeline() {
        writeFromFileToOutBuffer(STATUS_201_FILENAME);
        out.print("Timeline Created\r\n");
        out.flush();
    }

    private String getEncodedCredentials(StringTokenizer st) {
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals("Basic")) {
                return st.nextToken();
            }
        }
        return "";
    }

    private Set<String> buildCredentialsSet() {
        Set<String> credentialsSet = new HashSet<>();

        String path = new File(".").getAbsolutePath();
        try (BufferedReader br = new BufferedReader(new FileReader(path + PATH_TO_RESOURCES + CREDENTIALS))) {
            String credentials;
            while ((credentials = br.readLine()) != null) {
                credentialsSet.add(credentials);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return credentialsSet;
    }

    private void writeFromFileToOutBuffer(String filename) {
        String path = new File(".").getAbsolutePath();
        try (BufferedReader br = new BufferedReader(new FileReader(path + PATH_TO_RESOURCES + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.print(line + "\r\n");
            }
            out.print("\r\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
