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

package com.sun.ts.tests.ejb30.lite.interceptor.common.business;

import java.util.List;
import java.util.Map;

import com.sun.ts.tests.ejb30.common.appexception.AtCheckedRollbackAppException;

public interface InterceptorIF {

  void allInterceptors(List<String> history);

  void excludeDefaultInterceptors(List<String> history);

  void excludeClassInterceptors(List<String> history);

  void overrideInterceptorMethod(List<String> history);

  void overrideBeanInterceptorMethod(List<String> history);

  void overrideBeanInterceptorMethod2(List<String> history);

  void overrideBeanInterceptorMethod3(List<String> history);

  void overrideBeanInterceptorMethod4(List<String> history);

  void skipProceed(List<String> history);

  void applicationExceptionRollback() throws AtCheckedRollbackAppException;

  Map<String, Object> getContextData(List<String> history);
}
