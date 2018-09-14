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

package com.sun.ts.tests.j2eetools.mgmt.common;

// Java imports
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

// Harness imports
import com.sun.ts.lib.util.TestUtil;

// RMI Imports
import java.rmi.RemoteException;

// EJB imports
import javax.ejb.EJBObject;

// Management imports
import javax.management.Attribute;
import javax.management.QueryExp;
import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;
import javax.management.j2ee.Management;

/**
 * Static class which performs common test operations.
 *
 * @version 1.0
 */
public class MOUtils {

  // J2EE managed Object Attribute Names (needed by MOUtils)
  public static final String NAME_ATR = "objectName";

  // J2EE Managed Object Types, that are not actually MOs
  public static final String SESSION_BEAN = "SessionBean";

  public static final String J2EE_MOD = "J2EEModule";

  public static final String EJB = "EJB";

  public static final String J2EE_DEPLOYED_OBJ = "J2EEDeployedObject";

  public static final String J2EE_RESOURCE = "J2EEResource";

  // J2EE Managed Object Types
  public static final String J2EE_DOMAIN = "J2EEDomain";

  public static final String J2EE_SERVER = "J2EEServer";

  public static final String J2EE_APP = "J2EEApplication";

  public static final String APP_CLIENT_MOD = "AppClientModule";

  public static final String EJB_MOD = "EJBModule";

  public static final String WEB_MOD = "WebModule";

  public static final String RES_ADP_MOD = "ResourceAdapterModule";

  public static final String ENTITY_BEAN = "EntityBean";

  public static final String STATEFUL_SES_BEAN = "StatefulSessionBean";

  public static final String STATELESS_SES_BEAN = "StatelessSessionBean";

  public static final String MESSAGE_DR_BEAN = "MessageDrivenBean";

  public static final String SERVLET = "Servlet";

  public static final String RESOURCE_ADP = "ResourceAdapter";

  public static final String JAVA_MAIL_RES = "JavaMailResource";

  public static final String JCA_RES = "JCAResource";

  public static final String JCA_CONN_FACT = "JCAConnectionFactory";

  public static final String JCA_MNGD_CONN_FACT = "JCAManagedConnectionFactory";

  public static final String JDBC_RES = "JDBCResource";

  public static final String JDBC_DATA_SRC = "JDBCDataSource";

  public static final String JDBC_DRV = "JDBCDriver";

  public static final String JMS_RES = "JMSResource";

  public static final String JNDI_RES = "JNDIResource";

  public static final String JTA_RES = "JTAResource";

  public static final String RMI_IIOP_RES = "RMI_IIOPResource";

  public static final String URL_RES = "URLResource";

  public static final String JVM = "JVM";

  /**
   * Returns true if the specified objects have the same type else false.
   *
   * @param a
   *          The first object to compare the types of
   * @param a
   *          The second object to compare the types of
   * @return boolean true if the objects have the same type else false
   */
  private static boolean checkValueType(Object a, Object b) {

    TestUtil.logMsg("*** checkValueType() invoked");

    // lets handle cases when we have at least one null obj passed in
    if ((a == null) || (b == null)) {
      // we should not have any nulls at this point so if we do,
      // we'll want to return false
      TestUtil.logMsg("*** a = " + a);
      TestUtil.logMsg("*** b = " + b);
      return false;
    }

    TestUtil.logMsg("*** a.toString() = " + a.toString());
    TestUtil.logMsg("*** b.toString() = " + b.toString());
    String aName = a.getClass().getName();
    String bName = b.getClass().getName();
    return aName.equals(bName);
  }

  /**
   * Clears and sets the specifies attribute on the specified managed object.
   *
   * @param mejb
   *          The management EJB
   * @param oName
   *          The managed object's name, this is the object having it's
   *          attribute set
   * @param attr
   *          The attribute to set on the managed object
   * @throws Exception
   *           if there is an error clearing or setting the the managed object's
   *           attribute.
   */
  private static void checkSetAttribute(Management mejb, ObjectName oName,
      Attribute attr) throws Exception {
    // add checks to see if the attribute is writable and readable
    // add special logic for setting objectName, since it may not
    // be setable to a null value during the clear phase of this test
    Attribute clearAttr = new Attribute(attr.getName(), null);
    mejb.setAttribute(oName, clearAttr); // clear the current attribute
    Object obj = mejb.getAttribute(oName, attr.getName());
    if (obj != null) {
      throw new Exception(
          "Error clearing attribute \"" + attr.getName() + "\"");
    }
    mejb.setAttribute(oName, attr); // set to specified value
    obj = mejb.getAttribute(oName, attr.getName());
    if (obj == null || !obj.equals(attr.getValue())) {
      throw new Exception("Error setting attribute \"" + attr.getName() + "\"");
    }
  }

  /**
   * Returns true of the specified object is equal to an element in the
   * specified array.
   *
   * @param obj
   *          The object to compare to the elements in the specified array
   * @param objs
   *          The array to search for an equal object
   * @return boolean true of the specified object is equal to an element in the
   *         specified array else false
   */
  private static boolean arrayContains(Object obj, Object[] objs) {
    boolean result = false;
    for (int i = 0; i < objs.length; i++) {
      Object o = objs[i];
      if (obj.equals(o)) {
        result = true;
        break;
      }
    }
    return result;
  }

  /**
   * Returns true if every object in array a has an object that it is equal
   * (according to the equals method) to in array b. Otherwise the method return
   * false.
   *
   * @param a
   *          The object array whose elements must have an equal member in the
   *          other array
   * @param b
   *          The object array whose elements are being matched
   * @return boolean true if all the elements in a have an equal element in b
   */
  private static boolean arrayIsSubset(Object[] a, Object[] b) {
    boolean result = true;
    for (int i = 0; i < a.length; i++) {
      Object obj = a[i];
      if (!arrayContains(obj, b)) {
        result = false;
        break;
      }
    }
    return result;
  }

  /**
   * Returns true if the specified values are equal. If the specified object are
   * simple objects then the equals method is used to determine equality. If the
   * objects specified are arrays then each element of the array will have to
   * equal one object in the other array and vice versa. The size of the arrays
   * will also have to be equal. The equal length constraint implies that
   * duplicate objects must be duped in both arrays to be considered equal.
   *
   * @param a
   *          The array of objects that is being checked to see if each element
   *          is a member of the b array
   * @param b
   *          The array containing the comparison objects
   * @result boolean true if each object in a is equal to an object in b
   *         (according to the equals method) else false
   */
  private static boolean objectsEqual(Object a, Object b) {
    boolean result = false;
    if (a == null) {
      result = (b == null);
    } else if (a.getClass().isArray() && b.getClass().isArray()) {
      Object[] aArray = (Object[]) a;
      Object[] bArray = (Object[]) b;
      result = (aArray.length == bArray.length) && arrayIsSubset(aArray, bArray)
          && arrayIsSubset(bArray, aArray);
    } else {
      result = a.equals(b);
    }
    return result;
  }

  /**
   * Checks the attributes of the specified ObjectName. The expected attribute
   * values are specified in the attributes Map. If there are any errors an
   * exception is thrown and the client can deal with the error condition.
   *
   * @param mejb
   *          The management EJB used to retrieve the MO attributes
   * @param oName
   *          The name of the managed object
   * @param attributes
   *          The expected attribute values, the key is the attribute name and
   *          the value is the attribite Object mapped to the name
   * @param compareValues
   *          If set to true this method will verify that each attribute
   *          expected attribute valueis equal to the attribute value retrieved
   *          from the MO, if false the attribute values are ignored except for
   *          a type check
   * @throws Exception
   *           if there is an error checking or setting the any of the managed
   *           object's attributes
   */
  public static void checkAttributes(Management mejb, ObjectName oName,
      Map attributes, boolean compareValues) throws Exception {
    Iterator iter = attributes.keySet().iterator();
    while (iter.hasNext()) {
      TestUtil.logMsg("*** checkAttributes() invoked");
      String key = (String) iter.next();
      TestUtil.logMsg("*** Key: " + key);
      Object expectedValue = attributes.get(key);

      // throws AttributeNotFoundException if no such attribute exists
      Object mejbAttrObj = mejb.getAttribute(oName, key);
      // Special check for object name which can not be null
      if (key.equals(NAME_ATR) && mejbAttrObj == null) {
        throw new Exception("objectName must not be null");
      }

      if (mejbAttrObj == null) {
        throw new Exception(
            "Attribute value types not equal " + "for attribute '" + key
                + "' - with expected type = '" + expectedValue.getClass()
                + "'  but received NULL AttrObj from mejb.getAttribute() call");

      } else if (!checkValueType(mejbAttrObj, expectedValue)) {
        throw new Exception(
            "Attribute value types not equal " + "for attribute '" + key
                + "' - with expected type '" + expectedValue.getClass()
                + "' but received type '" + mejbAttrObj.getClass() + "'");
      }

      if (compareValues) {
        if (!objectsEqual(expectedValue, mejbAttrObj)) {
          throw new Exception(
              "Attribute values are not equal for attribute \"" + key + "\"");
        }
      }

      /*
       * The checkSetAttribute method can be used to set the value of the
       * attribute using the setAttribute API of the mejb interface. Currently
       * we are not doing this.
       */
      // checkSetAttribute(mejb, oName, new Attribute(key, expectedValue));
      TestUtil.logMsg(key + ": " + mejbAttrObj);
    }
  }

  /**
   * Checks the attributes of the specified ObjectName. The expected attribute
   * values are specified in the attributes Map. If there are any errors an
   * exception is thrown and the client can deal with the error condition.
   *
   * @param mejb
   *          The management EJB used to retrieve the MO attributes
   * @param oName
   *          The name of the managed object
   * @param attributes
   *          The expected attribute values, the key is the attribute name and
   *          the value is the attribite Object mapped to the name
   * @throws Exception
   *           if there is an error checking or setting the any of the managed
   *           object's attributes
   */
  public static void checkAttributes(Management mejb, ObjectName oName,
      Map attributes) throws Exception {
    checkAttributes(mejb, oName, attributes, false);
  }

  /**
   * Returns true if all the keys in map a are contained in map b's key set.
   * This does not mean that map a and map b are equal, it simply means map a's
   * keys are a subset of map b's keys. If compareValues is set to true we also
   * confirm that the keys in each Map, map to the same values (according to the
   * equals method).
   *
   * @param a
   *          Determine if this map is a subset of the other Map
   * @param b
   *          Determine if the other map is a subset of this Map
   * @param compareValues
   *          If true we also confirm that the Map values are equal between the
   *          two maps, If false we simply ensure Map a's key set is contained
   *          in Map b's key set and we ignore the values mapped to the keys
   */
  public static boolean mapIsSubset(Map a, Map b, boolean compareValues)
  // throws KeyNotFoundException
  {
    boolean result = true;
    if (a == null) {
      return result;
    }
    if (b == null) {
      return a == null;
    }
    Iterator aIter = a.keySet().iterator();
    while (aIter.hasNext()) {
      String aKey = (String) aIter.next();
      if (!b.containsKey(aKey)) {
        result = false;
        break;
        // throw new KeyNotFoundException(aKey);
      }
      if (compareValues) {
        Object aObject = a.get(aKey);
        Object bObject = b.get(aKey);
        result = objectsEqual(aObject, bObject);
        if (!result) {
          break;
        }
      }
    }
    return result;
  }

  /**
   * Returns true if all the key value pairs in map a are contained in map b.
   * This does not mean that map a and map b are equal, it simply means map a
   * keys are a subset of map b keys. The values mapped to the keys are ignored.
   *
   * @param a
   *          Determine if this map is a subset of the other Map
   * @param b
   *          Determine if the other map is a subset of this Map
   */
  public static boolean mapIsSubset(Map a, Map b)
  // throws KeyNotFoundException
  {
    return mapIsSubset(a, b, false);
  }

  /**
   * Returns true if all the keys that are in map a are contained in map b's key
   * set and map b's key set is contained in map a's key set. If compareValues
   * is set to true the values mapped to the keys must also equal (according to
   * the equals method). If compareValues is false the mapped values are
   * ignored.
   *
   * @param a
   *          Map to compare for equality.
   * @param b
   *          Map to compare for equality.
   * @param compareValues
   *          If true we also confirm that the Map values are equal between the
   *          two maps, If false we simply ensure Map a's key set is equivalent
   *          to Map b's key set and we ignore the values mapped to the keys
   */
  public static boolean mapsEqual(Map a, Map b, boolean compareValues)
  // throws KeyNotFoundException
  {
    return (mapIsSubset(a, b, compareValues)
        && mapIsSubset(a, b, compareValues));
  }

  /**
   * Returns true if all the keys that are in map a are contained in map b's key
   * set and map b's key set is contained in map a's key set. The mapped values
   * are ignored.
   *
   * @param a
   *          Map to compare for equality.
   * @param b
   *          Map to compare for equality.
   */
  public static boolean mapsEqual(Map a, Map b)
  // throws KeyNotFoundException
  {
    return mapsEqual(a, b, false);
  }

  /**
   * Check the validity of specified object name string. If the specified string
   * is invalid a MalformedObjectNameException is thrown.
   *
   * @param oName
   *          The string representation of the managed object name
   * @throws MalformedObjectNameException
   *           If the specified managed object name string is invalid
   */
  public static void checkObjectName(String oName)
      throws MalformedObjectNameException {
    TestUtil.logMsg("Verifying: " + oName);
    ObjectName newOname = new ObjectName(oName);
  }

  /**
   * Returns a Set for a given search pattern
   *
   * @param mejb
   *          The management EJB
   * @param searchpattern
   *          The string that specifies which managed objects to search for
   * @return Set The set of managed objects that match the search criteria
   * @throws RemoteException
   *           if there is a network error communicating with the MEJB
   * @throws MalformedObjectNameException
   *           If the specified search pattern an invalid management object name
   */
  public static Set getManagedObjects(Management mejb, String searchpattern)
      throws RemoteException, MalformedObjectNameException {
    // We're not using QueryExp, defined in JMX spec. We'll just
    // match based on OBJECT_NAME
    QueryExp myq = null;
    ObjectName onamesearch = new ObjectName(searchpattern);
    return (mejb.queryNames(onamesearch, myq));
  }

} // end class MOUtils
