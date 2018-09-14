/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.api.client.clientrequestcontext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;

public class SetEntityProvider extends ContextProvider {
  private AtomicInteger counter;

  protected Annotation anno1 = new GET() {
    @Override
    public Class<? extends Annotation> annotationType() {
      return null;
    }
  };

  protected Annotation anno2 = new POST() {
    @Override
    public Class<? extends Annotation> annotationType() {
      return null;
    }
  };

  protected Annotation[] annos = new Annotation[] { anno1, anno2 };

  protected MediaType type = MediaType.MULTIPART_FORM_DATA_TYPE;

  protected String entityName = "ENTITY";

  public SetEntityProvider(AtomicInteger counter) {
    super();
    this.counter = counter;
  }

  /**
   * This method expects the request with entity != String.class has been sent
   * Also, the mediaType is MediaType.WILDCARD_TYPE
   */
  @Override
  protected void checkFilterContext(ClientRequestContext context) throws Fault {
    Object entity;
    MediaType mtype;
    Annotation[] annotations;
    Type clz;
    switch (counter.incrementAndGet()) {
    case 1:
      TestUtil.logMsg("Counter is 1");
      // get
      entity = context.getEntity();
      mtype = context.getMediaType();
      annotations = context.getEntityAnnotations();
      clz = context.getEntityType();
      // check
      assertFault(entity != null, "there is no entity, yet");
      assertFault(!entity.toString().equals(entityName),
          "the fake entity was already set");
      assertFault(annotations == null || annotations.length == 0,
          "there are already annotations!");
      assertFault(!mtype.equals(type), "fake MediaType is already set");
      assertFault(clz != String.class, "String entity is already set");
      // set
      context.setEntity(entityName, annos, type);
      break;
    case 2:
      TestUtil.logMsg("Counter is 2");
      // get
      entity = context.getEntity();
      mtype = context.getMediaType();
      annotations = context.getEntityAnnotations();
      clz = context.getEntityType();
      // check
      assertFault(entity != null, "there is no entity set");
      assertFault(entity.toString().equals(entityName),
          "there is no fake entity set, yet");
      assertFault(annotations.length == 2,
          "the fake annotations were not set, yet");
      assertFault(mtype.equals(type), "fake MediaType was not set, yet");
      assertFault(clz == String.class, "String entity not set, yet");
      // set
      context.setEntity(entityName, annos, type);
      Response response = Response.ok().build();
      context.abortWith(response);
      break;
    }
  }

  /**
   * @param conditionTrue
   * @param message
   * @throws Fault
   *           when conditionTrue is not met with message provided
   */
  protected static void //
      assertFault(boolean conditionTrue, Object message) throws Fault {
    if (!conditionTrue) {
      throw new Fault(message.toString());
    }
  }
}
