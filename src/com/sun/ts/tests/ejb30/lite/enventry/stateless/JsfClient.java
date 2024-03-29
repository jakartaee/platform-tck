/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.enventry.stateless;

import java.io.Serializable;

import jakarta.ejb.EJB;

@jakarta.inject.Named("client")
@jakarta.enterprise.context.RequestScoped
public class JsfClient
        extends com.sun.ts.tests.ejb30.lite.enventry.common.JsfClientBase implements Serializable {

  private static final long serialVersionUID = 1L;

  @EJB(beanName = "EnvEntryBean", beanInterface = EnvEntryBean.class)
  protected void setEnvEntryBean(EnvEntryBean b) {
    this.envEntryBean = b;
  }

  /*
   * @testName: ejbPostConstructRecords
   * 
   * @test_Strategy: verify all env-entry are injected properly by the time
   * post-construct method is invoked.
   */

  /*
   * @testName: clientPostConstructRecords
   * 
   * @test_Strategy: verify all env-entry injected into ejb can also be looked
   * up from web client. In embeddable usage, this test is noop.
   */
}
