package com.sun.ts.tests.common.vehicle.stateless3;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF;
import com.sun.ts.tests.common.vehicle.servlet.ServletVehicle;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Replaces the remote ejb call used by AppManagedNoTxVehicleRunner with a servlet call.
 */
@WebServlet(name = "Stateless3ServletVehicle", urlPatterns = {"/stateless3_vehicle"})
public class Stateless3ServletVehicle extends ServletVehicle {
    private static final Logger log = Logger.getLogger(Stateless3ServletVehicle.class.getName());
    @EJB(name = "Stateless3VehicleBean")
    private Stateless3VehicleIF injectedBean;

    public Stateless3ServletVehicle() {
        super();
    }

    @Override
    protected RemoteStatus runTest() throws RemoteException {
        try {
            log.info("Stateless3ServletVehicle @EJB: " + injectedBean);
            return injectedBean.runTest(arguments, properties);
        } catch (Exception e) {
            log.log(Level.SEVERE, "runTest failed", e);
            throw new RemoteException("Stateless3ServletVehicle.runTest() failed", e);
        }
    }
}
