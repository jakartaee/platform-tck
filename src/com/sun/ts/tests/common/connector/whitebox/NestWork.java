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

import java.util.List;
import java.util.ArrayList;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkContextProvider;
import javax.resource.spi.work.WorkContext;
import com.sun.ts.tests.common.connector.util.*;

/*
 * this class is used to help facilitate the testing of nexted 
 * work objects/instances as well as the testing of nested contexts.
 * This class needs to be able to support testing the case of having
 * a setable context to allow for testing different spec scenarios, 
 * 
 */
public class NestWork implements Work, WorkContextProvider {

  private List<WorkContext> contextsList = new ArrayList<WorkContext>();

  private String name = "NestWork.name";

  private String description = "NestWork.description";

  public NestWork() {
    ConnectorStatus.getConnectorStatus().logState("NestWork.constructor");
  }

  @Override
  public List<WorkContext> getWorkContexts() {
    return contextsList;
  }

  public void setWorkContexts(List<WorkContext> val) {
    contextsList = val;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public void addWorkContext(WorkContext ic) {
    contextsList.add(ic);
  }

  public boolean hasContextEntry() {
    if (contextsList.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public void release() {
  }

  public void run() {
    ConnectorStatus.getConnectorStatus().logState("NestWork.run");
  }

}
