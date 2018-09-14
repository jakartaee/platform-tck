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
package com.sun.ts.tests.jsf.common.factories;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;

public class TCKContextFactoryFour extends TCKDecoratedContextFactory {

  public TCKContextFactoryFour(FacesContextFactory facesContextFactory) {
    super.fcf = facesContextFactory;
  }

  @Override
  public FacesContext getFacesContext(Object arg0, Object arg1, Object arg2,
      Lifecycle arg3) throws FacesException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public FacesContextFactory getWrapped() {
    // For testing purposes when this method is called return itself.
    return this;
  }

}
