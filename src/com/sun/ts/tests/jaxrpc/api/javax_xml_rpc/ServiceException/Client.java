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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc.ServiceException;

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
   * @testName: ServiceExceptionConstructorTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:1; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via ServiceException() constructor. Verify
   * ServiceException object created successfully.
   */
  public void ServiceExceptionConstructorTest1() throws Fault {
    TestUtil.logTrace("ServiceExceptionConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via ServiceException() ...");
      ServiceException e = new ServiceException();
      if (e != null) {
        TestUtil.logMsg("ServiceException object created successfully");
      } else {
        TestUtil.logErr("ServiceException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ServiceExceptionConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("ServiceExceptionConstructorTest1 failed");
  }

  /*
   * @testName: ServiceExceptionConstructorTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:3; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via ServiceException(String, Throwable).
   * Verify ServiceException object created successfully.
   */
  public void ServiceExceptionConstructorTest2() throws Fault {
    TestUtil.logTrace("ServiceExceptionConstructorTest2");
    boolean pass = true;
    String detailMsg = "a detail message";
    Exception foo = new Exception("foo");
    try {
      TestUtil.logMsg(
          "Create instance via " + " ServiceException(String, Throwable) ...");
      ServiceException e = new ServiceException(detailMsg, foo);
      if (e != null) {
        TestUtil.logMsg("ServiceException object created successfully");
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
        TestUtil.logErr("ServiceException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ServiceExceptionConstructorTest2 failed", e);
    }

    if (!pass)
      throw new Fault("ServiceExceptionConstructorTest2 failed");
  }

  /*
   * @testName: ServiceExceptionConstructorTest3
   *
   * @assertion_ids: JAXRPC:JAVADOC:2; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via ServiceException(String). Verify
   * ServiceException object created successfully.
   */
  public void ServiceExceptionConstructorTest3() throws Fault {
    TestUtil.logTrace("ServiceExceptionConstructorTest3");
    boolean pass = true;
    String detailMsg = "a detail message";
    try {
      TestUtil.logMsg("Create instance via " + " ServiceException(String) ...");
      ServiceException e = new ServiceException(detailMsg);
      if (e != null) {
        TestUtil.logMsg("ServiceException object created successfully");
        String msg = e.getMessage();
        if (msg.equals(detailMsg))
          TestUtil.logMsg("detail message match: " + detailMsg);
        else {
          TestUtil.logErr("detail message mismatch - expected: " + detailMsg
              + ", received: " + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("ServiceException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ServiceExceptionConstructorTest3 failed", e);
    }

    if (!pass)
      throw new Fault("ServiceExceptionConstructorTest3 failed");
  }

  /*
   * @testName: ServiceExceptionConstructorTest4
   *
   * @assertion_ids: JAXRPC:JAVADOC:4; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via ServiceException(Throwable). Verify
   * ServiceException object created successfully.
   */
  public void ServiceExceptionConstructorTest4() throws Fault {
    TestUtil.logTrace("ServiceExceptionConstructorTest4");
    boolean pass = true;
    Exception foo = new Exception("foo");
    try {
      TestUtil
          .logMsg("Create instance via " + " ServiceException(Throwable) ...");
      ServiceException e = new ServiceException(foo);
      if (e != null) {
        TestUtil.logMsg("ServiceException object created successfully");
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
        TestUtil.logErr("ServiceException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ServiceExceptionConstructorTest4 failed", e);
    }

    if (!pass)
      throw new Fault("ServiceExceptionConstructorTest4 failed");
  }

  /*
   * @testName: getLinkedCauseTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:5; WS4EE:SPEC:70
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
   * Test no linked exception use constructor ServiceException().
   */
  private boolean getLinkedCauseTest1() throws Fault {
    TestUtil.logTrace("getLinkedCauseTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via ServiceException() ...");
      ServiceException e = new ServiceException();
      if (e != null) {
        TestUtil.logMsg("ServiceException object created successfully");
        Throwable t = e.getLinkedCause();
        if (t == null) {
          TestUtil.logMsg("linked exception object non-existant - expected");
        } else {
          TestUtil.logErr("linked exception object exists - unexpected");
          pass = false;
        }
      } else {
        TestUtil.logErr("ServiceException object not created");
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
   * Test linked exception use constructor ServiceException(String, Throwable)
   */
  private boolean getLinkedCauseTest2() throws Fault {
    TestUtil.logTrace("getLinkedCauseTest2");
    boolean pass = true;
    Exception foo = new Exception("foo");
    try {
      TestUtil.logMsg(
          "Create instance via ServiceException(String, Throwable) ...");
      ServiceException e = new ServiceException("foo exception", foo);
      if (e != null) {
        TestUtil.logMsg("ServiceException object created successfully");
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
        TestUtil.logErr("ServiceException object not created");
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
   * Test linked exception use constructor ServiceException(Throwable)
   */
  private boolean getLinkedCauseTest3() throws Fault {
    TestUtil.logTrace("getLinkedCauseTest3");
    boolean pass = true;
    Exception foo = new Exception("foo");
    try {
      TestUtil.logMsg("Create instance via ServiceException(Throwable) ...");
      ServiceException e = new ServiceException(foo);
      if (e != null) {
        TestUtil.logMsg("ServiceException object created successfully");
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
        TestUtil.logErr("ServiceException object not created");
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
