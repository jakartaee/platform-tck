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

package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.envsharing;

import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF;
import java.net.URL;
import javax.annotation.Resources;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.mail.Session;
import com.sun.ts.tests.ejb30.common.annotation.resource.EnvSharingBeanBase;

@Stateless(name = "ResourceTypeBean")
@Remote({ ResourceIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
@Resource(name = "mailSession", type = Session.class, shareable = false)
@Resource(name = "url", shareable = false, type = URL.class)

public class ResourceTypeBean extends EnvSharingBeanBase implements ResourceIF {

  @Resource(name = "sessionContext", description = "session context", type = SessionContext.class)
  private SessionContext sessionContext;

  protected String getMailSessionName() {
    return "mailSession";
  }

  protected String getUrlName() {
    return "url";
  }

  protected javax.ejb.EJBContext getEJBContext() {
    return sessionContext;
  }

  protected javax.mail.Session getMailSession() {
    return (Session) getEJBContext().lookup(getMailSessionName());
  }

  protected URL getUrl() {
    return (URL) getEJBContext().lookup(getUrlName());
  }
}
