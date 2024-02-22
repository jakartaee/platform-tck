/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.el.api.jakarta_el.mapelresolver;

import java.util.HashMap;
import java.util.Properties;


import com.sun.ts.tests.el.common.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;

import jakarta.el.ELContext;
import jakarta.el.MapELResolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  private HashMap<String, String> animals;

  public ELClientIT(){
    animals = new HashMap<String, String>();
    animals.put("dog", "retriever");
    animals.put("fish", "bass");
    animals.put("bird", "parrot");
  }

  
  @AfterEach
  public void cleanup() throws Exception {
    logger.log(Logger.Level.INFO, "Cleanup method called");
  }

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
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
  @Test
  public void mapELResolverTest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      MapELResolver mapResolver = new MapELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, mapResolver, animals, "dog",
          "labador", buf, false);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());
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
  @Test
  public void mapELResolverNPETest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    MapELResolver resolver = new MapELResolver();

    try {
      pass = ResolverTest.testELResolverNPE(resolver, animals, "dog", "billy",
          buf);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());
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
  @Test
  public void mapELResolverPNWETest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    MapELResolver resolver = new MapELResolver(true);
    BareBonesELContext barebonesComtext = new BareBonesELContext();
    ELContext context = barebonesComtext.getELContext();

    try {
      pass = ResolverTest.testELResolverPNWE(context, resolver, animals, "dog",
          "billy", buf);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());
  }

}
