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

package com.sun.ts.tests.webservices12.servlet.WSMTOMSBAnnotationsTest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import javax.xml.ws.WebServiceException;
import javax.jws.WebService;

import javax.xml.ws.Holder;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;
import java.awt.Image;
import com.sun.ts.tests.jaxws.common.AttachmentHelper;
import java.net.URL;
import javax.activation.DataHandler;
import javax.xml.transform.Source;

@WebService(portName = "MTOMTestPort", serviceName = "MTOMTestService", targetNamespace = "http://mtomtestservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/MTOMTestService.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.servlet.WSMTOMSBAnnotationsTest.MTOMTest")
@BindingType(SOAPBinding.SOAP11HTTP_MTOM_BINDING)

public class MTOMTestImpl implements MTOMTest {

  public String mtomIn(
      com.sun.ts.tests.webservices12.servlet.WSMTOMSBAnnotationsTest.DataType data) {
    System.out.println("--------------------------");
    System.out.println("In mtomIn");

    String result = "";

    try {

      String docName1 = data.getDocName1();
      String docName2 = data.getDocName2();
      String docName3 = data.getDocName3();
      String docName4 = data.getDocName4();
      System.out.println("docName1=" + docName1);
      System.out.println("docName2=" + docName2);
      System.out.println("docName3=" + docName3);
      System.out.println("docName4=" + docName4);

      URL docURL1 = new URL(data.getDocUrl1());
      URL docURL2 = new URL(data.getDocUrl2());
      URL docURL3 = new URL(data.getDocUrl3());
      URL docURL4 = new URL(data.getDocUrl4());
      System.out.println("docURL1=" + docURL1.toString());
      System.out.println("docURL2=" + docURL2.toString());
      System.out.println("docURL3=" + docURL3.toString());
      System.out.println("docURL4=" + docURL4.toString());

      Source doc1 = AttachmentHelper.getSourceDoc(docURL1);
      Source doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);
      String tmpRes = AttachmentHelper.validateAttachmentData(doc1,
          data.getDoc1(), docName1);
      if (tmpRes != null) {
        result = result + tmpRes;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc2, data.getDoc2(),
          docName2);
      if (tmpRes != null) {
        result = result + tmpRes;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc3, data.getDoc3(),
          docName3);
      if (tmpRes != null) {
        result = result + tmpRes;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc4, data.getDoc4(),
          docName4);
      if (tmpRes != null) {
        result = result + tmpRes;
      }
    } catch (Exception e) {
      throw new WebServiceException(e.toString());
    }
    return result;
  }

  public void mtomInOut(Holder<String> hDocName1, Holder<String> hDocName2,
      Holder<String> hDocName3, Holder<String> hDocName4,
      Holder<String> hDocUrl1, Holder<String> hDocUrl2, Holder<String> hDocUrl3,
      Holder<String> hDocUrl4, Holder<String> hDocUrl11,
      Holder<String> hDocUrl12, Holder<String> hDocUrl13,
      Holder<String> hDocUrl14, Holder<Source> hDoc1, Holder<Source> hDoc2,
      Holder<DataHandler> hDoc3, Holder<Image> hDoc4, Holder<String> hResult) {

    System.out.println("--------------------------");
    System.out.println("In mtomInOut");
    String result = "";
    try {

      String docName1 = hDocName1.value;
      String docName2 = hDocName2.value;
      String docName3 = hDocName3.value;
      String docName4 = hDocName4.value;
      System.out.println("docName1=" + docName1);
      System.out.println("docName2=" + docName2);
      System.out.println("docName3=" + docName3);
      System.out.println("docName4=" + docName4);

      URL docURL1 = new URL(hDocUrl1.value);
      URL docURL2 = new URL(hDocUrl2.value);
      URL docURL3 = new URL(hDocUrl3.value);
      URL docURL4 = new URL(hDocUrl4.value);

      System.out.println("docURL1=" + docURL1.toString());
      System.out.println("docURL2=" + docURL2.toString());
      System.out.println("docURL3=" + docURL3.toString());
      System.out.println("docURL4=" + docURL4.toString());

      Source doc1 = AttachmentHelper.getSourceDoc(docURL1);
      Source doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);
      String tmpRes = AttachmentHelper.validateAttachmentData(doc1, hDoc1.value,
          docName1);
      if (tmpRes != null) {
        result = result + tmpRes;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc2, hDoc2.value,
          docName2);
      if (tmpRes != null) {
        result = result + tmpRes;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc3, hDoc3.value,
          docName3);
      if (tmpRes != null) {
        result = result + tmpRes;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc4, hDoc4.value,
          docName4);
      if (tmpRes != null) {
        result = result + tmpRes;
      }

      URL docURL11 = new URL(hDocUrl11.value);
      URL docURL12 = new URL(hDocUrl12.value);
      URL docURL13 = new URL(hDocUrl13.value);
      URL docURL14 = new URL(hDocUrl14.value);

      System.out.println("docURL11=" + docURL11.toString());
      System.out.println("docURL12=" + docURL12.toString());
      System.out.println("docURL13=" + docURL13.toString());
      System.out.println("docURL14=" + docURL14.toString());

      hDoc1.value = AttachmentHelper.getSourceDoc(docURL11);
      hDoc2.value = AttachmentHelper.getSourceDoc(docURL12);
      hDoc3.value = AttachmentHelper.getDataHandlerDoc(docURL13);
      hDoc4.value = AttachmentHelper.getImageDoc(docURL14);

      hResult.value = result;
    } catch (Exception e) {
      throw new WebServiceException(e.toString());
    }
  }

  public com.sun.ts.tests.webservices12.servlet.WSMTOMSBAnnotationsTest.DataType mtomOut(
      String urls) {
    System.out.println("--------------------------");
    System.out.println("In mtomOut");

    System.out.println("urls=" + urls);
    String[] tmpUrls = urls.split(",");

    for (int i = 0; i < tmpUrls.length; i++) {
      System.out.println("url[" + i + "]=" + tmpUrls[i]);
    }

    DataType d = new DataType();
    try {
      URL docURL1 = new URL(tmpUrls[0]);
      URL docURL2 = new URL(tmpUrls[1]);
      URL docURL3 = new URL(tmpUrls[2]);
      URL docURL4 = new URL(tmpUrls[3]);

      d.setDocUrl1(docURL1.toString());
      d.setDocUrl2(docURL2.toString());
      d.setDocUrl3(docURL3.toString());
      d.setDocUrl4(docURL4.toString());

      Source doc1 = AttachmentHelper.getSourceDoc(docURL1);
      Source doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);

      d.setDoc1(doc1);
      d.setDoc2(doc2);
      d.setDoc3(doc3);
      d.setDoc4(doc4);

    } catch (Exception e) {
      throw new WebServiceException(e.toString());
    }
    return d;
  }

  public com.sun.ts.tests.webservices12.servlet.WSMTOMSBAnnotationsTest.DataType3 mtomOut2(
      String urls) {
    System.out.println("--------------------------");
    System.out.println("In mtomOut2");

    System.out.println("urls=" + urls);
    String[] tmpUrls = urls.split(",");

    for (int i = 0; i < tmpUrls.length; i++) {
      System.out.println("url[" + i + "]=" + tmpUrls[i]);
    }

    DataType3 d = new DataType3();
    try {
      URL docURL = new URL(tmpUrls[0]);
      Image doc = AttachmentHelper.getImageDoc(docURL);
      d.setDoc(doc);
    } catch (Exception e) {
      throw new WebServiceException(e.toString());
    }
    return d;
  }
}
