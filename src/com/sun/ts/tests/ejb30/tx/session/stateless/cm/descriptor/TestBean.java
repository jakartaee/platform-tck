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

package com.sun.ts.tests.ejb30.tx.session.stateless.cm.descriptor;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.tx.common.session.cm.TestBeanBase;
import com.sun.ts.tests.ejb30.tx.common.session.cm.TestIF;
import com.sun.ts.tests.ejb30.tx.common.session.cm.TxIF;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRequiredException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@Stateless()
@Remote({ TestIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
public class TestBean extends TestBeanBase implements TestIF {

  @EJB
  private SameMethodRemoteIF sameMethodRemoteBean;

  @EJB
  private SameMethodLocalIF sameMethodLocalBean;

  @Override
  public void sameMethodDifferentTxAttr() throws TestFailedException {
    // should succeed, since sameMethod on remote intf has tx attr NEVER
    sameMethodRemoteBean.sameMethod();

    // should cause Exception, since sameMethod on local intf has tx attr
    // MANDATORY
    try {
      sameMethodLocalBean.sameMethod();
      throw new TestFailedException(
          "Expecting javax.ejb.EJBTransactionRequiredException, but got none");
    } catch (EJBTransactionRequiredException e) {
      Helper.getLogger()
          .info("Got expected javax.ejb.EJBTransactionRequiredException");
    }
  }

  @Override()
  protected void mandatoryTestOverloaded(TxIF b) throws TestFailedException {
    String reason = "";
    String description = "The transaction attribute specified in ejb-jar.xml "
        + "for method named mandatoryTest should apply to all overloaded methods "
        + "with this name";
    try {
      b.mandatoryTest(description);
      reason += this
          + " Expecting javax.ejb.EJBTransactionRequiredException, but got none";
      throw new TestFailedException(reason);
    } catch (EJBTransactionRequiredException e) {
      reason += this
          + " Got expected javax.ejb.EJBTransactionRequiredException";
      Helper.getLogger().info(reason);
    }
  }

  @Override()
  protected void neverTestOverloaded(TxIF b) throws TestFailedException {
    String reason = "";
    String description = "The transaction attribute specified in ejb-jar.xml "
        + "for method named neverTest should apply to all overloaded methods "
        + "with this name";
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      b.neverTest(description);
      reason = "Expecting javax.ejb.EJBException, but got none";
      throw new TestFailedException(reason);
    } catch (EJBException e) {
      reason = this + " Got expected javax.ejb.EJBException";
      Helper.getLogger().info(reason);
    } catch (NotSupportedException ex) {
      throw new TestFailedException(
          "Expecting javax.ejb.EJBException, but got " + ex);
    } catch (SystemException ex) {
      throw new TestFailedException(
          "Expecting javax.ejb.EJBException, but got " + ex);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }
    }
  }
}
