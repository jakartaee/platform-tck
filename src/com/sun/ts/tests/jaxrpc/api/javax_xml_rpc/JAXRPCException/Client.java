/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc.JAXRPCException;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: JAXRPCExceptionConstructorTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:13; JAXRPC:SPEC:316; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via JAXRPCException() constructor. Verify
   * JAXRPCException object created successfully.
   */
  public void JAXRPCExceptionConstructorTest1() throws Fault {
    TestUtil.logTrace("JAXRPCExceptionConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via JAXRPCException() ...");
      JAXRPCException e = new JAXRPCException();
      if (e != null) {
        TestUtil.logMsg("JAXRPCException object created successfully");
      } else {
        TestUtil.logErr("JAXRPCException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("JAXRPCExceptionConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("JAXRPCExceptionConstructorTest1 failed");
  }

  /*
   * @testName: JAXRPCExceptionConstructorTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:15; JAXRPC:SPEC:316; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via JAXRPCException(String, Throwable).
   * Verify JAXRPCException object created successfully.
   */
  public void JAXRPCExceptionConstructorTest2() throws Fault {
    TestUtil.logTrace("JAXRPCExceptionConstructorTest2");
    boolean pass = true;
    String detailMsg = "a detail message";
    Exception foo = new Exception("foo");
    try {
      TestUtil.logMsg(
          "Create instance via " + " JAXRPCException(String, Throwable) ...");
      JAXRPCException e = new JAXRPCException(detailMsg, foo);
      if (e != null) {
        TestUtil.logMsg("JAXRPCException object created successfully");
        String msg = e.getMessage();
        if (msg.equals(detailMsg))
          TestUtil.logMsg("detail message match: " + detailMsg);
        else {
          TestUtil.logErr("detail message mismatch - expected: " + detailMsg
              + ", received: " + msg);
          pass = false;
        }
        Throwable t = e.getLinkedCause();
        if (t != null) {
          TestUtil.logMsg("linked exception object exists - expected");
          if (t.equals(foo)) {
            TestUtil.logMsg("Linked exception returned correctly");
          } else {
            TestUtil.logErr("Linked exception returned incorrectly");
            pass = false;
          }
        } else {
          TestUtil.logErr("linked exception object non-existant - unexpected");
          pass = false;
        }
      } else {
        TestUtil.logErr("JAXRPCException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("JAXRPCExceptionConstructorTest2 failed", e);
    }

    if (!pass)
      throw new Fault("JAXRPCExceptionConstructorTest2 failed");
  }

  /*
   * @testName: JAXRPCExceptionConstructorTest3
   *
   * @assertion_ids: JAXRPC:JAVADOC:14; JAXRPC:SPEC:316; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via JAXRPCException(String). Verify
   * JAXRPCException object created successfully.
   */
  public void JAXRPCExceptionConstructorTest3() throws Fault {
    TestUtil.logTrace("JAXRPCExceptionConstructorTest3");
    boolean pass = true;
    String detailMsg = "a detail message";
    try {
      TestUtil.logMsg("Create instance via " + " JAXRPCException(String) ...");
      JAXRPCException e = new JAXRPCException(detailMsg);
      if (e != null) {
        TestUtil.logMsg("JAXRPCException object created successfully");
        String msg = e.getMessage();
        if (msg.equals(detailMsg))
          TestUtil.logMsg("detail message match: " + detailMsg);
        else {
          TestUtil.logErr("detail message mismatch - expected: " + detailMsg
              + ", received: " + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("JAXRPCException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("JAXRPCExceptionConstructorTest3 failed", e);
    }

    if (!pass)
      throw new Fault("JAXRPCExceptionConstructorTest3 failed");
  }

  /*
   * @testName: JAXRPCExceptionConstructorTest4
   *
   * @assertion_ids: JAXRPC:JAVADOC:16; JAXRPC:SPEC:316; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via JAXRPCException(Throwable). Verify
   * JAXRPCException object created successfully.
   */
  public void JAXRPCExceptionConstructorTest4() throws Fault {
    TestUtil.logTrace("JAXRPCExceptionConstructorTest4");
    boolean pass = true;
    Exception foo = new Exception("foo");
    try {
      TestUtil
          .logMsg("Create instance via " + " JAXRPCException(Throwable) ...");
      JAXRPCException e = new JAXRPCException(foo);
      if (e != null) {
        TestUtil.logMsg("JAXRPCException object created successfully");
        Throwable t = e.getLinkedCause();
        if (t != null) {
          TestUtil.logMsg("linked exception object exists - expected");
          if (t.equals(foo)) {
            TestUtil.logMsg("Linked exception returned correctly");
          } else {
            TestUtil.logErr("Linked exception returned incorrectly");
            pass = false;
          }
        } else {
          TestUtil.logErr("linked exception object non-existant - unexpected");
          pass = false;
        }
      } else {
        TestUtil.logErr("JAXRPCException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("JAXRPCExceptionConstructorTest4 failed", e);
    }

    if (!pass)
      throw new Fault("JAXRPCExceptionConstructorTest4 failed");
  }

  /*
   * @testName: getLinkedCauseTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:17; JAXRPC:SPEC:316; WS4EE:SPEC:70
   *
   * @test_Strategy: Test for both a linked exception and a non-linked
   * exception.
   */
  public void getLinkedCauseTest() throws Fault {
    TestUtil.logTrace("getLinkedCauseTest");
    boolean pass = true;

    if (!getLinkedCauseTest1())
      pass = false;
    if (!getLinkedCauseTest2())
      pass = false;
    if (!getLinkedCauseTest3())
      pass = false;

    if (!pass)
      throw new Fault("getLinkedCauseTest failed");
  }

  /*
   * Test no linked exception use constructor JAXRPCException().
   */
  private boolean getLinkedCauseTest1() throws Fault {
    TestUtil.logTrace("getLinkedCauseTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via JAXRPCException() ...");
      JAXRPCException e = new JAXRPCException();
      if (e != null) {
        TestUtil.logMsg("JAXRPCException object created successfully");
        Throwable t = e.getLinkedCause();
        if (t == null) {
          TestUtil.logMsg("linked exception object non-existant - expected");
        } else {
          TestUtil.logErr("linked exception object exists - unexpected");
          pass = false;
        }
      } else {
        TestUtil.logErr("JAXRPCException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  /*
   * Test linked exception use constructor JAXRPCException(String, Throwable)
   */
  private boolean getLinkedCauseTest2() throws Fault {
    TestUtil.logTrace("getLinkedCauseTest2");
    boolean pass = true;
    Exception foo = new Exception("foo");
    try {
      TestUtil
          .logMsg("Create instance via JAXRPCException(String, Throwable) ...");
      JAXRPCException e = new JAXRPCException("foo exception", foo);
      if (e != null) {
        TestUtil.logMsg("JAXRPCException object created successfully");
        Throwable t = e.getLinkedCause();
        if (t != null) {
          TestUtil.logMsg("linked exception object exists - expected");
          if (t.equals(foo)) {
            TestUtil.logMsg("Linked exception returned correctly");
          } else {
            TestUtil.logErr("Linked exception returned incorrectly");
            pass = false;
          }
        } else {
          TestUtil.logErr("linked exception object non-existant - unexpected");
          pass = false;
        }
      } else {
        TestUtil.logErr("JAXRPCException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    return pass;
  }

  /*
   * Test linked exception use constructor JAXRPCException(Throwable)
   */
  private boolean getLinkedCauseTest3() throws Fault {
    TestUtil.logTrace("getLinkedCauseTest3");
    boolean pass = true;
    Exception foo = new Exception("foo");
    try {
      TestUtil.logMsg("Create instance via JAXRPCException(Throwable) ...");
      JAXRPCException e = new JAXRPCException(foo);
      if (e != null) {
        TestUtil.logMsg("JAXRPCException object created successfully");
        Throwable t = e.getLinkedCause();
        if (t != null) {
          TestUtil.logMsg("linked exception object exists - expected");
          if (t.equals(foo)) {
            TestUtil.logMsg("Linked exception returned correctly");
          } else {
            TestUtil.logErr("Linked exception returned incorrectly");
            pass = false;
          }
        } else {
          TestUtil.logErr("linked exception object non-existant - unexpected");
          pass = false;
        }
      } else {
        TestUtil.logErr("JAXRPCException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    return pass;
  }
}
