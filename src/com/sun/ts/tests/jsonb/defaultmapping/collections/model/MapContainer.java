/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonb.defaultmapping.collections.model;

import com.sun.ts.tests.jsonb.TypeContainer;

import java.util.Map;

public class MapContainer implements TypeContainer<Map<String, String>> {
  private Map<String, String> instance;

  @Override
  public Map<String, String> getInstance() {
    return instance;
  }

  @Override
  public void setInstance(Map<String, String> instance) {
    this.instance = instance;
  }
}
