/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.metamodelapi.collectionattribute;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.PluralAttribute;

@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class ClientIT extends PMClientBase {

  public ClientIT() {
  }
  
  @Deployment(testable = false, managed = false)
	public static JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
		String pkgName = ClientIT.class.getPackageName() + ".";
		String[] classes = {pkgName + "Uni1XMPerson",
				pkgName + "Uni1XMProject"};
		return createDeploymentJar("jpa_core_metamodelapi_collectionattribute.jar", pkgNameWithoutSuffix, classes);

	}


@BeforeAll
  public void setup() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
    }
  }

  /*
   * @testName: getCollectionType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1228;
   * 
   * @test_Strategy:
   *
   */
@Test
  public void getCollectionType() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Uni1XMPerson> mType = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.collectionattribute.Uni1XMPerson.class);
      if (mType != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        CollectionAttribute<? super Uni1XMPerson, Uni1XMProject> colAttrib = mType
            .getCollection("projects",
                com.sun.ts.tests.jpa.core.metamodelapi.collectionattribute.Uni1XMProject.class);

        PluralAttribute.CollectionType pluralColType = colAttrib
            .getCollectionType();
        TestUtil.logTrace("collection Type = " + colAttrib.getCollectionType());
        if (pluralColType == PluralAttribute.CollectionType.COLLECTION) {
          TestUtil
              .logTrace("Received Expected Collection type = " + pluralColType);
          pass = true;
        } else {
          TestUtil.logTrace(
              "Received UnExpected Collection type = " + pluralColType);
        }

        /*
         * Type t = colAttrib.getElementType(); if (t != null) {
         * TestUtil.logTrace("element Type  = " + t.getJavaType()); pass = true;
         * }
         */
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getCollectionType Test  failed");
    }
  }

  /*
   * @testName: getElementType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1229;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getElementType() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Uni1XMPerson> mType = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.collectionattribute.Uni1XMPerson.class);
      if (mType != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        CollectionAttribute<? super Uni1XMPerson, Uni1XMProject> colAttrib = mType
            .getCollection("projects",
                com.sun.ts.tests.jpa.core.metamodelapi.collectionattribute.Uni1XMProject.class);

        TestUtil.logTrace("collection Element Type = "
            + colAttrib.getElementType().getJavaType().getName());
        String elementTypeName = colAttrib.getElementType().getJavaType()
            .getName();
        if (elementTypeName.equals(
            "com.sun.ts.tests.jpa.core.metamodelapi.collectionattribute.Uni1XMProject")) {
          TestUtil
              .logTrace("Received Expected Element type = " + elementTypeName);
          pass = true;
        } else {
          TestUtil.logTrace(
              "Received UnExpected Element type = " + elementTypeName);
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getElementType Test  failed");
    }
  }

@AfterAll
  public void cleanup() throws Exception {

    TestUtil.logTrace("in cleanup");
    try {
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception fe) {
      TestUtil.logErr("Unexpected exception rolling back TX:", fe);
    }
    TestUtil.logTrace("done cleanup, calling super.cleanup");
    super.cleanup();
  }
}
