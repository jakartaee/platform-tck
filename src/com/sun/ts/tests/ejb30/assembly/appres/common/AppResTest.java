/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.assembly.appres.common;

import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;
import static com.sun.ts.tests.ejb30.common.helper.Helper.assertNotEquals;
import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupNoTry;

import javax.ejb.SessionContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;

public class AppResTest {
  private AppResTest() {
  }

  public static HelloRemoteIF getHelloBeanRemote() {
    return (HelloRemoteIF) lookupNoTry("java:app/env/hello");
  }

  public static AppResRemoteIF getAppResBeanRemote() {
    return (AppResRemoteIF) lookupNoTry("java:app/env/AppResBean-remote");
  }

  public static AppResLocalIF getAppResBeanLocal() {
    return (AppResLocalIF) lookupNoTry("java:app/env/AppResBean-local");
  }

  public static final void verifyMyString(StringBuilder sb) {
    String s = (String) lookupNoTry("java:app/env/myString");
    assertEquals(null, "myString", s, sb);
  }

  public static final void verifyDataSource(StringBuilder sb) {
    DataSource db1 = (DataSource) lookupNoTry("java:app/env/db1");
    assertNotEquals(null, null, db1, sb);
  }

  public static final void verifyUserTransaction(StringBuilder sb) {
    UserTransaction ut = (UserTransaction) lookupNoTry("java:app/env/ut");
    assertNotEquals(null, null, ut, sb);
  }

  public static final void verifyPersistenceContext(StringBuilder sb) {
    EntityManager em = (EntityManager) lookupNoTry(
        "java:app/env/entityManager");
    assertNotEquals(null, null, em, sb);
  }

  public static final void verifyPersistenceUnit(StringBuilder sb) {
    EntityManagerFactory pu = (EntityManagerFactory) lookupNoTry(
        "java:app/env/persistenceUnit");
    assertNotEquals(null, null, pu, sb);
  }

  public static final void verifyQueue(StringBuilder sb) {
    Queue queue = (Queue) lookupNoTry("java:app/env/receiveQueue");
    assertNotEquals(null, null, queue, sb);
    try {
      queue.getQueueName();
    } catch (JMSException e) {
      throw new RuntimeException(e);
    }
  }

  public static final void verifyValidatorAndFactory(StringBuilder sb) {
    // lookup standard names
    Validator v = (Validator) lookupNoTry("java:comp/Validator");
    ValidatorFactory vf = (ValidatorFactory) lookupNoTry(
        "java:comp/ValidatorFactory");
    Validator v2 = vf.getValidator();
    assertNotEquals("Check Validator java:comp/Validator", null, v, sb);
    assertNotEquals("Check Validator from factory", null, v2, sb);
    assertNotEquals("Check ValidatorFactory java:comp/ValidatorFactory", null,
        vf, sb);

    // lookup names declared in application.xml
    v = (Validator) lookupNoTry("java:app/env/validator");
    vf = (ValidatorFactory) lookupNoTry("java:app/env/validatorFactory");
    v2 = vf.getValidator();
    assertNotEquals("Check Validator java:app/env/validator", null, v, sb);
    assertNotEquals("Check Validator from factory", null, v2, sb);
    assertNotEquals("Check ValidatorFactory java:app/env/validatorFactory",
        null, vf, sb);
  }

  public static final void verifyInjections(StringBuilder sb, Object... oo) {
    for (Object o : oo) {
      assertNotEquals(null, null, o, sb);
    }
  }

  public static void beanPostConstruct(String myString,
      StringBuilder postConstructRecords, boolean namingContextLookup,
      boolean ejbContextLookup) {
    int a = 1, b = 1, c = a + b;
    try {
      verifyMyString(postConstructRecords);
      verifyUserTransaction(postConstructRecords);
      verifyDataSource(postConstructRecords);
      verifyQueue(postConstructRecords);

      // skip verification of persistence-context-ref for appclientejb
      // directory,
      // since persistence-context-ref is not defined in
      // appclientejb/application.xml
      String appName = (String) lookupNoTry("java:app/AppName");
      if (!appName.endsWith("appclientejb")) {
        verifyPersistenceContext(postConstructRecords);
      }

      verifyPersistenceUnit(postConstructRecords);
      verifyValidatorAndFactory(postConstructRecords);

      assertEquals(null, c, getHelloBeanRemote().add(a, b),
          postConstructRecords);
      assertEquals(null, "myString", myString, postConstructRecords);

      if (namingContextLookup) {
        AppResRemoteIF br = getAppResBeanRemote();
        AppResLocalIF bl = getAppResBeanLocal();

        if (ejbContextLookup) {
          SessionContext sctx = (SessionContext) lookupNoTry(
              "java:app/env/sctx");
          AppResLocalIF bl2 = (AppResLocalIF) sctx
              .lookup("java:app/env/AppResBean-local");
          AppResRemoteIF br2 = (AppResRemoteIF) sctx
              .lookup("java:app/env/AppResBean-remote");

          assertEquals(null, br, br2, postConstructRecords);
          assertEquals(null, bl, bl2, postConstructRecords);
        }
      }

    } catch (RuntimeException e) {
      Helper.getLogger().warning(postConstructRecords.toString());
      throw e;
    } catch (Error e) {
      Helper.getLogger().warning(postConstructRecords.toString());
      throw e;
    }
  }
}
