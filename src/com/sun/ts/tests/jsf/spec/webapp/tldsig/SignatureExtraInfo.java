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
 * @(#)SignatureExtraInfo.java	1.1 04/11/05
 */
package com.sun.ts.tests.jsf.spec.webapp.tldsig;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import javax.servlet.jsp.tagext.ValidationMessage;

/**
 * <p>
 * This class provides the foundation for performing validation of taglibraries.
 * </p>
 *
 * TCKs wishing to perform such validation need to provide the following:
 * <ul>
 * <li>a concrete implementation of this class</li>
 * <li>A version of the signaturetest-template.tld which refers to the concrete
 * <code>SignatureExtraInfo</code> mentioned above</li>
 * <li>A version of the web-template.xml to be included in their archive. This
 * template has a listener reference for <code>SignatureInitListener</code></li>
 * <li>A JSP page that will be invoked once the archive is deployed which
 * contains taglib directives for all of the tag libraries that need to be
 * tested, and uses the SignatureTestTag at some point in the page so that the
 * <code>SignatureExtraInfo</code> implementation is invoked.</li>
 * </ul>
 */
public abstract class SignatureExtraInfo extends TagExtraInfo {

  // ----------------------------------------------- Methods from TagExtraInfo

  public ValidationMessage[] validate(TagData tagData) {

    String[][] tldTestInfo = getTaglibDescriptorInfo();
    ServletContext context = ServletContextHolder.getServletContext();

    List validationMessages = new ArrayList();
    List passedLibraryList = new ArrayList();

    for (int i = 0; i < tldTestInfo.length; i++) {

      TCKTagLibraryInfo controlTli = new TCKTagLibraryInfo("",
          tldTestInfo[i][0], context);

      TagLibraryInfo[] testTlis = getTagInfo().getTagLibrary()
          .getTagLibraryInfos();

      boolean found = false;
      for (int j = 0; j < testTlis.length; j++) {
        if (tldTestInfo[i][1].equals(testTlis[j].getReliableURN())) {
          found = true;
          TaglibSigValidator validator = new TaglibSigValidator(controlTli,
              testTlis[j]);
          String[] messages = validator.validate();
          if (messages.length == 0) {
            passedLibraryList
                .add("Taglibrary '" + tldTestInfo[i][1] + "' PASSED");
          } else {
            validationMessages
                .add("Taglibrary '" + tldTestInfo[i][1] + "' FAILED");
            validationMessages.addAll(Arrays.asList(messages));
          }
        }
      }

      if (!found) {
        String message = "Unable to obtain TagLibraryInfo from "
            + "container for URI '" + tldTestInfo[i][1] + '\'';
        validationMessages.add(message);
      }
    }

    context.setAttribute("com.sun.tck.taglibsig.failedmessages",
        validationMessages);
    context.setAttribute("com.sun.tck.taglibsig.passedmessages",
        passedLibraryList);

    return new ValidationMessage[] {};

  } // END validate

  // ------------------------------------------------------- Protected Methods

  /**
   * Return two dimensional array containing a mapping where the first element
   * is the resource that contains the 'master' tld, the second element contains
   * the well known URI for which we can obtain a matching TagLibraryInfo from
   * the container. Example: return new String[][] { { "/jsf-core.tld",
   * "http://java.sun.com/jsf/core" } };
   */
  protected abstract String[][] getTaglibDescriptorInfo();

}
