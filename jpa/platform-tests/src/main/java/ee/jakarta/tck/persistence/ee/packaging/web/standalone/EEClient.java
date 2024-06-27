
package ee.jakarta.tck.persistence.ee.packaging.web.standalone;
import org.jboss.arquillian.config.descriptor.api.DefaultProtocolDef;
import org.jboss.arquillian.config.impl.extension.ConfigurationRegistrar;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.container.test.impl.MapObject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class EEClient extends Client{


@Deployment(testable = false)
public static WebArchive getWarTestArchive() throws Exception     {
        WebArchive jpa_ee_packaging_web_standalone_component_web_war = ShrinkWrap.create(WebArchive.class, "jpa_ee_packaging_web_standalone_component_web_war");
        jpa_ee_packaging_web_standalone_component_web_war.addAsWebInfResource("web.xml");
        {
            JavaArchive jpa_ee_packaging_web_standalone_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_web_standalone.jar");
            jpa_ee_packaging_web_standalone_jar.addClass(ee.jakarta.tck.persistence.ee.common.Account.class);
            jpa_ee_packaging_web_standalone_jar.addAsManifestResource("MANIFEST.MF");
            jpa_ee_packaging_web_standalone_jar.addAsManifestResource("persistence.xml");
            jpa_ee_packaging_web_standalone_component_web_war.addAsLibrary(jpa_ee_packaging_web_standalone_jar);
        }
        jpa_ee_packaging_web_standalone_component_web_war.addClass(ee.jakarta.tck.persistence.ee.util.Data.class);
        jpa_ee_packaging_web_standalone_component_web_war.addClass(ee.jakarta.tck.persistence.ee.util.HttpTCKServlet.class);
        jpa_ee_packaging_web_standalone_component_web_war.addClass(ee.jakarta.tck.persistence.ee.packaging.web.standalone.ServletTest.class);
        return jpa_ee_packaging_web_standalone_component_web_war;
    }

@Test
public void test1() throws Exception     {
    }
}
