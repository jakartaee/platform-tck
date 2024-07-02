
package ee.jakarta.tck.persistence.core.callback.listeneroverride;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_callback_listeneroverride_vehicles.ear");

        {

            JavaArchive jpa_core_callback_listeneroverride_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listeneroverride_jar");
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerC.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.Constants.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.GenerictListener.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerCC.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.GenerictListenerImpl.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.CallbackStatusImpl.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerBB.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerB.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerBase.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Product.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.CallbackStatusIF.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Order.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerA.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerAA.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.LineItem.class);
            jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.LineItemSuper.class);
            ear.addAsLibrary(jpa_core_callback_listeneroverride_jar);

        }
        {
            JavaArchive jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar");
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.Constants.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.EntityCallbackClientBase.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Client.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar");
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_callback_listeneroverride_appmanaged_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar");
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.Constants.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.EntityCallbackClientBase.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Client.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war");
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_callback_listeneroverride_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listeneroverride.jar");
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerC.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.Constants.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.GenerictListener.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerCC.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.GenerictListenerImpl.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.CallbackStatusImpl.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerBB.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerB.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerBase.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Product.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.CallbackStatusIF.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Order.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerA.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerAA.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.LineItem.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.LineItemSuper.class);
                jpa_core_callback_listeneroverride_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_callback_listeneroverride_jar.addAsManifestResource("orm.xml");
                jpa_core_callback_listeneroverride_jar.addAsManifestResource("persistence.xml");
                jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_callback_listeneroverride_jar);
            }
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.callback.common.Constants.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.callback.common.EntityCallbackClientBase.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Client.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_callback_listeneroverride_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_callback_listeneroverride_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_callback_listeneroverride_puservlet_vehicle_web_war");
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_callback_listeneroverride_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listeneroverride.jar");
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerC.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.Constants.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.GenerictListener.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerCC.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.GenerictListenerImpl.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.CallbackStatusImpl.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerBB.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerB.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerBase.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Product.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.CallbackStatusIF.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Order.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerA.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.ListenerAA.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.LineItem.class);
                jpa_core_callback_listeneroverride_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.LineItemSuper.class);
                jpa_core_callback_listeneroverride_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_callback_listeneroverride_jar.addAsManifestResource("orm.xml");
                jpa_core_callback_listeneroverride_jar.addAsManifestResource("persistence.xml");
                jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addAsLibrary(jpa_core_callback_listeneroverride_jar);
            }
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.callback.common.Constants.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.callback.common.EntityCallbackClientBase.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Client.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_callback_listeneroverride_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_callback_listeneroverride_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar");
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_callback_listeneroverride_stateful3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar");
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.Constants.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.EntityCallbackClientBase.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Client.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_callback_listeneroverride_stateful3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar");
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_callback_listeneroverride_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar");
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.Constants.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.common.EntityCallbackClientBase.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.callback.listeneroverride.Client.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_callback_listeneroverride_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void preremovecascadetest() throws Exception     {
    }

@Test
public void postloadmultitest() throws Exception     {
    }

@Test
public void prepersisttest() throws Exception     {
    }

@Test
public void newproduct() throws Exception     {
    }

@Test
public void preremovemultitest() throws Exception     {
    }

@Test
public void removetestdata() throws Exception     {
    }

@Test
public void postloadtest() throws Exception     {
    }

@Test
public void preremovetest() throws Exception     {
    }

@Test
public void preupdatetest() throws Exception     {
    }

@Test
public void prepersistmulticascadetest() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void neworder() throws Exception     {
    }

@Test
public void newlineitem() throws Exception     {
    }

@Test
public void prepersistcascadetest() throws Exception     {
    }

@Test
public void preremovemulticascadetest() throws Exception     {
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
public void prepersistmultitest() throws Exception     {
    }
}
