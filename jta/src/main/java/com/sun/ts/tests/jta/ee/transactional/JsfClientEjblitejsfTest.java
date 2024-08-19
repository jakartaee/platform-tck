package com.sun.ts.tests.jta.ee.transactional;

import com.sun.ts.tests.jta.ee.transactional.JsfClient;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("jta")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")

public class JsfClientEjblitejsfTest extends com.sun.ts.tests.jta.ee.transactional.JsfClient {
    static final String VEHICLE_ARCHIVE = "transactional_ejblitejsf_vehicle";

        /**
        EE10 Deployment Descriptors:
        transactional_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive transactional_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "transactional_ejblitejsf_vehicle_web.war");
            // The class files
            transactional_ejblitejsf_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jta.ee.transactional.OneManagedQualifier.class,
            com.sun.ts.tests.jta.ee.transactional.TwoManagedQualifier.class,
            com.sun.ts.tests.jta.ee.transactional.Client.class,
            com.sun.ts.tests.jta.ee.transactional.TwoManagedBean.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.jta.ee.transactional.CTSRollbackException.class,
            com.sun.ts.tests.jta.ee.transactional.TransactionScopedBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.jta.ee.transactional.Helper.class,
            com.sun.ts.tests.jta.ee.transactional.CTSDontRollbackException.class,
            com.sun.ts.tests.jta.ee.transactional.OneManagedBean.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.jta.ee.transactional.JsfClient.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class
            );
            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              transactional_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              transactional_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
            transactional_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");

           archiveProcessor.processWebArchive(transactional_ejblitejsf_vehicle_web, JsfClient.class, warResURL);

        return transactional_ejblitejsf_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeRequired_withoutTransaction() throws java.lang.Exception {
            super.txTypeRequired_withoutTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeRequired_withTransaction() throws java.lang.Exception {
            super.txTypeRequired_withTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeRequired_IllegalStateException() throws java.lang.Exception {
            super.txTypeRequired_IllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeRequiresNew() throws java.lang.Exception {
            super.txTypeRequiresNew();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeRequiresNew_withTransaction() throws java.lang.Exception {
            super.txTypeRequiresNew_withTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeMandatory_withoutTransaction() throws java.lang.Exception {
            super.txTypeMandatory_withoutTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeMandatory_withTransaction() throws java.lang.Exception {
            super.txTypeMandatory_withTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeSupports_withoutTransaction() throws java.lang.Exception {
            super.txTypeSupports_withoutTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeSupports_withTransaction() throws java.lang.Exception {
            super.txTypeSupports_withTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeNotSupported_withoutTransaction() throws java.lang.Exception {
            super.txTypeNotSupported_withoutTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeNotSupported_withTransaction() throws java.lang.Exception {
            super.txTypeNotSupported_withTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeNever_withoutTransaction() throws java.lang.Exception {
            super.txTypeNever_withoutTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void txTypeNever_withTransaction() throws java.lang.Exception {
            super.txTypeNever_withTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void rollbackOnException() throws java.lang.Exception {
            super.rollbackOnException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void rollbackOnExceptionTwo() throws java.lang.Exception {
            super.rollbackOnExceptionTwo();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void dontRollbackOnException() throws java.lang.Exception {
            super.dontRollbackOnException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void dontRollbackOnExceptionTwo() throws java.lang.Exception {
            super.dontRollbackOnExceptionTwo();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void rollbackAndDontRollback() throws java.lang.Exception {
            super.rollbackAndDontRollback();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void rollbackAndDontRollbackTwo() throws java.lang.Exception {
            super.rollbackAndDontRollbackTwo();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void transactionScopedBean_withoutTransaction() throws java.lang.Exception {
            super.transactionScopedBean_withoutTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void transactionScopedBean_withTransaction() throws java.lang.Exception {
            super.transactionScopedBean_withTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void getInterceptorPriorityForTxTypeRequired() throws java.lang.Exception {
            super.getInterceptorPriorityForTxTypeRequired();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void getInterceptorPriorityForTxTypeRequiresNew() throws java.lang.Exception {
            super.getInterceptorPriorityForTxTypeRequiresNew();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void getInterceptorPriorityForTxTypeMandatory() throws java.lang.Exception {
            super.getInterceptorPriorityForTxTypeMandatory();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void getInterceptorPriorityForTxTypeSupports() throws java.lang.Exception {
            super.getInterceptorPriorityForTxTypeSupports();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void getInterceptorPriorityForTxTypeNotSupported() throws java.lang.Exception {
            super.getInterceptorPriorityForTxTypeNotSupported();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void getInterceptorPriorityForTxTypeNever() throws java.lang.Exception {
            super.getInterceptorPriorityForTxTypeNever();
        }


}