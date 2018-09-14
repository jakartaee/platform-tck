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
 * @(#)TestCode.java	1.7 03/05/16
 */

package com.sun.ts.tests.assembly.util.shared.resref.casesens;

import java.util.Properties;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnectionFactory;
import javax.jms.QueueConnection;
import javax.jms.TopicConnection;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;

public class TestCode {

  /** Prefix used for JNDI lookups */
  private static final String prefix = "java:comp/env/";

  /*
   * JNDI lookup names for resource manager connection factories. They differ
   * only by case.
   */
  protected static final String res1Lookup = prefix + "Aloha";

  protected static final String res2Lookup = prefix + "aloha";

  public static boolean testCaseSensitivity(TSNamingContext nctx) {
    QueueConnectionFactory res1;
    TopicConnectionFactory res2;
    QueueConnection conn1;
    TopicConnection conn2;
    boolean pass;

    try {
      TestUtil.logTrace("[TestCode] Looking up " + res1Lookup);
      res1 = (QueueConnectionFactory) nctx.lookup(res1Lookup);
      TestUtil.logTrace("[TestCode] Create QueueConnection");
      conn1 = res1.createQueueConnection();
      conn1.close();

      TestUtil.logTrace("[TestCode] Looking up " + res2Lookup);
      res2 = (TopicConnectionFactory) nctx.lookup(res2Lookup);
      TestUtil.logTrace("[TestCode] Create TopicConnection");
      conn2 = res2.createTopicConnection();

      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }

    return pass;
  }

}
