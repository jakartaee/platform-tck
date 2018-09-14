/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.xmloverride.resource;

import java.net.URL;

import javax.annotation.Resource;
import javax.annotation.Resource.AuthenticationType;
import javax.ejb.Stateless;

@Stateless
public class TestBean {
  private Integer overrideLookup;

  // lookup is overridden in ejb-jar.xml
  @SuppressWarnings("unused")
  @Resource(name = "overrideLookup", lookup = "java:module/env/0")
  private void setOverrideLookup(Integer i) {
    this.overrideLookup = i;
  }

  public Integer overrideLookup() {
    return overrideLookup;
  }

}
