package ee.jakarta.tck.persistence.ee.cdi;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Called by test client using the arquillian rest protocol
 */
@Path("/cdi-jpa")
public class RestEndpoint {
    @Inject
    TestBeanEM testBean;

    @GET
    @Path("/injectEmUsingQualifier")
    public String injectEmUsingQualifier() throws Exception {
        testBean.injectEmUsingQualifier();
        return "PASSED";
    }


}
