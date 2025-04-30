package com.sun.ts.tests.jms.ee20.cditests.ejbweb;

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
@WebServlet(name = "EjbServletTarget", urlPatterns = {"/appclient_novehicle"})
public class EjbServletTarget extends ServletNoVehicle<Client> {
    private static final Logger log = Logger.getLogger(EjbServletTarget.class.getName());
    
    @EJB
    EjbClientIF injectedBean;

    public EjbServletTarget() {
        super();
    }

    @Override
    protected RemoteStatus runTest() throws RemoteException {
        RemoteStatus status = new RemoteStatus(Status.failed("bad testName"));;
        try {
          
            boolean pass = false;
            log.info("EjbServletTarget @EJB: " + injectedBean);
            pass = switch (testName) {
                case "sendRecvQueueTestUsingCDIFromEjb" -> injectedBean.sendRecvQueueTestUsingCDIFromEjb();
                case "sendRecvTopicTestUsingCDIFromEjb" -> injectedBean.sendRecvTopicTestUsingCDIFromEjb();
                case "sendRecvUsingCDIDefaultFactoryFromEjb" -> injectedBean.sendRecvUsingCDIDefaultFactoryFromEjb();
                case "verifySessionModeOnCDIJMSContextFromEjb" -> injectedBean.verifySessionModeOnCDIJMSContextFromEjb();
                case "testRestrictionsOnCDIJMSContextFromEjb" -> injectedBean.testRestrictionsOnCDIJMSContextFromEjb();
                case "testActiveJTAUsingCDICallMethod1FromEjb" -> injectedBean.testActiveJTAUsingCDICallMethod1FromEjb();
                case "testActiveJTAUsingCDICallMethod2FromEjb" -> injectedBean.testActiveJTAUsingCDICallMethod2FromEjb();

                default -> false;
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
