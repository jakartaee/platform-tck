/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.ee.rs.beanparam.matrix.plain;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.ts.tests.jaxrs.ee.rs.Constants;
import com.sun.ts.tests.jaxrs.ee.rs.ParamTest;
import com.sun.ts.tests.jaxrs.ee.rs.beanparam.matrix.bean.MatrixBeanParamEntity;

@Path(value = "resource")
public class Resource extends ParamTest {

  @BeanParam
  MatrixBeanParamEntity field;

  @GET
  @Path("Field")
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public String field() {
    sb = new StringBuilder();
    setReturnValues(field.paramEntityWithConstructor,
        field.paramEntityWithFromString, field.paramEntityWithValueOf,
        field.setParamEntityWithFromString,
        field.sortedSetParamEntityWithFromString,
        field.listParamEntityWithFromString, FIELD);
    setReturnValues(field.inner.paramEntityWithConstructor,
        field.inner.paramEntityWithFromString,
        field.inner.paramEntityWithValueOf,
        field.inner.setParamEntityWithFromString,
        field.inner.sortedSetParamEntityWithFromString,
        field.inner.listParamEntityWithFromString, Constants.INNER + FIELD);
    return sb.toString();
  }

  @GET
  @Path("Param")
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public String stringParamHandling(@BeanParam MatrixBeanParamEntity bean) {
    sb = new StringBuilder();
    setReturnValues(bean.paramEntityWithConstructor,
        bean.paramEntityWithFromString, bean.paramEntityWithValueOf,
        bean.setParamEntityWithFromString,
        bean.sortedSetParamEntityWithFromString,
        bean.listParamEntityWithFromString, PARAM);
    setReturnValues(bean.inner.paramEntityWithConstructor,
        bean.inner.paramEntityWithFromString, bean.inner.paramEntityWithValueOf,
        bean.inner.setParamEntityWithFromString,
        bean.inner.sortedSetParamEntityWithFromString,
        bean.inner.listParamEntityWithFromString, Constants.INNER + PARAM);
    return sb.toString();
  }
}
