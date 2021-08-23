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

/*
 * $Id$
 */

package com.sun.ts.lib.porting;

import com.sun.ts.lib.util.*;

/**
 * This is a factory class for creating instances of TSJMSObjectsInterface. The
 * implementation classes used are determined by the values of the porting
 * package properties in TS_HOME/bin/ts.jte.
 *
 */
public class TSJMSObjects {

  private static final String DEFAULT_PORTING_CLASS = "com.sun.ts.lib.implementation.sun.jms.SunRIJMSObjects";

  private static String portingPropName = "porting.ts.jmsObjects.class.1";

  private static String portingClass = null;

  private static TSJMSObjectsInterface tsJmsObjects = null;

  public static TSJMSObjectsInterface getJMSObjectsInstance() throws Exception {
    try {
      // Create instance of the TSJMSObjectsInterface implementation
      // class
      TestUtil.logMsg("TSJMSObjects.getJMSObjectsInstance()");
      portingClass = TestUtil.getProperty(portingPropName);
      if (portingClass == null) {
        portingClass = DEFAULT_PORTING_CLASS;
        TestUtil.logMsg("Property " + portingPropName + " not set. "
            + "Using default porting class. ");
      }
      TestUtil.logMsg("Porting implementation class=" + portingClass);
      if (tsJmsObjects == null) {
        Class c = Class.forName(portingClass);
        tsJmsObjects = (TSJMSObjectsInterface) c.newInstance();
      }
      return tsJmsObjects;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
