/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.websocket.Extension;

public class TCKExtension implements Extension {

  private final String name;

  private final List<Parameter> parameters;

  public TCKExtension(String name) {
    this(name, null);
  }

  public TCKExtension(String name, List<Parameter> parameters) {
    if (name == null || name.length() == 0) {
      throw new IllegalArgumentException();
    }

    this.name = name;
    if (parameters != null) {
      this.parameters = Collections
          .unmodifiableList(new ArrayList<>(parameters));
    } else {
      this.parameters = Collections
          .unmodifiableList(Collections.<Parameter> emptyList());
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<Parameter> getParameters() {
    return parameters;
  }

  public static class TCKParameter implements Parameter {

    private final String name;

    private final String value;

    /**
     * Create {@link Parameter} with name and value.
     *
     * @param name
     *          parameter name.
     * @param value
     *          parameter value.
     */
    public TCKParameter(String name, String value) {
      this.name = name;
      this.value = value;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getValue() {
      return value;
    }
  }
}
