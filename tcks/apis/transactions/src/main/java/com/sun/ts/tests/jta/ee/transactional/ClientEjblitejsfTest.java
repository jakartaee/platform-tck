package com.sun.ts.tests.jta.ee.transactional;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.inject.Inject;
import jakarta.transaction.InvalidTransactionException;
import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionRequiredException;
import jakarta.transaction.TransactionalException;
import jakarta.transaction.UserTransaction;

import java.io.Serializable;
import java.lang.System.Logger;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import tck.arquillian.protocol.common.TargetVehicle;

import static jakarta.interceptor.Interceptor.Priority.PLATFORM_BEFORE;
import static java.lang.System.Logger.Level.INFO;

@ExtendWith(ArquillianExtension.class)
@Tag("jta")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
@jakarta.inject.Named("client")
@jakarta.enterprise.context.RequestScoped
public class ClientEjblitejsfTest extends EJBLiteJsfClientBase implements Serializable {

    static final String VEHICLE_ARCHIVE = "transactional_ejblitejsf_vehicle";

    private static final Logger logger = System.getLogger(ClientEjblitejsfTest.class.getName());

    @BeforeEach
    void logStartTest(TestInfo testInfo) {
        logger.log(INFO, "STARTING TEST : " + testInfo.getDisplayName());
    }

    @AfterEach
    void logFinishTest(TestInfo testInfo) {
        logger.log(INFO, "FINISHED TEST : " + testInfo.getDisplayName());
    }

    @Override
    @AfterEach
    public void cleanup() {
        logger.log(INFO, "cleanup ok");
    }

    private static final long serialVersionUID = 1L;
    private static StringBuilder callRecords = new StringBuilder();

    @Inject
    @OneManagedQualifier
    OneManagedBean one;

    @Inject
    TransactionScopedBean tscopedBean;

    @Resource(lookup = "java:comp/UserTransaction")
    private UserTransaction ut;

    public static void main(String[] args) {
        ClientEjblitejsfTest theTests = new ClientEjblitejsfTest();
        com.sun.ts.lib.harness.Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    @Inject
    @TwoManagedQualifier
    TwoManagedBean two;

    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static WebArchive createDeploymentVehicle() {
        // public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        WebArchive transactional_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "transactional_ejblitejsf_vehicle_web.war");
        // The class files
        transactional_ejblitejsf_vehicle_web.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class, com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
                com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class, com.sun.ts.tests.ejb30.common.helper.Helper.class,
                com.sun.ts.tests.ejb30.common.lite.NumberEnum.class, com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
                com.sun.ts.tests.ejb30.common.lite.NumberIF.class, Fault.class,
                EETest.class, SetupException.class,
                ServiceEETest.class, com.sun.ts.tests.jta.ee.transactional.OneManagedQualifier.class,
                com.sun.ts.tests.jta.ee.transactional.TwoManagedQualifier.class, com.sun.ts.tests.jta.ee.transactional.TwoManagedBean.class,
                com.sun.ts.tests.jta.ee.transactional.CTSRollbackException.class,
                com.sun.ts.tests.jta.ee.transactional.TransactionScopedBean.class, com.sun.ts.tests.jta.ee.transactional.Helper.class,
                com.sun.ts.tests.jta.ee.transactional.CTSDontRollbackException.class,
                com.sun.ts.tests.jta.ee.transactional.OneManagedBean.class, HttpServletDelegate.class, ClientEjblitejsfTest.class);
        // The web.xml descriptor
        URL warResURL = ClientEjblitejsfTest.class.getResource("/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
        if (warResURL != null) {
            transactional_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }
        warResURL = ClientEjblitejsfTest.class.getResource("/vehicle/ejblitejsf/faces-config.xml");
        if (warResURL != null) {
            transactional_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "faces-config.xml");
        }

        warResURL = ClientEjblitejsfTest.class.getResource("/vehicle/ejblitejsf/beans.xml");
        if (warResURL != null) {
            transactional_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "beans.xml");
        }

        // Web content
        warResURL = ClientEjblitejsfTest.class.getResource("/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
        transactional_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");

        return transactional_ejblitejsf_vehicle_web;
    }

    /*
     * @testName: txTypeRequired_withoutTransaction
     *
     * @test_Strategy: TxType.REQUIRED: If called outside a transaction context, the interceptor must begin a new JTA
     * transaction, the managed bean method execution must then continue inside this transaction context, and the
     * transaction must be completed by the interceptor.
     *
     * If called inside a transaction context, the managed bean method execution must then continue inside this transaction
     * context.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeRequired_withoutTransaction() throws Exception {

        Helper.assertEquals("\n", "txTypeRequired called successfully", one.txTypeRequired(), callRecords);
        // Helper.getLogger().info(callRecords.toString());
        appendReason(Helper.compareResult("txTypeRequired called successfully", one.txTypeRequired()));

    }

    /*
     * @testName: txTypeRequired_withTransaction
     *
     * @test_Strategy: TxType.REQUIRED: If called outside a transaction context, the interceptor must begin a new JTA
     * transaction, the managed bean method execution must then continue inside this transaction context, and the
     * transaction must be completed by the interceptor.
     *
     * If called inside a transaction context, the managed bean method execution must then continue inside this transaction
     * context.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeRequired_withTransaction() throws Exception {
        try {
            ut.begin();
            Helper.assertEquals(null, "txTypeRequired called successfully", one.txTypeRequired(), callRecords);
            // Helper.getLogger().info(callRecords.toString());
            appendReason(Helper.compareResult("txTypeRequired called successfully", one.txTypeRequired()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(Level.INFO, null, e);
            throw new Exception("txTypeRequired_withTransaction failed");
        }
    }

    /*
     * @testName: txTypeRequired_IllegalStateException
     *
     * @test_Strategy: If an attempt is made to call any method of the UserTransaction interface from within a bean or
     * method annotated with
     *
     * @Transactional and a Transactional.TxType other than NOT_SUPPORTED or NEVER, an IllegalStateException must be thrown.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeRequired_IllegalStateException() throws Exception {

        Helper.assertEquals(null, "IllegalStateException", one.txTypeRequiredIllegalStateException(), callRecords);
        // Helper.getLogger().info(callRecords.toString());
        appendReason(Helper.compareResult("IllegalStateException", one.txTypeRequiredIllegalStateException()));

    }

    /*
     * @testName: txTypeRequiresNew
     *
     * @test_Strategy: If called outside a transaction context, the interceptor must begin a new JTA transaction, the
     * managed bean method execution must then continue inside this transaction context, and the transaction must be
     * completed by the interceptor.
     *
     * If called inside a transaction context, the current transaction context must be suspended, a new JTA transaction will
     * begin, the managed bean method execution must then continue inside this transaction context, the transaction must be
     * completed, and the previously suspended transaction must be resumed.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeRequiresNew() throws Exception {

        Helper.assertEquals(null, "txTypeRequiresNew called successfully", one.txTypeRequiresNew(), callRecords);
        // Helper.getLogger().info(callRecords.toString());
        appendReason(Helper.compareResult("txTypeRequiresNew called successfully", one.txTypeRequiresNew()));

    }

    /*
     * @testName: txTypeRequiresNew_withTransaction
     *
     * @test_Strategy: If called outside a transaction context, the interceptor must begin a new JTA transaction, the
     * managed bean method execution must then continue inside this transaction context, and the transaction must be
     * completed by the interceptor.
     *
     * If called inside a transaction context, the current transaction context must be suspended, a new JTA transaction will
     * begin, the managed bean method execution must then continue inside this transaction context, the transaction must be
     * completed, and the previously suspended transaction must be resumed.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeRequiresNew_withTransaction() throws Exception {
        try {
            ut.begin();
            Helper.assertEquals(null, "txTypeRequiresNew called successfully", one.txTypeRequiresNew(), callRecords);
            // Helper.getLogger().info(callRecords.toString());
            appendReason(Helper.compareResult("txTypeRequiresNew called successfully", one.txTypeRequiresNew()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(Level.INFO, null, e);
            throw new Exception("txTypeRequiresNew_withTransaction failed");
        }
    }

    /*
     * @testName: txTypeMandatory_withoutTransaction
     *
     * @test_Strategy: If called outside a transaction context, a TransactionalException with a nested
     * TransactionRequiredException must be thrown.
     *
     * If called inside a transaction context, managed bean method execution will then continue under that context.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeMandatory_withoutTransaction() throws Exception {
        String result = "TransactionalException not received";

        try {
            Helper.getLogger().info("Invoking OneManagedBean.txTypeManadatory() without a transaction Context");
            one.txTypeMandatory();
        } catch (TransactionalException te) {
            if (te.getCause() instanceof TransactionRequiredException) {
                result = "Received expected TransactionalException with nested TransactionRequiredException";
            } else {
                throw new Exception("Received TransactionalException without nested TransactionRequiredExecption");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "Received unexcepted Exception :" + e.getMessage();
        }

        if (result.equals("Received expected TransactionalException with nested TransactionRequiredException")) {
            Helper.getLogger().log(Level.INFO, result);
            appendReason("Received expected TransactionalException with nested TransactionRequiredException");
        } else {
            throw new Exception(result);
        }

    }

    /*
     * @testName: txTypeMandatory_withTransaction
     *
     * @test_Strategy: If called outside a transaction context, a TransactionalException with a nested
     * TransactionRequiredException must be thrown.
     *
     * If called inside a transaction context, managed bean method execution will then continue under that context.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeMandatory_withTransaction() throws Exception {
        try {
            ut.begin();
            Helper.assertEquals(null, "txTypeMandatory called successfully", one.txTypeMandatory(), callRecords);
            // Helper.getLogger().info(callRecords.toString());
            appendReason(Helper.compareResult("txTypeMandatory called successfully", one.txTypeMandatory()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(Level.INFO, null, e);
            throw new Exception("txTypeRequiresNew_withTransaction failed");
        }
    }

    /*
     * @testName: txTypeSupports_withoutTransaction
     *
     * @test_Strategy: If called outside a transaction context, managed bean method execution must then continue outside a
     * transaction context.
     *
     * If called inside a transaction context, the managed bean method execution must then continue inside this transaction
     * context.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeSupports_withoutTransaction() throws Exception {

        Helper.assertEquals(null, "txTypeSupports run without active transaction", one.txTypeSupportsWithoutTransaction(), callRecords);
        // Helper.getLogger().info(callRecords.toString());
        appendReason(Helper.compareResult("txTypeSupports run without active transaction", one.txTypeSupportsWithoutTransaction()));

    }

    /*
     * @testName: txTypeSupports_withTransaction
     *
     * @test_Strategy: If called outside a transaction context, managed bean method execution must then continue outside a
     * transaction context.
     *
     * If called inside a transaction context, the managed bean method execution must then continue inside this transaction
     * context.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeSupports_withTransaction() throws Exception {
        try {
            ut.begin();
            Helper.assertEquals(null, "txTypeSupports called successfully", one.txTypeSupports(), callRecords);
            // Helper.getLogger().info(callRecords.toString());
            appendReason(Helper.compareResult("txTypeSupports called successfully", one.txTypeSupports()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(Level.INFO, null, e);
            throw new Exception("txTypeSupports failed");
        }
    }

    /*
     * @testName: txTypeNotSupported_withoutTransaction
     *
     * @test_Strategy: If called outside a transaction context, managed bean method execution must then continue outside a
     * transaction context.
     *
     * If called inside a transaction context, the current transaction context must be suspended, the managed bean method
     * execution must then continue outside a transaction context, and the previously suspended transaction must be resumed
     * by the interceptor that suspended it after the method execution has completed.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeNotSupported_withoutTransaction() throws Exception {

        Helper.assertEquals(null, "txTypeNotSupported run without active transaction", one.txTypeNotSupported(), callRecords);
        // Helper.getLogger().info(callRecords.toString());
        appendReason(Helper.compareResult("txTypeNotSupported run without active transaction", one.txTypeNotSupported()));

    }

    /*
     * @testName: txTypeNotSupported_withTransaction
     *
     * @test_Strategy: If called outside a transaction context, managed bean method execution must then continue outside a
     * transaction context.
     *
     * If called inside a transaction context, the current transaction context must be suspended, the managed bean method
     * execution must then continue outside a transaction context, and the previously suspended transaction must be resumed
     * by the interceptor that suspended it after the method execution has completed.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeNotSupported_withTransaction() throws Exception {
        try {
            ut.begin();
            Helper.assertEquals(null, "txTypeNotSupported run without active transaction", one.txTypeNotSupported(), callRecords);
            // Helper.getLogger().info(callRecords.toString());
            appendReason(Helper.compareResult("txTypeNotSupported run without active transaction", one.txTypeNotSupported()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(Level.INFO, null, e);
            throw new Exception("txTypeSupports failed");
        }
    }

    /*
     * @testName: txTypeNever_withoutTransaction
     *
     * @test_Strategy: If called outside a transaction context, managed bean method execution must then continue outside a
     * transaction context.
     *
     * If called inside a transaction context, a TransactionalException with a nested InvalidTransactionException must be
     * thrown
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeNever_withoutTransaction() throws Exception {
        Helper.assertEquals(null, "txTypeNever run without active transaction", one.txTypeNever(), callRecords);
        // Helper.getLogger().info(callRecords.toString());
        appendReason(Helper.compareResult("txTypeNever run without active transaction", one.txTypeNever()));

    }

    /*
     * @testName: txTypeNever_withTransaction
     *
     * @test_Strategy: If called outside a transaction context, managed bean method execution must then continue outside a
     * transaction context.
     *
     * If called inside a transaction context, a TransactionalException with a nested InvalidTransactionException must be
     * thrown
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void txTypeNever_withTransaction() throws Exception {
        String result = "Expected TransactionalException not received";

        try {
            Helper.getLogger().info("Invoking OneManagedBean.txTypeNever() with a transaction Context");
            ut.begin();
            one.txTypeNever();
            ut.commit();
        } catch (TransactionalException te) {

            if (te.getCause() instanceof InvalidTransactionException) {
                result = "Received expected TransactionalException with nested InvalidTransactionException";
            } else {
                throw new Exception("Received expected TransactionalException without nested InvalidTransactionException");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "Received unexcepted Exception :" + e.getMessage();
        }

        if (result.equals("Received expected TransactionalException with nested InvalidTransactionException")) {
            Helper.getLogger().log(Level.INFO, result);
            appendReason("Received expected TransactionalException with nested InvalidTransactionException");
        } else {
            throw new Exception(result);
        }
    }

    /*
     * @testName: rollbackOnException
     *
     * @test_Strategy: The rollbackOn element can be set to indicate exceptions that must cause the interceptor to mark the
     * transaction for rollback.
     *
     * Conversely, the dontRollbackOn element can be set to indicate exceptions that must not cause the interceptor to mark
     * the transaction for rollback.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void rollbackOnException() throws Exception {
        String result = "failed to set STATUS_MARKED_ROLLBACK on CTSRollbackException";
        int status;

        try {
            ut.begin();
            if (ut.getStatus() == Status.STATUS_ACTIVE) {
                Helper.getLogger().info("Current Transaction Status is = " + ut.getStatus());
                Helper.getLogger().info("Transaction Status value for Status.STATUS_ACTIVE = " + Status.STATUS_ACTIVE);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_NO_TRANSACTION = " + Status.STATUS_NO_TRANSACTION);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_MARKED_ROLLBACK = " + Status.STATUS_MARKED_ROLLBACK);
                Helper.getLogger().info("Calling one.rollbackOnException()");
                one.rollbackOnException();
            }

        } catch (CTSRollbackException ce) {
            Helper.getLogger().info("Received Expected CTSRollbackException");
            try {

                if (ut.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                    result = "Transaction STATUS_MARKED_ROLLBACK on CTSRollbackException";
                } else {
                    result = "Transaction Status is set to : " + ut.getStatus();

                }
            } catch (SystemException se) {
                result = "failed to get transaction status";
            }

        } catch (Exception e) {
            result = "Received unexpected exception :" + e.getClass();
        }

        if (result.equals("Transaction STATUS_MARKED_ROLLBACK on CTSRollbackException")) {
            Helper.getLogger().log(Level.INFO, result);
            appendReason(result);
        } else {
            appendReason(result);
            throw new Exception(result);
        }

    }

    /*
     * @testName: rollbackOnExceptionTwo
     *
     * @test_Strategy: The rollbackOn element can be set to indicate exceptions that must cause the interceptor to mark the
     * transaction for rollback.
     *
     * Conversely, the dontRollbackOn element can be set to indicate exceptions that must not cause the interceptor to mark
     * the transaction for rollback.
     *
     * When a class is specified for either of these elements, the designated behavior applies to subclasses of that class
     * as well.
     *
     * Note: This test verifies the behavior in SubClass
     *
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void rollbackOnExceptionTwo() throws Exception {
        String result = "failed to set STATUS_MARKED_ROLLBACK on CTSRollbackException";
        int status;

        try {
            ut.begin();
            if (ut.getStatus() == Status.STATUS_ACTIVE) {
                Helper.getLogger().info("Current Transaction Status is = " + ut.getStatus());
                Helper.getLogger().info("Transaction Status value for Status.STATUS_ACTIVE = " + Status.STATUS_ACTIVE);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_NO_TRANSACTION = " + Status.STATUS_NO_TRANSACTION);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_MARKED_ROLLBACK = " + Status.STATUS_MARKED_ROLLBACK);
                Helper.getLogger().info("Calling one.rollbackOnException()");
                two.rollbackOnException();
            }

        } catch (CTSRollbackException ce) {
            Helper.getLogger().info("Received Expected CTSRollbackException");
            try {

                if (ut.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                    result = "Transaction STATUS_MARKED_ROLLBACK on CTSRollbackException";
                } else {
                    result = "Transaction Status is set to : " + ut.getStatus();

                }
            } catch (SystemException se) {
                result = "failed to get transaction status";
            }

        } catch (Exception e) {
            result = "Received unexpected exception :" + e.getClass();
        }

        if (result.equals("Transaction STATUS_MARKED_ROLLBACK on CTSRollbackException")) {
            Helper.getLogger().log(Level.INFO, result);
            appendReason(result);
        } else {
            appendReason(result);
            throw new Exception(result);
        }

    }

    /*
     * @testName: dontRollbackOnException
     *
     * @test_Strategy: The rollbackOn element can be set to indicate exceptions that must cause the interceptor to mark the
     * transaction for rollback.
     *
     * Conversely, the dontRollbackOn element can be set to indicate exceptions that must not cause the interceptor to mark
     * the transaction for rollback.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void dontRollbackOnException() throws Exception {
        String result = "";
        int status;

        try {
            ut.begin();
            if (ut.getStatus() == Status.STATUS_ACTIVE) {
                Helper.getLogger().info("Current Transaction Status is = " + ut.getStatus());
                Helper.getLogger().info("Transaction Status value for Status.STATUS_ACTIVE = " + Status.STATUS_ACTIVE);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_NO_TRANSACTION = " + Status.STATUS_NO_TRANSACTION);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_MARKED_ROLLBACK = " + Status.STATUS_MARKED_ROLLBACK);
                Helper.getLogger().info("Calling one.dontRollbackOnException()");
                one.dontRollbackOnException();
            }

        } catch (CTSDontRollbackException ce) {
            Helper.getLogger().info("Received Expected CTSDontRollbackException");
            try {

                if (ut.getStatus() == Status.STATUS_ACTIVE) {
                    result = "Transaction Status not changed on CTSDontRollbackException";
                } else {
                    result = "Transaction Status is set to : " + ut.getStatus();

                }
            } catch (SystemException se) {
                result = "failed to get transaction status";
            }

        } catch (Exception e) {
            result = "Received unexpected exception :" + e.getClass();
            e.printStackTrace();
        }

        if (result.equals("Transaction Status not changed on CTSDontRollbackException")) {
            Helper.getLogger().log(Level.INFO, result);
            appendReason(result);
        } else {
            appendReason(result);
            throw new Exception(result);
        }

    }

    /*
     * @testName: dontRollbackOnExceptionTwo
     *
     * @test_Strategy: The rollbackOn element can be set to indicate exceptions that must cause the interceptor to mark the
     * transaction for rollback.
     *
     * Conversely, the dontRollbackOn element can be set to indicate exceptions that must not cause the interceptor to mark
     * the transaction for rollback.
     *
     * When a class is specified for either of these elements, the designated behavior applies to subclasses of that class
     * as well.
     *
     * Note: This test verifies the behavior in SubClass
     *
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void dontRollbackOnExceptionTwo() throws Exception {
        String result = "";
        int status;

        try {
            ut.begin();
            if (ut.getStatus() == Status.STATUS_ACTIVE) {
                Helper.getLogger().info("Current Transaction Status is = " + ut.getStatus());
                Helper.getLogger().info("Transaction Status value for Status.STATUS_ACTIVE = " + Status.STATUS_ACTIVE);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_NO_TRANSACTION = " + Status.STATUS_NO_TRANSACTION);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_MARKED_ROLLBACK = " + Status.STATUS_MARKED_ROLLBACK);
                Helper.getLogger().info("Calling two.dontRollbackOnException()");
                two.dontRollbackOnException();
            }

        } catch (CTSDontRollbackException ce) {
            Helper.getLogger().info("Received Expected CTSDontRollbackException");
            try {

                if (ut.getStatus() == Status.STATUS_ACTIVE) {
                    result = "Transaction Status not changed on CTSDontRollbackException";
                } else {
                    result = "Transaction Status is set to : " + ut.getStatus();

                }
            } catch (SystemException se) {
                result = "failed to get transaction status";
            }

        } catch (Exception e) {
            result = "Received unexpected exception :" + e.getClass();
            e.printStackTrace();
        }

        if (result.equals("Transaction Status not changed on CTSDontRollbackException")) {
            Helper.getLogger().log(Level.INFO, result);
            appendReason(result);
        } else {
            appendReason(result);
            throw new Exception(result);
        }

    }

    /*
     * @testName: rollbackAndDontRollback
     *
     * @test_Strategy: The rollbackOn element can be set to indicate exceptions that must cause the interceptor to mark the
     * transaction for rollback.
     *
     * Conversely, the dontRollbackOn element can be set to indicate exceptions that must not cause the interceptor to mark
     * the transaction for rollback.
     *
     * When a class is specified for either of these elements, the designated behavior applies to subclasses of that class
     * as well. If both elements are specified, dontRollbackOn takes precedence.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void rollbackAndDontRollback() throws Exception {
        String result = "";
        int status;

        try {
            ut.begin();
            if (ut.getStatus() == Status.STATUS_ACTIVE) {
                Helper.getLogger().info("Current Transaction Status is = " + ut.getStatus());
                Helper.getLogger().info("Transaction Status value for Status.STATUS_ACTIVE = " + Status.STATUS_ACTIVE);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_NO_TRANSACTION = " + Status.STATUS_NO_TRANSACTION);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_MARKED_ROLLBACK = " + Status.STATUS_MARKED_ROLLBACK);
                Helper.getLogger().info("Calling one.rollbackAndDontRollback()");
                one.rollbackAndDontRollback();
            }

        } catch (CTSRollbackException ce) {
            Helper.getLogger().info("Received Expected CTSRollbackException");
            try {

                if (ut.getStatus() == Status.STATUS_ACTIVE) {
                    result = "Transaction Status not changed on CTSRollbackException";
                } else {
                    result = "Transaction Status is set to : " + ut.getStatus();

                }
            } catch (SystemException se) {
                result = "failed to get transaction status";
            }

        } catch (Exception e) {
            result = "Received unexpected exception :" + e.getClass();
        }

        if (result.equals("Transaction Status not changed on CTSRollbackException")) {
            Helper.getLogger().log(Level.INFO, result);
            appendReason(result);
        } else {
            appendReason(result);
            throw new Exception(result);
        }

    }

    /*
     * @testName: rollbackAndDontRollbackTwo
     *
     * @test_Strategy: The rollbackOn element can be set to indicate exceptions that must cause the interceptor to mark the
     * transaction for rollback.
     *
     * Conversely, the dontRollbackOn element can be set to indicate exceptions that must not cause the interceptor to mark
     * the transaction for rollback.
     *
     * When a class is specified for either of these elements, the designated behavior applies to subclasses of that class
     * as well. If both elements are specified, dontRollbackOn takes precedence.
     *
     * Note: This test verifies the behavior in SubClass
     *
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void rollbackAndDontRollbackTwo() throws Exception {
        String result = "";
        int status;

        try {
            ut.begin();
            if (ut.getStatus() == Status.STATUS_ACTIVE) {
                Helper.getLogger().info("Current Transaction Status is = " + ut.getStatus());
                Helper.getLogger().info("Transaction Status value for Status.STATUS_ACTIVE = " + Status.STATUS_ACTIVE);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_NO_TRANSACTION = " + Status.STATUS_NO_TRANSACTION);
                Helper.getLogger().info("Transaction Status value for Status.STATUS_MARKED_ROLLBACK = " + Status.STATUS_MARKED_ROLLBACK);
                Helper.getLogger().info("Calling two.rollbackAndDontRollback()");
                two.rollbackAndDontRollback();
            }

        } catch (CTSRollbackException ce) {
            Helper.getLogger().info("Received Expected CTSRollbackException");
            try {

                if (ut.getStatus() == Status.STATUS_ACTIVE) {
                    result = "Transaction Status not changed on CTSRollbackException";
                } else {
                    result = "Transaction Status is set to : " + ut.getStatus();

                }
            } catch (SystemException se) {
                result = "failed to get transaction status";
            }

        } catch (Exception e) {
            result = "Received unexpected exception :" + e.getClass();
        }

        if (result.equals("Transaction Status not changed on CTSRollbackException")) {
            Helper.getLogger().log(Level.INFO, result);
            appendReason(result);
        } else {
            appendReason(result);
            throw new Exception(result);
        }

    }

    /*
     * @testName: transactionScopedBean_withoutTransaction
     *
     * @test_Strategy:
     *
     * The jakarta.transaction.TransactionScoped annotation provides the ability to specify a standard CDI scope to define
     * bean instances whose lifecycle is scoped to the currently active JTA transaction.
     *
     * The transaction scope is active when the return from a call to UserTransaction.getStatus or
     * TransactionManager.getStatus is one of the following states: Status.STATUS_ACTIVE Status.STATUS_MARKED_ROLLBACK
     * Status.STATUS_PREPARED Status.STATUS_UNKNOWN Status.STATUS_PREPARING Status.STATUS_COMMITTING
     * Status.STATUS_ROLLING_BACK
     *
     * A jakarta.enterprise.context.ContextNotActiveException must be thrown if a bean with this annotation is used when the
     * transaction context is not active.
     *
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void transactionScopedBean_withoutTransaction() throws Exception {

        String result = "ContextNotActiveException not received";

        try {
            Helper.getLogger().info("Invoking TransactionScopedBean.test() without a transaction Context");
            tscopedBean.test();
        } catch (ContextNotActiveException te) {
            result = "Received expected ContextNotActiveException";

        } catch (Exception e) {
            result = "Received unexcepted Exception :" + e.getClass();
            e.printStackTrace();
        }

        if (result.equals("Received expected ContextNotActiveException")) {
            Helper.getLogger().log(Level.INFO, result);
            appendReason(result);
        } else {
            throw new Exception(result);
        }
    }

    /*
     * @testName: transactionScopedBean_withTransaction
     *
     * @test_Strategy:
     *
     * The jakarta.transaction.TransactionScoped annotation provides the ability to specify a standard CDI scope to define
     * bean instances whose lifecycle is scoped to the currently active JTA transaction.
     *
     * The transaction scope is active when the return from a call to UserTransaction.getStatus or
     * TransactionManager.getStatus is one of the following states: Status.STATUS_ACTIVE Status.STATUS_MARKED_ROLLBACK
     * Status.STATUS_PREPARED Status.STATUS_UNKNOWN Status.STATUS_PREPARING Status.STATUS_COMMITTING
     * Status.STATUS_ROLLING_BACK
     *
     * A jakarta.enterprise.context.ContextNotActiveException must be thrown if a bean with this annotation is used when the
     * transaction context is not active.
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void transactionScopedBean_withTransaction() throws Exception {

        String result = "";

        try {
            ut.begin();
            Helper.getLogger().info("Invoking TransactionScopedBean.test() with a transaction Context");
            result = tscopedBean.test();
            ut.commit();
        } catch (Exception e) {
            result = "Received unexcepted Exception :" + e.getClass();
            e.printStackTrace();
        }

        if (result.equals("TransactionScopedBean.test called with active transaction")) {
            Helper.getLogger().log(Level.INFO, result);
            appendReason(result);
        } else {
            throw new Exception(result);
        }
    }

    /*
     * @testName: getInterceptorPriorityForTxTypeRequired
     *
     * @test_Strategy: The Transactional interceptors must have a priority of Interceptor.Priority.PLATFORM_BEFORE+200
     *
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void getInterceptorPriorityForTxTypeRequired() throws Exception {
        String methodName = "txTypeRequired";
        List<Integer> priorityList = one.getPriority(methodName);
        verifyInterceptorPriority(priorityList, "TxType.REQUIRED");
    }

    /*
     * @testName: getInterceptorPriorityForTxTypeRequiresNew
     *
     * @test_Strategy: The Transactional interceptors must have a priority of Interceptor.Priority.PLATFORM_BEFORE+200
     *
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void getInterceptorPriorityForTxTypeRequiresNew() throws Exception {
        String methodName = "txTypeRequiresNew";
        List<Integer> priorityList = one.getPriority(methodName);
        verifyInterceptorPriority(priorityList, "TxType.REQUIRES_NEW");
    }

    /*
     * @testName: getInterceptorPriorityForTxTypeMandatory
     *
     * @test_Strategy: The Transactional interceptors must have a priority of Interceptor.Priority.PLATFORM_BEFORE+200
     *
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void getInterceptorPriorityForTxTypeMandatory() throws Exception {
        String methodName = "txTypeMandatory";
        List<Integer> priorityList = one.getPriority(methodName);
        verifyInterceptorPriority(priorityList, "TxType.MANDATORY");
    }

    /*
     * @testName: getInterceptorPriorityForTxTypeSupports
     *
     * @test_Strategy: The Transactional interceptors must have a priority of Interceptor.Priority.PLATFORM_BEFORE+200
     *
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void getInterceptorPriorityForTxTypeSupports() throws Exception {
        String methodName = "txTypeSupports";
        List<Integer> priorityList = one.getPriority(methodName);
        verifyInterceptorPriority(priorityList, "TxType.SUPPORTS");
    }

    /*
     * @testName: getInterceptorPriorityForTxTypeNotSupported
     *
     * @test_Strategy: The Transactional interceptors must have a priority of Interceptor.Priority.PLATFORM_BEFORE+200
     *
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void getInterceptorPriorityForTxTypeNotSupported() throws Exception {
        String methodName = "txTypeNotSupported";
        List<Integer> priorityList = one.getPriority(methodName);
        verifyInterceptorPriority(priorityList, "TxType.NOT_SUPPORTED");
    }

    /*
     * @testName: getInterceptorPriorityForTxTypeNever
     *
     * @test_Strategy: The Transactional interceptors must have a priority of Interceptor.Priority.PLATFORM_BEFORE+200
     *
     */
    @Test
    @TargetVehicle("ejblitejsf")
    public void getInterceptorPriorityForTxTypeNever() throws Exception {
        String methodName = "txTypeNever";
        List<Integer> priorityList = one.getPriority(methodName);
        verifyInterceptorPriority(priorityList, "TxType.NEVER");

    }

    private void verifyInterceptorPriority(List<Integer> priorityList, String txType) throws Exception {
        String result = null;
        if (priorityList.contains(PLATFORM_BEFORE + 200)) {
            Helper.getLogger().log(Level.INFO, "Transactional Interceptor for " + txType + " has right interceptor priority");
            result = "Transactional Interceptor for " + txType + " has right interceptor priority";
        } else {
            throw new Exception("Transactional Interceptor for " + txType + " has incorrect interceptor priority : "
                    + Arrays.toString(priorityList.toArray()) + " Excpected value is :" + PLATFORM_BEFORE + 200);
        }

        if (result != null) {
            appendReason(result);
        }

    }

}