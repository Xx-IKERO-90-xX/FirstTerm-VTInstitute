package org.vtinstitute.utils;

import org.vtinstitute.utils.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RestApiConnection {

    protected String server = "http://localhost:8080/";
    protected String apiPath = "/api";

    public String getServer() { return server; }
    public String getApiPath() {
        return getServer()+apiPath;
    }

    public void setApiPath(String server, String apiPath) {
        this.server = server;
        this.apiPath = apiPath;
    }

    public RestApiConnection(String server, String apiPath) {
        setApiPath(server, apiPath);
    }

    public boolean isServerAvailable() {
        try {
            URL url = new URL(getServer());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000); // Timeout en milisegundos
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            return (responseCode == 200);
        } catch (IOException e) {
            return false; // Si no se puede conectar, se asume que el servidor no está disponible
        }
    }

    public HttpResponse getRequest(String endpoint) throws IOException {
        HttpURLConnection conn = null;

        URL url = new URL(getApiPath() + endpoint);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        HttpResponse response = new HttpResponse(conn.getResponseCode(), conn.getResponseMessage(), "");

        InputStream stream = null;

        if (conn.getResponseCode() == 200) {
            stream = conn.getInputStream();
        } else {
            stream = conn.getErrorStream();
        }

        if (stream != null) {
            Scanner scanner = new Scanner(stream, "UTF-8");
            String body = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
            response.setBody(body);
        }

        if (conn != null)
            conn.disconnect();

        return response;
    }

    public HttpResponse getRequest(String endPoint, String codeToRetrieve, String secondCode) throws IOException {
        if (secondCode != null && !secondCode.isEmpty()) {
            return getRequest(endPoint + "/" + codeToRetrieve + "/" + secondCode);
        }
        return getRequest(endPoint + "/" + codeToRetrieve);
    }

    public HttpResponse post(String endpoint, String jsonInputString) throws IOException {
        return postOrPut(jsonInputString, endpoint, "POST");
    }

    public HttpResponse put(String endpoint, String jsonInputString) throws IOException {
        return postOrPut(jsonInputString, endpoint, "PUT");
    }

    HttpResponse postOrPut(String jsonInputString, String endpoint, String method) throws IOException {
        HttpURLConnection conn = null;

        URL url = new URL(getApiPath() + endpoint );
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        HttpResponse response = new HttpResponse(conn.getResponseCode(), conn.getResponseMessage(), "");

        InputStream stream = null;

        if (conn.getResponseCode() == 200) {
            stream = conn.getInputStream();
        } else {
            stream = conn.getErrorStream();
        }

        if (stream != null) {
            Scanner scanner = new Scanner(stream, "UTF-8");
            String body = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
            response.setBody(body);
        }

        if (conn != null)
             conn.disconnect();

        return response;
    }

    public void delete(String endPoint, String codeToDelete) throws IOException {
        HttpURLConnection conn = null;
        URL url = new URL(getApiPath() + endPoint + "/" + codeToDelete);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        if (conn.getResponseCode() != 200)
            throw new ConnectException(conn.getResponseMessage());

        if (conn != null)
            conn.disconnect();
    }
}
