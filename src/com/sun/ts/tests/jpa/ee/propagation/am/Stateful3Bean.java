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

package com.sun.ts.tests.jpa.ee.propagation.am;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceUnit;
import java.math.BigInteger;
import java.util.Properties;

@Stateful(name = "Stateful3Bean")
@Remote({ Stateful3IF.class })
public class Stateful3Bean implements Stateful3IF {

  @PersistenceUnit(unitName = "CTS-APPMANAGED-UNIT")
  private EntityManagerFactory entityManagerFactory;

  public SessionContext sessionContext;

  private EntityManager entityManager;

  private static final Member mRef[] = new Member[5];

  public Properties p;

  @Resource
  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @PostConstruct
  public void prepareEnvironment() {
    try {

      TestUtil.logTrace("In PostContruct");
      if (entityManagerFactory != null) {
        entityManager = entityManagerFactory.createEntityManager();
      } else {
        TestUtil.logErr("EntityManagerFactory is null");
      }

    } catch (Exception e) {
      TestUtil.logErr(
          " In PostConstruct: Exception caught while setting EntityManager", e);
    }
  }

  public void init(final Properties p) {
    TestUtil.logTrace("init");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean test3() {
    boolean pass = false;
    int memId = 9;
    try {

      TestUtil.logTrace("createTestData");
      entityManager.joinTransaction();
      removeTestData();
      createTestData();
      TestUtil.logTrace("find member");
      Member member = getMember(memId);
      entityManager.clear();

      Member memberClone = getMember(memId);

      if (null != member) {
        checkMemberStatus(memberClone);
        entityManager.merge(member);
        entityManager.flush();
      } else {
        TestUtil
            .logErr(" member is null, Unexpected - cannot proceed with test.");
      }
      pass = false;

    } catch (OptimisticLockException e) {
      pass = true;
      TestUtil.logTrace("Caught expected OptimisticLockException");
    } finally {
      TestUtil.logTrace("Finally, removeTestData");
      removeTestData();
    }
    return pass;
  }

  public boolean test4() {
    boolean pass = false;
    int memId = 8;
    try {

      TestUtil.logTrace("test4: createTestData");
      entityManager.joinTransaction();
      removeTestData();
      createTestData();
      TestUtil.logTrace("find member");
      Member member = getMember(memId);

      if (null != member) {
        TestUtil.logTrace("check member status");
        checkMemberStatus(member);
        // When an EntityManager with an extended persistence context
        // is used, the persist, remove merge, and refresh operations
        // may be called regardless of whether a TX is active. The
        // effects of these operations will be committed to the database
        // when the extended persistence context is enlisted in a transactin
        // and the transaction commits.
        // The scope of an AM entity manager is extended.
        entityManager.refresh(member);

        if (member.getDonation().toString().equals("10000")) {
          pass = true;
        }

      } else {
        TestUtil
            .logErr(" member is null, Unexpected - cannot proceed with test.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception:", e);
    } finally {
      removeTestData();
    }
    return pass;
  }

  public void checkMemberStatus(final Member m) {
    System.out.println("checkMemberStatus - joinTransaction");
    int newDonation = 10000;

    if (null == m) {
      TestUtil.logTrace("checkMemberStatus: member is NULL");
    } else {
      if (m.isDuesPaid()) {
        TestUtil.logTrace(
            "checkCustomerStatus: thisMember is not null, setDonation");
        m.setDonation(new BigInteger("10000"));
      } else {
        BigInteger currentDonation = m.getDonation();
        int convertedDonation = 0;
        if (currentDonation != null) {
          convertedDonation = currentDonation.intValue();
        }
        m.setDonation(
            new BigInteger(String.valueOf(convertedDonation + newDonation)));
        m.setDuesPaid(true);
      }

      TestUtil.logTrace("merge thisMember");
      entityManager.flush();
    }
  }

  public Member getMember(final int memberId) {
    TestUtil.logTrace("getMember");
    return entityManager.find(Member.class, memberId);
  }

  public void createTestData() {
    TestUtil.logTrace("createTestData");
    try {

      TestUtil.logTrace("Create Member Entities");
      mRef[0] = new Member(7, "Jane Lam", false);
      mRef[1] = new Member(8, "Vinny Testa", false);
      mRef[2] = new Member(9, "Mario Luigi", true, new BigInteger("25000"));
      mRef[3] = new Member(10, "Sky Blue", false);
      mRef[4] = new Member(11, "Leonardi DaVinci", true,
          new BigInteger("100000"));

      TestUtil.logTrace("Start to persist Members ");
      System.out.println("Persist Member Entities");
      for (Member m : mRef) {
        if (m != null) {
          entityManager.persist(m);
          TestUtil.logTrace("persisted Member " + m);
        }
      }

      entityManager.flush();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected while creating member data:" + e);
    }
  }

  public void removeTestData() {
    TestUtil.logTrace("removeTestData");
    try {
      entityManager.createNativeQuery("DELETE FROM MEMBER").executeUpdate();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    }
    // clear the cache if the provider supports caching otherwise
    // the evictAll is ignored.
    TestUtil.logTrace("Clearing cache");
    entityManagerFactory.getCache().evictAll();
  }
}
