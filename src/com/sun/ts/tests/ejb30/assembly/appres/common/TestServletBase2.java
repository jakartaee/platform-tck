/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;

abstract public class TestServletBase2 extends TestServletBase {
  @Resource(name = "java:app/env/myString")
  protected String myString;

  @EJB(lookup = "java:app/env/hello")
  protected HelloRemoteIF hello;

  @EJB(lookup = "java:app/env/AppResBean-remote")
  protected AppResRemoteIF appResBeanRemote;

  @EJB(lookup = "java:app/env/AppResBean-local")
  protected AppResLocalIF appResBeanLocal;

  @Resource
  private Validator validator;

  @Resource
  private ValidatorFactory validatorFactory;

  @PostConstruct
  protected void postConstruct() {
    postConstructRecords = new StringBuilder();
    AppResTest.beanPostConstruct(myString, postConstructRecords, true, false);
    AppResTest.verifyInjections(postConstructRecords, hello, appResBeanRemote,
        appResBeanLocal, validatorFactory, validator);
  }

}
