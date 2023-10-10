package com.sun.ts.tests.jpa.core.annotations.access.field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

public class Client2IT extends Client {
	
	 @BeforeAll
	  public void setup2() throws Exception {
	    TestUtil.logTrace("setup2");
	    try {

	      super.setup();
	      removeTestData();
	      createTestData2();
	      TestUtil.logTrace("Done creating test data");

	    } catch (Exception e) {
	      TestUtil.logErr("Unexpected exception occurred", e);
	      throw new Exception("Setup failed:", e);
	    }
	  }
	 
	 public void createTestData2() {
		    TestUtil.logTrace("createTestData2");

		    try {
		      getEntityTransaction().begin();
		      d1 = new DataTypes(1, 300);

		      getEntityManager().persist(d1);
		      getEntityTransaction().commit();

		    } catch (Exception e) {
		      TestUtil.logErr("Unexpected Exception in createTestData:", e);
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



	 /*
	   * @testName: mixedAccessTest
	   * 
	   * @assertion_ids: PERSISTENCE:SPEC:1327.3;
	   * 
	   * @test_Strategy:
	   */
	  @Test
	  public void mixedAccessTest() throws Exception {

	    boolean pass = false;
	    final int newInt = 500;

	    try {
	      getEntityTransaction().begin();
	      d1 = getEntityManager().find(DataTypes.class, 1);
	      if ((null != d1) && (d1.getIntData2() == 300)) {
	        TestUtil.logMsg("Int value after find=" + d1.getIntData2());
	        d1.setIntData2(newInt);
	        TestUtil.logMsg("Int value after set=" + d1.getIntData2());
	        getEntityManager().merge(d1);
	        getEntityManager().flush();
	        clearCache();
	        d1 = null;
	        d1 = getEntityManager().find(DataTypes.class, 1);

	        if (d1.getIntData2() == newInt) {
	          pass = true;
	          TestUtil.logTrace("Received expected value:" + d1.getIntData2());
	        } else {
	          TestUtil
	              .logErr("Expected:" + newInt + ", actual:" + d1.getIntData2());
	        }
	        getEntityTransaction().commit();
	      } else {
	        TestUtil.logErr("find returned null");
	      }
	    } catch (Exception e) {
	      TestUtil.logErr("Unexpected exception occurred", e);
	      pass = false;
	    } finally {
	      try {
	        if (getEntityTransaction().isActive()) {
	          getEntityTransaction().rollback();
	        }
	      } catch (Exception re) {
	        TestUtil.logErr("Unexpected Exception during Rollback:", re);
	      }
	    }

	    if (!pass)
	      throw new Exception("mixedAccessTest failed");
	  }

}
