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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.common.tags;

import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.PageContext;

public class ConfigTag extends TagSupport {

  /**
   * Maps to Config.FMT_LOCALE
   */
  private static final String LOCALE = "locale";

  /**
   * Maps to Config.FMT_FALLBACK_LOCALE
   */
  private static final String FALLBACK_LOCALE = "fallback";

  /**
   * Maps to Config.FMT_LOCALIZATION_CONTEXT
   */
  private static final String LOCALE_CTX = "localectx";

  /**
   * Maps to Config.FMT_TIME_ZONE
   */
  private static final String TIMEZONE = "timezone";

  /**
   * Maps to Config.SQL_DATA_SOURCE
   */
  private static final String DATASOURCE = "datasource";

  /**
   * Maps to Config.SQL_MAX_ROWS
   */
  private static final String MAX_ROW = "maxrows";

  /**
   * Page scope
   */
  private static final String PAGE = "page";

  /**
   * Request scope
   */
  private static final String REQUEST = "request";

  /**
   * session scope
   */
  private static final String SESSION = "session";

  /**
   * Application scope
   */
  private static final String APPLICATION = "application";

  /**
   * Set operation
   */
  private static final String SET = "set";

  /**
   * Get operation
   */
  private static final String GET = "get";

  /**
   * Remove operation
   */
  private static final String REMOVE = "remove";

  /**
   * Configuration variable to set.
   */
  private String _configVar = null;

  /**
   * Scope to set the variable.
   */
  private String _scope = PAGE;

  /**
   * Value of configuration variable.
   */
  private Object _value = null;

  /**
   * Operation to perform: set, get, or remove
   */
  private String _operation = null;

  /**
   * Variable name to export result of get operation to.
   */
  private String _var = null;

  /** Creates new ConfigTag */
  public ConfigTag() {
    super();
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Sets scope.
   */
  public void setScope(String scope) {
    _scope = scope;
  }

  /**
   * Sets the operation.
   */
  public void setOp(String op) {
    _operation = op;
  }

  /**
   * Sets the name of the var to export.
   */
  public void setVar(String var) {
    _var = var;
  }

  /**
   * Sets the name of the configuration variable.
   */
  public void setConfigVar(String configVar) {
    _configVar = configVar;
  }

  /**
   * Sets the value of the configuration variable.
   */
  public void setValue(Object value) {
    _value = value;
  }

  /**
   * <pre>
   * Sets or gets one of the configuration variables as defined by the
   * javax.servlet.jsp.jstl.Config class, to the scope specified by the action.
   *
   * @return EVAL_PATH
   */
  public int doEndTag() throws JspException {
    String name = null;
    if (_configVar.equals(LOCALE)) {
      name = Config.FMT_LOCALE;
    } else if (_configVar.equals(FALLBACK_LOCALE)) {
      name = Config.FMT_FALLBACK_LOCALE;
    } else if (_configVar.equals(LOCALE_CTX)) {
      name = Config.FMT_LOCALIZATION_CONTEXT;
    } else if (_configVar.equals(TIMEZONE)) {
      name = Config.FMT_TIME_ZONE;
    } else if (_configVar.equals(DATASOURCE)) {
      name = Config.SQL_DATA_SOURCE;
    } else if (_configVar.equals(MAX_ROW)) {
      name = Config.SQL_MAX_ROWS;
    } else {
      throw new IllegalArgumentException(
          "[ERROR] Config variable: " + _configVar + " is invalid.");
    }

    if (_operation.equals(GET)) {
      Object o = Config.get(pageContext, name, getScope(_scope));
      if (o != null) {
        pageContext.setAttribute(_var, o);
      }
    } else if (_operation.equals(SET)) {
      Config.set(pageContext, name, _value, getScope(_scope));
    } else if (_operation.equals(REMOVE)) {
      Config.remove(pageContext, name, getScope(_scope));
    } else {
      throw new IllegalArgumentException(
          "[Error] Invalid operation: " + _operation);
    }
    return EVAL_PAGE;
  }

  /**
   * Releases tag state.
   */
  public void release() {
    _configVar = null;
    _value = null;
    _scope = PAGE;
    _var = null;
    _operation = null;

  }

  /*
   * private methods
   * ========================================================================
   */

  /**
   * Returns the int value of the requested scope.
   * 
   * @return int scope
   */
  private int getScope(String scopeName) throws JspException {
    if (scopeName.equals(PAGE)) {
      return PageContext.PAGE_SCOPE;
    } else if (scopeName.equals(REQUEST)) {
      return PageContext.REQUEST_SCOPE;
    } else if (scopeName.equals(SESSION)) {
      return PageContext.SESSION_SCOPE;
    } else if (scopeName.equals(APPLICATION)) {
      return PageContext.APPLICATION_SCOPE;
    } else {
      throw new IllegalArgumentException(
          "[Error] Scope: " + _scope + " is invalid.");
    }
  }

}
