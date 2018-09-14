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
package com.sun.ts.tests.ejb30.tx.session.stateless.cm.varargs;

import com.sun.ts.lib.deliverable.cts.resource.Dog;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
public class VarargsBean implements VarargsLocalIF, VarargsRemoteIF {

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public String listDogs(Dog... dogs) {
    String result = "";
    for (Dog dog : dogs) {
      result += dog.toString();
    }
    return result;
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public String format(String s, Object... o) {
    String result = s;
    for (Object item : o) {
      result += item;
    }
    return result;
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public int add(int... i) {
    int result = 0;
    for (int j : i) {
      result += j;
    }
    return result;
  }
}
