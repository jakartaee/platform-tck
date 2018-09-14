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

package com.sun.ts.tests.el.api.javax_el.mapelresolver;

import java.util.HashMap;
import java.util.Properties;

import javax.el.ELContext;
import javax.el.MapELResolver;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;

public class ELClient extends ServiceEETest {

  private Properties testProps;

  private HashMap<String, String> animals;

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
    animals = new HashMap<String, String>();
    animals.put("dog", "retriever");
    animals.put("fish", "bass");
    animals.put("bird", "parrot");
  }

  public void cleanup() throws Fault {
    // does nothing at this point
  }

  /**
   * @testName: mapELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:76; EL:JAVADOC:77; EL:JAVADOC:78; EL:JAVADOC:79;
   *                 EL:JAVADOC:80; EL:JAVADOC:81; EL:JAVADOC:83
   * @test_Strategy: Verify that API calls work as expected: MapELResolver()
   *                 getValue() getType() setValue() isReadOnly()
   *                 getCommonPropertyType() getFeatureDescriptors()
   */

  public void mapELResolverTest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      MapELResolver mapResolver = new MapELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, mapResolver, animals, "dog",
          "labador", buf, false);
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: mapELResolverNPETest
   * 
   * @assertion_ids: EL:JAVADOC:78; EL:JAVADOC:79; EL:JAVADOC:80; EL:JAVADOC:81;
   *                 EL:JAVADOC:83; EL:JAVADOC:293; EL:JAVADOC:295;
   *                 EL:JAVADOC:298; EL:JAVADOC:290
   * 
   * @test_Strategy: Verify that the following methods throw a
   *                 NullPointerException, if context is null:
   * 
   *                 getType() getValue() isReadOnly() setValue()
   */
  public void mapELResolverNPETest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    MapELResolver resolver = new MapELResolver();

    try {
      pass = ResolverTest.testELResolverNPE(resolver, animals, "dog", "billy",
          buf);
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: mapELResolverPNWETest
   * 
   * @assertion_ids: EL:JAVADOC:82; EL:JAVADOC:83; EL:JAVADOC:301
   * 
   * @test_Strategy: Verify that the single-parameter constructor for
   *                 mapELResolver can be used to construct a read-only
   *                 resolver, and that the setValue() method throws a
   *                 PropertyNotWritableException, if the resolver was
   *                 constructed in read-only mode.
   * 
   *                 MapELResolver(boolean) setValue()
   */
  public void mapELResolverPNWETest() throws Fault {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    MapELResolver resolver = new MapELResolver(true);
    BareBonesELContext barebonesComtext = new BareBonesELContext();
    ELContext context = barebonesComtext.getELContext();

    try {
      pass = ResolverTest.testELResolverPNWE(context, resolver, animals, "dog",
          "billy", buf);
    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

}
