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

package com.sun.ts.lib.porting;

import java.util.*;
import java.io.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.deliverable.*;

/**
 * This class acts as a Factory object for creating an implementation specific
 * instance of the TSJMSAdminInterface based on the value of the ts.jte
 * property, porting.ts.jms.class.1
 *
 * @author Kyle Grucci
 */
public class TSJMSAdmin {
  private static PropertyManagerInterface propMgr = null;

  private static Hashtable htQueues1 = null;

  private static Hashtable htTopics1 = null;

  private static Hashtable htQueues2 = null;

  private static Hashtable htTopics2 = null;

  private static Hashtable htTopicConnectionFactories1 = null;

  private static Hashtable htQueueConnectionFactories1 = null;

  private static Hashtable htConnectionFactories1 = null;

  private static Hashtable htTopicConnectionFactories2 = null;

  private static Hashtable htQueueConnectionFactories2 = null;

  private static Hashtable htConnectionFactories2 = null;

  /* List of prefixes for test directories using JMS factories */
  private static String factTestPrefixes[] = { "jms", "ejb", "ejb30",
      "jsp" + File.separator + "spec" + File.separator + "tagext"
          + File.separator + "resource",
      "appclient" + File.separator + "deploy" + File.separator + "resref",
      "servlet" + File.separator + "spec" + File.separator + "annotation",
      "servlet" + File.separator + "platform" + File.separator + "deploy"
          + File.separator + "resref",
      "webservices" + File.separator + "handler" + File.separator + "localtx",
      "webservices" + File.separator + "handlerEjb" + File.separator
          + "localtx", };

  public static TSJMSAdminInterface getTSJMSAdminInstance(PrintWriter writer,
      String sClassName) throws Exception {
    return createInstance(sClassName, writer);
  }

  private static TSJMSAdminInterface createInstance(String sClassName,
      PrintWriter writer) throws Exception {
    try {
      propMgr = DeliverableFactory.getDeliverableInstance()
          .getPropertyManager();
      // create and initialize a new instance of the Deployment class
      Class c = Class.forName(propMgr.getProperty(sClassName));
      TSJMSAdminInterface ctsJMS = (TSJMSAdminInterface) c.newInstance();
      ctsJMS.init(writer);
      return ctsJMS;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * Return true if a run of test directory 'sDir' requires preliminary creation
   * of JMS factories.
   */
  public static boolean requiresJmsFactories(String sDir) {
    String prefix = "tests" + File.separator;
    for (int i = 0; i < factTestPrefixes.length; i++) {
      if (-1 != sDir.indexOf(prefix + factTestPrefixes[i])) {
        return true;
      }
    }
    return false;
  }

  public static Hashtable getQueueConnectionFactories(int iServer) {
    if (iServer == 2)
      return htQueueConnectionFactories2;
    else
      return htQueueConnectionFactories1;
  }

  public static Hashtable getTopicConnectionFactories(int iServer) {
    if (iServer == 2)
      return htTopicConnectionFactories2;
    else
      return htTopicConnectionFactories1;
  }

  public static Hashtable getConnectionFactories(int iServer) {
    if (iServer == 2)
      return htConnectionFactories2;
    else
      return htConnectionFactories1;
  }

  public static String[] getQueues(String sPath, int iServer) {
    Hashtable htQueues = htQueues1;
    String sRelativeTestPath = convertSlashesToDashes(sPath);
    String[] sVal2 = null;
    String sRelativeTestPathCopy = sRelativeTestPath;
    TestUtil.logHarnessDebug("getQueues:  iServer = " + iServer);
    if (iServer == 2)
      htQueues = htQueues2;
    do {
      // get overridden vehicles property if it exists, otherwise
      // return null
      TestUtil
          .logHarnessDebug("getQueues:  going to check sRelativeTestPathCopy = "
              + sRelativeTestPathCopy);
      sVal2 = (String[]) htQueues.get(sRelativeTestPathCopy);
      TestUtil.logHarnessDebug("getQueues:  sVal2 = " + sVal2);
      if (sVal2 == null) {
        // do nothing
      } else {
        for (int ii = 0; ii < sVal2.length; ii++) {
          TestUtil.logHarnessDebug(
              "getQueuesToCreate:  " + "vehicle[" + ii + "] = " + sVal2[ii]);
        }
      }
    } while ((sRelativeTestPathCopy = getNextLevelUp(
        sRelativeTestPathCopy)) != null && sVal2 == null);
    TestUtil.logHarnessDebug("getQueues:  returning sVal2 = " + sVal2);
    return sVal2;
  }

  public static String[] getTopics(String sPath, int iServer) {
    Hashtable htTopics = htTopics1;
    String sRelativeTestPath = convertSlashesToDashes(sPath);
    String[] sVal2 = null;
    String sRelativeTestPathCopy = sRelativeTestPath;
    TestUtil.logHarnessDebug("getTopics:  iServer = " + iServer);
    if (iServer == 2)
      htTopics = htTopics2;
    do {
      // get overridden vehicles property if it exists, otherwise
      // return null
      TestUtil
          .logHarnessDebug("getTopics:  going to check sRelativeTestPathCopy = "
              + sRelativeTestPathCopy);
      sVal2 = (String[]) htTopics.get(sRelativeTestPathCopy);
      TestUtil.logHarnessDebug("getTopics:  sVal2 = " + sVal2);
      if (sVal2 == null) {
        // do nothing
      } else {
        for (int ii = 0; ii < sVal2.length; ii++) {
          TestUtil.logHarnessDebug(
              "getQueuesToCreate:  " + "vehicle[" + ii + "] = " + sVal2[ii]);
        }
      }
    } while ((sRelativeTestPathCopy = getNextLevelUp(
        sRelativeTestPathCopy)) != null && sVal2 == null);
    TestUtil.logHarnessDebug("getTopics:  returning sVal2 = " + sVal2);
    return sVal2;
  }

  private static String getNextLevelUp(String sDottedPath) {
    int index = 0;
    String sNewPath = null;
    index = sDottedPath.lastIndexOf("-");
    if (index != -1)
      sNewPath = sDottedPath.substring(0, index);
    return sNewPath;
  }

  private static String convertSlashesToDashes(String sTestDir) {
    String sRelativeTestPath = "";
    TestUtil.logHarnessDebug("sTestDir = " + sTestDir);
    sRelativeTestPath = (sTestDir.substring(sTestDir.indexOf(
        File.separator + "ts" + File.separator + "tests" + File.separator) + 4))
            .replace(File.separatorChar, '-');
    TestUtil.logHarnessDebug("sRelativeTestPath = " + sRelativeTestPath);
    return sRelativeTestPath;
  }

  static {
    // for server 1
    htQueues1 = new Hashtable();
    htTopics1 = new Hashtable();
    htTopicConnectionFactories1 = new Hashtable();
    htQueueConnectionFactories1 = new Hashtable();
    htConnectionFactories1 = new Hashtable();
    // for server 2
    htQueues2 = new Hashtable();
    htTopics2 = new Hashtable();
    htTopicConnectionFactories2 = new Hashtable();
    htQueueConnectionFactories2 = new Hashtable();
    htConnectionFactories2 = new Hashtable();
    // connection factories and associated properties to create
    htTopicConnectionFactories1.put("jms/TopicConnectionFactory", "");
    htTopicConnectionFactories1.put("jms/DURABLE_SUB_CONNECTION_FACTORY",
        "clientId=cts");
    htTopicConnectionFactories1.put("jms/MDBTACCESSTEST_FACTORY",
        "clientId=cts1");
    htTopicConnectionFactories1.put("jms/DURABLE_BMT_CONNECTION_FACTORY",
        "clientId=cts2");
    htTopicConnectionFactories1.put("jms/DURABLE_CMT_CONNECTION_FACTORY",
        "clientId=cts3");
    htTopicConnectionFactories1.put("jms/DURABLE_BMT_XCONNECTION_FACTORY",
        "clientId=cts4");
    htTopicConnectionFactories1.put("jms/DURABLE_CMT_XCONNECTION_FACTORY",
        "clientId=cts5");
    htTopicConnectionFactories1.put("jms/DURABLE_CMT_TXNS_XCONNECTION_FACTORY",
        "clientId=cts6");
    htQueueConnectionFactories1.put("jms/QueueConnectionFactory", "");
    htConnectionFactories1.put("jms/ConnectionFactory", "");
    // add default values
    htQueues1.put("tests-jms-ee-mdb",
        new String[] { "MDB_QUEUE_REPLY", "MDB_QUEUE" });
    htQueues1.put("tests-jms-ee-mdb-xa", new String[] { "MDB_QUEUE_REPLY",
        "jms_ee_mdb_xa_MDB_QUEUE_CMT", "jms_ee_mdb_xa_MDB_QUEUE_BMT" });
    htQueues1.put("tests-jms-ee-mdb-mdb_msgHdrQ",
        new String[] { "MDB_QUEUE_REPLY", "jms_ee_mdb_mdb_msgHdrQ_MDB_QUEUE" });
    htQueues1.put("tests-jms-ee-mdb-mdb_msgPropsQ", new String[] {
        "MDB_QUEUE_REPLY", "jms_ee_mdb_mdb_msgPropsQ_MDB_QUEUE" });
    htQueues1.put("tests-jms-ee-mdb-mdb_msgTypesQ1", new String[] {
        "MDB_QUEUE_REPLY", "jms_ee_mdb_mdb_msgTypesQ1_MDB_QUEUE" });
    htQueues1.put("tests-jms-ee-mdb-mdb_msgTypesQ2", new String[] {
        "MDB_QUEUE_REPLY", "jms_ee_mdb_mdb_msgTypesQ2_MDB_QUEUE" });
    htQueues1.put("tests-jms-ee-mdb-mdb_msgTypesQ3", new String[] {
        "MDB_QUEUE_REPLY", "jms_ee_mdb_mdb_msgTypesQ3_MDB_QUEUE" });
    htQueues1.put("tests-jms-ee-mdb-mdb_rec",
        new String[] { "MDB_QUEUE_REPLY", "jms_ee_mdb_mdb_rec_MDB_QUEUE" });
    htQueues1.put("tests-jms-ee-mdb-mdb_sndQ",
        new String[] { "MDB_QUEUE_REPLY", "jms_ee_mdb_mdb_sndQ_MDB_QUEUE" });
    htQueues1.put("tests-jms-ee-mdb-mdb_sndToQueue", new String[] {
        "MDB_QUEUE_REPLY", "jms_ee_mdb_mdb_sndToQueue_MDB_QUEUE" });
    htQueues1.put("tests-jms-ee-mdb-mdb_synchrec", new String[] { "MDB_QUEUE",
        "MDB_QUEUE_REPLY", "jms_ee_mdb_mdb_synchrec_MDB_QUEUE" });
    htQueues1.put("tests-jms-ee-mdb-mdb_exceptQ",
        new String[] { "MDB_QUEUE_REPLY",
            "jms_ee_mdb_mdb_exceptQ_MDB_QUEUE_CMT",
            "jms_ee_mdb_mdb_exceptQ_MDB_QUEUE_BMT",
            "jms_ee_mdb_mdb_exceptQ_MDB_QUEUETXNS_CMT" });
    htQueues1.put("tests-jms-ee-mdb-mdb_exceptT",
        new String[] { "MDB_QUEUE_REPLY",
            "jms_ee_mdb_mdb_exceptT_MDB_QUEUE_CMT",
            "jms_ee_mdb_mdb_exceptT_MDB_QUEUE_BMT",
            "jms_ee_mdb_mdb_exceptT_MDB_QUEUETXNS_CMT" });
    htQueues1.put("tests-jms-core", new String[] { "MY_QUEUE", "MY_QUEUE2" });
    htQueues1.put("tests-jms-core20", new String[] { "MY_QUEUE", "MY_QUEUE2" });
    htQueues1.put("tests-jms-ee-ejb", new String[] { "MY_QUEUE" });
    htQueues1.put("tests-jms-ee-ejbweb-xa", new String[] { "QUEUE_BMT" });
    htQueues1.put("tests-jms-ee-appclient", new String[] { "MY_QUEUE" });
    htQueues1.put("tests-jms-ee-appclient-queuetests", new String[] {
        "MY_QUEUE", "testQueue2", "testQ0", "testQ1", "testQ2" });
    htQueues1.put("tests-jms-ee-appclient-txqueuetests",
        new String[] { "MY_QUEUE", "Q2" });
    // local access tests
    htQueues1.put("tests-ejb-ee-bb-localaccess-mdbqaccesstest", new String[] {
        "ejb_ee_bb_localaccess_mdbqaccesstest_MDB_QUEUE", "MDB_QUEUE_REPLY" });
    htQueues1.put("tests-ejb-ee-bb-localaccess-mdbtaccesstest",
        new String[] { "MDB_QUEUE_REPLY" });
    htQueues1.put("tests-ejb-ee-sec-stateful-mdb", new String[] {
        "MDB_QUEUE_REPLY", "ejb_ee_sec_stateful_mdb_MDB_QUEUE" });
    htQueues1.put("tests-ejb-ee-sec-mdb", new String[] { "MDB_QUEUE_REPLY",
        "ejb_sec_mdb_MDB_QUEUE_BMT", "ejb_sec_mdb_MDB_QUEUE_CMT" });

    /*
     * Queues for ejb/ee/timer sub-tree
     */
    htQueues1.put("tests-ejb-ee-timer",
        new String[] { "MY_QUEUE", "ejb_ee_timer_mdb_MsgBean" });

    /*
     * Queues for ejb/ee/deploy sub-tree
     */
    htQueues1.put("tests-ejb-ee-deploy-mdb-enventry-single",
        new String[] { "ejb_ee_deploy_mdb_enventry_single_AllBean",
            "ejb_ee_deploy_mdb_enventry_single_StringBean",
            "ejb_ee_deploy_mdb_enventry_single_BooleanBean",
            "ejb_ee_deploy_mdb_enventry_single_ByteBean",
            "ejb_ee_deploy_mdb_enventry_single_ShortBean",
            "ejb_ee_deploy_mdb_enventry_single_IntegerBean",
            "ejb_ee_deploy_mdb_enventry_single_LongBean",
            "ejb_ee_deploy_mdb_enventry_single_FloatBean",
            "ejb_ee_deploy_mdb_enventry_single_DoubleBean",
            "ejb_ee_deploy_mdb_enventry_single_AllBeanBMT",
            "ejb_ee_deploy_mdb_enventry_single_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-enventry-scope",
        new String[] { "ejb_ee_deploy_mdb_enventry_scope_Bean1_SameJar",
            "ejb_ee_deploy_mdb_enventry_scope_Bean2_SameJar",
            "ejb_ee_deploy_mdb_enventry_scope_Bean1_MultiJar",
            "ejb_ee_deploy_mdb_enventry_scope_Bean2_MultiJar",
            "ejb_ee_deploy_mdb_enventry_scope_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-enventry-casesens",
        new String[] { "ejb_ee_deploy_mdb_enventry_casesens_CaseBean",
            "ejb_ee_deploy_mdb_enventry_casesens_CaseBeanBMT",
            "ejb_ee_deploy_mdb_enventry_casesens_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejbref-single",
        new String[] { "ejb_ee_deploy_mdb_ejbref_single_TestBean",
            "ejb_ee_deploy_mdb_ejbref_single_TestBeanBMT",
            "ejb_ee_deploy_mdb_ejbref_single_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejbref-scope",
        new String[] { "ejb_ee_deploy_mdb_ejbref_scope_Romeo",
            "ejb_ee_deploy_mdb_ejbref_scope_Tristan",
            "ejb_ee_deploy_mdb_ejbref_scope_Cyrano",
            "ejb_ee_deploy_mdb_ejbref_scope_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejbref-casesens",
        new String[] { "ejb_ee_deploy_mdb_ejbref_casesens_TestBean",
            "ejb_ee_deploy_mdb_ejbref_casesens_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejblink-single",
        new String[] { "ejb_ee_deploy_mdb_ejblink_single_TestBean",
            "ejb_ee_deploy_mdb_ejblink_single_TestBeanBMT",
            "ejb_ee_deploy_mdb_ejblink_single_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejblink-scope",
        new String[] { "ejb_ee_deploy_mdb_ejblink_scope_TestBean",
            "ejb_ee_deploy_mdb_ejblink_scope_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejblink-casesens",
        new String[] { "ejb_ee_deploy_mdb_ejblink_casesens_TestBean",
            "ejb_ee_deploy_mdb_ejblink_casesens_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-resref-single",
        new String[] { "ejb_ee_deploy_mdb_resref_single_TestBean",
            "ejb_ee_deploy_mdb_resref_single_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-enventry-singleT",
        new String[] { "ejb_ee_deploy_mdb_enventry_singleT_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-enventry-scopeT",
        new String[] { "ejb_ee_deploy_mdb_enventry_scopeT_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-enventry-casesensT",
        new String[] { "ejb_ee_deploy_mdb_enventry_casesensT_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejbref-singleT",
        new String[] { "ejb_ee_deploy_mdb_ejbref_singleT_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejbref-scopeT",
        new String[] { "ejb_ee_deploy_mdb_ejbref_scopeT_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejbref-casesensT",
        new String[] { "ejb_ee_deploy_mdb_ejbref_casesensT_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejblink-singleT",
        new String[] { "ejb_ee_deploy_mdb_ejblink_singleT_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejblink-scopeT",
        new String[] { "ejb_ee_deploy_mdb_ejblink_scopeT_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-ejblink-casesensT",
        new String[] { "ejb_ee_deploy_mdb_ejblink_casesensT_ReplyQueue" });
    htQueues1.put("tests-ejb-ee-deploy-mdb-resref-singleT",
        new String[] { "ejb_ee_deploy_mdb_resref_singleT_ReplyQueue" });

    /*
     * Queues for ejb30 sub-tree
     */
    htQueues1.put("tests-ejb30-bb-session-stateless-annotation",
        new String[] { "MY_QUEUE" });
    htQueues1.put("tests-ejb30-bb-mdb",
        new String[] { "MDB_QUEUE_REPLY", "MDB_QUEUE" });
    htQueues1.put("tests-ejb30-tx-mdb",
        new String[] { "MDB_QUEUE_REPLY", "MDB_QUEUE" });
    htQueues1.put("tests-ejb30-bb-localaccess-mdbclient",
        new String[] { "MDB_QUEUE_REPLY", "MDB_QUEUE" });
    htQueues1.put("tests-ejb30-timer-basic-mdb",
        new String[] { "MDB_QUEUE_REPLY", "MDB_QUEUE" });
    htQueues1.put("tests-ejb30-zombie", new String[] { "MDB_QUEUE" });
    htQueues1.put("tests-ejb30-assembly-appres",
        new String[] { "MDB_QUEUE_REPLY" });
    htQueues1.put("tests-ejb30-timer-interceptor-business-mdb",
        new String[] { "MDB_QUEUE" });
    htQueues1.put("tests-ejb30-timer-schedule-auto-attr-mdb",
        new String[] { "MDB_QUEUE" });

    /*
     * Queue for servlet sub-tree
     */
    htQueues1.put("tests-servlet-spec-annotation", new String[] { "MY_QUEUE" });

    /*
     * Queues for jsp sub-tree
     */
    htQueues1.put("tests-jsp-spec-tagext-resource",
        new String[] { "MY_QUEUE" });

    /*
     * Queues for webservices sub-tree
     */
    htQueues1.put("tests-webservices-handler-localtx",
        new String[] { "MY_QUEUE" });
    htQueues1.put("tests-webservices-handlerEjb-localtx",
        new String[] { "MY_QUEUE" });

    // add default values
    htTopics1.put("tests-jms-ee-mdb-mdb_sndToTopic",
        new String[] { "jms_ee_mdb_mdb_sndToTopic_MDB_TOPIC_REPLY",
            "jms_ee_mdb_mdb_sndToTopic_MDB_TOPIC" });
    htTopics1.put("tests-jms-core", new String[] { "MY_TOPIC", "MY_TOPIC2" });
    htTopics1.put("tests-jms-core20", new String[] { "MY_TOPIC", "MY_TOPIC2" });
    htTopics1.put("tests-jms-ee-ejb", new String[] { "MY_TOPIC" });
    htTopics1.put("tests-jms-ee-ejbweb-xa", new String[] { "TOPIC_BMT" });
    htTopics1.put("tests-jms-ee-appclient", new String[] { "MY_TOPIC" });
    htTopics1.put("tests-jms-ee-appclient-topictests",
        new String[] { "MY_TOPIC", "MY_TOPIC2" });
    htTopics1.put("tests-jms-ee-appclient-txtopictests",
        new String[] { "MY_TOPIC" });
    htTopics1.put("tests-jms-ee-mdb-xa", new String[] {
        "jms_ee_mdb_xa_MDB_DURABLE_BMT", "jms_ee_mdb_xa_MDB_DURABLE_CMT" });
    htTopics1.put("tests-jms-ee-mdb-mdb_exceptT",
        new String[] { "jms_ee_mdb_mdb_exceptT_MDB_DURABLE_BMT",
            "jms_ee_mdb_mdb_exceptT_MDB_DURABLE_CMT",
            "jms_ee_mdb_mdb_exceptT_MDB_DURABLETXNS_CMT" });
    htTopics1.put("tests-jms-ee-mdb-mdb_msgHdrT",
        new String[] { "jms_ee_mdb_mdb_msgHdrT_MDB_TOPIC" });
    htTopics1.put("tests-jms-ee-mdb-mdb_msgPropsT",
        new String[] { "jms_ee_mdb_mdb_msgPropsT_MDB_TOPIC" });
    htTopics1.put("tests-jms-ee-mdb-mdb_msgTypesT1",
        new String[] { "jms_ee_mdb_mdb_msgTypesT1_MDB_TOPIC" });
    htTopics1.put("tests-jms-ee-mdb-mdb_msgTypesT2",
        new String[] { "jms_ee_mdb_mdb_msgTypesT2_MDB_TOPIC" });
    htTopics1.put("tests-jms-ee-mdb-mdb_msgTypesT3",
        new String[] { "jms_ee_mdb_mdb_msgTypesT3_MDB_TOPIC" });
    htTopics1.put("tests-jms-ee-mdb-mdb_rec",
        new String[] { "jms_ee_mdb_mdb_rec_MDB_TOPIC" });
    // local access tests
    htTopics1.put("tests-ejb-ee-bb-localaccess-mdbtaccesstest",
        new String[] { "ejb_ee_bb_localaccess_mdbtaccesstest_MDB_TOPIC" });
    /*
     * Topics for ejb/ee/deploy sub-tree
     */
    htTopics1.put("tests-ejb-ee-deploy-mdb-enventry-singleT",
        new String[] { "ejb_ee_deploy_mdb_enventry_singleT_AllBean",
            "ejb_ee_deploy_mdb_enventry_singleT_StringBean",
            "ejb_ee_deploy_mdb_enventry_singleT_BooleanBean",
            "ejb_ee_deploy_mdb_enventry_singleT_ByteBean",
            "ejb_ee_deploy_mdb_enventry_singleT_ShortBean",
            "ejb_ee_deploy_mdb_enventry_singleT_IntegerBean",
            "ejb_ee_deploy_mdb_enventry_singleT_LongBean",
            "ejb_ee_deploy_mdb_enventry_singleT_FloatBean",
            "ejb_ee_deploy_mdb_enventry_singleT_DoubleBean",
            "ejb_ee_deploy_mdb_enventry_singleT_AllBeanBMT" });
    htTopics1.put("tests-ejb-ee-deploy-mdb-enventry-scopeT",
        new String[] { "ejb_ee_deploy_mdb_enventry_scopeT_Bean1_SameJar",
            "ejb_ee_deploy_mdb_enventry_scopeT_Bean2_SameJar",
            "ejb_ee_deploy_mdb_enventry_scopeT_Bean1_MultiJar",
            "ejb_ee_deploy_mdb_enventry_scopeT_Bean2_MultiJar" });
    htTopics1.put("tests-ejb-ee-deploy-mdb-enventry-casesensT",
        new String[] { "ejb_ee_deploy_mdb_enventry_casesensT_CaseBean",
            "ejb_ee_deploy_mdb_enventry_casesensT_CaseBeanBMT" });
    htTopics1.put("tests-ejb-ee-deploy-mdb-ejbref-singleT",
        new String[] { "ejb_ee_deploy_mdb_ejbref_singleT_TestBean",
            "ejb_ee_deploy_mdb_ejbref_singleT_TestBeanBMT" });
    htTopics1.put("tests-ejb-ee-deploy-mdb-ejbref-scopeT",
        new String[] { "ejb_ee_deploy_mdb_ejbref_scopeT_Romeo",
            "ejb_ee_deploy_mdb_ejbref_scopeT_Tristan",
            "ejb_ee_deploy_mdb_ejbref_scopeT_Cyrano" });
    htTopics1.put("tests-ejb-ee-deploy-mdb-ejbref-casesensT",
        new String[] { "ejb_ee_deploy_mdb_ejbref_casesensT_TestBean" });
    htTopics1.put("tests-ejb-ee-deploy-mdb-ejblink-singleT",
        new String[] { "ejb_ee_deploy_mdb_ejblink_singleT_TestBean",
            "ejb_ee_deploy_mdb_ejblink_singleT_TestBeanBMT" });
    htTopics1.put("tests-ejb-ee-deploy-mdb-ejblink-scopeT",
        new String[] { "ejb_ee_deploy_mdb_ejblink_scopeT_TestBean" });
    htTopics1.put("tests-ejb-ee-deploy-mdb-ejblink-casesensT",
        new String[] { "ejb_ee_deploy_mdb_ejblink_casesensT_TestBean" });
    htTopics1.put("tests-ejb-ee-deploy-mdb-resref-singleT",
        new String[] { "ejb_ee_deploy_mdb_resref_singleT_TestBean" });

    /*
     * Topics for ejb30 sub-tree
     */
    htTopics1.put("tests-ejb30-bb-session-stateless-annotation",
        new String[] { "MY_TOPIC" });
    htTopics1.put("tests-ejb30-bb-mdb-dest-topic", new String[] { "MY_TOPIC" });
    htTopics1.put(
        "tests-ejb30-bb-mdb-activationconfig-topic-selectordupsnondurable",
        new String[] { "MY_TOPIC" });

    /*
     * Topics for servlet sub-tree
     */
    htTopics1.put("tests-servlet-spec-annotation", new String[] { "MY_TOPIC" });

    /*
     * Topics for jsp sub-tree
     */
    htTopics1.put("tests-jsp-spec-tagext-resource",
        new String[] { "MY_TOPIC" });

  }
}
