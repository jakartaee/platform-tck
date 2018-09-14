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

public class MultipleCreatorsContainer {
  private String stringInstance;

  private Integer integerInstance;

  private float floatInstance;

  @JsonbCreator
  public MultipleCreatorsContainer(String stringInstance,
      Integer integerInstance) {
    this.stringInstance = stringInstance;
    this.integerInstance = integerInstance;
  }

  @JsonbCreator
  public MultipleCreatorsContainer(Integer integerInstance,
      float floatInstance) {
    this.integerInstance = integerInstance;
    this.floatInstance = floatInstance;
  }
}
