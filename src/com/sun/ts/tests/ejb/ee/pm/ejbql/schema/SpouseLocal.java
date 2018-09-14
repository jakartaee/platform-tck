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

import java.util.*;
import javax.ejb.*;

public interface SpouseLocal extends EJBLocalObject {
  // CMP fields

  public String getId();

  public String getFirstName();

  public void setFirstName(String v);

  public String getMaidenName();

  public void setMaidenName(String v);

  public String getLastName();

  public void setLastName(String v);

  public String getSocialSecurityNumber();

  public void setSocialSecurityNumber(String v);

  // CMR fields

  public CustomerLocal getCustomer();

  public void setCustomer(CustomerLocal v);

  public InfoLocal getInfo();

  public void setInfo(InfoLocal v);

}
