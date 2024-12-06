package ee.jakarta.tck.persistence.ee.cdi;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Cache;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.persistence.SchemaManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.metamodel.Metamodel;

import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

/**
 * A request scoped bean that uses the CTS-EM and CTS-EM2 persistence units
 */
@RequestScoped
public class TestBeanUtilities {
    Logger logger = Logger.getLogger(TestBeanUtilities.class.getName());

    @Inject
    @CtsEmQualifier
    private CriteriaBuilder criteriaBuilder;

    @Inject
    @CtsEmQualifier
    private PersistenceUnitUtil persistenceUnitUtil;

    @Inject
    @CtsEmQualifier
    private Cache cache;

    @Inject
    @CtsEmQualifier
    private Metamodel metamodel;

    @Inject
    @CtsEmQualifier
    private SchemaManager schemaManager;

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

    public void injectUtilitiesUsingQualifier() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        boolean pass3 = false;
        boolean pass4 = false;
        boolean pass5 = false;

        try {
            logMsg("Test @CtsEmQualifier");
            if (criteriaBuilder != null) {
                logTrace("Received non-null criteriaBuilder");
                pass1 = true;
            } else {
                logErr("Received null criteriaBuilder");
            }
        } catch (Exception e) {
            logErr("Received unexpected exception", e);
        }

        try {
            logMsg("Test @CtsEmQualifier");
            if (persistenceUnitUtil != null) {
                logTrace("Received non-null persistenceUnitUtil");
                pass2 = true;
            } else {
                logErr("Received null persistenceUnitUtil");
            }
        } catch (Exception e) {
            logErr("Received unexpected exception", e);
        }

        try {
            logMsg("Test @CtsEmQualifier");
            if (cache != null) {
                logTrace("Received non-null cache");
                pass3 = true;
            } else {
                logErr("Received null cache");
            }
        } catch (Exception e) {
            logErr("Received unexpected exception", e);
        }

        try {
            logMsg("Test @CtsEmQualifier");
            if (metamodel != null) {
                logTrace("Received non-null metamodel");
                pass4 = true;
            } else {
                logErr("Received null metamodel");
            }
        } catch (Exception e) {
            logErr("Received unexpected exception", e);
        }

        try {
            logMsg("Test @CtsEmQualifier");
            if (schemaManager != null) {
                logTrace("Received non-null schemaManager");
                pass5 = true;
            } else {
                logErr("Received null schemaManager");
            }
        } catch (Exception e) {
            logErr("Received unexpected exception", e);
        }

        if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5) {
            throw new Exception("injection failed");
        }

        logMsg("Test PASSED");
    }

}
