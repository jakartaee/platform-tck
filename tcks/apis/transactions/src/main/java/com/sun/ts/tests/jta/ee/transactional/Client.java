/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.jta.ee.transactional;

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

import java.util.Arrays;
import java.util.List;

import static java.util.logging.Level.INFO;

public class Client extends EJBLiteClientBase {
    private static StringBuilder callRecords = new StringBuilder();

    @Inject
    @OneManagedQualifier
    OneManagedBean one;

    @Inject
    TransactionScopedBean tscopedBean;

    @Resource(lookup = "java:comp/UserTransaction")
    private UserTransaction ut;

    public static void main(String[] args) {
        Client theTests = new Client();
        com.sun.ts.lib.harness.Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    @Inject
    @TwoManagedQualifier
    TwoManagedBean two;

    /*
     * @testName: txTypeRequiredReadOnly_withoutTransaction
     *
     * @test_Strategy: readOnly: If called inside a read-only transaction context, the managed bean method execution
     *  must then continue inside this transaction context.
     *  A read-only transaction may only run in a read-only transaction context, and similarly,
     *  a non-read-only transaction may only run in a non-read-only transaction context. As defined by the semantics
     *  of the configured {@code TxType}, a newly started transaction must be read-only if the {@code readOnly} element
     *  is {@code true}, and be non-read-only if the {@code readOnly} element is {@code false}.
     */
    public void txTypeRequiredReadOnly_withoutTransaction() throws Exception {
        Helper.assertEquals( "\n", "readOnly", one.txTypeRequiredReadOnly(), callRecords);
        appendReason(Helper.compareResult("readOnly", one.txTypeRequiredReadOnly()));

    }

    /*
     * @testName: txTypeRequiredReadOnly_withReadOnlyTransaction
     *
     * @test_Strategy: readOnly: If called inside a read-only transaction context, the managed bean method execution
     *  must then continue inside this transaction context.
     *  A read-only transaction may only run in a read-only transaction context, and similarly,
     *  a non-read-only transaction may only run in a non-read-only transaction context. As defined by the semantics
     *  of the configured {@code TxType}, a newly started transaction must be read-only if the {@code readOnly} element
     *  is {@code true}, and be non-read-only if the {@code readOnly} element is {@code false}.
     */
    public void txTypeRequiredReadOnly_withReadOnlyTransaction() throws Exception {
        try {
            ut.setReadOnly(true);
            ut.begin();
            Helper.assertEquals( null, "readOnly", one.txTypeRequiredReadOnly(), callRecords);
            appendReason(Helper.compareResult("readOnly", one.txTypeRequiredReadOnly()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(INFO, null, e);
            throw new Exception("txTypeRequiredReadOnly_withReadOnlyTransaction failed");
        } finally {
            ut.setReadOnly(false);
        }
    }

    /*
     * @testName: txTypeRequiredReadOnly_withNonReadOnlyTransaction
     *
     * @test_Strategy: readOnly: If called inside a read-only transaction context, the managed bean method execution
     *  must then continue inside this transaction context.
     *  A read-only transaction may only run in a read-only transaction context, and similarly,
     *  a non-read-only transaction may only run in a non-read-only transaction context. As defined by the semantics
     *  of the configured {@code TxType}, a newly started transaction must be read-only if the {@code readOnly} element
     *  is {@code true}, and be non-read-only if the {@code readOnly} element is {@code false}.
     */
    public void txTypeRequiredReadOnly_withNonReadOnlyTransaction() throws Exception {
        String result = "TransactionalException not received";

        try {
            ut.begin();
            Helper.getLogger().info("Invoking OneManagedBean.txTypeRequiredReadOnly() with a non-read-only transaction Context");
            one.txTypeRequiredReadOnly();
        } catch (TransactionalException te) {
            if (te.getCause() instanceof InvalidTransactionException) {
                result = "Received expected TransactionalException with nested InvalidTransactionException";
            } else {
                throw new Exception("Received TransactionalException without nested InvalidTransactionException");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "Received unexcepted Exception :" + e.getMessage();
        }

        if (result.equals("Received expected TransactionalException with nested InvalidTransactionException")) {
            Helper.getLogger().log(INFO, result);
            appendReason("Received expected TransactionalException with nested InvalidTransactionException");
        } else {
            throw new Exception(result);
        }
    }

    /*
     * @testName: txTypeRequiresNewReadOnly_withNonReadOnlyTransaction
     *
     * @test_Strategy: readOnly: If called inside a read-only transaction context, the managed bean method execution
     *  must then continue inside this transaction context.
     *  A read-only transaction may only run in a read-only transaction context, and similarly,
     *  a non-read-only transaction may only run in a non-read-only transaction context. As defined by the semantics
     *  of the configured {@code TxType}, a newly started transaction must be read-only if the {@code readOnly} element
     *  is {@code true}, and be non-read-only if the {@code readOnly} element is {@code false}.
     */
    public void txTypeRequiresNewReadOnly_withNonReadOnlyTransaction() throws Exception {
        try {
            ut.begin();
            Helper.assertEquals( null, "readOnly", one.txTypeRequiresNewReadOnly(), callRecords);
            appendReason(Helper.compareResult("readOnly", one.txTypeRequiresNewReadOnly()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(INFO, null, e);
            throw new Exception("txTypeRequiresNewReadOnly_withNonReadOnlyTransaction failed");
        }
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
    public void txTypeRequired_withoutTransaction() throws Exception {
        Helper.assertEquals("\n", "txTypeRequired called successfully", one.txTypeRequired(), callRecords);
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
    public void txTypeRequired_withTransaction() throws Exception {
        try {
            ut.begin();
            Helper.assertEquals(null, "txTypeRequired called successfully", one.txTypeRequired(), callRecords);
            appendReason(Helper.compareResult("txTypeRequired called successfully", one.txTypeRequired()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(INFO, null, e);
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
    public void txTypeRequired_IllegalStateException() throws Exception {
        Helper.assertEquals(null, "IllegalStateException", one.txTypeRequiredIllegalStateException(), callRecords);
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
    public void txTypeRequiresNew() throws Exception {
        Helper.assertEquals(null, "txTypeRequiresNew called successfully", one.txTypeRequiresNew(), callRecords);
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
    public void txTypeRequiresNew_withTransaction() throws Exception {
        try {
            ut.begin();
            Helper.assertEquals(null, "txTypeRequiresNew called successfully", one.txTypeRequiresNew(), callRecords);
            appendReason(Helper.compareResult("txTypeRequiresNew called successfully", one.txTypeRequiresNew()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(INFO, null, e);
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
            Helper.getLogger().log(INFO, result);
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
    public void txTypeMandatory_withTransaction() throws Exception {
        try {
            ut.begin();
            Helper.assertEquals(null, "txTypeMandatory called successfully", one.txTypeMandatory(), callRecords);
            appendReason(Helper.compareResult("txTypeMandatory called successfully", one.txTypeMandatory()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(INFO, null, e);
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
    public void txTypeSupports_withoutTransaction() throws Exception {
        Helper.assertEquals(null, "txTypeSupports run without active transaction", one.txTypeSupportsWithoutTransaction(), callRecords);
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
    public void txTypeSupports_withTransaction() throws Exception {
        try {
            ut.begin();
            Helper.assertEquals(null, "txTypeSupports called successfully", one.txTypeSupports(), callRecords);
            appendReason(Helper.compareResult("txTypeSupports called successfully", one.txTypeSupports()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(INFO, null, e);
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
    public void txTypeNotSupported_withoutTransaction() throws Exception {
        Helper.assertEquals(null, "txTypeNotSupported run without active transaction", one.txTypeNotSupported(), callRecords);
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
    public void txTypeNotSupported_withTransaction() throws Exception {
        try {
            ut.begin();
            Helper.assertEquals(null, "txTypeNotSupported run without active transaction", one.txTypeNotSupported(), callRecords);
            appendReason(Helper.compareResult("txTypeNotSupported run without active transaction", one.txTypeNotSupported()));
            ut.commit();
        } catch (Exception e) {
            Helper.getLogger().log(INFO, null, e);
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
    public void txTypeNever_withoutTransaction() throws Exception {
        Helper.assertEquals(null, "txTypeNever run without active transaction", one.txTypeNever(), callRecords);
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
            Helper.getLogger().log(INFO, result);
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
            Helper.getLogger().log(INFO, result);
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
            Helper.getLogger().log(INFO, result);
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
            Helper.getLogger().log(INFO, result);
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
            Helper.getLogger().log(INFO, result);
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
            Helper.getLogger().log(INFO, result);
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
            Helper.getLogger().log(INFO, result);
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
            Helper.getLogger().log(INFO, result);
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
            Helper.getLogger().log(INFO, result);
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
    public void getInterceptorPriorityForTxTypeNever() throws Exception {
        String methodName = "txTypeNever";
        List<Integer> priorityList = one.getPriority(methodName);
        verifyInterceptorPriority(priorityList, "TxType.NEVER");

    }

    private void verifyInterceptorPriority(List<Integer> priorityList, String txType) throws Exception {
        String result = null;
        if (priorityList.contains(Interceptor.Priority.PLATFORM_BEFORE + 200)) {
            Helper.getLogger().log(INFO, "Transactional Interceptor for " + txType + " has right interceptor priority");
            result = "Transactional Interceptor for " + txType + " has right interceptor priority";
        } else {
            throw new Exception("Transactional Interceptor for " + txType + " has incorrect interceptor priority : "
                    + Arrays.toString(priorityList.toArray()) + " Excpected value is :" + Interceptor.Priority.PLATFORM_BEFORE + 200);
        }

        if (result != null) {
            appendReason(result);
        }

    }
}
