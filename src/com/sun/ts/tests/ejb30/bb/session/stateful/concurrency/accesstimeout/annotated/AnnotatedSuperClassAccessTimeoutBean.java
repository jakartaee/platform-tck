/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.accesstimeout.annotated;

import jakarta.ejb.Remote;
import jakarta.ejb.Stateful;

import com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF;
import com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.ClassLevelAnnotatedAccessTimeoutBeanBase;

/**
 * All @AccessTimeout metadata are specified in superclass (either class-level
 * or method-level or method-level overriding class-level).
 */
@Stateful(mappedName = "stateful-concurrency-accesstimeout-annotated-AnnotatedSuperClassAccessTimeoutBean")
@Remote(AccessTimeoutRemoteIF.class)
public class AnnotatedSuperClassAccessTimeoutBean extends
    ClassLevelAnnotatedAccessTimeoutBeanBase implements AccessTimeoutIF {
  // 3 methods are declared in ejb-jar.xml as async methods:
  // beanSuperClassLevel
  // beanSuperClassMethodLevelOverride
  // beanSuperClassMethodLevel
}
