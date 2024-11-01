package ejb;

import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateless(name = "StatelessVehicleBean")
@Remote({ VehicleIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
public class StatelessVehicleBean implements VehicleIF {
    public void runTest() {
        System.out.println("StatelessVehicleBean.runTest()");
    }
}
