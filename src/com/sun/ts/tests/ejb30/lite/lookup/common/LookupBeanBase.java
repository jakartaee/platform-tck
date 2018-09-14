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
package com.sun.ts.tests.ejb30.lite.lookup.common;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;

public class LookupBeanBase extends LookupBeanPlainBase implements LookupIF {

  @SuppressWarnings("unused")
  @EJB(name = "ejb/LookupBeanBase/lookupBean", beanName = "LookupBean")
  private void setLookupBean(LookupIF b) {
    this.lookupBean = b;
  }

  @SuppressWarnings("unused")
  @EJB(name = "ejb/LookupBeanBase/lookup2Bean", beanName = "Lookup2Bean")
  private void setLookup2Bean(LookupIF b) {
    this.lookup2Bean = b;
  }

  @SuppressWarnings("unused")
  @EJB(lookup = "java:comp/env/ejb/LookupBeanBase/lookupBean")
  private void setLookupBeanComp(LookupIF b) {
    this.lookupBeanComp = b;
  }

  @SuppressWarnings("unused")
  @EJB(lookup = "java:comp/env/ejb/LookupBeanBase/lookup2Bean")
  private void setLookup2BeanComp(LookupIF b) {
    this.lookup2BeanComp = b;
  }

  @SuppressWarnings("unused")
  @EJB(lookup = "java:module/LookupBean")
  private void setLookupBeanModule(LookupIF b) {
    this.lookupBeanModule = b;
  }

  @SuppressWarnings("unused")
  @EJB(lookup = "java:module/Lookup2Bean")
  private void setLookup2BeanModule(LookupIF b) {
    this.lookup2BeanModule = b;
  }

  @SuppressWarnings("unused")
  @EJB(lookup = "java:module/LookupBean")
  private void setLookupBeanApp(LookupIF b) {
    this.lookupBeanApp = b;
  }

  @SuppressWarnings("unused")
  @EJB(lookup = "java:module/Lookup2Bean")
  private void setLookup2BeanApp(LookupIF b) {
    this.lookup2BeanApp = b;
  }

  @SuppressWarnings("unused")
  @Resource(lookup = "java:comp/EJBContext")
  private void setSctx(SessionContext sctx) {
    this.sctx = sctx;
  }
}
