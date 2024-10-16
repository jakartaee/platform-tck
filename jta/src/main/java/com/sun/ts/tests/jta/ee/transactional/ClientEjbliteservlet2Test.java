package com.sun.ts.tests.jta.ee.transactional;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import jakarta.transaction.InvalidTransactionException;
import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionRequiredException;
import jakarta.transaction.TransactionalException;
import jakarta.transaction.UserTransaction;

import java.lang.System.Logger;


@ExtendWith(ArquillianExtension.class)
@Tag("jta")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
public class ClientEjbliteservlet2Test extends com.sun.ts.tests.jta.ee.transactional.Client {
    static final String VEHICLE_ARCHIVE = "transactional_ejbliteservlet2_vehicle";

    private static final Logger logger = System.getLogger(ClientEjbliteservlet2Test.class.getName());

    @BeforeEach
    void logStartTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
    }
  
    @AfterEach
    void logFinishTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
    }

    @AfterEach
    public void cleanup() {
      logger.log(Logger.Level.INFO, "cleanup ok");
    }
  

    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static WebArchive createDeploymentVehicle() {
        // War
        // the war with the correct archive name
        WebArchive transactional_ejbliteservlet2_vehicle_web = ShrinkWrap.create(WebArchive.class, "transactional_ejbliteservlet2_vehicle_web.war");
        transactional_ejbliteservlet2_vehicle_web.addClasses(
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
        com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
        com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
        com.sun.ts.tests.ejb30.common.helper.Helper.class,
        com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
        com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
        com.sun.ts.tests.jta.ee.transactional.OneManagedQualifier.class,
        com.sun.ts.tests.jta.ee.transactional.TwoManagedQualifier.class,
        com.sun.ts.tests.jta.ee.transactional.TwoManagedBean.class,
        com.sun.ts.tests.jta.ee.transactional.CTSRollbackException.class,
        com.sun.ts.tests.jta.ee.transactional.TransactionScopedBean.class,
        com.sun.ts.tests.jta.ee.transactional.Helper.class,
        com.sun.ts.tests.jta.ee.transactional.CTSDontRollbackException.class,
        com.sun.ts.tests.jta.ee.transactional.OneManagedBean.class,
        com.sun.ts.tests.jta.ee.transactional.EJBLiteServlet2Filter.class,
        com.sun.ts.tests.jta.ee.transactional.HttpServletDelegate.class,
        com.sun.ts.tests.jta.ee.transactional.Client.class,
        ClientEjbliteservlet2Test.class
        );
        // The web.xml descriptor
        URL warResURL = ClientEjbliteservlet2Test.class.getResource("/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle_web.xml");
        if(warResURL != null) {
          transactional_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }

        warResURL = ClientEjbliteservlet2Test.class.getResource("/vehicle/ejbliteservlet2/beans.xml");
        if(warResURL != null) {
          transactional_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "beans.xml");
        }

        warResURL = ClientEjbliteservlet2Test.class.getResource("/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle.jsp");
        if(warResURL != null) {
          transactional_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/ejbliteservlet2_vehicle.jsp");
        }

        return transactional_ejbliteservlet2_vehicle_web;
      }

  
    /*
      * @testName: txTypeRequired_withoutTransaction
      * 
      * @test_Strategy: TxType.REQUIRED: If called outside a transaction context,
      * the interceptor must begin a new JTA transaction, the managed bean method
      * execution must then continue inside this transaction context, and the
      * transaction must be completed by the interceptor.
      * 
      * If called inside a transaction context, the managed bean method execution
      * must then continue inside this transaction context.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeRequired_withoutTransaction() throws Exception {  
      super.txTypeRequired_withoutTransaction();
    }
  
    /*
      * @testName: txTypeRequired_withTransaction
      * 
      * @test_Strategy: TxType.REQUIRED: If called outside a transaction context,
      * the interceptor must begin a new JTA transaction, the managed bean method
      * execution must then continue inside this transaction context, and the
      * transaction must be completed by the interceptor.
      * 
      * If called inside a transaction context, the managed bean method execution
      * must then continue inside this transaction context.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeRequired_withTransaction() throws Exception {
      super.txTypeRequired_withTransaction();
    }
  
    /*
      * @testName: txTypeRequired_IllegalStateException
      * 
      * @test_Strategy: If an attempt is made to call any method of the
      * UserTransaction interface from within a bean or method annotated with
      * 
      * @Transactional and a Transactional.TxType other than NOT_SUPPORTED or
      * NEVER, an IllegalStateException must be thrown.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeRequired_IllegalStateException() throws Exception {
      super.txTypeRequired_IllegalStateException();
    }
  
    /*
      * @testName: txTypeRequiresNew
      * 
      * @test_Strategy: If called outside a transaction context, the interceptor
      * must begin a new JTA transaction, the managed bean method execution must
      * then continue inside this transaction context, and the transaction must be
      * completed by the interceptor.
      * 
      * If called inside a transaction context, the current transaction context
      * must be suspended, a new JTA transaction will begin, the managed bean
      * method execution must then continue inside this transaction context, the
      * transaction must be completed, and the previously suspended transaction
      * must be resumed.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeRequiresNew() throws Exception {
      super.txTypeRequiresNew();
  
    }
  
    /*
      * @testName: txTypeRequiresNew_withTransaction
      * 
      * @test_Strategy: If called outside a transaction context, the interceptor
      * must begin a new JTA transaction, the managed bean method execution must
      * then continue inside this transaction context, and the transaction must be
      * completed by the interceptor.
      * 
      * If called inside a transaction context, the current transaction context
      * must be suspended, a new JTA transaction will begin, the managed bean
      * method execution must then continue inside this transaction context, the
      * transaction must be completed, and the previously suspended transaction
      * must be resumed.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeRequiresNew_withTransaction() throws Exception {
      super.txTypeRequiresNew_withTransaction();
    }
  
    /*
      * @testName: txTypeMandatory_withoutTransaction
      * 
      * @test_Strategy: If called outside a transaction context, a
      * TransactionalException with a nested TransactionRequiredException must be
      * thrown.
      * 
      * If called inside a transaction context, managed bean method execution will
      * then continue under that context.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeMandatory_withoutTransaction() throws Exception {
      super.txTypeMandatory_withoutTransaction();
    }
  
    /*
      * @testName: txTypeMandatory_withTransaction
      * 
      * @test_Strategy: If called outside a transaction context, a
      * TransactionalException with a nested TransactionRequiredException must be
      * thrown.
      * 
      * If called inside a transaction context, managed bean method execution will
      * then continue under that context.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeMandatory_withTransaction() throws Exception {
      super.txTypeMandatory_withTransaction();
    }
  
    /*
      * @testName: txTypeSupports_withoutTransaction
      * 
      * @test_Strategy: If called outside a transaction context, managed bean
      * method execution must then continue outside a transaction context.
      * 
      * If called inside a transaction context, the managed bean method execution
      * must then continue inside this transaction context.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeSupports_withoutTransaction() throws Exception {
      super.txTypeSupports_withoutTransaction();
    }
  
    /*
      * @testName: txTypeSupports_withTransaction
      * 
      * @test_Strategy: If called outside a transaction context, managed bean
      * method execution must then continue outside a transaction context.
      * 
      * If called inside a transaction context, the managed bean method execution
      * must then continue inside this transaction context.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeSupports_withTransaction() throws Exception {
      super.txTypeSupports_withTransaction();
    }
  
    /*
      * @testName: txTypeNotSupported_withoutTransaction
      * 
      * @test_Strategy: If called outside a transaction context, managed bean
      * method execution must then continue outside a transaction context.
      * 
      * If called inside a transaction context, the current transaction context
      * must be suspended, the managed bean method execution must then continue
      * outside a transaction context, and the previously suspended transaction
      * must be resumed by the interceptor that suspended it after the method
      * execution has completed.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeNotSupported_withoutTransaction() throws Exception {
      super.txTypeNotSupported_withoutTransaction();
  
    }
  
    /*
      * @testName: txTypeNotSupported_withTransaction
      * 
      * @test_Strategy: If called outside a transaction context, managed bean
      * method execution must then continue outside a transaction context.
      * 
      * If called inside a transaction context, the current transaction context
      * must be suspended, the managed bean method execution must then continue
      * outside a transaction context, and the previously suspended transaction
      * must be resumed by the interceptor that suspended it after the method
      * execution has completed.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeNotSupported_withTransaction() throws Exception {
      super.txTypeNotSupported_withTransaction();
    }
  
    /*
      * @testName: txTypeNever_withoutTransaction
      * 
      * @test_Strategy: If called outside a transaction context, managed bean
      * method execution must then continue outside a transaction context.
      * 
      * If called inside a transaction context, a TransactionalException with a
      * nested InvalidTransactionException must be thrown
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeNever_withoutTransaction() throws Exception {
      super.txTypeNever_withoutTransaction();
    }
  
    /*
      * @testName: txTypeNever_withTransaction
      * 
      * @test_Strategy: If called outside a transaction context, managed bean
      * method execution must then continue outside a transaction context.
      * 
      * If called inside a transaction context, a TransactionalException with a
      * nested InvalidTransactionException must be thrown
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void txTypeNever_withTransaction() throws Exception {
      super.txTypeNever_withTransaction();
    }
  
    /*
      * @testName: rollbackOnException
      * 
      * @test_Strategy: The rollbackOn element can be set to indicate exceptions
      * that must cause the interceptor to mark the transaction for rollback.
      * 
      * Conversely, the dontRollbackOn element can be set to indicate exceptions
      * that must not cause the interceptor to mark the transaction for rollback.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void rollbackOnException() throws Exception {
      super.rollbackOnException();
  
    }
  
    /*
      * @testName: rollbackOnExceptionTwo
      * 
      * @test_Strategy: The rollbackOn element can be set to indicate exceptions
      * that must cause the interceptor to mark the transaction for rollback.
      * 
      * Conversely, the dontRollbackOn element can be set to indicate exceptions
      * that must not cause the interceptor to mark the transaction for rollback.
      * 
      * When a class is specified for either of these elements, the designated
      * behavior applies to subclasses of that class as well.
      * 
      * Note: This test verifies the behavior in SubClass
      * 
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void rollbackOnExceptionTwo() throws Exception {
      super.rollbackOnExceptionTwo();
  
    }
  
    /*
      * @testName: dontRollbackOnException
      * 
      * @test_Strategy: The rollbackOn element can be set to indicate exceptions
      * that must cause the interceptor to mark the transaction for rollback.
      * 
      * Conversely, the dontRollbackOn element can be set to indicate exceptions
      * that must not cause the interceptor to mark the transaction for rollback.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void dontRollbackOnException() throws Exception {
      super.dontRollbackOnException();
    }
  
    /*
      * @testName: dontRollbackOnExceptionTwo
      * 
      * @test_Strategy: The rollbackOn element can be set to indicate exceptions
      * that must cause the interceptor to mark the transaction for rollback.
      * 
      * Conversely, the dontRollbackOn element can be set to indicate exceptions
      * that must not cause the interceptor to mark the transaction for rollback.
      * 
      * When a class is specified for either of these elements, the designated
      * behavior applies to subclasses of that class as well.
      * 
      * Note: This test verifies the behavior in SubClass
      * 
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void dontRollbackOnExceptionTwo() throws Exception {
      super.dontRollbackOnExceptionTwo();
  
    }
  
    /*
      * @testName: rollbackAndDontRollback
      * 
      * @test_Strategy: The rollbackOn element can be set to indicate exceptions
      * that must cause the interceptor to mark the transaction for rollback.
      * 
      * Conversely, the dontRollbackOn element can be set to indicate exceptions
      * that must not cause the interceptor to mark the transaction for rollback.
      * 
      * When a class is specified for either of these elements, the designated
      * behavior applies to subclasses of that class as well. If both elements are
      * specified, dontRollbackOn takes precedence.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void rollbackAndDontRollback() throws Exception {
      super.rollbackAndDontRollback();
  
    }
  
    /*
      * @testName: rollbackAndDontRollbackTwo
      * 
      * @test_Strategy: The rollbackOn element can be set to indicate exceptions
      * that must cause the interceptor to mark the transaction for rollback.
      * 
      * Conversely, the dontRollbackOn element can be set to indicate exceptions
      * that must not cause the interceptor to mark the transaction for rollback.
      * 
      * When a class is specified for either of these elements, the designated
      * behavior applies to subclasses of that class as well. If both elements are
      * specified, dontRollbackOn takes precedence.
      * 
      * Note: This test verifies the behavior in SubClass
      * 
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void rollbackAndDontRollbackTwo() throws Exception {
      super.rollbackAndDontRollbackTwo();
  
    }
  
    /*
      * @testName: transactionScopedBean_withoutTransaction
      * 
      * @test_Strategy:
      * 
      * The jakarta.transaction.TransactionScoped annotation provides the ability to
      * specify a standard CDI scope to define bean instances whose lifecycle is
      * scoped to the currently active JTA transaction.
      * 
      * The transaction scope is active when the return from a call to
      * UserTransaction.getStatus or TransactionManager.getStatus is one of the
      * following states: Status.STATUS_ACTIVE Status.STATUS_MARKED_ROLLBACK
      * Status.STATUS_PREPARED Status.STATUS_UNKNOWN Status.STATUS_PREPARING
      * Status.STATUS_COMMITTING Status.STATUS_ROLLING_BACK
      *
      * A jakarta.enterprise.context.ContextNotActiveException must be thrown if a
      * bean with this annotation is used when the transaction context is not
      * active.
      * 
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void transactionScopedBean_withoutTransaction() throws Exception {
      super.transactionScopedBean_withoutTransaction();
    }
  
    /*
      * @testName: transactionScopedBean_withTransaction
      * 
      * @test_Strategy:
      * 
      * The jakarta.transaction.TransactionScoped annotation provides the ability to
      * specify a standard CDI scope to define bean instances whose lifecycle is
      * scoped to the currently active JTA transaction.
      * 
      * The transaction scope is active when the return from a call to
      * UserTransaction.getStatus or TransactionManager.getStatus is one of the
      * following states: Status.STATUS_ACTIVE Status.STATUS_MARKED_ROLLBACK
      * Status.STATUS_PREPARED Status.STATUS_UNKNOWN Status.STATUS_PREPARING
      * Status.STATUS_COMMITTING Status.STATUS_ROLLING_BACK
      * 
      * A jakarta.enterprise.context.ContextNotActiveException must be thrown if a
      * bean with this annotation is used when the transaction context is not
      * active.
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void transactionScopedBean_withTransaction() throws Exception {
      super.transactionScopedBean_withTransaction();
    }
  
    /*
      * @testName: getInterceptorPriorityForTxTypeRequired
      * 
      * @test_Strategy: The Transactional interceptors must have a priority of
      * Interceptor.Priority.PLATFORM_BEFORE+200
      * 
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void getInterceptorPriorityForTxTypeRequired() throws Exception {
      super.getInterceptorPriorityForTxTypeRequired();
    }
  
    /*
      * @testName: getInterceptorPriorityForTxTypeRequiresNew
      * 
      * @test_Strategy: The Transactional interceptors must have a priority of
      * Interceptor.Priority.PLATFORM_BEFORE+200
      * 
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void getInterceptorPriorityForTxTypeRequiresNew() throws Exception {
      super.getInterceptorPriorityForTxTypeRequiresNew();
    }
  
    /*
      * @testName: getInterceptorPriorityForTxTypeMandatory
      * 
      * @test_Strategy: The Transactional interceptors must have a priority of
      * Interceptor.Priority.PLATFORM_BEFORE+200
      * 
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void getInterceptorPriorityForTxTypeMandatory() throws Exception {
      super.getInterceptorPriorityForTxTypeMandatory();
    }
  
    /*
      * @testName: getInterceptorPriorityForTxTypeSupports
      * 
      * @test_Strategy: The Transactional interceptors must have a priority of
      * Interceptor.Priority.PLATFORM_BEFORE+200
      * 
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void getInterceptorPriorityForTxTypeSupports() throws Exception {
      super.getInterceptorPriorityForTxTypeSupports();
    }
  
    /*
      * @testName: getInterceptorPriorityForTxTypeNotSupported
      * 
      * @test_Strategy: The Transactional interceptors must have a priority of
      * Interceptor.Priority.PLATFORM_BEFORE+200
      * 
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void getInterceptorPriorityForTxTypeNotSupported() throws Exception {
      super.getInterceptorPriorityForTxTypeNotSupported();
    }
  
    /*
      * @testName: getInterceptorPriorityForTxTypeNever
      * 
      * @test_Strategy: The Transactional interceptors must have a priority of
      * Interceptor.Priority.PLATFORM_BEFORE+200
      * 
      */
    @Test
    @Override
    @TargetVehicle("ejbliteservlet2")
    public void getInterceptorPriorityForTxTypeNever() throws Exception {
      super.getInterceptorPriorityForTxTypeNever();
  
    }
  
    private void verifyInterceptorPriority(List<Integer> priorityList,
        String txType) throws Exception {
      String result = null;
      if (priorityList.contains(Interceptor.Priority.PLATFORM_BEFORE + 200)) {
        Helper.getLogger().log(Level.INFO, "Transactional Interceptor for "
            + txType + " has right interceptor priority");
        result = "Transactional Interceptor for " + txType
            + " has right interceptor priority";
      } else {
        throw new Exception("Transactional Interceptor for " + txType
            + " has incorrect interceptor priority : "
            + Arrays.toString(priorityList.toArray()) + " Excpected value is :"
            + Interceptor.Priority.PLATFORM_BEFORE + 200);
      }
  
      if (result != null)
        appendReason(result);
  
    }
      
}