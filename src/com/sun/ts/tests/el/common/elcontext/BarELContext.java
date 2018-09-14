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

package com.sun.ts.tests.el.common.elcontext;

import com.sun.ts.tests.common.el.api.resolver.BarELResolver;

import javax.el.ELResolver;
import javax.el.ELContext;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

/* This ELContext contains only a simple EL resolver used to
   resolve expressions handled by the BarELResolver.
*/

public class BarELContext extends ELContext {

  private final ELResolver elResolver;

  /*
   * Constructor.
   */
  public BarELContext() {
    this.elResolver = new BarELResolver();
  }

  public ELResolver getELResolver() {
    return (ELResolver) elResolver;
  }

  public ELContext getELContext() {
    return (ELContext) (this);
  }

  public VariableMapper getVariableMapper() {
    return null;
  }

  public FunctionMapper getFunctionMapper() {
    return null;
  }

}
