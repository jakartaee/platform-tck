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

package com.sun.ts.tests.ejb30.common.busiface;

import javax.ejb.EJBException;

/*
 * This class should not implement any more interfaces. Its superclass
 * BusinessLocal1Base still implements BusinessLocalIF1.  It should be the 
 * only interface implemented by this class.
 * All subclasses should either use @Remote/@Local annotations, or use ejb-jar
 * descriptor elements to declare its remote/local interfaces.
 */
abstract public class BusinessBeanBase extends BusinessLocal1Base
// implements BusinessIF1, BusinessIF2,
// BusinessLocalIF1, BusinessLocalIF2,
// AnnotatedLocalBusinessInterface1, AnnotatedLocalBusinessInterface2,
// AnnotatedBusinessInterface1, AnnotatedBusinessInterface2
{
  public boolean _businessMethod1_(String[] s) {
    resetValue(s);
    return true;
  }

  public String[] businessMethodLocal2(String[] s) {
    resetValue(s);
    return s;
  }

  public boolean businessMethod2(String[] s) {
    resetValue(s);
    return true;
  }

  public boolean annotatedBusinessInterfaceMethod(String[] s) {
    resetValue(s);
    return true;
  }

  public void afterCompletion(boolean b) throws EJBException {
    throw new EJBException("This method should not be called since this bean"
        + " does not implement SessionSynchronization.");
  }

  public void beforeCompletion() throws EJBException {
    throw new EJBException("This method should not be called since this bean"
        + " does not implement SessionSynchronization.");
  }

  public void afterBegin() throws EJBException {
    throw new EJBException("This method should not be called since this bean"
        + " does not implement SessionSynchronization.");
  }
}
