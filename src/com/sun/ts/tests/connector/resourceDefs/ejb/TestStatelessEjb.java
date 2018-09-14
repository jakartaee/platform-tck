/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.connector.resourceDefs.ejb;

import com.sun.ts.tests.common.connector.whitebox.TSConnection;

import javax.ejb.Stateless;

import java.security.AccessController;
import java.security.AccessControlException;

import javax.resource.ConnectionFactoryDefinitions;
import javax.resource.ConnectionFactoryDefinition;
import javax.resource.spi.TransactionSupport.TransactionSupportLevel;
import javax.resource.spi.TransactionSupport;
import javax.resource.ConnectionFactoryDefinition;
import javax.resource.ConnectionFactoryDefinitions;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;

import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.ts.lib.util.TSNamingContext;

/**
 * This is the impl for a stateless ejb.
 */

@ConnectionFactoryDefinitions({
    @ConnectionFactoryDefinition(name = "java:app/env/EJBTestServlet_App_ConnectorResource", description = "application scoped connector resource definition", interfaceName = "com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory", resourceAdapter = "whitebox-tx", transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction),

    @ConnectionFactoryDefinition(name = "java:comp/env/EJBTestServlet_Comp_ConnectorResource", description = "component scoped connector resource definition", interfaceName = "com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory", resourceAdapter = "whitebox-tx", transactionSupport = TransactionSupport.TransactionSupportLevel.LocalTransaction),
    // transactionSupport=TransactionSupport.TransactionSupportLevel.NoTransaction),

    @ConnectionFactoryDefinition(name = "java:module/env/EJBTestServlet_Module_ConnectorResource", description = "module scoped connector resource definition", interfaceName = "com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory", resourceAdapter = "whitebox-tx", transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction),

    @ConnectionFactoryDefinition(name = "java:global/env/EJBTestServlet_Global_ConnectorResource", description = "globally scoped connector resource definition", interfaceName = "com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory", resourceAdapter = "whitebox-xa", transactionSupport = TransactionSupport.TransactionSupportLevel.XATransaction) })
@Stateless
public class TestStatelessEjb implements ITestStatelessEjb {

  public boolean validateConnectorResource(String jndiName) {
    TSConnection c = null;
    boolean rval = false;

    try {

      debug("validateConnectorResource():  calling new TSNamingContext()");
      TSNamingContext ic = new TSNamingContext();

      debug("Doing lookup of jndiName = " + jndiName);
      TSDataSource ds = (TSDataSource) (ic.lookup(jndiName));
      debug(
          "validateConnectorResource(): Successfully did lookup of jndiName = "
              + jndiName);

      rval = true;
    } catch (Exception e) {
      debug("Fail to access connector resource: " + jndiName);
      e.printStackTrace();
    } finally {
      debug("finally:  Fail to access connector resource: " + jndiName);
      try {
        if (c != null) {
          c.close();
        }
      } catch (Exception e) {
        return false;
      }
    }

    return rval;
  }

  private void debug(String str) {
    System.out.println(str);
  }

}
