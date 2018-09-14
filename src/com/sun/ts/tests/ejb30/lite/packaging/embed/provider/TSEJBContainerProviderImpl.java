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

import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.embeddable.EJBContainer;
import javax.ejb.spi.EJBContainerProvider;

public final class TSEJBContainerProviderImpl implements EJBContainerProvider {

  /*
   * (non-Javadoc)
   * 
   * @see javax.ejb.spi.EJBContainerProvider#createEJBContainer(java.util.Map)
   */
  public EJBContainer createEJBContainer(Map<?, ?> properties)
      throws EJBException {
    if (properties == null || properties.get(EJBContainer.PROVIDER) == null
        || properties.get(EJBContainer.PROVIDER)
            .equals(TSEJBContainerImpl.class.getName())) {
      return new TSEJBContainerImpl();
    }
    return null;
  }

}
