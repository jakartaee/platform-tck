/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.xmloverride.ejbref;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.sun.ts.tests.ejb30.common.lite.NumberIF;

@Stateless
public class TestBean {
  @EJB
  // there are 2 beans implementing NumberIF, and so this @EJB is
  // ambiguous. It's fully resolved in ejb-jar.xml with ejb-link
  private NumberIF overrideBean;

  // beanInterface is omitted here but specified in ejb-jar.xml (<local>)
  // target EJB resolution info is only present in @EJB.beanName.
  // Need to merge the info from both source to resolve it
  // using beanName here to link to target does not seem to work. use <ejb-link>
  // instead.
  // @EJB(name="ejb/overrideInterfaceType")
  @EJB(name = "ejb/overrideInterfaceType", beanName = "XmlOverrideBean")
  private Object overrideInterfaceType;

  // beanName is overridden with <ejb-link> in ejb-jar.xml
  @EJB(name = "ejb/overrideBeanName", beanName = "XmlOverride2Bean")
  private NumberIF overrideBeanName;

  private NumberIF overrideLookup;

  // lookup is overridden in ejb-jar.xml with a valid value
  @SuppressWarnings("unused")
  @EJB(name = "ejb/overrideLookup", lookup = "java:module/XmlOverride2Bean")
  private void setOverrideLookup(NumberIF b) {
    this.overrideLookup = b;
  }

  public int getNumber() {
    return overrideBean.getNumber();
  }

  public int overrideLookup() {
    return overrideLookup.add(0);
  }

  public int overrideInterfaceType() {
    return ((NumberIF) overrideInterfaceType).add(0);
  }

  public int overrideBeanName() {
    return overrideBeanName.add(0);
  }
}
