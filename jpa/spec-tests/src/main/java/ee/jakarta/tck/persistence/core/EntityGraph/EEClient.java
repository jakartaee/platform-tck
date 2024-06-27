
package ee.jakarta.tck.persistence.core.EntityGraph;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_EntityGraph_vehicles.ear");

        {

            JavaArchive jpa_core_EntityGraph_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph_jar");
            jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Employee2.class);
            jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Department.class);
            jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Employee.class);
            jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Employee3.class);
            ear.addAsLibrary(jpa_core_EntityGraph_jar);

        }
        {
            JavaArchive jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar");
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_EntityGraph_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Client.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_EntityGraph_appmanaged_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph_appmanaged_vehicle_client_jar");
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class);
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_EntityGraph_appmanaged_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_EntityGraph_appmanaged_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar");
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Client.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_EntityGraph_appmanaged_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_EntityGraph_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_EntityGraph_pmservlet_vehicle_web_war");
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_EntityGraph_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph.jar");
                jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Employee2.class);
                jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Department.class);
                jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Employee.class);
                jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Employee3.class);
                jpa_core_EntityGraph_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_EntityGraph_jar.addAsManifestResource("persistence.xml");
                jpa_core_EntityGraph_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_EntityGraph_jar);
            }
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Client.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_EntityGraph_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_EntityGraph_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_EntityGraph_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_EntityGraph_puservlet_vehicle_web_war");
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_EntityGraph_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph.jar");
                jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Employee2.class);
                jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Department.class);
                jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Employee.class);
                jpa_core_EntityGraph_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Employee3.class);
                jpa_core_EntityGraph_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_EntityGraph_jar.addAsManifestResource("persistence.xml");
                jpa_core_EntityGraph_puservlet_vehicle_web_war.addAsLibrary(jpa_core_EntityGraph_jar);
            }
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Client.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_EntityGraph_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_EntityGraph_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_EntityGraph_stateful3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph_stateful3_vehicle_client_jar");
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class);
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_EntityGraph_stateful3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_EntityGraph_stateful3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_EntityGraph_stateful3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph_stateful3_vehicle_ejb_jar");
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Client.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_EntityGraph_stateful3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_EntityGraph_stateful3_vehicle_ejb_jar);

        }
        {
            JavaArchive jpa_core_EntityGraph_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph_stateless3_vehicle_client_jar");
            jpa_core_EntityGraph_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_EntityGraph_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_EntityGraph_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_EntityGraph_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_EntityGraph_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_EntityGraph_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_EntityGraph_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_EntityGraph_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_EntityGraph_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_EntityGraph_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_EntityGraph_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            ear.addAsModule(jpa_core_EntityGraph_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_EntityGraph_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph_stateless3_vehicle_ejb_jar");
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.EntityGraph.Client.class);
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_EntityGraph_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            ear.addAsModule(jpa_core_EntityGraph_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void entitygraphgetnametest() throws Exception     {
    }

@Test
public void getentitygraphsclasstest() throws Exception     {
    }

@Test
public void createentitygraphstringtest() throws Exception     {
    }

@Test
public void createemployeedata() throws Exception     {
    }

@Test
public void addattributenodesattributearraytest() throws Exception     {
    }

@Test
public void getentitygraphstringtest() throws Exception     {
    }

@Test
public void entitygraphgetnamenonameexiststest() throws Exception     {
    }

@Test
public void removetestdata() throws Exception     {
    }

@Test
public void getentitygraphstringillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void setupemployeedata() throws Exception     {
    }

@Test
public void cleanupemployeedata() throws Exception     {
    }

@Test
public void getnametest() throws Exception     {
    }

@Test
public void addattributenodesstringarraytest() throws Exception     {
    }

@Test
public void addnamedentitygraphstringentitygraphtest() throws Exception     {
    }

@Test
public void addattributenodesstringarrayillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getentitygraphsclassillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void client() throws Exception     {
    }

@Test
public void createdeployment() throws Exception     {
    }

@Test
public void annotationstest() throws Exception     {
    }
}
