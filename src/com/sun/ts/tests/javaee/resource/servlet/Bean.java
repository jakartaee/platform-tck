/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.javaee.resource.servlet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.mail.Session;
import javax.mail.MailSessionDefinition;
import javax.enterprise.context.ApplicationScoped;

@MailSessionDefinition(name = "java:app/env/Bean_MailSession", properties = {
    "test=Bean_MailSession" })
@ApplicationScoped
public class Bean {

  public Session getSession(String jndiName) {
    try {
      InitialContext ic = new InitialContext();
      Object obj = ic.lookup(jndiName);
      if (obj instanceof Session)
        return (Session) obj;
      else
        return null;
    } catch (NamingException nex) {
      return null;
    }
  }
}
