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

package com.sun.ts.tests.common.connector.whitebox;

import com.sun.ts.tests.common.connector.util.*;

import javax.resource.ResourceException;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.work.WorkManager;

import java.util.Vector;

/*
 * This class is used to assist in testing with assertion Connector:SPEC:245.
 * This tests the association between the resource adapter instance and the 
 * Work instance before the exection of the Work instance has been started
 */
public class WorkAndAssocImpl extends WorkImpl
    implements ResourceAdapterAssociation {
  private int count = 0;

  protected String callingClassName = "WorkAndAssocImpl";

  private ResourceAdapter resourceAdapter;

  public WorkAndAssocImpl(WorkManager wm) {
    super(wm, "WorkAndAssocImpl");
  }

  public void run() {

    // do check for setResourceAdapter call
    checkAssociation();

    try {
      ConnectorStatus.getConnectorStatus().logState("WorkAndAssocImpl.run");
      debug("WorkAndAssocImpl.run");
    } catch (Exception ex) {
    }

  }

  /*
   * @name setResourceAdapter
   * 
   * @desc sets the Resource Adapter for this work instance
   * 
   * @return
   * 
   * @exception ResourceException
   */
  public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
    count++;
    String newStr1 = "WorkAndAssocImpl setResourceAdapter " + count;
    debug(newStr1);
    ConnectorStatus.getConnectorStatus().logState(newStr1);
    this.resourceAdapter = ra;
  }

  /*
   * @name getResourceAdapter
   * 
   * @desc gets the Resource Adapter for this work instance
   * 
   * @return Object
   * 
   * @exception ResourceException
   */
  public ResourceAdapter getResourceAdapter() {
    return resourceAdapter;
  }

  /*
   * This method is used to assist in the verification process of assertion
   * Connector:SPEC:245 This method must be called befor the work instances
   * 'run' method is called. This method checks if the setResourceAdapter()
   * method was called and if so, then this method logs a message to indicate
   * that it was called prior to the 'run' method of the run method.
   */
  public void checkAssociation() {
    Vector vLog = ConnectorStatus.getConnectorStatus().getStateLogVector();
    String toCheck1 = "WorkAndAssocImpl setResourceAdapter 1";

    for (int i = 0; i < vLog.size(); i++) {
      String str = (String) vLog.elementAt(i);
      if (str.startsWith(toCheck1)) {
        String str2 = "LocalTx - association exists between RA and work";
        ConnectorStatus.getConnectorStatus().logState(str2);
        break;
      }
    }
  }

  private void debug(String str) {
    Debug.trace(str);
  }

}
