
package ee.jakarta.tck.persistence.core.StoredProcedureQuery;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_StoredProcedureQuery_vehicles.ear");

        {

            JavaArchive jpa_core_StoredProcedureQuery_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery_jar");
            jpa_core_StoredProcedureQuery_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Employee2.class);
            jpa_core_StoredProcedureQuery_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Employee.class);
            jpa_core_StoredProcedureQuery_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.EmployeeMappedSC.class);
            ear.addAsLibrary(jpa_core_StoredProcedureQuery_jar);

        }
        {
            JavaArchive jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar");
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client1.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar");
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_StoredProcedureQuery_appmanaged_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar");
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client1.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war");
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_StoredProcedureQuery_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery.jar");
                jpa_core_StoredProcedureQuery_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Employee2.class);
                jpa_core_StoredProcedureQuery_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Employee.class);
                jpa_core_StoredProcedureQuery_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.EmployeeMappedSC.class);
                jpa_core_StoredProcedureQuery_jar.addAsManifestResource("myMappingFile.xml");
                jpa_core_StoredProcedureQuery_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_StoredProcedureQuery_jar.addAsManifestResource("persistence.xml");
                jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_StoredProcedureQuery_jar);
            }
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client1.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_StoredProcedureQuery_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war");
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_StoredProcedureQuery_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery.jar");
                jpa_core_StoredProcedureQuery_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Employee2.class);
                jpa_core_StoredProcedureQuery_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Employee.class);
                jpa_core_StoredProcedureQuery_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.EmployeeMappedSC.class);
                jpa_core_StoredProcedureQuery_jar.addAsManifestResource("myMappingFile.xml");
                jpa_core_StoredProcedureQuery_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_StoredProcedureQuery_jar.addAsManifestResource("persistence.xml");
                jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addAsLibrary(jpa_core_StoredProcedureQuery_jar);
            }
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client1.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_StoredProcedureQuery_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar");
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_StoredProcedureQuery_stateful3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar");
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client1.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar");
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_StoredProcedureQuery_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar");
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client1.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void executeupdateofadeletetest() throws Exception     {
    }

@Test
public void setparameterparameterobjectillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getparametervalueparameterillegalstateexceptiontest() throws Exception     {
    }

@Test
public void client1() throws Exception     {
    }

@Test
public void getoutputparametervalueinttest() throws Exception     {
    }

@Test
public void getsingleresulttest() throws Exception     {
    }

@Test
public void createemployeetestdata() throws Exception     {
    }

@Test
public void getparameterintillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getlockmodeillegalstateexceptiontest() throws Exception     {
    }

@Test
public void getsingleresultornullwithvaluetest() throws Exception     {
    }

@Test
public void createdeployment() throws Exception     {
    }

@Test
public void getparametervalueparameterillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getparametervalueparametertest() throws Exception     {
    }

@Test
public void sethintstringobjecttest() throws Exception     {
    }

@Test
public void executeupdateofanupdatetest() throws Exception     {
    }

@Test
public void getparametervalueintillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void setgetflushmodetest() throws Exception     {
    }

@Test
public void getsingleresultornullwithnulltest() throws Exception     {
    }

@Test
public void xmloverridesnamedstoredprocedurequerytest() throws Exception     {
    }

@Test
public void getsingleresultnonuniqueresultexceptiontest() throws Exception     {
    }

@Test
public void getparameterstest() throws Exception     {
    }

@Test
public void setparameterparameterdatetemporaltypetest() throws Exception     {
    }

@Test
public void executetest() throws Exception     {
    }

@Test
public void xmloverridessqlresultsetmappingannotationtest() throws Exception     {
    }

@Test
public void getparameterstringexceptiontest() throws Exception     {
    }

@Test
public void executeupdatetransactionrequiredexceptiontest() throws Exception     {
    }

@Test
public void getoutputparametervalueintillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void setparameterparameterobjecttest() throws Exception     {
    }

@Test
public void setparameterintdatetemporaltypetest() throws Exception     {
    }

@Test
public void getparametervalueinttest() throws Exception     {
    }

@Test
public void setgetparameterinttest() throws Exception     {
    }

@Test
public void setparameterparameterdatetemporaltypeillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getsingleresultnoresultexceptiontest() throws Exception     {
    }

@Test
public void getparametervalueintillegalstateexceptiontest() throws Exception     {
    }

@Test
public void setup() throws Exception     {
    }

@Test
public void getfirstresulttest() throws Exception     {
    }

@Test
public void getmaxresultstest() throws Exception     {
    }

@Test
public void setlockmodeillegalstateexceptiontest() throws Exception     {
    }

@Test
public void setparameterintobjectillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void setparameterintdatetemporaltypeillegalargumentexceptiontest() throws Exception     {
    }
}
