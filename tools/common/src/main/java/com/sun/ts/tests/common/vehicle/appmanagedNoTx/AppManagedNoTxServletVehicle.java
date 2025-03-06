package com.sun.ts.tests.common.vehicle.appmanagedNoTx;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.servlet.ServletVehicle;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The target servlet used by AppManagedNoTxVehicleRunner.
 */
@WebServlet(name = "AppManagedNoTxServletVehicle", urlPatterns = { "/appmanagedNoTx_vehicle" })
public class AppManagedNoTxServletVehicle extends ServletVehicle {
    private static final Logger log = Logger.getLogger(AppManagedNoTxServletVehicle.class.getName());
    @EJB(name = "AppManagedNoTxVehicleBean")
    private AppManagedNoTxVehicleIF injectedBean;

    public AppManagedNoTxServletVehicle() {
        super();
    }

    @Override
    protected RemoteStatus runTest() throws RemoteException {
        try {
            log.info("AppManagedNoTxServletVehicle @EJB: " + injectedBean);
            return injectedBean.runTest(arguments, properties);
        } catch (Exception e) {
            log.log(Level.SEVERE, "runTest failed", e);
            throw new RemoteException("AppManagedNoTxServletVehicle.runTest() failed", e);
        }
    }
}
