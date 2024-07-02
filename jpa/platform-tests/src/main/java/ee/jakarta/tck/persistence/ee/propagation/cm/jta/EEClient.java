
package ee.jakarta.tck.persistence.ee.propagation.cm.jta;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_propagation_cm_jta.ear");

        {

            JavaArchive jpa_ee_propagation_cm_jta_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_propagation_cm_jta_jar");
            jpa_ee_propagation_cm_jta_jar.addClass(ee.jakarta.tck.persistence.ee.common.A.class);
            jpa_ee_propagation_cm_jta_jar.addClass(ee.jakarta.tck.persistence.ee.common.Account.class);
            jpa_ee_propagation_cm_jta_jar.addClass(ee.jakarta.tck.persistence.ee.common.B.class);
            ear.addAsLibrary(jpa_ee_propagation_cm_jta_jar);

        }
        {
            JavaArchive jpa_ee_propagation_cm_jta_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_propagation_cm_jta_ejb_jar");
            jpa_ee_propagation_cm_jta_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.propagation.cm.jta.TellerBean2.class);
            jpa_ee_propagation_cm_jta_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.propagation.cm.jta.Teller.class);
            jpa_ee_propagation_cm_jta_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.propagation.cm.jta.TellerBean.class);
            ear.addAsModule(jpa_ee_propagation_cm_jta_ejb_jar);

        }
        {
            WebArchive jpa_ee_propagation_cm_jta_web_war = ShrinkWrap.create(WebArchive.class, "jpa_ee_propagation_cm_jta_web_war");
            jpa_ee_propagation_cm_jta_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_ee_propagation_cm_jta_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_propagation_cm_jta.jar");
                jpa_ee_propagation_cm_jta_jar.addClass(ee.jakarta.tck.persistence.ee.common.A.class);
                jpa_ee_propagation_cm_jta_jar.addClass(ee.jakarta.tck.persistence.ee.common.Account.class);
                jpa_ee_propagation_cm_jta_jar.addClass(ee.jakarta.tck.persistence.ee.common.B.class);
                jpa_ee_propagation_cm_jta_jar.addAsManifestResource("MANIFEST.MF");
                jpa_ee_propagation_cm_jta_jar.addAsManifestResource("persistence.xml");
                jpa_ee_propagation_cm_jta_web_war.addAsLibrary(jpa_ee_propagation_cm_jta_jar);
            }
            {
                JavaArchive jpa_ee_propagation_cm_jta_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_propagation_cm_jta_ejb.jar");
                jpa_ee_propagation_cm_jta_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.propagation.cm.jta.TellerBean2.class);
                jpa_ee_propagation_cm_jta_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.propagation.cm.jta.Teller.class);
                jpa_ee_propagation_cm_jta_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.propagation.cm.jta.TellerBean.class);
                jpa_ee_propagation_cm_jta_ejb_jar.addAsManifestResource("MANIFEST.MF");
                jpa_ee_propagation_cm_jta_web_war.addAsLibrary(jpa_ee_propagation_cm_jta_ejb_jar);
            }
            jpa_ee_propagation_cm_jta_web_war.addClass(ee.jakarta.tck.persistence.ee.util.Data.class);
            jpa_ee_propagation_cm_jta_web_war.addClass(ee.jakarta.tck.persistence.ee.util.HttpTCKServlet.class);
            jpa_ee_propagation_cm_jta_web_war.addClass(ee.jakarta.tck.persistence.ee.propagation.cm.jta.Teller.class);
            jpa_ee_propagation_cm_jta_web_war.addClass(ee.jakarta.tck.persistence.ee.propagation.cm.jta.ServletTest.class);
            ear.addAsModule(jpa_ee_propagation_cm_jta_web_war);

        }
        return ear;
    }

@Test
public void gettransactionillegalstateexception() throws Exception     {
    }

@Test
public void removeobjecttransactionrequiredexceptiontest() throws Exception     {
    }

@Test
public void test1() throws Exception     {
    }

@Test
public void refreshobjectlockmodetypemaptransactionrequiredexceptiontest() throws Exception     {
    }

@Test
public void refreshobjectmaptransactionrequiredexceptiontest() throws Exception     {
    }

@Test
public void test2() throws Exception     {
    }

@Test
public void mergeobjecttransactionrequiredexceptiontest() throws Exception     {
    }

@Test
public void test3() throws Exception     {
    }

@Test
public void test1a() throws Exception     {
    }

@Test
public void refreshobjecttransactionrequiredexceptiontest() throws Exception     {
    }

@Test
public void closeobjecttransactionrequiredexceptiontest() throws Exception     {
    }

@Test
public void persistobjecttransactionrequiredexceptiontest() throws Exception     {
    }

@Test
public void refreshobjectlockmodetypetransactionrequiredexceptiontest() throws Exception     {
    }
}
