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

package com.sun.ts.tests.jaxrs.spec.provider.standard;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import javax.activation.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.Source;

import com.sun.ts.tests.jaxrs.common.impl.StringStreamingOutput;

@Path("resource")
public class Resource {
  @Context
  HttpHeaders headers;

  @Path("bytearray")
  @POST
  public byte[] bytearray(byte[] bytes) {
    return bytes;
  }

  @Path("bytearraysvg")
  @Produces(MediaType.APPLICATION_SVG_XML)
  @Consumes(MediaType.APPLICATION_SVG_XML)
  @POST
  public byte[] bytearraySvg(byte[] bytes) {
    return bytes;
  }

  @Path("string")
  @POST
  public String string(String string) {
    return string;
  }

  @Path("stringsvg")
  @Produces(MediaType.APPLICATION_SVG_XML)
  @Consumes(MediaType.APPLICATION_SVG_XML)
  @POST
  public String stringsvg(String string) {
    return string;
  }

  @Path("inputstream")
  @POST
  public InputStream inputstream(InputStream inputstream) {
    return inputstream;
  }

  @Path("inputstreamsvg")
  @Produces(MediaType.APPLICATION_SVG_XML)
  @Consumes(MediaType.APPLICATION_SVG_XML)
  @POST
  public InputStream inputstreamsvg(InputStream inputstream) {
    return inputstream;
  }

  @Path("reader")
  @POST
  public Reader reader(Reader reader) {
    return reader;
  }

  @Path("readersvg")
  @Produces(MediaType.APPLICATION_SVG_XML)
  @Consumes(MediaType.APPLICATION_SVG_XML)
  @POST
  public Reader readersvg(Reader reader) {
    return reader;
  }

  @Path("file")
  @POST
  public File file(File file) {
    return file;
  }

  @Path("filesvg")
  @Produces(MediaType.APPLICATION_SVG_XML)
  @Consumes(MediaType.APPLICATION_SVG_XML)
  @POST
  public File filesvg(File file) {
    return file;
  }

  @Path("datasource")
  @POST
  public DataSource datasource(DataSource datasource) {
    return datasource;
  }

  @Path("datasourcesvg")
  @Produces(MediaType.APPLICATION_SVG_XML)
  @Consumes(MediaType.APPLICATION_SVG_XML)
  @POST
  public DataSource datasourcesvg(DataSource datasource) {
    return datasource;
  }

  @Path("streamingoutput")
  @POST
  public StreamingOutput streamingoutput(String streamingoutput) {
    return new StringStreamingOutput(streamingoutput);
  }

  @Path("streamingoutputsvg")
  @Produces(MediaType.APPLICATION_SVG_XML)
  @Consumes(MediaType.APPLICATION_SVG_XML)
  @POST
  public StreamingOutput datasourcesvg(String streamingoutput) {
    return new StringStreamingOutput(streamingoutput);
  }

  @Path("source")
  @POST
  public Response source(Source source) {
    MediaType media = headers.getMediaType();
    return Response.ok(source).type(media).build();
  }

  @Path("jaxb")
  @POST
  public Response jaxb(JAXBElement<String> jaxb) {
    MediaType media = headers.getMediaType();
    return Response.ok(jaxb).type(media).build();
  }
}
