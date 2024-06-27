
package ee.jakarta.tck.persistence.ee.pluggability.contracts.jta;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_pluggability_contracts_jta_vehicles.ear");

        {

            JavaArchive jpa_alternate_provider_jar = ShrinkWrap.create(JavaArchive.class, "jpa_alternate_provider_jar");
            jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.ClassTransformerImpl.class);
            jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.TSLogger.class);
            jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.TSXMLFormatter.class);
            jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.CacheImpl.class);
            jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.PersistenceUnitInfoImpl.class);
            jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.EntityManagerFactoryImpl.class);
            jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.QueryImpl.class);
            jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.EntityManagerImpl.class);
            jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.EntityTransactionImpl.class);
            jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.TSLogRecord.class);
            jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.PersistenceProvider.class);
            ear.addAsLibrary(jpa_alternate_provider_jar);

        }
        {

            JavaArchive jpa_ee_pluggability_contracts_jta_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_pluggability_contracts_jta_jar");
            jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order3.class);
            jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order2.class);
            jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.util.LogRecordEntry.class);
            jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order.class);
            jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order4.class);
            jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.util.LogFileProcessor.class);
            ear.addAsLibrary(jpa_ee_pluggability_contracts_jta_jar);

        }
        {
            JavaArchive jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar");
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client_jar);

        }
        {
            JavaArchive jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar");
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Client.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war");
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_alternate_provider_jar = ShrinkWrap.create(JavaArchive.class, "jpa_alternate_provider.jar");
                jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.ClassTransformerImpl.class);
                jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.TSLogger.class);
                jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.TSXMLFormatter.class);
                jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.CacheImpl.class);
                jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.PersistenceUnitInfoImpl.class);
                jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.EntityManagerFactoryImpl.class);
                jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.QueryImpl.class);
                jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.EntityManagerImpl.class);
                jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.EntityTransactionImpl.class);
                jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.TSLogRecord.class);
                jpa_alternate_provider_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.PersistenceProvider.class);
                jpa_alternate_provider_jar.addAsManifestResource("MANIFEST.MF");
                jpa_alternate_provider_jar.addAsManifestResource("services/jakarta.persistence.spi.PersistenceProvider");
                jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addAsLibrary(jpa_alternate_provider_jar);
            }
            {
                JavaArchive jpa_ee_pluggability_contracts_jta_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_pluggability_contracts_jta.jar");
                jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order3.class);
                jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order2.class);
                jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.util.LogRecordEntry.class);
                jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order.class);
                jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order4.class);
                jpa_ee_pluggability_contracts_jta_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.util.LogFileProcessor.class);
                jpa_ee_pluggability_contracts_jta_jar.addAsManifestResource("MANIFEST.MF");
                jpa_ee_pluggability_contracts_jta_jar.addAsManifestResource("persistence.xml");
                jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addAsLibrary(jpa_ee_pluggability_contracts_jta_jar);
            }
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Client.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar");
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar");
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Client.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar");
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar");
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Client.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void getnewtempclassloader() throws Exception     {
    }

@Test
public void getvalidationmode() throws Exception     {
    }

@Test
public void getpersistenceproviderclassname() throws Exception     {
    }

@Test
public void getmanagedclassnames() throws Exception     {
    }

@Test
public void excludeunlistedclasses() throws Exception     {
    }

@Test
public void getclassloader() throws Exception     {
    }

@Test
public void getjarfileurls() throws Exception     {
    }

@Test
public void getmappingfilenames() throws Exception     {
    }

@Test
public void gettransactiontype() throws Exception     {
    }

@Test
public void createemf() throws Exception     {
    }

@Test
public void getpersistenceunitnametest() throws Exception     {
    }

@Test
public void getpersistenceunitrooturl() throws Exception     {
    }

@Test
public void getclassobjects() throws Exception     {
    }

@Test
public void getproviderutil() throws Exception     {
    }

@Test
public void isloaded() throws Exception     {
    }

@Test
public void getproperties() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void getpersistencexmlschemaversion() throws Exception     {
    }

@Test
public void client() throws Exception     {
    }

@Test
public void setup() throws Exception     {
    }

@Test
public void getsharedcachemode() throws Exception     {
    }

@Test
public void getjtadatasource() throws Exception     {
    }
}
