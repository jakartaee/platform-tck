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

package com.sun.ts.tests.jaxrs.api.rs.core.linkjaxbadapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = -5902280515880564932L;

  protected static final String url = "some://where.at:port/";

  protected static final String rel = "Relation";

  protected static final String media = MediaType.APPLICATION_SVG_XML;

  protected static final String title = "XTitleX";

  protected static final String[] param_names = { "name1", "name2" };

  protected static final String[] param_vals = { "val1", "val2" };

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /*
   * @testName: marshallTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:815; JAXRS:JAVADOC:816;
   * 
   * @test_Strategy:
   */
  public byte[] marshallTest() throws Fault {
    Link link = RuntimeDelegate.getInstance().createLinkBuilder().uri(url)
        .title(title).rel(rel).type(media).param(param_names[0], param_vals[0])
        .param(param_names[1], param_vals[1]).build();
    Model model = new Model(link);

    ByteArrayOutputStream ostream = new ByteArrayOutputStream(1000);
    JAXBContext jc = null;
    Marshaller marshaller = null;
    byte[] array = null;
    try {
      jc = JAXBContext.newInstance(Model.class);
      marshaller = jc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(model, ostream);

      array = ostream.toByteArray();
      String string = new String(array, Charset.defaultCharset());
      assertContains(string, "link href=", "Marshalled Link", string,
          "does not contain expected uri reference field");
      assertContains(string, url, "Marshalled Link", string,
          " does not contain expected uri reference", url);
      assertContains(string, media, "MediaType has not been marshalled in",
          string);
      assertContains(string, title, "Title has not been marshalled in", string);
      assertContains(string, rel, "Relation has not been marshalled in",
          string);
      assertContains(string, param_names[0], "parameter name", param_names[0],
          "has not been marshalled in", string);
      assertContains(string, param_names[1], "parameter name", param_names[1],
          "has not been marshalled in", string);
      assertContains(string, param_vals[0], "parameter value", param_vals[0],
          "has not been marshalled in", string);
      assertContains(string, param_vals[1], "parameter value", param_vals[1],
          "has not been marshalled in", string);
      logMsg("Marshalled Link contains expected", string);
    } catch (JAXBException e) {
      throw new Fault(e);
    }
    return array;
  }

  /*
   * @testName: unmarshallTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:815; JAXRS:JAVADOC:816; JAXRS:JAVADOC:818;
   * 
   * @test_Strategy: Test whether a class with Link can be unmarshalled fine
   */
  public void unmarshallTest() throws Fault {
    JAXBContext jc = null;
    Unmarshaller unmarshaller = null;
    ByteArrayInputStream istream = null;
    Model unmarshalledModel = null;
    try {
      byte[] array = marshallTest();
      istream = new ByteArrayInputStream(array);

      jc = JAXBContext.newInstance(Model.class);
      unmarshaller = jc.createUnmarshaller();
      unmarshalledModel = (Model) unmarshaller.unmarshal(istream);
      Link link = unmarshalledModel.getLink();

      assertContains(link.toString(), url, "unmarshalled link", link,
          "does not contain expected url", url);
      assertContains(link.getRel(), rel, "unmarshalled link", link,
          "does not contain expected relation", rel);
      assertContains(link.getTitle(), title, "unmarshalled link", link,
          "does not contain expected title", title);
      assertContains(link.getType(), media, "unmarshalled link", link,
          "does not contain expected media type", media);
      assertTrue(link.getParams().size() > 2, "unmarshalled link", link,
          "does not contain expected parameters");
      assertContains(link.getParams().get(param_names[0]), param_vals[0],
          "unmarshalled link", link, "does not contain expected parameter",
          param_names[0], "=", param_vals[0]);
      assertContains(link.getParams().get(param_names[1]), param_vals[1],
          "unmarshalled link", link, "does not contain expected parameter",
          param_names[1], "=", param_vals[1]);
      logMsg(
          "unmarshalled Link contains expected url, title, media type, and parameters",
          link);
    } catch (JAXBException e) {
      throw new Fault(e);
    }
  }
}
