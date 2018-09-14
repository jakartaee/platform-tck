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
package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.ExtensibleObject;

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

  String streetNumber = "47";

  String street = "Amsden";

  String city = "Arlington";

  String stateOrProvince = "MA";

  String country = "USA";

  String postalCode = "02146";

  String type = "home";

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
   * @testName: extensibleObject_addGetSlotTest
   *
   * @assertion: addSlot - Adds a Slot to this object. getSlot - Get the slot
   * specified by slotName
   *
   * JAXR javadoc
   * 
   * @assertion_ids: JAXR:JAVADOC:706; JAXR:JAVADOC:714;JAXR:SPEC:104;
   *
   * @test_Strategy: createSlot with name,type and value. Add slot to
   * PostalAddress. Call getSlot to retrieve it and verify name, type and value
   * of the slot returned.
   *
   */
  public void extensibleObject_addGetSlotTest() throws Fault {
    String testName = "extensibleObject_addGetSlotTest";
    boolean pass = true;
    String slotName = Slot.SORT_CODE_SLOT;
    String value = "2472974";
    String slotType = "slotType";
    try {
      Collection values = new ArrayList();
      values.add(value);
      Slot s = blm.createSlot(slotName, values, slotType);
      // create a postal address
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      pa.addSlot(s);
      Slot slot = pa.getSlot(slotName);

      debug.add("\n");
      debug.add("getName should return:" + slotName + "\n");
      debug.add("getName returned:" + slot.getName() + "\n");
      if (!(slot.getName().equals(slotName)))
        pass = false;

      debug.add("getSlotType should return:" + slotType + "\n");
      debug.add("getSlotType returned:" + slot.getSlotType() + "\n");
      if (!(slot.getSlotType().equals(slotType)))
        pass = false;

      Collection c = slot.getValues();
      Object[] slotValues = c.toArray();
      debug.add("value should return: " + value + "\n");
      for (int i = 0; i < c.size(); i++) {
        debug.add("value returned " + (String) slotValues[i] + "\n");
      }
      if (!(c.containsAll(values)))
        pass = false;
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: extensibleObject_addGetSlotsTest
   *
   * @assertion: removeSlots(Collection)
   *
   * @assertion_ids: JAXR:JAVADOC:708; JAXR:JAVADOC:716
   *
   * @test_Strategy: Create 2 slots and add them to a PostalAddress. Verify the
   * addtions. Use removeSlots method to remove them. Verify the remove by
   * calling getSlots again. Should get back an empty collection.
   *
   */
  public void extensibleObject_addGetSlotsTest() throws Fault {
    String testName = "extensibleObject_addGetSlotsTest";
    String value = "2472974";
    String slotType = null;
    String value2 = "2323233";
    String slotName[] = { Slot.SORT_CODE_SLOT, Slot.ADDRESS_LINES_SLOT };
    int found = 0;
    try {
      Slot s1 = blm.createSlot(slotName[0], value, slotType);
      Slot s2 = blm.createSlot(slotName[1], value2, slotType);
      Collection slots = new ArrayList();
      slots.add(s1);
      slots.add(s2);

      // create a postal address
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      pa.addSlots(slots);
      debug.add("Add and verify 2 slots\n");
      Collection retSlots = pa.getSlots();
      if (!(retSlots.containsAll(slots))) {
        throw new Fault(testName
            + "failed to verify slots were added, test did not complete! ");
      }
      Iterator iter = retSlots.iterator();
      while (iter.hasNext()) {
        Slot slot = (Slot) iter.next();
        if (slot.getName().equals(slotName[0])
            || slot.getName().equals(slotName[1])) {
          found = found + 1;
        }
      }
      if (found != slotName.length)
        throw new Fault(
            testName + "fail - did not find expected number of slots");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "fail - unexpected error!");
    }
  }

  /*
   * @testName: extensibleObject_removeSlotTest
   *
   * @assertion: removeSlot - JAXR javadoc
   *
   * @assertion_ids: JAXR:JAVADOC:710;JAXR:SPEC:104;JAXR:SPEC:103;
   *
   * @test_Strategy: Create a slot object. Add it to a PostalAddress. Verify the
   * slots were added. Call removeSlot to remove the slot. Call getSlot and
   * verify that the slot has been removed.
   *
   */
  public void extensibleObject_removeSlotTest() throws Fault {
    String testName = "extensibleObject_removeSlotTest";
    String slotName = Slot.SORT_CODE_SLOT;
    String value = "2472974";
    String slotType = "slotType";

    try {
      Slot s = blm.createSlot(slotName, value, slotType);
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      pa.addSlot(s);
      debug.add("Created and added a slot to PostalAddress \n");
      Slot slot = pa.getSlot(slotName);
      debug.add("Verify that the slot was added \n");
      debug.add("getName should return:" + slotName + "\n");
      debug.add("getName returned:" + slot.getName() + "\n");
      if (!(slot.getName().equals(slotName))) {
        throw new Fault(testName + "failed  test did not complete");
      }

      debug.add("Remove the slot and verify it removal \n");
      pa.removeSlot(slotName);

      Slot noSlot = pa.getSlot(slotName);
      debug.add("returned slot should return null  \n");
      if (!(noSlot == null)) {
        throw new Fault(testName + "failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed - unexpected exception! ");
    }
  }

  /*
   * @testName: extensibleObject_removeSlotsTest
   *
   * @assertion: removeSlots(Collection)
   *
   * @assertion_ids: JAXR:JAVADOC:712
   *
   * @test_Strategy: Create 2 slots and add them to a PostalAddress. Verify the
   * addtions. Use removeSlots method to remove them. Verify the remove by
   * calling getSlots again. Should get back an empty collection.
   *
   */
  public void extensibleObject_removeSlotsTest() throws Fault {
    String testName = "extensibleObject_removeSlotsTest";
    String slotName = Slot.SORT_CODE_SLOT;
    String value = "2472974";
    String slotType = null;
    String slotName2 = Slot.ADDRESS_LINES_SLOT;
    String value2 = "2323233";
    try {
      Slot s1 = blm.createSlot(slotName, value, slotType);
      Slot s2 = blm.createSlot(slotName2, value2, slotType);
      Collection slots = new ArrayList();
      slots.add(s1);
      slots.add(s2);

      // create a postal address
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      pa.addSlot(s1);
      pa.addSlot(s2);
      debug.add("Add and verify 2 slots\n");
      Collection retSlots = pa.getSlots();
      if (!(retSlots.containsAll(slots))) {
        throw new Fault(testName
            + "failed to verify slots were added, test did not complete! ");
      }
      debug.add(
          "Slot addition verified ok - now remove the slots with removeSlots method \n");
      Collection slotNames = new ArrayList();
      slotNames.add(slotName);
      slotNames.add(slotName2);
      pa.removeSlots(slotNames);
      // debug.add("Verify the remove. Call getSlots. Should return an empty
      // collection. \n");
      Collection noSlots = pa.getSlots();
      debug.add("Returned " + noSlots.size() + " slots \n");
      if (noSlots.size() != 0) {
        throw new Fault(testName + "failed to return an empty collection \n");
      }
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "fail - unexpected error!");
    }
  }

} // end of test class
