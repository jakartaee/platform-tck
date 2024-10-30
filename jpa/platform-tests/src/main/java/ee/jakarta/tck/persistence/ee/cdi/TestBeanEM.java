package ee.jakarta.tck.persistence.ee.cdi;

import jakarta.inject.Inject;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;

import java.util.logging.Logger;

/**
 * A request scoped bean that uses the CTS-EM and CTS-EM-NOTX persistence units
 */
@RequestScoped
public class TestBeanEM {
    Logger logger = Logger.getLogger(TestBeanEM.class.getName());

    //@PersistenceUnit(unitName = "CTS-EM")
    @CtsEmQualifier
    @Inject
    private EntityManager em1;
    //@PersistenceUnit(unitName = "CTS-EM-NOTX")
    @CtsEmNoTxQualifier
    @Inject
    private EntityManager em2;

    public void logMsg(String msg) {
        logger.info(msg);
    }
    public void logTrace(String msg) {
        logger.fine(msg);
    }
    public void logErr(String msg) {
        logger.severe(msg);
    }
    public void logErr(String msg, Throwable e) {
        logger.log(java.util.logging.Level.SEVERE, msg, e);
    }

    public void injectEmUsingQualifier() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        try {
            logMsg( "Test CtsEmNoTxQualifier");
            if (em1 != null) {
                logTrace( "Received non-null EntityManager");
                pass1 = true;
            } else {
                logErr( "Received null EntityManager");
            }
        } catch (Exception e) {
            logErr( "Received unexpected exception", e);
        }
        try {
            logMsg( "Test CtsEmNoTxQualifier");
            if (em2 != null) {
                logTrace( "Received non-null EntityManager");
                pass2 = true;
            } else {
                logErr( "Received null EntityManager");
            }
        } catch (Exception e) {
            logErr( "Received unexpected exception", e);
        }
        if (!pass1 || !pass2) {
            throw new Exception("createEntityManagerSynchronizationTypeMapTest failed");
        }
        logMsg( "Test PASSED");
    }

}
