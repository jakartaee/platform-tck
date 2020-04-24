/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: JAXBAnnotationsImpl.java 52501 2007-01-24 02:29:49Z adf $
 */

package com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.soap.SOAPBinding;
import jakarta.xml.ws.soap.Addressing;
import jakarta.xml.ws.soap.MTOM;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import jakarta.xml.bind.annotation.adapters.HexBinaryAdapter;
import jakarta.activation.DataHandler;
import java.util.List;

@WebService(name = "JAXBAnnotationsEndpoint", serviceName = "JAXBAnnotationsService", targetNamespace = "http://doclitservice.org/wsdl")
@jakarta.jws.soap.SOAPBinding(style = jakarta.jws.soap.SOAPBinding.Style.DOCUMENT, use = jakarta.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = jakarta.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
@Addressing
@MTOM
public class JAXBAnnotationsImpl {

  @WebMethod
  @XmlJavaTypeAdapter(HexBinaryAdapter.class)
  public byte[] jaxbAnnotationsTest1(
      @WebParam(name = "name") java.lang.String str,
      @WebParam(name = "xmljavatypeadapter") @XmlElement(namespace = "foo", name = "xmljavatypeadapter", required = true, nillable = true) @XmlJavaTypeAdapter(HexBinaryAdapter.class) byte[] bin) {
    return bin;
  }

  @WebMethod
  public void jaxbAnnotationsTest2(
      @WebParam(name = "name") java.lang.String name,
      @WebParam(name = "xmlmimetype") @XmlElement(namespace = "foo", name = "xmlmimetype", required = false, nillable = false) @XmlMimeType("application/octet-stream") DataHandler data) {
  }

  @WebMethod
  public void jaxbAnnotationsTest3(
      @WebParam(name = "name") java.lang.String name,
      @WebParam(name = "xmlattachmentref") @XmlElement(namespace = "foo", name = "xmlattachmentref") @XmlAttachmentRef DataHandler data) {
  }

  @WebMethod
  public void jaxbAnnotationsTest4(
      @WebParam(name = "name") java.lang.String name,
      @WebParam(name = "xmllist") @XmlElement(namespace = "foo", name = "xmllist", nillable = true) @XmlList List<String> data) {
  }

}
