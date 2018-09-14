/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.ejbcontext.common;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.util.List;

public interface EJBContextIF {

  Class<?> getInvokedBusinessInterface();

  String getMessageContextIllegalStateException() throws TestFailedException;

  String lookupIllegalArgumentException() throws TestFailedException;

  String getBusinessObjectIllegalStateException() throws TestFailedException;

  Integer add(int a, int b);

  <T> T getBusinessObject(Class<T> businessInterface)
      throws IllegalStateException;

  List<String> getInjectionRecords();

  String lookupEJBContext();

  List<String> getInjectionRecordsInInterceptor();

  String lookupEJBContextInInterceptor();

  String lookupPortableJNDINames();

}
