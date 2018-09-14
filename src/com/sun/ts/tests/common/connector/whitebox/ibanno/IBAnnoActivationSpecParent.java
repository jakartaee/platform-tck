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
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.ResourceAdapter;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import javax.validation.constraints.*;
import com.sun.ts.tests.common.connector.util.TSMessageListenerInterface;

/*
 * This class shouldnt really be used for anything.  This will be extended
 * by the child activation class and checks will be done to ensure that 
 * proper annotation processing, inheritance, etc are occurring.
 * In this parent, we set a dummy listener of WorkListener, and in our
 * subclass, we will have a different, more speciif listener.
 *
 * Also, we expect that the ConfigProperty annotation in this parent 
 * will/must be inherited by any child/sub classes.
 *
 */
@Activation(messageListeners = { javax.resource.spi.work.WorkListener.class })
public class IBAnnoActivationSpecParent
    implements ActivationSpec, java.io.Serializable {

  private String annoDestinationName;

  private String annoDestinationType;

  @ConfigProperty()
  protected String propName = "IBAnnoConfigPropVal";

  private ResourceAdapter resourceAdapter;

  /**
   * Default constructor.
   */
  public IBAnnoActivationSpecParent() {
    Debug.trace("IBAnnoActivationSpecParent.constructor");
  }

  public String getAnnoDestinationName() {
    Debug.trace("IBAnnoActivationSpecParent.getAnnoDestinationName :"
        + this.annoDestinationName);
    return this.annoDestinationName;
  }

  public void setAnnoDestinationName(String name) {
    this.annoDestinationName = name;
    Debug.trace("IBAnnoActivationSpecParent.setAnnoDestinationName :" + name);
  }

  public String getAnnoDestinationType() {
    Debug.trace("IBAnnoActivationSpecParent.getDestinationType :"
        + this.annoDestinationType);
    return this.annoDestinationType;
  }

  public void setAnnoDestinationType(String type) {
    Debug.trace("IBAnnoActivationSpecParent.setAnnoDestinationType :" + type);
    this.annoDestinationType = type;
  }

  public ResourceAdapter getResourceAdapter() {
    return this.resourceAdapter;
  }

  public void setResourceAdapter(ResourceAdapter ra) {
    Debug.trace("IBAnnoActivationSpecParent.setResourceAdatper called");
    this.resourceAdapter = ra;
  }

  public void validate() throws InvalidPropertyException {
    Debug.trace("IBAnnoActivationSpecParent.validate called");
  }

  public void setPropName(String name) {
    propName = name;
  }

  public String getPropName() {
    return propName;
  }

}
