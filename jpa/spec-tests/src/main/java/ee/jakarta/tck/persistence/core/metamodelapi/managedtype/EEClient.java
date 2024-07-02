
package ee.jakarta.tck.persistence.core.metamodelapi.managedtype;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_metamodelapi_managedtype_vehicles.ear");

        {

            JavaArchive jpa_core_metamodelapi_managedtype_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype_jar");
            jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Department.class);
            jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.A.class);
            jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Project.class);
            jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Order.class);
            jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class);
            jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee.class);
            jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject.class);
            jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.B.class);
            jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class);
            jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person.class);
            jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMPerson.class);
            ear.addAsLibrary(jpa_core_metamodelapi_managedtype_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar");
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar");
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar");
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war");
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_metamodelapi_managedtype_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype.jar");
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Department.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.A.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Project.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Order.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.B.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMPerson.class);
                jpa_core_metamodelapi_managedtype_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_metamodelapi_managedtype_jar.addAsManifestResource("persistence.xml");
                jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_metamodelapi_managedtype_jar);
            }
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war");
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_metamodelapi_managedtype_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype.jar");
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Department.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.A.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Project.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Order.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.B.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person.class);
                jpa_core_metamodelapi_managedtype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMPerson.class);
                jpa_core_metamodelapi_managedtype_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_metamodelapi_managedtype_jar.addAsManifestResource("persistence.xml");
                jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addAsLibrary(jpa_core_metamodelapi_managedtype_jar);
            }
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_managedtype_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar");
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_managedtype_stateful3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar");
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar");
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_managedtype_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar");
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void getsetstringclasstest() throws Exception     {
    }

@Test
public void getdeclaredsingularattributestringclasstest() throws Exception     {
    }

@Test
public void getdeclaredcollectionstringclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getcollectionstringclasstest() throws Exception     {
    }

@Test
public void removetestdata() throws Exception     {
    }

@Test
public void getdeclaredmapstringclassclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getliststringclasstest() throws Exception     {
    }

@Test
public void getdeclaredsetstringillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getpluralattributes() throws Exception     {
    }

@Test
public void getsingularattributestringtest() throws Exception     {
    }

@Test
public void getdeclaredliststringtest() throws Exception     {
    }

@Test
public void getdeclaredsingularattributes() throws Exception     {
    }

@Test
public void getsingularattributestringillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getliststringillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getdeclaredcollectionstringillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getdeclaredmapstringtest() throws Exception     {
    }

@Test
public void getdeclaredliststringclasstest() throws Exception     {
    }

@Test
public void getcollectionstringclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getmapstringillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getliststringclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getdeclaredattributeillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getdeclaredliststringclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getattributes() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void getdeclaredattribute() throws Exception     {
    }

@Test
public void getdeclaredsetstringclasstest() throws Exception     {
    }

@Test
public void getdeclaredpluralattributes() throws Exception     {
    }

@Test
public void getsingularattributes() throws Exception     {
    }

@Test
public void getdeclaredsetstringtest() throws Exception     {
    }

@Test
public void getdeclaredsingularattributestringclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getattribute() throws Exception     {
    }

@Test
public void getdeclaredsetstringclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getliststringtest() throws Exception     {
    }

@Test
public void getdeclaredcollectionstringtest() throws Exception     {
    }

@Test
public void getmapstringclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getsetstringclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getsingularattributestringclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void client() throws Exception     {
    }

@Test
public void createdeployment() throws Exception     {
    }

@Test
public void getmapstringtest() throws Exception     {
    }

@Test
public void getsingularattributestringclasstest() throws Exception     {
    }

@Test
public void getdeclaredliststringillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getmapstringclasstest() throws Exception     {
    }

@Test
public void getdeclaredattributes() throws Exception     {
    }

@Test
public void getattributeillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredsingularattributestringtest() throws Exception     {
    }

@Test
public void getdeclaredsingularattributestringillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getsetstringtest() throws Exception     {
    }

@Test
public void getsetstringillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getpersistencetype() throws Exception     {
    }

@Test
public void setup() throws Exception     {
    }

@Test
public void managedtype() throws Exception     {
    }

@Test
public void getcollectionstringtest() throws Exception     {
    }

@Test
public void getdeclaredcollectionstringclasstest() throws Exception     {
    }

@Test
public void getcollectionstringillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getdeclaredmapstringclassclasstest() throws Exception     {
    }

@Test
public void getdeclaredmapstringillegalargumentexceptiontest() throws Exception     {
    }
}
