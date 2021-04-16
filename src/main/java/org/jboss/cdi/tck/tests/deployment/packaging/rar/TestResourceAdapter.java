package org.jboss.cdi.tck.tests.deployment.packaging.rar;

import java.util.logging.Logger;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ActivationSpec;
import jakarta.resource.spi.BootstrapContext;
import jakarta.resource.spi.ResourceAdapter;
import jakarta.resource.spi.ResourceAdapterInternalException;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;

/**
 * TestResourceAdapter
 * 
 * @version $Revision: $
 */
public class TestResourceAdapter implements ResourceAdapter, java.io.Serializable {

    /** The serial version UID */
    private static final long serialVersionUID = 1L;

    /** The logger */
    private static Logger log = Logger.getLogger("TestResourceAdapter");

    /**
     * Default constructor
     */
    public TestResourceAdapter() {

    }

    /**
     * This is called during the activation of a message endpoint.
     * 
     * @param endpointFactory A message endpoint factory instance.
     * @param spec An activation spec JavaBean instance.
     * @throws ResourceException generic exception
     */
    public void endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) throws ResourceException {
        log.finest("endpointActivation()");
    }

    /**
     * This is called when a message endpoint is deactivated.
     * 
     * @param endpointFactory A message endpoint factory instance.
     * @param spec An activation spec JavaBean instance.
     */
    public void endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) {
        log.finest("endpointDeactivation()");
    }

    /**
     * This is called when a resource adapter instance is bootstrapped.
     * 
     * @param ctx A bootstrap context containing references
     * @throws ResourceAdapterInternalException indicates bootstrap failure.
     */
    public void start(BootstrapContext ctx) throws ResourceAdapterInternalException {
        log.finest("start()");
    }

    /**
     * This is called when a resource adapter instance is undeployed or during application server shutdown.
     */
    public void stop() {
        log.finest("stop()");
    }

    /**
     * This method is called by the application server during crash recovery.
     * 
     * @param specs An array of ActivationSpec JavaBeans
     * @throws ResourceException generic exception
     * @return An array of XAResource objects
     */
    public XAResource[] getXAResources(ActivationSpec[] specs) throws ResourceException {
        log.finest("getXAResources()");
        return null;
    }

    /**
     * Returns a hash code value for the object.
     * 
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = 17;
        return result;
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param other The reference object with which to compare.
     * @return true if this object is the same as the obj argument, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (!(other instanceof TestResourceAdapter))
            return false;
        @SuppressWarnings("unused")
        TestResourceAdapter obj = (TestResourceAdapter) other;
        boolean result = true;
        return result;
    }

}
