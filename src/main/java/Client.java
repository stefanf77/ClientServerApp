import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class Client {
    private PrintWriter out;
    private BufferedReader in;
    private String encodedCredentials;


    public void startConnection(String ip, int port, String username, String password) {
        Socket clientSocket;

        encodeCredentials(username, password);
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendGetRequest(String host, String path) {
        return sendRequest(host, path, "GET");
    }

    public String sendPostRequest(String host, String path) {
        return sendRequest(host, path, "POST");
    }

    private String sendRequest(String host, String path, String type) {
        String request = type + " " + path + " HTTP/1.0 " +
                "Authorization: Basic " + encodedCredentials + " " +
                "Accept: */* " + "Host: "+ host + " " +
                "Connection: Close";

        out.println(request);
        return getServerResponse();
    }

    private String getServerResponse() {
        String line;
        StringBuilder response = new StringBuilder();
        try {
            while ((line = in.readLine()) != null) {
                response.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    private void encodeCredentials(String username, String password) {
        String credentials = username + ":" + password;
        byte[] encodedBytes = Base64.getEncoder().encode(credentials.getBytes());
        encodedCredentials = new String(encodedBytes);
    }
}
