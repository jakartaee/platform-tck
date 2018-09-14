/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.common.resolver;

import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;

public class TCKELResolver extends ELResolver {

  public Object getValue(ELContext elContext, Object object, Object object1) {
    return null;
  }

  public Class getType(ELContext elContext, Object object, Object object1) {
    return null;
  }

  public void setValue(ELContext elContext, Object object, Object object1,
      Object object2) {
  }

  public boolean isReadOnly(ELContext elContext, Object object,
      Object object1) {
    return false;
  }

  public Iterator getFeatureDescriptors(ELContext elContext, Object object) {
    return null;
  }

  public Class getCommonPropertyType(ELContext elContext, Object object) {
    return null;
  }
}
