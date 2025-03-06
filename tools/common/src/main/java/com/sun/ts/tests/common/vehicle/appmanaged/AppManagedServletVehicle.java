package com.sun.ts.tests.common.vehicle.appmanaged;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.servlet.ServletVehicle;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The target servlet used by AppManagedVehicleRunner.
 */
@WebServlet(name = "AppManagedServletVehicle", urlPatterns = { "/appmanaged_vehicle" })
public class AppManagedServletVehicle extends ServletVehicle {
    private static final Logger log = Logger.getLogger(AppManagedServletVehicle.class.getName());
    @Inject
    Instance<AppManagedVehicleIF> injectedBean;

    public AppManagedServletVehicle() {
        super();
    }

    @Override
    protected RemoteStatus runTest() throws RemoteException {
        try {
            log.info("AppManagedServletVehicle Instance<AppManagedVehicleIF>: " + injectedBean);
            AppManagedVehicleIF bean = injectedBean.get();
            return bean.runTest(arguments, properties);
        } catch (Exception e) {
            log.log(Level.SEVERE, "runTest failed", e);
            throw new RemoteException("AppManagedServletVehicle.runTest() failed", e);
        }
    }
}
