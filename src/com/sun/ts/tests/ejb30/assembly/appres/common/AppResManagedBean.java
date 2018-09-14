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
package com.sun.ts.tests.ejb30.assembly.appres.common;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;

@ManagedBean("test-managed-bean")
public class AppResManagedBean extends AppResBeanBase {
  public static final String NAME = "AppResManagedBean";

  @EJB(lookup = "java:app/env/hello")
  private HelloRemoteIF hello;

  @EJB(lookup = "java:app/env/AppResBean-remote")
  private AppResRemoteIF appResBeanRemote;

  @EJB(lookup = "java:app/env/AppResBean-local")
  private AppResLocalIF appResBeanLocal;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    AppResTest.beanPostConstruct(myString, getPostConstructRecords(), true,
        false);
    AppResTest.verifyInjections(getPostConstructRecords(), hello,
        appResBeanRemote, appResBeanLocal);
  }

  @SuppressWarnings("unused")
  @Resource(lookup = "java:app/env/myString")
  private void setMyString(String s) {
    this.myString = s;
  }

  @Override
  public String getName() {
    return NAME;
  }
}
