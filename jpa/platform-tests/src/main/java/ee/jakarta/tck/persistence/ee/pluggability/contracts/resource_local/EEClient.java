
package ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "pluggability_contracts_resource_local_vehicles.ear");

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

            JavaArchive pluggability_contracts_resource_local_jar = ShrinkWrap.create(JavaArchive.class, "pluggability_contracts_resource_local_jar");
            pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order4.class);
            pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order.class);
            pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.util.LogRecordEntry.class);
            pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order3.class);
            pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order2.class);
            pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.util.LogFileProcessor.class);
            ear.addAsLibrary(pluggability_contracts_resource_local_jar);

        }
        {
            JavaArchive pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar");
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar");
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Client.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            WebArchive pluggability_contracts_resource_local_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "pluggability_contracts_resource_local_puservlet_vehicle_web_war");
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
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
                pluggability_contracts_resource_local_puservlet_vehicle_web_war.addAsLibrary(jpa_alternate_provider_jar);
            }
            {
                JavaArchive pluggability_contracts_resource_local_jar = ShrinkWrap.create(JavaArchive.class, "pluggability_contracts_resource_local.jar");
                pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order4.class);
                pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order.class);
                pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.util.LogRecordEntry.class);
                pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order3.class);
                pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order2.class);
                pluggability_contracts_resource_local_jar.addClass(ee.jakarta.tck.persistence.common.pluggability.util.LogFileProcessor.class);
                pluggability_contracts_resource_local_jar.addAsManifestResource("MANIFEST.MF");
                pluggability_contracts_resource_local_jar.addAsManifestResource("myMappingFile1.xml");
                pluggability_contracts_resource_local_jar.addAsManifestResource("myMappingFile2.xml");
                pluggability_contracts_resource_local_jar.addAsManifestResource("orm.xml");
                pluggability_contracts_resource_local_jar.addAsManifestResource("persistence.xml");
                pluggability_contracts_resource_local_puservlet_vehicle_web_war.addAsLibrary(pluggability_contracts_resource_local_jar);
            }
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Client.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            pluggability_contracts_resource_local_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(pluggability_contracts_resource_local_puservlet_vehicle_web_war);

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
public void getnonjtadatasource() throws Exception     {
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
public void isloadedwithoutreference() throws Exception     {
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
}
