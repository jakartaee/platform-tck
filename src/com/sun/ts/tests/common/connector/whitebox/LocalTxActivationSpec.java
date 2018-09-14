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

package com.sun.ts.tests.common.connector.whitebox;

import javax.resource.spi.ActivationSpec;
import javax.resource.spi.ResourceAdapter;
import com.sun.ts.tests.common.connector.util.*;

public class LocalTxActivationSpec
    implements ActivationSpec, java.io.Serializable {

  private String destinationName;

  private String destinationType;

  private ResourceAdapter resourceAdapter;

  private int counter = 0;

  /**
   * Default constructor.
   */
  public LocalTxActivationSpec() {

  }

  public String getDestinationName() {
    System.out.println(
        "LocalTxActivationSpec.getDestinationName :" + this.destinationName);
    return this.destinationName;
  }

  public void setDestinationName(String name) {
    this.destinationName = name;
    System.out.println("LocalTxActivationSpec.setDestinationName :" + name);
  }

  public String getDestinationType() {
    System.out.println(
        "LocalTxActivationSpec.getDestinationType :" + this.destinationType);
    return this.destinationType;
  }

  public void setDestinationType(String type) {
    System.out.println("LocalTxActivationSpec.setDestinationType :" + type);
    this.destinationType = type;
  }

  @Override
  public ResourceAdapter getResourceAdapter() {
    return this.resourceAdapter;
  }

  @Override
  public void setResourceAdapter(ResourceAdapter ra) {
    counter++;
    ConnectorStatus.getConnectorStatus()
        .logState("LocalTxActivationSpec setResourceAdapter " + counter);
    System.out.println("LocalTxActivationSpec.setResourceAdatper called");
    this.resourceAdapter = ra;
  }

  @Override
  public void validate() {
    throw new UnsupportedOperationException();
  }

  public void setCounter(int val) {
    this.counter = val;
  }

  public int getCounter() {
    return this.counter;
  }

}
