/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

import jakarta.ejb.Stateless;
import jakarta.resource.AdministeredObjectDefinition;
import jakarta.resource.AdministeredObjectDefinitions;

/**
 * This is the impl for a stateless ejb.
 */

@AdministeredObjectDefinitions({
    @AdministeredObjectDefinition(name = "java:app/env/EJBAdminObjectForAppScope", description = "application scoped AdminObjectDefinition", interfaceName = "jakarta.jms.Queue", className = "com.sun.ts.tests.common.connector.embedded.adapter1.CRDAdminObject", resourceAdapter = "#whitebox-rd"),

    @AdministeredObjectDefinition(name = "java:comp/env/EJBAdminObjectForCompScope", description = "component scoped AdminObjectDefinition", interfaceName = "jakarta.jms.Queue", className = "com.sun.ts.tests.common.connector.embedded.adapter1.CRDAdminObject", resourceAdapter = "#whitebox-rd"),

    @AdministeredObjectDefinition(name = "java:module/env/EJBAdminObjectForModuleScope", description = "module scoped AdminObjectDefinition", interfaceName = "jakarta.jms.Queue", className = "com.sun.ts.tests.common.connector.embedded.adapter1.CRDAdminObject", resourceAdapter = "#whitebox-rd"),

    @AdministeredObjectDefinition(name = "java:global/env/EJBAdminObjectForGlobalScope", description = "globally scoped AdminObjectDefinition", interfaceName = "jakarta.jms.Queue", className = "com.sun.ts.tests.common.connector.embedded.adapter1.CRDAdminObject", resourceAdapter = "#whitebox-rd") })

@Stateless
public class TestAdminObjStatelessEjb implements ITestAdminObjStatelessEjb {

  public boolean validateConnectorResource(String jndiName) {
    TSConnection c = null;
    boolean rval = false;

    try {

      debug("validateConnectorResource():  calling new TSNamingContext()");
      TSNamingContext ic = new TSNamingContext();

      debug("Doing lookup of jndiName = " + jndiName);
      Object ds = ic.lookup(jndiName);
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
