/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)TCKTagLibraryInfo.java	1.1 04/11/05
 */
package com.sun.ts.tests.jsf.spec.webapp.tldsig;

import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.jsp.tagext.FunctionInfo;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import javax.servlet.ServletContext;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class represents the <code>control</code> Taglibrary which will be used
 * to validate the taglibrary under test against.
 */
public class TCKTagLibraryInfo extends TagLibraryInfo {

  protected TagLibraryInfo tagLibraryInfo;

  // ------------------------------------------------------------ Constructors

  /**
   * <p>
   * Constructs a complete <code>TagLibraryInfo</code> instance based on the
   * content parsed from the TLD referenced by the <code>uri</code> parameter.
   * </p>
   * 
   * @param prefix
   *          ignored
   * @param uri
   *          the context-relative path to the control TLD
   * @param context
   *          the ServletContext
   */
  public TCKTagLibraryInfo(String prefix, String uri, ServletContext context) {
    super(prefix, uri);
    TLDParser parser = new TLDParser();
    parser.populate(context.getResourceAsStream(uri));
    context.log(parser.debug());

  } // END TCKTagLibraryInfo

  protected TCKTagLibraryInfo(String prefix, String uri) {
    super(prefix, uri);
    tagLibraryInfo = this;

  } // END TCKTagLibraryInfo

  // --------------------------------------------- Methods from TagLibraryInfo

  public TagLibraryInfo[] getTagLibraryInfos() {

    return new TagLibraryInfo[] {};

  } // END getTagLibraryInfos

  // ----------------------------------------------------------- Inner Classes

  private class TLDParser extends DefaultHandler {

    /*
     * Elements we're interested in inspecting for TLD signatures tests.
     */
    private static final String TLD_TAGLIB = "taglib";

    private static final String TLD_TAGLIB_JSP_VERSION = TLD_TAGLIB
        + "/jsp-version";

    private static final String TLD_TAGLIB_TLIB_VERSION = TLD_TAGLIB
        + "/tlib-version";

    private static final String TLD_TAGLIB_SHORT_NAME = TLD_TAGLIB
        + "/short-name";

    private static final String TLD_TAGLIB_URI = TLD_TAGLIB + "/uri";

    private static final String TLD_TAGLIB_TAG = TLD_TAGLIB + "/tag";

    private static final String TLD_TAGLIB_TAG_NAME = TLD_TAGLIB_TAG + "/name";

    private static final String TLD_TAGLIB_TAG_CLASS = TLD_TAGLIB_TAG
        + "/tag-class";

    private static final String TLD_TAGLIB_TAG_BODY_CONTENT = TLD_TAGLIB_TAG
        + "/body-content";

    private static final String TLD_TAGLIB_TAG_ATTR = TLD_TAGLIB_TAG
        + "/attribute";

    private static final String TLD_TAGLIB_TAG_ATTR_NAME = TLD_TAGLIB_TAG_ATTR
        + "/name";

    private static final String TLD_TAGLIB_TAG_ATTR_REQ = TLD_TAGLIB_TAG_ATTR
        + "/required";

    private static final String TLD_TAGLIB_TAG_ATTR_RTEXPR = TLD_TAGLIB_TAG_ATTR
        + "/rtexprvalue";

    private static final String TLD_TAGLIB_TAG_ATTR_FRAGMENT = TLD_TAGLIB_TAG_ATTR
        + "/fragment";

    private static final String TLD_TAGLIB_TAG_ATTR_DEFRD_VAL_TYPE = TLD_TAGLIB_TAG_ATTR
        + "/deferred-value/type";

    private static final String TLD_TAGLIB_TAG_ATTR_DEFRD_METH_SIG = TLD_TAGLIB_TAG_ATTR
        + "/deferred-method/method-signature";

    private static final String TLD_TAGLIB_TAG_ATTR_TYPE = TLD_TAGLIB_TAG_ATTR
        + "/type";

    private static final String TLD_TAGLIB_FUNCTION = TLD_TAGLIB + "/function";

    private static final String TLD_TAGLIB_FUNCTION_NAME = TLD_TAGLIB_FUNCTION
        + "/name";

    private static final String TLD_TAGLIB_FUNCTION_CLASS = TLD_TAGLIB_FUNCTION
        + "/function-class";

    private static final String TLD_TAGLIB_FUNCTION_SIG = TLD_TAGLIB_FUNCTION
        + "/function-signature";

    protected StringBuffer content;

    protected Stack objectStack;

    protected Stack pathStack;

    private List tagList;

    private List functionList;

    private Map methodMap;

    private String currentPath;

    // -------------------------------------------------------- Constructors

    TLDParser() {

      tagList = new ArrayList();
      functionList = new ArrayList();
      methodMap = new HashMap();
      pathStack = new Stack();
      objectStack = new Stack();

      // TagHolder
      methodMap.put(TLD_TAGLIB_TAG_NAME,
          new MethodInfo("setName", new Class[] { String.class }));
      methodMap.put(TLD_TAGLIB_TAG_CLASS,
          new MethodInfo("setClass", new Class[] { String.class }));
      methodMap.put(TLD_TAGLIB_TAG_ATTR, new MethodInfo("addAttribute",
          new Class[] { TagAttributeInfo.class }));
      methodMap.put(TLD_TAGLIB_TAG_BODY_CONTENT,
          new MethodInfo("setBodyContent", new Class[] { String.class }));

      // AttributeHolder
      methodMap.put(TLD_TAGLIB_TAG_ATTR_NAME,
          new MethodInfo("setName", new Class[] { String.class }));
      methodMap.put(TLD_TAGLIB_TAG_ATTR_TYPE,
          new MethodInfo("setAttributeType", new Class[] { String.class }));
      methodMap.put(TLD_TAGLIB_TAG_ATTR_REQ,
          new MethodInfo("setRequired", new Class[] { String.class }));
      methodMap.put(TLD_TAGLIB_TAG_ATTR_RTEXPR,
          new MethodInfo("setRtexpr", new Class[] { String.class }));
      methodMap.put(TLD_TAGLIB_TAG_ATTR_FRAGMENT,
          new MethodInfo("setFragment", new Class[] { String.class }));
      methodMap.put(TLD_TAGLIB_TAG_ATTR_DEFRD_VAL_TYPE,
          new MethodInfo("setDeferredValueType", new Class[] { String.class }));
      methodMap.put(TLD_TAGLIB_TAG_ATTR_DEFRD_METH_SIG,
          new MethodInfo("setDeferredMethodSig", new Class[] { String.class }));

      // FunctionHolder
      methodMap.put(TLD_TAGLIB_FUNCTION_NAME,
          new MethodInfo("setName", new Class[] { String.class }));
      methodMap.put(TLD_TAGLIB_FUNCTION_CLASS,
          new MethodInfo("setClass", new Class[] { String.class }));
      methodMap.put(TLD_TAGLIB_FUNCTION_SIG,
          new MethodInfo("setSignature", new Class[] { String.class }));

    } // END TLDParser

    // ------------------------------------------------------ Public Methods

    public void populate(InputStream tldInputStream) {

      try {
        SAXParserFactory parserFactory = getConfiguredFactory();
        SAXParser parser = parserFactory.newSAXParser();
        parser.parse(tldInputStream, this);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    } // END populate

    public String debug() {

      StringBuffer sb = new StringBuffer(128);
      sb.append("[Taglibrary Info]\n");
      sb.append("JSP Version: ").append(jspversion).append('\n');
      sb.append("Taglibrary Version: ").append(tlibversion).append('\n');
      sb.append("URI: ").append(uri).append('\n');

      if (tags.length > 0) {
        sb.append("-------- TAGS ----------\n");
        for (int i = 0; i < tags.length; i++) {
          sb.append("Tag Name: ").append(tags[i].getTagName()).append('\n');
          sb.append("Tag Class: ").append(tags[i].getTagClassName())
              .append('\n');
          sb.append("\t---------- Attributes ---------\n");
          TagAttributeInfo[] attributes = tags[i].getAttributes();
          if (attributes.length > 0) {
            for (int ii = 0; ii < attributes.length; ii++) {
              sb.append("\tName: ").append(attributes[ii].getName())
                  .append('\n');
              if (attributes[ii].isDeferredValue()) {
                sb.append("\tDeferred Value: true\n");
                sb.append("\tDeferred Type: ")
                    .append(attributes[ii].getExpectedTypeName()).append('\n');
              } else if (attributes[ii].isDeferredMethod()) {
                sb.append("\tDeferred Method: true\n");
                sb.append("\tSignature: ")
                    .append(attributes[ii].getMethodSignature()).append('\n');
              } else {
                sb.append("\tType: ").append(attributes[ii].getTypeName())
                    .append('\n');
                sb.append("\tRequired: ").append(attributes[ii].isRequired())
                    .append('\n');
                sb.append("\tRtexpr: ")
                    .append(attributes[ii].canBeRequestTime()).append('\n');
                sb.append("\tFragment ").append(attributes[ii].isFragment())
                    .append('\n');
              }
              sb.append('\n');
            }
          }
          sb.append('\n');
        }
      }

      if (functions.length > 0) {
        sb.append("-------- Functions ---------");
        for (int i = 0; i < functions.length; i++) {
          sb.append("Name: ").append(functions[i].getName()).append('\n');
          sb.append("Function Class: ").append(functions[i].getFunctionClass())
              .append('\n');
          sb.append("Signature: ").append(functions[i].getFunctionSignature())
              .append('\n');
        }
        sb.append('\n');
      }

      return sb.toString();

    } // END debug

    // ----------------------------------------- Methods from DefaultHandler

    public InputSource resolveEntity(String publicId, String systemId)
        throws SAXException {

      return new InputSource(new StringReader(""));

    } // END resolveEntity

    public void startElement(String uri, String localName, String qName,
        Attributes attributes) throws SAXException {

      pathStack.push(localName);
      updatePath();
      content = new StringBuffer(32);
      if (TLD_TAGLIB.equals(currentPath)) {
        jspversion = attributes.getValue("version");
      } else if (TLD_TAGLIB_TAG.equals(currentPath)) {
        objectStack.push(new TagHolder());
      } else if (TLD_TAGLIB_TAG_ATTR.equals(currentPath)) {
        objectStack.push(new AttributeHolder());
      } else if (TLD_TAGLIB_FUNCTION.equals(currentPath)) {
        objectStack.push(new FunctionHolder());
      }

    } // END startElement

    public void characters(char[] ch, int start, int length)
        throws SAXException {

      content.append(ch, start, length);

    } // END characters

    public void endElement(String uri, String localName, String qName)
        throws SAXException {

      String contentValue = content.toString().trim();
      if (TLD_TAGLIB_TLIB_VERSION.equals(currentPath)) {
        tlibversion = contentValue;
      } else if (TLD_TAGLIB_JSP_VERSION.equals(currentPath)) {
        jspversion = contentValue;
      } else if (TLD_TAGLIB_SHORT_NAME.equals(currentPath)) {
        shortname = contentValue;
      } else if (TLD_TAGLIB_URI.equals(currentPath)) {
        urn = contentValue;
      } else if (TLD_TAGLIB_TAG.equals(currentPath)
          || TLD_TAGLIB_TAG_ATTR.equals(currentPath)
          || TLD_TAGLIB_FUNCTION.equals(currentPath)) {
        Holder holder = (Holder) objectStack.pop();
        holder.pushData();
      } else {
        if (objectStack.size() > 0) {
          MethodInfo info = (MethodInfo) methodMap.get(currentPath);
          if (info != null) {
            Object holder = objectStack.peek();
            callMethod(info, holder, new Object[] { contentValue });
          }
        }
      }

      pathStack.pop();
      updatePath();

    } // END endElement

    public void endDocument() throws SAXException {

      // set the protected ivars so when their associated method
      // is called, something useful is returned.
      functions = (FunctionInfo[]) functionList
          .toArray(new FunctionInfo[functionList.size()]);
      tags = (TagInfo[]) tagList.toArray(new TagInfo[tagList.size()]);

    } // END endDocument

    // ----------------------------------------------------- Protected Methods

    protected void callMethod(MethodInfo info, Object target, Object[] params) {

      try {
        Method method = target.getClass()
            .getDeclaredMethod(info.getMethodName(), info.getParamType());
        method.invoke(target, params);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    } // END callMethod

    // ----------------------------------------------------- Private Methods

    /**
     * When called, update the currentPath instance variable with an XPath-like
     * path based on the path elements contained in the stack.
     */
    private void updatePath() {
      StringBuffer sb = new StringBuffer();
      for (Iterator i = pathStack.iterator(); i.hasNext();) {
        sb.append(i.next());
        if (i.hasNext()) {
          sb.append('/');
        }
      }

      currentPath = sb.toString();

    } // END getCurrentPath

    /**
     * <p>
     * Return a <code>SAXParserFactory</code> instance that is non-validating
     * and is namespace aware.
     * </p>
     * 
     * @return configured <code>SAXParserFactory</code>
     */
    private SAXParserFactory getConfiguredFactory() {

      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setValidating(false);
      factory.setNamespaceAware(true);
      return factory;

    } // END getConfiguredFactory

    // ------------------------------------------------------- Inner Classes

    /**
     * Base class defining a common way to push data to the outter class
     * TCKTagLibraryInfo.
     */
    private abstract class Holder {

      // ------------------------------------------------ Abstract Methods

      public abstract void pushData();

    }

    /**
     * <p>
     * Simple structure for holding method name and parameter type information.
     * </p>
     */
    private class MethodInfo {

      private String methodName;

      private Class[] paramType;

      // ---------------------------------------------------- Constructors

      MethodInfo(String methodName, Class[] paramType) {

        this.methodName = methodName;
        this.paramType = paramType;

      } // END MethodInfo

      // -------------------------------------------------- Public Methods

      public String getMethodName() {

        return methodName;

      } // END getMethodName

      public Class[] getParamType() {

        return paramType;

      } // END getParamType

    } // END MethodInfo

    /**
     * <p>
     * This class is a temporary holder of parsed Tag data. Once all Tag related
     * data has been parsed, the content of this class will be used to construct
     * a new <code>TagInfo</code> instance.
     * </p>
     */
    private class TagHolder extends Holder {

      String name;

      String clazz;

      String bodyContent;

      List attributes;

      // ---------------------------------------------------- Constructors

      TagHolder() {
        attributes = new ArrayList();
      }

      // --------------------------------------------- Methods From Holder

      public void pushData() {

        TagAttributeInfo[] attrInfo = (TagAttributeInfo[]) attributes
            .toArray(new TagAttributeInfo[attributes.size()]);

        TagInfo info = new TagInfo(name, clazz, bodyContent, null,
            tagLibraryInfo, null, attrInfo);

        tagList.add(info);

      } // END pushData

      // -------------------------------------------------- Public Methods

      public void setName(String name) {

        this.name = name;

      } // END setName

      public void setClass(String clazz) {

        this.clazz = clazz;

      } // END setClass

      public void setBodyContent(String bodyContent) {

        this.bodyContent = bodyContent;

      } // END setBodyContent

      public void addAttribute(TagAttributeInfo attribute) {

        this.attributes.add(attribute);

      } // END addAttribute

    } // END TagHolder

    /**
     * <p>
     * This class is a temporary holder of parsed Tag attribute data. Once all
     * attribute related data has been parsed, the content of this class will be
     * used to construct a new <code>AttributeInfo</code> instance.
     * </p>
     */
    private class AttributeHolder extends Holder {

      private String name;

      private String attributeType;

      private String isRequired;

      private String isRtexpr;

      private String isDeferredValue;

      private String isDeferredMethod;

      private String isFragment;

      private String deferredValueType;

      private String deferredMethodSig;

      // ---------------------------------------------------- Constructors

      AttributeHolder() {
      }

      // --------------------------------------------- Methods from Holder

      public void pushData() {

        TagAttributeInfo info = new TagAttributeInfo(name,

            ((isRequired == null) ? false
                : Boolean.valueOf(isRequired).booleanValue()),

            ((attributeType == null) ? "" : attributeType),

            ((isRtexpr == null) ? false
                : Boolean.valueOf(isRtexpr).booleanValue()),

            ((isFragment == null) ? false
                : Boolean.valueOf(isFragment).booleanValue()),

            "", // description - not too worried about this

            ((isDeferredValue == null) ? false
                : Boolean.valueOf(isDeferredValue).booleanValue()),

            ((isDeferredMethod == null) ? false
                : Boolean.valueOf(isDeferredMethod).booleanValue()),

            deferredValueType, deferredMethodSig);
        TagHolder holder = (TagHolder) objectStack.peek();
        holder.addAttribute(info);

      } // END pushData

      // -------------------------------------------------- Public Methods

      public void setName(String name) {

        this.name = name;

      } // END setName

      public void setAttributeType(String attributeType) {

        this.attributeType = attributeType;

      } // END setAttributeType

      public void setRequired(String required) {

        isRequired = required;

      } // END setRequired

      public void setRtexpr(String rtexpr) {

        isRtexpr = rtexpr;

      } // END setRtexpr

      public void setFragment(String fragment) {

        isFragment = fragment;

      } // END setFragment

      public void setDeferredValueType(String deferredValueType) {

        this.deferredValueType = deferredValueType;
        isDeferredValue = "true";

      } // END setDefferredValueType

      public void setDeferredMethodSig(String deferredMethodSig) {

        this.deferredMethodSig = deferredMethodSig;
        isDeferredMethod = "true";

      } // END setDeferredMethodSig

    } // END AttributeHolder

    private class FunctionHolder extends Holder {

      private String name;

      private String clazz;

      private String signature;

      // ---------------------------------------------------- Constructors

      FunctionHolder() {
      }

      // --------------------------------------------- Methods from Holder

      public void pushData() {

        FunctionInfo info = new FunctionInfo(name, clazz, signature);
        functionList.add(info);

      } // END pushData

      // -------------------------------------------------- Public Methods

      public void setName(String name) {

        this.name = name;

      } // END setName

      public void setClass(String clazz) {

        this.clazz = clazz;

      } // END setClass

      public void setSignature(String signature) {

        this.signature = signature;

      } // END setSignature

    } // END FunctionHolder

  } // END TLDParser

} // END TCKTagLibraryInfo
