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
 * @(#)ValidationTest.java	1.9 03/05/16
 */

package com.sun.ts.tests.interop.csiv2.common.validation;

import com.sun.ts.tests.interop.csiv2.common.parser.*;
import java.io.*;

public class ValidationTest {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: java " + ValidationTest.class.getName()
          + "<log.xml> <assertion-name>");
      System.exit(1);
    }

    String filename = args[0];
    String assertionName = args[1];

    Parser p = new Parser();
    String data = "";
    try {
      BufferedReader in = new BufferedReader(new FileReader(filename));
      String line = "";
      while ((line = in.readLine()) != null) {
        data += line + "\n";
      }
      in.close();
      CSIv2LogEntry csiv2LogEntry = p.parse(data);
      CSIv2LogValidator validator = new CSIv2LogValidator(assertionName,
          csiv2LogEntry,
          // new IOR0ValidationStrategy( false ),
          null, new RequestValidationStrategy(false, true, true, 0),
          new ResponseValidationStrategy(true, false));
      boolean result = validator.validate();
      System.out.println("Validation Result: " + (result ? "PASS" : "FAIL"));
    } catch (IOException e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
    } catch (ParseException e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
    } catch (ValidationException e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
    }
  }
}
