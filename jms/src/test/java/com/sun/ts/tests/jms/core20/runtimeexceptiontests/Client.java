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

/*
 * $Id$
 */
package com.sun.ts.tests.jms.core20.runtimeexceptiontests;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;

public class Client extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core20.runtimeexceptiontests.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * @class.setup_props:
   * 
   * @exception Fault
   */

  public void setup(String[] args, Properties p) throws Fault {
  }

  /* cleanup */

  /*
   * cleanup() is called after each test
   * 
   * @exception Fault
   */

  public void cleanup() throws Fault {
  }

  /* Tests */

  /*
   * @testName: transactionRolledBackRuntimeExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:1117;
   * 
   * @test_Strategy: Construct TransactionRolledBackRuntimeException(String,
   * String)
   */
  public void transactionRolledBackRuntimeExceptionTest1() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Serious";
      String reason = "Rollback operation not allowed.";

      TestUtil
          .logMsg("Test TransactionRolledBackRuntimeException(String, String)");
      jakarta.jms.TransactionRolledBackRuntimeException exceptionToTest = new jakarta.jms.TransactionRolledBackRuntimeException(
          reason, errorCode);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.TransactionRolledBackRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("transactionRolledBackRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("transactionRolledBackRuntimeExceptionTest1 Failed");
  }

  /*
   * @testName: transactionRolledBackRuntimeExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:1118;
   * 
   * @test_Strategy: Construct TransactionRolledBackRuntimeException(String)
   */
  public void transactionRolledBackRuntimeExceptionTest2() throws Fault {
    boolean pass = true;
    try {
      String reason = "Rollback operation not allowed.";

      TestUtil.logMsg("Test TransactionRolledBackRuntimeException(String)");
      jakarta.jms.TransactionRolledBackRuntimeException exceptionToTest = new jakarta.jms.TransactionRolledBackRuntimeException(
          reason);
      try {
        throw exceptionToTest;
      } catch (jakarta.jms.TransactionRolledBackRuntimeException e) {
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("transactionRolledBackRuntimeExceptionTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("transactionRolledBackRuntimeExceptionTest2 Failed");
  }

  /*
   * @testName: transactionRolledBackRuntimeExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:1119;
   * 
   * @test_Strategy: Construct TransactionRolledBackRuntimeException(String,
   * String, Throwable)
   */
  public void transactionRolledBackRuntimeExceptionTest3() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Serious";
      String reason = "Rollback operation not allowed.";
      jakarta.jms.TransactionRolledBackException exception = new jakarta.jms.TransactionRolledBackException(
          reason);

      TestUtil.logMsg(
          "Test TransactionRolledBackRuntimeException(String, String, Throwable)");
      jakarta.jms.TransactionRolledBackRuntimeException exceptionToTest = new jakarta.jms.TransactionRolledBackRuntimeException(
          reason, errorCode, exception);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.TransactionRolledBackRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
        if (!exceptionToTest.getCause().equals(exception)) {
          TestUtil.logErr("Incorrect cause " + exceptionToTest.getCause());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("transactionRolledBackRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("transactionRolledBackRuntimeExceptionTest3 Failed");
  }

  /*
   * @testName: transactionInProgressRuntimeExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:1402;
   * 
   * @test_Strategy: Construct TransactionInProgressRuntimeException(String,
   * String)
   */
  public void transactionInProgressRuntimeExceptionTest1() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Serious";
      String reason = "Transaction already in progress.";

      TestUtil
          .logMsg("Test TransactionInProgressRuntimeException(String, String)");
      jakarta.jms.TransactionInProgressRuntimeException exceptionToTest = new jakarta.jms.TransactionInProgressRuntimeException(
          reason, errorCode);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.TransactionInProgressRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("transactionInProgressRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("transactionInProgressRuntimeExceptionTest1 Failed");
  }

  /*
   * @testName: transactionInProgressRuntimeExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:1403;
   * 
   * @test_Strategy: Construct TransactionInProgressRuntimeException(String)
   */
  public void transactionInProgressRuntimeExceptionTest2() throws Fault {
    boolean pass = true;
    try {
      String reason = "Transaction already in progress.";

      TestUtil.logMsg("Test TransactionInProgressRuntimeException(String)");
      jakarta.jms.TransactionInProgressRuntimeException exceptionToTest = new jakarta.jms.TransactionInProgressRuntimeException(
          reason);
      try {
        throw exceptionToTest;
      } catch (jakarta.jms.TransactionInProgressRuntimeException e) {
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("transactionInProgressRuntimeExceptionTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("transactionInProgressRuntimeExceptionTest2 Failed");
  }

  /*
   * @testName: transactionInProgressRuntimeExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:1439;
   * 
   * @test_Strategy: Construct TransactionInProgressRuntimeException(String,
   * String, Throwable)
   */
  public void transactionInProgressRuntimeExceptionTest3() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Serious";
      String reason = "Transaction already in progress.";
      jakarta.jms.TransactionInProgressRuntimeException exception = new jakarta.jms.TransactionInProgressRuntimeException(
          reason);

      TestUtil.logMsg(
          "Test TransactionInProgressRuntimeException(String, String, Throwable)");
      jakarta.jms.TransactionInProgressRuntimeException exceptionToTest = new jakarta.jms.TransactionInProgressRuntimeException(
          reason, errorCode, exception);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.TransactionInProgressRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
        if (!exceptionToTest.getCause().equals(exception)) {
          TestUtil.logErr("Incorrect cause " + exceptionToTest.getCause());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("transactionInProgressRuntimeExceptionTest3 Failed: ", e);
    }

    if (!pass)
      throw new Fault("transactionInProgressRuntimeExceptionTest3 Failed");
  }

  /*
   * @testName: resourceAllocationRuntimeExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:1440;
   * 
   * @test_Strategy: Construct ResourceAllocationRuntimeException(String,
   * String)
   */
  public void resourceAllocationRuntimeExceptionTest1() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Serious";
      String reason = "Resource allocation failure due to no more resources available.";

      TestUtil
          .logMsg("Test ResourceAllocationRuntimeException(String, String)");
      jakarta.jms.ResourceAllocationRuntimeException exceptionToTest = new jakarta.jms.ResourceAllocationRuntimeException(
          reason, errorCode);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.ResourceAllocationRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("resourceAllocationRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("resourceAllocationRuntimeExceptionTest1 Failed");
  }

  /*
   * @testName: resourceAllocationRuntimeExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:1441;
   * 
   * @test_Strategy: Construct ResourceAllocationRuntimeException(String)
   */
  public void resourceAllocationRuntimeExceptionTest2() throws Fault {
    boolean pass = true;
    try {
      String reason = "Resource allocation failure due to no more resources available.";

      TestUtil.logMsg("Test ResourceAllocationRuntimeException(String)");
      jakarta.jms.ResourceAllocationRuntimeException exceptionToTest = new jakarta.jms.ResourceAllocationRuntimeException(
          reason);
      try {
        throw exceptionToTest;
      } catch (jakarta.jms.ResourceAllocationRuntimeException e) {
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("resourceAllocationRuntimeExceptionTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("resourceAllocationRuntimeExceptionTest2 Failed");
  }

  /*
   * @testName: resourceAllocationRuntimeExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:1442;
   * 
   * @test_Strategy: Construct ResourceAllocationRuntimeException(String,
   * String, Throwable)
   */
  public void resourceAllocationRuntimeExceptionTest3() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Serious";
      String reason = "Resource allocation failure due to no more resources available.";
      jakarta.jms.TransactionRolledBackException exception = new jakarta.jms.TransactionRolledBackException(
          reason);

      TestUtil.logMsg(
          "Test ResourceAllocationRuntimeException(String, String, Throwable)");
      jakarta.jms.ResourceAllocationRuntimeException exceptionToTest = new jakarta.jms.ResourceAllocationRuntimeException(
          reason, errorCode, exception);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.ResourceAllocationRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
        if (!exceptionToTest.getCause().equals(exception)) {
          TestUtil.logErr("Incorrect cause " + exceptionToTest.getCause());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("resourceAllocationRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("resourceAllocationRuntimeExceptionTest3 Failed");
  }

  /*
   * @testName: messageNotWriteableRuntimeExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:1422;
   * 
   * @test_Strategy: Construct MessageNotWriteableRuntimeException(String,
   * String)
   */
  public void messageNotWriteableRuntimeExceptionTest1() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Serious";
      String reason = "Writing operation not allowed.";

      TestUtil
          .logMsg("Test MessageNotWriteableRuntimeException(String, String)");
      jakarta.jms.MessageNotWriteableRuntimeException exceptionToTest = new jakarta.jms.MessageNotWriteableRuntimeException(
          reason, errorCode);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.MessageNotWriteableRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("messageNotWriteableRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("messageNotWriteableRuntimeExceptionTest1 Failed");
  }

  /*
   * @testName: messageNotWriteableRuntimeExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:1423;
   * 
   * @test_Strategy: Construct MessageNotWriteableRuntimeException(String)
   */
  public void messageNotWriteableRuntimeExceptionTest2() throws Fault {
    boolean pass = true;
    try {
      String reason = "Writing operation not allowed.";

      TestUtil.logMsg("Test MessageNotWriteableRuntimeException(String)");
      jakarta.jms.MessageNotWriteableRuntimeException exceptionToTest = new jakarta.jms.MessageNotWriteableRuntimeException(
          reason);
      try {
        throw exceptionToTest;
      } catch (jakarta.jms.MessageNotWriteableRuntimeException e) {
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("messageNotWriteableRuntimeExceptionTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("messageNotWriteableRuntimeExceptionTest2 Failed");
  }

  /*
   * @testName: messageNotWriteableRuntimeExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:1424;
   * 
   * @test_Strategy: Construct MessageNotWriteableRuntimeException(String,
   * String, Throwable)
   */
  public void messageNotWriteableRuntimeExceptionTest3() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Serious";
      String reason = "Writing operation not allowed.";
      jakarta.jms.MessageNotWriteableException exception = new jakarta.jms.MessageNotWriteableException(
          reason);

      TestUtil.logMsg(
          "Test MessageNotWriteableRuntimeException(String, String, Throwable)");
      jakarta.jms.MessageNotWriteableRuntimeException exceptionToTest = new jakarta.jms.MessageNotWriteableRuntimeException(
          reason, errorCode, exception);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.MessageNotWriteableRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
        if (!exceptionToTest.getCause().equals(exception)) {
          TestUtil.logErr("Incorrect cause " + exceptionToTest.getCause());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("messageNotWriteableRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("messageNotWriteableRuntimeExceptionTest3 Failed");
  }

  /*
   * @testName: messageFormatRuntimeExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:883;
   * 
   * @test_Strategy: Construct MessageFormatRuntimeException(String, String)
   */
  public void messageFormatRuntimeExceptionTest1() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Urgent";
      String reason = "Cannot convert from int to char";

      TestUtil.logMsg("Test MessageFormatRuntimeException(String, String)");
      jakarta.jms.MessageFormatRuntimeException exceptionToTest = new jakarta.jms.MessageFormatRuntimeException(
          reason, errorCode);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.MessageFormatRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("messageFormatRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("messageFormatRuntimeExceptionTest1 Failed");
  }

  /*
   * @testName: messageFormatRuntimeExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:884;
   * 
   * @test_Strategy: Construct MessageFormatRuntimeException(String)
   */
  public void messageFormatRuntimeExceptionTest2() throws Fault {
    boolean pass = true;
    try {
      String reason = "Cannot convert from int to char";

      TestUtil.logMsg("Test MessageFormatRuntimeException(String)");
      jakarta.jms.MessageFormatRuntimeException exceptionToTest = new jakarta.jms.MessageFormatRuntimeException(
          reason);
      try {
        throw exceptionToTest;
      } catch (jakarta.jms.MessageFormatRuntimeException e) {
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("messageFormatRuntimeExceptionTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("messageFormatRuntimeExceptionTest2 Failed");
  }

  /*
   * @testName: messageFormatRuntimeExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:885;
   * 
   * @test_Strategy: Construct MessageFormatRuntimeException(String, String,
   * Throwable)
   */
  public void messageFormatRuntimeExceptionTest3() throws Fault {
    boolean pass = true;
    try {
      String errorCode = "Urgent";
      String reason = "Cannot convert from int to char";
      jakarta.jms.MessageFormatException exception = new jakarta.jms.MessageFormatException(
          reason);

      TestUtil.logMsg(
          "Test MessageFormatRuntimeException(String, String, Throwable)");
      jakarta.jms.MessageFormatRuntimeException exceptionToTest = new jakarta.jms.MessageFormatRuntimeException(
          reason, errorCode, exception);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.MessageFormatRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
        if (!exceptionToTest.getCause().equals(exception)) {
          TestUtil.logErr("Incorrect cause " + exceptionToTest.getCause());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("messageFormatRuntimeExceptionTest3 Failed: ", e);
    }

    if (!pass)
      throw new Fault("messageFormatRuntimeExceptionTest3 Failed");
  }

  /*
   * @testName: jmsSecurityRuntimeExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:871;
   * 
   * @test_Strategy: Construct JMSSecurityRuntimeException(String, String)
   */
  public void jmsSecurityRuntimeExceptionTest1() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Urgent";
      String reason = "Cannot find the user.";

      TestUtil.logMsg("Test JMSSecurityRuntimeException(String, String)");
      jakarta.jms.JMSSecurityRuntimeException exceptionToTest = new jakarta.jms.JMSSecurityRuntimeException(
          reason, errorCode);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.JMSSecurityRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jmsSecurityRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jmsSecurityRuntimeExceptionTest1 Failed");
  }

  /*
   * @testName: jmsSecurityRuntimeExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:872;
   * 
   * @test_Strategy: Construct JMSSecurityRuntimeException(String)
   */
  public void jmsSecurityRuntimeExceptionTest2() throws Fault {
    boolean pass = true;
    try {
      String reason = "Cannot find the user.";
      TestUtil.logMsg("Test JMSSecurityRuntimeException(String)");
      jakarta.jms.JMSSecurityRuntimeException exceptionToTest = new jakarta.jms.JMSSecurityRuntimeException(
          reason);
      try {
        throw exceptionToTest;
      } catch (jakarta.jms.JMSSecurityRuntimeException e) {
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("jmsSecurityRuntimeExceptionTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jmsSecurityRuntimeExceptionTest2 Failed");
  }

  /*
   * @testName: jmsSecurityRuntimeExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:873;
   * 
   * @test_Strategy: Construct JMSSecurityRuntimeException(String, String,
   * Throwable)
   */
  public void jmsSecurityRuntimeExceptionTest3() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Urgent";
      String reason = "Cannot find the user.";
      jakarta.jms.JMSSecurityException exception = new jakarta.jms.JMSSecurityException(
          reason);

      TestUtil.logMsg(
          "Test JMSSecurityRuntimeException(String, String, Throwable)");
      jakarta.jms.JMSSecurityRuntimeException exceptionToTest = new jakarta.jms.JMSSecurityRuntimeException(
          reason, errorCode, exception);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.JMSSecurityRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
        if (!exceptionToTest.getCause().equals(exception)) {
          TestUtil.logErr("Incorrect cause " + exceptionToTest.getCause());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("jmsSecurityRuntimeExceptionTest3 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jmsSecurityRuntimeExceptionTest3 Failed");
  }

  /*
   * @testName: jmsRuntimeExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:867; JMS:JAVADOC:865;
   * 
   * @test_Strategy: Construct JMSRuntimeException(String, String)
   */
  public void jmsRuntimeExceptionTest1() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Warning";
      String reason = "Not a JMS operation";

      TestUtil.logMsg("Test JMSRuntimeException(String, String)");
      jakarta.jms.JMSRuntimeException exceptionToTest = new jakarta.jms.JMSRuntimeException(
          reason, errorCode);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.JMSRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jmsRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jmsRuntimeExceptionTest1 Failed");
  }

  /*
   * @testName: jmsRuntimeExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:868;
   * 
   * @test_Strategy: Construct JMSRuntimeException(String)
   */
  public void jmsRuntimeExceptionTest2() throws Fault {
    boolean pass = true;

    try {
      String reason = "Not a JMS operation";

      TestUtil.logMsg("Test JMSRuntimeException(String)");
      jakarta.jms.JMSRuntimeException exceptionToTest = new jakarta.jms.JMSRuntimeException(
          reason);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.JMSRuntimeException e) {
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jmsRuntimeExceptionTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jmsRuntimeExceptionTest2 Failed");
  }

  /*
   * @testName: jmsRuntimeExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:869;
   * 
   * @test_Strategy: Construct JMSRuntimeException(String, String, Throwable)
   */
  public void jmsRuntimeExceptionTest3() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Warning";
      String reason = "Not a JMS operation";
      jakarta.jms.JMSException exception = new jakarta.jms.JMSException(reason);

      TestUtil.logMsg("Test JMSRuntimeException(String, String, Throwable)");
      jakarta.jms.JMSRuntimeException exceptionToTest = new jakarta.jms.JMSRuntimeException(
          reason, errorCode, exception);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.JMSRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
        if (!exceptionToTest.getCause().equals(exception)) {
          TestUtil.logErr("Incorrect cause " + exceptionToTest.getCause());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("jmsRuntimeExceptionTest3 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jmsRuntimeExceptionTest3 Failed");
  }

  /*
   * @testName: invalidSelectorRuntimeExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:862;
   * 
   * @test_Strategy: Construct InvalidSelectorRuntimeException(String, String)
   */
  public void invalidSelectorRuntimeExceptionTest1() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Urgent";
      String reason = "unknown variable";

      TestUtil.logMsg("Test InvalidSelectorRuntimeException(String, String)");
      jakarta.jms.InvalidSelectorRuntimeException exceptionToTest = new jakarta.jms.InvalidSelectorRuntimeException(
          reason, errorCode);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.InvalidSelectorRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("invalidSelectorRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("invalidSelectorRuntimeExceptionTest1 Failed");
  }

  /*
   * @testName: invalidSelectorRuntimeExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:863;
   * 
   * @test_Strategy: Construct InvalidSelectorRuntimeException(String)
   */
  public void invalidSelectorRuntimeExceptionTest2() throws Fault {
    boolean pass = true;
    try {
      String reason = "unknown variable";

      TestUtil.logMsg("Test InvalidSelectorRuntimeException(String)");
      jakarta.jms.InvalidSelectorRuntimeException exceptionToTest = new jakarta.jms.InvalidSelectorRuntimeException(
          reason);
      try {
        throw exceptionToTest;
      } catch (jakarta.jms.InvalidSelectorRuntimeException e) {
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("invalidSelectorRuntimeExceptionTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("invalidSelectorRuntimeExceptionTest2 Failed");
  }

  /*
   * @testName: invalidSelectorRuntimeExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:864;
   * 
   * @test_Strategy: Construct InvalidSelectorRuntimeException(String, String,
   * Throwable)
   */
  public void invalidSelectorRuntimeExceptionTest3() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Urgent";
      String reason = "unknown variable";
      jakarta.jms.InvalidSelectorException exception = new jakarta.jms.InvalidSelectorException(
          reason);

      TestUtil.logMsg(
          "Test InvalidSelectorRuntimeException(String, String, Throwable)");
      jakarta.jms.InvalidSelectorRuntimeException exceptionToTest = new jakarta.jms.InvalidSelectorRuntimeException(
          reason, errorCode, exception);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.InvalidSelectorRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
        if (!exceptionToTest.getCause().equals(exception)) {
          TestUtil.logErr("Incorrect cause " + exceptionToTest.getCause());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("invalidSelectorRuntimeExceptionTest3 Failed: ", e);
    }

    if (!pass)
      throw new Fault("invalidSelectorRuntimeExceptionTest3 Failed");
  }

  /*
   * @testName: invalidDestinationRuntimeExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:859;
   * 
   * @test_Strategy: Construct InvalidDestinationRuntimeException(String,
   * String)
   */
  public void invalidDestinationRuntimeExceptionTest1() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Urgent";
      String reason = "Destination is Null";

      TestUtil
          .logMsg("Test InvalidDestinationRuntimeException(String, String)");
      jakarta.jms.InvalidDestinationRuntimeException exceptionToTest = new jakarta.jms.InvalidDestinationRuntimeException(
          reason, errorCode);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.InvalidDestinationRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("invalidDestinationRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("invalidDestinationRuntimeExceptionTest1 Failed");
  }

  /*
   * @testName: invalidDestinationRuntimeExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:860;
   * 
   * @test_Strategy: Construct InvalidDestinationRuntimeException(String)
   */
  public void invalidDestinationRuntimeExceptionTest2() throws Fault {
    boolean pass = true;
    try {
      String reason = "Destination is Null";

      TestUtil.logMsg("Test InvalidDestinationRuntimeException(String)");
      jakarta.jms.InvalidDestinationRuntimeException exceptionToTest = new jakarta.jms.InvalidDestinationRuntimeException(
          reason);
      try {
        throw exceptionToTest;
      } catch (jakarta.jms.InvalidDestinationRuntimeException e) {
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("invalidDestinationRuntimeExceptionTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("invalidDestinationRuntimeExceptionTest2 Failed");
  }

  /*
   * @testName: invalidDestinationRuntimeExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:861;
   * 
   * @test_Strategy: Construct InvalidDestinationRuntimeException(String,
   * String, Throwable)
   */
  public void invalidDestinationRuntimeExceptionTest3() throws Fault {
    boolean pass = true;
    try {
      String errorCode = "Urgent";
      String reason = "Destination is Null";
      jakarta.jms.InvalidDestinationException exception = new jakarta.jms.InvalidDestinationException(
          reason);

      TestUtil.logMsg(
          "Test InvalidDestinationRuntimeException(String, String, Throwable)");
      jakarta.jms.InvalidDestinationRuntimeException exceptionToTest = new jakarta.jms.InvalidDestinationRuntimeException(
          reason, errorCode, exception);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.InvalidDestinationRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
        if (!exceptionToTest.getCause().equals(exception)) {
          TestUtil.logErr("Incorrect cause " + exceptionToTest.getCause());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("invalidDestinationRuntimeExceptionTest3 Failed: ", e);
    }

    if (!pass)
      throw new Fault("invalidDestinationRuntimeExceptionTest3 Failed");
  }

  /*
   * @testName: invalidClientIDRuntimeExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:856;
   * 
   * @test_Strategy: Construct InvalidClientIDRuntimeException(String, String)
   */
  public void invalidClientIDRuntimeExceptionTest1() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Urgent";
      String reason = "Duplicate Client ID";

      TestUtil.logMsg("Test InvalidClientIDRuntimeException(String, String)");
      jakarta.jms.InvalidClientIDRuntimeException exceptionToTest = new jakarta.jms.InvalidClientIDRuntimeException(
          reason, errorCode);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.InvalidClientIDRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("invalidClientIDRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("invalidClientIDRuntimeExceptionTest1 Failed");
  }

  /*
   * @testName: invalidClientIDRuntimeExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:857;
   * 
   * @test_Strategy: Construct InvalidClientIDRuntimeException(String)
   */
  public void invalidClientIDRuntimeExceptionTest2() throws Fault {
    boolean pass = true;
    try {
      String reason = "Duplicate Client ID";

      TestUtil.logMsg("Test InvalidClientIDRuntimeException(String)");
      jakarta.jms.InvalidClientIDRuntimeException exceptionToTest = new jakarta.jms.InvalidClientIDRuntimeException(
          reason);
      try {
        throw exceptionToTest;
      } catch (jakarta.jms.InvalidClientIDRuntimeException e) {
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("invalidClientIDRuntimeExceptionTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("invalidClientIDRuntimeExceptionTest2 Failed");
  }

  /*
   * @testName: invalidClientIDRuntimeExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:858;
   * 
   * @test_Strategy: Construct InvalidClientIDRuntimeException(String, String,
   * Throwable)
   */
  public void invalidClientIDRuntimeExceptionTest3() throws Fault {
    boolean pass = true;
    try {
      String errorCode = "Urgent";
      String reason = "Duplicate Client ID";
      jakarta.jms.InvalidClientIDException exception = new jakarta.jms.InvalidClientIDException(
          reason);

      TestUtil.logMsg(
          "Test InvalidClientIDRuntimeException(String, String, Throwable)");
      jakarta.jms.InvalidClientIDRuntimeException exceptionToTest = new jakarta.jms.InvalidClientIDRuntimeException(
          reason, errorCode, exception);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.InvalidClientIDRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
        if (!exceptionToTest.getCause().equals(exception)) {
          TestUtil.logErr("Incorrect cause " + exceptionToTest.getCause());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("invalidClientIDRuntimeExceptionTest3 Failed: ", e);
    }

    if (!pass)
      throw new Fault("invalidClientIDRuntimeExceptionTest3 Failed");
  }

  /*
   * @testName: illegalStateRuntimeExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:853;
   * 
   * @test_Strategy: Construct IllegalStateRuntimeException(String, String)
   */
  public void illegalStateRuntimeExceptionTest1() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Urgent";
      String reason = "The operation is illegal.";

      TestUtil.logMsg("Test IllegalStateRuntimeException(String, String)");
      jakarta.jms.IllegalStateRuntimeException exceptionToTest = new jakarta.jms.IllegalStateRuntimeException(
          reason, errorCode);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.IllegalStateRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("IllegalStateRuntimeExceptionTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("IllegalStateRuntimeExceptionTest1 Failed");
  }

  /*
   * @testName: illegalStateRuntimeExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:854;
   * 
   * @test_Strategy: Construct IllegalStateRuntimeException(String)
   */
  public void illegalStateRuntimeExceptionTest2() throws Fault {
    boolean pass = true;

    try {
      String reason = "The operation is illegal.";

      TestUtil.logMsg("Test IllegalStateRuntimeException(String)");
      jakarta.jms.IllegalStateRuntimeException exceptionToTest = new jakarta.jms.IllegalStateRuntimeException(
          reason);
      try {
        throw exceptionToTest;
      } catch (jakarta.jms.IllegalStateRuntimeException e) {
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("illegalStateRuntimeExceptionTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("IllegalStateRuntimeExceptionTest2 Failed");
  }

  /*
   * @testName: illegalStateRuntimeExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:855;
   * 
   * @test_Strategy: Construct IllegalStateRuntimeException(String, String,
   * Throwable)
   */
  public void illegalStateRuntimeExceptionTest3() throws Fault {
    boolean pass = true;

    try {
      String errorCode = "Urgent";
      String reason = "The operation is illegal.";
      jakarta.jms.IllegalStateException exception = new jakarta.jms.IllegalStateException(
          reason);

      TestUtil.logMsg(
          "Test IllegalStateRuntimeException(String, String, Throwable)");
      jakarta.jms.IllegalStateRuntimeException exceptionToTest = new jakarta.jms.IllegalStateRuntimeException(
          reason, errorCode, exception);

      try {
        throw exceptionToTest;
      } catch (jakarta.jms.IllegalStateRuntimeException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode)) {
          TestUtil
              .logErr("Incorrect ErrorCode " + exceptionToTest.getErrorCode());
          pass = false;
        }
        if (!exceptionToTest.getMessage().equals(reason)) {
          TestUtil.logErr("Incorrect reason " + exceptionToTest.getMessage());
          pass = false;
        }
        if (!exceptionToTest.getCause().equals(exception)) {
          TestUtil.logErr("Incorrect cause " + exceptionToTest.getCause());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("illegalStateRuntimeExceptionTest3 Failed: ", e);
    }

    if (!pass)
      throw new Fault("illegalStateRuntimeExceptionTest3 Failed");
  }
}
