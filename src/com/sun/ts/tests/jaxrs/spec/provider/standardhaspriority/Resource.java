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

package com.sun.ts.tests.jaxrs.spec.provider.standardhaspriority;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.Source;

@Path("resource")
public class Resource {
  @Path("source")
  @POST
  public Source source(Source source) {
    return source;
  }

  @Path("jaxb")
  @POST
  public JAXBElement<String> jaxb(JAXBElement<String> jaxb) {
    return jaxb;
  }

  @Path("map")
  @POST
  public MultivaluedMap<String, String> map(
      MultivaluedMap<String, String> map) {
    return map;
  }

  @Path("character")
  @POST
  public Character character(Character character) {
    if (character != 'a')
      throw new WebApplicationException(Status.NOT_ACCEPTABLE);
    return character;
  }

  @Path("boolean")
  @POST
  public Boolean bool(Boolean bool) {
    if (bool)
      throw new WebApplicationException(Status.NOT_ACCEPTABLE);
    return false;
  }

  @Path("number")
  @POST
  public Integer number(Integer i) {
    if (i != 0)
      throw new WebApplicationException(Status.NOT_ACCEPTABLE);
    return i;
  }

}
