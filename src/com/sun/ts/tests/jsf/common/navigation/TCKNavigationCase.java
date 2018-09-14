/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.common.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.NavigationCase;

public class TCKNavigationCase {

  public static NavigationCase getCase(String caseName) {
    String fromViewId = null;
    String fromAction = null;
    String fromOutcome = null;
    String condition = null;
    String toViewId = null;
    boolean isRedirect = false;
    boolean isParameters = false;
    Map<String, List<java.lang.String>> parameters = null;

    if ("red".equalsIgnoreCase(caseName)) {
      fromViewId = "/stop.xhtml";
      fromAction = "#{color.result}";
      fromOutcome = "Red";
      condition = "#{'Red' == color.color}";
      toViewId = "/red.xhtml";

    } else if ("blue".equalsIgnoreCase(caseName)) {
      fromViewId = "/stop.xhtml";
      fromAction = "#{color.result}";
      fromOutcome = "Blue";
      condition = "#{'Blue' == color.color}";
      toViewId = "/blue.xhtml";

    } else if ("green".equalsIgnoreCase(caseName)) {
      ArrayList<String> al = new ArrayList<String>();
      parameters = new HashMap<String, List<java.lang.String>>();
      al.add("/red.xhtml");
      fromViewId = "/stop.xhtml";
      fromAction = "#{color.result}";
      toViewId = "/blue.xhtml";
      isRedirect = true;
      isParameters = true;
      parameters.put("id", al);

    }

    return new NavigationCase(fromViewId, fromAction, fromOutcome, condition,
        toViewId, parameters, isRedirect, isParameters);
  }

}
