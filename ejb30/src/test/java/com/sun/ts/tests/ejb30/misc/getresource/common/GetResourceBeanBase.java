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

package com.sun.ts.tests.ejb30.misc.getresource.common;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public class GetResourceBeanBase implements GetResourceIF {

  private GetResourceTest tester = new GetResourceTest();

  protected GetResourceBeanBase() {
  }

  public void getResourceSamePackage() throws TestFailedException {
    tester.getResourceWithClass(getClass(), RESOURCE_NAME, RESOURCE_CONTENT);
    tester.getResourceWithClassLoader(getClass(),
        getAbsoluteName(RESOURCE_NAME, false), RESOURCE_CONTENT);
  }

  public void getResourceAsStreamSamePackage() throws TestFailedException {
    tester.getResourceAsStreamWithClass(getClass(), RESOURCE_NAME,
        RESOURCE_CONTENT);
    tester.getResourceAsStreamWithClassLoader(getClass(),
        getAbsoluteName(RESOURCE_NAME, false), RESOURCE_CONTENT);
  }

  public void getResourceResolve() throws TestFailedException {
    tester.getResourceWithClass(getClass(),
        getAbsoluteName(RESOURCE_NAME, true), RESOURCE_CONTENT);
    tester.getResourceWithClass(getClass(), RESOURCE_NAME_AT_ROOT,
        RESOURCE_CONTENT_AT_ROOT);
    tester.getResourceWithClassLoader(getClass(), RESOURCE_NAME,
        RESOURCE_CONTENT_AT_ROOT); // no leading / in RESOURCE_NAME
  }

  public void getResourceAsStreamResolve() throws TestFailedException {
    tester.getResourceAsStreamWithClass(getClass(),
        getAbsoluteName(RESOURCE_NAME, true), RESOURCE_CONTENT);
    tester.getResourceAsStreamWithClass(getClass(), RESOURCE_NAME_AT_ROOT,
        RESOURCE_CONTENT_AT_ROOT);
    tester.getResourceAsStreamWithClassLoader(getClass(), RESOURCE_NAME,
        RESOURCE_CONTENT_AT_ROOT);
  }

  public void getResourceResolveEarLib() throws TestFailedException {
    tester.getResourceWithClass(getClass(), EAR_LIB_RESOURCE_NAME_AT_ROOT,
        EAR_LIB_RESOURCE_CONTENT_AT_ROOT);
    tester.getResourceWithClassLoader(getClass(), EAR_LIB_RESOURCE_NAME,
        EAR_LIB_RESOURCE_CONTENT_AT_ROOT);

    tester.getResourceWithClass(getClass(), EAR_LIB_RESOURCE_NAME,
        EAR_LIB_RESOURCE_CONTENT);
    tester.getResourceWithClass(getClass(),
        getAbsoluteName(EAR_LIB_RESOURCE_NAME, true), EAR_LIB_RESOURCE_CONTENT);
    tester.getResourceWithClassLoader(getClass(),
        getAbsoluteName(EAR_LIB_RESOURCE_NAME, false),
        EAR_LIB_RESOURCE_CONTENT);
  }

  public void getResourceAsStreamResolveEarLib() throws TestFailedException {
    tester.getResourceAsStreamWithClass(getClass(),
        EAR_LIB_RESOURCE_NAME_AT_ROOT, EAR_LIB_RESOURCE_CONTENT_AT_ROOT);
    tester.getResourceAsStreamWithClassLoader(getClass(), EAR_LIB_RESOURCE_NAME,
        EAR_LIB_RESOURCE_CONTENT_AT_ROOT);

    tester.getResourceAsStreamWithClass(getClass(), EAR_LIB_RESOURCE_NAME,
        EAR_LIB_RESOURCE_CONTENT);
    tester.getResourceAsStreamWithClass(getClass(),
        getAbsoluteName(EAR_LIB_RESOURCE_NAME, true), EAR_LIB_RESOURCE_CONTENT);
    tester.getResourceAsStreamWithClassLoader(getClass(),
        getAbsoluteName(EAR_LIB_RESOURCE_NAME, false),
        EAR_LIB_RESOURCE_CONTENT);
  }

  public void getResourceNullParam() throws TestFailedException {
    tester.getResourceNullParam(getClass());
  }

  public void getResourceNonexisting() throws TestFailedException {
    tester.getResourceNonexisting(getClass());
  }

  public void getResourceAsStreamNullParam() throws TestFailedException {
    tester.getResourceAsStreamNullParam(getClass());
  }

  public void getResourceAsStreamNonexisting() throws TestFailedException {
    tester.getResourceAsStreamNonexisting(getClass());
  }

  // when creating an absolute resource name for Class.getResource, strarts with
  // /
  // when creating an absolute resource name for ClassLoader.getResource, no /
  protected String getAbsoluteName(String name, boolean leadingSlash) {
    return (leadingSlash ? "/" : "")
        + "com/sun/ts/tests/ejb30/misc/common/warejb/" + name;
  }

}
