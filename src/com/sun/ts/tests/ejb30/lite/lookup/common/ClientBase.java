/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.lookup.common;

import static com.sun.ts.tests.ejb30.lite.lookup.common.LookupIF.LOOKUP2_BEAN;
import static com.sun.ts.tests.ejb30.lite.lookup.common.LookupIF.LOOKUP_BEAN;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

abstract public class ClientBase extends EJBLiteClientBase {
  private StringBuilder postConstructRecords = new StringBuilder();

  @EJB(name = "ejb/ClientBase/lookupBean", beanName = "LookupBean")
  private LookupIF lookupBean;

  @EJB(name = "ejb/ClientBase/lookup2Bean", beanName = "Lookup2Bean")
  private LookupIF lookup2Bean;

  // It's an error to specify both lookup and beanName. To provide beanName
  // value
  // to ejbembed, beanName value is embedded inside description attribute. It is
  // processed in com.sun.ts.tests.common.vehicle.ejbembed.InjectionResolver,
  // when
  // the tests run in ejbembed vehicle.

  @EJB(lookup = "java:comp/env/ejb/ClientBase/lookupBean", description = "beanName=LookupBean")
  private LookupIF lookupBeanAgain;

  @EJB(lookup = "java:comp/env/ejb/ClientBase/lookup2Bean", description = "beanName=Lookup2Bean")
  private LookupIF lookup2BeanAgain;

  @EJB(lookup = "java:module/LookupBean", description = "beanName=LookupBean")
  private LookupIF lookupBeanModule;

  @EJB(lookup = "java:module/Lookup2Bean", description = "beanName=Lookup2Bean")
  private LookupIF lookup2BeanModule;

  @EJB(lookup = "java:module/LookupBean", description = "beanName=LookupBean")
  private LookupIF lookupBeanApp;

  @EJB(lookup = "java:module/Lookup2Bean", description = "beanName=Lookup2Bean")
  private LookupIF lookup2BeanApp;

  // setModuleName is called in vehicle class as part of request processing,
  // after @PostConstruct method. So change the following method to be called
  // at request processing time.
  private void nonPostConstruct() {
    if (getContainer() != null) {
      return;
    }
    Helper.assertEquals("Verify lookupBean injection", LOOKUP_BEAN,
        lookupBean.getName(), postConstructRecords);
    Helper.assertEquals("Verify lookup2Bean injection", LOOKUP2_BEAN,
        lookup2Bean.getName(), postConstructRecords);

    Helper.assertEquals("Verify lookupBeanAgain injection", LOOKUP_BEAN,
        lookupBeanAgain.getName(), postConstructRecords);
    Helper.assertEquals("Verify lookup2BeanAgain injection", LOOKUP2_BEAN,
        lookup2BeanAgain.getName(), postConstructRecords);

    Helper.assertEquals("Verify lookupBeanModule injection", LOOKUP_BEAN,
        lookupBeanModule.getName(), postConstructRecords);
    Helper.assertEquals("Verify lookup2BeanModule injection", LOOKUP2_BEAN,
        lookup2BeanModule.getName(), postConstructRecords);

    Helper.assertEquals("Verify lookupBeanApp injection", LOOKUP_BEAN,
        lookupBeanApp.getName(), postConstructRecords);
    Helper.assertEquals("Verify lookup2BeanApp injection", LOOKUP2_BEAN,
        lookup2BeanApp.getName(), postConstructRecords);

    // Just verify the lookup of ModuleName and AppName, not injections
    String lookup = "java:module/ModuleName";
    String expected = getModuleName();
    String actual = (String) ServiceLocator.lookupNoTry(lookup);
    Helper.assertEquals("Check " + lookup, expected, actual,
        postConstructRecords);

    lookup = "java:app/AppName";
    actual = (String) ServiceLocator.lookupNoTry(lookup);
    Helper.assertEquals("Check " + lookup, expected, actual);
  }

  /*
   * testName: ejbPostConstructRecords
   * 
   * @test_Strategy: verify all ejbs are injected properly by the time
   * post-construct method is invoked.
   */
  public void ejbPostConstructRecords() {
    appendReason(lookupBean.getPostConstructRecords());
  }

  /*
   * testName: ejb2PostConstructRecords
   * 
   * @test_Strategy: verify all ejbs are injected properly by the time
   * post-construct method is invoked.
   */
  public void ejb2PostConstructRecords() {
    appendReason(lookup2Bean.getPostConstructRecords());
  }

  /*
   * testName: clientPostConstructRecords
   * 
   * @test_Strategy: verify all ejbs injected into web client. In embeddable
   * usage, this test is noop.
   */
  public void clientPostConstructRecords() {
    nonPostConstruct();
    appendReason(postConstructRecords);
  }

}
