package com.sun.ts.tests.common.vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Enums for the vehicles found in the vehicle.properties file
 */
public enum VehicleType {
    appclient, appmanaged, appmanagedNoTx, ejb, ejbembed, ejblitejsf, ejblitejsp, ejblitesecuredjsp, ejbliteservlet, ejbliteservlet2, jsp,
    pmservlet, puservlet, servlet, standalone, stateful3, stateless3, web, wsappclient, wsejb, wsservlet, none;

    public static List<VehicleType> toEnumList(String[] types) {
        List<VehicleType> result = new ArrayList<>(types.length);
        for (String type : types) {
            result.add(VehicleType.valueOf(type));
        }
        return result;
    }
}
