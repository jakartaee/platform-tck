
package ee.jakarta.tck.persistence.ee.packaging.ejb.standalone;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_ejb_standalone_ejb.ear");

        {
            JavaArchive jpa_ee_packaging_ejb_standalone_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_standalone_client_jar");
            jpa_ee_packaging_ejb_standalone_client_jar.addClass(ee.jakarta.tck.persistence.ee.packaging.ejb.standalone.Client.class);
            jpa_ee_packaging_ejb_standalone_client_jar.addClass(ee.jakarta.tck.persistence.ee.packaging.ejb.standalone.Stateful3IF.class);
            ear.addAsModule(jpa_ee_packaging_ejb_standalone_client_jar);

        }
        return ear;
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void setup() throws Exception     {
    }

@Test
public void test1() throws Exception     {
    }
}
