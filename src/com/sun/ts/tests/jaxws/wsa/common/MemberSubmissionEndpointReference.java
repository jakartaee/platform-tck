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
 * $Id: MemberSubmissionEndpointReference.java 51109 2006-09-28 15:28:12Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.common;

import org.w3c.dom.Element;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import jakarta.xml.ws.EndpointReference;
import jakarta.xml.ws.WebServiceException;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "EndpointReference", namespace = MemberSubmissionEndpointReference.MSNS)
@XmlType(name = "EndpointReferenceType", namespace = MemberSubmissionEndpointReference.MSNS)
public class MemberSubmissionEndpointReference extends EndpointReference
    implements MemberSubmissionAddressingConstants {

  private final static JAXBContext msjc = MemberSubmissionEndpointReference
      .getMSJaxbContext();

  private Marshaller marshaller;

  private Unmarshaller unmarshaller = null;

  public MemberSubmissionEndpointReference() {
  }

  /**
   * construct an EPR from infoset representation
   *
   * @param source
   *          A source object containing valid XmlInfoset instance consistent
   *          with the Member Submission WS-Addressing
   * @throws jakarta.xml.ws.WebServiceException
   *           if the source does not contain a valid W3C WS-Addressing
   *           EndpointReference.
   * @throws WebServiceException
   *           if the <code>null</code> <code>source</code> value is given
   */
  public MemberSubmissionEndpointReference(Source source) {

    if (source == null)
      throw new WebServiceException(
          "Source parameter can not be null on constructor");

    try {
      if (unmarshaller == null)
        unmarshaller = MemberSubmissionEndpointReference.msjc
            .createUnmarshaller();
      MemberSubmissionEndpointReference epr = (MemberSubmissionEndpointReference) unmarshaller
          .unmarshal(source);

      this.addr = epr.addr;
      this.referenceProperties = epr.referenceProperties;
      this.referenceParameters = epr.referenceParameters;
      this.portTypeName = epr.portTypeName;
      this.serviceName = epr.serviceName;
      this.attributes = epr.attributes;
      this.elements = epr.elements;
    } catch (JAXBException e) {
      throw new WebServiceException(
          "Error unmarshalling MemberSubmissionEndpointReference ", e);
    } catch (ClassCastException e) {
      throw new WebServiceException(
          "Source did not contain MemberSubmissionEndpointReference", e);
    }
  }

  public void writeTo(Result result) {
    try {
      if (marshaller == null)
        marshaller = MemberSubmissionEndpointReference.msjc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      marshaller.marshal(this, result);
    } catch (JAXBException e) {
      throw new WebServiceException("Error marshalling W3CEndpointReference. ",
          e);
    }
  }

  private static JAXBContext getMSJaxbContext() {
    try {
      return JAXBContext.newInstance(MemberSubmissionEndpointReference.class);
    } catch (JAXBException e) {
      throw new WebServiceException(
          "Error creating JAXBContext for MemberSubmissionEndpointReference. ",
          e);
    }
  }

  @XmlElement(name = "Address", namespace = MemberSubmissionEndpointReference.MSNS)
  public Address addr;

  @XmlElement(name = "ReferenceProperties", namespace = MemberSubmissionEndpointReference.MSNS)
  public Elements referenceProperties;

  @XmlElement(name = "ReferenceParameters", namespace = MemberSubmissionEndpointReference.MSNS)
  public Elements referenceParameters;

  @XmlElement(name = "PortType", namespace = MemberSubmissionEndpointReference.MSNS)
  public AttributedQName portTypeName;

  @XmlElement(name = "ServiceName", namespace = MemberSubmissionEndpointReference.MSNS)
  public ServiceNameType serviceName;

  @XmlAnyAttribute
  public Map<QName, String> attributes;

  @XmlAnyElement
  public Elements elements;

  public static class Address {
    public Address() {
    }

    @XmlValue
    public String uri;

    @XmlAnyAttribute
    public Map<QName, String> attributes;
  }

  public static class Elements {
    public Elements() {
    }

    @XmlAnyElement
    public List<Element> elements;
  }

  public static class AttributedQName {
    public AttributedQName() {
    }

    @XmlValue
    public QName name;

    @XmlAnyAttribute
    public Map<QName, String> attributes;
  }

  public static class ServiceNameType extends AttributedQName {
    public ServiceNameType() {
    }

    @XmlAttribute(name = "PortName")
    public String portName;
  }

  protected static final String MSNS = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
}
