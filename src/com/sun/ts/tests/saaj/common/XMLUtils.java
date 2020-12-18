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

package com.sun.ts.tests.saaj.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.ts.lib.util.TestUtil;

import jakarta.xml.soap.SOAPConstants;

public final class XMLUtils {
  private static TransformerFactory tfactory = null;

  private static DocumentBuilderFactory dbfactory = null;

  private static DocumentBuilder builder = null;

  private static Transformer transformer = null;

  private static Document document = null;

  private static Element rootElement = null;

  private static Element faultElement = null;

  private static int spaces = 0;

  private static String spacesString = "";

  private static boolean tckmode = true;

  private static boolean validating = false;

  private static boolean nsaware = false;

  private static boolean valid = true;

  private static String prefix = null;

  public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

  public static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

  public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

  public static void main(String[] args) {
    String usage = "java XMLUtils [-v -n] <file> <schema>";
    String filename = null;
    String schema = null;
    tckmode = false;
    boolean errors = false;
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-v"))
        validating = true;
      else if (args[i].equals("-n"))
        nsaware = true;
      else {
        filename = args[i];
        if (args[i + 1] != null) {
          schema = args[i + 1];
          ++i;
        }
      }
    }
    if (filename == null) {
      System.err.println(usage);
      System.exit(1);
    }
    String data = "";
    try {
      System.out.println("validating=" + validating);
      System.out.println("namespaceaware=" + nsaware);
      System.out.println("file=" + filename);
      System.out.println("schema=" + schema);
      BufferedReader in = new BufferedReader(new FileReader(filename));
      String line = "";
      while ((line = in.readLine()) != null) {
        data += line;
      }
      in.close();
      System.out.println("data=" + data);
      dbfactory = DocumentBuilderFactory.newInstance();
      if (validating) {
        dbfactory.setValidating(validating);
        if (nsaware)
          dbfactory.setNamespaceAware(nsaware);
        dbfactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        dbfactory.setAttribute(JAXP_SCHEMA_SOURCE, new File(schema));
      }
      builder = dbfactory.newDocumentBuilder();
      if (validating) {
        builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
          public void fatalError(SAXParseException e) throws SAXException {
          }

          public void error(SAXParseException e) throws SAXParseException {
            throw e;
          }

          public void warning(SAXParseException e) throws SAXParseException {
            if (tckmode) {
              TestUtil.logMsg("** Warning, line " + e.getLineNumber() + ", uri "
                  + e.getSystemId());
              TestUtil.logMsg("   " + e.getMessage());
            } else {
              System.out.println("** Warning, line " + e.getLineNumber()
                  + ", uri " + e.getSystemId());
              System.out.println("   " + e.getMessage());
            }
          }
        });
      }
      document = builder.parse(new InputSource(new StringReader(data)));
      rootElement = document.getDocumentElement();
      NodeList nodes = document.getElementsByTagName("Fault");
      if (nodes.getLength() == 0)
        nodes = document.getElementsByTagName(prefix + ":Fault");
      Node node = nodes.item(0);
      faultElement = (Element) node;
      if (!errors)
        XmlDumpDOMNodes(XMLUtils.getRootElement());
    } catch (SAXParseException e) {
      if (tckmode) {
        TestUtil.logErr("SAXParseException");
        TestUtil.logErr("  " + e.getMessage());
      } else {
        System.err.println("SAXParseException");
        System.err.println("  " + e.getMessage());
      }
      Throwable x = e;
      if (e.getException() != null) {
        x = e.getException();
      }
      if (!tckmode)
        x.printStackTrace();
      errors = true;
    } catch (SAXException e) {
      if (tckmode) {
        TestUtil.logErr("SAXException");
        TestUtil.logErr("  " + e.getMessage());
      } else {
        System.err.println("SAXException");
        System.err.println("  " + e.getMessage());
      }
      Throwable x = e;
      if (e.getException() != null) {
        x = e.getException();
      }
      if (!tckmode)
        x.printStackTrace();
      errors = true;
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("Exception: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public static Element getRootElement() {
    return rootElement;
  }

  public static Element getFaultElement() {
    return faultElement;
  }

  public static void setValidating(boolean b) {
    validating = b;
  }

  public static void setPrefix(String s) {
    prefix = s;
  }

  public static void setNamespaceAware(boolean b) {
    nsaware = b;
  }

  public static boolean XmlDOMSetup() {
    boolean errors = false;
    try {
      dbfactory = DocumentBuilderFactory.newInstance();
      dbfactory.setNamespaceAware(nsaware);
      dbfactory.setValidating(validating);
      if (validating && nsaware) {
        dbfactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11))
          dbfactory.setAttribute(JAXP_SCHEMA_SOURCE,
              SOAPConstants.URI_NS_SOAP_ENVELOPE);
        else
          dbfactory.setAttribute(JAXP_SCHEMA_SOURCE,
              SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
      }
      builder = dbfactory.newDocumentBuilder();
      if (validating) {
        builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
          public void fatalError(SAXParseException e) throws SAXException {
          }

          public void error(SAXParseException e) throws SAXParseException {
            throw e;
          }

          public void warning(SAXParseException e) throws SAXParseException {
            if (tckmode) {
              TestUtil.logMsg("** Warning, line " + e.getLineNumber() + ", uri "
                  + e.getSystemId());
              TestUtil.logMsg("   " + e.getMessage());
            } else {
              System.out.println("** Warning, line " + e.getLineNumber()
                  + ", uri " + e.getSystemId());
              System.out.println("   " + e.getMessage());
            }
          }
        });
      }
      tfactory = TransformerFactory.newInstance();
      transformer = tfactory.newTransformer();
    } catch (TransformerConfigurationException e) {
      if (tckmode) {
        TestUtil.logErr("TransformerConfigurationException");
        TestUtil.logErr("  " + e.getMessage());
      } else {
        System.err.println("TransformerConfigurationException");
        System.err.println("  " + e.getMessage());
      }
      Throwable x = e;
      if (e.getException() != null)
        x = e.getException();
      if (!tckmode)
        x.printStackTrace();
      errors = true;
    } catch (Exception e) {
      if (tckmode) {
        TestUtil.logErr("Exception");
        TestUtil.logErr("  " + e.getMessage());
        TestUtil.printStackTrace(e);
      } else {
        System.err.println("Exception");
        System.err.println("  " + e.getMessage());
        e.printStackTrace();
      }
      errors = true;
    }
    return errors;
  }

  public static boolean XmlDOMParse(String data) {
    boolean errors = false;
    try {
      document = builder.parse(new InputSource(new StringReader(data)));
      rootElement = document.getDocumentElement();
      NodeList nodes = document.getElementsByTagName("Fault");
      if (nodes.getLength() == 0)
        nodes = document.getElementsByTagName(prefix + ":Fault");
      Node node = nodes.item(0);
      faultElement = (Element) node;
    } catch (SAXParseException e) {
      if (tckmode) {
        TestUtil.logErr("SAXParseException");
        TestUtil.logErr("  " + e.getMessage());
      } else {
        System.err.println("SAXParseException");
        System.err.println("  " + e.getMessage());
      }
      Throwable x = e;
      if (e.getException() != null) {
        x = e.getException();
      }
      if (!tckmode)
        x.printStackTrace();
      errors = true;
    } catch (SAXException e) {
      if (tckmode) {
        TestUtil.logErr("SAXException");
        TestUtil.logErr("  " + e.getMessage());
      } else {
        System.err.println("SAXException");
        System.err.println("  " + e.getMessage());
      }
      Throwable x = e;
      if (e.getException() != null) {
        x = e.getException();
      }
      if (!tckmode)
        x.printStackTrace();
      errors = true;
    } catch (IOException e) {
      if (tckmode) {
        TestUtil.logErr("IOException");
        TestUtil.logErr("  " + e.getMessage());
        TestUtil.printStackTrace(e);
      } else {
        System.err.println("IOException");
        System.err.println("  " + e.getMessage());
        e.printStackTrace();
      }
      errors = true;
    } catch (Exception e) {
      if (tckmode) {
        TestUtil.logErr("Exception");
        TestUtil.logErr("  " + e.getMessage());
        TestUtil.printStackTrace(e);
      } else {
        System.err.println("Exception");
        System.err.println("  " + e.getMessage());
        e.printStackTrace();
      }
      errors = true;
    }
    return errors;
  }

  public static boolean XmlDOMParse(File f) {
    boolean errors = false;
    try {
      document = builder.parse(f);
      rootElement = document.getDocumentElement();
    } catch (SAXParseException e) {
      if (tckmode) {
        TestUtil.logErr("SAXParseException");
        TestUtil.logErr("  " + e.getMessage());
      } else {
        System.err.println("SAXParseException");
        System.err.println("  " + e.getMessage());
      }
      Throwable x = e;
      if (e.getException() != null) {
        x = e.getException();
      }
      if (!tckmode)
        x.printStackTrace();
      errors = true;
    } catch (SAXException e) {
      if (tckmode) {
        TestUtil.logErr("SAXException");
        TestUtil.logErr("  " + e.getMessage());
      } else {
        System.err.println("SAXException");
        System.err.println("  " + e.getMessage());
      }
      Throwable x = e;
      if (e.getException() != null) {
        x = e.getException();
      }
      if (!tckmode)
        x.printStackTrace();
      errors = true;
    } catch (IOException e) {
      if (tckmode) {
        TestUtil.logErr("IOException");
        TestUtil.logErr("  " + e.getMessage());
        TestUtil.printStackTrace(e);
      } else {
        System.err.println("IOException");
        System.err.println("  " + e.getMessage());
        e.printStackTrace();
      }
      errors = true;
    } catch (Exception e) {
      if (tckmode) {
        TestUtil.logErr("Exception");
        TestUtil.logErr("  " + e.getMessage());
        TestUtil.printStackTrace(e);
      } else {
        System.err.println("Exception");
        System.err.println("  " + e.getMessage());
        e.printStackTrace();
      }
      errors = true;
    }
    return errors;
  }

  public static boolean XmlDOMTransform() {
    boolean errors = false;
    try {
      DOMSource source = new DOMSource(document);
      StreamResult result = new StreamResult(System.out);
      transformer.transform(source, result);
    } catch (TransformerConfigurationException e) {
      if (tckmode) {
        TestUtil.logErr("TransformerConfigurationException");
        TestUtil.logErr("  " + e.getMessage());
      } else {
        System.err.println("TransformerConfigurationException");
        System.err.println("  " + e.getMessage());
      }
      Throwable x = e;
      if (e.getException() != null)
        x = e.getException();
      if (!tckmode)
        x.printStackTrace();
      errors = true;
    } catch (TransformerException e) {
      if (tckmode) {
        TestUtil.logErr("TransformerException");
        TestUtil.logErr("  " + e.getMessage());
      } else {
        System.err.println("TransformerException");
        System.err.println("  " + e.getMessage());
      }
      Throwable x = e;
      if (e.getException() != null) {
        x = e.getException();
      }
      if (!tckmode)
        x.printStackTrace();
      errors = true;
    } catch (Exception e) {
      if (tckmode) {
        TestUtil.logErr("Exception");
        TestUtil.logErr("  " + e.getMessage());
        TestUtil.printStackTrace(e);
      } else {
        System.err.println("Exception");
        System.err.println("  " + e.getMessage());
        e.printStackTrace();
      }
      errors = true;
    }
    return errors;
  }

  private static void setSpaces() {
    spacesString = "";
    for (int i = 0; i < spaces; i++)
      spacesString = spacesString + " ";
  }

  private static String getText(Node node) {
    String result = "";
    result = node.getNodeValue();
    if (result == null)
      result = "";
    result = result.trim();
    return result;
  }

  private static void processAttributes(Node root) {
    NamedNodeMap attribs = root.getAttributes();
    if (attribs != null) {
      for (int i = 0; i < attribs.getLength(); i++) {
        Node attnode = attribs.item(i);
        String attName = attnode.getNodeName();
        String attValue = attnode.getNodeValue();
        setSpaces();
        if (tckmode)
          TestUtil.logMsg(spacesString + "<Attribute>" + attName + "="
              + attValue + "</Attribute>");
        else
          System.out.print(spacesString + "<Attribute>" + attName + "="
              + attValue + "</Attribute>\n");
      }
    }
  }

  private static boolean hasAttributes(Node root) {
    NamedNodeMap attribs = root.getAttributes();
    if (attribs == null || attribs.getLength() == 0)
      return false;
    else
      return true;
  }

  public static void XmlDumpDOMNodes(Element element) {
    spaces = 0;
    if (tckmode)
      TestUtil.logMsg("Begin Dumping DOM Nodes");
    else
      System.out.println("Begin Dumping DOM Nodes");
    String rootNodeName = element.getNodeName();
    if (tckmode)
      TestUtil.logMsg("<RootElement>" + rootNodeName + "</RootElement>");
    else
      System.out.print("<RootElement>" + rootNodeName + "</RootElement>\n");
    spaces += 2;
    XmlDumpDOMNodes_(element);
    if (tckmode)
      TestUtil.logMsg("Done Dumping DOM Nodes");
    else
      System.out.println("Done Dumping DOM Nodes");
  }

  public static void XmlDumpDOMNodes(Node node) {
    spaces = 0;
    if (tckmode)
      TestUtil.logMsg("Begin Dumping DOM Nodes");
    else
      System.out.println("Begin Dumping DOM Nodes");
    String rootNodeName = node.getNodeName();
    if (tckmode)
      TestUtil.logMsg("<RootElement>" + rootNodeName + "</RootElement>");
    else
      System.out.print("<RootElement>" + rootNodeName + "</RootElement>\n");
    spaces += 2;
    XmlDumpDOMNodes_((Element) node);
    if (tckmode)
      TestUtil.logMsg("Done Dumping DOM Nodes");
    else
      System.out.println("Done Dumping DOM Nodes");
  }

  public static void XmlDumpDOMNodes_(Element element) {
    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      String nodeName = node.getNodeName();
      String nodeValue = node.getNodeValue();
      short nodeType = node.getNodeType();
      switch (nodeType) {
      case Node.ATTRIBUTE_NODE:
        setSpaces();
        if (tckmode)
          TestUtil.logMsg(spacesString + "<Attribute>" + nodeName + "="
              + nodeValue + "</Attribute>");
        else
          System.out.print(spacesString + "<Attribute>" + nodeName + "="
              + nodeValue + "</Attribute>\n");
        break;
      case Node.CDATA_SECTION_NODE:
        if (tckmode)
          TestUtil.logMsg("<CDATA>" + nodeValue + "</CDATA>");
        else
          System.out.print("<CDATA>" + nodeValue + "</CDATA>\n");
        break;
      case Node.COMMENT_NODE:
        if (tckmode)
          TestUtil.logMsg("<Comment>" + nodeValue + "</Comment>");
        else
          System.out.print("<Comment>" + nodeValue + "</Comment>\n");
        break;
      case Node.DOCUMENT_FRAGMENT_NODE:
        if (tckmode)
          TestUtil.logMsg("<DocumentFragment/>");
        else
          System.out.print("<DocumentFragment/>\n");
        break;
      case Node.DOCUMENT_NODE:
        if (tckmode)
          TestUtil.logMsg("<Document/>");
        else
          System.out.print("<Document/>\n");
        break;
      case Node.DOCUMENT_TYPE_NODE:
        if (tckmode)
          TestUtil.logMsg("<DocumentType>" + nodeName + "</DocumentType>");
        else
          System.out.print("<DocumentType>" + nodeName + "</DocumentType>\n");
        break;
      case Node.ELEMENT_NODE:
        setSpaces();
        if (tckmode)
          TestUtil.logMsg(spacesString + "<Element>" + nodeName + "</Element>");
        else
          System.out
              .print(spacesString + "<Element>" + nodeName + "</Element>\n");
        spaces += 2;
        processAttributes(node);
        break;
      case Node.ENTITY_NODE:
        if (tckmode)
          TestUtil.logMsg("<Entity>" + nodeValue + "</Entity>");
        else
          System.out.print("<Entity>" + nodeValue + "</Entity>\n");
        break;
      case Node.ENTITY_REFERENCE_NODE:
        if (tckmode)
          TestUtil
              .logMsg("<EntityReference>" + nodeValue + "</EntityReference>");
        else
          System.out
              .print("<EntityReference>" + nodeValue + "</EntityReference>\n");
        break;
      case Node.NOTATION_NODE:
        if (tckmode)
          TestUtil.logMsg("<Notation>" + nodeValue + "</Notation>");
        else
          System.out.print("<Notation>" + nodeValue + "</Notation>\n");
        break;
      case Node.PROCESSING_INSTRUCTION_NODE:
        if (tckmode)
          TestUtil.logMsg("<ProcessingInstruction>" + nodeName
              + "</ProcessingInstruction>");
        else
          System.out.print("<ProcessingInstruction>" + nodeName
              + "</ProcessingInstruction>\n");
        break;
      case Node.TEXT_NODE:
        String text = getText(node);
        if (!text.equals("")) {
          setSpaces();
          if (tckmode)
            TestUtil.logMsg(spacesString + "<Text>" + text + "</text>");
          else
            System.out.print(spacesString + "<Text>" + text + "</text>\n");
        }
        break;
      default:
        if (tckmode)
          TestUtil.logMsg("<" + nodeName + ">");
        else
          System.out.print("<" + nodeName + ">\n");
        break;
      }
      if (node instanceof Element) {
        XmlDumpDOMNodes_((Element) node);
        spaces -= 2;
      }
    }
  }
}
