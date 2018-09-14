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

import javax.ejb.SessionContext;

import com.sun.ts.tests.ejb30.common.helper.Helper;

public class LookupBeanPlainBase implements LookupIF {

  protected StringBuilder postConstructRecords = new StringBuilder();

  protected LookupIF lookupBean;

  protected LookupIF lookup2Bean;

  protected LookupIF lookupBeanComp;

  protected LookupIF lookup2BeanComp;

  protected LookupIF lookupBeanModule;

  protected LookupIF lookup2BeanModule;

  protected LookupIF lookupBeanApp;

  protected LookupIF lookup2BeanApp;

  protected SessionContext sctx;

  protected void nonPostConstruct() {
    Helper.assertEquals("Verify lookupBean injection", LOOKUP_BEAN,
        lookupBean.getName(), postConstructRecords);
    Helper.assertEquals("Verify lookup2Bean injection", LOOKUP2_BEAN,
        lookup2Bean.getName(), postConstructRecords);

    Helper.assertEquals("Verify lookupBeanComp injection", LOOKUP_BEAN,
        lookupBeanComp.getName(), postConstructRecords);
    Helper.assertEquals("Verify lookup2BeanComp injection", LOOKUP2_BEAN,
        lookup2BeanComp.getName(), postConstructRecords);

    Helper.assertEquals("Verify lookupBeanModule injection", LOOKUP_BEAN,
        lookupBeanModule.getName(), postConstructRecords);
    Helper.assertEquals("Verify lookup2BeanModule injection", LOOKUP2_BEAN,
        lookup2BeanModule.getName(), postConstructRecords);

    Helper.assertEquals("Verify lookupBeanApp injection", LOOKUP_BEAN,
        lookupBeanApp.getName(), postConstructRecords);
    Helper.assertEquals("Verify lookup2BeanApp injection", LOOKUP2_BEAN,
        lookup2BeanApp.getName(), postConstructRecords);

    Helper.assertNotEquals("Verify sctx injection", null, sctx,
        postConstructRecords);

  }

  public String getName() {
    return null;
  }

  public StringBuilder getPostConstructRecords() {
    nonPostConstruct();
    return postConstructRecords;
  }

}
