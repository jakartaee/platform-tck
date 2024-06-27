
package ee.jakarta.tck.persistence.core.metamodelapi.entitytype;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_metamodelapi_entitytype_vehicles.ear");

        {

            JavaArchive jpa_core_metamodelapi_entitytype_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_entitytype_jar");
            jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.ZipCode.class);
            jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.DID2EmployeeId.class);
            jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.B.class);
            jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.Address.class);
            jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.A.class);
            jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.DID2Employee.class);
            ear.addAsLibrary(jpa_core_metamodelapi_entitytype_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar");
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.Client.class);
            ear.addAsModule(jpa_core_metamodelapi_entitytype_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar");
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_entitytype_appmanaged_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar");
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.Client.class);
            ear.addAsModule(jpa_core_metamodelapi_entitytype_appmanaged_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war");
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_metamodelapi_entitytype_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_entitytype.jar");
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.ZipCode.class);
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.DID2EmployeeId.class);
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.B.class);
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.Address.class);
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.A.class);
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.DID2Employee.class);
                jpa_core_metamodelapi_entitytype_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_metamodelapi_entitytype_jar.addAsManifestResource("persistence.xml");
                jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_metamodelapi_entitytype_jar);
            }
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.Client.class);
            ear.addAsModule(jpa_core_metamodelapi_entitytype_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war");
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_metamodelapi_entitytype_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_entitytype.jar");
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.ZipCode.class);
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.DID2EmployeeId.class);
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.B.class);
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.Address.class);
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.A.class);
                jpa_core_metamodelapi_entitytype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.DID2Employee.class);
                jpa_core_metamodelapi_entitytype_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_metamodelapi_entitytype_jar.addAsManifestResource("persistence.xml");
                jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addAsLibrary(jpa_core_metamodelapi_entitytype_jar);
            }
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.Client.class);
            ear.addAsModule(jpa_core_metamodelapi_entitytype_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar");
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_entitytype_stateful3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar");
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.Client.class);
            ear.addAsModule(jpa_core_metamodelapi_entitytype_stateful3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar");
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_entitytype_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar");
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.entitytype.Client.class);
            ear.addAsModule(jpa_core_metamodelapi_entitytype_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void getdeclaredversion() throws Exception     {
    }

@Test
public void getidclassattributes() throws Exception     {
    }

@Test
public void hassingleidattribute() throws Exception     {
    }

@Test
public void getbindabletype() throws Exception     {
    }

@Test
public void getdeclaredversionillegalargumentexception() throws Exception     {
    }

@Test
public void hasversionattribute() throws Exception     {
    }

@Test
public void removetestdata() throws Exception     {
    }

@Test
public void getid() throws Exception     {
    }

@Test
public void getversion() throws Exception     {
    }

@Test
public void getsupertype() throws Exception     {
    }

@Test
public void getname() throws Exception     {
    }

@Test
public void getdeclaredidillegalargumentexception() throws Exception     {
    }

@Test
public void getidclassattributesillegalargumentexception() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void getbindablejavatype() throws Exception     {
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
public void getidillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredid() throws Exception     {
    }

@Test
public void getversionillegalargumentexception() throws Exception     {
    }

@Test
public void getidtype() throws Exception     {
    }
}
