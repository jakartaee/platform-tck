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

package com.sun.ts.tests.ejb.ee.deploy.mdb.ejbref.scope;

import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.ejb.wrappers.MDBWrapper;

public class MsgBean extends MDBWrapper {

  private static final String lookupName = "java:comp/env/ejb/Partner";

  public Boolean whoIsYourPartner(Properties props) {

    boolean pass = false;
    ReferencedBeanHome home = null;
    ReferencedBean bean = null;
    String partnerName = null;
    String expectedName;

    try {
      expectedName = TestUtil.getProperty("beanPartner");

      TestUtil.logTrace("[MsgBean] looking up " + lookupName);
      home = (ReferencedBeanHome) nctx.lookup(lookupName,
          ReferencedBeanHome.class);
      bean = home.create();
      bean.initLogging(props);
      partnerName = bean.whoAreYou();

      pass = expectedName.equals(partnerName);
      if (!pass) {
        TestUtil.logErr("[MsgBean] expected " + expectedName);
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("[MsgBean] Caught exception! ", e);
    } finally {
      try {
        if (null != bean) {
          bean.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("[MsgBean] Ignoring exception on bean " + "removal ",
            e);
      }
    }

    return new Boolean(pass);
  }
}
