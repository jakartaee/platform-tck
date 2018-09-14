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

package com.sun.ts.tests.el.api.javax_el.compositeelresolver;

import java.util.Properties;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELManager;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.SimpleBean;

public class ELClient extends ServiceEETest {

  private static final String[] names = { "doug", "nick", "roger", "ryan",
      "ed" };

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
    // does nothing at this point
  }

  /**
   * @testName: compositeELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:24; EL:JAVADOC:25; EL:JAVADOC:26; EL:JAVADOC:27;
   *                 EL:JAVADOC:28; EL:JAVADOC:29; EL:JAVADOC:30; EL:JAVADOC:31;
   *                 EL:JAVADOC:34
   * 
   * @test_Strategy: Verify that API calls work as expected:
   *                 CompositeELResolver() add() getValue() getType() setValue()
   *                 isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors() ELContext.getELResolver()
   */
  public void compositeELResolverTest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();

      // Create the CompositeELResolver.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();

      pass = ResolverTest.testCompositeELResolver(context, compResolver, buf);

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: compositeELResolverInvokeTest
   * 
   * @assertion_ids: EL:JAVADOC:24; EL:JAVADOC:25; EL:JAVADOC:181
   * 
   * @test_Strategy: Verify that API calls work as expected for
   *                 BeanELResolver.invoke().
   */
  public void compositeELResolverInvokeTest() throws Fault {
    SimpleBean sb = new SimpleBean();
    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      ELManager elm = new ELManager();
      ELContext context = elm.getELContext();

      // Create the CompositeELResolver.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();

      compResolver.add(beanResolver);

      Class<?>[] types = { String.class, String.class };
      String[] values = { "Doug", "Donahue" };

      pass = ResolverTest.testELResolverInvoke(context, compResolver, sb,
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
   * @testName: compositeELResolverAddNPETest
   * 
   * @assertion_ids: EL:JAVADOC:24; EL:JAVADOC:25; EL:JAVADOC:174
   * 
   * @test_Strategy: Verify that the CompositeELResolver.add method throws a
   *                 NullPointerException if the provided resolver is null.
   */
  public void compositeELResolverAddNPETest() throws Fault {

    boolean pass = true;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();

      compResolver.add(null);

      buf.append(TestUtil.NEW_LINE + "Tested Failed, CompositeResolver.add(), "
          + "should have thrown NullPointerException!");
      pass = false;

    } catch (NullPointerException npe) {
      TestUtil.logMsg("Expected Exception thrown, when providing a"
          + " null resolver to Composite.add()");

    } catch (Exception e) {
      buf.append("test failed with: " + TestUtil.NEW_LINE + "EXPECTED: "
          + "NullPointerException to be thrown " + TestUtil.NEW_LINE
          + "RECEIVED: " + e.toString() + "" + TestUtil.NEW_LINE);
      pass = false;
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: compositeELResolverNPETest
   * 
   * @assertion_ids: EL:JAVADOC:25; EL:JAVADOC:26; EL:JAVADOC:27; EL:JAVADOC:28;
   *                 EL:JAVADOC:29; EL:JAVADOC:30; EL:JAVADOC:31;
   *                 EL:JAVADOC:175; EL:JAVADOC:178; EL:JAVADOC:175;
   *                 EL:JAVADOC:182; EL:JAVADOC:185
   * 
   * @test_Strategy: Verify that API calls work as expected: getValue()
   *                 getType() setValue() isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors()
   */
  public void compositeELResolverNPETest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();

      // Create dummy ELResolvers to populate the CompositeELResolver.
      ArrayELResolver aResolver = new ArrayELResolver();
      BeanELResolver bResolver = new BeanELResolver();

      // Create the CompositeELResolver & add the dummy ELResolvers to it.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();
      compResolver.add(aResolver);
      compResolver.add(bResolver);

      pass = ResolverTest.testELResolverNPE(compResolver, names,
          Integer.valueOf(2), "steve", buf);

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: compositeELResolverPNFETest
   * 
   * @assertion_ids: EL:JAVADOC:25; EL:JAVADOC:28; EL:JAVADOC:30; EL:JAVADOC:31;
   *                 EL:JAVADOC:176; EL:JAVADOC:179; EL:JAVADOC:183;
   *                 EL:JAVADOC:186
   * 
   * @test_Strategy: Verify that API calls throw PropertyNotFoundException as
   *                 expected: getType() setValue() isReadOnly()
   */
  public void compositeELResolverPNFETest() throws Fault {

    boolean pass = true;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();

      // Create an ArrayELResolver to populate the CompositeELResolver.
      ArrayELResolver aResolver = new ArrayELResolver();

      // Create the CompositeELResolver & add the ELResolver to it.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();
      compResolver.add(aResolver);

      pass = ResolverTest.testELResolverPNFE(context, compResolver, names,
          Integer.valueOf(10), buf);

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: compositeELResolverPNWETest
   * 
   * @assertion_ids: EL:JAVADOC:25; EL:JAVADOC:187
   * 
   * @test_Strategy: Verify that the setValue() method throws a
   *                 PropertyNotWritableException, if the resolver was
   *                 constructed in read-only mode.
   * 
   *                 setValue()
   */
  public void compositeELResolverPNWETest() throws Fault {
    boolean pass = false;
    StringBuffer buf = new StringBuffer();
    SimpleBean sb = new SimpleBean();

    BareBonesELContext compContext = new BareBonesELContext();
    ELContext context = compContext.getELContext();

    // Create an ArrayELResolver to populate the CompositeELResolver.
    BeanELResolver resolver = new BeanELResolver(true);

    // Create the CompositeELResolver & add the ELResolver to it.
    CompositeELResolver compResolver = (CompositeELResolver) context
        .getELResolver();
    compResolver.add(resolver);

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
