package com.sun.ts.tests.common.vehicle.stateful3;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.servlet.ServletVehicle;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.enterprise.inject.Instance;
import jakarta.servlet.annotation.WebServlet;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The alternate servlet target used by Stateful3VehicleRunner. It injects a Stateful3VehicleIF bean and runs the test
 * using that bean since a stateful EJB cannot be injected directly into a servlet.
 */
@WebServlet(name = "Stateful3ServletVehicle", urlPatterns = {"/stateful3_vehicle"})
public class Stateful3ServletVehicle extends ServletVehicle {
    private static final Logger log = Logger.getLogger(Stateful3ServletVehicle.class.getName());
    @Dependent
    @Inject
    Instance<Stateful3VehicleIF> injectedBean;

    public Stateful3ServletVehicle() {
        super();
    }

    @Override
    protected RemoteStatus runTest() throws RemoteException {
        try {
            log.info("Stateful3ServletVehicle Instance<Stateful3VehicleIF>: " + injectedBean);
            Stateful3VehicleIF bean = injectedBean.get();
            return bean.runTest(arguments, properties);
        } catch (Exception e) {
            log.log(Level.SEVERE, "runTest failed", e);
            throw new RemoteException("Stateful3ServletVehicle.runTest() failed", e);
        }
    }
}
