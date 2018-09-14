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

package com.sun.ts.tests.compat13.ejb.oneXmany;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String bean = "java:comp/env/ejb/Bean";

  private Bean bRef = null;

  private BeanHome bHome = null;

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + bean);
      bHome = (BeanHome) nctx.lookup(bean, BeanHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void dumpCollectionDataA(Collection c) {
    logMsg("Collection Data");
    logMsg("---------------");
    logMsg("- size=" + c.size());
    Iterator i = c.iterator();
    int elem = 1;
    while (i.hasNext()) {
      ADVC v = (ADVC) i.next();
      logMsg("- Element #" + elem++);
      logMsg("  id=" + v.getId() + ", name=" + v.getName() + ", value="
          + v.getValue());
    }
  }

  private void dumpCollectionDataB(Collection c) {
    logMsg("Collection Data");
    logMsg("---------------");
    logMsg("- size=" + c.size());
    Iterator i = c.iterator();
    int elem = 1;
    while (i.hasNext()) {
      BDVC v = (BDVC) i.next();
      logMsg("- Element #" + elem++);
      logMsg("  id=" + v.getId() + ", name=" + v.getName() + ", value="
          + v.getValue());
    }
  }

  /* Run test */

  /*
   * @testName: compat13_1xM_test0
   * 
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: If the cmr-fields are not set than they should be set to
   * empty or null by default by the persistence manager. Create a 1xmany
   * bi-directional relationship between entitybean objects. Do not set the
   * relationship fields. The results should be set to empty or null. Deploy EAR
   * built on a J2EE 1.3 platform to ensure application portability. Ensure the
   * entity beans were created and that the persistence manager has null
   * settings for the relationship fields not set.
   *
   */

  public void compat13_1xM_test0() throws Fault {
    boolean pass = true;
    try {
      // Create entity bean with two entitybean objects in a 1xmany
      // bi-directional relationship with each other (no relations set)
      logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      bRef = bHome.create("1", "bean1", 1, a1, b1, 0);
      bRef.init(props);

      // Bi-Directional relationship fields should be empty or null for beans
      if (bRef.test0())
        TestUtil.logMsg("relationship fields are empty or null - expected");
      else {
        TestUtil
            .logErr("relationship fields are not empty or null - unexpected");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("compat13_1xM_test0 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("compat13_1xM_test0 failed");
  }

  /*
   * @testName: compat13_1xM_test0a
   * 
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: If the cmr-fields are set to null than they should be set
   * to null by default by the persistence manager. For a collection expect
   * IllegalArgumentException if set to null. Create a 1xmany bi-directional
   * relationship between entitybean objects. Do set relationship fields to
   * null. The results should be set to null for single-valued relatonships and
   * raise IllegaArgumentException for Collection fields. Deploy EAR built on a
   * J2EE 1.3 platform to ensure application portability. Ensure the entity
   * beans were created and that the persistence manager has null settings for
   * the relationship fields not set.
   *
   *
   */

  public void compat13_1xM_test0a() throws Fault {
    boolean pass = true;
    try {
      // Create entity bean with two entitybean objects in a 1xmany
      // bi-directional relationship with each other (no relations set)
      logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      bRef = bHome.create("1", "bean1", 1, a1, b1, 1);
      bRef.init(props);

      // Bi-Directional relationship fields should be empty or null for beans
      if (bRef.test0())
        TestUtil.logMsg("relationship fields are empty or null - expected");
      else {
        TestUtil
            .logErr("relationship fields are not empty or null - unexpected");
        pass = false;
      }

      TestUtil.logMsg("Attempting to set collection cmr-field to null");
      pass = bRef.setCmrFieldToNull();
    } catch (Exception e) {
      throw new Fault("compat13_1xM_test0a failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("compat13_1xM_test0a failed");
  }

  /*
   * @testName: compat13_1xM_test0b
   * 
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: If the collection cmr-fields are set to the wrong
   * relationship type than the persistence manager should raise the
   * IllegalArgumentException. Uses set accessor method. Create a 1xmany
   * bi-directional relationship between entitybean objects. Do set relationship
   * fields to collection containing wrong type using the set method. The
   * results should be the exception IllegalArgumentException. Deploy EAR built
   * on a J2EE 1.3 platform to ensure application portability. Ensure the entity
   * beans were created and that the persistence manager has null settings for
   * the relationship fields not set.
   *
   * Create 1 entity bean and verify the exception IllegalArgumentException is
   * raised.
   *
   */

  public void compat13_1xM_test0b() throws Fault {
    boolean pass = true;
    try {
      // Create entity bean with two entitybean objects in a 1xmany
      // bi-directional relationship with each other (no relations set)
      logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      bRef = bHome.create("1", "bean1", 1, a1, b1, 0);
      bRef.init(props);

      TestUtil.logMsg("Attempting to set collection cmr-fields to wrong type");
      pass = bRef.setCmrFieldToWrongType(1);
    } catch (Exception e) {
      throw new Fault("compat13_1xM_test0b failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("compat13_1xM_test0b failed");
  }

  /*
   * @testName: compat13_1xM_test0c
   * 
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: If the collection cmr-fields are set to the wrong
   * relationship type than the persistence manager should raise the
   * IllegalArgumentException to null. Uses the Collection API add method.
   * Create a 1xmany bi-directional relationship between entitybean objects. Do
   * set relationship fields to collection containing wrong type using the
   * Collection api add(Object o) method. The results should be the exception
   * IllegalArgumentException. Deploy EAR built on J2EE 1.3 platform to ensure
   * application portability. Ensure the entity beans were created and that the
   * persistence manager has null settings for the relationship fields not set.
   *
   * Create 1 entity bean and verify the exception IllegalArgumentException is
   * raised.
   *
   */

  public void compat13_1xM_test0c() throws Fault {
    boolean pass = true;
    try {
      // Create entity bean with two entitybean objects in a 1xmany
      // bi-directional relationship with each other (no relations set)
      logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      bRef = bHome.create("1", "bean1", 1, a1, b1, 0);
      bRef.init(props);

      TestUtil.logMsg("Attempting to set collection cmr-fields to wrong type");
      pass = bRef.setCmrFieldToWrongType(2);
    } catch (Exception e) {
      throw new Fault("compat13_1xM_test0c failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("compat13_1xM_test0c failed");
  }

  /*
   * @testName: compat13_1xM_test1
   * 
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: Assignment rules for relationships. 0..1 0..* A <-----> B
   * a1<----->b11,b12 a2<----->b21,b22 Collection b1 = a1.getB() Collection b2 =
   * a2.getB()
   *
   * Change: a1.setB(a2.getB())
   *
   * Result: a1.getB() = collection containing b21,b22 a2.getB() = empty
   * collection b1 == a1.getB() b2 == a2.getB() b11.getA() = null b12.getA() =
   * null b21.getA() = a1 b22.getA() = a1
   * 
   * Create a 1xmany bi-directional relationship between entitybean objects.
   * Deploy EAR built on the J2EE 1.3 platform to ensure application
   * portability. Perform the relationship assignment listed above. Ensure the
   * proper relationship results are correct after the assignment by the
   * persistence manager.
   *
   */

  public void compat13_1xM_test1() throws Fault {
    boolean pass = false;
    try {
      // Create A and B entitybean objects
      logMsg("Create A and B EntityBean Objects");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b11 = new BDVC("11", "b11", 11);
      BDVC b12 = new BDVC("12", "b12", 12);

      ADVC a2 = new ADVC("2", "a2", 2);
      BDVC b21 = new BDVC("21", "b21", 21);
      BDVC b22 = new BDVC("22", "b22", 22);

      bRef = bHome.create("1", "bean1", 1, a1, b11, b12, a2, b21, b22);
      bRef.init(props);

      pass = bRef.doAssignmentTest1();
    } catch (Exception e) {
      throw new Fault("compat13_1xM_test1 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("compat13_1xM_test1 failed");
  }

  /*
   * @testName: compat13_1xM_test2
   * 
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: Assignment rules for relationships. 0..1 0..* A <-----> B
   * a1<----->b11,b12 a2<----->b21,b22 Collection b1 = a1.getB() Collection b2 =
   * a2.getB()
   *
   * Change: b22.setA(b12.getA())
   *
   * Result: a1.getB() = collection containing b11,b12,b22 a2.getB() =
   * collection containing b21 b11.getA() = a1 b12.getA() = a1 b21.getA() = a2
   * b22.getA() = a1
   * 
   * Create a 1xmany bi-directional relationship between entitybean objects.
   * Deploy EAR built on the J2EE 1.3 platform to ensure application
   * portability. Perform the relationship assignment listed above. Ensure the
   * proper relationship results are correct after the assignment by the
   * persistence manager.
   *
   */

  public void compat13_1xM_test2() throws Fault {
    boolean pass = false;
    try {
      // Create A and B entitybean objects
      logMsg("Create A and B EntityBean Objects");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b11 = new BDVC("11", "b11", 11);
      BDVC b12 = new BDVC("12", "b12", 12);

      ADVC a2 = new ADVC("2", "a2", 2);
      BDVC b21 = new BDVC("21", "b21", 21);
      BDVC b22 = new BDVC("22", "b22", 22);

      bRef = bHome.create("1", "bean1", 1, a1, b11, b12, a2, b21, b22);
      bRef.init(props);

      pass = bRef.doAssignmentTest2();
    } catch (Exception e) {
      throw new Fault("compat13_1xM_test2 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("compat13_1xM_test2 failed");
  }

  /*
   * @testName: compat13_1xM_test3
   * 
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: Assignment rules for relationships. 0..1 0..* A <-----> B
   * a1<----->b11,b12 a2<----->b21,b22
   *
   * Change: a1.getB().add(b22)
   *
   * Result: a1.getB() = collection containing b11,b12,b22 a2.getB() =
   * collection containing b21 b11.getA() = a1 b12.getA() = a1 b21.getA() = a2
   * b22.getA() = a1
   * 
   * Create a 1xmany bi-directional relationship between entitybean objects.
   * Deploy EAR built on the J2EE 1.3 platform to ensure application
   * portability. Perform the relationship assignment listed above. Ensure the
   * proper relationship results are correct after the assignment by the
   * persistence manager.
   *
   */

  public void compat13_1xM_test3() throws Fault {
    boolean pass = false;
    try {
      // Create A and B entitybean objects
      logMsg("Create A and B EntityBean Objects");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b11 = new BDVC("11", "b11", 11);
      BDVC b12 = new BDVC("12", "b12", 12);

      ADVC a2 = new ADVC("2", "a2", 2);
      BDVC b21 = new BDVC("21", "b21", 21);
      BDVC b22 = new BDVC("22", "b22", 22);

      bRef = bHome.create("1", "bean1", 1, a1, b11, b12, a2, b21, b22);
      bRef.init(props);

      pass = bRef.doAssignmentTest3();
    } catch (Exception e) {
      throw new Fault("compat13_1xM_test3 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("compat13_1xM_test3 failed");
  }

  /*
   * @testName: compat13_1xM_test4
   * 
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: Assignment rules for relationships. 0..1 0..* A <-----> B
   * a1<----->b11,b12 a2<----->b21,b22
   *
   * Change: a1.getB().remove(b12)
   *
   * Result: a1.getB() = collection containing b11 b12.getA() = null
   * 
   * Create a 1xmany bi-directional relationship between entitybean objects.
   * Deploy EAR built on the J2EE 1.3 platform to ensure application
   * portability. Perform the relationship assignment listed above. Ensure the
   * proper relationship results are correct after the assignment by the
   * persistence manager.
   *
   */

  public void compat13_1xM_test4() throws Fault {
    boolean pass = false;
    try {

      // Create A and B entitybean objects
      logMsg("Create A and B EntityBean Objects");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b11 = new BDVC("11", "b11", 11);
      BDVC b12 = new BDVC("12", "b12", 12);

      ADVC a2 = new ADVC("2", "a2", 2);
      BDVC b21 = new BDVC("21", "b21", 21);
      BDVC b22 = new BDVC("22", "b22", 22);

      bRef = bHome.create("1", "bean1", 1, a1, b11, b12, a2, b21, b22);
      bRef.init(props);

      pass = bRef.doAssignmentTest4();
    } catch (Exception e) {
      throw new Fault("compat13_1xM_test4 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("compat13_1xM_test4 failed");
  }

  /*
   * @testName: compat13_1xM_test5
   * 
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: Create a 1xmany bi-directional relationship between
   * entitybean objects. Deploy EAR build on a J2EE 1.3 platform to ensure
   * application portability. Ensure the entity beans were created and that the
   * persistence manager is handling getting/setting of data. Ensure
   * bi-directional data access from both entitybean objects.
   *
   * Create 1 entity bean and verify the data. Verify bi-directional access of
   * relationship data.
   *
   */

  public void compat13_1xM_test5() throws Fault {
    boolean pass = true;
    try {
      // Create A and B entitybean objects
      logMsg("Create A and B EntityBean Objects");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b11 = new BDVC("11", "b11", 11);
      BDVC b12 = new BDVC("12", "b12", 12);

      bRef = bHome.create("1", "bean1", 1, a1, b11, b12, null, null, null);
      bRef.init(props);

      // Get relationship info from bean set

      // Bi-Directional Relationship access
      // Get B info from A
      Vector b = new Vector();
      b.add(new BDVC("11", "b11", 11));
      b.add(new BDVC("12", "b12", 12));
      logMsg("Getting B info from entitybean object A");
      Collection bInfo = bRef.getBInfoFromA();
      logMsg("Dumping B info ...");
      dumpCollectionDataB(bInfo);

      // Bi-Directional Relationship access
      // Get A info from B
      Vector a = new Vector();
      a.add(new ADVC("1", "a1", 1));
      logMsg("Getting A info from entitybean object B");
      Collection aInfo = bRef.getAInfoFromB();
      dumpCollectionDataA(aInfo);

      if (!bInfo.containsAll(b) || !aInfo.containsAll(a)) {
        pass = false;
        logErr("DataMismatch error");
        if (!bInfo.containsAll(b))
          logErr("bInfo does not match expected results");
        if (!aInfo.containsAll(a))
          logErr("aInfo does not match expected results");
      } else
        logMsg("Data matches as expected");
    } catch (Exception e) {
      throw new Fault("compat13_1xM_test5 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("compat13_1xM_test5 failed");
  }

}
