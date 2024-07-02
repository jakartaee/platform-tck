
package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder;
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

public class EEClient2 extends Client2{


@Deployment(testable = false)
public static Archive<?> getEarTestArchive()
    {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriapia_CriteriaBuilder_vehicles.ear");

        {

            JavaArchive jpa_core_criteriapia_CriteriaBuilder_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_jar");
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product_.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItemException.class);
            jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country.class);
            ear.addAsLibrary(jpa_core_criteriapia_CriteriaBuilder_jar);

        }
        {
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar");
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar");
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar");
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war");
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_criteriapia_CriteriaBuilder_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder.jar");
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItemException.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_criteriapia_CriteriaBuilder_jar.addAsManifestResource("persistence.xml");
                jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_criteriapia_CriteriaBuilder_jar);
            }
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war");
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_criteriapia_CriteriaBuilder_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder.jar");
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Department_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.ShelfLife.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Phone_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Address.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Info_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Trim.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.HardwareProduct_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.CreditCard_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.SoftwareProduct_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Spouse_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItem.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Employee.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Product_.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Customer.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Alias.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Order.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.LineItemException.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Country.class);
                jpa_core_criteriapia_CriteriaBuilder_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_criteriapia_CriteriaBuilder_jar.addAsManifestResource("persistence.xml");
                jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addAsLibrary(jpa_core_criteriapia_CriteriaBuilder_jar);
            }
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar");
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar");
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar");
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar");
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.schema30.Util.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void trimbothexptest() throws Exception     {
    }

@Test
public void tupleintclasstest() throws Exception     {
    }

@Test
public void isnull() throws Exception     {
    }

@Test
public void multiroottest() throws Exception     {
    }

@Test
public void notlikeexpexpchartest() throws Exception     {
    }

@Test
public void concatexpexptest() throws Exception     {
    }

@Test
public void construct() throws Exception     {
    }

@Test
public void expressionincollectiontest() throws Exception     {
    }

@Test
public void andpredicates() throws Exception     {
    }

@Test
public void likeexpstringtest() throws Exception     {
    }

@Test
public void constructillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void concatstringexptest() throws Exception     {
    }

@Test
public void arrayillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void concatexpstringtest() throws Exception     {
    }

@Test
public void expressioninexpressionarraytest() throws Exception     {
    }

@Test
public void array() throws Exception     {
    }

@Test
public void criteriabuilderin1test() throws Exception     {
    }

@Test
public void notlikeexpstringexptest() throws Exception     {
    }

@Test
public void tupletoarraytest() throws Exception     {
    }

@Test
public void isnotnull() throws Exception     {
    }

@Test
public void parameter() throws Exception     {
    }

@Test
public void createdeployment() throws Exception     {
    }

@Test
public void notlikeexpstringchartest() throws Exception     {
    }

@Test
public void likeexpexptest() throws Exception     {
    }

@Test
public void notlikeexpstringtest() throws Exception     {
    }

@Test
public void expressioninexpressiontest() throws Exception     {
    }

@Test
public void notlikeexpexptest() throws Exception     {
    }

@Test
public void expressioninobjectarraytest() throws Exception     {
    }

@Test
public void tupleselectionarraytest() throws Exception     {
    }

@Test
public void criteriabuilderin2test() throws Exception     {
    }

@Test
public void parameterexpressionisnulltest() throws Exception     {
    }

@Test
public void lower() throws Exception     {
    }

@Test
public void nullifexpressionexpressiontest() throws Exception     {
    }

@Test
public void tupleinttest() throws Exception     {
    }

@Test
public void notlikeexpexpexptest() throws Exception     {
    }

@Test
public void tupleelementgetjavatypetest() throws Exception     {
    }

@Test
public void criteriabuilderinvaluetest() throws Exception     {
    }

@Test
public void orpredicates() throws Exception     {
    }

@Test
public void criteriabuildervaluestest() throws Exception     {
    }

@Test
public void selectmultiselecttest() throws Exception     {
    }

@Test
public void countdistinct() throws Exception     {
    }

@Test
public void parameterexpressionisnotnulltest() throws Exception     {
    }

@Test
public void tuplegetintclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void nullifexpressionobjecttest() throws Exception     {
    }

@Test
public void parametercasesensitivetest() throws Exception     {
    }
}
