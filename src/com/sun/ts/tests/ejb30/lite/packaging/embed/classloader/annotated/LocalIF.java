/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.packaging.embed.classloader.annotated;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ejb.Local;

@Local
public interface LocalIF {
  public String getName();

  public List<String> call123();

  public StringBuilder getPostConstructRecords();

  public StringBuilder lookupJNDINames(String appName, String moduleName1,
      String moduleName2, String moduleName3);

  public List<String> setupOneBean(String url, String user, String password,
      String driverName);

  public List<String> setupOneBeanWithArrayList(CopyOnWriteArrayList arrayList);
}
