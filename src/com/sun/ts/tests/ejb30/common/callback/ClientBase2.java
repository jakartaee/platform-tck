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

package com.sun.ts.tests.ejb30.common.callback;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.omg.CORBA.ORB;

abstract public class ClientBase2 extends ClientBase2NoAnnotation {
  @Resource
  private static ORB orbInClientBase2;

  public ClientBase2() {
  }

  /**
   * When this method is invoked, all injections, including those in this class,
   * and all of its subclasses, should have been completed.
   */
  @PostConstruct
  private static void postConstructInBase2() {
    addPostConstructCall(BASE2);
    // check injected fields
    if (orbInClientBase2 != null) {
      addInjectedField(orbInClientBase2);
    } else {
      TLogger.log("WARNING: ClientBase2.orbInClientBase2 has not been "
          + "initialized when checking inside ClientBase2.postConstructInBase2()");
    }
  }
}
