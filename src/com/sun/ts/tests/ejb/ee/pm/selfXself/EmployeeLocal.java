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

package com.sun.ts.tests.ejb.ee.pm.selfXself;

import com.sun.ts.lib.util.*;

import java.util.*;
import javax.ejb.*;

public interface EmployeeLocal extends EJBLocalObject {
  // Business Methods for CMP Fields
  public Integer getId();

  public String getFirstName();

  public void setFirstName(String v);

  public String getLastName();

  public void setLastName(String v);

  public java.util.Date getHireDate();

  public void setHireDate(java.util.Date d);

  public float getSalary();

  public void setSalary(float f);

  // Business Methods for CMR Fields

  public DepartmentLocal getDepartment();

  public void setDepartment(DepartmentLocal v);

  public EmployeeLocal getManager();

  public void setManager(EmployeeLocal v);
}
