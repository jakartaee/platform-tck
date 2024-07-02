
package ee.jakarta.tck.persistence.core.query.flushmode;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_query_flushmode_vehicles.ear");

        {

            JavaArchive jpa_core_query_flushmode_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_flushmode_jar");
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItemException.class);
            jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country.class);
            ear.addAsLibrary(jpa_core_query_flushmode_jar);

        }
        {
            JavaArchive jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar");
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_query_flushmode_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.query.flushmode.Client1.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_query_flushmode_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_query_flushmode_pmservlet_vehicle_web_war");
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_query_flushmode_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_flushmode.jar");
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItemException.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country.class);
                jpa_core_query_flushmode_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_query_flushmode_jar.addAsManifestResource("persistence.xml");
                jpa_core_query_flushmode_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_query_flushmode_jar);
            }
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.query.flushmode.Client1.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_flushmode_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_query_flushmode_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_query_flushmode_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_query_flushmode_puservlet_vehicle_web_war");
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_query_flushmode_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_flushmode.jar");
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItemException.class);
                jpa_core_query_flushmode_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country.class);
                jpa_core_query_flushmode_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_query_flushmode_jar.addAsManifestResource("persistence.xml");
                jpa_core_query_flushmode_puservlet_vehicle_web_war.addAsLibrary(jpa_core_query_flushmode_jar);
            }
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.query.flushmode.Client1.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_flushmode_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_query_flushmode_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_query_flushmode_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_flushmode_stateless3_vehicle_client_jar");
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_flushmode_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_query_flushmode_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_query_flushmode_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_flushmode_stateless3_vehicle_ejb_jar");
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.query.flushmode.Client1.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_flushmode_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_query_flushmode_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void flushmodetest1() throws Exception     {
    }

@Test
public void createdeployment() throws Exception     {
    }

@Test
public void client1() throws Exception     {
    }
}
