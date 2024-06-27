
package ee.jakarta.tck.persistence.ee.packaging.web.scope;
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
public static Archive<?> getEarTestArchive()
    {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_web_scope.ear");

        {
            WebArchive jpa_ee_packaging_web_scope_web_war = ShrinkWrap.create(WebArchive.class, "jpa_ee_packaging_web_scope_web_war");
            jpa_ee_packaging_web_scope_web_war.addAsWebInfResource("persistence.xml");
            jpa_ee_packaging_web_scope_web_war.addAsWebInfResource("web.xml");
            jpa_ee_packaging_web_scope_web_war.addClass(ee.jakarta.tck.persistence.ee.util.Data.class);
            jpa_ee_packaging_web_scope_web_war.addClass(ee.jakarta.tck.persistence.ee.util.HttpTCKServlet.class);
            jpa_ee_packaging_web_scope_web_war.addClass(ee.jakarta.tck.persistence.ee.packaging.web.scope.ServletTest.class);
            jpa_ee_packaging_web_scope_web_war.addClass(ee.jakarta.tck.persistence.ee.common.Account.class);
            ear.addAsModule(jpa_ee_packaging_web_scope_web_war);

        }
        return ear;
    }

@Test
public void test1() throws Exception     {
    }
}
