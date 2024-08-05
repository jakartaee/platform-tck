/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.el.api.jakarta_el.elprocessor;

import java.lang.reflect.Method;
import java.util.Properties;


import com.sun.ts.tests.el.common.util.ELTestUtil;

import jakarta.el.ELProcessor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());


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
   * @testName: elProcessorDefineFunctionNPETest
   * @assertion_ids: EL:JAVADOC:220; EL:JAVADOC:216; EL:JAVADOC:219
   * @test_Strategy: Assert that a NullPointerException is thrown if any of the
   *                 arguments is null.
   * 
   * @since: 3.0
   */
  @Test
  public void elProcessorDefineFunctionNPETest() throws Exception {
    ELProcessor elp = new ELProcessor();

    Method meth;
    try {
      meth = elp.getClass().getMethod("toString", new Class<?>[] {});

      // Tests for defineFunction(String, String, Method)
      logger.log(Logger.Level.INFO, 
          "Testing: ELProcessor.defineFunction(null, " + "function, meth)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, Method.class },
          new Object[] { null, "function", meth });

      logger.log(Logger.Level.INFO, 
          "Testing: ELProcessor.defineFunction(prefix, " + "null, meth)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, Method.class },
          new Object[] { "prefix", null, meth });

      logger.log(Logger.Level.INFO, 
          "Testing: ELProcessor.defineFunction(prefix, " + "function, null)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, Method.class },
          new Object[] { "prefix", "function", null });

      // Tests for defineFunction(String, String, String, String)
      logger.log(Logger.Level.INFO, "Testing: ELProcessor.defineFunction(prefix, "
          + "function, className, null)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, String.class,
              String.class },
          new Object[] { "prefix", "function", "className", null });

      logger.log(Logger.Level.INFO, "Testing: ELProcessor.defineFunction(prefix, "
          + "function, null, method)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, String.class,
              String.class },
          new Object[] { "prefix", "function", null, "method" });

      logger.log(Logger.Level.INFO, "Testing: ELProcessor.defineFunction(prefix, "
          + "null, className, method)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, String.class,
              String.class },
          new Object[] { "prefix", null, "className", "method" });

      logger.log(Logger.Level.INFO, "Testing: ELProcessor.defineFunction(null, "
          + "function, className, method)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, String.class,
              String.class },
          new Object[] { null, "function", "className", "method" });

    } catch (SecurityException e) {
      e.printStackTrace();

    } catch (NoSuchMethodException nsme) {
      nsme.printStackTrace();
    }

  } // end elProcessorDefineFunctionNPETest

  /**
   * @testName: elProcessorDefineFunctionCNFETest
   * @assertion_ids: EL:JAVADOC:220; EL:JAVADOC:214; EL:JAVADOC:217
   * @test_Strategy: Assert that a ClassNotFoundException if the specified class
   *                 does not exists.
   * 
   * @since: 3.0
   */
  @Test
  public void elProcessorDefineFunctionCNFETest() throws Exception {
    ELProcessor elp = new ELProcessor();

    logger.log(Logger.Level.INFO, "Testing: ELProcessor.defineFunction(null, "
        + "function, className, method)");
    ELTestUtil.checkForCNFE(elp, "defineFunction",
        new Class<?>[] { String.class, String.class, String.class,
            String.class },
        new Object[] { "prefix", "function", "bogus", "method" });

  } // end elProcessorDefineFunctionCNFETest

  /**
   * @testName: elProcessorDefineFunctionNSMETest
   * @assertion_ids: EL:JAVADOC:220; EL:JAVADOC:215
   * @test_Strategy: Assert that a NoSuchMethodException if the method (with or
   *                 without the signature) is not a declared method of the
   *                 class, or if the method signature is not valid.
   * 
   * @since: 3.0
   */
  @Test
  public void elProcessorDefineFunctionNSMETest() throws Exception {
    ELProcessor elp = new ELProcessor();

    logger.log(Logger.Level.INFO, "Testing: ELProcessor.defineFunction(null, "
        + "function, className, method)");
    ELTestUtil.checkForCNFE(elp, "defineFunction",
        new Class<?>[] { String.class, String.class, String.class,
            String.class },
        new Object[] { "prefix", "function", "java.util.String", "bogus" });

  } // end elProcessorDefineFunctionNSMETest
}
