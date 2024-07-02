
package ee.jakarta.tck.persistence.core.query.apitests;
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
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_query_apitests_vehicles.ear");

        {

            JavaArchive jpa_core_query_apitests_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_apitests_jar");
            jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.Employee.class);
            jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.Department.class);
            jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.DataTypes2.class);
            jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.Insurance.class);
            ear.addAsLibrary(jpa_core_query_apitests_jar);

        }
        {
            JavaArchive jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar");
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_query_apitests_appmanagedNoTx_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar");
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.Client1.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class);
            ear.addAsModule(jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb_jar);

        }
        {
            WebArchive jpa_core_query_apitests_pmservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_query_apitests_pmservlet_vehicle_web_war");
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_query_apitests_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_apitests.jar");
                jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.Employee.class);
                jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.Department.class);
                jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.DataTypes2.class);
                jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.Insurance.class);
                jpa_core_query_apitests_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_query_apitests_jar.addAsManifestResource("persistence.xml");
                jpa_core_query_apitests_pmservlet_vehicle_web_war.addAsLibrary(jpa_core_query_apitests_jar);
            }
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.query.apitests.Client1.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_apitests_pmservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_query_apitests_pmservlet_vehicle_web_war);

        }
        {
            WebArchive jpa_core_query_apitests_puservlet_vehicle_web_war = ShrinkWrap.create(WebArchive.class, "jpa_core_query_apitests_puservlet_vehicle_web_war");
            jpa_core_query_apitests_puservlet_vehicle_web_war.addAsWebInfResource("web.xml");
            {
                JavaArchive jpa_core_query_apitests_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_apitests.jar");
                jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.Employee.class);
                jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.Department.class);
                jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.DataTypes2.class);
                jpa_core_query_apitests_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.Insurance.class);
                jpa_core_query_apitests_jar.addAsManifestResource("MANIFEST.MF");
                jpa_core_query_apitests_jar.addAsManifestResource("persistence.xml");
                jpa_core_query_apitests_puservlet_vehicle_web_war.addAsLibrary(jpa_core_query_apitests_jar);
            }
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(ee.jakarta.tck.persistence.core.query.apitests.Client1.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_apitests_puservlet_vehicle_web_war.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_query_apitests_puservlet_vehicle_web_war);

        }
        {
            JavaArchive jpa_core_query_apitests_stateless3_vehicle_client_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_apitests_stateless3_vehicle_client_jar");
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_apitests_stateless3_vehicle_client_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_query_apitests_stateless3_vehicle_client_jar);

        }
        {
            JavaArchive jpa_core_query_apitests_stateless3_vehicle_ejb_jar = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_apitests_stateless3_vehicle_ejb_jar");
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.common.PMClientBase.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(ee.jakarta.tck.persistence.core.query.apitests.Client1.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
            jpa_core_query_apitests_stateless3_vehicle_ejb_jar.addClass(com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class);
            ear.addAsModule(jpa_core_query_apitests_stateless3_vehicle_ejb_jar);

        }
        return ear;
    }

@Test
public void getparametervaluestringillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getresultlistillegalstateexception() throws Exception     {
    }

@Test
public void setparameter7test() throws Exception     {
    }

@Test
public void queryapitest12() throws Exception     {
    }

@Test
public void queryapitest11() throws Exception     {
    }

@Test
public void removetestdata() throws Exception     {
    }

@Test
public void queryapitest17() throws Exception     {
    }

@Test
public void queryapitest16() throws Exception     {
    }

@Test
public void getparametervalueparameterillegalstateexceptiontest() throws Exception     {
    }

@Test
public void client1() throws Exception     {
    }

@Test
public void setparameter8test() throws Exception     {
    }

@Test
public void getsingleresulttest() throws Exception     {
    }

@Test
public void setparameterintobjecttest() throws Exception     {
    }

@Test
public void getparametertest() throws Exception     {
    }

@Test
public void getparameterillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void setgetmaxresultstest() throws Exception     {
    }

@Test
public void queryapitest21() throws Exception     {
    }

@Test
public void getsingleresultillegalstateexception() throws Exception     {
    }

@Test
public void setparameterintcalendartemporaltypeillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void queryapitest25() throws Exception     {
    }

@Test
public void getparametervaluestringillegalstateexceptiontest() throws Exception     {
    }

@Test
public void queryapitest24() throws Exception     {
    }

@Test
public void queryapitest23() throws Exception     {
    }

@Test
public void queryapitest22() throws Exception     {
    }

@Test
public void setparameterparameterdatetemporaltypetest() throws Exception     {
    }

@Test
public void queryapitest27() throws Exception     {
    }

@Test
public void setparameterstringdatetemporaltypetest() throws Exception     {
    }

@Test
public void setfirstresultillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getparametervalueinttest() throws Exception     {
    }

@Test
public void setparameterintdatetemporaltypetest() throws Exception     {
    }

@Test
public void setparameterparametercalendartemporaltypetest() throws Exception     {
    }

@Test
public void getsingleresulttransactionrequiredexception() throws Exception     {
    }

@Test
public void getparameterillegalargumentexception2test() throws Exception     {
    }

@Test
public void cleanup() throws Exception     {
    }

@Test
public void getparameterintclasstest() throws Exception     {
    }

@Test
public void setparameterstringobject2illegalargumentexceptiontest() throws Exception     {
    }

@Test
public void setparameterstringcalendartemporaltypetest() throws Exception     {
    }

@Test
public void setparameterstringdatetemporaltypeillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void setparameter2test() throws Exception     {
    }

@Test
public void getresultlisttransactionrequiredexceptiontest() throws Exception     {
    }

@Test
public void setparameterparameterobjectillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void setmaxresults() throws Exception     {
    }

@Test
public void setparameterstringcalendartemporaltypetestillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void getparameterintillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void setparameter1test() throws Exception     {
    }

@Test
public void setparameterintdatetemporaltypeillegalargumentexception1test() throws Exception     {
    }

@Test
public void setparameterintcalendartemporaltypetest() throws Exception     {
    }

@Test
public void setmaxresultsillegalargumentexceptiontest() throws Exception     {
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
public void getparametervalueintillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void isboundtest() throws Exception     {
    }

@Test
public void setfirstresultillegalargumentexception() throws Exception     {
    }

@Test
public void getsingleresultnonuniqueresultexceptiontest() throws Exception     {
    }

@Test
public void setparameterstringobject1illegalargumentexceptiontest() throws Exception     {
    }

@Test
public void executeupdateillegalstateexception() throws Exception     {
    }

@Test
public void executeupdatetransactionrequiredexceptiontest() throws Exception     {
    }

@Test
public void setfirstresulttest() throws Exception     {
    }

@Test
public void setparameterparameterdatetemporaltypeillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void createtestdata() throws Exception     {
    }

@Test
public void getparametervalueintillegalstateexceptiontest() throws Exception     {
    }

@Test
public void getsingleresultnoresultexceptiontest() throws Exception     {
    }

@Test
public void setup() throws Exception     {
    }

@Test
public void getparametervaluestringtest() throws Exception     {
    }

@Test
public void setfirstresult() throws Exception     {
    }

@Test
public void setparameterintobjectillegalargumentexceptiontest() throws Exception     {
    }

@Test
public void notransactionlockmodetypenonetest() throws Exception     {
    }

@Test
public void setparameterparametercalendartemporaltypeillegalargumentexceptiontest() throws Exception     {
    }
}
