/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.api.javax_el.beannameelresolver;

import java.util.Properties;

import javax.el.BeanNameELResolver;
import javax.el.BeanNameResolver;
import javax.el.ELContext;
import javax.el.ELManager;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.SimpleBean;

public class ELClient extends ServiceEETest {

  private Properties testProps;

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("Cleanup method called");
  }

  /**
   * @testName: beanNameELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:152; EL:JAVADOC:153; EL:JAVADOC:154;
   *                 EL:JAVADOC:155; EL:JAVADOC:158; EL:JAVADOC:161;
   *                 EL:JAVADOC:164
   * 
   * @test_Strategy: Verify the following method calls work as expected:
   *                 getValue() getType() setValue() isReadOnly()
   *                 getCommonPropertyType() getFeatureDescriptors()
   */
  public void beanNameELResolverTest() throws Fault {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    elm.addBeanNameResolver(bnr);
    elm.addELResolver(belr);
    elm.defineBean("simpleBean", new SimpleBean());

    ELContext context = elm.getELContext();

    try {
      pass = ResolverTest.testELResolver(context, context.getELResolver(),
          bnr.getBean("simpleBean"), "intention", "GOLDEN", buf, false);

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }

    TestUtil.logMsg(buf.toString());
  }

  /**
   * @testName: beanNameELResolverInvokeMNFETest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16;
   *                 EL:JAVADOC:143; EL:JAVADOC:237
   * 
   * @test_Strategy: Verify that the invoke() method throws
   *                 MethodNotFoundException if no suitable method can be found.
   */
  public void beanNameELResolverInvokeMNFETest() throws Fault {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    elm.addBeanNameResolver(bnr);
    elm.addELResolver(belr);
    elm.defineBean("simpleBean", new SimpleBean());

    ELContext context = elm.getELContext();

    Class<?>[] types = { String.class, String.class };
    String[] values = { "Doug", "Donahue" };

    try {
      pass = ResolverTest.testELResolverInvoke(context, context.getELResolver(),
          new SimpleBean(), "bogue_method", types, values, true, buf);

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logMsg(buf.toString());

  }// End beanELResolverInvokeMNFETest

  /**
   * @testName: beanNameELResolverInvokeTest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16;
   *                 EL:JAVADOC:142; EL:JAVADOC:199; EL:JAVADOC:200;
   *                 EL:JAVADOC:202; EL:JAVADOC:203; EL:JAVADOC:236
   * 
   * @test_Strategy: Verify that API calls work as expected for
   *                 BeanNameELResolver.invoke().
   */
  public void beanNameELResolverInvokeTest() throws Fault {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    elm.addBeanNameResolver(bnr);
    elm.addELResolver(belr);
    elm.defineBean("simpleBean", new SimpleBean());

    ELContext context = elm.getELContext();

    Class<?>[] types = { String.class, String.class };
    String[] values = { "Doug", "Donahue" };

    try {
      pass = ResolverTest.testELResolverInvoke(context, context.getELResolver(),
          new SimpleBean(), "isName", types, values, false, buf);

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logMsg(buf.toString());

  }// End beanELResolverInvokeTest

  /**
   * @testName: beanNameELResolverGetTypeNPETest
   * 
   * @assertion_ids: EL:JAVADOC:156
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  public void beanNameELResolverGetTypeNPETest() throws Fault {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "getType",
        new Class<?>[] { ELContext.class, Object.class, Object.class },
        new Object[] { null, "Ender", "Wiggins" });

  }// End beanNameELResolverGetTypeNPETest

  /**
   * @testName: beanNameELResolverGetValueNPETest
   * 
   * @assertion_ids: EL:JAVADOC:159
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  public void beanNameELResolverGetValueNPETest() throws Fault {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "getValue",
        new Class<?>[] { ELContext.class, Object.class, Object.class },
        new Object[] { null, "Ender", "Wiggins" });

  }// End beanNameELResolverGetValueNPETest

  /**
   * @testName: beanNameELResolverIsReadOnlyNPETest
   * 
   * @assertion_ids: EL:JAVADOC:162
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  public void beanNameELResolverIsReadOnlyNPETest() throws Fault {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "isReadOnly",
        new Class<?>[] { ELContext.class, Object.class, Object.class },
        new Object[] { null, "Ender", "Wiggins" });

  }// End beanNameELResolverIsReadOnlyNPETest

  /**
   * @testName: beanNameELResolverSetValueNPETest
   * 
   * @assertion_ids: EL:JAVADOC:165
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  public void beanNameELResolverSetValueNPETest() throws Fault {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "setValue",
        new Class<?>[] { ELContext.class, Object.class, Object.class,
            Object.class },
        new Object[] { null, "Ender", "Valintine", "Wiggins" });

  }// End beanNameELResolverSetValueNPETest

  // -------------------------------------------- private classes

  private static class TCKBeanNameResolver extends BeanNameResolver {

  }
}
