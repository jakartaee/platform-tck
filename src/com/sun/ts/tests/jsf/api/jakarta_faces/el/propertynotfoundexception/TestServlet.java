/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.jakarta_faces.el.propertynotfoundexception;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.api.jakarta_faces.el.common.BaseELExceptionTestServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import jakarta.faces.el.EvaluationException;
import jakarta.faces.el.PropertyNotFoundException;

public final class TestServlet extends BaseELExceptionTestServlet {

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    super.setExceptionClass(PropertyNotFoundException.class);
  }

  // ------------------------------------------- Test Methods ----
  // Inherited from BaseELExceptionTestServlet

}
