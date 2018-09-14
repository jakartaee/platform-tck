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

package com.sun.ts.tests.jsonb.customizedmapping.instantiation.model;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

public class SimpleCreatorPlusFieldsContainer {
  private String stringInstance;

  private Integer integerInstance;

  private float floatInstance;

  @JsonbCreator
  public SimpleCreatorPlusFieldsContainer(
      @JsonbProperty("stringInstance") String stringInstance,
      @JsonbProperty("integerInstance") Integer integerInstance) {
    this.stringInstance = "Constructor String";
    this.integerInstance = 2;
  }

  public String getStringInstance() {
    return stringInstance;
  }

  public Integer getIntegerInstance() {
    return integerInstance;
  }

  public float getFloatInstance() {
    return floatInstance;
  }

  public void setFloatInstance(float floatInstance) {
    this.floatInstance = floatInstance;
  }
}
