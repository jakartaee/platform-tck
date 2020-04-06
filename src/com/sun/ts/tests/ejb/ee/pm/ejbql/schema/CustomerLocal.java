/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.ts.lib.util.*;

import java.util.*;
import jakarta.ejb.*;

public interface CustomerLocal extends EJBLocalObject {
  // Business Methods for CMP Fields
  public String getId();

  public String getName();

  public void setName(String v);

  public Country getCountry();

  public void setCountry(Country v);

  // Business Methods for CMR Fields

  public AddressLocal getHome();

  public void setHome(AddressLocal v);

  public AddressLocal getWork();

  public void setWork(AddressLocal v);

  public Collection getCreditCards();

  public void setCreditCards(Collection v);

  public Collection getOrders();

  public void setOrders(Collection v);

  public Collection getAliases();

  public void setAliases(Collection v);

}
