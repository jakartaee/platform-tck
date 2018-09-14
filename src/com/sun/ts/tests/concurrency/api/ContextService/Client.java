/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.concurrency.api.ContextService;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;

import javax.enterprise.concurrent.ContextService;
import javax.enterprise.concurrent.ManagedTaskListener;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Client extends ServiceEETest implements Serializable {

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      // do your setup if any here
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: ContextServiceWithIntf
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:5
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using instance and interface.
   */
  public void ContextServiceWithIntf() throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");
      Runnable proxy = (Runnable) cs
          .createContextualProxy(new TestRunnableWork(), Runnable.class);
      pass = true;
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("ContextServiceWithIntf failed");
  }

  /*
   * @testName: ContextServiceWithIntfAndIntfNoImplemented
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:6
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using instance and interface. if the instance does not implement the
   * specified interface, IllegalArgumentException will be thrown
   */
  public void ContextServiceWithIntfAndIntfNoImplemented() throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");
      Object proxy = cs.createContextualProxy(new Object(), Runnable.class);
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (IllegalArgumentException ie) {
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("ContextServiceWithIntfAndIntfNoImplemented failed");
  }

  /*
   * @testName: ContextServiceWithIntfAndInstanceIsNull
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:6
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using instance and interface. if the instance is null,
   * IllegalArgumentException will be thrown
   */
  public void ContextServiceWithIntfAndInstanceIsNull() throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");
      Object proxy = cs.createContextualProxy(null, Runnable.class);
      TestUtil.logTrace(proxy.toString());
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (IllegalArgumentException ie) {
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("ContextServiceWithIntfAndInstanceIsNull failed");
  }

  /*
   * @testName: ContextServiceWithMultiIntfs
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:7
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using instance and multiple interfaces.
   */
  public void ContextServiceWithMultiIntfs() throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");
      Object proxy = cs.createContextualProxy(new TestRunnableWork(),
          Runnable.class, TestWorkInterface.class);
      pass = proxy instanceof Runnable && proxy instanceof TestWorkInterface;
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("ContextServiceWithMultiIntfs failed");
  }

  /*
   * @testName: ContextServiceWithMultiIntfsAndIntfNoImplemented
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:8
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using instance and multi interfaces. if the instance does not
   * implement the specified interface, IllegalArgumentException will be thrown
   */
  public void ContextServiceWithMultiIntfsAndIntfNoImplemented() throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");
      Object proxy = cs.createContextualProxy(new TestRunnableWork(),
          Runnable.class, TestWorkInterface.class, ManagedTaskListener.class);
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (IllegalArgumentException ie) {
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault(
          "ContextServiceWithMultiIntfsAndIntfNoImplemented failed");
  }

  /*
   * @testName: ContextServiceWithMultiIntfsAndInstanceIsNull
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:8
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using object and multi interfaces. if the instance is null,
   * IllegalArgumentException will be thrown
   */
  public void ContextServiceWithMultiIntfsAndInstanceIsNull() throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");
      Object proxy = cs.createContextualProxy(null, Runnable.class,
          TestWorkInterface.class);
      TestUtil.logTrace(proxy.toString());
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (IllegalArgumentException ie) {
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("ContextServiceWithMultiIntfsAndInstanceIsNull failed");
  }

  /*
   * @testName: ContextServiceWithIntfAndProperties
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:9
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using ExecutionProperties and interface.
   */
  public void ContextServiceWithIntfAndProperties() throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");

      Map<String, String> execProps = new HashMap<String, String>();
      execProps.put("vendor_a.security.tokenexpiration", "15000");
      execProps.put("USE_PARENT_TRANSACTION", "true");

      Runnable proxy = (Runnable) cs.createContextualProxy(
          new TestRunnableWork(), execProps, Runnable.class);
      pass = true;

    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("ContextServiceWithIntfAndProperties failed");
  }

  /*
   * @testName: ContextServiceWithMultiIntfsAndProperties
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:11
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using ExecutionProperties and multiple interfaces.
   */
  public void ContextServiceWithMultiIntfsAndProperties() throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");

      Map<String, String> execProps = new HashMap<String, String>();
      execProps.put("vendor_a.security.tokenexpiration", "15000");
      execProps.put("USE_PARENT_TRANSACTION", "true");

      Object proxy = cs.createContextualProxy(new TestRunnableWork(), execProps,
          Runnable.class, TestWorkInterface.class);
      pass = proxy instanceof Runnable && proxy instanceof TestWorkInterface;
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("ContextServiceWithMultiIntfsAndProperties failed");
  }

  /*
   * @testName: ContextServiceWithIntfAndPropertiesAndIntfNoImplemented
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:10
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using ExecutionProperties and interface. if the instance does not
   * implement the specified interface, IllegalArgumentException will be thrown
   */
  public void ContextServiceWithIntfAndPropertiesAndIntfNoImplemented()
      throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");

      Map<String, String> execProps = new HashMap<String, String>();
      execProps.put("vendor_a.security.tokenexpiration", "15000");
      execProps.put("USE_PARENT_TRANSACTION", "true");

      Object proxy = cs.createContextualProxy(new TestRunnableWork(), execProps,
          Runnable.class, ManagedTaskListener.class);
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (IllegalArgumentException ie) {
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault(
          "ContextServiceWithIntfAndPropertiesAndIntfNoImplemented failed");
  }

  /*
   * @testName: ContextServiceWithIntfsAndPropertiesAndInstanceIsNull
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:10
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using ExecutionProperties and interfaces. if the instance is null,
   * IllegalArgumentException will be thrown
   */
  public void ContextServiceWithIntfsAndPropertiesAndInstanceIsNull()
      throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");

      Map<String, String> execProps = new HashMap<String, String>();
      execProps.put("vendor_a.security.tokenexpiration", "15000");
      execProps.put("USE_PARENT_TRANSACTION", "true");

      Object proxy = cs.createContextualProxy(null, execProps, Runnable.class);
      TestUtil.logTrace(proxy.toString());
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (IllegalArgumentException ie) {
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault(
          "ContextServiceWithIntfsAndPropertiesAndInstanceIsNull failed");
  }

  /*
   * @testName: ContextServiceWithMultiIntfsAndPropertiesAndIntfNoImplemented
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:12
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using ExecutionProperties and multiple interfaces. if the instance
   * does not implement the specified interface, IllegalArgumentException will
   * be thrown
   */
  public void ContextServiceWithMultiIntfsAndPropertiesAndIntfNoImplemented()
      throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");

      Map<String, String> execProps = new HashMap<String, String>();
      execProps.put("vendor_a.security.tokenexpiration", "15000");
      execProps.put("USE_PARENT_TRANSACTION", "true");

      Object proxy = cs.createContextualProxy(new TestRunnableWork(), execProps,
          Runnable.class, TestWorkInterface.class, ManagedTaskListener.class);
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (IllegalArgumentException ie) {
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault(
          "ContextServiceWithMultiIntfsAndPropertiesAndIntfNoImplemented failed");
  }

  /*
   * @testName: ContextServiceWithMultiIntfsAndPropertiesAndInstanceIsNull
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:12
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using ExecutionProperties and multiple interfaces. if the instance
   * is null, IllegalArgumentException will be thrown
   */
  public void ContextServiceWithMultiIntfsAndPropertiesAndInstanceIsNull()
      throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");

      Map<String, String> execProps = new HashMap<String, String>();
      execProps.put("vendor_a.security.tokenexpiration", "15000");
      execProps.put("USE_PARENT_TRANSACTION", "true");

      Object proxy = cs.createContextualProxy(null, execProps, Runnable.class,
          TestWorkInterface.class);
      TestUtil.logTrace(proxy.toString());
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (IllegalArgumentException ie) {
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault(
          "ContextServiceWithMultiIntfsAndPropertiesAndInstanceIsNull failed");
  }

  /*
   * @testName: GetExecutionProperties
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:13
   * 
   * @test_Strategy: Lookup default ContextService object and create proxy
   * object using ExecutionProperties and multiple interfaces. Retrieve
   * ExecutionProperties from proxy object and verify property value.
   */
  public void GetExecutionProperties() throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");

      Map<String, String> execProps = new HashMap<String, String>();
      execProps.put("USE_PARENT_TRANSACTION", "true");

      Object proxy = cs.createContextualProxy(new TestRunnableWork(), execProps,
          Runnable.class, TestWorkInterface.class);
      Map<String, String> returnedExecProps = cs.getExecutionProperties(proxy);

      if (!"true".equals(returnedExecProps.get("USE_PARENT_TRANSACTION"))) {
        TestUtil.logErr("Expected:true, actual message="
            + returnedExecProps.get("USE_PARENT_TRANSACTION"));
      } else {
        pass = true;
      }
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("GetExecutionProperties failed");
  }

  /*
   * @testName: GetExecutionPropertiesNoProxy
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:14
   * 
   * @test_Strategy: Lookup default ContextService object. Retrieve
   * ExecutionProperties from plain object.
   */
  public void GetExecutionPropertiesNoProxy() throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");
      Map<String, String> returnedExecProps = cs
          .getExecutionProperties(new Object());
      pass = true;
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (IllegalArgumentException ie) {
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("GetExecutionPropertiesNoProxy failed");
  }
}
