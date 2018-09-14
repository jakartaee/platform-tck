/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.deliverable;

/**
 * This is a factory class for creating instances of TSDeploymentInterface. The
 * implementation classes used are determined by the values of the porting
 * package properties in TS_HOME/bin/ts.jte.
 *
 * @author Kyle Grucci
 */

public class DeliverableFactory {
  private static DeliverableInterface di;

  public static DeliverableInterface getDeliverableInstance() throws Exception {
    if (di == null)
      di = createInstance();
    return di;
  }

  public static DeliverableInterface getDeliverableInstance(
      ClassLoader classLoader) throws Exception {
    if (di == null)
      di = createInstance(classLoader);
    return di;
  }

  private static DeliverableInterface createInstance() throws Exception {
    return createInstance(null);
  }

  private static DeliverableInterface createInstance(ClassLoader classLoader)
      throws Exception {
    try {
      // get property value from within the ts specific properties file
      String sClassName = System.getProperty("deliverable.class");
      // create and initialize a new instance of the Deployment class
      Class c = null;
      if (classLoader == null) {
        c = Class.forName(sClassName);
      } else {
        c = Class.forName(sClassName, true, classLoader);
      }
      return (DeliverableInterface) c.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
