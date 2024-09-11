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

package ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery;


import java.util.List;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;



import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Address;
import ee.jakarta.tck.persistence.common.schema30.Address_;
import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.Phone;
import ee.jakarta.tck.persistence.common.schema30.Phone_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;


public class Client6 extends Util {


	public static void main(String[] args) {
		Client6 theTests = new Client6();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	  /*
	   * @testName: queryTest55
	   * 
	   * @assertion_ids: PERSISTENCE:SPEC:358; PERSISTENCE:SPEC:1712;
	   * PERSISTENCE:SPEC:1713
	   * 
	   * @test_Strategy: The LIKE expression uses an input parameter for the
	   * condition. Verify the results were accurately returned.
	   *
	   */
	  @SetupMethod(name = "setupPhoneData")
	  	  public void queryTest55() throws Exception {
	    boolean pass = false;
	    String expectedPKs[];

	    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

	    try {
	      getEntityTransaction().begin();
	      logTrace(
	          "determine which customers have an area code beginning with 9");
	      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
	      Root<Customer> customer = cquery.from(Customer.class);
	      Join<Address, Phone> p = customer.join(Customer_.home)
	          .join(Address_.phones);
	      cquery.where(cbuilder.like(p.get(Phone_.area),
	          cbuilder.parameter(String.class, "area")));
	      cquery.select(customer);
	      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
	      tquery.setParameter("area", "9%");
	      tquery.setMaxResults(customerRef.length);
	      List<Customer> clist = tquery.getResultList();

	      expectedPKs = new String[3];
	      expectedPKs[0] = "3";
	      expectedPKs[1] = "12";
	      expectedPKs[2] = "16";

	      if (!checkEntityPK(clist, expectedPKs)) {
	    	  logErr(
	            "Did not get expected results.  Expected 3 references, got: "
	                + clist.size());
	      } else {
	    	  logTrace("Expected results received");
	        pass = true;
	      }
	      getEntityTransaction().commit();
	    } catch (Exception e) {
	    	logErr("Caught unexpected exception:", e);

	    }

	    if (!pass) {
	      throw new Exception("queryTest55 failed");
	    }
	  }

}
