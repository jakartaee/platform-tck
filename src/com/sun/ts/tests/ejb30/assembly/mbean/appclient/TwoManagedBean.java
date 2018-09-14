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
package com.sun.ts.tests.ejb30.assembly.mbean.appclient;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest;
import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;

@ManagedBean
public class TwoManagedBean extends AppResBeanBase {
  public static final String NAME = "TwoManagedBean";

  @EJB(lookup = "java:module/env/hello")
  private HelloRemoteIF hello;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    AppResTest.verifyInjections(getPostConstructRecords(), hello);
    Helper.assertEquals(null, "ejb3_assembly_mbean_appclient_client", myString,
        getPostConstructRecords());
  }

  @SuppressWarnings("unused")
  @Resource(lookup = "java:module/ModuleName")
  private void setMyString(String s) {
    this.myString = s;
  }

  @Override
  public String getName() {
    return NAME;
  }
}
