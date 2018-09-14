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

package com.sun.ts.tests.ejb30.common.annotation.resource;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.USER_TRANSACTION_JNDI_NAME;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

abstract public class UserTransactionNegativeBeanBase
    implements UserTransactionNegativeIF {

  /////////////////////////////////////////////////////////////////////////
  // business methods
  /////////////////////////////////////////////////////////////////////////
  public void testUserTransactionNegativeLookup() throws TestFailedException {
    // See glassfish issue 1538, EJB 3.0 spec page 448
    try {
      Object obj = ServiceLocator.lookup(USER_TRANSACTION_JNDI_NAME);
      if (obj != null) {
        throw new TestFailedException(
            "lookup of UserTransaction must not succeed for CMT beans.  Unexpectedly returned "
                + obj);
      }
    } catch (Exception e) {
      // should throw NameNotFoundException, but also ok for other exceptions.
    }
  }

}
