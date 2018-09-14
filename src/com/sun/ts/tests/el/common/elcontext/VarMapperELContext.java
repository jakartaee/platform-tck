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

import com.sun.ts.tests.el.common.elresolver.VariableELResolver;

import com.sun.ts.lib.util.TestUtil;

import javax.el.ELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

import java.util.Properties;

/**
 * This ELContext provides a VariableELResolver to enable the setting and
 * resolution of variables as well as a VariableMapper implementation. The path
 * to the implementation is passed to the EL Context via a property in the
 * ts.jte file.
 */
public class VarMapperELContext extends ELContext {

  private final VariableMapper varMapper;

  private final CompositeELResolver compResolver;

  /*
   * Constructor.
   */
  public VarMapperELContext(Properties testProps) {
    this.compResolver = new CompositeELResolver();
    varMapper = this.getVariableMapperImpl(testProps);
  }

  public ELResolver getELResolver() {
    ELResolver elResolver;
    this.compResolver.add(new VariableELResolver());
    elResolver = (ELResolver) compResolver;

    return elResolver;
  }

  public ELContext getELContext() {
    return (ELContext) (this);
  }

  public VariableMapper getVariableMapper() {
    return varMapper;
  }

  public FunctionMapper getFunctionMapper() {
    return null;
  }

  private Object getImplSpecificInstance(String classname) {

    Class clazz;
    Object instance = null;

    try {
      clazz = Class.forName(classname);
      instance = clazz.newInstance();
    } catch (ClassNotFoundException cnfe) {
      TestUtil.logErr("ClassNotFoundException: " + cnfe.getMessage());
    } catch (InstantiationException ie) {
      TestUtil.logErr("InstantiationException: " + ie.getMessage());
    } catch (IllegalAccessException iae) {
      TestUtil.logErr("IllegalAccessException: " + iae.getMessage());
    }
    return instance;
  }

  private VariableMapper getVariableMapperImpl(Properties testProps) {
    String implSpecificClassName = testProps.getProperty("variable.mapper");

    return (implSpecificClassName == null) ? null
        : (VariableMapper) getImplSpecificInstance(implSpecificClassName);
  }
}
