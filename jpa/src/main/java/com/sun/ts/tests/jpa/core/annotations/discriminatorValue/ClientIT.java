/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.discriminatorValue;

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

@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class ClientIT extends PMClientBase {

	@Deployment(testable = false, managed = false)
	public static JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
		String pkgName = ClientIT.class.getPackageName() + ".";
		String[] classes = { pkgName + "PartProduct", pkgName + "PartProduct2", pkgName + "PricedPartProduct2",
				pkgName + "Product", pkgName + "Product2" };
		return createDeploymentJar("jpa_core_annotations_discrinimatorValue.jar", pkgNameWithoutSuffix, classes);

	}

	public ClientIT() {
		super();
	}

	@BeforeAll
	public void setup() throws Exception {
		TestUtil.logTrace("setup");
		try {

			super.setup();
			removeTestData();
		} catch (Exception e) {
			TestUtil.logErr("Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	private PricedPartProduct2 newPricedPartProduct(final String testName) {
		PricedPartProduct2 product = new PricedPartProduct2();
		product.setId(testName);
		product.setName(testName);
		product.setPartNumber(1L);
		product.setPrice(1D);
		product.setQuantity(1);
		return product;
	}

	private PartProduct newPartProduct(final String testName) {
		PartProduct product = new PartProduct();
		product.setId(testName);
		product.setName(testName);
		product.setPartNumber(1L);
		product.setQuantity(1);
		return product;
	}

	private Product newProduct(final String testName) {
		Product product = new Product();
		product.setId(testName);
		product.setName(testName);
		product.setQuantity(1);
		return product;
	}

	/*
	 * @testName: integerDiscriminatorValueTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2006;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void integerDiscriminatorValueTest() throws Exception {
		boolean pass = false;
		final String testName = "integerDiscriminatorValueTest";
		try {
			getEntityTransaction().begin();
			PricedPartProduct2 p1 = newPricedPartProduct(testName);
			getEntityManager().persist(p1);
			getEntityManager().flush();
			clearCache();
			PricedPartProduct2 p2 = getEntityManager().find(PricedPartProduct2.class, testName);
			TestUtil.logTrace("finding PricedPartProduct2 with id '" + testName + "'");

			if (p1.equals(p2)) {
				TestUtil.logTrace("Received expected PricedPartProduct2:" + p2);
				pass = true;
			} else {
				TestUtil.logErr("Did not get expected result.");
				TestUtil.logErr("Expected:" + p1);
				TestUtil.logErr("Actual:" + p2);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("integerDiscriminatorValueTest Failed");
		}
	}

	/*
	 * @testName: discriminatorValueTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2005; PERSISTENCE:SPEC:2513;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void discriminatorValueTest() throws Exception {
		boolean pass1 = false;
		final String testName = "discriminatorValueTest";
		try {
			getEntityTransaction().begin();
			PartProduct p1 = newPartProduct(testName);
			getEntityManager().persist(p1);
			getEntityManager().flush();
			clearCache();
			PartProduct p2 = getEntityManager().find(PartProduct.class, testName);
			TestUtil.logTrace("finding PartProduct with id '" + testName + "'");

			if (p1.equals(p2)) {
				TestUtil.logTrace("Received expected PartProduct:" + p2);
				pass1 = true;
			} else {
				TestUtil.logErr("Did not get expected result.");
				TestUtil.logErr("Expected:" + p1);
				TestUtil.logErr("Actual:" + p2);
			}

			Product p3 = newProduct(testName);
			getEntityManager().persist(p3);
			getEntityManager().flush();
			clearCache();
			Product p4 = getEntityManager().find(Product.class, testName);
			TestUtil.logTrace("finding Product with id '" + testName + "'");

			if (p3.equals(p4)) {
				TestUtil.logTrace("Received expected Product:" + p2);
				pass1 = true;
			} else {
				TestUtil.logErr("Did not get expected result.");
				TestUtil.logErr("Expected:" + p3);
				TestUtil.logErr("Actual:" + p4);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Unexpected exception occurred", e);
		}

		if (!pass1) {
			throw new Exception("discriminatorValueTest Failed");
		}
	}

	@AfterAll
	public void cleanup() throws Exception {
		TestUtil.logTrace("cleanup");
		removeTestData();
		TestUtil.logTrace("cleanup complete, calling super.cleanup");
		super.cleanup();
	}

	private void removeTestData() {
		TestUtil.logTrace("removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_TABLE_DISCRIMINATOR").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_TABLE").executeUpdate();
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
