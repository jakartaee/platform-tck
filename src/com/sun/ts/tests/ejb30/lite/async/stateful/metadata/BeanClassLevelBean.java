/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.async.stateful.metadata;

import jakarta.ejb.Local;
import jakarta.ejb.Stateful;

import com.sun.ts.tests.ejb30.lite.async.common.metadata.BeanClassLevel3BeanBase;
import com.sun.ts.tests.ejb30.lite.async.common.metadata.PlainInterfaceTypeLevelIF;

/**
 * The purpose of this bean is to verify the superclasses of a bean class can
 * have type-level @Asynchronous, which applies to all business methods
 * implemented there. Bean class hierarchy:
 *
 * InterfaceTypeLevelBeanBase BeanClassLevel0BeanBase
 * BeanClassLevel1BeanBase @Asynchronous BeanClassLevel2BeanBase @Asynchronous
 * BeanClassLevel3BeanBase BeanClassLevelBean
 */
@Stateful
@Local(PlainInterfaceTypeLevelIF.class)
public class BeanClassLevelBean extends BeanClassLevel3BeanBase {
}
