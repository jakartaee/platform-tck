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
 * @(#)TaglibSigValidator.java	1.1 04/11/05
 */
package com.sun.ts.tests.jsf.spec.webapp.tldsig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.FunctionInfo;

public class TaglibSigValidator {

  private Map<String, String[]> attributeExcludes;

  private TCKTagLibraryInfo control;

  private TagLibraryInfo underTest;

  private List messages;

  String uri;

  // ------------------------------------------------------------ Constructors

  public TaglibSigValidator(TCKTagLibraryInfo control,
      TagLibraryInfo underTest) {

    this.control = control;
    this.underTest = underTest;
    messages = new ArrayList();
    uri = control.getReliableURN();
    attributeExcludes = new HashMap<String, String[]>();

  } // END TaglibSigValidator

  // ---------------------------------------------------------- Public Methods

  /**
   * <p>
   * When called, validation between the control TagLibraryInfo and the
   * TagLibraryInfo under test will be compared. If there are any errors during
   * the validation, they will be returned as an array of Strings. If there are
   * no errors, a zero-length array will be returned.
   * </p>
   */
  public String[] validate() {

    validateTaglibVersion();
    validateTags();
    validateFunctions();

    return (String[]) messages.toArray(new String[messages.size()]);

  } // END validate

  // --------------------------------------------------------- Private Methods

  private void validateTaglibVersion() {

    // String tlibVersion = control.getRequiredVersion();
    // if (tlibVersion != null && tlibVersion.length() > 0) {
    // if (!tlibVersion)
    // }

  } // END validateTaglibVersion

  private void validateTags() {

    String prefix = '[' + uri + "] - ";
    TagInfo[] controlTags = control.getTags();
    TagInfo[] tagsUnderTest = underTest.getTags();

    String[] controlNames = new String[controlTags.length];
    String[] underTestNames = new String[tagsUnderTest.length];

    for (int i = 0; i < controlNames.length; i++) {
      controlNames[i] = controlTags[i].getTagName();
    }
    for (int i = 0; i < underTestNames.length; i++) {
      underTestNames[i] = tagsUnderTest[i].getTagName();
    }

    Arrays.sort(controlNames);
    Arrays.sort(underTestNames);

    // validate we have the right number of tags between the two
    // libraries and that the names mesh.
    if (!Arrays.equals(controlNames, underTestNames)) {
      // ok, there was a mismatch somewhere...figure out the
      // differences
      List notFound = new ArrayList();
      List extra = new ArrayList();
      // first pass: find the values that exist in control, but not in
      // under test
      for (int i = 0; i < controlNames.length; i++) {
        if (Arrays.binarySearch(underTestNames, controlNames[i]) < 0) {
          notFound.add(controlNames[i]);
        }
      }

      // second pass: find
      for (int i = 0; i < underTestNames.length; i++) {
        if (Arrays.binarySearch(controlNames, underTestNames[i]) < 0) {
          extra.add(underTestNames[i]);
        }
      }

      if (notFound.size() > 0) {
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        sb.append("The following tags were not found in the ");
        sb.append("taglibrary under test:\n");
        for (int i = 0, size = notFound.size(); i < size; i++) {
          sb.append('\t').append(notFound.get(i)).append('\n');
        }
        messages.add(sb.toString());
      }

      if (extra.size() > 0) {
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        sb.append("The following tags were defined in the ");
        sb.append("taglibrary under test and not defined by the ");
        sb.append("specification:\n");
        for (int i = 0, size = extra.size(); i < size; i++) {
          sb.append('\t').append(extra.get(i)).append('\n');
        }
        messages.add(sb.toString());
      }
    } else {
      validateTagAttributes(controlNames);
    }

  } // END validateTags

  private void validateTagAttributes(String[] tagNames) {

    for (int i = 0; i < tagNames.length; i++) {

      String prefix = '[' + uri + ':' + tagNames[i] + "] - ";

      TagAttributeInfo[] controlAttrs = control.getTag(tagNames[i])
          .getAttributes();
      TagAttributeInfo[] testAttrs = underTest.getTag(tagNames[i])
          .getAttributes();

      String[] controlAttrNames = new String[controlAttrs.length];
      String[] testAttrNames = new String[testAttrs.length];

      for (int ii = 0; ii < controlAttrs.length; ii++) {
        controlAttrNames[ii] = controlAttrs[ii].getName();
      }

      for (int ii = 0; ii < testAttrs.length; ii++) {
        testAttrNames[ii] = testAttrs[ii].getName();
      }

      Arrays.sort(controlAttrNames);
      Arrays.sort(testAttrNames);

      if (!Arrays.equals(controlAttrNames, testAttrNames)) {
        List notFound = new ArrayList();
        List extra = new ArrayList();
        // first pass: find the values that exist in control, but not in
        // under test
        for (int ii = 0; ii < controlAttrNames.length; ii++) {
          if (Arrays.binarySearch(testAttrNames, controlAttrNames[ii]) < 0) {
            notFound.add(controlAttrNames[ii]);
          }
        }

        // second pass: find
        for (int ii = 0; ii < testAttrNames.length; ii++) {
          if (Arrays.binarySearch(controlAttrNames, testAttrNames[ii]) < 0) {
            extra.add(testAttrNames[ii]);
          }
        }

        if (notFound.size() > 0) {
          StringBuffer sb = new StringBuffer();
          sb.append(prefix);
          sb.append("The following attributes for tag '");
          sb.append(tagNames[i]).append("' were not found in the ");
          sb.append("taglibrary under test:\n");
          for (int ii = 0, size = notFound.size(); ii < size; ii++) {
            sb.append('\t').append(notFound.get(ii)).append('\n');
          }
          messages.add(sb.toString());
        }

        if (extra.size() > 0) {
          StringBuffer sb = new StringBuffer();
          sb.append(prefix);
          sb.append("The following attributes for tag '");
          sb.append(tagNames[i]).append("' were defined in the ");
          sb.append("taglibrary under test and not defined by the ");
          sb.append("specification:\n");
          for (int ii = 0, size = extra.size(); ii < size; ii++) {
            sb.append('\t').append(extra.get(ii)).append('\n');
          }
          messages.add(sb.toString());
        }
      } else {

        for (int k = 0; k < controlAttrs.length; k++) {
          for (int n = 0; n < testAttrs.length; n++) {
            if (controlAttrs[k].getName().equals(testAttrs[n].getName())) {
              validateAttribute(tagNames[i], controlAttrs[k], testAttrs[n]);
            }
          }
        }

      }
    }

  } // END validateAttributes

  private void validateAttribute(String tagName, TagAttributeInfo controlAttr,
      TagAttributeInfo testAttr) {

    String attrName = controlAttr.getName();
    String[] excludes = attributeExcludes.get(tagName);
    if (excludes != null) {
      Arrays.sort(excludes);
      if (Arrays.binarySearch(excludes, attrName) >= 0) {
        System.out.println("Attribute " + attrName + " of tag " + tagName
            + " is excluded from processing");
        return;
      }
    }
    String prefix = '[' + uri + ':' + tagName + ':' + attrName + "] - ";

    boolean controlRequired = controlAttr.isRequired();
    boolean testRequired = testAttr.isRequired();
    if (controlRequired != testRequired) {
      StringBuffer sb = new StringBuffer();
      sb.append(prefix);
      sb.append("Mismatch on required value configuration - ");
      sb.append("expected: ").append(controlRequired);
      sb.append(", received: ").append(testRequired);
      messages.add(sb.toString());
    }

    if (controlAttr.isDeferredValue()) {
      if (!testAttr.isDeferredValue()) {
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        sb.append("attribute is not a deferred value");
        messages.add(sb.toString());
      } else {
        // validate the deferred value type.
        String controlType = fixType(controlAttr.getExpectedTypeName());
        String testType = fixType(testAttr.getExpectedTypeName());
        if (controlAttr.getExpectedTypeName() == null) {
          controlType = "java.lang.Object";
        }

        if (testType == null) {
          testType = "java.lang.Object";
        }

        if (!controlType.equals(testType)) {
          StringBuffer sb = new StringBuffer();
          sb.append(prefix);
          sb.append("Expected deferred value type to be '");
          sb.append(controlType).append("', found '");
          sb.append(testType).append('\'');
          messages.add(sb.toString());
        }
      }
    } else if (controlAttr.isDeferredMethod()) {
      if (!testAttr.isDeferredMethod()) {
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        sb.append("attribute is not a deferred method");
        messages.add(sb.toString());
      } else {
        String controlSig = controlAttr.getMethodSignature();
        String testSig = testAttr.getMethodSignature();

        if (!signaturesAreEqual(controlSig, testSig)) {
          StringBuffer sb = new StringBuffer();
          sb.append(prefix);
          sb.append("deferred-method signature mismatch on return");
          sb.append(" types and/or parameter types/order - expected: '");
          sb.append(controlSig).append("', received: '");
          sb.append(testSig).append('\'');
          messages.add(sb.toString());
        }
      }
    } else {
      boolean controlRequestTime = controlAttr.canBeRequestTime();
      boolean testRequestTime = testAttr.canBeRequestTime();
      if (controlRequestTime != testRequestTime) {
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        sb.append("Mismatch on rtexpr value configuration - ");
        sb.append("expected: ").append(controlRequestTime);
        sb.append(", received: ").append(testRequestTime);
        messages.add(sb.toString());
      }

      if (controlRequestTime) {
        // if the attribute can accept request time values, validate
        // the type
        String controlType = fixType(controlAttr.getTypeName());
        String testType = fixType(testAttr.getTypeName());
        if (testType == null) {
          testType = "";
        }
        if (!controlType.equals(testType)) {
          StringBuffer sb = new StringBuffer();
          sb.append(prefix);
          sb.append("Incorrect attribute type for attribute '");
          sb.append(attrName).append("' for tag '");
          sb.append(tagName).append("' - Expected '");
          sb.append(controlType);
          sb.append("', found '").append(testType);
          sb.append('\'');
          messages.add(sb.toString());
        }
      }
    }

  } // END validateAttibute

  public void validateFunctions() {

    String prefix = '[' + uri + "] - ";
    FunctionInfo[] controlFunctions = control.getFunctions();
    FunctionInfo[] testFunctions = underTest.getFunctions();

    String[] controlFunctionNames = new String[controlFunctions.length];
    String[] testFunctionNames = new String[testFunctions.length];

    for (int i = 0; i < controlFunctions.length; i++) {
      controlFunctionNames[i] = controlFunctions[i].getName();
    }

    for (int i = 0; i < testFunctions.length; i++) {
      testFunctionNames[i] = testFunctions[i].getName();
    }

    Arrays.sort(controlFunctionNames);
    Arrays.sort(testFunctionNames);

    if (!Arrays.equals(controlFunctionNames, testFunctionNames)) {

      List notFound = new ArrayList();
      List extra = new ArrayList();

      // first pass: find the values that exist in control, but not in
      // under test
      for (int ii = 0; ii < controlFunctionNames.length; ii++) {
        if (Arrays.binarySearch(testFunctionNames,
            controlFunctionNames[ii]) < 0) {
          notFound.add(controlFunctionNames[ii]);
        }
      }

      // second pass: find
      for (int ii = 0; ii < testFunctionNames.length; ii++) {
        if (Arrays.binarySearch(controlFunctionNames,
            testFunctionNames[ii]) < 0) {
          extra.add(testFunctionNames[ii]);
        }
      }

      if (notFound.size() > 0) {
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        sb.append("The following functions were not found in the ");
        sb.append("taglibrary under test:\n");
        for (int ii = 0, size = notFound.size(); ii < size; ii++) {
          sb.append('\t').append(notFound.get(ii)).append('\n');
        }
        messages.add(sb.toString());
      }

      if (extra.size() > 0) {
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        sb.append("The following functions ");
        sb.append(" were defined in the ");
        sb.append("taglibrary under test and not defined by the ");
        sb.append("specification:\n");
        for (int ii = 0, size = extra.size(); ii < size; ii++) {
          sb.append('\t').append(extra.get(ii)).append('\n');
        }
        messages.add(sb.toString());
      }
    } else {
      // the names match up, now confirm the signatures
      for (int i = 0; i < controlFunctions.length; i++) {
        String functionName = controlFunctions[i].getName();
        prefix = '[' + uri + ':' + functionName + "] - ";
        for (int j = 0; j < testFunctions.length; j++) {
          if (functionName.equals(testFunctions[j].getName())) {
            if (!signaturesAreEqual(controlFunctions[i].getFunctionSignature(),
                testFunctions[j].getFunctionSignature())) {
              StringBuffer sb = new StringBuffer(prefix);
              sb.append("Function signature mismatch on");
              sb.append(" return type and/or parameter");
              sb.append(" type/order - expected: '");
              sb.append(controlFunctions[i].getFunctionSignature());
              sb.append("', received '");
              sb.append(testFunctions[j].getFunctionSignature());
              sb.append('\'');
              messages.add(sb.toString());
            }
          }
        }
      }
    }

  } // END validateFunctions

  /**
   * Performs equality validation of the signatures based on the defined return
   * type and the types and order of the parameters. If all are equal, this
   * method returns <code>true</code> otherwise, it returns <code>false</code>
   * 
   * @param controlSig
   *          the expected signature
   * @param testSig
   *          the signature to validate
   * @return <code>true</code> if the signatures are equal otherwise
   *         <code>false</code>
   */
  private boolean signaturesAreEqual(String controlSig, String testSig) {

    // we're only going to pay attention to the return and parameter types.
    String controlRetType = fixType(
        controlSig.substring(0, controlSig.indexOf(' ')));
    String[] controlParamsTypes = getParamTypes(controlSig
        .substring(controlSig.indexOf('(') + 1, controlSig.indexOf(')'))
        .trim());

    String testRetType = fixType(testSig.substring(0, testSig.indexOf(' ')));
    String[] testParamsTypes = getParamTypes(testSig
        .substring(testSig.indexOf('(') + 1, testSig.indexOf(')')).trim());

    if (!controlRetType.equals(testRetType)) {
      return false;
    }

    if (controlParamsTypes.length != testParamsTypes.length) {
      return false;
    }

    for (int i = 0; i < controlParamsTypes.length; i++) {
      if (!controlParamsTypes[i].equals(testParamsTypes[i])) {
        return false;
      }
    }

    return true;

  } // END signaturesAreEqual

  /**
   * Inspects the provided <code>type</code> and will prepend the
   * <code>java.lang</code> package to the <code>type</code> if the type has no
   * package and the first character is upper case, otherwise, it will return
   * the provided <code>type</code> as is.
   */
  private String fixType(String type) {

    if (type == null || type.length() == 0) {
      return "";
    }

    String newType;
    // if the type has no package and the first character of the type is
    // uppercase, prepend the package 'java.lang' to the value.
    if (type.indexOf('.') < 0 && Character.isUpperCase(type.charAt(0))) {
      newType = "java.lang." + type;
    } else {
      newType = type;
    }

    return newType;

  } // END fixType

  /**
   * This method takes a String in the format of (without the quotes) 'String,
   * Object, javax.somepackage.SomeClass' and returns an array split on the
   * commas and trimmed of any whitespace.
   */
  private String[] getParamTypes(String paramString) {

    if (paramString == null || paramString.length() == 0) {
      return new String[] {};
    }

    return paramString.split("\\s*,\\s*");

  } // END getParamTypes

}
