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
 * @(#)BaseMO.java	1.10 03/05/16
 */

package com.sun.ts.tests.j2eetools.mgmt.common;

// Java imports
import java.io.*;
import java.util.*;

// Harness imports
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

// RMI imports
import java.rmi.RemoteException;

// EJB imports
import javax.ejb.*;
import javax.rmi.PortableRemoteObject;

// Management imports
import javax.management.*;
import javax.management.j2ee.Management;
import javax.management.j2ee.ManagementHome;
import javax.management.ObjectName;

/**
 * BaseMO class is used for all J2EE Management API testing. This class verifies
 * MO attributes and key values pairs in the object name. Test clients extend it
 * this class adding attributes and attribute values as well as keys and key
 * values.
 *
 * @version 1.0
 */
public abstract class BaseMO extends ServiceEETest implements Serializable {

  // MEJB fields. Used in this class only to perform lookup of MEJB
  private static final String mejbLookup = "java:comp/env/ejb/MEJB";

  private ManagementHome mejbHome = null;

  private TSNamingContext ctx = null;

  // mejb bean reference will be used in extending classes to check
  // specific attributes to the MO under test
  private Management mejb = null;

  // MOs to be validated. Used by all test methods in this package
  private Set mos;

  // These maps contain the expected values for attributes and managed
  // object name keys
  protected Map moRequiredKeyValues; // required keys for all MOs

  protected Map moParentKeyValues; // Parent keys for a MO

  protected Map moAttributeValues; // MO attributes

  // Base MO attributes, all MOs have these attributes
  public static final String NAME_ATR = "objectName";

  public static final String STATE_ATR = "stateManageable";

  public static final String STAT_ATR = "statisticsProvider";

  public static final String EVENT_ATR = "eventProvider";

  // Used to specify the values associated with the attributes.
  // These methods should be overridden by child classes if they
  // wish to provide expected values for the MO's base attributes.
  // Hmmmm, the getAttribute method returns a String not an
  // ObjectName
  // protected Object getNameAtrValue() {
  // try { return new ObjectName(""); }
  // catch (Exception e) { return null; }
  // }
  protected Object getNameAtrValue() {
    return new String("");
  }

  protected Object getStateAtrValue() {
    return new Boolean(false);
  }

  protected Object getStatAtrValue() {
    return new Boolean(false);
  }

  protected Object getEventAtrValue() {
    return new Boolean(false);
  }

  /**
   * Adds all the attributes that must be present in every MO. If a test class
   * extends this class and wants to add attributes they must override this
   * method and call super.initAttributes() and add the new attributes to the
   * returned Map and then return the modified Map.
   *
   * @return Map The expected managed object attributes and their associated
   *         values
   */
  protected Map initAttributes() {
    Map result = new HashMap();
    result.put(NAME_ATR, getNameAtrValue());
    result.put(STATE_ATR, getStateAtrValue());
    result.put(STAT_ATR, getStatAtrValue());
    result.put(EVENT_ATR, getEventAtrValue());
    return result;
  }

  // Required object name keys
  private static final String J2EE_TYPE_KEY = "j2eeType";

  private static final String NAME_KEY = "name";

  // Used to specify the values associated to the required object
  // name key values. These methods should be overridden by child
  // classes if they wish to provide expected values for the MO's
  // key values.
  protected Object getTypeKeyValue() {
    return null;
  }

  protected Object getNameKeyValue() {
    return null;
  }

  /**
   * Adds all the name keys that must be present in every MO's object name.
   * Currently those keys are j2eeType and name. If a child-class wishes to add
   * or modify these keys or their values the sub-class should override this
   * method.
   *
   * @return Map The expected managed object name keys and their associated
   *         values
   */
  protected Map initRequiredKeys() {
    Map result = new HashMap();
    result.put(J2EE_TYPE_KEY, getTypeKeyValue());
    result.put(NAME_KEY, getNameKeyValue());
    return result;
  }

  /**
   * Adds all the expected parent keys and their associated values. This method
   * returns an empty HashMap since there are no common parent keys across the
   * MO data model. Subclasses hould override this method to provide parent keys
   * and values.
   */
  protected Map initParentKeys() {
    return new HashMap();
  }

  /* convenience method for dumping set members */
  private void dumpSet(Set set, String message) {
    TestUtil.logTrace(message + ":");
    if (set != null) {
      Iterator setIter = set.iterator();
      try {
        int ii = 0;
        while (setIter.hasNext()) {
          ObjectName objectname = (ObjectName) setIter.next();
          if (objectname == null) {
            TestUtil.logTrace("Set item[" + ii + "] = null");
          } else {
            TestUtil
                .logTrace("Set item[" + ii + "] = " + objectname.toString());
          }
          ii++;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      TestUtil.logTrace("\n");
    }
  }

  private void dumpMap(Map map, String message) {
    StringBuffer buf = new StringBuffer("\t");
    TestUtil.logMsg(message + ":");
    if (map != null) {
      Iterator iter = map.keySet().iterator();
      while (iter.hasNext()) {
        String key = (String) iter.next();
        buf.append(key);
        if (iter.hasNext()) {
          buf.append(", ");
        }
      }
      TestUtil.logMsg(buf.toString());
    }
  }

  private void dumpMaps() {
    dumpMap(moAttributeValues, "*** Attribute Keys");
    dumpMap(moRequiredKeyValues, "*** Required Keys");
    dumpMap(moParentKeyValues, "*** Parent Keys");
  }

  /**
   *
   * Setup method. Called by harness for each test in this source.
   *
   * @param args
   *          List of test arguments
   * @param p
   *          Test properties
   * @throws Fault
   *           If there is any error in the setup process
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      moAttributeValues = initAttributes();
      moRequiredKeyValues = initRequiredKeys();
      moParentKeyValues = initParentKeys();
      dumpMaps();
      TestUtil.logTrace("Setup method, intialization complete");

      ctx = new TSNamingContext();
      TestUtil.logTrace("Got TSNamingContext");

      TestUtil.logMsg("###: loookup:" + mejbLookup);
      Object obj = ctx.lookup(mejbLookup);
      mejbHome = (ManagementHome) PortableRemoteObject.narrow(obj,
          ManagementHome.class);
      // mejbHome = (ManagementHome)ctx.lookup(mejbLookup);
      TestUtil.logTrace("Got MEJB Home interface");

      mejb = mejbHome.create();
      TestUtil.logMsg("Created MEJB instance");

      // Get a Set of MO's. All tests will use this set of ObjectNames
      TestUtil.logMsg("*** Looking for: " + getMOName());
      this.mos = MOUtils.getManagedObjects(mejb, getMOName());
      TestUtil.logMsg("*** Found " + mos.size() + " matching managed objects.");

      // dump the set of managed objects
      dumpSet(this.mos, "Dumping MEJB managed objects ");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("###:", e);
      TestUtil.logMsg("Exception during setup: " + e.getMessage());
    }
  }

  /**
   * Cleanup method. Called by harness after each test in this source.
   *
   * @throws Fault
   *           If there is any error in the setup process
   */
  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup() invoked");
    try {
      mejb.remove();
    } catch (Exception e) {
      TestUtil.logErr("Error removing MEJB reference: " + e.getMessage(), e);
    }
  }

  /**
   * Returns the lookup string passed to MEJB.queryNames(). All sub-classes must
   * override this method to provide the lookup name of the managed objects the
   * test is interested in.
   */
  public abstract String getMOName();

  /**
   * Checks that all J2EE managed objects (of the retrieved type) contains the
   * expected attributes. The setup method retrieves the J2EE managed object set
   * and this method iterates over the set verifying that the expected
   * attributes exist. The expected attributes are stored in the
   * moAttributeValues instance variable. This variable is initialized during
   * the setup method.
   *
   * @throws Exception
   *           If there is an error checking the attributes
   */
  public void checkAttributes() throws Exception {
    // Need to figure out how to handle URIResource, since it is not a
    // required MO based on the J2EE Platform spec.
    // if (mos.size() <= 0) {
    // throw new Exception
    // ("FAILED: No Managed Objects Found in checkAttributes" +
    // "MO Query String = " + getMOName());
    // }
    Iterator mosIter = this.mos.iterator();
    try {
      while (mosIter.hasNext()) {
        ObjectName objectname = (ObjectName) mosIter.next();
        MOUtils.checkAttributes(mejb, objectname, moAttributeValues);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("FAILED: Error on test: " + e.getMessage());
    }
  }

  /**
   * This method verifies that the specified key values exist in the managed
   * object set that we are currently testing.
   *
   * @param expectedValues
   *          The expected key value pairs in the managed object name
   * @throws Exception
   *           if the actual keys do not match the expected keys
   */
  private void checkKeys(Map expectedValues) throws Exception {
    // if (mos.size() <= 0) {
    // throw new Exception
    // ("FAILED: No Managed Objects Found in checkKeys" +
    // "MO Query String = " + getMOName());
    // }
    Iterator mosIter = this.mos.iterator();
    try {
      while (mosIter.hasNext()) {
        ObjectName objectname = (ObjectName) mosIter.next();
        Map moAttributes = objectname.getKeyPropertyList();
        if (!MOUtils.mapIsSubset(expectedValues, moAttributes)) {
          throw new Exception("Keys do not match");
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("FAILED: Error on test: " + e.getMessage());
    }
  }

  /**
   * This method verifies that the manadatory key values exist in the managed
   * object set that we are currently testing.
   *
   * @throws Exception
   *           if the actual keys do not match the expected keys
   */
  public void checkMandatoryKeys() throws Exception {
    checkKeys(moRequiredKeyValues);
  }

  /**
   * This method verifies that the parent key values exist in the managed object
   * set that we are currently testing.
   *
   * @throws Exception
   *           if the actual keys do not match the expected keys
   */
  public void checkParentKeys() throws Exception {
    checkKeys(moParentKeyValues);
  }

  /***** These are the actually test methods *****/

  public void testAttributes() throws Fault {
    TestUtil.logMsg("Starting testAttributes " + getMOName());
    try {
      checkAttributes();
    } catch (Exception e) {
      TestUtil.logErr("###", e);
      throw new Fault("FAILED: Error on test: " + e.getMessage(), e);
    }
  }

  public void testMandatoryKeys() throws Fault {
    TestUtil.logMsg("Starting testMandatoryKeys " + getMOName());
    try {
      checkMandatoryKeys();
    } catch (Exception e) {
      throw new Fault("FAILED: Error on test: " + e.getMessage(), e);
    }
  }

  public void testParentKeys() throws Fault {
    TestUtil.logMsg("Starting testParentKeys " + getMOName());
    try {
      checkParentKeys();
    } catch (Exception e) {
      throw new Fault("FAILED: Error on test: " + e.getMessage(), e);
    }
  }

}
