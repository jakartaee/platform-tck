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

package com.sun.ts.tests.assembly.util.shared.resref.single.appclient;

import java.io.Serializable;
import java.io.IOException;
import java.util.Properties;
import java.util.Date;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import java.net.URL;
import java.net.URLConnection;

import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnectionFactory;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;

public class TestCode {

  /** Prefix used for JNDI lookups */
  private static final String prefix = "java:comp/env/";

  /*
   * JNDI lookup names for resource manager connection factories.
   */
  protected static final String dbLookup = prefix + "jdbc/DB1";

  protected static final String urlLookup = prefix + "url/URL";

  protected static final String queueLookup = prefix
      + "jms/myQueueConnectionFactory";

  protected static final String topicLookup = prefix
      + "jms/myTopicConnectionFactory";

  public static boolean testDatasource(TSNamingContext nctx) {
    DataSource ds;
    Connection connection;

    try {
      TestUtil.logTrace("[TestCode] looking up " + dbLookup);
      ds = (DataSource) nctx.lookup(dbLookup);
      TestUtil.logTrace("[TestCode] get a new DB connection...");
      connection = ds.getConnection();
      connection.close();
    } catch (Exception e) {
      TestUtil.logErr("RestTest: Caught exception: " + e, e);
      return false;
    }

    return true;
  }

  public static boolean testURL(TSNamingContext nctx) {
    Properties props;
    String resRef;
    URL myUrl;
    URLConnection urlConnection;

    try {
      TestUtil.logTrace("[TestCode] looking up " + urlLookup);
      myUrl = (java.net.URL) nctx.lookup(urlLookup);
      TestUtil.logTrace("[TestCode] get a new URL connection...");
      urlConnection = myUrl.openConnection();

      props = TestUtil.getResponseProperties(urlConnection);
      resRef = props.getProperty("resourceref");

      if ((null == resRef) || (!resRef.equals("true"))) {
        TestUtil.logErr("ResRef: Invalid connection!");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("RestTest: Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }

    return true;
  }

  public static boolean testQueue(TSNamingContext nctx) {
    QueueConnectionFactory queueFact;

    try {
      TestUtil.logTrace("[TestCode] looking up " + queueLookup);
      queueFact = (QueueConnectionFactory) nctx.lookup(queueLookup);
    } catch (Exception e) {
      TestUtil.logErr("RestTest: Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }

    return true;
  }

  public static boolean testTopic(TSNamingContext nctx) {
    TopicConnectionFactory topicFact;

    try {
      TestUtil.logTrace("[TestCode] looking up " + topicLookup);
      topicFact = (TopicConnectionFactory) nctx.lookup(topicLookup);
    } catch (Exception e) {
      TestUtil.logErr("RestTest: Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }

    return true;
  }

}
