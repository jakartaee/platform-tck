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
 * @(#)TopicCode.java	1.6 03/05/16
 */

package com.sun.ts.tests.assembly.util.shared.resref.scope;

import java.util.Properties;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicConnection;
import javax.jms.TopicConnection;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;

public class TopicCode {

  /** Prefix used for JNDI lookups */
  private static final String prefix = "java:comp/env/jms/";

  /*
   * JNDI lookup names for resource manager connection factories. They differ
   * only by case.
   */
  protected static final String resLookup = prefix + "myFactory";

  public static boolean checkYourTopic(TSNamingContext nctx) {
    TopicConnectionFactory res;
    TopicConnection conn;
    boolean pass;

    try {
      TestUtil.logTrace("[TopicCode] Looking up " + resLookup);
      res = (TopicConnectionFactory) nctx.lookup(resLookup);
      TestUtil.logTrace("[TopicCode] Create TopicConnection");
      conn = res.createTopicConnection();
      conn.close();

      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }

    return pass;
  }

}
