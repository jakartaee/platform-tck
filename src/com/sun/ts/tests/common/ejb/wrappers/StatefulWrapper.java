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

package com.sun.ts.tests.common.ejb.wrappers;

import java.util.Properties;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.RemoteLoggingInitException;

/**
 * Stateful wrapper that provide the bean life cycle methods for a Stateful
 * Session bean. This class is intended to be subclassed by the final entity
 * bean class that will provide the test logic (business methods).
 */
public class StatefulWrapper implements SessionBean {

  protected TSNamingContext nctx = null;

  protected SessionContext sctx = null;

  protected Properties props;

  /*
   * Bean life cycle
   */

  public void ejbCreate(Properties props) throws CreateException {
    try {
      this.props = props;
      TestUtil.init(props);
      TestUtil.logTrace("[StatefulWrapper] ejbCreate()");
    } catch (RemoteLoggingInitException e) {
      throw new CreateException(e.getMessage());
    }
    TestUtil.logTrace("[StatefulWrapper] ejbCreate OK");
  }

  public void ejbPostCreate(Properties props) throws CreateException {
    TestUtil.logTrace("[StatefulWrapper] ejbPostCreate()");
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("[StatefulWrapper] setSessionContext()");
    sctx = sc;
    try {
      nctx = new TSNamingContext();
    } catch (Exception e) {
      TestUtil.logErr("[StatefulWrapper] Cannot create Naming Context: " + e);
    }
  }

  public void ejbRemove() {
    TestUtil.logTrace("[StatefulWrapper] ejbRemove()");
  }

  public void ejbActivate() {
    TestUtil.logTrace("[StatefulWrapper] ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("[StatefulWrapper] ejbPassivate()");
  }
}
