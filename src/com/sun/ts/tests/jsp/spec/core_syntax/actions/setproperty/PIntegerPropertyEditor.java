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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.setproperty;

import java.beans.PropertyEditorSupport;

public class PIntegerPropertyEditor extends PropertyEditorSupport {
  public PIntegerPropertyEditor() {

  }

  /**
   *
   * @param param1
   *          a valid String-based integer value. The String value of "314" will
   *          be appended to whatever value is passed.
   * @exception java.lang.IllegalArgumentException
   *              if unable to set property
   */
  public void setAsText(String param1) throws IllegalArgumentException {
    setValue(new Integer(param1 + "314"));
  }

}// PIntegerPropertyEditor
