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
package com.sun.ts.tests.jms.core.exceptiontests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class ExceptionTest extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.exceptiontests.ExceptionTest";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    ExceptionTest theTests = new ExceptionTest();
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
   * Closes the default connections that are created by setup(). Any separate
   * connections made by individual tests should be closed by that test.
   * 
   * @exception Fault
   */

  public void cleanup() throws Fault {
  }

  /* Tests */
  /*
   * @testName: transactionRolledBackExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:1;
   * 
   * @test_Strategy: Construct TransactionRolledBackException(String, String)
   * the correct message in it.
   */

  public void transactionRolledBackExceptionTest1() throws Fault {

    try {
      String errorCode = "Serious";
      String cause = "Rollback operation not allowed.";

      javax.jms.TransactionRolledBackException exceptionToTest = new TransactionRolledBackException(
          cause, errorCode);

      try {
        throw exceptionToTest;
      } catch (TransactionRolledBackException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("TransactionRolledBackExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: transactionRolledBackExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:2;
   * 
   * @test_Strategy: Construct TransactionRolledBackException(String)
   */

  public void transactionRolledBackExceptionTest2() throws Fault {
    try {
      javax.jms.TransactionRolledBackException exceptionToTest = new TransactionRolledBackException(
          "Rollback operation not allowed.");
      try {
        throw exceptionToTest;
      } catch (TransactionRolledBackException e) {
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("TransactionRolledBackExceptionTest2 Failed: ");
    }
  }

  /*
   * @testName: testTransactionInProgressException1
   * 
   * @assertion_ids: JMS:JAVADOC:3;
   * 
   * @test_Strategy: Construct TransactionInProgressException(String, String)
   */

  public void testTransactionInProgressException1() throws Fault {
    try {
      String errorCode = "Warning";
      String cause = "Transaction is still in progress.";

      javax.jms.TransactionInProgressException exceptionToTest = new TransactionInProgressException(
          cause, errorCode);
      try {
        throw exceptionToTest;
      } catch (TransactionInProgressException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("TransactionInProgressExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: testTransactionInProgressException2
   * 
   * @assertion_ids: JMS:JAVADOC:4;
   * 
   * @test_Strategy: Construct TransactionInProgressException(String)
   */

  public void testTransactionInProgressException2() throws Fault {
    try {
      javax.jms.TransactionInProgressException exceptionToTest = new TransactionInProgressException(
          "Transaction is still in progress.");
      try {
        throw exceptionToTest;
      } catch (TransactionInProgressException e) {
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("TransactionInProgressExceptionTest2 Failed: ");
    }
  }

  /*
   * @testName: testResourceAllocationException1
   * 
   * @assertion_ids: JMS:JAVADOC:10;
   * 
   * @test_Strategy: Construct ResourceAllocationException(String, String)
   */

  public void testResourceAllocationException1() throws Fault {

    try {
      String errorCode = "Urgent";
      String cause = "Cannot get the resource, not enough heap space.";

      javax.jms.ResourceAllocationException exceptionToTest = new ResourceAllocationException(
          cause, errorCode);

      try {
        throw exceptionToTest;
      } catch (ResourceAllocationException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("ResourceAllocationException1 Failed: ");
    }
  }

  /*
   * @testName: resourceAllocationExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:11;
   * 
   * @test_Strategy: Construct ResourceAllocationException(String)
   */

  public void resourceAllocationExceptionTest2() throws Fault {
    try {
      javax.jms.ResourceAllocationException exceptionToTest = new ResourceAllocationException(
          "Cannot get the resource, " + "not enough heap space.");
      try {
        throw exceptionToTest;
      } catch (ResourceAllocationException e) {
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("ResourceAllocationException2 Failed: ");
    }
  }

  /*
   * @testName: testMessageNotWriteableException1
   * 
   * @assertion_ids: JMS:JAVADOC:17;
   * 
   * @test_Strategy: Construct MessageNotWriteableException(String, String)
   */

  public void testMessageNotWriteableException1() throws Fault {

    try {
      String errorCode = "Urgent";
      String cause = "Message just received";

      javax.jms.MessageNotWriteableException exceptionToTest = new MessageNotWriteableException(
          cause, errorCode);

      try {
        throw exceptionToTest;
      } catch (MessageNotWriteableException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MessageNotWriteableExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: testMessageNotWriteableException2
   * 
   * @assertion_ids: JMS:JAVADOC:18;
   * 
   * @test_Strategy: Construct MessageNotWriteableException(String)
   */

  public void testMessageNotWriteableException2() throws Fault {
    try {
      javax.jms.MessageNotWriteableException exceptionToTest = new MessageNotWriteableException(
          "Message just received");
      try {
        throw exceptionToTest;
      } catch (MessageNotWriteableException e) {
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MessageNotWriteableExceptionTest2 Failed: ");
    }
  }

  /*
   * @testName: testMessageNotReadableException1
   * 
   * @assertion_ids: JMS:JAVADOC:19;
   * 
   * @test_Strategy: Construct MessageNotReadableException(String, String)
   */

  public void testMessageNotReadableException1() throws Fault {

    try {
      String errorCode = "Urgent";
      String cause = "Message is empty";

      javax.jms.MessageNotReadableException exceptionToTest = new MessageNotReadableException(
          cause, errorCode);

      try {
        throw exceptionToTest;
      } catch (MessageNotReadableException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MessageNotReadableExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: testMessageNotReadableException2
   * 
   * @assertion_ids: JMS:JAVADOC:20;
   * 
   * @test_Strategy: Construct MessageNotReadableException(String)
   */

  public void testMessageNotReadableException2() throws Fault {
    try {
      javax.jms.MessageNotReadableException exceptionToTest = new MessageNotReadableException(
          "Message is empty");
      try {
        throw exceptionToTest;
      } catch (MessageNotReadableException e) {
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MessageNotReadableExceptionTest2 Failed: ");
    }
  }

  /*
   * @testName: testMessageFormatException1
   * 
   * @assertion_ids: JMS:JAVADOC:21;
   * 
   * @test_Strategy: Construct MessageFormatException(String, String)
   */

  public void testMessageFormatException1() throws Fault {

    try {
      String errorCode = "Urgent";
      String cause = "Cannot convert from int to char";

      javax.jms.MessageFormatException exceptionToTest = new MessageFormatException(
          cause, errorCode);

      try {
        throw exceptionToTest;
      } catch (MessageFormatException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MessageFormatExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: testMessageFormatException2
   * 
   * @assertion_ids: JMS:JAVADOC:22;
   * 
   * @test_Strategy: Construct MessageFormatException(String)
   */

  public void testMessageFormatException2() throws Fault {
    try {
      javax.jms.MessageFormatException exceptionToTest = new MessageFormatException(
          "Cannot convert from int to char");
      try {
        throw exceptionToTest;
      } catch (MessageFormatException e) {
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MessageFormatExceptionTest2 Failed: ");
    }
  }

  /*
   * @testName: testMessageEOFException1
   * 
   * @assertion_ids: JMS:JAVADOC:23;
   * 
   * @test_Strategy: Construct MessageEOFException(String, String)
   */

  public void testMessageEOFException1() throws Fault {

    try {
      String errorCode = "Urgent";
      String cause = "It is a byte, not an int.";

      javax.jms.MessageEOFException exceptionToTest = new MessageEOFException(
          cause, errorCode);

      try {
        throw exceptionToTest;
      } catch (MessageEOFException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MessageEOFExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: testMessageEOFException2
   * 
   * @assertion_ids: JMS:JAVADOC:24;
   * 
   * @test_Strategy: Construct MessageEOFException(String)
   */

  public void testMessageEOFException2() throws Fault {
    try {
      javax.jms.MessageEOFException exceptionToTest = new MessageEOFException(
          "It is a byte, not an int.");
      try {
        throw exceptionToTest;
      } catch (MessageEOFException e) {
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MessageEOFExceptionTest2 Failed: ");
    }
  }

  /*
   * @testName: testJMSSecurityException1
   * 
   * @assertion_ids: JMS:JAVADOC:25;
   * 
   * @test_Strategy: Construct JMSSecurityException(String, String)
   */

  public void testJMSSecurityException1() throws Fault {

    try {
      String errorCode = "Urgent";
      String cause = "Cannot find the user.";

      javax.jms.JMSSecurityException exceptionToTest = new JMSSecurityException(
          cause, errorCode);

      try {
        throw exceptionToTest;
      } catch (JMSSecurityException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("JMSSecurityExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: testJMSSecurityException2
   * 
   * @assertion_ids: JMS:JAVADOC:26;
   * 
   * @test_Strategy: Construct JMSSecurityException(String)
   */

  public void testJMSSecurityException2() throws Fault {
    try {
      javax.jms.JMSSecurityException exceptionToTest = new JMSSecurityException(
          "Cannot find the user.");
      try {
        throw exceptionToTest;
      } catch (JMSSecurityException e) {
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("JMSSecurityExceptionTest2 Failed: ");
    }
  }

  /*
   * @testName: testJMSException1
   * 
   * @assertion_ids: JMS:JAVADOC:27; JMS:JAVADOC:29;
   * 
   * @test_Strategy: Construct JMSException(String, String)
   */

  public void testJMSException1() throws Fault {

    try {
      String errorCode = "Warning";
      String cause = "Not a JMS operation";

      javax.jms.JMSException exceptionToTest = new JMSException(cause,
          errorCode);

      try {
        throw exceptionToTest;
      } catch (JMSException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("JMSExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: testJMSException2
   * 
   * @assertion_ids: JMS:JAVADOC:28; JMS:JAVADOC:30; JMS:JAVADOC:31;
   * 
   * @test_Strategy: Construct JMSException(String)
   */

  public void testJMSException2() throws Fault {

    try {
      String cause = "Not a JMS operation";
      String cause1 = "Deprecated since v1.1";

      javax.jms.JMSException exceptionToTest = new JMSException(cause);
      javax.jms.JMSException exceptionToTest1 = new JMSException(cause1);

      exceptionToTest.setLinkedException(exceptionToTest1);

      if (!exceptionToTest.getLinkedException().getMessage()
          .equals(exceptionToTest1.getMessage()))
        throw new Fault("Linked Exception does not return correct message "
            + exceptionToTest.getLinkedException().getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("JMSExceptionTest2 Failed: ");
    }
  }

  /*
   * @testName: invalidSelectorExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:32;
   * 
   * @test_Strategy: Construct InvalidSelectorException(String, String)
   */

  public void invalidSelectorExceptionTest1() throws Fault {

    try {
      String errorCode = "Urgent";
      String cause = "unknown variable";

      javax.jms.InvalidSelectorException exceptionToTest = new InvalidSelectorException(
          cause, errorCode);

      try {
        throw exceptionToTest;
      } catch (InvalidSelectorException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("InvalidSelectorExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: invalidSelectorExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:33;
   * 
   * @test_Strategy: Construct InvalidSelectorException(String)
   */

  public void invalidSelectorExceptionTest2() throws Fault {
    try {
      javax.jms.InvalidSelectorException exceptionToTest = new InvalidSelectorException(
          "unknown variable");
      try {
        throw exceptionToTest;
      } catch (InvalidSelectorException e) {
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("InvalidSelectorExceptionTest2 Failed: ");
    }
  }

  /*
   * @testName: invalidDestinationExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:34;
   * 
   * @test_Strategy: Construct InvalidDestinationException(String, String)
   */

  public void invalidDestinationExceptionTest1() throws Fault {

    try {
      String errorCode = "Urgent";
      String cause = "Destination is Null";

      javax.jms.InvalidDestinationException exceptionToTest = new InvalidDestinationException(
          cause, errorCode);

      try {
        throw exceptionToTest;
      } catch (InvalidDestinationException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("InvalidDestinationExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: invalidDestinationExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:35;
   * 
   * @test_Strategy: Construct InvalidDestinationException(String)
   */

  public void invalidDestinationExceptionTest2() throws Fault {
    try {
      javax.jms.InvalidDestinationException exceptionToTest = new InvalidDestinationException(
          "Destination is Null");
      try {
        throw exceptionToTest;
      } catch (InvalidDestinationException e) {
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("InvalidDestinationExceptionTest2 Failed: ");
    }
  }

  /*
   * @testName: invalidClientIDExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:36;
   * 
   * @test_Strategy: Construct InvalidClientIDException(String, String)
   */

  public void invalidClientIDExceptionTest1() throws Fault {

    try {
      String errorCode = "Urgent";
      String cause = "Duplicate Client ID";

      javax.jms.InvalidClientIDException exceptionToTest = new InvalidClientIDException(
          cause, errorCode);

      try {
        throw exceptionToTest;
      } catch (InvalidClientIDException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("InvalidClientIDExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: invalidClientIDExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:37;
   * 
   * @test_Strategy: Construct InvalidClientIDException(String)
   */

  public void invalidClientIDExceptionTest2() throws Fault {
    try {
      javax.jms.InvalidClientIDException exceptionToTest = new InvalidClientIDException(
          "Duplicate Client ID");
      try {
        throw exceptionToTest;
      } catch (InvalidClientIDException e) {
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("InvalidClientIDExceptionTest2 Failed: ");
    }
  }

  /*
   * @testName: illegalStateExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:38;
   * 
   * @test_Strategy: Construct IllegalStateException(String, String)
   */

  public void illegalStateExceptionTest1() throws Fault {

    try {
      String errorCode = "Urgent";
      String cause = "The operation is intended for Queue Configuration only.";

      javax.jms.IllegalStateException exceptionToTest = new javax.jms.IllegalStateException(
          cause, errorCode);

      try {
        throw exceptionToTest;
      } catch (javax.jms.IllegalStateException e) {
        if (!exceptionToTest.getErrorCode().equals(errorCode))
          throw new Fault(
              "Incorrect ErrorCode " + exceptionToTest.getErrorCode(), e);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("IllegalStateExceptionTest1 Failed: ");
    }
  }

  /*
   * @testName: illegalStateExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:39;
   * 
   * @test_Strategy: Construct IllegalStateException(String)
   */

  public void illegalStateExceptionTest2() throws Fault {

    try {
      javax.jms.IllegalStateException exceptionToTest = new javax.jms.IllegalStateException(
          "The operation is intended for Queue Configuration only.");
      try {
        throw exceptionToTest;
      } catch (javax.jms.IllegalStateException e) {
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Failed to construct IllegalStateException: ");
    }
  }

}
