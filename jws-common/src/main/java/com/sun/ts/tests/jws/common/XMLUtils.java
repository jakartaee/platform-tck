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

package com.sun.ts.tests.jws.common;

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
    StringBuilder data = new StringBuilder();
    try {
      System.out.println("validating=" + validating);
      System.out.println("namespaceaware=" + nsaware);
      System.out.println("file=" + filename);
      System.out.println("schema=" + schema);
      BufferedReader in = new BufferedReader(new FileReader(filename));
      String line = "";
      while ((line = in.readLine()) != null) {
        data.append(line);
      }
      in.close();
      System.out.println("data=" + data.toString());
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
      document = builder
          .parse(new InputSource(new StringReader(data.toString())));
      rootElement = document.getDocumentElement();
      NodeList nodes = document.getElementsByTagName("Fault");
      if (nodes.getLength() == 0)
        nodes = document.getElementsByTagName(prefix + ":Fault");
      Node node = nodes.item(0);
      faultElement = (Element) node;
      if (!errors)
        xmlDumpDOMNodes(XMLUtils.getRootElement());
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

  public static boolean xmlDOMSetup() {
    boolean errors = false;
    try {
      dbfactory = DocumentBuilderFactory.newInstance();
      dbfactory.setNamespaceAware(nsaware);
      dbfactory.setValidating(validating);
      if (validating && nsaware) {
        dbfactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        dbfactory.setAttribute(JAXP_SCHEMA_SOURCE,
            SOAPConstants.URI_NS_SOAP_ENVELOPE);
        /***
         * if(JWS_Util.getSOAPVersion().equals(JWS_Util.SOAP11))
         * dbfactory.setAttribute( JAXP_SCHEMA_SOURCE,
         * SOAPConstants.URI_NS_SOAP_ENVELOPE); else dbfactory.setAttribute(
         * JAXP_SCHEMA_SOURCE, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
         ***/
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

  public static boolean xmlDOMParse(String data) {
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

  public static boolean xmlDOMParse(File f) {
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

  public static boolean xmlDOMTransform() {
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
        if (tckmode)
          TestUtil.logMsg(spacesString + "<Attribute>" + attName + "="
              + attValue + "</Attribute>");
        else
          System.out.println(spacesString + "<Attribute>" + attName + "="
              + attValue + "</Attribute>");
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

  public static void xmlDumpDocument(Document document) {
    xmlDumpDOMNodes(document.getDocumentElement());
  }

  public static void xmlDumpDOMNodes(Node node) {
    if (node instanceof Document)
      xmlDumpDocument((Document) node);
    else if (node instanceof Element)
      xmlDumpDOMNodes((Element) node);
  }

  public static void xmlDumpDOMNodes(Element element) {
    if (tckmode)
      TestUtil.logMsg("Begin Dumping DOM Nodes");
    else
      System.out.println("Begin Dumping DOM Nodes");
    Node node = (Node) element;
    String nodeName = node.getNodeName();
    String nodeValue = node.getNodeValue();
    short nodeType = node.getNodeType();
    spaces = 0;
    setSpaces();
    processNode(node, nodeName, nodeValue, nodeType);
    spaces += 2;
    setSpaces();
    xmlDumpDOMNodes_(element);
    if (tckmode)
      TestUtil.logMsg("Done Dumping DOM Nodes");
    else
      System.out.println("Done Dumping DOM Nodes");
  }

  public static void xmlDumpDOMNodes_(Element element) {
    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      String nodeName = node.getNodeName();
      String nodeValue = node.getNodeValue();
      short nodeType = node.getNodeType();
      processNode(node, nodeName, nodeValue, nodeType);
      if (node instanceof Element) {
        Element e = (Element) node;
        NodeList nl = e.getChildNodes();
        if (nl.getLength() > 0) {
          spaces += 2;
          setSpaces();
          xmlDumpDOMNodes_((Element) node);
          spaces -= 2;
          setSpaces();
        }
      }
    }
  }

  public static void processNode(Node node, String nodeName, String nodeValue,
      short nodeType) {
    switch (nodeType) {
    case Node.ATTRIBUTE_NODE:
      if (tckmode)
        TestUtil.logMsg(spacesString + "<Attribute>" + nodeName + "="
            + nodeValue + "</Attribute>");
      else
        System.out.println(spacesString + "<Attribute>" + nodeName + "="
            + nodeValue + "</Attribute>");
      break;
    case Node.CDATA_SECTION_NODE:
      if (tckmode)
        TestUtil.logMsg(spacesString + "<CDATA>" + nodeValue + "</CDATA>");
      else
        System.out.println(spacesString + "<CDATA>" + nodeValue + "</CDATA>");
      break;
    case Node.COMMENT_NODE:
      if (tckmode)
        TestUtil.logMsg(spacesString + "<Comment>" + nodeValue + "</Comment>");
      else
        System.out
            .println(spacesString + "<Comment>" + nodeValue + "</Comment>");
      break;
    case Node.DOCUMENT_FRAGMENT_NODE:
      if (tckmode)
        TestUtil.logMsg(spacesString + "<DocumentFragment/>");
      else
        System.out.println(spacesString + "<DocumentFragment/>");
      break;
    case Node.DOCUMENT_NODE:
      if (tckmode)
        TestUtil.logMsg(spacesString + "<Document/>");
      else
        System.out.println(spacesString + "<Document/>");
      break;
    case Node.DOCUMENT_TYPE_NODE:
      if (tckmode)
        TestUtil.logMsg(
            spacesString + "<DocumentType>" + nodeName + "</DocumentType>");
      else
        System.out.println(
            spacesString + "<DocumentType>" + nodeName + "</DocumentType>");
      break;
    case Node.ELEMENT_NODE:
      if (tckmode)
        TestUtil.logMsg(spacesString + "<Element>" + nodeName + "</Element>");
      else
        System.out
            .println(spacesString + "<Element>" + nodeName + "</Element>");
      spaces += 2;
      setSpaces();
      processAttributes(node);
      spaces -= 2;
      setSpaces();
      break;
    case Node.ENTITY_NODE:
      if (tckmode)
        TestUtil.logMsg(spacesString + "<Entity>" + nodeValue + "</Entity>");
      else
        System.out.println(spacesString + "<Entity>" + nodeValue + "</Entity>");
      break;
    case Node.ENTITY_REFERENCE_NODE:
      if (tckmode)
        TestUtil.logMsg(spacesString + "<EntityReference>" + nodeValue
            + "</EntityReference>");
      else
        System.out.println(spacesString + "<EntityReference>" + nodeValue
            + "</EntityReference>");
      break;
    case Node.NOTATION_NODE:
      if (tckmode)
        TestUtil
            .logMsg(spacesString + "<Notation>" + nodeValue + "</Notation>");
      else
        System.out
            .println(spacesString + "<Notation>" + nodeValue + "</Notation>");
      break;
    case Node.PROCESSING_INSTRUCTION_NODE:
      if (tckmode)
        TestUtil.logMsg(spacesString + "<ProcessingInstruction>" + nodeName
            + "</ProcessingInstruction>");
      else
        System.out.println(spacesString + "<ProcessingInstruction>" + nodeName
            + "</ProcessingInstruction>");
      break;
    case Node.TEXT_NODE:
      String text = getText(node);
      if (!text.equals("")) {
        if (tckmode)
          TestUtil.logMsg(spacesString + "<Text>" + text + "</text>");
        else
          System.out.println(spacesString + "<Text>" + text + "</text>");
      }
      break;
    default:
      if (tckmode)
        TestUtil.logMsg(spacesString + "<nodename=" + nodeName + ", nodetype="
            + nodeType + ", nodeValue=" + nodeValue + ">");
      else
        System.out.println(spacesString + "<nodename=" + nodeName
            + ", nodetype=" + nodeType + ", nodeValue=" + nodeValue + ">");
      break;
    }
  }
}
