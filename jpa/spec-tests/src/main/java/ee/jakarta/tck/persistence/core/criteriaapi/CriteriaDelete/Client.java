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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaDelete;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;



import ee.jakarta.tck.persistence.common.schema30.Product;
import ee.jakarta.tck.persistence.common.schema30.SoftwareProduct;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class Client extends Util {

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: fromClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1685; PERSISTENCE:JAVADOC:631;
	 * PERSISTENCE:SPEC:1780; PERSISTENCE:SPEC:1781; PERSISTENCE:SPEC:1782;
	 * PERSISTENCE:SPEC:1794;
	 *
	 * @test_Strategy: DELETE FROM Product p
	 */
		public void fromClassTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaDelete<Product> cd = cbuilder.createCriteriaDelete(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logTrace( "Obtained Non-null root");
			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
				logTrace( "Received expected number deleted:" + actual);
				clearCache();
				logMsg( "Make sure items were deleted by looking up the Products id");
				for (Product p : productRef) {
					pass1 = true;
					if (getEntityManager().find(Product.class, p.getId()) != null) {
						logErr( "Expected product:" + p.getId() + " to have been deleted");
						pass2 = false;
					}
				}
			} else {
				logErr( "Expected:" + productRef.length + ", actual:" + actual);
			}
		} else {
			logErr( "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("fromClassTest failed");
		}
	}

	/*
	 * @testName: fromEntityTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1686;
	 *
	 * @test_Strategy: DELETE FROM Product p
	 */
		public void fromEntityTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaDelete<Product> cd = cbuilder.createCriteriaDelete(Product.class);

		Metamodel mm = getEntityManager().getMetamodel();
		EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

		Root<Product> root = cd.from(Product_);

		if (root != null) {
			logTrace( "Obtained Non-null root");
			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
				logTrace( "Received expected number deleted:" + actual);
				clearCache();
				logMsg( "Make sure items were deleted by looking up the Products id");
				for (Product p : productRef) {
					pass1 = true;
					if (getEntityManager().find(Product.class, p.getId()) != null) {
						logErr( "Expected product:" + p.getId() + " to have been deleted");
						pass2 = false;
					}
				}
			} else {
				logErr( "Expected:" + productRef.length + ", actual:" + actual);
			}
		} else {
			logErr( "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("fromEntityTypeTest failed");
		}
	}

	/*
	 * @testName: getRootTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1687
	 *
	 * @test_Strategy:
	 *
	 */
		public void getRootTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		CriteriaDelete<Product> cd = cbuilder.createCriteriaDelete(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logTrace( "Obtained Non-null root");
			if (root.getModel().getName().equals(cd.getRoot().getModel().getName())) {
				logTrace( "Obtained expected root");
				pass = true;
			} else {
				logErr( "Failed to get expected root");
				logErr( "Expected:" + cd.getRoot() + ", actual:" + root);
			}
		} else {
			logErr( "Failed to get Non-null root");
		}

		if (!pass) {
			throw new Exception("getRootTest failed");
		}
	}

	/*
	 * @testName: whereExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1688; PERSISTENCE:JAVADOC:3352;
	 *
	 * @test_Strategy: DELETE FROM Product p where p.id in (1,2,3)
	 */
		public void whereExpressionTest() throws Exception {
		boolean pass2 = false;
		boolean pass3 = true;
		boolean pass4 = true;
		boolean pass5 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaDelete<Product> cd = cbuilder.createCriteriaDelete(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logTrace( "Obtained Non-null root");

			Expression exp = root.get("id");
			Collection<String> col = new ArrayList<String>();
			col.add("1");
			col.add("2");
			col.add("3");

			cd.where(exp.in(col));

			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == col.size()) {
				logTrace( "Received expected number deleted:" + actual);
				clearCache();
				pass2 = true;
				for (Product p : productRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (p.getId().equals("1") || p.getId().equals("2") || p.getId().equals("3")) {
						if (pp != null) {
							logErr( "Expected product:" + p.getId() + " to have been deleted");
							pass3 = false;
						} else {
							logTrace( "Product:" + p.getId() + " was successfully deleted");
						}
					} else {
						if (pp == null) {
							logErr( "Product:" + p.getId() + " was incorrectly deleted");
							pass3 = false;
						} else {
							logTrace( "Found Product:" + pp.getId());
						}
					}
				}
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp == null) {
						logErr( "Software Product:" + p.getId() + " was incorrectly deleted");

						pass4 = false;
					} else {
						logTrace( "Found Software Product:" + pp.getId());
					}
				}
				for (Product p : hardwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp == null) {
						logErr( "Harware Product:" + p.getId() + " was incorrectly deleted");
						pass5 = false;
					} else {
						logTrace( "Found Harware Product:" + pp.getId());
					}
				}
			} else {
				logErr( "Expected:" + col.size() + ", actual:" + actual);
			}
		} else {
			logErr( "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass2 || !pass3 || !pass4 || !pass5) {
			throw new Exception("whereExpressionTest failed");
		}
	}

	/*
	 * @testName: wherePredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1689;
	 *
	 * @test_Strategy: DELETE FROM Product p where p.id in (2)
	 */
		public void wherePredicateArrayTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaDelete<Product> cd = cbuilder.createCriteriaDelete(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logTrace( "Obtained Non-null root");

			Predicate[] predArray = { cbuilder.equal(root.get("id"), "2") };

			cd.where(predArray);

			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == predArray.length) {
				logTrace( "Received expected number deleted:" + actual);
				pass1 = true;
			} else {
				logErr( "Expected deleted:" + predArray.length + ", actual:" + actual);
			}
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			for (Product p : productRef) {
				Product pp = getEntityManager().find(Product.class, p.getId());
				if (p.getId().equals("2")) {
					if (pp != null) {
						logErr( "Expected product:" + p.getId() + " to have been deleted");
						pass2 = false;
					} else {
						logTrace( "Product:" + p.getId() + " was successfully deleted");
					}
				} else {
					if (pp == null) {
						logErr( "Product:" + p.getId() + " was incorrectly deleted");
						pass2 = false;
					} else {
						logTrace( "Found Product:" + pp.getId());
					}
				}
			}
			for (Product p : softwareRef) {
				Product pp = getEntityManager().find(Product.class, p.getId());
				if (pp == null) {
					logErr( "Software product:" + p.getId() + " should not have been deleted");
					pass3 = false;
				}
			}
			for (Product p : hardwareRef) {
				Product pp = getEntityManager().find(Product.class, p.getId());
				if (pp == null) {
					logErr( "Hardware product:" + p.getId() + " should not have been deleted");
					pass4 = false;
				}
			}
		} else {
			logErr( "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("wherePredicateArrayTest failed");
		}
	}

	/*
	 * @testName: subquery
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3353
	 *
	 * @test_Strategy:
	 *
	 * DELETE FROM Product p where product WHERE EXISTS (Select hardProd From
	 * PRODUCT hardprod where hardprod.id = '1').
	 *
	 */
		public void subquery() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		List<Integer> expected = new ArrayList<Integer>();
		expected.add(19);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaDelete<Product> cd = cbuilder.createCriteriaDelete(Product.class);
		if (cd != null) {
			logTrace( "Obtained Non-null Criteria Query");
			Root<Product> product = cd.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();

			// create Subquery instance, with root Customer
			Subquery<Product> sq = cd.subquery(Product.class);
			Root<Product> hardProd = sq.from(Product.class);

			// the subquery references the root of the containing query
			sq.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "1"))
					.select(hardProd);

			// an exists condition is applied to the subquery result
			cd.where(cbuilder.exists(sq));

			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == 1) {
				logTrace( "Received expected number deleted:" + actual);
				clearCache();
				for (Product p : productRef) {
					pass1 = true;
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (p.getId().equals("1")) {
						if (pp != null) {
							logErr(
									"Expected product:" + p.getId() + " to have been deleted:" + p.toString());
							pass2 = false;
						} else {
							logTrace( "Product:" + p.getId() + " was successfully deleted");
						}
					} else {
						if (pp == null) {
							logErr( "Product:" + p.getId() + " was incorrectly deleted");
							pass2 = false;
						} else {
							logTrace( "Found product:" + pp.getId());
						}
					}
				}
			} else {
				logErr( "Expected:1, actual:" + actual);
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("exists test failed");
		}
	}

	/*
	 * @testName: modifiedQueryTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1789; PERSISTENCE:SPEC:1797;
	 * 
	 * @test_Strategy: DELETE FROM SoftwareProduct WHERE p.quantity < 35) DELETE
	 * FROM SoftwareProduct WHERE p.quantity > 100)
	 *
	 */
		public void modifiedQueryTest() throws Exception {
		int passDeletedCount1 = 0;
		int passUnDeletedCount1 = 0;
		int passDeletedCount2 = 0;
		int passUnDeletedCount2 = 0;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();
		logMsg( "Testing initial query");
		CriteriaDelete<SoftwareProduct> cd = cbuilder.createCriteriaDelete(SoftwareProduct.class);
		Root<SoftwareProduct> softwareproduct = cd.from(SoftwareProduct.class);
		if (softwareproduct != null) {
			EntityType<SoftwareProduct> softwareproduct_ = softwareproduct.getModel();
			cd.where(cbuilder.lt(softwareproduct.get(softwareproduct_.getSingularAttribute("quantity", Integer.class)),
					35));
			Query q = getEntityManager().createQuery(cd);
			logMsg( "Modify CriteriaDelete object");
			cd.where(cbuilder.lt(softwareproduct.get(softwareproduct_.getSingularAttribute("quantity", Integer.class)),
					500));
			int actual = q.executeUpdate();
			if (actual == 4) {
				logTrace( "Received expected number of deletes:" + actual);
				clearCache();
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(SoftwareProduct.class, p.getId());
					if (p.getId().equals("30") || p.getId().equals("31") || p.getId().equals("36")
							|| p.getId().equals("37")) {
						if (pp != null) {
							logErr( "Expected product:" + p.getId() + " to have been deleted");
						} else {
							logTrace( "id:" + p.getId() + " was successfully deleted");
							passDeletedCount1++;
						}
					} else {
						if (pp == null) {
							logErr( "id:" + p.getId() + " was incorrectly deleted");
						} else {
							logTrace( "Found product:" + pp.getId());
							passUnDeletedCount1++;
						}
					}
				}
			} else {
				logErr( "Expected: 4 deletes [30,31,36,37], actual:" + actual);
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp != null) {
						logErr(
								"id:" + p.getId() + ", quantity:" + pp.getQuantity() + " exists");
					} else {
						logErr( "id:" + p.getId() + " was deleted");
					}
				}
			}
			logMsg( "Testing modified CriteriaQuery");
			cd.where(cbuilder.gt(softwareproduct.get(softwareproduct_.getSingularAttribute("quantity", Integer.class)),
					100));

			actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == 3) {
				logTrace( "Received expected number of deletes:" + actual);
				clearCache();
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(SoftwareProduct.class, p.getId());
					if (p.getId().equals("29") || p.getId().equals("34") || p.getId().equals("38")
							|| p.getId().equals("30") || p.getId().equals("31") || p.getId().equals("36")
							|| p.getId().equals("37")) {
						if (pp != null) {
							logErr( "Expected product:" + p.getId() + " to have been deleted");
						} else {
							logTrace( "id:" + p.getId() + " was successfully deleted");
							passDeletedCount2++;
						}
					} else {
						if (pp == null) {
							logErr( "id:" + p.getId() + " was incorrectly deleted");
						} else {
							logTrace( "Found id:" + pp.getId());
							passUnDeletedCount2++;
						}
					}
				}
			} else {
				logErr(
						"Expected: 3 additional deletes [29,34,38] total deletes [29,30,31,34,36,37,38], actual:"
								+ actual);
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					logErr( "id:" + p.getId() + ", quantity:" + pp.getQuantity());
				}
			}

		} else {
			logErr( "Failed to get Non-null root");
		}

		getEntityTransaction().commit();
		if (passDeletedCount1 != 4 || passUnDeletedCount1 != 6 || passDeletedCount2 != 7 || passUnDeletedCount2 != 3) {
			throw new Exception("modifiedQueryTest failed");
		}

	}

	public void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_DETAILS").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_TABLE").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
