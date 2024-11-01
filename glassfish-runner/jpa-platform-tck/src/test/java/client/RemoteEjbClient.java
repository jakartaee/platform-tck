package client;

import ejb.VehicleIF;

import javax.naming.InitialContext;

/**
 * Simple appclient main that looks up a remote EJB
 */
public class RemoteEjbClient {
    public static void main(String[] args) throws Exception{
        InitialContext ctx = new InitialContext();
        VehicleIF ejb = (VehicleIF) ctx.lookup("java:comp/env/ejb/StatelessVehicleBean");
        ejb.runTest();
        System.out.println("RemoteEjbClient.main() PASSED");
    }
}
