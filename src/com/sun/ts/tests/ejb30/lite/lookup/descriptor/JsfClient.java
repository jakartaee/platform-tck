/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.lookup.descriptor;

import java.io.Serializable;

@jakarta.inject.Named("client")
@jakarta.enterprise.context.RequestScoped
public class JsfClient
    extends com.sun.ts.tests.ejb30.lite.lookup.common.JsfClientBase implements Serializable {

    private static final long serialVersionUID = 1L;

  /*
   * @testName: ejbPostConstructRecords
   * 
   * @test_Strategy: verify all ejbs are injected properly by the time
   * post-construct method is invoked.
   */

  /*
   * @testName: ejb2PostConstructRecords
   * 
   * @test_Strategy: verify all ejbs are injected properly by the time
   * post-construct method is invoked.
   */

  /*
   * @testName: clientPostConstructRecords
   * 
   * @test_Strategy: verify all ejbs injected into web client. In embeddable
   * usage, this test is noop.
   */

}
