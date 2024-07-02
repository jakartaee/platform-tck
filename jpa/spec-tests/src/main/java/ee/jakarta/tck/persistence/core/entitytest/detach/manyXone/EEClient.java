
package ee.jakarta.tck.persistence.core.entitytest.detach.manyXone;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_et_detach_manyXone_vehicles.ear");

        {

            JavaArchive jpa_core_et_detach_manyXone_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_detach_manyXone_jar");
            jpa_core_et_detach_manyXone_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.detach.manyXone.A.class);
            jpa_core_et_detach_manyXone_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.detach.manyXone.B.class);
            ear.addAsLibrary(jpa_core_et_detach_manyXone_jar);

        }
        {
            WebArchive jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war");
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_et_detach_manyXone_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_detach_manyXone.jar");
                jpa_core_et_detach_manyXone_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.detach.manyXone.A.class);
                jpa_core_et_detach_manyXone_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.detach.manyXone.B.class);
                jpa_core_et_detach_manyXone_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_et_detach_manyXone_jar.addAsManifestResource("persistence.xml");
                jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_et_detach_manyXone_jar);
            }
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.entitytest.detach.manyXone.Client.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_detach_manyXone_pmservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar");
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_detach_manyXone_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar");
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.detach.manyXone.Client.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_detach_manyXone_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void detachmx1test1() throws Exception     {
    }

@Test
public void removetestdata() throws Exception     {
    }

@Test
public void client() throws Exception     {
    }

@Test
public void createdeployment() throws Exception     {
    }

@Test
public void setup() throws Exception     {
    }

@Test
public void createa() throws Exception     {
    }
}
