
package ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_ejb_resource_local.ear");

        {
            JavaArchive jpa_ee_packaging_ejb_resource_local_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_resource_local_client_jar");
            jpa_ee_packaging_ejb_resource_local_client_jar.addClass(ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local.Stateless3IF.class);
            jpa_ee_packaging_ejb_resource_local_client_jar.addClass(ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local.Client.class);
            ear.addAsModule(jpa_ee_packaging_ejb_resource_local_client_jar);

        }
        {
            JavaArchive jpa_ee_packaging_ejb_resource_local_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_resource_local_ejb_jar");
            jpa_ee_packaging_ejb_resource_local_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local.Stateless3Bean.class);
            jpa_ee_packaging_ejb_resource_local_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local.Stateless3IF.class);
            jpa_ee_packaging_ejb_resource_local_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.common.A.class);
            jpa_ee_packaging_ejb_resource_local_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.common.B.class);
            ear.addAsModule(jpa_ee_packaging_ejb_resource_local_ejb_jar);

        }
        return ear;
    }

@Test
public void test15() throws Exception     {
    }

@Test
public void test14() throws Exception     {
    }

@Test
public void test13() throws Exception     {
    }

@Test
public void test12() throws Exception     {
    }

@Test
public void test11() throws Exception     {
    }

@Test
public void test10() throws Exception     {
    }

@Test
public void test1() throws Exception     {
    }

@Test
public void test4() throws Exception     {
    }

@Test
public void test5() throws Exception     {
    }

@Test
public void test2() throws Exception     {
    }

@Test
public void test3() throws Exception     {
    }

@Test
public void test8() throws Exception     {
    }

@Test
public void test9() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void test6() throws Exception     {
    }

@Test
public void test7() throws Exception     {
    }

@Test
public void setup() throws Exception     {
    }
}
