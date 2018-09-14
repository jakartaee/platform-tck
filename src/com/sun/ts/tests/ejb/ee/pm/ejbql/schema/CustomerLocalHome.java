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

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import javax.ejb.*;
import java.util.*;

public interface CustomerLocalHome extends EJBLocalHome {
  public CustomerLocal create(String id, String name, AddressDVC home,
      AddressDVC work, Country country) throws CreateException;

  public CustomerLocal findByPrimaryKey(String key) throws FinderException;

  public CustomerLocal findCustomerByHomePhoneNumber(String phone)
      throws FinderException;

  public Collection findCustomersByWorkCity(String city) throws FinderException;

  public CustomerLocal findCustomerByQuery29(String street, String city,
      String state, String zip) throws FinderException;

  public Collection findCustomersByQuery32(String city) throws FinderException;
}
