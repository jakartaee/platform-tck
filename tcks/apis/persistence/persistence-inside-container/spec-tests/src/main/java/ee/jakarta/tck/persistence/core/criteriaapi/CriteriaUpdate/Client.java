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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaUpdate;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import ee.jakarta.tck.persistence.common.schema30.Util;



import ee.jakarta.tck.persistence.common.schema30.Product;
import ee.jakarta.tck.persistence.common.schema30.SoftwareProduct;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class Client extends Util {



	public Client() {

	}
	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		TestUtil.logTrace("setup");
		try {
			super.setup(args, p);
			removeTestData();
			createProductData();
		} catch (Exception e) {
			TestUtil.logErr("Exception: ", e);
			throw new Fault("Setup failed:", e);
		}
	}

	/*
	 * @testName: fromClassGetRootSetStringObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1696; PERSISTENCE:JAVADOC:1698;
	 * PERSISTENCE:JAVADOC:1703; PERSISTENCE:JAVADOC:631; PERSISTENCE:SPEC:1777;
	 * PERSISTENCE:SPEC:1778; PERSISTENCE:SPEC:1779; PERSISTENCE:SPEC:1793;
	 *
	 * @test_Strategy: UPDATE Product SET QUANTITY = 0
	 *
	 */
		public void fromClassGetRootSetStringObjectTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logTrace( "Obtained Non-null root");
			if (root.equals(cd.getRoot())) {
				logTrace( "Obtained expected root");
				cd.set("quantity", 0);
				int actual = getEntityManager().createQuery(cd).executeUpdate();
				if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
					pass1 = true;
					logTrace( "Received expected number deleted:" + actual);
					clearCache();
					for (Product p : productRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass2 = false;
						}
					}
					for (Product p : softwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass3 = false;
						}
					}
					for (Product p : hardwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass4 = false;
						}
					}
				} else {
					logErr( "Expected:" + productRef.length + softwareRef.length
							+ hardwareRef.length + ", actual:" + actual);
				}
			} else {
				logErr( "Failed to get expected root");
				logErr( "Expected:" + cd.getRoot() + ", actual:" + root);
			}
		} else {
			logErr( "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("fromClassGetRootSetStringObjectTest failed");
		}
	}

	/*
	 * @testName: fromEntityTypeSetStringObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1697; PERSISTENCE:JAVADOC:1703;
	 * 
	 * @test_Strategy: UPDATE Product SET QUANTITY = 0
	 */
		public void fromEntityTypeSetStringObjectTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);

		Metamodel mm = getEntityManager().getMetamodel();
		EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

		Root<Product> root = cd.from(Product_);
		if (root != null) {
			logTrace( "Obtained Non-null root");
			cd.set("quantity", 0);
			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
				pass1 = true;
				logTrace( "Received expected number deleted:" + actual);
				clearCache();
				for (Product p : productRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logErr(
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass2 = false;
					}
				}
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logErr(
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass3 = false;
					}
				}
				for (Product p : hardwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logErr(
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass4 = false;
					}
				}
			} else {
				logErr( "Expected:" + productRef.length + softwareRef.length + hardwareRef.length
						+ ", actual:" + actual);
			}
		} else {
			logErr( "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("fromEntityTypeSetStringObjectTest failed");
		}
	}

	/*
	 * @testName: whereExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1704; PERSISTENCE:JAVADOC:3354;
	 *
	 * @test_Strategy: UPDATE Product p SET p.quantity = 0 WHERE p.id in (1,2,3)
	 */
		public void whereExpressionTest() throws Exception {
		boolean pass2 = false;
		boolean pass3 = true;
		boolean pass4 = true;
		boolean pass5 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logTrace( "Obtained Non-null root");
			EntityType<Product> product_ = root.getModel();
			cd.set(product_.getSingularAttribute("quantity", Integer.class), 0);

			Expression exp = root.get("id");
			Collection<String> col = new ArrayList<String>();
			col.add("1");
			col.add("2");
			col.add("3");

			cd.where(exp.in(col));

			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == col.size()) {
				logTrace( "Received expected number of updates:" + actual);
				pass2 = true;
				clearCache();
				for (Product p : productRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (p.getId().equals("1") || p.getId().equals("2") || p.getId().equals("3")) {
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass3 = false;
						} else {
							logTrace( "Product:" + p.getId() + " update was successfully");
						}
					} else {
						if (pp.getQuantity() != p.getQuantity()) {
							logErr( "Product:" + p.getId()
									+ " quantity does not equal original value:" + p.getQuantity());
							pass3 = false;
						} else {
							logTrace(
									"Product:" + pp.getId() + " quantity matches original value" + p.getQuantity());
						}
					}
				}
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != p.getQuantity()) {
						logErr( "Expected product:" + p.getId() + " to have quantity of "
								+ p.getQuantity() + ", actual:" + pp.getQuantity());
						pass4 = false;
					} else {
						logTrace( "Received expected software quantity:" + pp.getQuantity());
					}
				}
				for (Product p : hardwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != p.getQuantity()) {
						logErr( "Expected product:" + p.getId() + " to have quantity of "
								+ p.getQuantity() + ", actual:" + pp.getQuantity());
						pass5 = false;
					} else {
						logTrace( "Received expected hardware quantity:" + pp.getQuantity());
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1705; PERSISTENCE:JAVADOC:949;
	 *
	 * @test_Strategy: UPDATE Product p SET p.quantity = 0 WHERE p.id in (2)
	 */
		public void wherePredicateArrayTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);

		if (root != null) {
			logTrace( "Obtained Non-null root");

			EntityType<Product> product_ = root.getModel();
			cd.set(product_.getSingularAttribute("quantity", Integer.class), 0);

			Predicate[] predArray = { cbuilder.equal(root.get("id"), "2") };

			cd.where(predArray);

			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == predArray.length) {
				logTrace( "Received expected number of updates:" + actual);
				pass1 = true;
				clearCache();
				for (Product p : productRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (p.getId().equals("2")) {
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass2 = false;
						} else {
							logTrace( "Product:" + p.getId() + " update was successfully");
						}
					} else {
						if (pp.getQuantity() != p.getQuantity()) {
							logErr( "Product:" + p.getId()
									+ " quantity does not equal original value:" + p.getQuantity());
							pass2 = false;
						} else {
							logTrace(
									"Product:" + pp.getId() + " quantity matches original value" + p.getQuantity());
						}
					}
				}
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != p.getQuantity()) {
						logErr( "Expected product:" + p.getId() + " to have quantity of "
								+ p.getQuantity() + ", actual:" + pp.getQuantity());

						pass3 = false;
					} else {
						logTrace( "Received expected software quantity:" + pp.getQuantity());
					}
				}
				for (Product p : hardwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != p.getQuantity()) {
						logErr( "Expected product:" + p.getId() + " to have quantity of "
								+ p.getQuantity() + ", actual:" + pp.getQuantity());
						pass4 = false;
					} else {
						logTrace( "Received expected hardware quantity:" + pp.getQuantity());
					}
				}
			} else {
				logErr( "Expected updates:" + predArray.length + ", actual:" + actual);
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
	 * @testName: setSingularAttributeObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1699; PERSISTENCE:JAVADOC:950;
	 * 
	 * @test_Strategy: UPDATE Product SET QUANTITY = 0
	 *
	 */
		public void setSingularAttributeObjectTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logTrace( "Obtained Non-null root");
			if (root.getModel().getName().equals(cd.getRoot().getModel().getName())) {
				logTrace( "Obtained expected root");

				EntityType<Product> product_ = root.getModel();
				cd.set(product_.getSingularAttribute("quantity", Integer.class), 0);

				int actual = getEntityManager().createQuery(cd).executeUpdate();
				if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
					pass1 = true;
					logTrace( "Received expected number of updates:" + actual);
					clearCache();
					for (Product p : productRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass2 = false;
						}
					}
					for (Product p : softwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass3 = false;
						}
					}
					for (Product p : hardwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass4 = false;
						}
					}
				} else {
					logErr( "Expected:" + productRef.length + softwareRef.length
							+ hardwareRef.length + ", actual:" + actual);
				}
			} else {
				logErr( "Failed to get expected root");
				logErr(
						"Expected:" + cd.getRoot().getModel().getName() + ", actual:" + root.getModel().getName());
			}
		} else {
			logErr( "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setSingularAttributeObjectTest failed");
		}
	}

	/*
	 * @testName: setSingularAttributeExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1700;
	 * 
	 * @test_Strategy: UPDATE Product p SET p.quantity = prod(p.quantity,0)
	 *
	 */
		public void setSingularAttributeExpressionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logTrace( "Obtained Non-null root");
			if (root.getModel().getName().equals(cd.getRoot().getModel().getName())) {
				logTrace( "Obtained expected root");

				EntityType<Product> product_ = root.getModel();
				Expression exp = cbuilder.prod(root.get(product_.getSingularAttribute("quantity", Integer.class)), 0);
				cd.set(product_.getSingularAttribute("quantity", Integer.class), exp);
				int actual = getEntityManager().createQuery(cd).executeUpdate();
				if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
					pass1 = true;
					logTrace( "Received expected number of updates:" + actual);
					clearCache();
					for (Product p : productRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass2 = false;
						}
					}
					for (Product p : softwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass3 = false;
						}
					}
					for (Product p : hardwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass4 = false;
						}
					}
				} else {
					logErr( "Expected:" + productRef.length + softwareRef.length
							+ hardwareRef.length + ", actual:" + actual);
				}
			} else {
				logErr( "Failed to get expected root");
				logErr(
						"Expected:" + cd.getRoot().getModel().getName() + ", actual:" + root.getModel().getName());
			}
		} else {
			logErr( "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setSingularAttributeExpressionTest failed");
		}
	}

	/*
	 * @testName: setPathObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1701;
	 * 
	 * @test_Strategy: UPDATE Product SET QUANTITY = 0
	 *
	 */
		public void setPathObjectTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logTrace( "Obtained Non-null root");
			if (root.getModel().getName().equals(cd.getRoot().getModel().getName())) {
				logTrace( "Obtained expected root");
				cd.set(root.get("quantity"), 0);
				int actual = getEntityManager().createQuery(cd).executeUpdate();
				if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
					pass1 = true;
					logTrace( "Received expected number of updates:" + actual);
					clearCache();
					for (Product p : productRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass2 = false;
						}
					}
					for (Product p : softwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass3 = false;
						}
					}
					for (Product p : hardwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logErr( "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass4 = false;
						}
					}
				} else {
					logErr( "Expected:" + productRef.length + softwareRef.length
							+ hardwareRef.length + ", actual:" + actual);
				}
			} else {
				logErr( "Failed to get expected root");
				logErr(
						"Expected:" + cd.getRoot().getModel().getName() + ", actual:" + root.getModel().getName());
			}
		} else {
			logErr( "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setPathObjectTest failed");
		}
	}

	/*
	 * @testName: setPathExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1702;
	 * 
	 * @test_Strategy: UPDATE Product p SET p.quantity = prod(p.quantity,0)
	 *
	 */
		public void setPathExpressionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logTrace( "Obtained Non-null root");

			EntityType<Product> product_ = root.getModel();
			Expression<Integer> exp = cbuilder.prod(root.get(product_.getSingularAttribute("quantity", Integer.class)),
					0);

			cd.set(root.<Integer>get("quantity"), exp);
			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
				pass1 = true;
				logTrace( "Received expected number of updates:" + actual);
				clearCache();
				for (Product p : productRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logErr(
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass2 = false;
					}
				}
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logErr(
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass3 = false;
					}
				}
				for (Product p : hardwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logErr(
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass4 = false;
					}
				}
			} else {
				logErr( "Expected:" + productRef.length + softwareRef.length + hardwareRef.length
						+ ", actual:" + actual);
			}
		} else {
			logErr( "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setPathExpressionTest failed");
		}
	}

	/*
	 * @testName: subquery
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3355
	 *
	 * @test_Strategy:
	 *
	 * UPDATE Product p SET p.quantity = prod(p.quantity,0) WHERE EXISTS (Select
	 * hardProd From PRODUCT hardprod where hardprod.id = '1').
	 *
	 */
		public void subquery() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cu = cbuilder.createCriteriaUpdate(Product.class);
		if (cu != null) {
			logTrace( "Obtained Non-null Criteria Query");
			Root<Product> product = cu.from(Product.class);
			EntityType<Product> product_ = product.getModel();

			Expression<Integer> exp = cbuilder
					.prod(product.get(product_.getSingularAttribute("quantity", Integer.class)), 0);

			cu.set(product.<Integer>get("quantity"), exp);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();

			// create Subquery instance, with root Customer
			Subquery<Product> sq = cu.subquery(Product.class);
			Root<Product> hardProd = sq.from(Product.class);

			// the subquery references the root of the containing query
			sq.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "1"))
					.select(hardProd);

			// an exists condition is applied to the subquery result
			cu.where(cbuilder.exists(sq));

			int actual = getEntityManager().createQuery(cu).executeUpdate();
			if (actual == 1) {
				logTrace( "Received expected number of updates:" + actual);
				clearCache();
				Product pp = getEntityManager().find(Product.class, "1");
				if (pp.getQuantity() != 0) {
					logErr(
							"Expected product:1 to have quantity of 0, actual:" + pp.getQuantity());
				} else {
					pass = true;
				}
			} else {
				logErr( "Expected:1, actual:" + actual);
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("exists test failed");
		}
	}

	/*
	 * @testName: modifiedQueryTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1788; PERSISTENCE:SPEC:1796;
	 * 
	 * @test_Strategy: UPDATE SoftwareProduct p SET p.quantity = 0 WHERE p.quantity
	 * < 35) UPDATE SoftwareProduct p SET p.quantity = 0 WHERE p.quantity > 100)
	 *
	 */
		public void modifiedQueryTest() throws Exception {
		int passModifiedCount1 = 0;
		int passUnModifiedCount1 = 0;
		int passModifiedCount2 = 0;
		int passUnModifiedCount2 = 0;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();
		logMsg( "Testing initial query");
		CriteriaUpdate<SoftwareProduct> cd = cbuilder.createCriteriaUpdate(SoftwareProduct.class);
		Root<SoftwareProduct> softwareproduct = cd.from(SoftwareProduct.class);
		if (softwareproduct != null) {
			EntityType<SoftwareProduct> softwareproduct_ = softwareproduct.getModel();
			cd.set(softwareproduct.get(softwareproduct_.getSingularAttribute("quantity", Integer.class)), 0);
			cd.where(cbuilder.lt(softwareproduct.get(softwareproduct_.getSingularAttribute("quantity", Integer.class)),
					35));
			Query q = getEntityManager().createQuery(cd);
			logMsg( "Modify CriteriaUpdate object");
			cd.where(cbuilder.lt(softwareproduct.get(softwareproduct_.getSingularAttribute("quantity", Integer.class)),
					500));
			int actual = q.executeUpdate();
			if (actual == 4) {
				logTrace( "Received expected number of updates:" + actual);
				clearCache();
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(SoftwareProduct.class, p.getId());
					if (p.getId().equals("30") || p.getId().equals("31") || p.getId().equals("36")
							|| p.getId().equals("37")) {
						if (pp.getQuantity() != 0) {
							logErr(
									"id:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						} else {
							logTrace( "id:" + p.getId() + " update was successfully");
							passModifiedCount1++;
						}
					} else {
						if (pp.getQuantity() != p.getQuantity()) {
							logErr(
									"id:" + p.getId() + " quantity does not equal original value:" + p.getQuantity());
						} else {
							logTrace(
									"id:" + pp.getId() + " quantity matches original value:" + p.getQuantity());
							passUnModifiedCount1++;
						}
					}
				}
			} else {
				logErr( "Expected: 4 updates, actual:" + actual);
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					logErr( "id:" + p.getId() + ", quantity:" + pp.getQuantity());
				}
			}
			logMsg( "Testing modified CriteriaQuery");
			cd.where(cbuilder.gt(softwareproduct.get(softwareproduct_.getSingularAttribute("quantity", Integer.class)),
					100));

			actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == 3) {
				logTrace( "Received expected number of updates:" + actual);
				clearCache();
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(SoftwareProduct.class, p.getId());
					if (p.getId().equals("29") || p.getId().equals("34") || p.getId().equals("38")
							|| p.getId().equals("30") || p.getId().equals("31") || p.getId().equals("36")
							|| p.getId().equals("37")) {
						if (pp.getQuantity() != 0) {
							logErr(
									"id:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						} else {
							logTrace( "id:" + p.getId() + " update was successfully");
							passModifiedCount2++;
						}
					} else {
						if (pp.getQuantity() != p.getQuantity()) {
							logErr(
									"id:" + p.getId() + " quantity does not equal original value:" + p.getQuantity());
						} else {
							logTrace(
									"id:" + pp.getId() + " quantity matches original value:" + p.getQuantity());
							passUnModifiedCount2++;
						}
					}
				}
			} else {
				logErr( "Expected: 3, actual:" + actual);
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					logErr( "id:" + p.getId() + ", quantity:" + pp.getQuantity());
				}
			}

		} else {
			logErr( "Failed to get Non-null root");
		}

		getEntityTransaction().commit();
		if (passModifiedCount1 != 4 || passUnModifiedCount1 != 6 || passModifiedCount2 != 7
				|| passUnModifiedCount2 != 3) {
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
