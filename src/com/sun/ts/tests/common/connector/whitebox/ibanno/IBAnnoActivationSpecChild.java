/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox.ibanno;

import java.util.*;
import javax.resource.spi.*;
import javax.resource.spi.ResourceAdapter;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import javax.validation.constraints.*;
import com.sun.ts.tests.common.connector.util.TSMessageListenerInterface;
import javax.resource.spi.ResourceAdapterAssociation;

/*
 * This extends the parent class and MUST have a different listener than the parent.
 * This must inherit any configProperty annotations from parent.
 * This will implement ResourceAdaperAssociation as part of test for
 * Connector:SPEC:282.  Since this implements ResourceAdaperAssociation, we should
 * see that the setResourceAdapter() class gets called.
 */
@Activation(messageListeners = {
    com.sun.ts.tests.common.connector.util.TSMessageListenerInterface.class })
public class IBAnnoActivationSpecChild extends IBAnnoActivationSpecParent
    implements ResourceAdapterAssociation, ActivationSpec {

  private String annoDestinationName;

  private String annoDestinationType;

  private ResourceAdapter resourceAdapter;

  /**
   * Default constructor.
   */
  public IBAnnoActivationSpecChild() {
    Debug.trace("IBAnnoActivationSpecChild.constructor");
  }

  public String getAnnoDestinationName() {
    Debug.trace("IBAnnoActivationSpecChild.getAnnoDestinationName :"
        + this.annoDestinationName);
    return this.annoDestinationName;
  }

  public void setAnnoDestinationName(String name) {
    this.annoDestinationName = name;
    Debug.trace("IBAnnoActivationSpecChild.setAnnoDestinationName :" + name);
  }

  public String getAnnoDestinationType() {
    Debug.trace("IBAnnoActivationSpecChild.getDestinationType :"
        + this.annoDestinationType);
    return this.annoDestinationType;
  }

  public void setAnnoDestinationType(String type) {
    Debug.trace("IBAnnoActivationSpecChild.setAnnoDestinationType :" + type);
    this.annoDestinationType = type;
  }

  public ResourceAdapter getResourceAdapter() {
    return this.resourceAdapter;
  }

  public void setResourceAdapter(ResourceAdapter ra) {
    String str = "IBAnnoActivationSpecChild.setResourceAdatper called";
    ConnectorStatus.getConnectorStatus().logState(str);
    Debug.trace(str);
    this.resourceAdapter = ra;
  }

  public void validate() throws InvalidPropertyException {
    Debug.trace("IBAnnoActivationSpecChild.validate called");
  }

  public void setPropName(String name) {
    propName = name;
  }

  public String getPropName() {
    return propName;
  }

}
