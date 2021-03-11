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
 * $Id$
 */

package com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest;

import jakarta.xml.ws.WebServiceException;
import jakarta.xml.soap.*;
import jakarta.activation.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.*;
import java.net.*;
import java.awt.*;

import jakarta.jws.WebService;

@WebService(portName = "SwaTestOnePort", serviceName = "WSIDLSwaTestService", targetNamespace = "http://SwaTestService.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSW2JDLSwaTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.SwaTest1")

public class SwaTestImpl1 implements SwaTest1 {
  public void getMultipleAttachments(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.InputRequestGet request,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponse> response,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2) {
    try {
      System.out.println("Enter getMultipleAttachments() ......");
      OutputResponse theResponse = new OutputResponse();
      theResponse.setMimeType1(request.getMimeType1());
      theResponse.setMimeType2(request.getMimeType2());
      theResponse.setResult("ok");
      theResponse.setReason("ok");
      DataHandler dh1 = new DataHandler(new URL(request.getUrl1()));
      DataHandler dh2 = new DataHandler(new URL(request.getUrl2()));
      attach1.value = dh1;
      attach2.value = dh2;
      response.value = theResponse;
      System.out.println("Leave getMultipleAttachments() ......");
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage());
    }
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponseString putMultipleAttachments(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.InputRequestPut request,
      jakarta.activation.DataHandler attach1,
      jakarta.activation.DataHandler attach2) {
    try {
      OutputResponseString theResponse = new OutputResponseString();
      theResponse.setMyString("ok");
      System.out.println("Enter putMultipleAttachments() ......");
      if (attach1 == null) {
        System.err.println("attach1 is null (unexpected)");
        theResponse.setMyString("not ok");
      }
      if (attach2 == null) {
        System.err.println("attach2 is null (unexpected)");
        theResponse.setMyString("not ok");
      }
      System.out.println("Leave putMultipleAttachments() ......");
      return theResponse;
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage());
    }
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponse echoMultipleAttachments(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.InputRequest request,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2) {
    try {
      System.out.println("Enter echoMultipleAttachments() ......");
      OutputResponse theResponse = new OutputResponse();
      theResponse.setMimeType1(request.getMimeType1());
      theResponse.setMimeType2(request.getMimeType2());
      theResponse.setResult("ok");
      theResponse.setReason("ok");
      if (attach1 == null || attach1.value == null) {
        System.err.println("attach1.value is null (unexpected)");
        theResponse.setReason("attach1.value is null (unexpected)");
        theResponse.setResult("not ok");
      }
      if (attach2 == null || attach2.value == null) {
        System.err.println("attach2.value is null (unexpected)");
        if (theResponse.getReason().equals("ok"))
          theResponse.setReason("attach2.value is null (unexpected)");
        else
          theResponse.setReason(
              theResponse.getReason() + "\nattach2.value is null (unexpected)");
        theResponse.setResult("not ok");
      }
      System.out.println("Leave echoMultipleAttachments() ......");
      return theResponse;
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage());
    }
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponseString echoNoAttachments(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.InputRequestString request) {
    try {
      System.out.println("Enter echoNoAttachments() ......");
      OutputResponseString theResponse = new OutputResponseString();
      theResponse.setMyString(request.getMyString());
      System.out.println("Leave echoNoAttachments() ......");
      return theResponse;
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage());
    }
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponseAll echoAllAttachmentTypes(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.VoidRequest request,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2,
      jakarta.xml.ws.Holder<javax.xml.transform.Source> attach3,
      jakarta.xml.ws.Holder<java.awt.Image> attach4,
      jakarta.xml.ws.Holder<java.awt.Image> attach5) {
    try {
      System.out.println("Enter echoAllAttachmentTypes() ......");
      OutputResponseAll theResponse = new OutputResponseAll();
      theResponse.setResult("ok");
      theResponse.setReason("ok");
      if (attach1 == null || attach1.value == null) {
        System.err.println("attach1.value is null (unexpected)");
        theResponse.setReason("attach1.value is null (unexpected)");
        theResponse.setResult("not ok");
      }
      if (attach2 == null || attach2.value == null) {
        System.err.println("attach2.value is null (unexpected)");
        if (theResponse.getReason().equals("ok"))
          theResponse.setReason("attach2.value is null (unexpected)");
        else
          theResponse.setReason(
              theResponse.getReason() + "\nattach2.value is null (unexpected)");
        theResponse.setResult("not ok");
      }
      if (attach3 == null || attach3.value == null) {
        System.err.println("attach3.value is null (unexpected)");
        if (theResponse.getReason().equals("ok"))
          theResponse.setReason("attach3.value is null (unexpected)");
        else
          theResponse.setReason(
              theResponse.getReason() + "\nattach3.value is null (unexpected)");
        theResponse.setResult("not ok");
      }
      if (attach4 == null || attach4.value == null) {
        System.err.println("attach4.value is null (unexpected)");
        if (theResponse.getReason().equals("ok"))
          theResponse.setReason("attach4.value is null (unexpected)");
        else
          theResponse.setReason(
              theResponse.getReason() + "\nattach4.value is null (unexpected)");
        theResponse.setResult("not ok");
      }
      if (attach5 == null || attach5.value == null) {
        System.err.println("attach5.value is null (unexpected)");
        if (theResponse.getReason().equals("ok"))
          theResponse.setReason("attach5.value is null (unexpected)");
        else
          theResponse.setReason(
              theResponse.getReason() + "\nattach5.value is null (unexpected)");
        theResponse.setResult("not ok");
      }
      System.out.println("Leave echoAllAttachmentTypes() ......");
      return theResponse;
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage());
    }
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponse echoAttachmentsAndThrowAFault(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.InputRequestThrowAFault request,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2)
      throws com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.MyFault {
    System.out.println("Enter echoAttachmentsAndThrowAFault() ......");
    System.out.println("Throwing back a fault [MyFault] ......");
    throw new MyFault("This is my fault", new MyFaultType());
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponse echoAttachmentsWithHeader(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.InputRequestWithHeader request,
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.MyHeader header,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2)
      throws com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.MyFault {
    System.out.println("Enter echoAttachmentsWithHeader() ......");
    if (header.getMessage().equals("do throw a fault")) {
      System.out.println("Throwing back a fault [MyFault] ......");
      throw new MyFault("This is my fault", new MyFaultType());
    }
    try {
      OutputResponse theResponse = new OutputResponse();
      theResponse.setMimeType1(request.getMimeType1());
      theResponse.setMimeType2(request.getMimeType2());
      theResponse.setResult("ok");
      theResponse.setReason("ok");
      System.out.println("Leave echoAttachmentsWithHeader() ......");
      return theResponse;
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage());
    }
  }
}
