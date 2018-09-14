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
 * @(#)CSIv2TestLogicImpl.java	1.14 03/09/13
 */

package com.sun.ts.tests.interop.csiv2.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;

import javax.ejb.*;

import java.rmi.*;
import java.util.*;

import javax.naming.*;
import javax.rmi.*;

/**
 * Centralized class for all CSIv2 test logic. This code is delegated to by the
 * the CSIv2SessionBean, the CSIv2AppClient and the CSIV2Servlet. Writes
 * appropriate information to the log for later verification.
 */
public class CSIv2TestLogicImpl implements CSIv2TestLogicInterface {
  /**
   * A substring guaranteed to be in the JNDI name of a session bean. This
   * allows us to determine, in advance, what Home interface type to expect.
   */
  private static final String SESSION_BEAN_NAME = "Session";

  private static final String linePrefix = "------[  ";

  private static final String lineSuffix = "  ]-----";

  private static final String printLine = "--------------------";

  /**
   * @see CSIv2TestLogicInterface for javadocs.
   */
  public void invoke(ArrayList chain, Properties p) {

    CSIv2Log log = CSIv2Log.getLog();

    boolean exit = false;

    if (chain.size() > 0) {
      TestUtil.logMsg(linePrefix + "Begin invoke" + lineSuffix);

      // The next test logic component to invoke.
      CSIv2TestLogicInterface next = null;

      // Look at first element in chain
      String lookupName = (String) chain.get(0);
      chain.remove(0);

      TestUtil.logMsg("INVOKING " + lookupName);

      try {
        TestUtil.logTrace("Executing logStartInvocation");
        log.logStartInvocation();
        // Perform JNDI lookup:
        TestUtil.logTrace("Executing new InitialContext()");
        Context initialContext = new InitialContext();
        TestUtil.logTrace("Invoking initialContext.lookup(" + lookupName + ")");
        Object objHome = initialContext.lookup(lookupName);
        TestUtil.logTrace("initialContext.lookup: " + lookupName);
        if (lookupName.indexOf(SESSION_BEAN_NAME) != -1) {
          TestUtil.logTrace("lookupName is : " + SESSION_BEAN_NAME);
          CSIv2SessionHome sessionHome = (CSIv2SessionHome) PortableRemoteObject
              .narrow(objHome, CSIv2SessionHome.class);
          CSIv2Session session = null;

          // Test home interface:
          TestUtil.logTrace("Executing logStartEJBHome and logStartClient");
          log.logStartEJBHome();
          log.logStartClient();

          // First, enable logging of create method here:
          // log.expectCall( "create", chain );

          // Now, try to invoke the home interface (we're logging).
          TestUtil.logTrace("Invoke the home interface");

          try {
            // used to be "createInvoke" but we had to change
            // this to create since it's now a 1.x bean:
            TestUtil.logMsg("Calling sessionHome.create(p,true)");
            sessionHome.create(p, true);
            log.logReply(null);
          } catch (CreateException e) {
            TestUtil.logMsg("Received expected CreateException");
            // expected result.
            log.logReply(e);
          } catch (RemoteException e) {
            TestUtil.printStackTrace(e);
            log.logReply(e);
            exit = true;
          }
          TestUtil.logTrace("Executing logEndClient and logEndEJBHome");
          log.logEndClient();
          log.logEndEJBHome();

          // Continue if create succeeded
          if (!exit) {
            try {
              TestUtil.logMsg("Calling sessionHome.create(p,false)");
              session = sessionHome.create(p, false);
              TestUtil.logMsg("sessionHome.create() successful");
              next = session;
            } catch (Exception e) {
              TestUtil.logErr("sessionHome.create() failed: " + e.getMessage());
              TestUtil.printStackTrace(e);
              exit = true;
            }
          }
        }

        // Continue if create succeeded
        if (!exit) {
          TestUtil.logTrace("Continue, create was successful");

          // Pass control to next component in chain:
          TestUtil.logTrace("Executing logStartEJBHome and logStartClient");
          log.logStartEJBRemote();
          log.logStartClient();
          try {
            next.invoke(chain, p);
            log.logReply(null);
          } catch (RemoteException e) {
            TestUtil.printStackTrace(e);
            log.logReply(e);
            exit = true;
          }
          TestUtil.logTrace("Executing logEndClient and logEndEJBHome");
          log.logEndClient();
          log.logEndEJBRemote();
        }
      } catch (NamingException e) {
        TestUtil.logErr("NAMINGEXCEPTION: " + e);
        TestUtil.printStackTrace(e);
      }
      /*
       * catch( RemoteException e ) { TestUtil.logErr( "REMOTEEXCEPTION: " + e
       * ); TestUtil.printStackTrace(e); }
       */
      catch (Exception e) {
        TestUtil.logErr("EXCEPTION: " + e);
        TestUtil.printStackTrace(e);
      }
      TestUtil.logTrace("Executing logEndInvocation");
      log.logEndInvocation();
      TestUtil.logMsg(linePrefix + "End   invoke" + lineSuffix);
    }

  }
}
