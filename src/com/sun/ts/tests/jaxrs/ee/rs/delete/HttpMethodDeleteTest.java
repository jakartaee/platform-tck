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

package com.sun.ts.tests.jaxrs.ee.rs.delete;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path(value = "/DeleteTest")

public class HttpMethodDeleteTest {

  static String html_content = "<html>"
      + "<head><title>CTS-Delete text/html</title></head>"
      + "<body>CTS-Delete text/html</body></html>";

  @DELETE
  @Produces(value = "text/plain")
  public String getPlain() {
    return "CTS-Delete text/plain";
  }

  @DELETE
  @Produces(value = "text/html")
  public String getHtml() {
    return html_content;
  }

  @DELETE
  @Path(value = "/sub")
  @Produces(value = "text/html")
  public String getSub() {
    return html_content;
  }
}
