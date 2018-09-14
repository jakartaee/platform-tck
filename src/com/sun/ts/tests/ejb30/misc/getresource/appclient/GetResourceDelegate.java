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

package com.sun.ts.tests.ejb30.misc.getresource.appclient;

import com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceBeanBase;

/**
 * Used by Client class so that getClass() in the superclass GetResourceBeanBase
 * will resolve to this class, and so that the package name used by JDK to
 * convert relative resource name to absolute name will be this package, as
 * opposed to com.sun.ts.tests.ejb30.misc.getresource.common.
 * 
 */
public class GetResourceDelegate extends GetResourceBeanBase {
  // when creating an absolute resource name for Class.getResource, strarts with
  // /
  // when creating an absolute resource name for ClassLoader.getResource, no /
  @Override
  protected String getAbsoluteName(String name, boolean leadingSlash) {
    return (leadingSlash ? "/" : "")
        + "com/sun/ts/tests/ejb30/misc/getresource/appclient/" + name;
  }
}
