/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package com.sun.ts.tests.ejb30.bb.localaccess.mdbclient;

import com.sun.ts.tests.ejb30.bb.localaccess.common.StatefulDefaultLocalIF;
import com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.EJBs;
import javax.annotation.Resource;
import com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanBase;
import com.sun.ts.tests.ejb30.bb.localaccess.common.CommonIF;
import com.sun.ts.tests.ejb30.bb.localaccess.common.LocalIF;
import com.sun.ts.tests.ejb30.bb.localaccess.common.DefaultLocalIF;
import com.sun.ts.tests.ejb30.bb.localaccess.common.StatefulLocalIF;
import com.sun.ts.tests.ejb30.common.calc.CalculatorException;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.common.messaging.StatusReporter;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ejb.TransactionRolledbackLocalException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.transaction.UserTransaction;
import static javax.transaction.Status.STATUS_MARKED_ROLLBACK;
import static javax.transaction.Status.STATUS_UNKNOWN;
import static javax.transaction.Status.STATUS_ACTIVE;

@MessageDriven(name = "MdbTestBean", messageListenerInterface = MessageListener.class, activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })

@EJBs({
    @EJB(name = "ejb/localStatelessRefName", beanName = "StatelessLocalBean", beanInterface = LocalIF.class),
    @EJB(name = "ejb/localStateless2RefName", beanName = "StatelessLocal2Bean", beanInterface = LocalIF.class),
    @EJB(name = "ejb/defaultLocalStatelessRefName", beanName = "StatelessDefaultLocalBean", beanInterface = DefaultLocalIF.class),
    @EJB(name = "ejb/localStatefulRefName", beanName = "StatefulLocalBean", beanInterface = StatefulLocalIF.class),
    @EJB(name = "ejb/defaultLocalStatefulRefName", beanName = "StatefulDefaultLocalBean", beanInterface = StatefulDefaultLocalIF.class) })

@TransactionManagement(TransactionManagementType.BEAN)
public class MdbTestBean extends TestBeanBase implements MessageListener,
    com.sun.ts.tests.ejb30.bb.localaccess.common.Constants {

  @Resource(name = "mdc")
  private MessageDrivenContext context;

  @Resource(name = "ut")
  private UserTransaction ut;

  @Resource(name = "qFactory")
  private QueueConnectionFactory qFactory;

  @Resource(name = "replyQueue")
  private Queue replyQueue;

  public EJBContext getEJBContext() {
    return context;
  }

  private final static Map mBeanTypes = new HashMap();

  private final static Map mBeanRefNames = new HashMap();

  static {
    // 1 localStateless
    mBeanTypes.put(passByReferenceTest1, LocalIF.class);
    mBeanTypes.put(exceptionTest1, LocalIF.class);
    mBeanTypes.put(runtimeExceptionTest1, LocalIF.class);

    mBeanRefNames.put(passByReferenceTest1, "ejb/localStatelessRefName");
    mBeanRefNames.put(exceptionTest1, "ejb/localStatelessRefName");
    mBeanRefNames.put(runtimeExceptionTest1, "ejb/localStatelessRefName");

    // 2 defaultLocalStateless
    mBeanTypes.put(passByReferenceTest2, DefaultLocalIF.class);
    mBeanTypes.put(exceptionTest2, DefaultLocalIF.class);
    mBeanTypes.put(runtimeExceptionTest2, DefaultLocalIF.class);

    mBeanRefNames.put(passByReferenceTest2, "ejb/defaultLocalStatelessRefName");
    mBeanRefNames.put(exceptionTest2, "ejb/defaultLocalStatelessRefName");
    mBeanRefNames.put(runtimeExceptionTest2,
        "ejb/defaultLocalStatelessRefName");

    // 3 localStateful
    mBeanTypes.put(passByReferenceTest3, StatefulLocalIF.class);
    mBeanTypes.put(exceptionTest3, StatefulLocalIF.class);
    mBeanTypes.put(runtimeExceptionTest3, StatefulLocalIF.class);

    mBeanRefNames.put(passByReferenceTest3, "ejb/localStatefulRefName");
    mBeanRefNames.put(exceptionTest3, "ejb/localStatefulRefName");
    mBeanRefNames.put(runtimeExceptionTest3, "ejb/localStatefulRefName");

    // 4 defaultLocalStateful
    mBeanTypes.put(passByReferenceTest4, DefaultLocalIF.class);
    mBeanTypes.put(exceptionTest4, DefaultLocalIF.class);
    mBeanTypes.put(runtimeExceptionTest4, DefaultLocalIF.class);

    mBeanRefNames.put(passByReferenceTest4, "ejb/defaultLocalStatefulRefName");
    mBeanRefNames.put(exceptionTest4, "ejb/defaultLocalStatefulRefName");
    mBeanRefNames.put(runtimeExceptionTest4, "ejb/defaultLocalStatefulRefName");

    // 5 localStateless2
    mBeanTypes.put(passByReferenceTest5, LocalIF.class);
    mBeanTypes.put(exceptionTest5, LocalIF.class);
    mBeanTypes.put(runtimeExceptionTest5, LocalIF.class);

    mBeanRefNames.put(passByReferenceTest5, "ejb/localStateless2RefName");
    mBeanRefNames.put(exceptionTest5, "ejb/localStateless2RefName");
    mBeanRefNames.put(runtimeExceptionTest5, "ejb/localStateless2RefName");

  }

  public MdbTestBean() {
    super();
  }

  // ================== business methods ====================================
  public void onMessage(javax.jms.Message msg) {
    boolean status = false;
    String reason = null;
    String testname = null;
    try {
      testname = msg.getStringProperty(
          com.sun.ts.tests.ejb30.common.messaging.Constants.TEST_NAME_KEY);
    } catch (javax.jms.JMSException e) {
      status = false;
      reason = "Failed to get test name from message: " + msg;
      TLogger.log(reason);
      StatusReporter.report(testname, status, reason, qFactory, replyQueue);
      return;
    }
    if (isPassByReferenceTest(testname)) {
      passByReferenceTest(testname, new String[] { CLIENT_MSG },
          getBeanRefName(testname), getBeanType(testname));
    } else if (isExceptionTest(testname)) {
      exceptionTest(testname, getBeanRefName(testname), getBeanType(testname));
    } else if (isRuntimeExceptionTest(testname)) {
      runtimeExceptionTest(testname, getBeanRefName(testname),
          getBeanType(testname));
    } else {
      unrecognizedTest(testname);
    }
  }

  public void remove() {
  }

  public void unrecognizedTest(String testname) {
    boolean status = false;
    String reason = "Unrecognized test: " + testname;
    TLogger.log(reason);
    StatusReporter.report(testname, status, reason, qFactory, replyQueue);
  }

  public void passByReferenceTest(String testname, String[] args,
      String refName, Class klass) {
    boolean status = true;
    String reason = null;
    CommonIF bean = null;
    try {
      bean = lookup(refName, klass);
    } catch (TestFailedException e) {
      status = false;
      reason = e.getMessage();
      TLogger.log(status, reason);
      StatusReporter.report(testname, status, reason, qFactory, replyQueue);
      return;
    }
    try {
      bean.passByReferenceTest(args);
    } catch (Exception e) {
      status = false;
      reason = "Unexpected exception: " + e.toString();
      TLogger.log(status, reason);
      StatusReporter.report(testname, status, reason, qFactory, replyQueue);
      return;
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        // ignore
      }
    }
    String expected = SERVER_MSG;
    String actual = args[0];

    if (expected.equals(actual)) {
      status = true;
      reason = "Got expected value '" + expected + "'";
    } else {
      status = false;
      reason = "Expect '" + expected + "', but actual '" + actual + "'";
    }
    TLogger.log(status, reason);
    StatusReporter.report(testname, status, reason, qFactory, replyQueue);
  }

  public void exceptionTest(String testname, String refName, Class klass) {
    boolean status = true;
    String reason = null;
    CommonIF bean = null;
    try {
      bean = lookup(refName, klass);
    } catch (TestFailedException e) {
      status = false;
      reason = e.getMessage();
      TLogger.log(status, reason);
      StatusReporter.report(testname, status, reason, qFactory, replyQueue);
      return;
    }

    try {
      ut.begin();
      bean.exceptionTest();
      status = false;
      reason = "Expect CalculatorException, but got no exception.";
    } catch (CalculatorException e) {
      int code = STATUS_UNKNOWN;
      try {
        code = ut.getStatus();
        if (code == STATUS_ACTIVE) {
          status = true;
          reason = "Good, got expected exception and tx status.";
        } else {
          status = false;
          reason = "Got expected CalculatorException, but tx status " + code
              + " is not expected " + STATUS_ACTIVE;
        }
      } catch (Exception e2) {
        status = false;
        reason = "Failed to get ut.getStatus " + e2;
      }
    } catch (Exception e) {
      status = false;
      reason = "Expecting CalculatorException, but got " + e;
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        // ignore
      }
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }
    }
    TLogger.log(status, reason);
    StatusReporter.report(testname, status, reason, qFactory, replyQueue);
  }

  public void runtimeExceptionTest(String testname, String refName,
      Class klass) {
    boolean status = true;
    String reason = null;
    CommonIF bean = null;
    try {
      bean = lookup(refName, klass);
    } catch (TestFailedException e) {
      status = false;
      reason = e.getMessage();
      TLogger.log(status, reason);
      StatusReporter.report(testname, status, reason, qFactory, replyQueue);
      return;
    }

    try {
      ut.begin();
      bean.runtimeExceptionTest();
      status = false;
      reason = "Expect javax.ejb.EJBTransactionRolledbackException, but got no exception.";
    } catch (EJBTransactionRolledbackException e) {
      int code = STATUS_UNKNOWN;
      try {
        code = ut.getStatus();
        if (code == STATUS_MARKED_ROLLBACK) {
          status = true;
          reason = "Good, got expected exception and tx status.";
        } else {
          status = false;
          reason = "Got expected EJBTransactionRolledbackException, but tx status "
              + code + " is not expected " + STATUS_MARKED_ROLLBACK;
        }
      } catch (Exception e2) {
        status = false;
        reason = "Failed to get ut.getStatus " + e2;
      }
    } catch (Exception e) {
      status = false;
      reason = "Expecting EJBTransactionRolledbackException, but got " + e;
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        // ignore
      }
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }
    }
    TLogger.log(status, reason);
    StatusReporter.report(testname, status, reason, qFactory, replyQueue);
  }

  private String getBeanRefName(String testname) {
    return (String) mBeanRefNames.get(testname);
  }

  private Class getBeanType(String testname) {
    return (Class) mBeanTypes.get(testname);
  }

  private boolean isRuntimeExceptionTest(String testname) {
    return runtimeExceptionTest1.equals(testname)
        || runtimeExceptionTest2.equals(testname)
        || runtimeExceptionTest3.equals(testname)
        || runtimeExceptionTest4.equals(testname)
        || runtimeExceptionTest5.equals(testname);
  }

  private boolean isPassByReferenceTest(String testname) {
    return passByReferenceTest1.equals(testname)
        || passByReferenceTest2.equals(testname)
        || passByReferenceTest3.equals(testname)
        || passByReferenceTest4.equals(testname)
        || passByReferenceTest5.equals(testname);
  }

  private boolean isExceptionTest(String testname) {
    return exceptionTest1.equals(testname) || exceptionTest2.equals(testname)
        || exceptionTest3.equals(testname) || exceptionTest4.equals(testname)
        || exceptionTest5.equals(testname);
  }
}
