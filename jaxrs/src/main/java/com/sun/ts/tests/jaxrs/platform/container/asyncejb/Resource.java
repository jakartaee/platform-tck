/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.container.asyncejb;

import jakarta.ejb.Asynchronous;
import jakarta.ejb.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;

@Path("resource")
@Singleton
public class Resource {
  private long sync;

  @GET
  @Path("check")
  @Asynchronous
  public void check(@Suspended AsyncResponse asyncResponse) {
    sync = System.currentTimeMillis();
    doSomethingLongTime();
    sync = System.currentTimeMillis() - sync;
    asyncResponse.resume(sync);
  }

  // ////////////////////////////////////////////////////////////////////////

  private static void doSomethingLongTime() {
    try {
      Thread.sleep(1000L);
    } catch (Exception e) {

    }
  }
}
