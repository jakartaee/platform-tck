/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.json.bind.annotation.JsonbCreator;

public class MultipleFactoryCreatorsContainer {
  private String stringInstance;

  private Integer integerInstance;

  private float floatInstance;

  private MultipleFactoryCreatorsContainer() {
  }

  @JsonbCreator
  public MultipleFactoryCreatorsContainer createInstance(String stringInstance,
      Integer integerInstance) {
    MultipleFactoryCreatorsContainer simpleFactoryCreatorContainer = new MultipleFactoryCreatorsContainer();
    simpleFactoryCreatorContainer.setStringInstance(stringInstance);
    simpleFactoryCreatorContainer.setIntegerInstance(integerInstance);
    return simpleFactoryCreatorContainer;
  }

  @JsonbCreator
  public MultipleFactoryCreatorsContainer createInstance(
      Integer integerInstance, float floatInstance) {
    MultipleFactoryCreatorsContainer simpleFactoryCreatorContainer = new MultipleFactoryCreatorsContainer();
    simpleFactoryCreatorContainer.setIntegerInstance(integerInstance);
    simpleFactoryCreatorContainer.setFloatInstance(floatInstance);
    return simpleFactoryCreatorContainer;
  }

  public String getStringInstance() {
    return stringInstance;
  }

  public void setStringInstance(String stringInstance) {
    this.stringInstance = stringInstance;
  }

  public Integer getIntegerInstance() {
    return integerInstance;
  }

  public void setIntegerInstance(Integer integerInstance) {
    this.integerInstance = integerInstance;
  }

  public float getFloatInstance() {
    return floatInstance;
  }

  public void setFloatInstance(float floatInstance) {
    this.floatInstance = floatInstance;
  }
}
