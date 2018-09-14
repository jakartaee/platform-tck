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

package com.sun.ts.tests.ejb30.assembly.common;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.net.URL;

public interface AssemblyCommonIF {
  public static final String RESOURCE_NAME = "foo.txt";

  public static final String EAR_LIB_JAR_NAME = "/shared.jar";

  int getPostConstructCalls();

  int remoteAdd(int a, int b);

  String callHelloBean();

  void libSubdirNotScanned() throws TestFailedException;

  void earLibNotInClasspath() throws TestFailedException;

  String dirUsedInClassPath();

  URL getResource(String name);

  String getResourceContent(String name) throws TestFailedException;
}
