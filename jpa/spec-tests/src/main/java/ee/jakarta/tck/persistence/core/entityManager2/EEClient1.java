
package ee.jakarta.tck.persistence.core.entityManager2;
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

public class EEClient1 extends Client1{


@Deployment(testable = false)
public static Archive<?> getEarTestArchive()
    {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_entityManager2_vehicles.ear");

        {

            JavaArchive jpa_core_entityManager2_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager2_jar");
            jpa_core_entityManager2_jar.addClass(ee.jakarta.tck.persistence.core.entityManager2.Employee.class);
            jpa_core_entityManager2_jar.addClass(ee.jakarta.tck.persistence.core.entityManager2.Order.class);
            jpa_core_entityManager2_jar.addClass(ee.jakarta.tck.persistence.core.entityManager2.DoesNotExist.class);
            ear.addAsLibrary(jpa_core_entityManager2_jar);

        }
        {
            JavaArchive jpa_core_entityManager2_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager2_stateless3_vehicle_client_jar");
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entityManager2_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entityManager2_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_entityManager2_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager2_stateless3_vehicle_ejb_jar");
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.entityManager2.Client1.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entityManager2_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entityManager2_stateless3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar");
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entityManager2_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.entityManager2.Client1.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_entityManager2_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_entityManager2_pmservlet_vehicle_web_war");
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_entityManager2_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager2.jar");
                jpa_core_entityManager2_jar.addClass(ee.jakarta.tck.persistence.core.entityManager2.Employee.class);
                jpa_core_entityManager2_jar.addClass(ee.jakarta.tck.persistence.core.entityManager2.Order.class);
                jpa_core_entityManager2_jar.addClass(ee.jakarta.tck.persistence.core.entityManager2.DoesNotExist.class);
                jpa_core_entityManager2_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_entityManager2_jar.addAsManifestResource("persistence.xml");
                jpa_core_entityManager2_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_entityManager2_jar);
            }
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.entityManager2.Client1.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entityManager2_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entityManager2_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_entityManager2_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_entityManager2_puservlet_vehicle_web_war");
            jpa_core_entityManager2_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_entityManager2_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager2.jar");
                jpa_core_entityManager2_jar.addClass(ee.jakarta.tck.persistence.core.entityManager2.Employee.class);
                jpa_core_entityManager2_jar.addClass(ee.jakarta.tck.persistence.core.entityManager2.Order.class);
                jpa_core_entityManager2_jar.addClass(ee.jakarta.tck.persistence.core.entityManager2.DoesNotExist.class);
                jpa_core_entityManager2_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_entityManager2_jar.addAsManifestResource("persistence.xml");
                jpa_core_entityManager2_puservlet_vehicle_web_war.addAsLibrary(jpa_core_entityManager2_jar);
            }
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.entityManager2.Client1.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entityManager2_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entityManager2_puservlet_vehicle_web_war);

        }
        return ear;
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback9test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback21test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback4test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback12test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback25test() throws Exception     {
    }

@Test
public void client1() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback8test() throws Exception     {
    }

@Test
public void findexceptionstest() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback5test() throws Exception     {
    }

@Test
public void createdeployment() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback17test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback30test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback26test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback13test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback15test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback10test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback7test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback23test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback2test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback32test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback31test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback6test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback1test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback16test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback14test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback29test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback3test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback27test() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void setup() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback24test() throws Exception     {
    }

@Test
public void flushexceptionstest() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback11test() throws Exception     {
    }

@Test
public void entitymanagermethodsruntimeexceptionscauserollback28test() throws Exception     {
    }
}
