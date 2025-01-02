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

package com.sun.ts.tests.ejb30.misc.threebeans;

import com.sun.ts.tests.ejb30.common.helper.Helper;

import jakarta.ejb.EJB;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

@Stateless
@Local(OneLocalIF.class)
@Remote(OneRemoteIF.class)
public class OneBean extends CommonBeanBase {

  @EJB(lookup = "java:comp/env/ejb/twoRemote")
  private TwoRemoteIF twoRemote2;

  @EJB(lookup = "java:comp/env/ejb/twoLocal")
  private TwoLocalIF twoLocal2;

  @EJB(lookup = "java:comp/env/ejb/threeRemote")
  private ThreeRemoteIF threeRemote2;

  @EJB(lookup = "java:comp/env/ejb/threeLocal")
  private ThreeLocalIF threeLocal2;

  @EJB(name = "ejb/twoRemote")
  private TwoRemoteIF twoRemote;

  @EJB(name = "ejb/twoLocal")
  private TwoLocalIF twoLocal;

  @EJB(beanName = "ThreeBean", name = "ejb/threeRemote")
  private ThreeRemoteIF threeRemote;

  @EJB(beanName = "ThreeBean", name = "ejb/threeLocal")
  private ThreeLocalIF threeLocal;

  // testNumber methods are declared in OneRemoteIF. The 2 overloaded methods
  // should be able to co-exist in the same bean class
  public String testNumber(int i) {
    return int.class.getName();
  }

  public String testNumber(Integer i) {
    return Integer.class.getName();
  }

  public String testNumber(double n) {
    return double.class.getName();
  }

  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  // this should cause the appclient to receive EJBException, since there is no
  // client-initiated tx
  public String testNumber(Double n) {
    return Double.class.getName();
  }

  // testException() is declared in OneRemoteIF
  // testException throws TestFailedException is declared in OneLocalIF
  public String testException() {
    return "testException";
  }

  @Override
  protected void verifyInjectedEJB() {
    if (twoRemote == null)
      throw new IllegalStateException("twoRemote was not injected.");
    if (twoLocal == null)
      throw new IllegalStateException("twoLocal was not injected.");
    if (threeRemote == null)
      throw new IllegalStateException("threeRemote was not injected.");
    if (threeLocal == null)
      throw new IllegalStateException("threeLocal was not injected.");

    if (twoRemote2 == null)
      throw new IllegalStateException("twoRemote2 was not injected.");
    if (twoLocal2 == null)
      throw new IllegalStateException("twoLocal2 was not injected.");
    if (threeRemote2 == null)
      throw new IllegalStateException("threeRemote2 was not injected.");
    if (threeLocal2 == null)
      throw new IllegalStateException("threeLocal2 was not injected.");

    Helper.getLogger().info(String.format(
        "Got expected injection result: %s%n %s%n %s%n %s%n %s%n %s%n %s%n %s%n",
        twoRemote, twoLocal, threeRemote, threeLocal, twoRemote2, twoLocal2,
        threeRemote2, threeLocal2));
  }

  @Override
  protected String getBeanName() {
    return "OneBean";
  }
}
