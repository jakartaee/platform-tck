
package ee.jakarta.tck.persistence.jpa22.repeatable.convert;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_jpa22_repeatable_converts_vehicles.ear");

        {

            JavaArchive jpa_jpa22_repeatable_converts_jar = ShrinkWrap.create(JavaArchive.class, "jpa_jpa22_repeatable_converts_jar");
            jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.DotConverter.class);
            jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.B.class);
            jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.Address.class);
            jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.Employee3.class);
            jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.NumberToStateConverter.class);
            ear.addAsLibrary(jpa_jpa22_repeatable_converts_jar);

        }
        {
            JavaArchive jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar");
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_jpa22_repeatable_converts_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar");
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.Client.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_jpa22_repeatable_converts_stateless3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar");
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar");
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.Client.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_jpa22_repeatable_converts_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war");
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_jpa22_repeatable_converts_jar = ShrinkWrap.create(JavaArchive.class, "jpa_jpa22_repeatable_converts.jar");
                jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.DotConverter.class);
                jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.B.class);
                jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.Address.class);
                jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.Employee3.class);
                jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.NumberToStateConverter.class);
                jpa_jpa22_repeatable_converts_jar.addAsManifestResource("MANIFEST.MF");
                jpa_jpa22_repeatable_converts_jar.addAsManifestResource("persistence.xml");
                jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addAsLibrary(jpa_jpa22_repeatable_converts_jar);
            }
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.Client.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_jpa22_repeatable_converts_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war");
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_jpa22_repeatable_converts_jar = ShrinkWrap.create(JavaArchive.class, "jpa_jpa22_repeatable_converts.jar");
                jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.DotConverter.class);
                jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.B.class);
                jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.Address.class);
                jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.Employee3.class);
                jpa_jpa22_repeatable_converts_jar.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.NumberToStateConverter.class);
                jpa_jpa22_repeatable_converts_jar.addAsManifestResource("MANIFEST.MF");
                jpa_jpa22_repeatable_converts_jar.addAsManifestResource("persistence.xml");
                jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addAsLibrary(jpa_jpa22_repeatable_converts_jar);
            }
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.jpa22.repeatable.convert.Client.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_jpa22_repeatable_converts_puservlet_vehicle_web_war);

        }
        return ear;
    }

@Test
public void convertstest() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
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
}
