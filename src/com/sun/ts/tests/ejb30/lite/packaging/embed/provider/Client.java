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
package com.sun.ts.tests.ejb30.lite.packaging.embed.provider;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.embeddable.EJBContainer;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

public final class Client extends EJBLiteClientBase {
  private Map<String, Object> containerInitProps = new HashMap<String, Object>();

  /*
   * @class.testArgs:
   * 
   * @class.setup_props:
   */

  @Override
  public Map<String, Object> getContainerInitProperties() {
    containerInitProps.put(EJBContainer.PROVIDER,
        TSEJBContainerImpl.class.getName());
    return containerInitProps;
  }

  /*
   * @testName: customProvider
   * 
   * @test_Strategy: load a custom provider TSEJBContainerImpl, which is
   * packaged in the jar along with test classes. This jar file also contains a
   * META-INF/services entry for this custom provider
   */
  public void customProvider() {
    TSEJBContainerImpl container = (TSEJBContainerImpl) getContainer();
    appendReason("Created EJBContainer provider ", getContainer(),
        " with init properties: ", getContainerInitProperties());

    assertEquals(null, null, container.getContext());
  }

}
