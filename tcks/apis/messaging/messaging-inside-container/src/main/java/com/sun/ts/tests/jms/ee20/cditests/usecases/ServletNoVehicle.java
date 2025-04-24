package com.sun.ts.tests.jms.ee20.cditests.usecases;


import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.util.Properties;

public class ServletNoVehicle<T extends EETest> extends HttpServlet {
    protected Properties properties = null;
    protected String[] arguments = null;
    protected EETest testObj = null;
    protected String testName;

    public void init(ServletConfig config) throws ServletException {
        TestUtil.logTrace("init " + this.getClass().getName() + " ...");
        super.init(config);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            TestUtil.logTrace("ServletVehicle - In doGet");
            Type t = getClass().getGenericSuperclass();
            ParameterizedType pt = (ParameterizedType) t;
            Class<EETest> testClass = (Class<EETest>) pt.getActualTypeArguments()[0];
            Constructor<EETest> ctor = testClass.getDeclaredConstructor();
            this.testObj = ctor.newInstance();

            ObjectOutputStream objOutStream = new ObjectOutputStream(res.getOutputStream());
            System.out.println("got outputstream");
            testName = req.getParameter("test");
            this.properties = new Properties();
            this.properties.setProperty("testName", testName);
            RemoteStatus finalStatus = this.runTest();
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

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("In doPost!");
        TestUtil.logTrace("In doPost");
        this.doGet(req, res);
    }

    protected RemoteStatus runTest() throws RemoteException {
        new RemoteStatus(Status.passed(""));

        RemoteStatus sTestStatus;
        try {
            sTestStatus = new RemoteStatus(this.testObj.run(this.arguments, this.properties));
            if (sTestStatus.getType() == 0) {
                TestUtil.logMsg("Test running in servlet vehicle passed");
            } else {
                TestUtil.logMsg("Test running in servlet vehicle failed");
            }
        } catch (Throwable e) {
            TestUtil.logErr("Test running in servlet vehicle failed", e);
            sTestStatus = new RemoteStatus(Status.failed("Test running in servlet vehicle failed"));
        }

        return sTestStatus;
    }
}