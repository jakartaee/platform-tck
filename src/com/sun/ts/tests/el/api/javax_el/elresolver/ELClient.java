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

package com.sun.ts.tests.el.api.javax_el.elresolver;

import java.util.Properties;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELManager;
import javax.el.ELResolver;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BarELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.SimpleBean;

public class ELClient extends ServiceEETest {

  private Properties testProps;

  private static final String[] names = { "doug", "nick", "roger", "ryan",
      "ed" };

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
   * @testName: elResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:50; EL:JAVADOC:51; EL:JAVADOC:52; EL:JAVADOC:53;
   *                 EL:JAVADOC:54; EL:JAVADOC:55; EL:JAVADOC:229
   * 
   * @test_Strategy: Create an ELContext and get its ELResolver. Verify that API
   *                 calls work as expected: getValue() getType() setValue()
   *                 isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors()
   */

  public void elResolverTest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BarELContext barContext = new BarELContext();
      ELContext context = barContext.getELContext();

      if (barContext != null) {
        ELResolver resolver = barContext.getELResolver();
        pass = ResolverTest.testELResolver(context, resolver, null, "Bar",
            "Foo", buf, true);
      }
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());

  } // end elResolverTest

  /**
   * @testName: elResolverNPETest
   * 
   * @assertion_ids: EL:JAVADOC:233; EL:JAVADOC:239; EL:JAVADOC:242
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown as expected
   *                 for the following methods: getValue() getType() setValue()
   *                 isReadOnly()
   * 
   * @since: 3.0
   */
  public void elResolverNPETest() throws Fault {
    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    ELContext context = new ELManager().getELContext();

    // Create dummy ELResolvers to populate the CompositeELResolver.
    ELResolver aResolver = new ArrayELResolver();
    ELResolver bResolver = new BeanELResolver();

    // Create the CompositeELResolver & add the dummy ELResolvers to it.
    CompositeELResolver compResolver = (CompositeELResolver) context
        .getELResolver();
    compResolver.add(aResolver);
    compResolver.add(bResolver);

    try {
      pass = ResolverTest.testELResolverNPE(compResolver, names,
          Integer.valueOf(2), "steve", buf);

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());

  }// end elResolverNPETest

  /**
   * @testName: elResolverPNFETest
   * 
   * @assertion_ids: EL:JAVADOC:231; EL:JAVADOC:234; EL:JAVADOC:240
   * 
   * @test_Strategy: Verify that API calls throw PropertyNotFoundException as
   *                 expected: getType() setValue() isReadOnly()
   */
  public void elResolverPNFETest() throws Fault {
    boolean pass = true;
    StringBuffer buf = new StringBuffer();

    ELContext context = new ELManager().getELContext();

    // Create an ArrayELResolver to populate the CompositeELResolver.
    ELResolver aResolver = new ArrayELResolver();

    // Create the CompositeELResolver & add the ELResolver to it.
    CompositeELResolver compResolver = (CompositeELResolver) context
        .getELResolver();
    compResolver.add(aResolver);

    try {
      pass = ResolverTest.testELResolverPNFE(context, compResolver, names,
          Integer.valueOf(10), buf);

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());

  }// end elResolverPNFETest

  /**
   * @testName: elResolverPNWETest
   * 
   * @assertion_ids: EL:JAVADOC:50; EL:JAVADOC:244
   * 
   * @test_Strategy: Verify that the setValue() method throws a
   *                 PropertyNotWritableException, if the resolver was
   *                 constructed in read-only mode.
   * 
   *                 setValue()
   */
  public void elResolverPNWETest() throws Fault {
    boolean pass = false;
    StringBuffer buf = new StringBuffer();
    SimpleBean sb = new SimpleBean();

    ELContext context = new ELManager().getELContext();

    ELResolver resolver = new BeanELResolver(true);
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

  } // end elResolverPNWETest

}
