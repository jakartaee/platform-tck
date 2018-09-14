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
 * @(#)JAXRClient.java	1.14 03/05/16
 */
package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.Slot;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.JAXRCommonClient;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import com.sun.javatest.Status;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;
import java.net.PasswordAuthentication;

public class JAXRClient extends JAXRCommonClient {
  public static void main(String[] args) {
    JAXRClient theTests = new JAXRClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */
  /*
   * @class.setup_props: jaxrPassword; jaxrUser; jaxrPassword2; jaxrUser2;
   * registryURL; queryManagerURL; authenticationMethod; providerCapability;
   * jaxrConnectionFactoryLookup; jaxrSecurityCredentialType; jaxrJNDIResource;
   * jaxrAlias; jaxrAlias2; jaxrAliasPassword; jaxrAlias2Password;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    try {
      super.setup(args, p);
      debug.clear();

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      logMsg("in cleanup");
      if (conn != null) {
        logTrace("Cleanup is closing the connection");
        conn.close();
      }
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
    } finally {
      // print out messages
      if (debug != null)
        TestUtil.logTrace(debug.toString());
    }

  }

  /*
   * @testName: slot_getNameTest
   *
   * @assertion_ids: JAXR:JAVADOC:392;
   *
   * @assertion: getName - Gets the name for this slot.
   *
   * @test_Strategy: createSlot with name,type and value. Verify valid name
   * returned with getName
   *
   */
  public void slot_getNameTest() throws Fault {
    String testName = "slot_getNameTest";
    boolean pass = true;
    String name = Slot.ADDRESS_LINES_SLOT;
    String value1 = "1 Network Drive";
    String value2 = "Burlington,MA";
    String slotType = "myLocalAddress";
    try {
      Collection values = new ArrayList();
      values.add(value1);
      values.add(value2);
      Slot slot = blm.createSlot(name, values, slotType);
      debug.add("\n");
      debug.add("getName should return:" + name + "\n");
      debug.add("getName returned:" + slot.getName() + "\n");
      if (!(slot.getName().equals(name)))
        throw new Fault(testName + " returned invalid testname");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: slot_setNameTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:394;
   *
   * @assertion: setName - sets the name for this slot.
   *
   *
   * @test_Strategy: createSlot with name,type and value. Use setName to change
   * the name. Verify updated name returned with getName
   * 
   *
   */
  public void slot_setNameTest() throws Fault {
    String testName = "slot_setNameTest";
    boolean pass = true;
    String name = Slot.ADDRESS_LINES_SLOT;
    String name2 = Slot.SORT_CODE_SLOT;
    String value1 = "1 Network Drive";
    String value2 = "Burlington,MA";
    String slotType = "myLocalAddress";
    try {
      Collection values = new ArrayList();
      values.add(value1);
      values.add(value2);
      Slot slot = blm.createSlot(name, values, slotType);
      slot.setName(name2);
      debug.add("\n");
      debug.add("getName should return:" + name2 + "\n");
      debug.add("getName returned:" + slot.getName() + "\n");
      if (!(slot.getName().equals(name2)))
        throw new Fault(testName + " returned invalid testname");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: slot_getSlotTypeTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:396;
   *
   * @assertion: getSlotType - The slotType for this Slot.
   *
   *
   * @test_Strategy: createSlot with name,type and value. Verify that the
   * correct SlotType is returned
   *
   *
   */
  public void slot_getSlotTypeTest() throws Fault {
    String testName = "slot_getSlotTypeTest";
    boolean pass = true;
    String name = Slot.ADDRESS_LINES_SLOT;
    String value1 = "1 Network Drive";
    String value2 = "Burlington,MA";
    String slotType = "myLocalAddress";
    try {
      Collection values = new ArrayList();
      values.add(value1);
      values.add(value2);
      Slot slot = blm.createSlot(name, values, slotType);
      debug.add("\n");
      debug.add("getSlotType should return:" + slotType + "\n");
      debug.add("getSlotType returned:" + slot.getSlotType() + "\n");
      if (!(slot.getSlotType().equals(slotType)))
        throw new Fault(testName + " returned invalid slotType");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: slot_setSlotTypeTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:398;
   *
   * @assertion: setSlotType - Sets the slotType for this Slot.
   *
   *
   * @test_Strategy: createSlot with name,type and value. Use setSlotType to
   * change the slotType. Verify updated type returned
   *
   *
   */
  public void slot_setSlotTypeTest() throws Fault {
    String testName = "slot_setSlotTypeTest";
    boolean pass = true;
    String name = Slot.ADDRESS_LINES_SLOT;
    String value1 = "1 Network Drive";
    String value2 = "Burlington,MA";
    String slotType = "myLocalAddress";
    String slotType2 = "myWorkAddress";
    try {
      Collection values = new ArrayList();
      values.add(value1);
      values.add(value2);
      Slot slot = blm.createSlot(name, values, slotType);
      slot.setSlotType(slotType2);
      debug.add("\n");
      debug.add("slotType should return:" + slotType2 + "\n");
      debug.add("slotType returned:" + slot.getSlotType() + "\n");
      if (!(slot.getSlotType().equals(slotType2)))
        throw new Fault(testName + " returned invalid slotType");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: slot_getValuesTest
   *
   * @assertion_ids: JAXR:JAVADOC:400;
   *
   * @assertion: getValues - Gets the values for this Slot.
   *
   *
   * @test_Strategy: createSlot with name,type and value. Verify that the
   * correct values are returned
   *
   */
  public void slot_getValuesTest() throws Fault {
    String testName = "slot_getValuesTest";
    boolean pass = true;
    String name = Slot.ADDRESS_LINES_SLOT;
    String value1 = "1 Network Drive";
    String value2 = "Burlington,MA";
    String slotType = "myLocalAddress";
    try {
      Collection values = new ArrayList();
      values.add(value1);
      values.add(value2);
      Slot slot = blm.createSlot(name, values, slotType);
      Collection c = slot.getValues();
      Object[] slotValues = c.toArray();
      debug.add("\n");
      debug.add("value 1 should return: " + value1 + "\n");
      debug.add("value 2 should return: " + value2 + "\n");
      for (int i = 0; i < c.size(); i++) {
        debug.add("value " + i + " returned " + (String) slotValues[i] + "\n");
      }
      if (!(c.containsAll(values)))
        throw new Fault(testName + " returned invalid values");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: slot_setValuesTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:402;
   *
   * @assertion: setValues - Sets the values for this Slot.
   *
   * JAXR javadoc
   *
   * @test_Strategy: createSlot with name,type and value. Verify that the
   * correct values are returned
   *
   *
   */
  public void slot_setValuesTest() throws Fault {
    String testName = "slot_setValuesTest";
    boolean pass = true;
    String name = Slot.ADDRESS_LINES_SLOT;
    String value1 = "1 Network Drive";
    String value2 = "Burlington,MA";
    String newValue1 = "47 Amsden Street";
    String newValue2 = "Arlington, MA";
    String slotType = "myLocalAddress";
    try {
      Collection values = new ArrayList();
      values.add(value1);
      values.add(value2);
      Slot slot = blm.createSlot(name, values, slotType);
      Collection newValues = new ArrayList();

      newValues.add(newValue1);
      newValues.add(newValue2);

      slot.setValues(newValues);
      Collection c = slot.getValues();
      Object[] slotValues = c.toArray();
      debug.add("\n");
      debug.add("value 1 should return: " + newValue1 + "\n");
      debug.add("value 2 should return: " + newValue2 + "\n");
      for (int i = 0; i < c.size(); i++) {
        debug.add("value " + i + " returned " + (String) slotValues[i] + "\n");
      }
      if (!(c.containsAll(newValues)))
        throw new Fault(testName + " returned invalid values");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

} // end of test class
