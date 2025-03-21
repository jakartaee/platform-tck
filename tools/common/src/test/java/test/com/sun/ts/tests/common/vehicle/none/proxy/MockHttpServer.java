package test.com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.harness.Status;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class MockHttpServer {
    HttpServer server;
    Object testCase;
    RequestHandler requestHandler;

    public MockHttpServer(Object testCase) {
        this.testCase = testCase;
        this.requestHandler = new RequestHandler(testCase);
    }

    public void run(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/appclientproxy/appclient_novehicle", requestHandler);
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("MockHttpServer started on port " + port);
    }

    public void stop() {
        server.stop(0);
        System.out.println("MockHttpServer stopped");
    }

    static class RequestHandler implements HttpHandler {
        Object testCase;
        RequestHandler(Object testCase) {
            this.testCase = testCase;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String testName = getParameterByName(query, "test");
            System.out.println("Invoking " + testName);
            Method testMethod = null;
            RemoteStatus status = null;
            try {
                testMethod = testCase.getClass().getDeclaredMethod(testName);
            } catch (NoSuchMethodException e) {
                status = new RemoteStatus(Status.failed("Method not found: " + testName));
            }
            if(testMethod != null) {
                try {
                    testMethod.invoke(testCase);
                    status = new RemoteStatus(Status.passed("Test passed"));
                } catch (Throwable e) {
                    status = new RemoteStatus(Status.failed("Test failed: " + testName), e);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(status);
            oos.close();
            byte[] bytes = baos.toByteArray();
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
        // Helper method to extract a specific parameter from a query string
        private String getParameterByName(String query, String paramName) {
            if (query == null) {
                return null;
            }

            String[] params = query.split("&");
            for (String param : params) {
                String[] pair = param.split("=");
                if (pair.length > 1 && pair[0].equals(paramName)) {
                    return URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
                }
            }
            return null;
        }
    }
}
