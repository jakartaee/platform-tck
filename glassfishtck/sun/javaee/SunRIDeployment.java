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

package com.sun.ts.lib.implementation.sun.javaee;

import java.util.*;
import java.io.*;
import java.net.*;
import com.sun.javatest.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.deliverable.*;

/**
 * This is an implementation of the TSDeploymentInterface. An implementation of
 * this class must be supplied by any Java EE implementation wishing to have
 * JavaTest (the harness which drives the TS tests) automatically deploy and
 * undeploy test applications. Providing this functionality enables the entire
 * test suite to be run without having to manually deploy/undeploy the Java EE
 * test applications prior to running the tests. This particular implementation
 * uses the deploy/undeploy semantics of Sun's Java EE reference implementation.
 * 
 * @author Kyle Grucci
 */
public class SunRIDeployment extends SunRIDeployment14
    implements TSDeploymentInterface {
  /**
   * Initializes a new TSDeployment instance. All output should be printed to
   * this PrintWriter. All properties in the ts.jte file are accessible to this
   * porting implementation class only via the PropertyManagerInterface. Please
   * see Sun's implementation of this method (below) for an example.
   *
   * @param writer
   *          The PrintWriter that should be used to log output.
   */
  public void init(PrintWriter out) {
    try {
      System.out.println("SunRIDeployment:  in init");
      PropertyManagerInterface propMgr = DeliverableFactory
          .getDeliverableInstance().getPropertyManager();
      init(out, "", propMgr);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
