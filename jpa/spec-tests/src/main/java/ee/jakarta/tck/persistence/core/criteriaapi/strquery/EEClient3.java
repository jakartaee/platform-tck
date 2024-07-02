
package ee.jakarta.tck.persistence.core.criteriaapi.strquery;
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

public class EEClient3 extends Client3{


@Deployment(testable = false)
public static Archive<?> getEarTestArchive()
    {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriaapi_strquery_vehicles.ear");

        {

            JavaArchive jpa_core_criteriaapi_strquery_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery_jar");
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product_.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItemException.class);
            jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country.class);
            ear.addAsLibrary(jpa_core_criteriaapi_strquery_jar);

        }
        {
            JavaArchive jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar");
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client3.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar");
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriaapi_strquery_appmanaged_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar");
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client3.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war");
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_criteriaapi_strquery_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery.jar");
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItemException.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country.class);
                jpa_core_criteriaapi_strquery_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_criteriaapi_strquery_jar.addAsManifestResource("persistence.xml");
                jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_criteriaapi_strquery_jar);
            }
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client3.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriaapi_strquery_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war");
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_criteriaapi_strquery_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery.jar");
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product_.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItemException.class);
                jpa_core_criteriaapi_strquery_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country.class);
                jpa_core_criteriaapi_strquery_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_criteriaapi_strquery_jar.addAsManifestResource("persistence.xml");
                jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addAsLibrary(jpa_core_criteriaapi_strquery_jar);
            }
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client3.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriaapi_strquery_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar");
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriaapi_strquery_stateful3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar");
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client3.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar");
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriaapi_strquery_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar");
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client3.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void querytest4() throws Exception     {
    }

@Test
public void test_concathavingclause() throws Exception     {
    }

@Test
public void isnullonetoonetest() throws Exception     {
    }

@Test
public void querytest2() throws Exception     {
    }

@Test
public void test_subquery_in() throws Exception     {
    }

@Test
public void test_lowerhavingclause() throws Exception     {
    }

@Test
public void querytest6() throws Exception     {
    }

@Test
public void querytest61() throws Exception     {
    }

@Test
public void querytest22() throws Exception     {
    }

@Test
public void querytest23() throws Exception     {
    }

@Test
public void querytest64() throws Exception     {
    }

@Test
public void querytest47() throws Exception     {
    }

@Test
public void querytest69() throws Exception     {
    }

@Test
public void querytest19() throws Exception     {
    }

@Test
public void test_locatehavingclause() throws Exception     {
    }

@Test
public void querytest17() throws Exception     {
    }

@Test
public void querytest18() throws Exception     {
    }

@Test
public void test_fetchjoin_1x1() throws Exception     {
    }

@Test
public void fetchstringjointypetest() throws Exception     {
    }

@Test
public void createdeployment() throws Exception     {
    }

@Test
public void test_innerjoin_1x1() throws Exception     {
    }

@Test
public void fromiscorrelatedtest() throws Exception     {
    }

@Test
public void querytest52() throws Exception     {
    }

@Test
public void querytest71() throws Exception     {
    }

@Test
public void test_groupbyhaving() throws Exception     {
    }

@Test
public void test_fetchjoin_1xm() throws Exception     {
    }

@Test
public void querytest56() throws Exception     {
    }

@Test
public void querytest15() throws Exception     {
    }

@Test
public void querytest37() throws Exception     {
    }

@Test
public void querytest59() throws Exception     {
    }

@Test
public void querytest16() throws Exception     {
    }

@Test
public void querytest36() throws Exception     {
    }

@Test
public void querytest58() throws Exception     {
    }

@Test
public void test_groupby() throws Exception     {
    }

@Test
public void fetchstringtest() throws Exception     {
    }

@Test
public void test_upperhavingclause() throws Exception     {
    }

@Test
public void test_lengthhavingclause() throws Exception     {
    }
}
