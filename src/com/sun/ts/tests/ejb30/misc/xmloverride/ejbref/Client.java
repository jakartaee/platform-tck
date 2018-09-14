/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.misc.xmloverride.ejbref;

import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;

import java.util.Properties;

import javax.ejb.EJB;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.lite.NumberIF;

/**
 * Similar tests are also at ejb30/lite/xmloverride/ejbref. This test dir tests
 * remote ejb ref from appclient, and its overriding behaviors.
 */
public class Client extends EETest {
  protected Properties props;

  @EJB
  // there are 2 beans implementing NumberIF, and so this @EJB is
  // ambiguous. It's fully resolved in application-client.xml with ejb-link
  private static NumberIF overrideBean;

  // beanInterface is omitted here but specified in application-client.xml
  // (<local>)
  // target EJB resolution info is only present in @EJB.beanName.
  // Need to merge the info from both source to resolve it
  // using beanName here to link to target does not seem to work. use <ejb-link>
  // instead.
  // @EJB(name="ejb/overrideInterfaceType")
  @EJB(name = "ejb/overrideInterfaceType", beanName = "XmlOverrideBean")
  private static Object overrideInterfaceType;

  // beanName is overridden with <ejb-link> in application-client.xml
  @EJB(name = "ejb/overrideBeanName", beanName = "XmlOverride2Bean")
  private static NumberIF overrideBeanName;

  private static NumberIF overrideLookup;

  // lookup is overridden in application-client.xml with a valid value
  @SuppressWarnings("unused")
  @EJB(name = "ejb/overrideLookup", lookup = "java:app/ejb/XmlOverride2Bean")
  private static void setOverrideLookup(NumberIF b) {
    overrideLookup = b;
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  @SuppressWarnings("unused")
  public void setup(String[] args, Properties p) {
    props = p;
  }

  public void cleanup() {
  }

  /*
   * @testName: resolveByEjbLinkInXml
   * 
   * @test_Strategy: @EJB in Client class is incomplete. But the corresponding
   * ejb-local-ref in application-client.xml resolves it with ejb-link.
   */
  public void resolveByEjbLinkInXml() {
    TLogger.log(assertEquals("Check correct target EJB is resolved. ", 1,
        overrideBean.getNumber()));
  }

  /*
   * @testName: overrideLookup
   * 
   * @test_Strategy: lookup-name in application-client.xml overrides lookup attr
   * in @EJB
   */
  public void overrideLookup() {
    TLogger.log(assertEquals("Check correct target EJB is resolved. ", 1,
        overrideLookup.add(0)));
  }

  /*
   * @testName: overrideInterfaceType
   * 
   * @test_Strategy: <local> in application-client.xml overrides beanInterface
   * attr in @EJB
   */
  public void overrideInterfaceType() {
    TLogger.log(assertEquals("Check correct target EJB is resolved. ", 1,
        ((NumberIF) overrideInterfaceType).add(0)));
  }

  /*
   * @testName: overrideBeanName
   * 
   * @test_Strategy: <ejb-link> in application-client.xml overrides beanName
   * attr in @EJB
   */
  public void overrideBeanName() {
    TLogger.log(assertEquals("Check correct target EJB is resolved. ", 1,
        overrideBeanName.add(0)));
  }

}
