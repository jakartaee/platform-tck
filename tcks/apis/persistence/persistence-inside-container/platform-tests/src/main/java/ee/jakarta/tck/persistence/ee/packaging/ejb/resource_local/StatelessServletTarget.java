package ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local;

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
@WebServlet(name = "StatelessServletTarget", urlPatterns = {"/appclient_novehicle"})
public class StatelessServletTarget extends ServletNoVehicle<Client> {
    private static final Logger log = Logger.getLogger(StatelessServletTarget.class.getName());
    @EJB
    Stateless3IF injectedBean;
    public StatelessServletTarget() {
        super();
    }

    @Override
    protected RemoteStatus runTest() throws RemoteException {
        RemoteStatus status = new RemoteStatus(Status.failed("bad testName"));;
        try {
            log.info("StatelessServletTarget @EJB: " + injectedBean);
            boolean pass = switch (testName) {
                case "test1" -> injectedBean.test1();
                case "test2" -> injectedBean.test2();
                case "test3" -> injectedBean.test3();
                case "test4" -> injectedBean.test4();
                case "test5" -> injectedBean.test5();
                case "test6" -> injectedBean.test6();
                case "test7" -> injectedBean.test7();
                case "test8" -> injectedBean.test8();
                case "test9" -> injectedBean.test9();
                case "test10" -> injectedBean.test10();
                case "test11" -> injectedBean.test11();
                case "test12" -> injectedBean.test12();
                case "test13" -> injectedBean.test13();
                case "test14" -> injectedBean.test14();
                case "test15" -> injectedBean.test15();
                case "removeTestData" -> true;
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
