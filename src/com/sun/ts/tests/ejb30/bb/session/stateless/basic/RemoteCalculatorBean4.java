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

package com.sun.ts.tests.ejb30.bb.session.stateless.basic;

import com.sun.ts.tests.ejb30.common.calc.RemoteCalculator;
import javax.annotation.Resource;
import org.omg.CORBA.ORB;

/**
 * This bean is similar to RemoteCalculatorBean2, and is added here to see
 * 
 * @PostConstruct method in its superclass is invoked correctly.
 *
 *                This bean contains no resource, ejb injection, or component
 *                defining annotations. It has a post-construct method that is
 *                declared in its superclass.
 */

public class RemoteCalculatorBean4 extends RemoteCalculatorBean4Super
    implements RemoteCalculator {

  public RemoteCalculatorBean4() {
  }

  // There may be a bug that annotation in superclass is not process when the
  // component class itself does not contain any annotation.
  // Adding the following injection is a workaround to bypass this bug.
  // @Resource
  // private ORB orb;

}
