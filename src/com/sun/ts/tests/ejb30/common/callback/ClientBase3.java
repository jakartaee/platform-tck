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

/**
 * the direct superclass of Client classes that test application client
 * PostConstruct and injection-complete, e.g.,
 * stateless/callback/method/annotated/Client.
 */
abstract public class ClientBase3 extends ClientBase2 {
  @Resource
  private static ORB orbInClientBase3;

  public ClientBase3() {
  }

  @PostConstruct
  private static void postConstructInBase3() {
    addPostConstructCall(BASE3);
    // check injected fields
    if (orbInClientBase3 != null) {
      addInjectedField(orbInClientBase3);
    } else {
      TLogger.log("WARNING: ClientBase3.orbInClientBase3 has not been "
          + "initialized when checking inside ClientBase3.postConstructInBase3()");
    }
  }

}
