package com.sun.ts.tests.common.vehicle.none.proxy;


import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;

/**
 * This is the target servlet for appclient tests that use a proxy to invoke the client test class injected into
 * a subclass of this servlet.
 */
public abstract class ServletNoVehicle extends HttpServlet {
    // Extract the test name from the request
    protected String testName;

    /**
     * No usage of this setup method currently.
     * @param config the <code>ServletConfig</code> object that contains configuration information for this servlet
     *
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        TestUtil.logTrace("init " + this.getClass().getName() + " ...");
        super.init(config);
    }

    /**
     * GET methods are test method invocations. The test name is passed in as the 'test' parameter.
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param res an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws ServletException on test invocation failure
     * @throws IOException is not thrown
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            TestUtil.logTrace("ServletNoVehicle - In doGet");
            ObjectOutputStream objOutStream = new ObjectOutputStream(res.getOutputStream());
            System.out.println("got outputstream");
            testName = req.getParameter("test");
            RemoteStatus finalStatus = runTest();
            System.out.println("ran test");
            objOutStream.writeObject(finalStatus);
            objOutStream.flush();
            objOutStream.close();
        } catch (Throwable t) {
            System.out.println(t.getMessage());
            TestUtil.logTrace(t.getMessage());
            t.printStackTrace();
            throw new ServletException("test failed to run within the Servlet Vehicle");
        }
    }

    /**
     * POST methods are test method setup invocations. This implementation is based on expecting a single serialized
     * array of arguments to be passed in. The test name is passed in as the last element of the array.
     *
     * @see com.sun.ts.tests.common.vehicle.none.proxy.ServletDispatcher#sendPost(String, Object[])
     *
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param res an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws ServletException
     * @throws IOException - thrown on failure to deserialize the request body
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("In doPost!");
        TestUtil.logTrace("In doPost");
        String[] args = null;
        Properties props = null;
        // Read the request body
        int contentLength = req.getContentLength();
        InputStream inputStream = req.getInputStream();
        try(ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            Object[] payload = (Object[]) objectInputStream.readObject();
            args = (String[]) payload[0];
            if(payload.length > 1) {
                props = (Properties) payload[1];
            }
            System.out.println("Received POST request with body: " + Arrays.toString(args));
        } catch (Exception e) {
            throw new IOException("Failed to deserialize request body", e);
        }
        TestUtil.logTrace("post args: " + Arrays.toString(args));

        try {
            ObjectOutputStream objOutStream = new ObjectOutputStream(res.getOutputStream());
            System.out.println("got outputstream");
            RemoteStatus finalStatus;
            if(props != null) {
                finalStatus = setup(args, props);
            } else {
                finalStatus = setup(args);
            }
            System.out.println("ran setup");
            objOutStream.writeObject(finalStatus);
            objOutStream.flush();
            objOutStream.close();
        } catch (Throwable t) {
            System.out.println(t.getMessage());
            TestUtil.logTrace(t.getMessage());
            t.printStackTrace();
            throw new ServletException("test failed to run within the Servlet Vehicle");
        }
    }

    /**
     * Default setup does nothing for those existing tests that do not require setup.
     * @param args - the arguments to pass to the setup method
     * @return the status of the setup method
     */
    protected RemoteStatus setup(String[] args) {
        return new RemoteStatus(Status.passed("No setup required"));
    }
    protected RemoteStatus setup(String[] args, Properties props) {
        return new RemoteStatus(Status.passed("No setup required"));
    }

    /**
     * Override this method to implement the test method invocation. The name of the test to
     * run is stored in the testName variable.
     *
     * @return the status of the test method
     */
    protected abstract RemoteStatus runTest();
}