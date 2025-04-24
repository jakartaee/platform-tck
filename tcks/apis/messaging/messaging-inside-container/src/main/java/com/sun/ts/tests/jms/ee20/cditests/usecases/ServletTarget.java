package com.sun.ts.tests.jms.ee20.cditests.usecases;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.vehicle.servlet.ServletVehicle;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Replaces the remote ejb call used by Client with a servlet call.
 */
@WebServlet(name = "ServletTarget", urlPatterns = {"/appclient_novehicle"})
public class ServletTarget extends ServletNoVehicle<Client> {
    private static final Logger log = Logger.getLogger(ServletTarget.class.getName());
    
    @EJB
    BMBean1IF injectedBMBean1;

    @EJB
    BMBean2IF injectedBMBean2;

    @EJB
    CMBean1IF injectedCMBean1;

    @EJB
    CMBean2IF injectedCMBean2;


    public ServletTarget() {
        super();
    }

    @Override
    protected RemoteStatus runTest() throws RemoteException {
        RemoteStatus status = new RemoteStatus(Status.failed("bad testName"));;
        try {
          
            boolean pass = true;
            log.info("ServletTarget @EJB: " + injectedBMBean1);
            log.info("ServletTarget @EJB: " + injectedBMBean2);
            log.info("ServletTarget @EJB: " + injectedCMBean1);
            log.info("ServletTarget @EJB: " + injectedCMBean2);
            switch (testName) {
                case "beanUseCaseA" -> injectedCMBean1.method1a(); 
                case "beanUseCaseB" -> injectedCMBean1.method2();
                case "beanUseCaseC" -> injectedCMBean1.method3();
                case "beanUseCaseD" -> injectedCMBean1.method4();
                case "beanUseCaseE" -> injectedBMBean1.method1();
                case "beanUseCaseF" -> injectedBMBean1.method2();
                case "beanUseCaseG" -> injectedBMBean1.method3();
                case "beanUseCaseH" -> injectedBMBean1.method4();
                case "beanUseCaseJ" -> injectedCMBean1.method5();
                case "beanUseCaseK" -> injectedCMBean1.method6();
            };
            if (pass) {
                status = new RemoteStatus(Status.passed(testName));
            } else {
                status = new RemoteStatus(Status.failed(testName));
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "runTest failed", e);
            status = new RemoteStatus(Status.failed(testName+": "+e.getMessage()));
        }
        return status;
    }
}
