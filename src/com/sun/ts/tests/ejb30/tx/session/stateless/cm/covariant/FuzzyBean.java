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

package com.sun.ts.tests.ejb30.tx.session.stateless.cm.covariant;

import com.sun.ts.tests.ejb30.common.covariant.FuzzyBeanBase;
import com.sun.ts.tests.ejb30.common.covariant.FuzzyLocalIF;
import com.sun.ts.tests.ejb30.common.covariant.FuzzyRemoteIF;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless()
public class FuzzyBean extends FuzzyBeanBase
    implements FuzzyRemoteIF, FuzzyLocalIF {

  public FuzzyBean() {
  }

  @Override()
  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public Double[] getNumbers() {
    return (Double[]) super.getNumbers();
  }

  @Override()
  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public String[] getMessages() {
    return super.getMessages();
  }

  @Override()
  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public String getMessage() {
    return super.getMessage();
  }

}
