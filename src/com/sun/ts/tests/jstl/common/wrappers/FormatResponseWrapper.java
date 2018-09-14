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

package com.sun.ts.tests.jstl.common.wrappers;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/*
 * This is a simple HttpResposneWrapper to "log" calls
 * to setLocale() made by specific I18N actions.
 */

public class FormatResponseWrapper extends HttpServletResponseWrapper {

  /** Creates new FormatResponseWrapper */
  public FormatResponseWrapper(HttpServletResponse response) {
    super(response);
  }

  /*
   * public methods
   * ========================================================================
   */

  /*
   * Sets the locale of the response.
   *
   * @param locale the locale of the response
   */
  public void setLocale(Locale locale) {
    super.setHeader("setlocale", locale.toString());
    super.setLocale(locale);
  }

  /*
   * Returns the name of the charset used for the MIME body sent in this reponse
   *
   * @return a String specifying the name of the charset, for example,
   * ISO-8859-1
   */
  public String getCharacterEncoding() {
    super.setHeader("charencoding", "called");
    return super.getCharacterEncoding();
  }
}
