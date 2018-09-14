/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.bb.async.common.descriptor;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorIF.EXCEPTION_MESSAGE;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

abstract public class DescriptorClientBase extends EJBLiteClientBase {

  @EJB(name = "noInterface", beanName = "DescriptorBean")
  private DescriptorBean noInterface;

  @EJB(name = "descriptorIF", beanName = "DescriptorBean")
  private DescriptorIF descriptorIF;

  @EJB(name = "descriptor2IF", beanName = "DescriptorBean")
  private Descriptor2IF descriptor2IF;

  @EJB(name = "descriptorRemoteIF", beanName = "DescriptorBean")
  private DescriptorRemoteIF descriptorRemoteIF;

  @EJB(name = "descriptor2RemoteIF", beanName = "DescriptorBean")
  private Descriptor2RemoteIF descriptor2RemoteIF;

  protected DescriptorBean getNoInterface() {
    return noInterface;
  }

  protected DescriptorIF getDescriptorIF() {
    return descriptorIF;
  }

  protected Descriptor2IF getDescriptor2IF() {
    return descriptor2IF;
  }

  protected DescriptorRemoteIF getDescriptorRemoteIF() {
    return descriptorRemoteIF;
  }

  protected Descriptor2RemoteIF getDescriptor2RemoteIF() {
    return descriptor2RemoteIF;
  }

  protected TimeoutDescriptorBeanBase getTimeoutDescriptorBean() {
    return null;
  }

  private void checkEJBException(EJBException e) {
    RuntimeException cause = (RuntimeException) e.getCause();
    assertEquals(null, EXCEPTION_MESSAGE, cause.getMessage());
  }

  private DescriptorIF[] getAllBeans() {
    return new DescriptorIF[] { getNoInterface(), getDescriptorIF(),
        getDescriptor2IF(), getDescriptorRemoteIF(), getDescriptor2RemoteIF() };
  }

  private DescriptorIF[] getRemoteBeans() {
    return new DescriptorIF[] { getDescriptorRemoteIF(),
        getDescriptor2RemoteIF() };
  }

  private DescriptorIF[] getLocalBeans() {
    return new DescriptorIF[] { getNoInterface(), getDescriptorIF(),
        getDescriptor2IF() };
  }

  /*
   * testName: allViews
   * 
   * @test_Strategy:
   */
  public void allViews() {
    for (DescriptorIF b : getAllBeans()) {
      b.allViews();
      appendReason("Verified async method on " + b);
    }
  }

  /*
   * testName: localViews
   * 
   * @test_Strategy:
   */
  public void localViews() {
    for (DescriptorIF b : getLocalBeans()) {
      b.localViews();
    }

    for (DescriptorIF b : getRemoteBeans()) {
      try {
        b.localViews();
      } catch (EJBException e) {
        checkEJBException(e);
      }
    }
  }

  /*
   * testName: remoteViews
   * 
   * @test_Strategy:
   */
  public void remoteViews() {
    for (DescriptorIF b : getLocalBeans()) {
      try {
        b.remoteViews();
      } catch (EJBException e) {
        checkEJBException(e);
      }
    }

    for (DescriptorIF b : getRemoteBeans()) {
      b.remoteViews();
    }
  }

  /*
   * testName: allParams
   * 
   * @test_Strategy:
   */
  public void allParams() {
    for (DescriptorIF b : getAllBeans()) {
      b.allParams();
      b.allParams(0);
      b.allParams("s");
    }
  }

  /*
   * testName: noParams
   * 
   * @test_Strategy:
   */
  public void noParams() {
    for (DescriptorIF b : getAllBeans()) {
      b.noParams();
    }
    for (DescriptorIF b : getAllBeans()) {
      try {
        b.noParams(0);
      } catch (EJBException e) {
        checkEJBException(e);
      }
    }

  }

  /*
   * testName: intParams
   * 
   * @test_Strategy:
   */
  public void intParams() {
    for (DescriptorIF b : getAllBeans()) {
      b.intParams(0, 1);
    }
    for (DescriptorIF b : getAllBeans()) {
      try {
        b.intParams(0, 1, 2);
      } catch (EJBException e) {
        checkEJBException(e);
      }
    }
  }

  /*
   * testName: intParamsLocalViews
   * 
   * @test_Strategy:
   */
  public void intParamsLocalViews() {
    for (DescriptorIF b : getLocalBeans()) {
      b.intParamsLocalViews(0, 1);
    }
    for (DescriptorIF b : getLocalBeans()) {
      try {
        b.intParamsLocalViews();
      } catch (EJBException e) {
        checkEJBException(e);
      }
    }
    for (DescriptorIF b : getRemoteBeans()) {
      try {
        b.intParamsLocalViews(0, 1);
      } catch (EJBException e) {
        checkEJBException(e);
      }
    }
    for (DescriptorIF b : getRemoteBeans()) {
      try {
        b.intParamsLocalViews();
      } catch (EJBException e) {
        checkEJBException(e);
      }
    }
  }

  /*
   * testName: timeoutDescriptorBean
   * 
   * @test_Strategy:
   */
  public void timeoutDescriptorBean() {
    getTimeoutDescriptorBean().voidRuntimeException();
    appendReason("No exception received by the client, as expected.");
  }

}
