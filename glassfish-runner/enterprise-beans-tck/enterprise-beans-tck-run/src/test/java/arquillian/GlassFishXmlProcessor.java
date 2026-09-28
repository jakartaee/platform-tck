package arquillian;

import com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry.Client;

import java.net.URL;
import java.util.logging.Logger;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import tck.arquillian.porting.lib.spi.AbstractTestArchiveProcessor;

public class GlassFishXmlProcessor extends AbstractTestArchiveProcessor {

    static Logger log = Logger.getLogger(GlassFishXmlProcessor.class.getName());

    /**
     * Called on completion of the Arquillian configuration.
     */
    @Override
    public void initalize(@Observes ArquillianDescriptor descriptor) {
        // Must call to setup the ResourceProvider
        super.initalize(descriptor);
    }

    @Override
    public void processClientArchive(JavaArchive clientArchive, Class<?> testClass, URL sunXmlURL) {
       if (testClass.isAssignableFrom(Client.class)) {
           clientArchive.delete("META-INF/sun-application-client.xml");

           URL resURL = Client.class.getResource("/ejb30/bb/session/stateless/annotation/enventry/ejb3_bb_stateless_enventry_client.glassfish-application-client.xml");
           clientArchive.addAsManifestResource(resURL, "glassfish-application-client.xml");

           log.info("Updated " + clientArchive.getName() + " Contents: \n"  + clientArchive.toString(true));
       }
    }

    @Override
    public void processWebArchive(WebArchive webArchive, Class<?> testClass, URL sunXmlURL) {
        String name = webArchive.getName();
    }

    @Override
    public void processRarArchive(JavaArchive warArchive, Class<?> testClass, URL sunXmlURL) {

    }

    @Override
    public void processParArchive(JavaArchive javaArchive, Class<?> aClass, URL url) {

    }

    @Override
    public void processEarArchive(EnterpriseArchive earArchive, Class<?> testClass, URL sunXmlURL) {
        String name = earArchive.getName();
    }

    @Override
    public void processEjbArchive(JavaArchive ejbArchive, Class<?> testClass, URL sunXmlURL) {
        String name = ejbArchive.getName();
    }

}
