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
package com.sun.ts.tests.ejb30.lite.packaging.war.webinflibonly;

/**
 * See com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.Client. All ejb
 * classes are packaged under WEB-INF/lib/*.jar
 */
public class Client
    extends com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.Client {
  /*
   * @testName: clientToBeanClassLookup
   * 
   * @test_Strategy: client looking up ejb-ref injections in bean class. It must
   * succeed since client and ejb packaged together share the same naming. Not
   * for standalone client.
   */

  /*
   * @testName: beanClassToClientLookup
   * 
   * @test_Strategy: bean looking up resource injected into client. It must
   * succeed since client and ejb packaged together share the same naming. Not
   * for standalone client.
   */

  /*
   * @testName: crossEJBLookup
   * 
   * @test_Strategy: EJBContext looking up ejb-ref injected into other beans. It
   * must succeed since ejbs packaged together share the same naming context.
   */
}
