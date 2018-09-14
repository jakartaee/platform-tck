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
package com.sun.ts.tests.ejb30.lite.ejbcontext.stateless;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import static com.sun.ts.tests.ejb30.lite.ejbcontext.common.Util.postConstruct1;
import com.sun.ts.tests.ejb30.lite.ejbcontext.common.EJBContext2IF;
import com.sun.ts.tests.ejb30.lite.ejbcontext.common.EJBContextBeanBase;
import com.sun.ts.tests.ejb30.lite.ejbcontext.common.EJBContextIF;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.EJBContext;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

@Stateless
@Local({ EJBContext2IF.class, EJBContextIF.class })
public class EJBContext2Bean extends EJBContextBeanBase
    implements EJBContext2IF, EJBContextIF {

  @Resource
  private EJBContext ejbContext; // field-inject EJBContext

  private SessionContext sessionContext; // setter-inject SessionContext

  private EJBContext ejbContextFromDescriptorInjection; // injected in
                                                        // ejb-jar.xml

  @SuppressWarnings("unused")
  @Resource
  private void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    Helper.getLogger()
        .info("In postConstruct of " + this + ", ejbContext=" + ejbContext
            + ", sessionContext=" + sessionContext
            + ", ejbContextFromDescriptorInjection="
            + ejbContextFromDescriptorInjection);
    postConstruct1(injectionRecords, ejbContext, sessionContext,
        ejbContextFromDescriptorInjection);
  }

  @SuppressWarnings("unused")
  @PreDestroy
  private void preDestroy() {
    Helper.preDestroy(this);
  }
}
