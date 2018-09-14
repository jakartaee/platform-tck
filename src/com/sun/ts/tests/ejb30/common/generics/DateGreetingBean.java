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

package com.sun.ts.tests.ejb30.common.generics;

import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless()
public class DateGreetingBean implements GenericGreetingIF<java.util.Date> {

  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public java.util.Date greet(java.util.Date date) {
    return date;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.sun.ts.tests.ejb30.common.generics.GenericGreetingIF#rolesAllowed()
   */
  @RolesAllowed("Administrator")
  public Date rolesAllowed(Date d) {
    return d;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.sun.ts.tests.ejb30.common.generics.GenericGreetingIF#rolesAllowedNoArg(
   * )
   */
  @RolesAllowed("Manager")
  public Date rolesAllowedNoArg() {
    return new Date();
  }
}
