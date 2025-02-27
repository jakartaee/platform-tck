package com.sun.ts.tests.common.vehicle.appmanaged;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF;
import com.sun.ts.tests.common.vehicle.servlet.ServletVehicle;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;

import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * Replaces the remote ejb call used by AppManagedNoTxVehicleRunner with a servlet call.
 */
@WebServlet(name = "AppManagedServletVehicle", urlPatterns = {"/appmanaged_vehicle"})
public class AppManagedServletVehicle extends ServletVehicle {
    private static final Logger log = Logger.getLogger(AppManagedServletVehicle.class.getName());
    @EJB(name = "AppManagedVehicleBean")
    private AppManagedVehicleIF injectedBean;

    public AppManagedServletVehicle() {
        super();
    }

    @Override
    protected RemoteStatus runTest() throws RemoteException {
        try {
            log.info("AppManagedServletVehicle @EJB: " + injectedBean);
            return injectedBean.runTest(arguments, properties);
        } catch (Exception e) {
            throw new RemoteException("AppManagedServletVehicle.runTest() failed", e);
        }
    }
}
