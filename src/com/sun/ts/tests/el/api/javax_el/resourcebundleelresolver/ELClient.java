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

package com.sun.ts.tests.el.api.javax_el.resourcebundleelresolver;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.el.ELContext;
import javax.el.ResourceBundleELResolver;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;

public class ELClient extends ServiceEETest {

  private Properties testProps;

  private TckResourceBundle tckrb;

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
    tckrb = new TckResourceBundle();
  }

  public void cleanup() throws Fault {
  }

  /**
   * @testName: resourceBundleELResolverTest
   *
   * @assertion_ids: EL:JAVADOC:103; EL:JAVADOC:104; EL:JAVADOC:105;
   *                 EL:JAVADOC:106; EL:JAVADOC:107; EL:JAVADOC:108;
   *                 EL:JAVADOC:109
   *
   * @test_Strategy: Verify that API calls work as expected:
   *                 ResourceBundleELResolver() setValue() getValue() getType()
   *                 isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors()
   */

  public void resourceBundleELResolverTest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      ResourceBundleELResolver rsbResolver = new ResourceBundleELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, rsbResolver, tckrb, "Dinner",
          "DINNER", buf, true);
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: resourceBundleELResolverNPETest
   *
   * @assertion_ids: EL:JAVADOC:106; EL:JAVADOC:105; EL:JAVADOC:107;
   *                 EL:JAVADOC:108; EL:JAVADOC:109; EL:JAVADOC:313;
   *                 EL:JAVADOC:314; EL:JAVADOC:316; EL:JAVADOC:317
   *
   * @test_Strategy: Verify that the following methods throw a
   *                 NullPointerException, if context is null:
   *
   *                 getValue() getType() setValue() isReadOnly()
   */
  public void resourceBundleELResolverNPETest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    ResourceBundleELResolver resolver = new ResourceBundleELResolver();

    try {
      pass = ResolverTest.testELResolverNPE(resolver, tckrb, "brunch", "BRUNCH",
          buf);
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: resourceBundleELResolverPNWETest
   *
   * @assertion_ids: EL:JAVADOC:108; EL:JAVADOC:109; EL:JAVADOC:318
   *
   * @test_Strategy: Verify that the following methods throw a
   *                 PropertyNotWritableException, since
   *                 ResourceBundleELResolvers are non-writable.
   *
   *                 setValue()
   */
  public void resourceBundleELResolverPNWETest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    BareBonesELContext barebonesContext = new BareBonesELContext();
    ELContext context = barebonesContext.getELContext();
    ResourceBundleELResolver rsbResolver = new ResourceBundleELResolver();

    try {
      pass = ResolverTest.testELResolverPNWE(context, rsbResolver, tckrb,
          "snack", "SNACK", buf);
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  private static class TckResourceBundle extends ResourceBundle
      implements Serializable {

    private static final String KEYS = "Breakfast Lunch Dinner";

    public Object handleGetObject(String key) {
      if ("Breakfast".equals(key))
        return "BREAKFAST";
      if ("Lunch".equals(key))
        return "LUNCH";
      if ("Dinner".equals(key))
        return "DINNER";

      return null;
    }

    public Enumeration getKeys() {
      StringTokenizer keyTokenizer = new StringTokenizer(KEYS);

      return keyTokenizer;

    }
  }
}
