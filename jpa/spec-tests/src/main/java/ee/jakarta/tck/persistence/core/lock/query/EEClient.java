
package ee.jakarta.tck.persistence.core.lock.query;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_lock_query_vehicles.ear");

        {

            JavaArchive jpa_core_lock_query_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_lock_query_jar");
            jpa_core_lock_query_jar.addClass(ee.jakarta.tck.persistence.core.lock.query.Employee.class);
            jpa_core_lock_query_jar.addClass(ee.jakarta.tck.persistence.core.lock.query.Department.class);
            jpa_core_lock_query_jar.addClass(ee.jakarta.tck.persistence.core.lock.query.Insurance.class);
            ear.addAsLibrary(jpa_core_lock_query_jar);

        }
        {
            JavaArchive jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar");
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_lock_query_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.lock.query.Client.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_lock_query_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_lock_query_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_lock_query_puservlet_vehicle_web_war");
            jpa_core_lock_query_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_lock_query_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_lock_query.jar");
                jpa_core_lock_query_jar.addClass(ee.jakarta.tck.persistence.core.lock.query.Employee.class);
                jpa_core_lock_query_jar.addClass(ee.jakarta.tck.persistence.core.lock.query.Department.class);
                jpa_core_lock_query_jar.addClass(ee.jakarta.tck.persistence.core.lock.query.Insurance.class);
                jpa_core_lock_query_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_lock_query_jar.addAsManifestResource("persistence.xml");
                jpa_core_lock_query_puservlet_vehicle_web_war.addAsLibrary(jpa_core_lock_query_jar);
            }
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.lock.query.Client.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_lock_query_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_lock_query_puservlet_vehicle_web_war);

        }
        return ear;
    }

@Test
public void setlockmodeillegalstateexception() throws Exception     {
    }

@Test
public void removetestdata() throws Exception     {
    }

@Test
public void getlockmodenonselectillegalstateexceptiontest() throws Exception     {
    }

@Test
public void getsingleresulttest() throws Exception     {
    }

@Test
public void getresultlisttest2() throws Exception     {
    }

@Test
public void getresultlisttest1() throws Exception     {
    }

@Test
public void getlockmodeobjecttransactionrequiredexception1test() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void createtestdata() throws Exception     {
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
public void getlockmodeobjectillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getlockmodeobjectillegalargumentexception1test() throws Exception     {
    }
}
