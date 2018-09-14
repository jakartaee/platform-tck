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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.common.taglibsig;

import com.sun.ts.tests.common.taglibsig.validation.ValidationConfiguration;

public class Test {

  public Test() {
  }

  public static void main(String[] args) {
    TagLibraryDescriptor tld = TagLibraryDescriptor.getInstance(
        "file:///files/projects/jakarta-taglibs/dist/standard/lib/standard.jar",
        "http://java.sun.com/jstl/core_rt");

    System.out.println();
    if (tld != null) {
      System.out.println("URI: " + tld.getURI());
      System.out.println("TAGLIB VERSION: " + tld.getTaglibraryVersion());
      System.out
          .println("CONTAINER VERSION: " + tld.getRequiredContainerVersion());
      TagEntry[] tagEntries = tld.getTagEntries();
      for (int i = 0; i < tagEntries.length; i++) {
        System.out.println("TAG NAME: " + tagEntries[i].getName());
        System.out.println("TAG BODY: " + tagEntries[i].getBody());
        AttributeEntry[] attrs = tagEntries[i].getAttributes();
        for (int j = 0; j < attrs.length; j++) {
          System.out.println("ATTRIBUTE NAME: " + attrs[j].getName());
          System.out.println("ATTRIBUTE TYPE: " + attrs[j].getType());
          System.out.println("ATTRIBUTE REQ: " + attrs[j].getRequired());
          System.out.println("ATTRIBUTE RTEXPR: " + attrs[j].getRtexpr());
        }
      }
    } else {
      System.out.println("OOOPPS");
    }

    TagLibraryDescriptor tld2 = TagLibraryDescriptor.getInstance(
        "file:///files/nightly/jstl/lib/standard.jar",
        "http://java.sun.com/jstl/fmt");

    System.out.println();
    if (tld2 != null) {
      System.out.println("URI: " + tld2.getURI());
      System.out.println("TAGLIB VERSION: " + tld2.getTaglibraryVersion());
      System.out
          .println("CONTAINER VERSION: " + tld2.getRequiredContainerVersion());
      TagEntry[] tagEntries = tld2.getTagEntries();
      for (int i = 0; i < tagEntries.length; i++) {
        System.out.println("TAG NAME: " + tagEntries[i].getName());
        System.out.println("TAG BODY: " + tagEntries[i].getBody());
        AttributeEntry[] attrs = tagEntries[i].getAttributes();
        for (int j = 0; j < attrs.length; j++) {
          System.out.println("ATTRIBUTE NAME: " + attrs[j].getName());
          System.out.println("ATTRIBUTE TYPE: " + attrs[j].getType());
          System.out.println("ATTRIBUTE REQ: " + attrs[j].getRequired());
          System.out.println("ATTRIBUTE RTEXPR: " + attrs[j].getRtexpr());
        }
      }
    } else {
      System.out.println("OOOPPS");
    }

    ValidationConfiguration configuration = new ValidationConfiguration();
    configuration.addValidator(ValidationConfiguration.URI_VALIDATOR);

    TagLibraryComparitor comparitor = new TagLibraryComparitor(configuration);
    String[] messages = comparitor.compare(tld, tld2);
    if (messages.length == 0) {
      System.out.println("EQUAL");
    } else {
      for (int i = 0; i < messages.length; i++) {
        System.out.println("ERRORS\n==================");
        System.out.println(messages[i]);
      }
    }

  }
}
