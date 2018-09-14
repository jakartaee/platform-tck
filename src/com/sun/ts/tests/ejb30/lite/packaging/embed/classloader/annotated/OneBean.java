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

/*
 * $Id$
 */
package com.sun.ts.tests.ejb30.lite.packaging.embed.classloader.annotated;

import javax.ejb.Singleton;
import javax.annotation.PreDestroy;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import java.sql.Connection;

@Singleton
public class OneBean extends BeanBase implements LocalIF {

  public String getName() {
    return "OneBean";
  }

  @SuppressWarnings("unused")
  @PreDestroy
  private void preDestroy() {
    // PreDestroy method is called during the processing of embeddable
    // container close
    Helper.getLogger().info("Invoking preDestroy in " + this);

    TSDbUtil tsDbUtil = new TSDbUtil(databaseURL, databaseUser,
        databasePassword, driverClassName);
    Connection connection = tsDbUtil.getConnection();
    tsDbUtil.writeToDatabase(connection, "OneBean", "PreDestroy called");
    Helper.getLogger().info("Database write successful");

    String msg = "OneBean PreDestroy called";
    // add this msg to CopyOnWriteArrayList
    arrayList.add(msg);
    Helper.getLogger().info("Added message to CopyOnWriteArrayList");
  }

}
