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

package com.sun.ts.lib.util;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;

public final class AssertionMapper {
  private static final String NO_DESC = "See assertion html documents.";

  private static final String INVALID = "WARN: invalid assertion_id: ";

  public final static String NL = System.getProperty("line.separator");

  public final static String NL2 = NL;

  private static boolean debug;

  private static boolean showRetrievedAssertions;

  protected static String assertionDir;

  private static String[] assertionFiles;

  private static Map fileName2Map = new HashMap();

  private static Map aMap = new HashMap();

  // xmlFileNames that parser has failed to load, and thus will not load it
  // again.
  private static Set unloadable = new HashSet();

  static {
    debug = Boolean.getBoolean("AssertionMapper.debug");
    showRetrievedAssertions = Boolean.getBoolean("show.retrieved.assertions");
    assertionDir = System.getProperty("assertion.dir");
    if (assertionDir == null)
      assertionDir = TSTestFinder.TS_HOME + File.separator + "internal"
          + File.separator + "docs";

    DirectoryScanner ds = new DirectoryScanner();
    ds.setBasedir(assertionDir);
    ds.addDefaultExcludes();
    ds.setCaseSensitive(false);
    ds.setIncludes(new String[] { "**/*Assertions.xml" });
    ds.scan();
    assertionFiles = ds.getIncludedFiles();
    System.out.println(
        assertionFiles.length + " assertion files under " + assertionDir);
    for (int i = 0; i < assertionFiles.length; i++) {
      System.out.println(assertionFiles[i]);
    }
  }

  private AssertionMapper() {
  }

  /**
   * main program to test AssertionComparator, the nested class.
   */
  public static void main(String[] args) {
    String[] s = { "JMS:SPEC:130.3", "EJB:JAVADOC:27", "JMS:SPEC:130.19",
        "JSP:SPEC:130", "JMS:SPEC:130.9.5", "JMS:SPEC:130.9" };
    Arrays.sort(s, AssertionComparator.getInstance());
    System.out.println("After sorting");
    for (int i = 0; i < s.length; i++) {
      System.out.println(s[i]);
    }

  }

  public static void log(String s) {
    System.out.println(s);
  }

  /*
   * Retrieves assertion descriptions for assertion_ids.
   * 
   * @param assertionIds separated by ;
   * 
   * @param file test source file that contains the assertion ids. For debug
   * purpose.
   * 
   * @return a single string containing all assertion descriptions separated by
   * new line.
   *
   */
  public static String getAssertionDescriptions(String assertionIds,
      File file) {
    String filePath = file.getPath();
    if (assertionIds == null || assertionIds.length() == 0) {
      System.out.println("WARN: no value for assertion_ids (" + filePath + ")");
      return NO_DESC;
    }
    StringTokenizer st = new StringTokenizer(assertionIds, ";,\n\r\f\t");
    int countId = st.countTokens();
    String[] idArray = new String[countId];
    for (int i = 0; i < idArray.length; i++) {
      idArray[i] = st.nextToken().trim();
    }
    Arrays.sort(idArray, AssertionComparator.getInstance());
    Set addedAssertions = new HashSet(); // avoid one assertion to be added
                                         // multiple times
    StringBuffer retval = new StringBuffer();
    for (int i = 0; i < idArray.length; i++) {
      String aDescription = getDescription0(idArray[i], filePath,
          addedAssertions);
      if (aDescription != null && aDescription.length() > 0) {
        retval.append(aDescription).append(NL2);
      }
    }
    return retval.toString().trim();
  }// getAssertionDescription

  /**
   * @param token
   *          a token in assertion_ids. It has not beenn validated or split.
   * @param filePath
   *          the full path to the test source code that contained the assertion
   *          ids. For example,
   *          /files/ts/src/com/sun/ts/tests/jsp/syntax/Client.java.
   * @param addedAssertion
   *          set to keep track of assertions that have been added to the
   *          result.
   * @return a complete description of this assertion id. If it's not a valid
   *         assertion, null or empty string is returned. A complete description
   *         includes description of all of its ancestors, its own description,
   *         and that of all of its descendents.
   */
  private static String getDescription0(String token, String filePath,
      Set addedAssertions) {
    String aDescription = null; // assertion description for token(id)
    int pos1 = token.indexOf(":");
    if (pos1 == -1) {
      System.out.println(INVALID + token + " (" + filePath + ")");
      return null;
    }
    String first = token.substring(0, pos1);
    int pos2 = token.indexOf(":", pos1 + 1);
    if (pos2 == -1) {
      System.out.println(INVALID + token + " (" + filePath + ")");
      return null;
    }
    String second = token.substring(pos1 + 1, pos2);
    String third = token.substring(pos2 + 1); // the number, e.g., 14

    // for example, first=JMS, second=SPEC, third=14.1
    StringBuffer xmlFileName = new StringBuffer();
    xmlFileName.append(first);
    xmlFileName.append(Character.toUpperCase(second.charAt(0)));
    xmlFileName.append(second.substring(1).toLowerCase());
    xmlFileName.append("Assertions.xml");

    // some old assertion doc use 8, instead of JMS:SPEC:8
    String fn = xmlFileName.toString();
    Map assertionMap = (Map) fileName2Map.get(fn);
    if (assertionMap == null) { // has not been read, or has failed to load
      if (!unloadable.contains(fn)) {
        new HelpHandler().load(fn, first);
      }
    }
    assertionMap = (Map) fileName2Map.get(fn);
    StringBuffer resultBuffer = new StringBuffer();
    if (assertionMap == null) { // failed to read xml file
      resultBuffer.append(token).append("  ").append(NO_DESC);
      return resultBuffer.toString();
    }

    // get description of all ancestors. Assume ancestors and descendants are
    // in the same map.
    int dotPosition = token.indexOf(".", pos2);
    while (dotPosition != -1) {
      String upperId = token.substring(0, dotPosition);
      if (!addedAssertions.contains(upperId)) {
        String upperDesc = (String) assertionMap.get(upperId);
        if (upperDesc == null || upperDesc.length() == 0) {
          System.out.println(
              "WARN: no description for " + upperId + " in " + filePath);
          upperDesc = NO_DESC;
        }
        resultBuffer.append(upperId).append("  ").append(upperDesc).append(NL2);
        addedAssertions.add(upperId);
      }
      dotPosition = token.indexOf(".", dotPosition + 1);
    }

    // get description for itself and all descendants
    if (!addedAssertions.contains(token)) {
      String thisDescription = (String) assertionMap.get(token);
      if (thisDescription == null) {
        thisDescription = NO_DESC;
        System.out
            .println("WARN: no description for " + token + " in " + filePath);
      }
      resultBuffer.append(token).append("  ").append(thisDescription)
          .append(NL2);
      addedAssertions.add(token);
    }

    List keyList = new ArrayList();
    String tokenDot = token + '.';
    for (Iterator i = assertionMap.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();
      if (key.startsWith(tokenDot) && !addedAssertions.contains(key)) {
        keyList.add(key);
      }
    }
    Collections.sort(keyList, AssertionComparator.getInstance());
    for (int i = 0, n = keyList.size(); i < n; i++) {
      String k = (String) keyList.get(i);
      resultBuffer.append(k).append("  ").append(assertionMap.get(k))
          .append(NL2);
      addedAssertions.add(k);
    }
    return resultBuffer.toString().trim();
    // TODO: remove <code> <tt> from assertion description
  }

  // ------------------------ static nested class ---------------------------
  public static class AssertionComparator implements Comparator {
    private static AssertionComparator instance = new AssertionComparator();

    private AssertionComparator() {
    }

    public static AssertionComparator getInstance() {
      return instance;
    }

    public int compare(Object o1, Object o2) {
      String s1 = (String) o1;
      String s2 = (String) o2;
      int pos1 = s1.lastIndexOf(":");
      int pos2 = s2.lastIndexOf(":");
      String word1 = null;
      String word2 = null;
      if (pos1 != -1) {
        word1 = s1.substring(0, pos1);
      }
      if (pos2 != -1) {
        word2 = s2.substring(0, pos2);
      }

      String numPart1 = null;
      String numPart2 = null;
      // to handle invalid ids like 145, 234.
      if (pos1 == -1 && pos2 == -1) {
        numPart1 = s1;
        numPart2 = s2;
      } else {
        // one is null, but not both
        if (word1 == null || word2 == null) {
          return -1;
        }
        // both have wordpart
        int wordCompare = word1.compareTo(word2);
        if (wordCompare != 0) {
          return wordCompare;
        }
        // continue to compare number part. pos1 and pos2 are not -1 now.
        numPart1 = s1.substring(pos1 + 1);
        numPart2 = s2.substring(pos2 + 1); // for example, 1.1.4.5
      }

      StringTokenizer st1 = new StringTokenizer(numPart1, ".");
      StringTokenizer st2 = new StringTokenizer(numPart2, ".");
      int size1 = st1.countTokens();
      int size2 = st2.countTokens();
      int biggerSize = (size1 == size2) ? size1
          : (size1 > size2) ? size1 : size2;

      int[] int1 = new int[biggerSize];
      int[] int2 = new int[biggerSize];
      fillIntArray(st1, int1);
      fillIntArray(st2, int2);

      for (int i = 0; i < biggerSize; i++) {
        int diff = int1[i] - int2[i];
        if (diff != 0) {
          return diff;
        }
        // both are zero, or the same positive number
        if (int1[i] == 0) {
          return -1;
        }
      }
      return -1;
    }

    /**
     * @param int1
     *          bigger of the two. equal to bigger than number of tokens.
     */
    private void fillIntArray(StringTokenizer st, int[] int1) {
      for (int i = 0; i < int1.length && st.hasMoreTokens(); i++) {
        try {
          int1[i] = Integer.parseInt(st.nextToken());
        } catch (NumberFormatException exp) {
          break; // stop once we hit a non-number. Unitializaed part will be 0.
        }
      }
    }
  }

  // ------------------------------------------------------------------------
  /**
   * sax2 event handler is too slow. So stick to HandlerBase.
   */
  public static class HelpHandler
      // extends DefaultHandler {
      extends HandlerBase {
    private String xmlFileName;

    private boolean b_assertions;

    private boolean b_assertion;

    private boolean b_id;

    private boolean b_description;

    private boolean b_technology;

    private String currentId;

    private String currentDescription;

    private String techType; // JMS:SPEC, JSP:JAVADOC, etc

    private String specOrJavadoc; // spec, or javadoc

    private Map aFileMap;

    private boolean alreadyPut;

    private String parentDir;

    public HelpHandler() {
    }

    public void load(String xmlFileName, String technologyName) {
      String fn = null;
      for (int i = 0; i < assertionFiles.length; i++) {
        int index = assertionFiles[i].lastIndexOf(File.separator) + 1;
        String fName = assertionFiles[i].substring(index);
        if (fName.equalsIgnoreCase(xmlFileName)) {
          fn = assertionFiles[i];
          break;
        }
      }
      if (fn == null) {
        System.out.println("WARN: failed to find file " + xmlFileName
            + " under " + assertionDir + ", verify the technology name ["
            + technologyName + "] is spelled correcting in the test clients");
        return;
      }
      this.xmlFileName = xmlFileName;
      try {
        // Store the parent directory of this file for the resolver to use
        System.err.println("%%%%%%%%%% Parsing file \"" + fn + "\" %%%%%%%%%");
        File fileFN = new File(fn);
        parentDir = fileFN.getParent();
        System.err.println("%%%%%%%%%% parentDir set to (resolver uses this) \""
            + parentDir + "\" %%%%%%%%%");

        InputSource is = new InputSource(
            new FileInputStream(new File(assertionDir, fn)));
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(is, this);
      } catch (SAXException se) {
        se.printStackTrace();
        unloadable.add(xmlFileName);
        System.out.println("Will skip all assertions in " + xmlFileName);
      } catch (IOException fe) {
        fe.printStackTrace();
        unloadable.add(xmlFileName);
        System.out.println("Will skip all assertions in " + xmlFileName);
      } catch (ParserConfigurationException pe) {
        pe.printStackTrace();
        unloadable.add(xmlFileName);
        System.out.println("Will skip all assertions in " + xmlFileName);
      }
    }

    /*
     * Implementation of the entity resolver interface. This allows CTS
     * developers to use entity declarations in their assertion docs provided
     * the assertion doc (the referencing doc) and the referenced docs are in
     * the same directory.
     */
    public InputSource resolveEntity(String publicId, String systemId) {
      System.err.println("publicId \"" + publicId + "\"");
      System.err.println("systemId \"" + systemId + "\"");
      String result = null;
      String fileName = systemId.substring(systemId.lastIndexOf("/") + 1);
      String possibleLocation = assertionDir + File.separator + parentDir
          + File.separator + fileName;
      File possibleFile = new File(possibleLocation);
      if (possibleFile.isFile()) {
        result = possibleLocation;
      } else {
        System.err.println(
            "%%%%%%% Error could not resolve file \"" + fileName + "\"");
        result = systemId;
      }
      System.err
          .println("%%%%%%%% Entity Resolver returning \"" + result + "\"");
      return new InputSource(result);
    }

    public void startElement(String localName, AttributeList attrs)
        throws SAXException {
      // public void startElement(String uri,
      // String localName,
      // String qName,
      // Attributes attributes)
      // throws SAXException {
      if (localName.equalsIgnoreCase("assertions")) {
        b_assertions = true;
        aFileMap = new HashMap();
      } else if (localName.equalsIgnoreCase("assertion") && b_assertions) {
        b_assertion = true;
      } else if (localName.equalsIgnoreCase("id") && b_assertions
          && b_assertion) {
        b_id = true;
      } else if (localName.equalsIgnoreCase("description") && b_assertions
          && b_assertion) {
        b_description = true;
      } else if (localName.equalsIgnoreCase("sub-assertions") && b_assertions
          && b_assertion) {
        putIdAndDescription();
      } else if (localName.equalsIgnoreCase("spec")
          || localName.equalsIgnoreCase("javadoc")) {
        specOrJavadoc = localName;
      } else if (localName.equalsIgnoreCase("technology")) {
        b_technology = true;
      }
    }

    public void endElement(String localName) throws SAXException {
      // public void endElement(String uri,
      // String localName,
      // String qName)
      // throws SAXException {
      if (localName.equalsIgnoreCase("assertions")) {
        fileName2Map.put(this.xmlFileName, aFileMap);
        b_assertions = false;
        return;
      } else if (localName.equalsIgnoreCase("assertion")) {
        putIdAndDescription();
        b_assertion = false;
        b_id = false;
        b_description = false;
      } else if (localName.equalsIgnoreCase("id") && b_assertions
          && b_assertion) {
        b_id = false;
      } else if (localName.equalsIgnoreCase("description") && b_assertions
          && b_assertion) {
        b_description = false;
      } else if (localName.equalsIgnoreCase("technology")) {
        b_technology = false;
      }
    }

    public void characters(char[] ch, int start, int length)
        throws SAXException {
      if (b_id && b_description) {
        System.out.println(
            "WARN: invalid state: in both id and description element.");
      }
      if (b_id) {
        currentId = new String(ch, start, length);
        alreadyPut = false;
      } else if (b_description) {
        String content = new String(ch, start, length);
        if (currentDescription == null) {
          currentDescription = content;
        } else { // some descriptions have formatting elements <code>,
                 // <tt>,which
          // may be treated as nested elements of description by the parser
          currentDescription += " " + content;
        }
        alreadyPut = false;
      } else if (b_technology) {
        if (specOrJavadoc == null) {
          System.out
              .println("Should have encountered javadoc or spec element!");
        } else {
          String tech = new String(ch, start, length).trim();
          techType = (tech + ':' + specOrJavadoc).toUpperCase();
        }
      }
    } // characters

    /**
     * need to put at end of assertion, or start of sub-assertion
     */
    private void putIdAndDescription() {
      if (alreadyPut) {
        return;
      }
      if (currentId == null || currentId.length() == 0) {
        System.out.println("WARN: null id in " + xmlFileName);
        return;
      }
      currentId = currentId.trim();
      // some id may be like 14, while it really should be like JMS:SPEC:14
      if (techType != null && !currentId.startsWith(techType)) {
        currentId = techType + ':' + currentId;
      }
      if (currentDescription == null || currentDescription.length() == 0) {
        System.out.println("WARN: for id:[" + currentId
            + "] null description in " + xmlFileName);
        currentDescription = NO_DESC;
      } else {
        currentDescription = currentDescription.trim();
      }
      if (showRetrievedAssertions) {
        System.out.println(currentId + "  " + currentDescription);
      }
      aFileMap.put(currentId, currentDescription);
      alreadyPut = true;
      currentId = null;
      currentDescription = null;
    }

  } // nested class

}// main class
