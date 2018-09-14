/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.api.rs.core.form;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */

public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 1L;

  protected static final String tck = "tck";

  protected static final String cts = "cts";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: constructorNoArgTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:765; JAXRS:JAVADOC:766;
   * 
   * @test_Strategy: Returns multivalued map representation of the form.
   * 
   * Create a new form data instance.
   */
  public Form constructorNoArgTest() throws Fault {
    Form form = new Form();
    assertFault(form != null, "No Form created");

    MultivaluedMap<String, String> map = form.asMap();
    assertFault(map.isEmpty(), "Created From instance is not empty");
    logMsg("Form instance created");
    return form;
  }

  /*
   * @testName: constructorStringArgsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:767; JAXRS:JAVADOC:765;
   * 
   * @test_Strategy: Create a new form data instance with a single parameter
   * entry.
   */
  public void constructorStringArgsTest() throws Fault {
    Form form = new Form(tck, cts);
    assertFault(form != null, "No Form created");

    MultivaluedMap<String, String> map = form.asMap();
    assertFault(map.containsKey(tck), "No given key", tck,
        "exists in form instance");
    assertFault(map.getFirst(tck).equals(cts),
        "Different value has been given from map for key", tck, ":",
        map.getFirst(tck));
    logMsg("Form instance with String arguments sucessfully created");
  }

  /*
   * @testName: constructorMultivaluedMapArgTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:768; JAXRS:JAVADOC:765;
   * 
   * @test_Strategy: Create a new form data instance with a single parameter
   * entry.
   */
  public void constructorMultivaluedMapArgTest() throws Fault {
    MultivaluedHashMap<String, String> init = new MultivaluedHashMap<String, String>();
    init.add(tck, cts);
    init.add(cts, cts);
    Form form = new Form(init);
    assertFault(form != null, "No Form created");

    MultivaluedMap<String, String> map = form.asMap();
    assertFault(map.containsKey(tck), "No given key ", tck,
        "exists in form instance");
    assertFault(map.getFirst(tck).equals(cts),
        "Different value has been given from map for key", tck, ":",
        map.getFirst(tck));
    assertFault(map.containsKey(cts), "No given key", cts,
        "exists in form instance");
    assertFault(map.getFirst(cts).equals(cts),
        "Different value has been given from map for key", cts, ":",
        map.getFirst(cts));
    logMsg("Form instance with MultivaluedMap argument sucessfully created");
  }

  /*
   * @testName: paramTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:765; JAXRS:JAVADOC:769;
   * 
   * @test_Strategy: Returns multivalued map representation of the form.
   * 
   * Adds a new value to the specified form parameter.
   */
  public void paramTest() throws Fault {
    Form form = constructorNoArgTest();
    form.param(tck, tck);
    form.param(cts, cts);

    MultivaluedMap<String, String> map = form.asMap();
    assertFault(map.containsKey(tck), "No given key ", tck,
        "exists in form instance");
    assertFault(map.getFirst(tck).equals(tck),
        "Different value has been given from map for key", tck, ":",
        map.getFirst(tck));
    assertFault(map.containsKey(cts), "No given key", cts,
        "exists in form instance");
    assertFault(map.getFirst(cts).equals(cts),
        "Different value has been given from map for key", cts, ":",
        map.getFirst(cts));
  }
}
