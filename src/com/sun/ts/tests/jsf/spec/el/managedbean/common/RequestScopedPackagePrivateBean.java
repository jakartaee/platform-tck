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

package com.sun.ts.tests.jsf.spec.el.managedbean.common;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class RequestScopedPackagePrivateBean extends ScopedBean
    implements Serializable {

  @PostConstruct
  void onPostConstruct() {
    setPostConstructProperty(
        "request package private PostConstruct method invoked");
  }

  @PreDestroy
  void onPreDestroy() {
    PreDestroyProp.setPreDestroyProperty(
        "request package private PreDestroy method invoked");
  }

  public RequestScopedPackagePrivateBean() {
    System.out.println("RequestScopedPackagePrivateBean created.");
  }
}
