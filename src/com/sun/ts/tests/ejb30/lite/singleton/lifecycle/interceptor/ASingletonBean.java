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

package com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

/**
 * A no-interface singleton. BeanBase implements CommonSingletonIF, but it must
 * not be treated as ASingletonBean's business interface. CommonSingletonIF is
 * used in the client to serve as a common type for A, B, and CSingletonBean.
 */
@Singleton
@Interceptors({ Interceptor1.class, Interceptor2.class })
@TransactionManagement(TransactionManagementType.BEAN)
public class ASingletonBean extends BeanBase {
}
