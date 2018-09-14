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

package com.sun.ts.tests.ejb30.misc.threebeans;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Local(TwoLocalIF.class)
@Remote(TwoRemoteIF.class)
public class TwoBean extends CommonBeanBase {

  @EJB(lookup = "java:comp/env/ejb/threeRemote")
  private ThreeRemoteIF threeRemote2;

  @EJB(lookup = "java:comp/env/ejb/threeLocal")
  private ThreeLocalIF threeLocal2;

  @EJB(lookup = "java:comp/env/ejb/oneRemote")
  private OneRemoteIF oneRemote2;

  @EJB(lookup = "java:comp/env/ejb/oneLocal")
  private OneLocalIF oneLocal2;

  @EJB(beanName = "ThreeBean", name = "ejb/threeRemote")
  private ThreeRemoteIF threeRemote;

  @EJB(beanName = "ThreeBean", name = "ejb/threeLocal")
  private ThreeLocalIF threeLocal;

  @EJB(name = "ejb/oneRemote")
  private OneRemoteIF oneRemote;

  @EJB(name = "ejb/oneLocal")
  private OneLocalIF oneLocal;

  @Override
  protected void verifyInjectedEJB() {
    if (oneRemote == null)
      throw new IllegalStateException("oneRemote was not injected.");
    if (oneLocal == null)
      throw new IllegalStateException("oneLocal was not injected.");
    if (threeRemote == null)
      throw new IllegalStateException("threeRemote was not injected.");
    if (threeLocal == null)
      throw new IllegalStateException("threeLocal was not injected.");

    if (oneRemote2 == null)
      throw new IllegalStateException("oneRemote2 was not injected.");
    if (oneLocal2 == null)
      throw new IllegalStateException("oneLocal2 was not injected.");
    if (threeRemote2 == null)
      throw new IllegalStateException("threeRemote2 was not injected.");
    if (threeLocal2 == null)
      throw new IllegalStateException("threeLocal2 was not injected.");

    Helper.getLogger().info(String.format(
        "Got expected injection result: %s%n %s%n %s%n %s%n %s%n %s%n %s%n %s%n",
        oneRemote, oneLocal, threeRemote, threeLocal, oneRemote2, oneLocal2,
        threeRemote2, threeLocal2));
  }

  @Override
  protected String getBeanName() {
    return "TwoBean";
  }

  public String testException() throws TestFailedException {
    return oneLocal.testException();
  }
}
