/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

/*
 * @(#)Ejb1Test.java	1.10 02/02/22
 */

package com.sun.ts.tests.xa.ee.xresXcomp2;

import java.util.Properties;
import java.util.Vector;

public interface Ejb1Test {
  public void initialize(Properties p);

  public void dbConnect(String tName);

  public void txDbConnect(String tName);

  public void createData(String tName);

  public void insertDup(String tName, String tSize);

  public boolean insert(String tName, int key);

  public void delete(String tName, int fromKey, int toKey);

  public Vector getResults(String tName);

  public void destroyData(String tName);

  public void dbUnConnect(String tName);

  public void txDbUnConnect(String tName);

  public void initLogging(Properties p);

  public void throwEJBException();
}
