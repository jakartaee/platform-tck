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
package com.sun.ts.tests.ejb30.lite.packaging.war.datasource.stateful;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceIF;

public class Client extends EJBLiteClientBase {
  @EJB
  private DataSourceIF dataSourceBean;

  /*
   * @testName: postConstructRecordsEJB
   * 
   * @test_Strategy: look up the data sources declared with annotation in EJB,
   * and verify the injected datasource inside its PostConstruct method.
   */
  public void postConstructRecordsEJB() {
    appendReason(dataSourceBean.getPostConstructRecords());
  }

  /*
   * @testName: postConstructRecordsInterceptor
   * 
   * @test_Strategy: look up the data sources declared with annotation in
   * Interceptor1, and verify the injected datasource inside its PostConstruct
   * method.
   */
  public void postConstructRecordsInterceptor() {
    appendReason(dataSourceBean.getPostConstructRecords());
  }

}
