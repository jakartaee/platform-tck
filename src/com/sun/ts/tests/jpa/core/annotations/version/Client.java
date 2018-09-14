/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.version;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.sql.Timestamp;
import java.util.*;

public class Client extends PMClientBase {

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setupIntData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupIntData");
    try {
      super.setup(args, p);

      removeTestData();
      createIntTestData();

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupShortData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupShortData");
    try {
      super.setup(args, p);

      removeTestData();
      createShortTestData();

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupLongData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupLongData");
    try {
      super.setup(args, p);

      removeTestData();
      createLongTestData();

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupTimestampData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupTimestampData");
    try {
      super.setup(args, p);

      removeTestData();
      createTimestampTestData();

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: intFieldTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2115; PERSISTENCE:SPEC:2116;
   * PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.1
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupIntData")
  public void intFieldTest() throws Fault {

    boolean pass = false;
    try {
      Int_Field a = getEntityManager().find(Int_Field.class, "1");
      if (a != null) {
        // if (a.getVersion() == 1) {
        TestUtil.logTrace("version:" + a.getVersion());
        int version = a.getVersion();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Int_Field a1 = getEntityManager().find(Int_Field.class, "1");
        if (a1 != null) {
          if (a1.getVersion() > version) {
            TestUtil.logTrace("version:" + a1.getVersion());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getVersion());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("intFieldTest failed");
    }

  }

  /*
   * @testName: intPropertyTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2115; PERSISTENCE:SPEC:2116;
   * PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.1
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupIntData")
  public void intPropertyTest() throws Fault {
    boolean pass = false;
    try {
      Int_Property a = getEntityManager().find(Int_Property.class, "2");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getValue());
        // if (a.getVersion() == 1) {
        int version = a.getValue();

        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Int_Property a1 = getEntityManager().find(Int_Property.class, "2");
        if (a1 != null) {
          if (a1.getValue() > version) {
            TestUtil.logTrace("version:" + a1.getValue());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getValue());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("intPropertyTest failed");
    }

  }

  /*
   * @testName: integerFieldTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.2
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupIntData")
  public void integerFieldTest() throws Fault {

    boolean pass = false;
    try {
      Integer_Field a = getEntityManager().find(Integer_Field.class, "3");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getVersion());
        // if (a.getVersion() == 1) {
        Integer version = a.getVersion();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Integer_Field a1 = getEntityManager().find(Integer_Field.class, "3");
        if (a1 != null) {
          if (a1.getVersion() > version) {
            TestUtil.logTrace("version:" + a1.getVersion());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getVersion());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("integerFieldTest failed");
    }

  }

  /*
   * @testName: integerPropertyTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.2
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupIntData")
  public void integerPropertyTest() throws Fault {
    boolean pass = false;
    try {
      Integer_Property a = getEntityManager().find(Integer_Property.class, "4");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getBasicInteger());
        // if (a.getVersion() == 1) {
        Integer version = a.getBasicInteger();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Integer_Property a1 = getEntityManager().find(Integer_Property.class,
            "4");
        if (a1 != null) {
          if (a1.getBasicInteger() > version) {
            TestUtil.logTrace("version:" + a1.getBasicInteger());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getBasicInteger());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("integerPropertyTest failed");
    }

  }

  /*
   * @testName: shortFieldTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.3
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupShortData")
  public void shortFieldTest() throws Fault {

    boolean pass = false;
    try {
      Short_Field a = getEntityManager().find(Short_Field.class, "1");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getVersion());
        // if (a.getVersion() == 1) {
        short version = a.getVersion();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Short_Field a1 = getEntityManager().find(Short_Field.class, "1");
        if (a1 != null) {
          if (a1.getVersion() > version) {
            TestUtil.logTrace("version:" + a1.getVersion());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getVersion());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("shortFieldTest failed");
    }

  }

  /*
   * @testName: shortPropertyTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.3
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupShortData")
  public void shortPropertyTest() throws Fault {
    boolean pass = false;
    try {
      Short_Property a = getEntityManager().find(Short_Property.class, "2");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getBasicShort());
        // if (a.getVersion() == 1) {
        short version = a.getBasicShort();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Short_Property a1 = getEntityManager().find(Short_Property.class, "2");
        if (a1 != null) {
          if (a1.getBasicShort() > version) {
            TestUtil.logTrace("version:" + a1.getBasicShort());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getBasicShort());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("shortPropertyTest failed");
    }

  }

  /*
   * @testName: shortClassFieldTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.4
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupShortData")
  public void shortClassFieldTest() throws Fault {

    boolean pass = false;
    try {
      ShortClass_Field a = getEntityManager().find(ShortClass_Field.class, "3");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getVersion());
        // if (a.getVersion() == 1) {
        Short version = a.getVersion();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        ShortClass_Field a1 = getEntityManager().find(ShortClass_Field.class,
            "3");
        if (a1 != null) {
          if (a1.getVersion() > version) {
            TestUtil.logTrace("version:" + a1.getVersion());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getVersion());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("shortClassFieldTest failed");
    }

  }

  /*
   * @testName: shortClassPropertyTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.4
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupShortData")
  public void shortClassPropertyTest() throws Fault {
    boolean pass = false;
    try {
      ShortClass_Property a = getEntityManager().find(ShortClass_Property.class,
          "4");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getBasicShort());
        // if (a.getVersion() == 1) {
        Short version = a.getBasicShort();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        ShortClass_Property a1 = getEntityManager()
            .find(ShortClass_Property.class, "4");
        if (a1 != null) {
          if (a1.getBasicShort() > version) {
            TestUtil.logTrace("version:" + a1.getBasicShort());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getBasicShort());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("shortClassPropertyTest failed");
    }

  }

  /*
   * @testName: longFieldTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.5
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupLongData")
  public void longFieldTest() throws Fault {

    boolean pass = false;
    try {
      Long_Field a = getEntityManager().find(Long_Field.class, "1");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getVersion());
        // if (a.getVersion() == 1) {
        long version = a.getVersion();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Long_Field a1 = getEntityManager().find(Long_Field.class, "1");
        if (a1 != null) {
          if (a1.getVersion() > version) {
            TestUtil.logTrace("version:" + a1.getVersion());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getVersion());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("shortFieldTest failed");
    }

  }

  /*
   * @testName: longPropertyTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.5
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupLongData")
  public void longPropertyTest() throws Fault {
    boolean pass = false;
    try {
      Long_Property a = getEntityManager().find(Long_Property.class, "2");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getBasicLong());
        // if (a.getVersion() == 1) {
        long version = a.getBasicLong();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Long_Property a1 = getEntityManager().find(Long_Property.class, "2");
        if (a1 != null) {
          if (a1.getBasicLong() > version) {
            TestUtil.logTrace("version:" + a1.getBasicLong());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getBasicLong());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("shortPropertyTest failed");
    }

  }

  /*
   * @testName: longClassFieldTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.6
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupLongData")
  public void longClassFieldTest() throws Fault {

    boolean pass = false;
    try {
      LongClass_Field a = getEntityManager().find(LongClass_Field.class, "3");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getVersion());
        // if (a.getVersion() == 1) {
        Long version = a.getVersion();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        LongClass_Field a1 = getEntityManager().find(LongClass_Field.class,
            "3");
        if (a1 != null) {
          if (a1.getVersion() > version) {
            TestUtil.logTrace("version:" + a1.getVersion());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getVersion());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("shortClassFieldTest failed");
    }

  }

  /*
   * @testName: longClassPropertyTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.6
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupLongData")
  public void longClassPropertyTest() throws Fault {
    boolean pass = false;
    try {
      LongClass_Property a = getEntityManager().find(LongClass_Property.class,
          "4");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getBasicLong());
        // if (a.getVersion() == 1) {
        Long version = a.getBasicLong();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        LongClass_Property a1 = getEntityManager()
            .find(LongClass_Property.class, "4");
        if (a1 != null) {
          if (a1.getBasicLong() > version) {
            TestUtil.logTrace("version:" + a1.getBasicLong());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getBasicLong());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("shortClassPropertyTest failed");
    }

  }

  /*
   * @testName: timestampFieldTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.7
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupTimestampData")
  public void timestampFieldTest() throws Fault {

    boolean pass = false;
    try {
      Timestamp_Field a = getEntityManager().find(Timestamp_Field.class, "1");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getVersion());
        // if (a.getVersion() == 1) {
        Timestamp version = a.getVersion();
        a.setName("two");
        // Sleep for 1 second
        try {
          Thread.sleep(1 * 1000L);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Timestamp_Field a1 = getEntityManager().find(Timestamp_Field.class,
            "1");
        if (a1 != null) {
          if (a1.getVersion().after(version)) {
            TestUtil.logTrace("version:" + a1.getVersion());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getVersion());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("timestampFieldTest failed");
    }

  }

  /*
   * @testName: timestampPropertyTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.7
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupTimestampData")
  public void timestampPropertyTest() throws Fault {
    boolean pass = false;
    try {
      Timestamp_Property a = getEntityManager().find(Timestamp_Property.class,
          "2");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getBasicTimestamp());
        // if (a.getVersion() == 1) {
        Timestamp version = a.getBasicTimestamp();
        a.setName("two");
        // Sleep for 1 second
        try {
          Thread.sleep(1 * 1000L);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Timestamp_Property a1 = getEntityManager()
            .find(Timestamp_Property.class, "2");
        if (a1 != null) {
          if (a1.getBasicTimestamp().after(version)) {
            TestUtil.logTrace("version:" + a1.getBasicTimestamp());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getBasicTimestamp());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("timestampPropertyTest failed");
    }

  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
    removeTestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  public void createIntTestData() {
    TestUtil.logTrace("createIntTestData");

    try {
      getEntityTransaction().begin();
      getEntityManager().persist(new Int_Field("1"));
      getEntityManager().persist(new Int_Property("2"));
      getEntityManager().persist(new Integer_Field("3", new Integer(0)));
      getEntityManager().persist(new Integer_Property("4", new Integer(0)));

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createIntTestData:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

  }

  public void createShortTestData() {
    TestUtil.logTrace("createShortTestData");

    try {
      getEntityTransaction().begin();
      getEntityManager().persist(new Short_Field("1"));
      getEntityManager().persist(new Short_Property("2"));
      getEntityManager()
          .persist(new ShortClass_Field("3", new Short((short) 0)));
      getEntityManager()
          .persist(new ShortClass_Property("4", new Short((short) 0)));
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createShortTestData:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

  }

  public void createLongTestData() {
    TestUtil.logTrace("createLongTestData");

    try {
      getEntityTransaction().begin();
      getEntityManager().persist(new Long_Field("1"));
      getEntityManager().persist(new Long_Property("2"));
      getEntityManager().persist(new LongClass_Field("3", new Long(0)));
      getEntityManager().persist(new LongClass_Property("4", new Long(0)));
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createLongTestData:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

  }

  public void createTimestampTestData() {
    TestUtil.logTrace("createTimestampTestData");

    try {
      getEntityTransaction().begin();
      Timestamp currentTime = new Timestamp(new Date().getTime());
      getEntityManager().persist(new Timestamp_Field("1", currentTime));
      getEntityManager().persist(new Timestamp_Property("2", currentTime));
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createTimestampTestData:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM A_BASIC")
          .executeUpdate();
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
      }
    }
  }
}
