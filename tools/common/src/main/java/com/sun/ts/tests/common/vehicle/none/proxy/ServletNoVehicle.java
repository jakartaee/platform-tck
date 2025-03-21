package com.sun.ts.tests.common.vehicle.none.proxy;


import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.util.TestUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.ObjectOutputStream;

public abstract class ServletNoVehicle extends HttpServlet {
    protected String testName;

    public void init(ServletConfig config) throws ServletException {
        TestUtil.logTrace("init " + this.getClass().getName() + " ...");
        super.init(config);
    }

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

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("In doPost!");
        TestUtil.logTrace("In doPost");
        this.doGet(req, res);
    }

    protected abstract RemoteStatus runTest();
}