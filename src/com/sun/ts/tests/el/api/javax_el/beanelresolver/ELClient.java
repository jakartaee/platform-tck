/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.api.javax_el.beanelresolver;

import java.util.Properties;

import javax.el.BeanELResolver;
import javax.el.ELContext;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.SimpleBean;

public class ELClient extends ServiceEETest {

  private SimpleBean sb;

  private Properties testProps;

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
    sb = new SimpleBean();
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("Cleanup method called");
  }

  /**
   * @testName: beanELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16
   * 
   * @test_Strategy: Verify that API calls work as expected: beanELResolver()
   *                 getValue() getType() setValue() isReadOnly()
   *                 getCommonPropertyType() getFeatureDescriptors()
   */
  public void beanELResolverTest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();
    sb.setIntention("initial_setting");

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, beanResolver, sb, "intention",
          "ABC123", buf, false);
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: beanELResolverInvokeTest
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16; EL:JAVADOC:142
   * @test_Strategy: Verify that API calls work as expected for
   *                 BeanELResolver.invoke().
   */
  public void beanELResolverInvokeTest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      Class<?>[] types = { String.class, String.class };
      String[] values = { "Doug", "Donahue" };

      pass = ResolverTest.testELResolverInvoke(context, beanResolver, sb,
          "isName", types, values, false, buf);
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());

  }// End beanELResolverInvokeTest

  /**
   * @testName: beanELResolverInvokeVoidTest
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16
   * @test_Strategy: Verify that the ELResolver.invoke() API calls work as
   *                 expected when calling a method that returns void.
   */
  public void beanELResolverInvokeVoidTest() throws Fault {

    boolean pass;
    StringBuffer buf = new StringBuffer();

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      Class<?>[] types = { String.class, String.class };
      String[] values = { "Peter", "Pan" };

      Object result = beanResolver.invoke(context, sb, "setFullName", types,
          values);

      if (null == result) {
        // validate the new values.
        pass = ResolverTest.testELResolverInvoke(context, beanResolver, sb,
            "isName", types, values, false, buf);
      } else {
        pass = false;
        buf.append("Unexpected Value returned!" + TestUtil.NEW_LINE
            + "Expected: null" + TestUtil.NEW_LINE + "Recieved: "
            + result.getClass().getName());
      }

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());

  }// End beanELResolverInvokeVoidTest

  /**
   * @testName: beanELResolverInvokeMNFETest
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16; EL:JAVADOC:143
   * @test_Strategy: Verify that the invoke() method throws
   *                 MethodNotFoundException if no suitable method can be found.
   */
  public void beanELResolverInvokeMNFETest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      Class<?>[] types = { String.class, String.class };
      String[] values = { "Doug", "Donahue" };

      pass = ResolverTest.testELResolverInvoke(context, beanResolver, sb,
          "bogus_Method", types, values, true, buf);

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());

  }// End beanELResolverInvokeMNFETest

  /**
   * @testName: beanELResolverNPETest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:13; EL:JAVADOC:14; EL:JAVADOC:15;
   *                 EL:JAVADOC:16; EL:JAVADOC:136; EL:JAVADOC:139;
   *                 EL:JAVADOC:145; EL:JAVADOC:148
   * 
   * @test_Strategy: Verify that the following methods throw a
   *                 NullPointerException, if context is null:
   * 
   *                 getType() getValue() isReadOnly() setValue()
   */
  public void beanELResolverNPETest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();
    sb.setIntention("initial_setting");

    BeanELResolver resolver = new BeanELResolver();

    try {
      pass = ResolverTest.testELResolverNPE(resolver, sb, "intention", "billy",
          buf);
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: beanELResolverPNFETest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:13; EL:JAVADOC:14; EL:JAVADOC:15;
   *                 EL:JAVADOC:16; EL:JAVADOC:137; EL:JAVADOC:140;
   *                 EL:JAVADOC:146; EL:JAVADOC:149
   * 
   * @test_Strategy: Verify that the following methods throw a
   *                 PropertyNotFoundException, if the base is not null and the
   *                 specified property does not exist.
   * 
   *                 getType() isReadOnly() setValue() getValue()
   */
  public void beanELResolverPNFETest() throws Fault {

    boolean pass = true;

    StringBuffer buf = new StringBuffer();
    sb.setIntention("initial_setting");

    BeanELResolver resolver = new BeanELResolver();
    BareBonesELContext barebonesComtext = new BareBonesELContext();
    ELContext context = barebonesComtext.getELContext();

    try {
      pass = ResolverTest.testELResolverPNFE(context, resolver, sb,
          "Bogus_Field", buf);
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: beanELResolverPNWETest
   * 
   * @assertion_ids: EL:JAVADOC:10; EL:JAVADOC:16; EL:JAVADOC:150
   * 
   * @test_Strategy: Verify that the single-parameter constructor for
   *                 beanELResolver can be used to construct a read-only
   *                 resolver, and that the setValue() method throws a
   *                 PropertyNotWritableException, if the resolver was
   *                 constructed in read-only mode.
   * 
   *                 BeanELResolver(boolean) setValue()
   */
  public void beanELResolverPNWETest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    BeanELResolver resolver = new BeanELResolver(true);
    BareBonesELContext barebonesComtext = new BareBonesELContext();
    ELContext context = barebonesComtext.getELContext();

    try {
      pass = ResolverTest.testELResolverPNWE(context, resolver, sb, "intention",
          "billy", buf);
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }
}
