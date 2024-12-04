package ee.jakarta.tck.persistence.ee.cdi;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

/**
 * A request scoped bean that uses the CTS-EM and CTS-EM2 persistence units
 */
@RequestScoped
public class TestBeanEM {
    Logger logger = Logger.getLogger(TestBeanEM.class.getName());

    @Inject
    @CtsEmQualifier
    private EntityManager entityManager1;

    @Inject
    @CtsEm2Qualifier
    private EntityManager entityManager2;

    @Inject
    @CtsEmQualifier
    private EntityManagerFactory entityManagerFactory1;

    @Inject
    @CtsEm2Qualifier
    private EntityManagerFactory entityManagerFactory2;

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
        logger.log(SEVERE, msg, e);
    }

    public void injectEntityManagerUsingQualifier() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;

        try {
            logMsg("Test @CtsEmQualifier");
            if (entityManager1 != null) {
                logTrace("Received non-null EntityManager");
                pass1 = true;
            } else {
                logErr("Received null EntityManager");
            }
        } catch (Exception e) {
            logErr("Received unexpected exception", e);
        }

        try {
            logMsg("Test @CtsEm2Qualifier");
            if (entityManager2 != null) {
                logTrace("Received non-null EntityManager");
                pass2 = true;
            } else {
                logErr("Received null EntityManager");
            }
        } catch (Exception e) {
            logErr("Received unexpected exception", e);
        }

        if (!pass1 || !pass2) {
            throw new Exception("injectEntityManagerUsingQualifier failed");
        }

        logMsg("Test PASSED");
    }

    public void injectEntityManagerFactoryUsingQualifier() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;

        try {
            logMsg("Test @CtsEmQualifier");
            if (entityManagerFactory1 != null) {
                logTrace("Received non-null EntityManagerFactory1");
                pass1 = true;
            } else {
                logErr("Received null EntityManagerFactory1");
            }
        } catch (Exception e) {
            logErr("Received unexpected exception", e);
        }

        try {
            logMsg("Test @CtsEm2Qualifier");
            if (entityManagerFactory2 != null) {
                logTrace("Received non-null EntityManagerFactory2");
                pass2 = true;
            } else {
                logErr("Received null EntityManagerFactory2");
            }
        } catch (Exception e) {
            logErr("Received unexpected exception", e);
        }

        if (!pass1 || !pass2) {
            throw new Exception("injectEntityManagerFactoryUsingQualifier failed");
        }

        logMsg("Test PASSED");
    }

}
