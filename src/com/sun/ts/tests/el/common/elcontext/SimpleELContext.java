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

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.elresolver.EmployeeELResolver;
import com.sun.ts.tests.el.common.util.ResolverType;
import com.sun.ts.tests.el.common.elresolver.VariableELResolver;
import com.sun.ts.tests.el.common.elresolver.VectELResolver;

import javax.el.ELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

/**
 * A basic ELContext that meets the needs of most EL tests. The no-args
 * constructor builds the ELContext from a VariableELResolver that enables the
 * setting and resolution of variables when base is null. The single- argument
 * constructor allows the user to build the ELContext from any ELResolver. This
 * ELContext is "simple" because no VariableMapper or FunctionMapper are used.
 */
public class SimpleELContext extends ELContext {

  private static final String NLINE = System.getProperty("line.separator");

  private final CompositeELResolver compResolver;

  private ELResolver myResolver;

  private VariableELResolver varResolver;

  // Custom ELResolvers.
  public enum Resolver {

    EmployeeELResolver, VariableELResolver, VectELResolver
  };

  /*
   * Default Constructor.
   */
  public SimpleELContext() {
    this.compResolver = new CompositeELResolver();
  }

  /*
   * Constructor with an ELResolver given through an enum.
   */
  public SimpleELContext(ResolverType resolverType) {
    this.myResolver = this.getMyResolver(resolverType);
    this.compResolver = new CompositeELResolver();
  }

  public ELResolver getELResolver() {
    ELResolver elResolver;

    if (myResolver == null) {
      varResolver = new VariableELResolver();
      compResolver.add(varResolver);
    } else {
      if (myResolver instanceof VariableELResolver) {
        varResolver = (VariableELResolver) myResolver;
      } else {
        varResolver = null;
      }
      compResolver.add(myResolver);
    }

    elResolver = (ELResolver) compResolver;

    return elResolver;
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

  // This method is not required by the ELContext API. It is for
  // removing all entries from the the VariableELResolver's
  // static hashtable between test invocations.
  public void cleanup() {
    if (varResolver != null) {
      varResolver.cleanup();
    }
  }

  // ------------------------------- Private Methods
  // ------------------------------
  private ELResolver getMyResolver(ResolverType enumResolver) {

    switch (enumResolver) {
    case EMPLOYEE_ELRESOLVER:
      myResolver = new EmployeeELResolver();
      TestUtil.logTrace("Setting ELResolver == EmployeeELResolver");
      break;

    case VARIABLE_ELRESOLVER:
      myResolver = new VariableELResolver();
      TestUtil.logTrace("Setting ELResolver == VariableELResolver");
      break;

    case VECT_ELRESOLVER:
      myResolver = new VectELResolver();
      TestUtil.logTrace("Setting ELResolver == VectELResolver");
      break;

    default:
      TestUtil.logTrace(
          "Unknown ELResolver! " + enumResolver + " trying to use default"
              + NLINE + "Setting" + " ELResolver == VariableELResolver");
      break;
    }

    return myResolver;
  }
}
