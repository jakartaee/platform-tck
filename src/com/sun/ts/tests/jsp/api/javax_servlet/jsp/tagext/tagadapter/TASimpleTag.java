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

/*
 * @(#)TASimpleTag.java 1.2 11/07/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagadapter;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * SimpleTag instance to validate the that this SimpleTag is passed to the
 * nested Classic tag wrapped with by a TagAdapter.
 */
public class TASimpleTag extends SimpleTagSupport {

  /**
   * Default Constructor.
   */
  public TASimpleTag() {
    super();
  }

  /**
   * Invokes the JspFragment, in this case, a nested Classic Tag.
   * 
   * @throws JspException
   *           - if an unexpected error occurs
   * @throws IOException
   *           - if an I/O error occurs
   */
  public void doTag() throws JspException, IOException {
    this.getJspBody().invoke(this.getJspContext().getOut());
  }
}
