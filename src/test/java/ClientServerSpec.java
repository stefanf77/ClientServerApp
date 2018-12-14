import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClientServerSpec {
    private final int PORT_NUMBER = 6670;
    private final String URL = "127.0.0.1";

    @Test
    public void sendUnauthorizedGetRequest() {
        Client client = new Client();
        client.startConnection(URL, PORT_NUMBER, "Stefan", "wrongPassword");
        String response =  client.sendGetRequest(URL, "/unknown");

        assertTrue(response.contains("HTTP/1.1 401 Unauthorized"));
    }

    @Test
    public void sendUnknownGetRequest() {
        Client client = new Client();
        client.startConnection(URL, PORT_NUMBER, "Stefan", "1234");
        String response =  client.sendGetRequest(URL, "/unknown");

        assertTrue(response.contains("HTTP/1.1 404 Page Not Found"));
    }

    @Test
    public void sendGetIndexRequest() {
        Client client = new Client();
        client.startConnection(URL, PORT_NUMBER,"Stefan", "1234");
        String response =  client.sendGetRequest(URL, "/index");

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Hello World"));
    }

    @Test
    public void sendGetIndexRequestWithAnotherUser() {
        Client client = new Client();
        client.startConnection(URL, PORT_NUMBER,"Marian", "password");
        String response =  client.sendGetRequest(URL, "/index");

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Hello World"));
    }

    @Test
    public void sendGetVersionRequest() {
        Client client = new Client();
        client.startConnection(URL, PORT_NUMBER,"Stefan", "1234");
        String response =  client.sendGetRequest(URL, "/version");

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Current version is"));
    }

    @Test
    public void sendPostVersionRequest() {
        Client client = new Client();
        client.startConnection(URL, PORT_NUMBER,"Stefan", "1234");
        String response =  client.sendPostRequest(URL, "/version");

        assertTrue(response.contains("HTTP/1.1 201 OK"));
        assertTrue(response.contains("Version Created"));
    }

    @Test
    public void sendPostTimelineRequest() {
        Client client = new Client();
        client.startConnection(URL, PORT_NUMBER,"Stefan", "1234");
        String response =  client.sendPostRequest(URL, "/timeline");

        assertTrue(response.contains("HTTP/1.1 201 OK"));
        assertTrue(response.contains("Timeline Created"));
    }

    @Test
    public void sendUnknownPostRequest() {
        Client client = new Client();
        client.startConnection(URL, PORT_NUMBER,"Stefan", "1234");
        String response =  client.sendPostRequest(URL, "/unknown");

        assertTrue(response.contains("HTTP/1.1 404 Page Not Found"));
    }

    @Test
    public void sendUnauthorizedPostRequest() {
        Client client = new Client();
        client.startConnection(URL, PORT_NUMBER,"Stefan", "wrongPassword");
        String response =  client.sendPostRequest(URL, "/unknown");

        assertTrue(response.contains("HTTP/1.1 401 Unauthorized"));
    }
}
