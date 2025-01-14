package ee.jakarta.tck.persistence.ee.cdi;

import ee.jakarta.tck.persistence.ee.entityManager.Order;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;

import java.net.URL;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for EntityManager lookup via CDI
 */
@ExtendWith(ArquillianExtension.class)
public class ServletEMLookupTest {

    @ArquillianResource
    URL contextPath;

    @Deployment(name = "jpa-cdi-em-inject", testable = false)
    public static WebArchive deployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                                   .addClasses(
                                       RestActivator.class,
                                       RestEndpoint.class,
                                       TestBeanEM.class,
                                       TestBeanUtilities.class,
                                       CtsEmQualifier.class,
                                       CtsEm2Qualifier.class,
                                       CtsEmNoTxQualifier.class);

        // Par
        // the jar with the correct archive name
        JavaArchive jpa_ee_entityManager = ShrinkWrap.create(JavaArchive.class, "jpa_ee_entityManager.jar");

        // The class files
        jpa_ee_entityManager.addClasses(
            Order.class
        );

        // The persistence.xml descriptor
        URL parURL = ServletEMLookupTest.class.getResource("persistence.xml");
        jpa_ee_entityManager.addAsManifestResource(parURL, "persistence.xml");

        // Call the archive processor
        archiveProcessor.processParArchive(jpa_ee_entityManager, ServletEMLookupTest.class, parURL);

        // Web content
        war.addAsLibrary(jpa_ee_entityManager);

        // Call the archive processor
        archiveProcessor.processWebArchive(war, ServletEMLookupTest.class, null);

        return war;
    }

    /*
     * @testName: injectEmUsingQualifierTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:3318; PERSISTENCE:SPEC:1801;
     * PERSISTENCE:SPEC:1804; PERSISTENCE:SPEC:1883.2;
     *
     * @test_Strategy: Inject an EntityManager using a qualifier and injection
     */
    @Test
    public void injectEmUsingQualifierTest() throws Exception {
        try (Response response =
            ClientBuilder.newBuilder()
                         .build()
                         .target(contextPath.toURI().resolve("rest/cdi-persistence/injectEntityManagerUsingQualifier"))
                         .request(APPLICATION_JSON_TYPE)
                         .get()) {

            assertEquals(200, response.getStatus());
        }
    }

    /*
     * @testName: injectEntityManagerFactoryUsingQualifier
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:3318; PERSISTENCE:SPEC:1801;
     * PERSISTENCE:SPEC:1804; PERSISTENCE:SPEC:1883.2;
     *
     * @test_Strategy: Inject an EntityManager using a qualifier and injection
     */
    @Test
    public void injectEntityManagerFactoryUsingQualifier() throws Exception {
        try (Response response =
            ClientBuilder.newBuilder()
                         .build()
                         .target(contextPath.toURI().resolve("rest/cdi-persistence/injectEntityManagerFactoryUsingQualifier"))
                         .request(APPLICATION_JSON_TYPE)
                         .get()) {

            assertEquals(200, response.getStatus());
        }
    }

    /*
     * @testName: injectUtilitiesUsingQualifier
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:3318; PERSISTENCE:SPEC:1801;
     * PERSISTENCE:SPEC:1804; PERSISTENCE:SPEC:1883.2;
     *
     * @test_Strategy: Inject an EntityManager using a qualifier and injection
     */
    @Test
    public void injectUtilitiesUsingQualifier() throws Exception {
        try (Response response =
            ClientBuilder.newBuilder()
                         .build()
                         .target(contextPath.toURI().resolve("rest/cdi-persistence/injectUtilitiesUsingQualifier"))
                         .request(APPLICATION_JSON_TYPE)
                         .get()) {

            assertEquals(200, response.getStatus());
        }
    }


}
