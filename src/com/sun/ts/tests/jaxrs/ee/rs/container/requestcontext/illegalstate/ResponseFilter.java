/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.ee.rs.container.requestcontext.illegalstate;

import java.io.ByteArrayInputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.sun.ts.tests.jaxrs.common.impl.SecurityContextImpl;

@Provider
public class ResponseFilter extends ResponseTemplateFilter {
  public void abortWith() {
    try {
      requestContext.abortWith(Response.ok().build());
      setEntity(NOEXCEPTION);
    } catch (IllegalStateException e) {
      setEntity(ISEXCEPTION);
    }
  }

  public void setEntityStream() {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(
          NOEXCEPTION.getBytes());
      requestContext.setEntityStream(bais);
      setEntity(NOEXCEPTION);
    } catch (IllegalStateException e) {
      setEntity(ISEXCEPTION);
    }
  }

  public void setSecurityContext() {
    SecurityContext ctx = new SecurityContextImpl();
    try {
      requestContext.setSecurityContext(ctx);
      setEntity(NOEXCEPTION);
    } catch (IllegalStateException e) {
      setEntity(ISEXCEPTION);
    }
  }

}
