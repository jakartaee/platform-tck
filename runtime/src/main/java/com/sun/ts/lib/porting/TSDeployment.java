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

import java.io.*;
import com.sun.ts.lib.deliverable.*;

/**
 * This is a factory class for creating instances of TSDeploymentInterface. The
 * implementation classes used are determined by the values of the porting
 * package properties in TS_HOME/bin/ts.jte.
 * 
 * @author Kyle Grucci
 */
public class TSDeployment {
  private static PropertyManagerInterface propMgr = null;

  public static int iPortingSet = 1;

  public static TSDeploymentInterface getDeploymentInstance(PrintWriter writer,
      String sClassName) throws Exception {
    return createInstance(sClassName, writer);
  }

  private static TSDeploymentInterface createInstance(String sClassName,
      PrintWriter writer) throws Exception {
    try {
      propMgr = DeliverableFactory.getDeliverableInstance()
          .getPropertyManager();

      // create and initialize a new instance of the Deployment class
      Class c = Class.forName(propMgr.getProperty(sClassName));
      TSDeploymentInterface ctsDep1 = (TSDeploymentInterface) c.newInstance();

      // set static prop so porting impls in the same VM can look it up
      iPortingSet = Integer
          .parseInt(sClassName.substring(sClassName.lastIndexOf(".") + 1));

      // tell this 88 class which porting set of props we are using
      // (1 or 2)
      // if(ctsDep1 instanceof
      // com.sun.ts.lib.deliverable.cts.deploy.StandardDeployment14)
      // {
      // ((com.sun.ts.lib.deliverable.cts.deploy.StandardDeployment14)ctsDep1).setFirstLevelPortingSet(Integer.parseInt(sClassName.substring(sClassName.lastIndexOf(".")+1)));
      // }

      ctsDep1.init(writer);

      return ctsDep1;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
