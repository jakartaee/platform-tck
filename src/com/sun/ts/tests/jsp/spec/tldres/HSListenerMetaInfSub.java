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

package com.sun.ts.tests.jsp.spec.tldres;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSession;

public final class HSListenerMetaInfSub implements HttpSessionListener {
  public void sessionCreated(HttpSessionEvent event) {
    HttpSession session = event.getSession();
    session.setAttribute("session.created.meta.inf.sub",
        "session created meta inf sub.");
  }

  public void sessionDestroyed(HttpSessionEvent event) {
    HttpSession session = event.getSession();
    session.setAttribute("session.destroyed.meta.inf.sub",
        "session about to be destroyed meta inf sub.");
  }
}
