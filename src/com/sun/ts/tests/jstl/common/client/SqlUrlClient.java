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

package com.sun.ts.tests.jstl.common.client;

import java.util.Properties;
import java.util.Enumeration;
import com.sun.ts.tests.jstl.common.JstlTckConstants;
import com.sun.ts.tests.common.webclient.WebTestCase;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

public class SqlUrlClient extends AbstractUrlClient
    implements JstlTckConstants {
  protected Properties dbArgs = new Properties();

  @Override
  public void setup(String[] args, Properties p) throws Fault {
    for (int i = 0; i < JSTL_DB_PROPS.length; i++) {
      String s = p.getProperty(JSTL_DB_PROPS[i]);
      if (s != null) {
        dbArgs.setProperty(JSTL_DB_PROPS[i], s.trim());
      }
    }
    super.setup(args, p);
  }

  @Override
  public void setTestProperties(WebTestCase testCase) {
    super.setTestProperties(testCase);
    HttpRequest httpReq = testCase.getRequest();
    Enumeration enumm = dbArgs.propertyNames();
    for (; enumm.hasMoreElements();) {
      String name = (String) enumm.nextElement();
      String value = dbArgs.getProperty(name);
      httpReq.addRequestHeader(name, value);
    }
  }

  private String aggregateParams(String url) {
    StringBuffer newParams = new StringBuffer();
    Enumeration enumm = dbArgs.propertyNames();
    int size = dbArgs.size();
    for (int i = 0; enumm.hasMoreElements(); i++) {
      String name = (String) enumm.nextElement();
      String value = dbArgs.getProperty(name);
      newParams.append(name + "=" + value);
      if (i < (size - 1))
        newParams.append("&");
    }

    // insert these parameters into the URL as appropriate
    if (newParams.length() > 0) {
      int questionMark = url.indexOf('?');
      StringBuffer workingUrl = new StringBuffer(url);
      if (questionMark == -1) {
        int httpMark = url.indexOf("HTTP/");
        workingUrl.insert(httpMark - 1, ("?" + newParams));
      } else {
        workingUrl.insert(questionMark + 1, (newParams + "&"));
      }
      return workingUrl.toString();
    } else {
      return url;
    }
  }
}
