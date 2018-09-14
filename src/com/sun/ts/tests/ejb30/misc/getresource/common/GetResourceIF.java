/*
 * Copyright (c) 2006, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.misc.getresource.common;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public interface GetResourceIF {
  /**
   * Name of the resource file, and also its content when it is packaged in the
   * same package of the requesting java class.
   */
  public static final String RESOURCE_NAME = "cts-ejb3-test-5.1.1-beta.txt";

  public static final String RESOURCE_CONTENT = RESOURCE_NAME;

  /**
   * When the resource file <RESOURCE_NAME> is packaged at the root of a
   * classpath element, e.g., directly under WEB-INF/classes, its content is
   * <RESOURCE_AT_ROOT_CONTENT>. So it is possible there are two resource files
   * named <RESOURCE_NAME> within the classpath, but each has different
   * namespace and content.
   */
  public static final String RESOURCE_NAME_AT_ROOT = "/cts-ejb3-test-5.1.1-beta.txt";

  public static final String RESOURCE_CONTENT_AT_ROOT = "/";

  public static final String EAR_LIB_RESOURCE_NAME = "cts-ejb3-test-5.1.1-beta-ear-lib.txt";

  public static final String EAR_LIB_RESOURCE_CONTENT = EAR_LIB_RESOURCE_NAME;

  public static final String EAR_LIB_RESOURCE_NAME_AT_ROOT = "/cts-ejb3-test-5.1.1-beta-ear-lib.txt";

  public static final String EAR_LIB_RESOURCE_CONTENT_AT_ROOT = "/lib";

  void getResourceNullParam() throws TestFailedException;

  void getResourceAsStreamNullParam() throws TestFailedException;

  void getResourceNonexisting() throws TestFailedException;

  void getResourceAsStreamNonexisting() throws TestFailedException;

  void getResourceSamePackage() throws TestFailedException;

  void getResourceAsStreamSamePackage() throws TestFailedException;

  void getResourceResolve() throws TestFailedException;

  void getResourceAsStreamResolve() throws TestFailedException;

  void getResourceResolveEarLib() throws TestFailedException;

  void getResourceAsStreamResolveEarLib() throws TestFailedException;

}
