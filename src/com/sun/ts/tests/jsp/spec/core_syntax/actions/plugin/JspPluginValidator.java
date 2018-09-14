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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin;

import com.sun.ts.tests.common.webclient.validation.ValidationStrategy;
import com.sun.ts.tests.common.webclient.WebTestCase;
import com.sun.ts.lib.util.TestUtil;

import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;

/**
 * This validator will, loosely, validate the generated output of a jsp:plugin
 * action.
 */
public class JspPluginValidator implements ValidationStrategy {
  private static final String NL = System.getProperty("line.separartor", "\n");

  /**
   * A map to provided a relation ship between HTML spec'd attributes and the
   * alternate attributes suggested by the Java Plugin documentation.
   */
  private static HashMap<String, String> attributeMap = new HashMap<String, String>();
  static {
    attributeMap.put("code", "java_code");
    attributeMap.put("codebase", "java_codebase");
    attributeMap.put("archive", "java_archive");
    attributeMap.put("object", "java_object");
    attributeMap.put("type", "java_type");
  }

  /**
   * Creates a new JspPluginValidator instance.
   */
  public JspPluginValidator() {
  }

  /**
   * Validates the response from the search using a string or series of strings
   * obtained from the WebTestCase. The order and case of the Strings found in
   * the response are not important.
   * 
   * @param testCase
   * @return
   */
  public boolean validate(WebTestCase testCase) {

    List<String> searchStrings = testCase.getSearchStrings();
    List<String> unexpectedSearchStrings = testCase
        .getUnexpectedSearchStrings();
    boolean passed = true;
    String response = null;

    // Any status code aside from a 200 will result in
    // test failure.
    String statusCode = testCase.getResponse().getStatusCode();
    if (!statusCode.equals("200")) {
      TestUtil.logErr("[CaseInsensitiveValidator] Expected a status code "
          + "of 200.  Received: " + statusCode);
      return false;
    }

    // get response body
    try {
      response = testCase.getResponse().getResponseBodyAsRawString()
          .toLowerCase();

      String[] htmlTokens = prepareTokens(
          new StringTokenizer(response, "<>='\";\n\t "));

      // If tracing is enabled, display the tokens returned by
      // prepareTokens.
      if (TestUtil.traceflag) {
        StringBuffer sb = new StringBuffer(255);
        for (int i = 0; i < htmlTokens.length; i++) {
          sb.append(htmlTokens[i]).append(" ");
          if (i > 0 && (i % 5) == 0) {
            sb.append(NL);
          }
        }
        TestUtil.logTrace("Parsed Tokens from response" + NL
            + "------------------------------------------" + NL
            + sb.toString());
      }

      // check the response for either an object or embed tag in the
      // response
      if (response.indexOf("<embed") < 0 && response.indexOf("<object") < 0) {
        TestUtil.logErr("[JspPluginValidator] Unable to locate either an"
            + "OBJECT or EMBED tag in the server's response.");
        return false;
      }

      List<String> itemsNotFound = scanForExpectedValues(response, htmlTokens,
          searchStrings);

      // check to see if we had any misses
      if (itemsNotFound.size() > 0) {
        passed = false;
        TestUtil.logErr("[JspPluginValidator]  Unable to find the following"
            + "search strings in the response");
        for (int i = 0, size = itemsNotFound.size(); i < size; i++) {
          TestUtil.logErr("\t" + itemsNotFound.get(i));
        }
        TestUtil.logErr("[JspPluginValidator] Server's response:" + NL
            + "-------------------------------------------------" + NL
            + response);
      }

      if (scanForUnexpectedValues(response, unexpectedSearchStrings)) {
        passed = false;
      }

    } catch (IOException ioe) {
      TestUtil.logErr("[CaseInsensitiveValidator] Unexpected IOException "
          + "reading response body.", ioe);
    } catch (NullPointerException npe) {
      TestUtil.logErr(
          "[CaseInsensitiveValidator] Unexpected HullPointerException when calling "
              + "getResponseBodyAsRawString",
          npe);
    }

    return passed;
  }

  /**
   * Scan the response for anything that the test doesn't expect.
   * 
   * @param response
   *          - the server's response
   * @param unexpectedSearchStrings
   *          - a List of strings to search for
   * @return false if none of the strings are found or true on the first match
   */
  private static boolean scanForUnexpectedValues(String response,
      List<String> unexpectedSearchStrings) {
    boolean found = false;
    if (unexpectedSearchStrings != null) {
      for (int i = 0, size = unexpectedSearchStrings.size(); i < size; i++) {
        String sString = (String) unexpectedSearchStrings.get(i);
        TestUtil.logTrace("[JspPluginValidator] Searching from unexpected"
            + " search string '" + sString + "' in the server's response...");
        if (response.indexOf((String) unexpectedSearchStrings.get(i)) > -1) {
          TestUtil.logTrace("[JspPluginValidator] '" + sString + "' found!  "
              + "Test fails!");
          found = true;
          break;
        }
      }
    }
    return found;
  }

  /**
   * Scan the plugin attributes or other data aside from OBJECT or EMBED tags.
   * The search works as follows: - If the search string doesn't contain an
   * equal ('=') sign, then do an indexOf against the response body using the
   * search string as the argument. - If the search string contains an equal
   * ('=') sign, then tokenize the search string to get the name and the value.
   * Check to see if the name token and the next token being checked are the
   * same. If they are, set found to true. If they aren't the same, check to see
   * if this name has an alternate name. If it does, use the altername name and
   * check to see if it's equal to the current token. If they are, set found to
   * true. - If found is false, then add the search string that couldn't be
   * found to a list of search misses to be displayed after the loop completes.
   * 
   * @param response
   *          - the server's response
   * @param htmlTokens
   *          - a tokenized version of the server's response
   * @param searchStrings
   *          - the strings to search for.
   * @return a list containing the search strings that were not found. If all
   *         search strings were found, then an empty list will be returned.
   */
  private static List<String> scanForExpectedValues(String response,
      String[] htmlTokens, List<String> searchStrings) {
    List<String> itemsNotFound = new ArrayList<String>();

    for (int i = 0, size = searchStrings.size(); i < size; i++) {
      boolean found = false;
      String currentSearchString = (String) searchStrings.get(i);
      TestUtil.logTrace("[JspPluginValidator] Searching for '"
          + currentSearchString + "'....");
      if (currentSearchString.indexOf("=") > -1) {
        String[] tokStrings = tokenizeNameValuePair(currentSearchString);
        for (int j = 0; j < htmlTokens.length; j++) {
          if (tokStrings[0].equals(htmlTokens[j])) {
            if (tokStrings[1].equals(htmlTokens[++j])) {
              found = true;
              j--;
            }
          } else if (hasAlternateAttributeName(tokStrings[0])) {
            if (getAlternateAttributeName(tokStrings[0])
                .equals(htmlTokens[j])) {
              if (tokStrings[1].equals(htmlTokens[++j])) {
                found = true;
                j--;
              }
            }
          }
        }
        if (!found) {
          itemsNotFound.add(currentSearchString);
        } else {
          TestUtil.logTrace("[JspPluginValidator] Search string '"
              + currentSearchString + "' found!");
        }
      } else {
        if (response.indexOf(currentSearchString) < 0) {
          itemsNotFound.add(currentSearchString);
        } else {
          TestUtil.logTrace("[JspPluginValidator] Search string '"
              + currentSearchString + "' found!");
        }
      }
    }
    return itemsNotFound;
  }

  /**
   * Builds an array based of the StringTokenizer provided. If the method finds
   * a sequence of tokens like: param name paramName value paramValue - or -
   * param value paramValue name paramName It will not add the tokens 'param',
   * 'name', or 'value', but will add the paramName and paraValue to the final
   * token list (in that order).
   * 
   * @param st
   *          - the StringTokenizer to build the array from
   * @return an array of String values based off the server's response
   */
  private static String[] prepareTokens(StringTokenizer st) {
    List<String> finalTokens = new ArrayList<String>();

    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if (token.equals("param")) {
        String name = null;
        String value = null;
        token = st.nextToken();
        if (token.equals("name")) {
          name = st.nextToken();
        }
        if (token.equals("value")) {
          value = st.nextToken();
        }
        finalTokens.add(name);
        finalTokens.add(value);
      } else {
        finalTokens.add(token);
      }
    }
    return (String[]) finalTokens.toArray(new String[finalTokens.size()]);
  }

  /**
   * Used for tokenizing the search strings configured by the test case.
   * 
   * @param nameValuePair
   *          a search string in the format of 'name=value'
   * @return a String array representation of the name value pair
   */
  private static String[] tokenizeNameValuePair(String nameValuePair) {
    StringTokenizer st = new StringTokenizer(nameValuePair.trim().toLowerCase(),
        "=");
    List<String> list = new ArrayList<String>();
    while (st.hasMoreTokens()) {
      list.add(st.nextToken());
    }
    return (String[]) list.toArray(new String[list.size()]);
  }

  /**
   * Returns the alternate attribute name for the provided name.
   * 
   * @param name
   *          - the attribute name whose alternate name will be returned
   * @return this name's alternate attribute name
   */
  private static String getAlternateAttributeName(String name) {
    return (String) attributeMap.get(name);
  }

  /**
   * Determines if the provide name has an alternate name.
   * 
   * @param name
   *          - the attribute name
   * @return - true if the attribute name provided has an alternative.
   */
  private static boolean hasAlternateAttributeName(String name) {
    return attributeMap.containsKey(name);
  }
}
