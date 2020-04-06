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

import java.util.*;
import jakarta.ejb.*;

public interface InfoLocal extends EJBLocalObject {
  // CMP fields

  public String getId();

  public String getStreet();

  public void setStreet(String v);

  public String getCity();

  public void setCity(String v);

  public String getState();

  public void setState(String v);

  public String getZip();

  public void setZip(String v);

  // CMR fields

  public SpouseLocal getSpouse();

  public void setSpouse(SpouseLocal v);

}
