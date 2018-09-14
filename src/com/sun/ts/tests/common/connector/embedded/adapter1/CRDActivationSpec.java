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

package com.sun.ts.tests.common.connector.embedded.adapter1;

import java.util.*;
import javax.resource.spi.*;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.ResourceAdapter;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import javax.validation.constraints.*;
import com.sun.ts.tests.common.connector.util.TSMessageListenerInterface;

/*
 * In this ActivationSpec, we set a listener of WorkListener.
 *
 */
@Activation(messageListeners = { javax.resource.spi.work.WorkListener.class })
public class CRDActivationSpec implements ActivationSpec, java.io.Serializable {

  private String annoDestinationName;

  private String annoDestinationType;

  @ConfigProperty()
  protected String propName = "CRDConfigPropVal";

  private ResourceAdapter resourceAdapter;

  /**
   * Default constructor.
   */
  public CRDActivationSpec() {
    Debug.trace("CRDActivationSpec.constructor");
  }

  public String getAnnoDestinationName() {
    Debug.trace("CRDActivationSpec.getAnnoDestinationName :"
        + this.annoDestinationName);
    return this.annoDestinationName;
  }

  public void setAnnoDestinationName(String name) {
    this.annoDestinationName = name;
    Debug.trace("CRDActivationSpec.setAnnoDestinationName :" + name);
  }

  public String getAnnoDestinationType() {
    Debug.trace(
        "CRDActivationSpec.getDestinationType :" + this.annoDestinationType);
    return this.annoDestinationType;
  }

  public void setAnnoDestinationType(String type) {
    Debug.trace("CRDActivationSpec.setAnnoDestinationType :" + type);
    this.annoDestinationType = type;
  }

  public ResourceAdapter getResourceAdapter() {
    return this.resourceAdapter;
  }

  public void setResourceAdapter(ResourceAdapter ra) {
    Debug.trace("CRDActivationSpec.setResourceAdatper called");
    this.resourceAdapter = ra;
  }

  public void validate() throws InvalidPropertyException {
    Debug.trace("CRDActivationSpec.validate called");
  }

  public void setPropName(String name) {
    propName = name;
  }

  public String getPropName() {
    return propName;
  }

}
