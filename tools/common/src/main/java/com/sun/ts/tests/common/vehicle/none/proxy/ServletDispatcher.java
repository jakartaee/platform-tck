package com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.harness.Status;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * A function that dispatches method calls to a remote server using the {@link HttpClient}. This requires that
 * a subclass of {@link ServletNoVehicle} has been deployed to a server under the /appclientproxy/appclient_novehicle
 * path. The test method name is appended to the URL as the 'test' query parameter.
 */
public class ServletDispatcher implements Function<Object[], RemoteStatus> {
    private static final Logger log = Logger.getLogger(ServletDispatcher.class.getName());
    private final String webServerHost;
    private final String webServerPort;

    /**
     * Creates a new instance of the dispatcher with the given host and port.
     * @param webServerHost - the host name of the server
     * @param webServerPort - the port number of the server
     */
    public ServletDispatcher(String webServerHost, String webServerPort) {
        this.webServerHost = webServerHost;
        this.webServerPort = webServerPort;
    }

    @Override
    public RemoteStatus apply(Object[] args) {
        RemoteStatus status;
        // Extract test name
        String testName = args[args.length - 1].toString();
        if (args.length == 1) {
            status = sendGet(testName);
        } else {
            Object[] newArgs = Arrays.copyOfRange(args, 0, args.length-1);
            status = sendPost(testName, newArgs);
        }
        return status;
    }

    /**
     * Sends a GET request to the server with the given method name as test parameter.
     * @param methodName - the name of the method to call
     * @return the status of the method call
     */
    private RemoteStatus sendGet(String methodName) {
        URI uri = URI.create("http://"+webServerHost + ":" + webServerPort + "/appclientproxy/appclient_novehicle?test=" + methodName);
        log.info("Sending GET request to " + uri);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        // Send the request
        HttpResponse<byte[]> response;
        try {
            HttpClient client = HttpClient.newBuilder().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() != 200) {
                String msg = String.format("Failed to send %s(), status code %d, content length: %s",
                        methodName, response.statusCode(), response.headers().firstValue("Content-Length"));
                log.severe(msg);
                return new RemoteStatus(Status.failed("Failed to send " + methodName));
            }
        } catch (Throwable e) {
            return new RemoteStatus(Status.failed("Failed to send " + methodName), e);
        }
        // Read the response
        RemoteStatus status;
        byte[] body = response.body();
        if(body != null && body.length > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(response.body()))) {
                status = (RemoteStatus) ois.readObject();
            } catch (Throwable e) {
                status = new RemoteStatus(Status.failed("Failed to read response body"), e);
            }
        } else {
            status = new RemoteStatus(Status.failed("Failed to read response body, empty response"));
            log.severe("Failed to read response body, empty response");
        }
        return status;
    }

    /**
     * Sends a POST request to the server with the given method name as test parameter and the arguments
     * as the body in the format of a serialized object byte[].
     * @param methodName - the name of the method to call
     * @param args - the arguments to pass to the method
     * @return the status of the method call
     */
    private RemoteStatus sendPost(String methodName, Object[] args) {
        URI uri = URI.create("http://" + webServerHost + ":" + webServerPort + "/appclientproxy/appclient_novehicle?test=" + methodName);
        log.info("Sending POST request to " + uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(args);
            oos.flush();
            oos.close();
        } catch (Throwable e) {
            return new RemoteStatus(Status.failed("Failed to serialize args"), e);
        }
        byte[] body = baos.toByteArray();
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofByteArray(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(publisher)
                .build();
        HttpResponse<byte[]> response;
        try {
            HttpClient client = HttpClient.newBuilder().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() != 200) {
                String msg = String.format("Failed to send init(Properties), status code %d, body=%s",
                        response.statusCode(), response.headers().firstValue("Content-Length"));
                log.severe(msg);
            }
        } catch (Throwable e) {
            return new RemoteStatus(Status.failed("Failed to send " + methodName), e);
        }
        // Read the response
        RemoteStatus status;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(response.body()))) {
            status = (RemoteStatus) ois.readObject();
        } catch (Throwable e) {
            status = new RemoteStatus(Status.failed("Failed to read response body"), e);
        }
        return status;
    }
}
