/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxws.common;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class used to enforce accessibility of a module when using Reflection
 * API.
 *
 * @author Miroslav Kos (miroslav.kos at oracle.com)
 */
public class Modules {

  private static final Logger logger = Logger
      .getLogger(Modules.class.getName());

  private static Method GET_MODULE;

  private static Method ADD_READS;

  private static Method GET_NAME;

  private static Method CAN_READ;

  private static boolean initializationFailed;

  static {
    try {
      Class<?> moduleClass = Class.forName("java.lang.reflect.Module");
      GET_MODULE = Class.class.getMethod("getModule");
      GET_NAME = moduleClass.getMethod("getName");
      ADD_READS = moduleClass.getMethod("addReads", moduleClass);
      CAN_READ = moduleClass.getMethod("canRead", moduleClass);
    } catch (Throwable t) {
      logger.log(Level.INFO,
          "Error during initialization of [{0}]. Probably not Jigsaw runtime.",
          Modules.class.getName());
      // this code needs to run on older JDKs too
      // so this can be correct
      initializationFailed = true;
    }
  }

  /**
   * This method uses jdk9 specific API. For all the JDKs <= 8 empty.
   *
   * @param sourceClass
   *          class (current module) usinf Core Reflection API
   * @param targetClass
   *          class to be accessed via Core Reflection
   */
  public static void ensureReadable(Class<?> sourceClass,
      Class<?> targetClass) {
    if (initializationFailed)
      return;
    Object targetModule = getModule(targetClass);
    Object sourceModule = getModule(sourceClass);
    if (!canRead(sourceModule, targetModule)) {
      logger.log(Level.FINE, "Adding module [{0}] to module [{1}]'s reads",
          new Object[] { getName(targetModule), getName(sourceModule) });
      addReads(sourceModule, targetModule);
    }
  }

  private static void addReads(Object sourceModule, Object targetModule) {
    if (sourceModule == null)
      return;
    try {
      ADD_READS.invoke(sourceModule, targetModule);
    } catch (Exception e) {
      throw new InternalError(e.getMessage());
    }
  }

  private static Object getModule(Class<?> targetClass) {
    if (targetClass == null)
      return null;
    try {
      return GET_MODULE.invoke(targetClass);
    } catch (Exception e) {
      throw new InternalError(e.getMessage());
    }
  }

  private static Object getName(Object module) {
    if (module == null)
      return "<anonymous-module>";
    try {
      return GET_NAME.invoke(module);
    } catch (Exception e) {
      throw new InternalError(e.getMessage());
    }
  }

  private static boolean canRead(Object sourceModule, Object targetModule) {
    if (sourceModule == null)
      return true;
    try {
      return (Boolean) CAN_READ.invoke(sourceModule, targetModule);
    } catch (Exception e) {
      throw new InternalError(e.getMessage());
    }
  }

}
