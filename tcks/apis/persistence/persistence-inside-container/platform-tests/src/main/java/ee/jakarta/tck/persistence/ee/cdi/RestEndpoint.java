package ee.jakarta.tck.persistence.ee.cdi;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Called by test client using the arquillian rest protocol
 */
@Path("/cdi-persistence")
public class RestEndpoint {

    @Inject
    TestBeanEM testBean;

    @Inject
    TestBeanUtilities testBeanUtilities;

    @GET
    @Path("/injectEntityManagerUsingQualifier")
    public String injectEntityManagerUsingQualifier() throws Exception {
        testBean.injectEntityManagerUsingQualifier();
        return "PASSED";
    }

    @GET
    @Path("/injectEntityManagerFactoryUsingQualifier")
    public String injectEntityManagerFactoryUsingQualifier() throws Exception {
        testBean.injectEntityManagerFactoryUsingQualifier();
        return "PASSED";
    }

    @GET
    @Path("/injectUtilitiesUsingQualifier")
    public String injectUtilitiesUsingQualifier() throws Exception {
        testBeanUtilities.injectUtilitiesUsingQualifier();
        return "PASSED";
    }

}
