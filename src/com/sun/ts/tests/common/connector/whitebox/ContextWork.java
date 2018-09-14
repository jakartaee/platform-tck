/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

import javax.resource.spi.work.WorkContextProvider;
import javax.resource.spi.work.WorkContext;
import java.util.List;
import java.util.ArrayList;
import javax.resource.spi.work.WorkManager;

import javax.resource.spi.work.Work;
import com.sun.ts.tests.common.connector.util.ConnectorStatus;
import javax.resource.spi.work.WorkException;

/*
 * this class is used to help facilitate the testing of both nested
 * work objects/instances as well as nested (work) contexts.  In 
 * order to properly use this class to test nested work and contexts,
 * you should create an instance of this class, add a context to it
 * by using the addWorkContext() method, then add a NestedWork
 * instance to it - where the NestedWork instance will need to 
 * have its own context assigned to it BEFOR it gets added into
 * this class.
 *
 */
public class ContextWork implements Work, WorkContextProvider {

  private List<WorkContext> contextsList = new ArrayList<WorkContext>();

  private WorkManager wm;

  private NestWork nw = null;

  private String name = "ContextWork.name";

  private String description = "ContextWork.description";

  public ContextWork(WorkManager wm) {
    this.wm = wm;
    Debug.trace("WorkImpl.constructor");
  }

  public void addNestedWork(NestWork val) {
    nw = val;
  }

  public NestWork getNestWork() {
    return nw;
  }

  public WorkManager getWorkManager() {
    return wm;
  }

  @Override
  public List<WorkContext> getWorkContexts() {
    return contextsList;
  }

  public void setWorkContexts(List<WorkContext> val) {
    contextsList = val;
  }

  public void addWorkContext(WorkContext ic) {
    contextsList.add(ic);
  }

  @Override
  public void release() {
    Debug.trace("WorkImpl.release");
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public void run() {
    try {
      Debug.trace("ContextWork.run");
      if (nw != null) {
        wm.doWork(nw);
      }

      // we expect nw to be executed with no SIC and thus an unauthenticated
      // user...but what the work manager chooses to do with that is not
      // defined.
      Debug.trace(
          "ContextWork.run:  just executed wm.doWork(nw) where nw has no SIC");
      if ((nw != null) && (nw.hasContextEntry())) {
        String str = "(ContextWork) ";
        str += "It appears that Security context is being inherited from parent SIC.";
        Debug.trace(str);
      }

    } catch (WorkException we) {
      Debug.trace(
          "ContextWork.run:  got WorkException - which is fine since child had no SIC");
      if ((nw != null) && (!nw.hasContextEntry())) {
        // excellant - this is what we expected (for Connector:SPEC:305)
        // this verifies that nw did not inherit the SIC from
        // this (ie its parent) work class.
        String str = "Security Context info not inherited from parent Work";
        ConnectorStatus.getConnectorStatus().logState(str);
        Debug.trace(str);
      } else if ((nw != null) && (nw.hasContextEntry())) {
        // this verified Connector:SPEC:210 becaue we should have
        // a SIC with invalid creds that could not be authenticated
        String str = "Security Context info had invalid creds.";
        ConnectorStatus.getConnectorStatus().logState(str);
        Debug.trace(str);
      }
    }
  }

}
