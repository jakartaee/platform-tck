/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: TestServlet.java 64808 2012-02-07 18:11:12Z dougd $
 */

package com.sun.ts.tests.jsf.api.javax_faces.event.preclearflashevent;

import java.util.HashMap;
import java.util.Map;

import javax.faces.event.PreClearFlashEvent;
import javax.faces.event.SystemEvent;

import com.sun.ts.tests.jsf.api.javax_faces.event.common.BaseSystemEventTestServlet;

public class TestServlet extends BaseSystemEventTestServlet {
  private Map<String, Object> keys = new HashMap<String, Object>();

  @Override
  protected SystemEvent createEvent(Object src) {
    keys.put("test", "test");
    return new PreClearFlashEvent(keys);
  }

} // End TestServlet
