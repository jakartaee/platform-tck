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
 * $Id: Client.java 63518 2011-09-16 11:36:26Z sdimilla $
 */

package com.sun.ts.tests.jpa.core.enums;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.Bindable.BindableType;
import javax.persistence.metamodel.PluralAttribute.CollectionType;
import javax.persistence.metamodel.Type.PersistenceType;
import javax.persistence.spi.LoadState;
import javax.persistence.spi.PersistenceUnitTransactionType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

public class Client extends PMClientBase {
  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {

      super.setup(args, p);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: accessTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:301
   * 
   * @test_Strategy:
   *
   */
  public void accessTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      AccessType at = AccessType.valueOf(AccessType.FIELD.name());
      if (!at.equals(AccessType.FIELD)) {
        TestUtil.logErr(
            "expected:" + AccessType.FIELD.name() + ", actual:" + at.name());
        pass = false;
      }
      at = AccessType.valueOf(AccessType.PROPERTY.name());
      if (!at.equals(AccessType.PROPERTY)) {
        TestUtil.logErr(
            "expected:" + AccessType.PROPERTY.name() + ", actual:" + at.name());
        pass = false;
      }
      try {
        AccessType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        AccessType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("accessTypeValueOfTest failed");
    }
  }

  /*
   * @testName: accessTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:302
   * 
   * @test_Strategy:
   *
   */
  public void accessTypeValuesTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin accessTypeValuesTest");
    try {

      Collection<AccessType> at = Arrays.asList(AccessType.values());
      if (at.size() != 2) {
        TestUtil.logErr("Number of AccessType expected:2, actual:" + at.size());
        pass = false;
      }

      if (at.contains(AccessType.FIELD)) {
        TestUtil.logTrace("received:" + AccessType.FIELD.name());
      } else {
        TestUtil.logErr("Expected value:" + AccessType.FIELD.name());
        pass = false;
      }
      if (at.contains(AccessType.PROPERTY)) {
        TestUtil.logTrace("received:" + AccessType.PROPERTY.name());
      } else {
        TestUtil.logErr("Expected value:" + AccessType.PROPERTY.name());
        pass = false;
      }

      for (AccessType a : at) {
        try {
          AccessType.valueOf(a.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("accessTypeValuesTest failed");
    }
  }

  /*
   * @testName: cacheRetrieveModeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:309
   * 
   * @test_Strategy:
   *
   */
  public void cacheRetrieveModeValueOfTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin cacheRetrieveModeValueOfTest");
    try {

      CacheRetrieveMode crm = CacheRetrieveMode
          .valueOf(CacheRetrieveMode.USE.name());
      if (!crm.equals(CacheRetrieveMode.USE)) {
        TestUtil.logErr("expected:" + CacheRetrieveMode.USE.name() + ", actual:"
            + crm.name());
        pass = false;
      }
      crm = CacheRetrieveMode.valueOf(CacheRetrieveMode.BYPASS.name());
      if (!crm.equals(CacheRetrieveMode.BYPASS)) {
        TestUtil.logErr("expected:" + CacheRetrieveMode.BYPASS.name()
            + ", actual:" + crm.name());
        pass = false;
      }
      try {
        CacheRetrieveMode.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        CacheRetrieveMode.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("cacheRetrieveModeValueOfTest failed");
    }
  }

  /*
   * @testName: cacheRetrieveModeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:310
   * 
   * @test_Strategy:
   *
   */
  public void cacheRetrieveModeValuesTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin cacheRetrieveModeValuesTest");
    try {

      Collection<CacheRetrieveMode> crm = Arrays
          .asList(CacheRetrieveMode.values());
      if (crm.size() != 2) {
        TestUtil.logErr(
            "Number of CacheRetrieveMode expected:2, actual:" + crm.size());
        pass = false;
      }

      if (crm.contains(CacheRetrieveMode.USE)) {
        TestUtil.logTrace("received:" + CacheRetrieveMode.USE);
      } else {
        TestUtil.logErr("Expected value:" + CacheRetrieveMode.USE);
        pass = false;
      }
      if (crm.contains(CacheRetrieveMode.BYPASS)) {
        TestUtil.logTrace("received:" + CacheRetrieveMode.BYPASS);
      } else {
        TestUtil.logErr("Expected value:" + CacheRetrieveMode.BYPASS);
        pass = false;
      }

      for (CacheRetrieveMode c : crm) {
        try {
          CacheRetrieveMode.valueOf(c.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("cacheRetrieveModeValuesTest failed");
    }
  }

  /*
   * @testName: cacheStoreModeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:311
   * 
   * @test_Strategy:
   *
   */
  public void cacheStoreModeValueOfTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin cacheStoreModeValueOfTest");
    try {

      CacheStoreMode csm = CacheStoreMode.valueOf(CacheStoreMode.USE.name());
      if (!csm.equals(CacheStoreMode.USE)) {
        TestUtil.logErr(
            "expected:" + CacheStoreMode.USE.name() + ", actual:" + csm.name());
        pass = false;
      }
      csm = CacheStoreMode.valueOf(CacheStoreMode.BYPASS.name());
      if (!csm.equals(CacheStoreMode.BYPASS)) {
        TestUtil.logErr("expected:" + CacheStoreMode.BYPASS.name() + ", actual:"
            + csm.name());
        pass = false;
      }
      csm = CacheStoreMode.valueOf(CacheStoreMode.REFRESH.name());
      if (!csm.equals(CacheStoreMode.REFRESH)) {
        TestUtil.logErr("expected:" + CacheStoreMode.REFRESH.name()
            + ", actual:" + csm.name());
        pass = false;
      }
      try {
        CacheStoreMode.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        CacheStoreMode.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("cacheStoreModeValueOfTest failed");
    }
  }

  /*
   * @testName: cacheStoreModeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:312
   * 
   * @test_Strategy:
   *
   */
  public void cacheStoreModeValuesTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin cacheStoreModeValuesTest");
    try {

      Collection<CacheStoreMode> csm = Arrays.asList(CacheStoreMode.values());
      if (csm.size() != 3) {
        TestUtil.logErr(
            "Number of CacheStoreMode expected:3, actual:" + csm.size());
        pass = false;
      }

      if (csm.contains(CacheStoreMode.USE)) {
        TestUtil.logTrace("received:" + CacheStoreMode.USE);
      } else {
        TestUtil.logErr("Expected value:" + CacheStoreMode.USE);
        pass = false;
      }
      if (csm.contains(CacheStoreMode.BYPASS)) {
        TestUtil.logTrace("received:" + CacheStoreMode.BYPASS);
      } else {
        TestUtil.logErr("Expected value:" + CacheStoreMode.BYPASS);
        pass = false;
      }
      if (csm.contains(CacheStoreMode.REFRESH)) {
        TestUtil.logTrace("received:" + CacheStoreMode.REFRESH);
      } else {
        TestUtil.logErr("Expected value:" + CacheStoreMode.REFRESH);
        pass = false;
      }

      for (CacheStoreMode c : csm) {
        try {
          CacheStoreMode.valueOf(c.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("cacheStoreModeValuesTest failed");
    }
  }

  /*
   * @testName: cascadeTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:9
   * 
   * @test_Strategy:
   *
   */
  public void cascadeTypeValueOfTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin cascadeTypeValueOfTest");
    try {

      CascadeType ct = CascadeType.valueOf(CascadeType.ALL.name());
      if (!ct.equals(CascadeType.ALL)) {
        TestUtil.logErr(
            "expected:" + CascadeType.ALL.name() + ", actual:" + ct.name());
        pass = false;
      }
      ct = CascadeType.valueOf(CascadeType.DETACH.name());
      if (!ct.equals(CascadeType.DETACH)) {
        TestUtil.logErr(
            "expected:" + CascadeType.DETACH.name() + ", actual:" + ct.name());
        pass = false;
      }
      ct = CascadeType.valueOf(CascadeType.MERGE.name());
      if (!ct.equals(CascadeType.MERGE)) {
        TestUtil.logErr(
            "expected:" + CascadeType.MERGE.name() + ", actual:" + ct.name());
        pass = false;
      }
      ct = CascadeType.valueOf(CascadeType.PERSIST.name());
      if (!ct.equals(CascadeType.PERSIST)) {
        TestUtil.logErr(
            "expected:" + CascadeType.PERSIST.name() + ", actual:" + ct.name());
        pass = false;
      }
      ct = CascadeType.valueOf(CascadeType.REFRESH.name());
      if (!ct.equals(CascadeType.REFRESH)) {
        TestUtil.logErr(
            "expected:" + CascadeType.REFRESH.name() + ", actual:" + ct.name());
        pass = false;
      }
      ct = CascadeType.valueOf(CascadeType.REMOVE.name());
      if (!ct.equals(CascadeType.REMOVE)) {
        TestUtil.logErr(
            "expected:" + CascadeType.REMOVE.name() + ", actual:" + ct.name());
        pass = false;
      }
      try {
        CascadeType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        CascadeType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("cascadeTypeValueOfTest failed");
    }
  }

  /*
   * @testName: cascadeTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:10
   * 
   * @test_Strategy:
   *
   */
  public void cascadeTypeValuesTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin cascadeTypeValuesTest");
    try {

      Collection<CascadeType> ct = Arrays.asList(CascadeType.values());
      if (ct.size() != 6) {
        TestUtil
            .logErr("Number of CascadeType expected:6, actual:" + ct.size());
        pass = false;
      }

      if (ct.contains(CascadeType.ALL)) {
        TestUtil.logTrace("received:" + CascadeType.ALL);
      } else {
        TestUtil.logErr("Expected value:" + CascadeType.ALL);
        pass = false;
      }
      if (ct.contains(CascadeType.DETACH)) {
        TestUtil.logTrace("received:" + CascadeType.DETACH);
      } else {
        TestUtil.logErr("Expected value:" + CascadeType.DETACH);
        pass = false;
      }
      if (ct.contains(CascadeType.MERGE)) {
        TestUtil.logTrace("received:" + CascadeType.MERGE);
      } else {
        TestUtil.logErr("Expected value:" + CascadeType.MERGE);
        pass = false;
      }
      if (ct.contains(CascadeType.PERSIST)) {
        TestUtil.logTrace("received:" + CascadeType.PERSIST);
      } else {
        TestUtil.logErr("Expected value:" + CascadeType.PERSIST);
        pass = false;
      }
      if (ct.contains(CascadeType.REFRESH)) {
        TestUtil.logTrace("received:" + CascadeType.REFRESH);
      } else {
        TestUtil.logErr("Expected value:" + CascadeType.REFRESH);
        pass = false;
      }
      if (ct.contains(CascadeType.REMOVE)) {
        TestUtil.logTrace("received:" + CascadeType.REMOVE);
      } else {
        TestUtil.logErr("Expected value:" + CascadeType.REMOVE);
        pass = false;
      }

      for (CascadeType c : ct) {
        try {
          CascadeType.valueOf(c.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("cascadeTypeValuesTest failed");
    }
  }

  /*
   * @testName: discriminatorTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:26
   * 
   * @test_Strategy:
   *
   */
  public void discriminatorTypeValueOfTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin discriminatorTypeValueOfTest");
    try {

      DiscriminatorType dt = DiscriminatorType
          .valueOf(DiscriminatorType.CHAR.name());
      if (!dt.equals(DiscriminatorType.CHAR)) {
        TestUtil.logErr("expected:" + DiscriminatorType.CHAR.name()
            + ", actual:" + dt.name());
        pass = false;
      }
      dt = DiscriminatorType.valueOf(DiscriminatorType.INTEGER.name());
      if (!dt.equals(DiscriminatorType.INTEGER)) {
        TestUtil.logErr("expected:" + DiscriminatorType.INTEGER.name()
            + ", actual:" + dt.name());
        pass = false;
      }
      dt = DiscriminatorType.valueOf(DiscriminatorType.STRING.name());
      if (!dt.equals(DiscriminatorType.STRING)) {
        TestUtil.logErr("expected:" + DiscriminatorType.STRING.name()
            + ", actual:" + dt.name());
        pass = false;
      }
      try {
        DiscriminatorType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        DiscriminatorType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("discriminatorTypeValueOfTest failed");
    }
  }

  /*
   * @testName: discriminatorTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:27
   * 
   * @test_Strategy:
   *
   */
  public void discriminatorTypeValuesTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin discriminatorTypeValuesTest");
    try {

      Collection<DiscriminatorType> dt = Arrays
          .asList(DiscriminatorType.values());
      if (dt.size() != 3) {
        TestUtil.logErr(
            "Number of DiscriminatorType expected:3, actual:" + dt.size());
        pass = false;
      }

      if (dt.contains(DiscriminatorType.CHAR)) {
        TestUtil.logTrace("received:" + DiscriminatorType.CHAR);
      } else {
        TestUtil.logErr("Expected value:" + DiscriminatorType.CHAR);
        pass = false;
      }
      if (dt.contains(DiscriminatorType.INTEGER)) {
        TestUtil.logTrace("received:" + DiscriminatorType.INTEGER);
      } else {
        TestUtil.logErr("Expected value:" + DiscriminatorType.INTEGER);
        pass = false;
      }
      if (dt.contains(DiscriminatorType.STRING)) {
        TestUtil.logTrace("received:" + DiscriminatorType.STRING);
      } else {
        TestUtil.logErr("Expected value:" + DiscriminatorType.STRING);
        pass = false;
      }

      for (DiscriminatorType d : dt) {
        try {
          DiscriminatorType.valueOf(d.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("discriminatorTypeValuesTest failed");
    }
  }

  /*
   * @testName: enumTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:74
   * 
   * @test_Strategy:
   *
   */
  public void enumTypeValueOfTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin enumTypeValueOfTest");
    try {

      EnumType et = EnumType.valueOf(EnumType.ORDINAL.name());
      if (!et.equals(EnumType.ORDINAL)) {
        TestUtil.logErr(
            "expected:" + EnumType.ORDINAL.name() + ", actual:" + et.name());
        pass = false;
      }
      et = EnumType.valueOf(EnumType.STRING.name());
      if (!et.equals(EnumType.STRING)) {
        TestUtil.logErr(
            "expected:" + EnumType.STRING.name() + ", actual:" + et.name());
        pass = false;
      }
      try {
        EnumType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        EnumType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("enumTypeValueOfTest failed");
    }
  }

  /*
   * @testName: enumTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:73
   * 
   * @test_Strategy:
   *
   */
  public void enumTypeValuesTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin enumTypeValuesTest");
    try {

      Collection<EnumType> et = Arrays.asList(EnumType.values());
      if (et.size() != 2) {
        TestUtil.logErr("Number of EnumType expected:2, actual:" + et.size());
        pass = false;
      }

      if (et.contains(EnumType.ORDINAL)) {
        TestUtil.logTrace("received:" + EnumType.ORDINAL);
      } else {
        TestUtil.logErr("Expected value:" + EnumType.ORDINAL);
        pass = false;
      }
      if (et.contains(EnumType.STRING)) {
        TestUtil.logTrace("received:" + EnumType.STRING);
      } else {
        TestUtil.logErr("Expected value:" + EnumType.STRING);
        pass = false;
      }

      for (EnumType e : et) {
        try {
          EnumType.valueOf(e.name());
        } catch (Exception ex) {
          TestUtil.logErr("Received exception for valueOf", ex);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("enumTypeValuesTest failed");
    }
  }

  /*
   * @testName: fetchTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:75
   * 
   * @test_Strategy:
   *
   */
  public void fetchTypeValueOfTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin fetchTypeValueOfTest");
    try {

      FetchType ft = FetchType.valueOf(FetchType.EAGER.name());
      if (!ft.equals(FetchType.EAGER)) {
        TestUtil.logErr(
            "expected:" + FetchType.EAGER.name() + ", actual:" + ft.name());
        pass = false;
      }
      ft = FetchType.valueOf(FetchType.LAZY.name());
      if (!ft.equals(FetchType.LAZY)) {
        TestUtil.logErr(
            "expected:" + FetchType.LAZY.name() + ", actual:" + ft.name());
        pass = false;
      }
      try {
        FetchType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        FetchType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("fetchTypeValueOfTest failed");
    }
  }

  /*
   * @testName: fetchTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:76
   * 
   * @test_Strategy:
   *
   */
  public void fetchTypeValuesTest() throws Fault {
    boolean pass = true;

    TestUtil.logTrace("Begin fetchTypeValuesTest");
    try {

      Collection<FetchType> ft = Arrays.asList(FetchType.values());
      if (ft.size() != 2) {
        TestUtil.logErr("Number of FetchType expected:2, actual:" + ft.size());
        pass = false;
      }

      if (ft.contains(FetchType.EAGER)) {
        TestUtil.logTrace("received:" + FetchType.EAGER);
      } else {
        TestUtil.logErr("Expected value:" + FetchType.EAGER);
        pass = false;
      }
      if (ft.contains(FetchType.LAZY)) {
        TestUtil.logTrace("received:" + FetchType.LAZY);
      } else {
        TestUtil.logErr("Expected value:" + FetchType.LAZY);
        pass = false;
      }

      for (FetchType f : ft) {
        try {
          FetchType.valueOf(f.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("fetchTypeValuesTest failed");
    }
  }
  /*
   * @testName: flushModeTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:79
   * 
   * @test_Strategy: Verify the various values of FlushModeType can verified set
   * using valueOf
   *
   */

  public void flushModeTypeValueOfTest() throws Fault {
    boolean pass = true;
    try {

      FlushModeType fmt = FlushModeType.valueOf(FlushModeType.AUTO.name());
      if (!fmt.equals(FlushModeType.AUTO)) {
        TestUtil.logErr(
            "expected:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
        pass = false;
      }
      fmt = FlushModeType.valueOf(FlushModeType.COMMIT.name());
      if (!fmt.equals(FlushModeType.COMMIT)) {
        TestUtil.logErr("expected:" + FlushModeType.COMMIT.name() + ", actual:"
            + fmt.name());
        pass = false;
      }
      try {
        FlushModeType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        FlushModeType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("flushModeTypeValueOfTest failed");
  }
  /*
   * @testName: flushModeTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:80
   * 
   * @test_Strategy: Verify the various values of FlushModeType using Values
   * then try valueOf for the returned values
   *
   */

  public void flushModeTypeValuesTest() throws Fault {
    boolean pass = true;
    int count = 0;
    try {
      Collection<FlushModeType> fmt = Arrays.asList(FlushModeType.values());
      if (fmt.size() != 2) {
        TestUtil
            .logErr("Number of FlushModeType expected:2, actual:" + fmt.size());
        pass = false;
      }

      if (fmt.contains(FlushModeType.COMMIT)) {
        TestUtil.logTrace("received:" + FlushModeType.COMMIT);
      } else {
        TestUtil.logErr("Expected value:" + FlushModeType.COMMIT);
        pass = false;
      }
      if (fmt.contains(FlushModeType.AUTO)) {
        TestUtil.logTrace("received:" + FlushModeType.AUTO);
      } else {
        TestUtil.logErr("Expected value:" + FlushModeType.AUTO);
        pass = false;
      }

      for (FlushModeType f : fmt) {
        try {
          FlushModeType.valueOf(f.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("flushModeTypeValuesTest failed");
  }

  /*
   * @testName: setgetFlushModeEntityManagerTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:173
   * 
   * @test_Strategy: Set and Get the various flushModes of the EntityManager
   */

  public void setgetFlushModeEntityManagerTest() throws Fault {
    boolean pass = true;
    try {
      EntityManager em = getEntityManager();
      TestUtil.logTrace("Checking Default mode");
      FlushModeType fmt = em.getFlushMode();
      if (fmt.equals(FlushModeType.AUTO)) {
        TestUtil.logTrace("Checking COMMIT");
        em.setFlushMode(FlushModeType.COMMIT);
        fmt = em.getFlushMode();
        if (fmt.equals(FlushModeType.COMMIT)) {
          TestUtil.logTrace("Checking AUTO");
          em.setFlushMode(FlushModeType.AUTO);
          fmt = em.getFlushMode();
          if (!fmt.equals(FlushModeType.AUTO)) {
            TestUtil.logErr("Expected a value of:" + FlushModeType.AUTO.name()
                + ", actual:" + fmt.name());
            pass = false;
          }
        } else {
          TestUtil.logErr("Expected a value of:" + FlushModeType.COMMIT.name()
              + ", actual:" + fmt.name());
          pass = false;
        }
      } else {
        TestUtil.logErr("Expected a default value of:"
            + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("setgetFlushModeEntityManagerTest failed");
  }
  /*
   * @testName: setgetFlushModeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:46; PERSISTENCE:JAVADOC:56
   * 
   * @test_Strategy: Set and Get the various flushModes of a Query
   */

  public void setgetFlushModeTest() throws Fault {
    boolean pass = true;
    try {
      EntityManager em = getEntityManager();
      Query q = em.createQuery("SELECT o FROM Order o WHERE o.id = 1");
      TestUtil.logTrace("Getting mode from query");
      FlushModeType fmt = q.getFlushMode();
      if (fmt.equals(em.getFlushMode())) {
        TestUtil.logTrace("Setting mode to return default mode");
        q.setFlushMode(fmt);
        TestUtil.logTrace("Setting mode to FlushModeType.COMMIT");
        q.setFlushMode(FlushModeType.COMMIT);
        fmt = q.getFlushMode();
        if (fmt.equals(FlushModeType.COMMIT)) {
          TestUtil.logTrace("Setting mode to FlushModeType.AUTO");
          q.setFlushMode(FlushModeType.AUTO);
          fmt = q.getFlushMode();
          if (fmt.equals(FlushModeType.AUTO)) {
            TestUtil.logTrace("Received expected FlushModeType:" + fmt.name());
          } else {
            TestUtil.logErr("Expected a value of:" + FlushModeType.AUTO.name()
                + ", actual:" + fmt.name());
            pass = false;
          }
        } else {
          TestUtil.logErr("Expected a default value of:"
              + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
          pass = false;
        }
      } else {
        TestUtil.logErr("Expected EntityManager value of:" + em.getFlushMode()
            + ", actual:" + fmt.name());
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("setgetFlushModeTest failed");
  }
  /*
   * @testName: setgetFlushModeTQTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:441; PERSISTENCE:JAVADOC:56
   * 
   * @test_Strategy: Set and Get the various flushModes of a TypedQuery
   */

  public void setgetFlushModeTQTest() throws Fault {
    boolean pass = true;
    try {
      EntityManager em = getEntityManager();
      TypedQuery<Order> q = em
          .createQuery("SELECT o FROM Order o WHERE o.id = 1", Order.class);

      FlushModeType fmt = q.getFlushMode();
      if (fmt.equals(em.getFlushMode())) {
        TestUtil.logTrace("Setting mode to returned default mode");
        q.setFlushMode(fmt);
        TestUtil.logTrace("Setting mode to FlushModeType.COMMIT");
        q.setFlushMode(FlushModeType.COMMIT);
        fmt = q.getFlushMode();
        if (fmt.equals(FlushModeType.COMMIT)) {
          TestUtil.logTrace("Setting mode to FlushModeType.AUTO");
          q.setFlushMode(FlushModeType.AUTO);
          fmt = q.getFlushMode();
          if (!fmt.equals(FlushModeType.AUTO)) {
            TestUtil.logErr("Expected a value of:" + FlushModeType.AUTO.name()
                + ", actual:" + fmt.name());
            pass = false;
          }
        } else {
          TestUtil.logErr("Expected a default value of:"
              + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
          pass = false;
        }
      } else {
        TestUtil.logErr("Expected EntityManager value of:"
            + em.getFlushMode().name() + ", actual:" + fmt.name());
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("setgetFlushModeTQTest failed");
  }

  /*
   * @testName: generationTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:83
   * 
   * @test_Strategy:
   *
   */
  public void generationTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      GenerationType ft = GenerationType.valueOf(GenerationType.AUTO.name());
      if (!ft.equals(GenerationType.AUTO)) {
        TestUtil.logErr(
            "expected:" + GenerationType.AUTO.name() + ", actual:" + ft.name());
        pass = false;
      }
      ft = GenerationType.valueOf(GenerationType.IDENTITY.name());
      if (!ft.equals(GenerationType.IDENTITY)) {
        TestUtil.logErr("expected:" + GenerationType.IDENTITY.name()
            + ", actual:" + ft.name());
        pass = false;
      }
      ft = GenerationType.valueOf(GenerationType.SEQUENCE.name());

      if (!ft.equals(GenerationType.SEQUENCE)) {
        TestUtil.logErr("expected:" + GenerationType.SEQUENCE.name()
            + ", actual:" + ft.name());
        pass = false;
      }
      ft = GenerationType.valueOf(GenerationType.TABLE.name());
      if (!ft.equals(GenerationType.TABLE)) {
        TestUtil.logErr("expected:" + GenerationType.TABLE.name() + ", actual:"
            + ft.name());
        pass = false;
      }
      try {
        GenerationType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        GenerationType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("generationTypeValueOfTest failed");
    }
  }

  /*
   * @testName: generationTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:84
   * 
   * @test_Strategy:
   *
   */
  public void generationTypeValuesTest() throws Fault {
    boolean pass = true;

    try {

      Collection<GenerationType> gt = Arrays.asList(GenerationType.values());
      if (gt.size() != 4) {
        TestUtil
            .logErr("Number of GenerationType expected:4, actual:" + gt.size());
        pass = false;
      }

      if (gt.contains(GenerationType.AUTO)) {
        TestUtil.logTrace("received:" + GenerationType.AUTO);
      } else {
        TestUtil.logErr("Expected value:" + GenerationType.AUTO);
        pass = false;
      }
      if (gt.contains(GenerationType.IDENTITY)) {
        TestUtil.logTrace("received:" + GenerationType.IDENTITY);
      } else {
        TestUtil.logErr("Expected value:" + GenerationType.IDENTITY);
        pass = false;
      }
      if (gt.contains(GenerationType.SEQUENCE)) {
        TestUtil.logTrace("received:" + GenerationType.SEQUENCE);
      } else {
        TestUtil.logErr("Expected value:" + GenerationType.SEQUENCE);
        pass = false;
      }
      if (gt.contains(GenerationType.TABLE)) {
        TestUtil.logTrace("received:" + GenerationType.TABLE);
      } else {
        TestUtil.logErr("Expected value:" + GenerationType.TABLE);
        pass = false;
      }

      for (GenerationType g : gt) {
        try {
          GenerationType.valueOf(g.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("generationTypeValuesTest failed");
    }
  }

  /*
   * @testName: inheritanceTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:87
   * 
   * @test_Strategy:
   *
   */
  public void inheritanceTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      InheritanceType ft = InheritanceType
          .valueOf(InheritanceType.JOINED.name());
      if (!ft.equals(InheritanceType.JOINED)) {
        TestUtil.logErr("expected:" + InheritanceType.JOINED.name()
            + ", actual:" + ft.name());
        pass = false;
      }
      ft = InheritanceType.valueOf(InheritanceType.SINGLE_TABLE.name());
      if (!ft.equals(InheritanceType.SINGLE_TABLE)) {
        TestUtil.logErr("expected:" + InheritanceType.SINGLE_TABLE.name()
            + ", actual:" + ft.name());
        pass = false;
      }
      ft = InheritanceType.valueOf(InheritanceType.TABLE_PER_CLASS.name());

      if (!ft.equals(InheritanceType.TABLE_PER_CLASS)) {
        TestUtil.logErr("expected:" + InheritanceType.TABLE_PER_CLASS.name()
            + ", actual:" + ft.name());
        pass = false;
      }
      try {
        InheritanceType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        InheritanceType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("inheritanceTypeValueOfTest failed");
    }
  }

  /*
   * @testName: inheritanceTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:88
   * 
   * @test_Strategy:
   *
   */
  public void inheritanceTypeValuesTest() throws Fault {
    boolean pass = true;

    try {

      Collection<InheritanceType> it = Arrays.asList(InheritanceType.values());
      if (it.size() != 3) {
        TestUtil.logErr(
            "Number of InheritanceType expected:3, actual:" + it.size());
        pass = false;
      }

      if (it.contains(InheritanceType.JOINED)) {
        TestUtil.logTrace("received:" + InheritanceType.JOINED);
      } else {
        TestUtil.logErr("Expected value:" + InheritanceType.JOINED);
        pass = false;
      }
      if (it.contains(InheritanceType.SINGLE_TABLE)) {
        TestUtil.logTrace("received:" + InheritanceType.SINGLE_TABLE);
      } else {
        TestUtil.logErr("Expected value:" + InheritanceType.SINGLE_TABLE);
        pass = false;
      }
      if (it.contains(InheritanceType.TABLE_PER_CLASS)) {
        TestUtil.logTrace("received:" + InheritanceType.TABLE_PER_CLASS);
      } else {
        TestUtil.logErr("Expected value:" + InheritanceType.TABLE_PER_CLASS);
        pass = false;
      }

      for (InheritanceType i : it) {
        try {
          InheritanceType.valueOf(i.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("inheritanceTypeValuesTest failed");
    }
  }

  /*
   * @testName: lockModeTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:104
   * 
   * @test_Strategy:
   *
   */
  public void lockModeTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      LockModeType lm = LockModeType.valueOf(LockModeType.NONE.name());
      if (!lm.equals(LockModeType.NONE)) {
        TestUtil
            .logErr("expected:" + LockModeType.NONE + ", actual:" + lm.name());
        pass = false;
      }
      lm = LockModeType.valueOf(LockModeType.OPTIMISTIC.name());
      if (!lm.equals(LockModeType.OPTIMISTIC)) {
        TestUtil.logErr("expected:" + LockModeType.OPTIMISTIC.name()
            + ", actual:" + lm.name());
        pass = false;
      }
      lm = LockModeType.valueOf(LockModeType.OPTIMISTIC_FORCE_INCREMENT.name());
      if (!lm.equals(LockModeType.OPTIMISTIC_FORCE_INCREMENT)) {
        TestUtil
            .logErr("expected:" + LockModeType.OPTIMISTIC_FORCE_INCREMENT.name()
                + ", actual:" + lm.name());
        pass = false;
      }
      lm = LockModeType.valueOf(LockModeType.PESSIMISTIC_READ.name());
      if (!lm.equals(LockModeType.PESSIMISTIC_READ)) {
        TestUtil.logErr("expected:" + LockModeType.PESSIMISTIC_READ.name()
            + ", actual:" + lm.name());
        pass = false;
      }
      lm = LockModeType.valueOf(LockModeType.PESSIMISTIC_WRITE.name());
      if (!lm.equals(LockModeType.PESSIMISTIC_WRITE)) {
        TestUtil.logErr("expected:" + LockModeType.PESSIMISTIC_WRITE.name()
            + ", actual:" + lm.name());
        pass = false;
      }
      lm = LockModeType.valueOf(LockModeType.READ.name());
      if (!lm.equals(LockModeType.READ)) {
        TestUtil.logErr(
            "expected:" + LockModeType.READ.name() + ", actual:" + lm.name());
        pass = false;
      }
      lm = LockModeType.valueOf(LockModeType.WRITE.name());
      if (!lm.equals(LockModeType.WRITE)) {
        TestUtil.logErr(
            "expected:" + LockModeType.WRITE.name() + ", actual:" + lm.name());
        pass = false;
      }

      try {
        LockModeType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        LockModeType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("lockModeTypeValueOfTest failed");
    }
  }

  /*
   * @testName: lockModeTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:105
   * 
   * @test_Strategy: Test each LockModeType value
   *
   */
  public void lockModeTypeValuesTest() throws Fault {
    boolean pass = true;

    try {

      Collection<LockModeType> lm = Arrays.asList(LockModeType.values());
      if (lm.size() != 8) {
        TestUtil
            .logErr("Number of LockModeTypes expected:8, actual:" + lm.size());
        pass = false;
      }

      if (lm.contains(LockModeType.NONE)) {
        TestUtil.logTrace("received:" + LockModeType.NONE);
      } else {
        TestUtil.logErr("Expected value:" + LockModeType.NONE);
        pass = false;
      }
      if (lm.contains(LockModeType.OPTIMISTIC)) {
        TestUtil.logTrace("received:" + LockModeType.OPTIMISTIC);
      } else {
        TestUtil.logErr("Expected value:" + LockModeType.OPTIMISTIC);
        pass = false;
      }
      if (lm.contains(LockModeType.OPTIMISTIC_FORCE_INCREMENT)) {
        TestUtil
            .logTrace("received:" + LockModeType.OPTIMISTIC_FORCE_INCREMENT);
      } else {
        TestUtil.logErr(
            "Expected value:" + LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        pass = false;
      }
      if (lm.contains(LockModeType.PESSIMISTIC_FORCE_INCREMENT)) {
        TestUtil
            .logTrace("received:" + LockModeType.PESSIMISTIC_FORCE_INCREMENT);
      } else {
        TestUtil.logErr(
            "Expected value:" + LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        pass = false;
      }
      if (lm.contains(LockModeType.PESSIMISTIC_READ)) {
        TestUtil.logTrace("received:" + LockModeType.PESSIMISTIC_READ);
      } else {
        TestUtil.logErr("Expected value:" + LockModeType.PESSIMISTIC_READ);
        pass = false;
      }
      if (lm.contains(LockModeType.PESSIMISTIC_WRITE)) {
        TestUtil.logTrace("received:" + LockModeType.PESSIMISTIC_WRITE);
      } else {
        TestUtil.logErr("Expected value:" + LockModeType.PESSIMISTIC_WRITE);
        pass = false;
      }
      if (lm.contains(LockModeType.READ)) {
        TestUtil.logTrace("received:" + LockModeType.READ);
      } else {
        TestUtil.logErr("Expected value:" + LockModeType.READ);
        pass = false;
      }
      if (lm.contains(LockModeType.WRITE)) {
        TestUtil.logTrace("received:" + LockModeType.WRITE);
      } else {
        TestUtil.logErr("Expected value:" + LockModeType.WRITE);
        pass = false;
      }

      for (LockModeType l : lm) {
        try {
          LockModeType.valueOf(l.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("lockModeTypeValuesTest failed");
    }
  }

  /*
   * @testName: persistenceContextTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:154
   * 
   * @test_Strategy:
   *
   */
  public void persistenceContextTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      PersistenceContextType pct = PersistenceContextType
          .valueOf(PersistenceContextType.EXTENDED.name());
      if (!pct.equals(PersistenceContextType.EXTENDED)) {
        TestUtil.logErr("expected:" + PersistenceContextType.EXTENDED.name()
            + ", actual:" + pct.name());
        pass = false;
      }
      pct = PersistenceContextType
          .valueOf(PersistenceContextType.TRANSACTION.name());
      if (!pct.equals(PersistenceContextType.TRANSACTION)) {
        TestUtil.logErr("expected:" + PersistenceContextType.TRANSACTION.name()
            + ", actual:" + pct.name());
        pass = false;
      }
      try {
        PersistenceContextType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        PersistenceContextType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("persistenceContextTypeValueOfTest failed");
    }
  }

  /*
   * @testName: persistenceContextTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:155
   * 
   * @test_Strategy:
   *
   */
  public void persistenceContextTypeValuesTest() throws Fault {
    boolean pass = true;

    try {

      Collection<PersistenceContextType> pct = Arrays
          .asList(PersistenceContextType.values());
      if (pct.size() != 2) {
        TestUtil.logErr("Number of PersistenceContextType expected:2, actual:"
            + pct.size());
        pass = false;
      }

      if (pct.contains(PersistenceContextType.EXTENDED)) {
        TestUtil.logTrace("received:" + PersistenceContextType.EXTENDED);
      } else {
        TestUtil.logErr("Expected value:" + PersistenceContextType.EXTENDED);
        pass = false;
      }
      if (pct.contains(PersistenceContextType.TRANSACTION)) {
        TestUtil.logTrace("received:" + PersistenceContextType.TRANSACTION);
      } else {
        TestUtil.logErr("Expected value:" + PersistenceContextType.TRANSACTION);
        pass = false;
      }

      for (PersistenceContextType p : pct) {
        try {
          PersistenceContextType.valueOf(p.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("persistenceContextTypeValuesTest failed");
    }
  }

  /*
   * @testName: pessimisticLockScopeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:397
   * 
   * @test_Strategy:
   *
   */
  public void pessimisticLockScopeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      PessimisticLockScope pls = PessimisticLockScope
          .valueOf(PessimisticLockScope.EXTENDED.name());
      if (!pls.equals(PessimisticLockScope.EXTENDED)) {
        TestUtil.logErr("expected:" + PessimisticLockScope.EXTENDED.name()
            + ", actual:" + pls.name());
        pass = false;
      }
      pls = PessimisticLockScope.valueOf(PessimisticLockScope.NORMAL.name());
      if (!pls.equals(PessimisticLockScope.NORMAL)) {
        TestUtil.logErr("expected:" + PessimisticLockScope.NORMAL.name()
            + ", actual:" + pls.name());
        pass = false;
      }
      try {
        PessimisticLockScope.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        PessimisticLockScope.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("pessimisticLockScopeValueOfTest failed");
    }
  }

  /*
   * @testName: pessimisticLockScopeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:398
   * 
   * @test_Strategy:
   *
   */
  public void pessimisticLockScopeValuesTest() throws Fault {
    boolean pass = true;

    try {

      Collection<PessimisticLockScope> pls = Arrays
          .asList(PessimisticLockScope.values());
      if (pls.size() != 2) {
        TestUtil.logErr(
            "Number of PessimisticLockScope expected:2, actual:" + pls.size());
        pass = false;
      }

      if (pls.contains(PessimisticLockScope.EXTENDED)) {
        TestUtil.logTrace("received:" + PessimisticLockScope.EXTENDED);
      } else {
        TestUtil.logErr("Expected value:" + PessimisticLockScope.EXTENDED);
        pass = false;
      }
      if (pls.contains(PessimisticLockScope.NORMAL)) {
        TestUtil.logTrace("received:" + PessimisticLockScope.NORMAL);
      } else {
        TestUtil.logErr("Expected value:" + PessimisticLockScope.NORMAL);
        pass = false;
      }

      for (PessimisticLockScope p : pls) {
        try {
          PessimisticLockScope.valueOf(p.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("pessimisticLockScopeValuesTest failed");
    }
  }

  /*
   * @testName: sharedCacheModeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:427; PERSISTENCE:SPEC:1910;
   * 
   * @test_Strategy:
   *
   */
  public void sharedCacheModeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      SharedCacheMode scm = SharedCacheMode.valueOf(SharedCacheMode.ALL.name());
      if (!scm.equals(SharedCacheMode.ALL)) {
        TestUtil.logErr("expected:" + SharedCacheMode.ALL.name() + ", actual:"
            + scm.name());
        pass = false;
      }
      scm = SharedCacheMode.valueOf(SharedCacheMode.DISABLE_SELECTIVE.name());
      if (!scm.equals(SharedCacheMode.DISABLE_SELECTIVE)) {
        TestUtil.logErr("expected:" + SharedCacheMode.DISABLE_SELECTIVE.name()
            + ", actual:" + scm.name());
        pass = false;
      }
      scm = SharedCacheMode.valueOf(SharedCacheMode.ENABLE_SELECTIVE.name());
      if (!scm.equals(SharedCacheMode.ENABLE_SELECTIVE)) {
        TestUtil.logErr("expected:" + SharedCacheMode.ENABLE_SELECTIVE.name()
            + ", actual:" + scm.name());
        pass = false;
      }
      scm = SharedCacheMode.valueOf(SharedCacheMode.NONE.name());
      if (!scm.equals(SharedCacheMode.NONE)) {
        TestUtil.logErr("expected:" + SharedCacheMode.NONE.name() + ", actual:"
            + scm.name());
        pass = false;
      }
      scm = SharedCacheMode.valueOf(SharedCacheMode.UNSPECIFIED.name());
      if (!scm.equals(SharedCacheMode.UNSPECIFIED)) {
        TestUtil.logErr("expected:" + SharedCacheMode.UNSPECIFIED.name()
            + ", actual:" + scm.name());
        pass = false;
      }
      try {
        SharedCacheMode.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        SharedCacheMode.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("sharedCacheModeValueOfTest failed");
    }
  }

  /*
   * @testName: sharedCacheModeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:428; PERSISTENCE:SPEC:1910;
   * 
   * @test_Strategy:
   *
   */
  public void sharedCacheModeValuesTest() throws Fault {
    boolean pass = true;

    try {

      Collection<SharedCacheMode> scm = Arrays.asList(SharedCacheMode.values());
      if (scm.size() != 5) {
        TestUtil.logErr(
            "Number of SharedCacheMode expected:5, actual:" + scm.size());
        pass = false;
      }

      if (scm.contains(SharedCacheMode.ALL)) {
        TestUtil.logTrace("received:" + SharedCacheMode.ALL);
      } else {
        TestUtil.logErr("Expected value:" + SharedCacheMode.ALL);
        pass = false;
      }
      if (scm.contains(SharedCacheMode.DISABLE_SELECTIVE)) {
        TestUtil.logTrace("received:" + SharedCacheMode.DISABLE_SELECTIVE);
      } else {
        TestUtil.logErr("Expected value:" + SharedCacheMode.DISABLE_SELECTIVE);
        pass = false;
      }
      if (scm.contains(SharedCacheMode.ENABLE_SELECTIVE)) {
        TestUtil.logTrace("received:" + SharedCacheMode.ENABLE_SELECTIVE);
      } else {
        TestUtil.logErr("Expected value:" + SharedCacheMode.ENABLE_SELECTIVE);
        pass = false;
      }
      if (scm.contains(SharedCacheMode.NONE)) {
        TestUtil.logTrace("received:" + SharedCacheMode.NONE);
      } else {
        TestUtil.logErr("Expected value:" + SharedCacheMode.NONE);
        pass = false;
      }
      if (scm.contains(SharedCacheMode.UNSPECIFIED)) {
        TestUtil.logTrace("received:" + SharedCacheMode.UNSPECIFIED);
      } else {
        TestUtil.logErr("Expected value:" + SharedCacheMode.UNSPECIFIED);
        pass = false;
      }

      for (SharedCacheMode s : scm) {
        try {
          SharedCacheMode.valueOf(s.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("sharedCacheModeValuesTest failed");
    }
  }

  /*
   * @testName: validationModeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:455
   * 
   * @test_Strategy:
   *
   */
  public void validationModeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      ValidationMode vm = ValidationMode
          .valueOf(ValidationMode.CALLBACK.name());
      if (!vm.equals(ValidationMode.CALLBACK)) {
        TestUtil.logErr("expected:" + ValidationMode.CALLBACK.name()
            + ", actual:" + vm.name());
        pass = false;
      }
      vm = ValidationMode.valueOf(ValidationMode.AUTO.name());
      if (!vm.equals(ValidationMode.AUTO)) {
        TestUtil.logErr(
            "expected:" + ValidationMode.AUTO.name() + ", actual:" + vm.name());
        pass = false;
      }
      vm = ValidationMode.valueOf(ValidationMode.NONE.name());
      if (!vm.equals(ValidationMode.NONE)) {
        TestUtil.logErr(
            "expected:" + ValidationMode.NONE.name() + ", actual:" + vm.name());
        pass = false;
      }
      try {
        ValidationMode.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        ValidationMode.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("validationModeValueOfTest failed");
    }
  }

  /*
   * @testName: validationModeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:456
   * 
   * @test_Strategy:
   *
   */
  public void validationModeValuesTest() throws Fault {
    boolean pass = true;

    try {

      Collection<ValidationMode> vm = Arrays.asList(ValidationMode.values());
      if (vm.size() != 3) {
        TestUtil
            .logErr("Number of ValidationMode expected:3, actual:" + vm.size());
        pass = false;
      }

      if (vm.contains(ValidationMode.CALLBACK)) {
        TestUtil.logTrace("received:" + ValidationMode.CALLBACK);
      } else {
        TestUtil.logErr("Expected value:" + ValidationMode.CALLBACK);
        pass = false;
      }
      if (vm.contains(ValidationMode.AUTO)) {
        TestUtil.logTrace("received:" + ValidationMode.AUTO);
      } else {
        TestUtil.logErr("Expected value:" + ValidationMode.AUTO);
        pass = false;
      }
      if (vm.contains(ValidationMode.NONE)) {
        TestUtil.logTrace("received:" + ValidationMode.NONE);
      } else {
        TestUtil.logErr("Expected value:" + ValidationMode.NONE);
        pass = false;
      }

      for (ValidationMode v : vm) {
        try {
          ValidationMode.valueOf(v.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("validationModeValuesTest failed");
    }
  }

  /*
   * @testName: temporalTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:218
   * 
   * @test_Strategy:
   *
   */
  public void temporalTypeValuesTest() throws Fault {
    boolean pass = true;

    Collection<TemporalType> tt = Arrays.asList(TemporalType.values());
    if (tt.size() != 3) {
      TestUtil.logErr("Number of TemporalType expected:3, actual:" + tt.size());
      pass = false;
    }

    if (tt.contains(TemporalType.DATE)) {
      TestUtil.logTrace("received:" + TemporalType.DATE);
    } else {
      TestUtil.logErr("Expected value:" + TemporalType.DATE);
      pass = false;
    }
    if (tt.contains(TemporalType.TIME)) {
      TestUtil.logTrace("received:" + TemporalType.TIME);
    } else {
      TestUtil.logErr("Expected value:" + TemporalType.TIME);
      pass = false;
    }
    if (tt.contains(TemporalType.TIMESTAMP)) {
      TestUtil.logTrace("received:" + TemporalType.TIMESTAMP);
    } else {
      TestUtil.logErr("Expected value:" + TemporalType.TIMESTAMP);
      pass = false;
    }

    for (TemporalType t : tt) {
      try {
        TemporalType.valueOf(t.name());
      } catch (Exception e) {
        TestUtil.logErr("Received exception for valueOf", e);
        pass = false;
      }
    }

    if (!pass) {
      throw new Fault("temporalTypeValuesTest failed");
    }

  }

  /*
   * @testName: temporalTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:217
   * 
   * @test_Strategy:
   *
   */
  public void temporalTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      TemporalType tt = TemporalType.valueOf(TemporalType.DATE.name());
      if (!tt.equals(TemporalType.DATE)) {
        TestUtil.logErr(
            "expected:" + TemporalType.DATE.name() + ", actual:" + tt.name());
        pass = false;
      }
      tt = TemporalType.valueOf(TemporalType.TIME.name());
      if (!tt.equals(TemporalType.TIME)) {
        TestUtil.logErr(
            "expected:" + TemporalType.TIME.name() + ", actual:" + tt.name());
        pass = false;
      }
      tt = TemporalType.valueOf(TemporalType.TIMESTAMP.name());
      if (!tt.equals(TemporalType.TIMESTAMP)) {
        TestUtil.logErr("expected:" + TemporalType.TIMESTAMP.name()
            + ", actual:" + tt.name());
        pass = false;
      }

      try {
        TemporalType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        TemporalType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("temporalTypeValueOfTest failed");
    }
  }

  /*
   * @testName: joinTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1073
   * 
   * @test_Strategy:
   *
   */
  public void joinTypeValuesTest() throws Fault {
    boolean pass = true;

    Collection<JoinType> jt = Arrays.asList(JoinType.values());
    if (jt.size() != 3) {
      TestUtil.logErr("Number of TemporalType expected:3, actual:" + jt.size());
      pass = false;
    }

    if (jt.contains(JoinType.LEFT)) {
      TestUtil.logTrace("received:" + JoinType.LEFT);
    } else {
      TestUtil.logErr("Expected value:" + JoinType.LEFT);
      pass = false;
    }
    if (jt.contains(JoinType.INNER)) {
      TestUtil.logTrace("received:" + JoinType.INNER);
    } else {
      TestUtil.logErr("Expected value:" + JoinType.INNER);
      pass = false;
    }
    if (jt.contains(JoinType.RIGHT)) {
      TestUtil.logTrace("received:" + JoinType.RIGHT);
    } else {
      TestUtil.logErr("Expected value:" + JoinType.RIGHT);
      pass = false;
    }

    for (JoinType j : jt) {
      try {
        JoinType.valueOf(j.name());
      } catch (Exception e) {
        TestUtil.logErr("Received exception for valueOf", e);
        pass = false;
      }
    }

    if (!pass) {
      throw new Fault("joinTypeValuesTest failed");
    }
  }

  /*
   * @testName: joinTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1072
   * 
   * @test_Strategy:
   *
   */
  public void joinTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      JoinType jt = JoinType.valueOf(JoinType.LEFT.name());
      if (!jt.equals(JoinType.LEFT)) {
        TestUtil.logErr(
            "expected:" + JoinType.LEFT.name() + ", actual:" + jt.name());
        pass = false;
      }
      jt = JoinType.valueOf(JoinType.INNER.name());
      if (!jt.equals(JoinType.INNER)) {
        TestUtil.logErr(
            "expected:" + JoinType.INNER.name() + ", actual:" + jt.name());
        pass = false;
      }
      jt = JoinType.valueOf(JoinType.RIGHT.name());
      if (!jt.equals(JoinType.RIGHT)) {
        TestUtil.logErr(
            "expected:" + JoinType.RIGHT.name() + ", actual:" + jt.name());
        pass = false;
      }

      try {
        JoinType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        JoinType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("joinTypeValueOfTest failed");
    }
  }

  /*
   * @testName: persistentAttributeTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1221
   * 
   * @test_Strategy:
   *
   */
  public void persistentAttributeTypeValuesTest() throws Fault {
    boolean pass = true;

    Collection<PersistentAttributeType> pat = Arrays
        .asList(PersistentAttributeType.values());

    if (pat.size() != 7) {
      TestUtil
          .logErr("Number of TemporalType expected:7, actual:" + pat.size());
      pass = false;
    }

    if (pat.contains(PersistentAttributeType.BASIC)) {
      TestUtil.logTrace("received:" + PersistentAttributeType.BASIC);
    } else {
      TestUtil.logErr("Expected value:" + PersistentAttributeType.BASIC);
      pass = false;
    }
    if (pat.contains(PersistentAttributeType.ELEMENT_COLLECTION)) {
      TestUtil
          .logTrace("received:" + PersistentAttributeType.ELEMENT_COLLECTION);
    } else {
      TestUtil.logErr(
          "Expected value:" + PersistentAttributeType.ELEMENT_COLLECTION);
      pass = false;
    }
    if (pat.contains(PersistentAttributeType.EMBEDDED)) {
      TestUtil.logTrace("received:" + PersistentAttributeType.EMBEDDED);
    } else {
      TestUtil.logErr("Expected value:" + PersistentAttributeType.EMBEDDED);
      pass = false;
    }
    if (pat.contains(PersistentAttributeType.MANY_TO_MANY)) {
      TestUtil.logTrace("received:" + PersistentAttributeType.MANY_TO_MANY);
    } else {
      TestUtil.logErr("Expected value:" + PersistentAttributeType.MANY_TO_MANY);
      pass = false;
    }
    if (pat.contains(PersistentAttributeType.MANY_TO_ONE)) {
      TestUtil.logTrace("received:" + PersistentAttributeType.MANY_TO_ONE);
    } else {
      TestUtil.logErr("Expected value:" + PersistentAttributeType.MANY_TO_ONE);
      pass = false;
    }
    if (pat.contains(PersistentAttributeType.ONE_TO_MANY)) {
      TestUtil.logTrace("received:" + PersistentAttributeType.ONE_TO_MANY);
    } else {
      TestUtil.logErr("Expected value:" + PersistentAttributeType.ONE_TO_MANY);
      pass = false;
    }
    if (pat.contains(PersistentAttributeType.ONE_TO_ONE)) {
      TestUtil.logTrace("received:" + PersistentAttributeType.ONE_TO_ONE);
    } else {
      TestUtil.logErr("Expected value:" + PersistentAttributeType.ONE_TO_ONE);
      pass = false;
    }

    for (PersistentAttributeType p : pat) {
      try {
        PersistentAttributeType.valueOf(p.name());
      } catch (Exception e) {
        TestUtil.logErr("Received exception for valueOf", e);
        pass = false;
      }
    }

    if (!pass) {
      throw new Fault("persistentAttributeTypeValuesTest failed");
    }
  }

  /*
   * @testName: persistentAttributeTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1220
   * 
   * @test_Strategy:
   *
   */
  public void persistentAttributeTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      PersistentAttributeType pat = PersistentAttributeType
          .valueOf(PersistentAttributeType.BASIC.name());
      if (!pat.equals(PersistentAttributeType.BASIC)) {
        TestUtil.logErr("expected:" + PersistentAttributeType.BASIC.name()
            + ", actual:" + pat.name());
        pass = false;
      }
      pat = PersistentAttributeType
          .valueOf(PersistentAttributeType.ELEMENT_COLLECTION.name());
      if (!pat.equals(PersistentAttributeType.ELEMENT_COLLECTION)) {
        TestUtil.logErr(
            "expected:" + PersistentAttributeType.ELEMENT_COLLECTION.name()
                + ", actual:" + pat.name());
        pass = false;
      }
      pat = PersistentAttributeType
          .valueOf(PersistentAttributeType.EMBEDDED.name());
      if (!pat.equals(PersistentAttributeType.EMBEDDED)) {
        TestUtil.logErr("expected:" + PersistentAttributeType.EMBEDDED.name()
            + ", actual:" + pat.name());
        pass = false;
      }
      pat = PersistentAttributeType
          .valueOf(PersistentAttributeType.MANY_TO_MANY.name());
      if (!pat.equals(PersistentAttributeType.MANY_TO_MANY)) {
        TestUtil
            .logErr("expected:" + PersistentAttributeType.MANY_TO_MANY.name()
                + ", actual:" + pat.name());
        pass = false;
      }
      pat = PersistentAttributeType
          .valueOf(PersistentAttributeType.MANY_TO_ONE.name());
      if (!pat.equals(PersistentAttributeType.MANY_TO_ONE)) {
        TestUtil.logErr("expected:" + PersistentAttributeType.MANY_TO_ONE.name()
            + ", actual:" + pat.name());
        pass = false;
      }
      pat = PersistentAttributeType
          .valueOf(PersistentAttributeType.ONE_TO_MANY.name());
      if (!pat.equals(PersistentAttributeType.ONE_TO_MANY)) {
        TestUtil.logErr("expected:" + PersistentAttributeType.ONE_TO_MANY.name()
            + ", actual:" + pat.name());
        pass = false;
      }
      pat = PersistentAttributeType
          .valueOf(PersistentAttributeType.ONE_TO_ONE.name());
      if (!pat.equals(PersistentAttributeType.ONE_TO_ONE)) {
        TestUtil.logErr("expected:" + PersistentAttributeType.ONE_TO_ONE.name()
            + ", actual:" + pat.name());
        pass = false;
      }
      try {
        PersistentAttributeType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        PersistentAttributeType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("persistentAttributeTypeValueOfTest failed");
    }
  }

  /*
   * @testName: bindableTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1227
   * 
   * @test_Strategy:
   *
   */
  public void bindableTypeValuesTest() throws Fault {
    boolean pass = true;

    Collection<BindableType> bt = Arrays.asList(BindableType.values());

    if (bt.size() != 3) {
      TestUtil.logErr("Number of TemporalType expected:3, actual:" + bt.size());
      pass = false;
    }

    if (bt.contains(BindableType.ENTITY_TYPE)) {
      TestUtil.logTrace("received:" + BindableType.ENTITY_TYPE.name());
    } else {
      TestUtil.logErr("Expected value:" + BindableType.ENTITY_TYPE.name());
      pass = false;
    }
    if (bt.contains(BindableType.PLURAL_ATTRIBUTE)) {
      TestUtil.logTrace("received:" + BindableType.PLURAL_ATTRIBUTE.name());
    } else {
      TestUtil.logErr("Expected value:" + BindableType.PLURAL_ATTRIBUTE.name());
      pass = false;
    }
    if (bt.contains(BindableType.SINGULAR_ATTRIBUTE)) {
      TestUtil.logTrace("received:" + BindableType.SINGULAR_ATTRIBUTE.name());
    } else {
      TestUtil
          .logErr("Expected value:" + BindableType.SINGULAR_ATTRIBUTE.name());
      pass = false;
    }

    for (BindableType b : bt) {
      try {
        BindableType.valueOf(b.name());
      } catch (Exception e) {
        TestUtil.logErr("Received exception for valueOf", e);
        pass = false;
      }
    }

    if (!pass) {
      throw new Fault("bindableTypeValuesTest failed");
    }
  }

  /*
   * @testName: bindableTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1226
   * 
   * @test_Strategy:
   *
   */
  public void bindableTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      BindableType bt = BindableType.valueOf(BindableType.ENTITY_TYPE.name());
      if (!bt.equals(BindableType.ENTITY_TYPE)) {
        TestUtil.logErr("expected:" + BindableType.ENTITY_TYPE.name()
            + ", actual:" + bt.name());
        pass = false;
      }
      bt = BindableType.valueOf(BindableType.PLURAL_ATTRIBUTE.name());
      if (!bt.equals(BindableType.PLURAL_ATTRIBUTE)) {
        TestUtil.logErr("expected:" + BindableType.PLURAL_ATTRIBUTE.name()
            + ", actual:" + bt.name());
        pass = false;
      }
      bt = BindableType.valueOf(BindableType.SINGULAR_ATTRIBUTE.name());
      if (!bt.equals(BindableType.SINGULAR_ATTRIBUTE)) {
        TestUtil.logErr("expected:" + BindableType.SINGULAR_ATTRIBUTE.name()
            + ", actual:" + bt.name());
        pass = false;
      }
      try {
        BindableType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        BindableType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("bindableTypeValueOfTest failed");
    }
  }

  /*
   * @testName: collectionTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1454
   * 
   * @test_Strategy:
   *
   */
  public void collectionTypeValuesTest() throws Fault {
    boolean pass = true;

    Collection<CollectionType> ct = Arrays.asList(CollectionType.values());

    if (ct.size() != 4) {
      TestUtil.logErr("Number of TemporalType expected:4, actual:" + ct.size());
      pass = false;
    }

    if (ct.contains(CollectionType.COLLECTION)) {
      TestUtil.logTrace("received:" + CollectionType.COLLECTION);
    } else {
      TestUtil.logErr("Expected value:" + CollectionType.COLLECTION);
      pass = false;
    }
    if (ct.contains(CollectionType.LIST)) {
      TestUtil.logTrace("received:" + CollectionType.LIST);
    } else {
      TestUtil.logErr("Expected value:" + CollectionType.LIST);
      pass = false;
    }
    if (ct.contains(CollectionType.MAP)) {
      TestUtil.logTrace("received:" + CollectionType.MAP);
    } else {
      TestUtil.logErr("Expected value:" + CollectionType.MAP);
      pass = false;
    }
    if (ct.contains(CollectionType.SET)) {
      TestUtil.logTrace("received:" + CollectionType.SET);
    } else {
      TestUtil.logErr("Expected value:" + CollectionType.SET);
      pass = false;
    }

    for (CollectionType c : ct) {
      try {
        CollectionType.valueOf(c.name());
      } catch (Exception e) {
        TestUtil.logErr("Received exception for valueOf", e);
        pass = false;
      }
    }

    if (!pass) {
      throw new Fault("collectionTypeValuesTest failed");
    }
  }

  /*
   * @testName: collectionTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1453
   * 
   * @test_Strategy:
   *
   */
  public void collectionTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      CollectionType ct = CollectionType
          .valueOf(CollectionType.COLLECTION.name());
      if (!ct.equals(CollectionType.COLLECTION)) {
        TestUtil.logErr("expected:" + CollectionType.COLLECTION.name()
            + ", actual:" + ct.name());
        pass = false;
      }
      ct = CollectionType.valueOf(CollectionType.LIST.name());
      if (!ct.equals(CollectionType.LIST)) {
        TestUtil.logErr(
            "expected:" + CollectionType.LIST.name() + ", actual:" + ct.name());
        pass = false;
      }
      ct = CollectionType.valueOf(CollectionType.MAP.name());
      if (!ct.equals(CollectionType.MAP)) {
        TestUtil.logErr(
            "expected:" + CollectionType.MAP.name() + ", actual:" + ct.name());
        pass = false;
      }
      ct = CollectionType.valueOf(CollectionType.SET.name());
      if (!ct.equals(CollectionType.SET)) {
        TestUtil.logErr(
            "expected:" + CollectionType.SET.name() + ", actual:" + ct.name());
        pass = false;
      }
      try {
        CollectionType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        CollectionType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("collectionTypeValueOfTest failed");
    }
  }

  /*
   * @testName: persistenceTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1474
   * 
   * @test_Strategy:
   *
   */
  public void persistenceTypeValuesTest() throws Fault {
    boolean pass = true;

    Collection<PersistenceType> pt = Arrays.asList(PersistenceType.values());

    if (pt.size() != 4) {
      TestUtil.logErr("Number of TemporalType expected:4, actual:" + pt.size());
      pass = false;
    }
    if (pt.contains(PersistenceType.BASIC)) {
      TestUtil.logTrace("received:" + PersistenceType.BASIC);
    } else {
      TestUtil.logErr("Expected value:" + PersistenceType.BASIC);
      pass = false;
    }
    if (pt.contains(PersistenceType.EMBEDDABLE)) {
      TestUtil.logTrace("received:" + PersistenceType.EMBEDDABLE);
    } else {
      TestUtil.logErr("Expected value:" + PersistenceType.EMBEDDABLE);
      pass = false;
    }
    if (pt.contains(PersistenceType.ENTITY)) {
      TestUtil.logTrace("received:" + PersistenceType.ENTITY);
    } else {
      TestUtil.logErr("Expected value:" + PersistenceType.ENTITY);
      pass = false;
    }
    if (pt.contains(PersistenceType.MAPPED_SUPERCLASS)) {
      TestUtil.logTrace("received:" + PersistenceType.MAPPED_SUPERCLASS);
    } else {
      TestUtil.logErr("Expected value:" + PersistenceType.MAPPED_SUPERCLASS);
      pass = false;
    }

    for (PersistenceType p : pt) {
      try {
        PersistenceType.valueOf(p.name());
      } catch (Exception e) {
        TestUtil.logErr("Received exception for valueOf", e);
        pass = false;
      }
    }

    if (!pass) {
      throw new Fault("persistenceTypeValuesTest failed");
    }
  }

  /*
   * @testName: persistenceTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1473
   * 
   * @test_Strategy:
   *
   */
  public void persistenceTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      PersistenceType pt = PersistenceType
          .valueOf(PersistenceType.BASIC.name());
      if (!pt.equals(PersistenceType.BASIC)) {
        TestUtil.logErr("expected:" + PersistenceType.BASIC.name() + ", actual:"
            + pt.name());
        pass = false;
      }
      pt = PersistenceType.valueOf(PersistenceType.EMBEDDABLE.name());
      if (!pt.equals(PersistenceType.EMBEDDABLE)) {
        TestUtil.logErr("expected:" + PersistenceType.EMBEDDABLE.name()
            + ", actual:" + pt.name());
        pass = false;
      }
      pt = PersistenceType.valueOf(PersistenceType.ENTITY.name());
      if (!pt.equals(PersistenceType.ENTITY)) {
        TestUtil.logErr("expected:" + PersistenceType.ENTITY.name()
            + ", actual:" + pt.name());
        pass = false;
      }
      pt = PersistenceType.valueOf(PersistenceType.MAPPED_SUPERCLASS.name());
      if (!pt.equals(PersistenceType.MAPPED_SUPERCLASS)) {
        TestUtil.logErr("expected:" + PersistenceType.MAPPED_SUPERCLASS.name()
            + ", actual:" + pt.name());
        pass = false;
      }
      try {
        PersistenceType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        PersistenceType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("persistenceTypeValueOfTest failed");
    }
  }

  /*
   * @testName: loadStateValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1478
   * 
   * @test_Strategy:
   *
   */
  public void loadStateValuesTest() throws Fault {
    boolean pass = true;

    Collection<LoadState> ls = Arrays.asList(LoadState.values());

    if (ls.size() != 3) {
      TestUtil.logErr("Number of TemporalType expected:3, actual:" + ls.size());
      pass = false;
    }

    if (ls.contains(LoadState.LOADED)) {
      TestUtil.logTrace("received:" + LoadState.LOADED);
    } else {
      TestUtil.logErr("Expected value:" + LoadState.LOADED);
      pass = false;
    }
    if (ls.contains(LoadState.NOT_LOADED)) {
      TestUtil.logTrace("received:" + LoadState.NOT_LOADED);
    } else {
      TestUtil.logErr("Expected value:" + LoadState.NOT_LOADED);
      pass = false;
    }
    if (ls.contains(LoadState.UNKNOWN)) {
      TestUtil.logTrace("received:" + LoadState.UNKNOWN);
    } else {
      TestUtil.logErr("Expected value:" + LoadState.UNKNOWN);
      pass = false;
    }

    for (LoadState l : ls) {
      try {
        LoadState.valueOf(l.name());
      } catch (Exception e) {
        TestUtil.logErr("Received exception for valueOf", e);
        pass = false;
      }
    }

    if (!pass) {
      throw new Fault("loadStateValuesTest failed");
    }
  }

  /*
   * @testName: loadStateValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1477
   * 
   * @test_Strategy:
   *
   */
  public void loadStateValueOfTest() throws Fault {
    boolean pass = true;

    try {

      LoadState ls = LoadState.valueOf(LoadState.LOADED.name());
      if (!ls.equals(LoadState.LOADED)) {
        TestUtil.logErr(
            "expected:" + LoadState.LOADED.name() + ", actual:" + ls.name());
        pass = false;
      }
      ls = LoadState.valueOf(LoadState.NOT_LOADED.name());
      if (!ls.equals(LoadState.NOT_LOADED)) {
        TestUtil.logErr("expected:" + LoadState.NOT_LOADED.name() + ", actual:"
            + ls.name());
        pass = false;
      }
      ls = LoadState.valueOf(LoadState.UNKNOWN.name());
      if (!ls.equals(LoadState.UNKNOWN)) {
        TestUtil.logErr(
            "expected:" + LoadState.UNKNOWN.name() + ", actual:" + ls.name());
        pass = false;
      }
      try {
        LoadState.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        LoadState.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception occurred", e);
    }

    if (!pass) {
      throw new Fault("loadStateValueOfTest failed");
    }
  }

  /*
   * @testName: persistenceUnitTransactionTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1505; PERSISTENCE:SPEC:1909;
   * 
   * @test_Strategy:
   *
   */
  public void persistenceUnitTransactionTypeValuesTest() throws Fault {
    boolean pass = true;

    Collection<PersistenceUnitTransactionType> putt = Arrays
        .asList(PersistenceUnitTransactionType.values());

    if (putt.size() != 2) {
      TestUtil
          .logErr("Number of TemporalType expected:2, actual:" + putt.size());
      pass = false;
    }

    if (putt.contains(PersistenceUnitTransactionType.JTA)) {
      TestUtil.logTrace("received:" + PersistenceUnitTransactionType.JTA);
    } else {
      TestUtil.logErr("Expected value:" + PersistenceUnitTransactionType.JTA);
      pass = false;
    }
    if (putt.contains(PersistenceUnitTransactionType.RESOURCE_LOCAL)) {
      TestUtil.logTrace(
          "received:" + PersistenceUnitTransactionType.RESOURCE_LOCAL);
    } else {
      TestUtil.logErr(
          "Expected value:" + PersistenceUnitTransactionType.RESOURCE_LOCAL);
      pass = false;
    }

    for (PersistenceUnitTransactionType p : putt) {
      try {
        PersistenceUnitTransactionType.valueOf(p.name());
      } catch (Exception e) {
        TestUtil.logErr("Received exception for valueOf", e);
        pass = false;
      }
    }

    if (!pass) {
      throw new Fault("persistenceUnitTransactionTypeValuesTest failed");
    }
  }

  /*
   * @testName: persistenceUnitTransactionTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1504
   * 
   * @test_Strategy:
   *
   */
  public void persistenceUnitTransactionTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      PersistenceUnitTransactionType putt = PersistenceUnitTransactionType
          .valueOf(PersistenceUnitTransactionType.JTA.name());
      if (!putt.equals(PersistenceUnitTransactionType.JTA)) {
        TestUtil.logErr("expected:" + PersistenceUnitTransactionType.JTA.name()
            + ", actual:" + putt.name());
        pass = false;
      }
      putt = PersistenceUnitTransactionType
          .valueOf(PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
      if (!putt.equals(PersistenceUnitTransactionType.RESOURCE_LOCAL)) {
        TestUtil.logErr(
            "expected:" + PersistenceUnitTransactionType.RESOURCE_LOCAL.name()
                + ", actual:" + putt.name());
        pass = false;
      }
      try {
        PersistenceUnitTransactionType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        PersistenceUnitTransactionType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception occurred", e);
    }

    if (!pass) {
      throw new Fault("persistenceUnitTransactionTypeValueOfTest failed");
    }
  }

  /*
   * @testName: parameterModeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1538
   * 
   * @test_Strategy:
   *
   */
  public void parameterModeValuesTest() throws Fault {
    boolean pass = true;

    Collection<ParameterMode> cpm = Arrays.asList(ParameterMode.values());

    if (cpm.size() != 4) {
      TestUtil
          .logErr("Number of TemporalType expected:4, actual:" + cpm.size());
      pass = false;
    }

    if (cpm.contains(ParameterMode.IN)) {
      TestUtil.logTrace("received:" + ParameterMode.IN);
    } else {
      TestUtil.logErr("Expected value:" + ParameterMode.IN);
      pass = false;
    }
    if (cpm.contains(ParameterMode.INOUT)) {
      TestUtil.logTrace("received:" + ParameterMode.INOUT);
    } else {
      TestUtil.logErr("Expected value:" + ParameterMode.INOUT);
      pass = false;
    }
    if (cpm.contains(ParameterMode.OUT)) {
      TestUtil.logTrace("received:" + ParameterMode.OUT);
    } else {
      TestUtil.logErr("Expected value:" + ParameterMode.OUT);
      pass = false;
    }
    if (cpm.contains(ParameterMode.REF_CURSOR)) {
      TestUtil.logTrace("received:" + ParameterMode.REF_CURSOR);
    } else {
      TestUtil.logErr("Expected value:" + ParameterMode.REF_CURSOR);
      pass = false;
    }

    for (ParameterMode pm : cpm) {
      try {
        ParameterMode.valueOf(pm.name());
      } catch (Exception e) {
        TestUtil.logErr("Received Exception for valueOf", e);
        pass = false;
      }
    }

    if (!pass) {
      throw new Fault("parameterModeValuesTest failed");
    }
  }

  /*
   * @testName: parameterModeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1537
   * 
   * @test_Strategy:
   *
   */
  public void parameterModeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      ParameterMode pm = ParameterMode.valueOf(ParameterMode.IN.name());
      if (!pm.equals(ParameterMode.IN)) {
        TestUtil.logErr(
            "expected:" + ParameterMode.IN.name() + ", actual:" + pm.name());
        pass = false;
      }
      pm = ParameterMode.valueOf(ParameterMode.INOUT.name());
      if (!pm.equals(ParameterMode.INOUT)) {
        TestUtil.logErr(
            "expected:" + ParameterMode.INOUT.name() + ", actual:" + pm.name());
        pass = false;
      }
      pm = ParameterMode.valueOf(ParameterMode.OUT.name());
      if (!pm.equals(ParameterMode.OUT)) {
        TestUtil.logErr(
            "expected:" + ParameterMode.OUT.name() + ", actual:" + pm.name());
        pass = false;
      }
      pm = ParameterMode.valueOf(ParameterMode.REF_CURSOR.name());
      if (!pm.equals(ParameterMode.REF_CURSOR)) {
        TestUtil.logErr("expected:" + ParameterMode.REF_CURSOR.name()
            + ", actual:" + pm.name());
        pass = false;
      }
      try {
        ParameterMode.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        ParameterMode.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException npe) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception occurred", e);
    }

    if (!pass) {
      throw new Fault("parameterModeValueOfTest failed");
    }
  }

  /*
   * @testName: synchronizationTypeValueOfTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1660;
   * 
   * @test_Strategy:
   *
   */
  public void synchronizationTypeValueOfTest() throws Fault {
    boolean pass = true;

    try {

      SynchronizationType st = SynchronizationType
          .valueOf(SynchronizationType.SYNCHRONIZED.name());
      if (!st.equals(SynchronizationType.SYNCHRONIZED)) {
        TestUtil.logErr("expected:" + SynchronizationType.SYNCHRONIZED.name()
            + ", actual:" + st.name());
        pass = false;
      }
      st = SynchronizationType
          .valueOf(SynchronizationType.UNSYNCHRONIZED.name());
      if (!st.equals(SynchronizationType.UNSYNCHRONIZED)) {
        TestUtil.logErr("expected:" + SynchronizationType.UNSYNCHRONIZED.name()
            + ", actual:" + st.name());
        pass = false;
      }
      try {
        SynchronizationType.valueOf("DOESNOTEXIST");
        TestUtil.logErr("IllegalArgumentException was not thrown");
        pass = false;
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
      }
      try {
        SynchronizationType.valueOf(null);
        TestUtil.logErr("NullPointerException was not thrown");
        pass = false;
      } catch (NullPointerException iae) {
        TestUtil.logTrace("Received expected NullPointerException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("synchronizationTypeValueOfTest failed");
    }
  }

  /*
   * @testName: synchronizationTypeValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1661;
   * 
   * @test_Strategy:
   *
   */
  public void synchronizationTypeValuesTest() throws Fault {
    boolean pass = true;

    try {

      Collection<SynchronizationType> st = Arrays
          .asList(SynchronizationType.values());
      if (st.size() != 2) {
        TestUtil.logErr(
            "Number of SynchronizationType expected:2, actual:" + st.size());
        pass = false;
      }

      if (st.contains(SynchronizationType.SYNCHRONIZED)) {
        TestUtil.logTrace("received:" + SynchronizationType.SYNCHRONIZED);
      } else {
        TestUtil.logErr("Expected value:" + SynchronizationType.SYNCHRONIZED);
        pass = false;
      }
      if (st.contains(SynchronizationType.UNSYNCHRONIZED)) {
        TestUtil.logTrace("received:" + SynchronizationType.UNSYNCHRONIZED);
      } else {
        TestUtil.logErr("Expected value:" + SynchronizationType.UNSYNCHRONIZED);
        pass = false;
      }

      for (SynchronizationType s : st) {
        try {
          SynchronizationType.valueOf(s.name());
        } catch (Exception e) {
          TestUtil.logErr("Received exception for valueOf", e);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("synchronizationTypeValuesTest failed");
    }
  }

}
