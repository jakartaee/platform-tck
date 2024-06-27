
package ee.jakarta.tck.persistence.core.entitytest.apitests;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_entitytest_apitests_vehicles.ear");

        {

            JavaArchive jpa_core_entitytest_apitests_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entitytest_apitests_jar");
            jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Coffee.class);
            jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Foo.class);
            jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Bar.class);
            jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.CoffeeMappedSC.class);
            ear.addAsLibrary(jpa_core_entitytest_apitests_jar);

        }
        {
            JavaArchive jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar");
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Client.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_entitytest_apitests_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar");
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entitytest_apitests_appmanaged_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar");
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Client.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entitytest_apitests_appmanaged_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_entitytest_apitests_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_entitytest_apitests_pmservlet_vehicle_web_war");
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_entitytest_apitests_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entitytest_apitests.jar");
                jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Coffee.class);
                jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Foo.class);
                jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Bar.class);
                jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.CoffeeMappedSC.class);
                jpa_core_entitytest_apitests_jar.addAsManifestResource("myMappingFile.xml");
                jpa_core_entitytest_apitests_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_entitytest_apitests_jar.addAsManifestResource("orm.xml");
                jpa_core_entitytest_apitests_jar.addAsManifestResource("persistence.xml");
                jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_entitytest_apitests_jar);
            }
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Client.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entitytest_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entitytest_apitests_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_entitytest_apitests_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_entitytest_apitests_puservlet_vehicle_web_war");
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_entitytest_apitests_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entitytest_apitests.jar");
                jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Coffee.class);
                jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Foo.class);
                jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Bar.class);
                jpa_core_entitytest_apitests_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.CoffeeMappedSC.class);
                jpa_core_entitytest_apitests_jar.addAsManifestResource("myMappingFile.xml");
                jpa_core_entitytest_apitests_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_entitytest_apitests_jar.addAsManifestResource("orm.xml");
                jpa_core_entitytest_apitests_jar.addAsManifestResource("persistence.xml");
                jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addAsLibrary(jpa_core_entitytest_apitests_jar);
            }
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Client.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entitytest_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entitytest_apitests_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_entitytest_apitests_stateful3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entitytest_apitests_stateful3_vehicle_client_jar");
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entitytest_apitests_stateful3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar");
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Client.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entitytest_apitests_stateful3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_entitytest_apitests_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entitytest_apitests_stateless3_vehicle_client_jar");
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entitytest_apitests_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar");
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.apitests.Client.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_entitytest_apitests_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void entityapitest10() throws Exception     {
    }

@Test
public void xmloverridesnamedquerytest() throws Exception     {
    }

@Test
public void entityapitest12() throws Exception     {
    }

@Test
public void entityapitest13() throws Exception     {
    }

@Test
public void entityapitest14() throws Exception     {
    }

@Test
public void entityapitest15() throws Exception     {
    }

@Test
public void entityapitest16() throws Exception     {
    }

@Test
public void entityapitest17() throws Exception     {
    }

@Test
public void removetestdata() throws Exception     {
    }

@Test
public void verifyentity() throws Exception     {
    }

@Test
public void entityapitest18() throws Exception     {
    }

@Test
public void client() throws Exception     {
    }

@Test
public void createdeployment() throws Exception     {
    }

@Test
public void xmlnamednativequerytest() throws Exception     {
    }

@Test
public void getreferenceexceptionstest() throws Exception     {
    }

@Test
public void entityapitest8() throws Exception     {
    }

@Test
public void entityapitest1() throws Exception     {
    }

@Test
public void entityapitest2() throws Exception     {
    }

@Test
public void entityapitest3() throws Exception     {
    }

@Test
public void xmloverridesnamednativequerytest() throws Exception     {
    }

@Test
public void entityapitest4() throws Exception     {
    }

@Test
public void xmlnamedquerytest() throws Exception     {
    }

@Test
public void namedqueryinmappedsuperclass() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void namednativequeryinmappedsuperclass() throws Exception     {
    }

@Test
public void createtestdata() throws Exception     {
    }

@Test
public void setup() throws Exception     {
    }

@Test
public void getreferencetest() throws Exception     {
    }
}
