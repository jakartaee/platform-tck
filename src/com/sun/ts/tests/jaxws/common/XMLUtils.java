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

package com.sun.ts.tests.jaxws.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;
import javax.xml.soap.SOAPConstants;
import org.xml.sax.*;
import org.w3c.dom.*;

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

  private static boolean captureResults = false;

  private static ArrayList capturedResults = null;

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
        filename = args[i++];
        if (i < args.length) {
          schema = args[i];
          break;
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
      StringBuffer sb = new StringBuffer();
      while ((line = in.readLine()) != null) {
        sb.append(line);
      }
      in.close();
      data = sb.toString();
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

  public static void startCapturedResults() {
    captureResults = true;
    capturedResults = new ArrayList();
  }

  public static void stopCapturedResults() {
    captureResults = false;
  }

  public static ArrayList getCapturedResults() {
    return capturedResults;
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
         * if(JAXWS_Util.getSOAPVersion().equals(JAXWS_Util.SOAP11))
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

  public static void setSpaces() {
    spacesString = "";
    for (int i = 0; i < spaces; i++)
      spacesString = spacesString + " ";
  }

  public static String getText(Node node) {
    String result = "";
    result = node.getNodeValue();
    if (result == null)
      result = "";
    result = result.trim();
    return result;
  }

  public static String getAttrValue_(Node n, String name, String value) {
    String retAttr = null;
    NamedNodeMap attribs = n.getAttributes();
    if (attribs != null) {
      for (int i = 0; i < attribs.getLength(); i++) {
        Node attnode = attribs.item(i);
        String attName = attnode.getNodeName();
        String attValue = attnode.getNodeValue();
        System.out
            .println("<Attribute>" + attName + "=" + attValue + "</Attribute>");
        int index = attValue.indexOf("?wsdl");
        if (index != -1) {
          attValue = attValue.substring(0, index)
              + attValue.substring(index).toUpperCase();
        }
        index = value.indexOf("?wsdl");
        if (index != -1) {
          value = value.substring(0, index)
              + value.substring(index).toUpperCase();
        }
        if ((attName.equals(name) || attName.indexOf(name) != -1)
            && attValue.indexOf(value) != -1) {
          retAttr = attValue;
          break;
        }
      }
    }
    return retAttr;
  }

  public static void processAttributes(Node root) {
    NamedNodeMap attribs = root.getAttributes();
    if (attribs != null) {
      for (int i = 0; i < attribs.getLength(); i++) {
        Node attnode = attribs.item(i);
        String attName = attnode.getNodeName();
        String attValue = attnode.getNodeValue();
        String _string = "<Attribute>" + attName + "=" + attValue
            + "</Attribute>";
        if (captureResults)
          capturedResults.add(_string);
        if (tckmode)
          TestUtil.logMsg(spacesString + _string);
        else
          System.out.println(spacesString + _string);
      }
    }
  }

  public static boolean hasAttributes(Node root) {
    NamedNodeMap attribs = root.getAttributes();
    if (attribs == null || attribs.getLength() == 0)
      return false;
    else
      return true;
  }

  public static void xmlDumpDocument(Document document) {
    xmlDumpDOMNodes(document.getDocumentElement());
  }

  public static void xmlDumpDocument(Document document, boolean mode) {
    tckmode = mode;
    xmlDumpDOMNodes(document.getDocumentElement());
  }

  public static void xmlDumpDOMNodes(Element element, boolean mode) {
    tckmode = mode;
    xmlDumpDOMNodes(element);
  }

  public static void xmlDumpDOMNodes(Element element) {
    if (tckmode) {
      TestUtil.logMsg("");
      TestUtil.logMsg("-----------------------");
      TestUtil.logMsg("DOM DUMP OF XML MESSAGE");
      TestUtil.logMsg("-----------------------");
    } else {
      System.out.println("\n-----------------------\nDOM DUMP OF XML MESSAGE"
          + "\n-----------------------");
    }
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
  }

  public static void xmlDumpDOMNodes(Node node, boolean mode) {
    tckmode = mode;
    xmlDumpDOMNodes(node);
  }

  public static void xmlDumpDOMNodes(Node node) {
    if (node instanceof Document)
      xmlDumpDocument((Document) node);
    else if (node instanceof Element)
      xmlDumpDOMNodes((Element) node);
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
    String _string = "";
    switch (nodeType) {
    case Node.ATTRIBUTE_NODE:
      spaces += 2;
      setSpaces();
      _string = "<Attribute>" + nodeName + "=" + nodeValue + "</Attribute>";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      spaces -= 2;
      setSpaces();
      break;
    case Node.CDATA_SECTION_NODE:
      _string = "<CDATA>" + nodeValue + "</CDATA>";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      break;
    case Node.COMMENT_NODE:
      _string = "<Comment>" + nodeValue + "</Comment>";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      break;
    case Node.DOCUMENT_FRAGMENT_NODE:
      _string = "<DocumentFragment/>";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      break;
    case Node.DOCUMENT_NODE:
      _string = "<Document/>";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      break;
    case Node.DOCUMENT_TYPE_NODE:
      _string = "<DocumentType>" + nodeName + "</DocumentType>";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      break;
    case Node.ELEMENT_NODE:
      _string = "<Element>" + nodeName + "</Element>";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      spaces += 2;
      setSpaces();
      processAttributes(node);
      spaces -= 2;
      setSpaces();
      break;
    case Node.ENTITY_NODE:
      _string = "<Entity>" + nodeValue + "</Entity>";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      break;
    case Node.ENTITY_REFERENCE_NODE:
      _string = "<EntityReference>" + nodeValue + "</EntityReference>";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      break;
    case Node.NOTATION_NODE:
      _string = "<Notation>" + nodeValue + "</Notation>";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      break;
    case Node.PROCESSING_INSTRUCTION_NODE:
      _string = "<ProcessingInstruction>" + nodeName
          + "</ProcessingInstruction>";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      break;
    case Node.TEXT_NODE:
      String text = getText(node);
      if (!text.equals("")) {
        _string = "<Text>" + text + "</text>";
        if (captureResults)
          capturedResults.add(_string);
        if (tckmode)
          TestUtil.logMsg(spacesString + _string);
        else
          System.out.println(spacesString + _string);
      }
      break;
    default:
      _string = "<nodename=" + nodeName + ",nodetype=" + nodeType
          + ",nodeValue=" + nodeValue + ">";
      if (captureResults)
        capturedResults.add(_string);
      if (tckmode)
        TestUtil.logMsg(spacesString + _string);
      else
        System.out.println(spacesString + _string);
      break;
    }
  }

  public static void changeNodeValue_(Node n, String nName, String newValue) {
    Node n1 = findNode_(n, nName);
    Node node = null;
    String nodeValue = null;
    if (n1 != null) {
      NodeList nl = n1.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
        node = nl.item(i);
        short nodeType = node.getNodeType();
        if (nodeType == Node.TEXT_NODE) {
          node.setNodeValue(newValue);
          break;
        }
      }
    }
  }

  public static String getNodeValue_(Node n, String nName) {
    Node n1 = findNode_(n, nName);
    Node node = null;
    String nodeValue = null;
    if (n1 != null) {
      NodeList nl = n1.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
        node = nl.item(i);
        short nodeType = node.getNodeType();
        if (nodeType == Node.TEXT_NODE) {
          nodeValue = node.getNodeValue();
          break;
        }
      }
    }
    return nodeValue;
  }

  public static Node findNode_(Node n, String nName) {
    Node result = null;

    if (n != null) {
      String nodeName = n.getNodeName();
      if (nodeName != null) {
        if (nodeName.endsWith(nName)) {
          return n;
        }
      }

      NodeList nodes = n.getChildNodes();
      Node node = null;
      for (int i = 0; i < nodes.getLength(); i++) {
        node = nodes.item(i);
        if (node instanceof Element) {
          result = findNode_((Element) node, nName);
        }
        if (result != null) {
          break;
        }
      }
    }
    return result;
  }
}
