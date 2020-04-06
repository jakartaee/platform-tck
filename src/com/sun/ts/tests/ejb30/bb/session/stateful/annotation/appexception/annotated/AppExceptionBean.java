/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.bb.session.stateful.annotation.appexception.annotated;

import com.sun.ts.tests.ejb30.common.appexception.AppExceptionBeanBase;
import com.sun.ts.tests.ejb30.common.appexception.AppExceptionIF;
import com.sun.ts.tests.ejb30.common.appexception.AppExceptionLocalIF;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateful;

@Stateful(name = "AppExceptionBean")
@Local({ AppExceptionLocalIF.class })
@Remote({ AppExceptionIF.class })
// this bean must be CMT. Otherwise, tx would not be propogated from
// RollbackBean.
// Stateless BMT and MDB BMT beans cannot have an active transaction when their
// business method completes.
// @TransactionManagement(TransactionManagementType.BEAN)
public class AppExceptionBean extends AppExceptionBeanBase
    implements AppExceptionIF, AppExceptionLocalIF {
}
