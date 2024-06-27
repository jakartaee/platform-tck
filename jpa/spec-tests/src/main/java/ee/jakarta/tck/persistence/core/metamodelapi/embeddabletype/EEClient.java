
package ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_metamodelapi_embeddabletype_vehicles.ear");

        {

            JavaArchive jpa_core_metamodelapi_embeddabletype_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_embeddabletype_jar");
            jpa_core_metamodelapi_embeddabletype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.ZipCode.class);
            jpa_core_metamodelapi_embeddabletype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.A.class);
            jpa_core_metamodelapi_embeddabletype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.Address.class);
            ear.addAsLibrary(jpa_core_metamodelapi_embeddabletype_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar");
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.Client.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_metamodelapi_embeddabletype_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar");
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar");
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.Client.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_embeddabletype_appmanaged_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war");
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_metamodelapi_embeddabletype_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_embeddabletype.jar");
                jpa_core_metamodelapi_embeddabletype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.ZipCode.class);
                jpa_core_metamodelapi_embeddabletype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.A.class);
                jpa_core_metamodelapi_embeddabletype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.Address.class);
                jpa_core_metamodelapi_embeddabletype_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_metamodelapi_embeddabletype_jar.addAsManifestResource("persistence.xml");
                jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_metamodelapi_embeddabletype_jar);
            }
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.Client.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_embeddabletype_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war");
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_metamodelapi_embeddabletype_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_embeddabletype.jar");
                jpa_core_metamodelapi_embeddabletype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.ZipCode.class);
                jpa_core_metamodelapi_embeddabletype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.A.class);
                jpa_core_metamodelapi_embeddabletype_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.Address.class);
                jpa_core_metamodelapi_embeddabletype_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_metamodelapi_embeddabletype_jar.addAsManifestResource("persistence.xml");
                jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addAsLibrary(jpa_core_metamodelapi_embeddabletype_jar);
            }
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.Client.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_embeddabletype_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar");
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar");
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.Client.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_embeddabletype_stateful3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar");
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar");
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype.Client.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_metamodelapi_embeddabletype_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void getdeclaredliststring() throws Exception     {
    }

@Test
public void getdeclaredcollectionstringclassillegalargumentexception() throws Exception     {
    }

@Test
public void getsingularattributestringillegalargumentexception() throws Exception     {
    }

@Test
public void removetestdata() throws Exception     {
    }

@Test
public void getdeclaredliststringillegalargumentexception() throws Exception     {
    }

@Test
public void getsetstringclassillegalargumentexception() throws Exception     {
    }

@Test
public void getpluralattributes() throws Exception     {
    }

@Test
public void getdeclaredsingularattributestringillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredsingularattributes() throws Exception     {
    }

@Test
public void getsingularattributestringclassillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredliststringclassillegalargumentexception() throws Exception     {
    }

@Test
public void getsingularattributestringclass() throws Exception     {
    }

@Test
public void getmapstring() throws Exception     {
    }

@Test
public void getmapstringillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredmapstringclassclass() throws Exception     {
    }

@Test
public void getdeclaredmapstring() throws Exception     {
    }

@Test
public void getdeclaredattributeillegalargumentexception() throws Exception     {
    }

@Test
public void getattributes() throws Exception     {
    }

@Test
public void getdeclaredsetstringclass() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void getsetstringillegalargumentexception() throws Exception     {
    }

@Test
public void getcollectionstring() throws Exception     {
    }

@Test
public void getdeclaredattribute() throws Exception     {
    }

@Test
public void getcollectionstringillegalargumentexception() throws Exception     {
    }

@Test
public void getcollectionstringclass() throws Exception     {
    }

@Test
public void getdeclaredpluralattributes() throws Exception     {
    }

@Test
public void getsingularattributes() throws Exception     {
    }

@Test
public void getdeclaredliststringclass() throws Exception     {
    }

@Test
public void getattribute() throws Exception     {
    }

@Test
public void getmapstringclassclass() throws Exception     {
    }

@Test
public void getliststringclassillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredsetstring() throws Exception     {
    }

@Test
public void getsingularattributestring() throws Exception     {
    }

@Test
public void getliststring() throws Exception     {
    }

@Test
public void getcollectionstringclassillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredcollectionstring() throws Exception     {
    }

@Test
public void getdeclaredsingularattributestringclassillegalargumentexception() throws Exception     {
    }

@Test
public void client() throws Exception     {
    }

@Test
public void createdeployment() throws Exception     {
    }

@Test
public void getdeclaredsingularattributestringclass() throws Exception     {
    }

@Test
public void embeddabletest() throws Exception     {
    }

@Test
public void getliststringillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredsingularattributestring() throws Exception     {
    }

@Test
public void getsetstring() throws Exception     {
    }

@Test
public void getdeclaredattributes() throws Exception     {
    }

@Test
public void getdeclaredmapstringillegalargumentexception() throws Exception     {
    }

@Test
public void getsetstringclass() throws Exception     {
    }

@Test
public void getattributeillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredsetstringclassillegalargumentexception() throws Exception     {
    }

@Test
public void getliststringclass() throws Exception     {
    }

@Test
public void getmapstringclassclassillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredmapstringclassclassillegalargumentexception() throws Exception     {
    }

@Test
public void setup() throws Exception     {
    }

@Test
public void getdeclaredcollectionstringillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredsetstringillegalargumentexception() throws Exception     {
    }

@Test
public void getdeclaredcollectionstringclass() throws Exception     {
    }
}
