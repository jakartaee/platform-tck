
package ee.jakarta.tck.persistence.core.exceptions;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_exceptions_vehicles.ear");

        {

            JavaArchive jpa_core_exceptions_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_exceptions_jar");
            jpa_core_exceptions_jar.addClass(ee.jakarta.tck.persistence.core.exceptions.Coffee.class);
            ear.addAsLibrary(jpa_core_exceptions_jar);

        }
        {
            WebArchive jpa_core_exceptions_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_exceptions_pmservlet_vehicle_web_war");
            jpa_core_exceptions_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_exceptions_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_exceptions.jar");
                jpa_core_exceptions_jar.addClass(ee.jakarta.tck.persistence.core.exceptions.Coffee.class);
                jpa_core_exceptions_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_exceptions_jar.addAsManifestResource("persistence.xml");
                jpa_core_exceptions_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_exceptions_jar);
            }
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.exceptions.Client.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_exceptions_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_exceptions_pmservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_exceptions_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_exceptions_stateless3_vehicle_client_jar");
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_exceptions_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_exceptions_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_exceptions_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_exceptions_stateless3_vehicle_ejb_jar");
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.exceptions.Client.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_exceptions_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_exceptions_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void exceptiontest4() throws Exception     {
    }

@Test
public void exceptiontest3() throws Exception     {
    }

@Test
public void querytimeoutexceptiontest() throws Exception     {
    }

@Test
public void nonuniqueresultexceptiontest() throws Exception     {
    }

@Test
public void exceptiontest2() throws Exception     {
    }

@Test
public void exceptiontest6() throws Exception     {
    }

@Test
public void exceptiontest5() throws Exception     {
    }

@Test
public void removetestdata() throws Exception     {
    }

@Test
public void pessimisticlockexceptiontest() throws Exception     {
    }

@Test
public void locktimeoutexceptiontest() throws Exception     {
    }

@Test
public void entityexistsexceptiontest() throws Exception     {
    }

@Test
public void persistenceexceptiontest() throws Exception     {
    }

@Test
public void entitynotfoundexceptiontest() throws Exception     {
    }

@Test
public void noresultexceptiontest() throws Exception     {
    }

@Test
public void transactionrequiredexception2test() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void transactionrequiredexceptiontest() throws Exception     {
    }

@Test
public void optimisticlockexceptiontest() throws Exception     {
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
public void rollbackexceptiontest() throws Exception     {
    }
}
