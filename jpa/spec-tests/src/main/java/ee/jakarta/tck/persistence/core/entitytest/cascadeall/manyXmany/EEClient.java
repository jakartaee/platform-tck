
package ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_et_cascadeall_manyXmany_vehicles.ear");

        {

            JavaArchive jpa_core_et_cascadeall_manyXmany_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany_jar");
            jpa_core_et_cascadeall_manyXmany_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.B.class);
            jpa_core_et_cascadeall_manyXmany_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.A.class);
            ear.addAsLibrary(jpa_core_et_cascadeall_manyXmany_jar);

        }
        {
            JavaArchive jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar");
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.Client.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar");
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar");
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.Client.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war");
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_et_cascadeall_manyXmany_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany.jar");
                jpa_core_et_cascadeall_manyXmany_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.B.class);
                jpa_core_et_cascadeall_manyXmany_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.A.class);
                jpa_core_et_cascadeall_manyXmany_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_et_cascadeall_manyXmany_jar.addAsManifestResource("persistence.xml");
                jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_et_cascadeall_manyXmany_jar);
            }
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.Client.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war");
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_et_cascadeall_manyXmany_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany.jar");
                jpa_core_et_cascadeall_manyXmany_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.B.class);
                jpa_core_et_cascadeall_manyXmany_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.A.class);
                jpa_core_et_cascadeall_manyXmany_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_et_cascadeall_manyXmany_jar.addAsManifestResource("persistence.xml");
                jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addAsLibrary(jpa_core_et_cascadeall_manyXmany_jar);
            }
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.Client.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar");
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar");
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.Client.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar");
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar");
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.Client.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void cascadeallmxmtest9() throws Exception     {
    }

@Test
public void cascadeallmxmtest8() throws Exception     {
    }

@Test
public void finda() throws Exception     {
    }

@Test
public void cascadeallmxmtest7() throws Exception     {
    }

@Test
public void findb() throws Exception     {
    }

@Test
public void cascadeallmxmtest11() throws Exception     {
    }

@Test
public void removetestdata() throws Exception     {
    }

@Test
public void cascadeallmxmtest10() throws Exception     {
    }

@Test
public void createa() throws Exception     {
    }

@Test
public void getinstancestatus() throws Exception     {
    }

@Test
public void dumpcollectiondatab() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void cascadeallmxmtest6() throws Exception     {
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
public void cascadeallmxmtest5() throws Exception     {
    }

@Test
public void cascadeallmxmtest4() throws Exception     {
    }

@Test
public void cascadeallmxmtest3() throws Exception     {
    }

@Test
public void cascadeallmxmtest2() throws Exception     {
    }

@Test
public void cascadeallmxmtest1() throws Exception     {
    }
}
