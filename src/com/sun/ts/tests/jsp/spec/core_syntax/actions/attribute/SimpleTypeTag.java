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
 * $Id$
 */

package com.sun.ts.tests.jsp.spec.core_syntax.actions.attribute;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class SimpleTypeTag extends SimpleTagSupport {
  private static final String FAIL_MSG = "Test FAILED.";

  private Boolean _boolean = null;

  private Character _character = null;

  private Short _short = null;

  private Integer _integer = null;

  private Long _long = null;

  private Float _float = null;

  private Double _double = null;

  private String _string = null;

  public void setString(String _string) {
    this._string = _string;
  }

  public void setBoolean(Boolean _boolean) {
    this._boolean = _boolean;
  }

  public void setCharacter(Character _character) {
    this._character = _character;
  }

  public void setShort(Short _short) {
    this._short = _short;
  }

  public void setInteger(Integer _integer) {
    this._integer = _integer;
  }

  public void setLong(Long _long) {
    this._long = _long;
  }

  public void setFloat(Float _float) {
    this._float = _float;
  }

  public void setDouble(Double _double) {
    this._double = _double;
  }

  public void doTag() throws JspException {
    try {
      JspWriter out = getJspContext().getOut();

      if (_boolean == null) {
        out.println(FAIL_MSG + " boolean value not set.");

      } else if (_character == null) {
        out.println(FAIL_MSG + "  character value not set.");

      } else if (_short == null) {
        out.println(FAIL_MSG + "  short value not set.");

      } else if (_integer == null) {
        out.println(FAIL_MSG + "  integer value not set.");

      } else if (_long == null) {
        out.println(FAIL_MSG + "  long value not set.");

      } else if (_float == null) {
        out.println(FAIL_MSG + "  float value not set.");

      } else if (_double == null) {
        out.println(FAIL_MSG + "  double value not set.");

      } else if (_string == null) {
        out.println(FAIL_MSG + "  string value not set.");

      } else {
        out.println("Test PASSED");
      }

    } catch (IOException ioe) {
      throw new JspException(FAIL_MSG + " Unexpected IOException!", ioe);
    }
  }

}
