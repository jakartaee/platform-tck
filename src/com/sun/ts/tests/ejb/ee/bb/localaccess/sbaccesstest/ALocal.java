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

package com.sun.ts.tests.ejb.ee.bb.localaccess.sbaccesstest;

import com.sun.ts.lib.util.*;

import java.util.*;
import jakarta.ejb.*;
import java.rmi.*;

public interface ALocal extends EJBLocalObject {
  // Business Methods for ALocal CMP Fields
  public Integer getId();

  public String getName();

  public void setName(String v);

  public int getValue();

  public void setValue(int v);

  // Other Business Methods
  public String whoAmI();
}
