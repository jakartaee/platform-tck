package com.sun.ts.tests.jpa.core.annotations.access.field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

public class Client3IT extends Client{
	
	 @BeforeEach
	  public void setup3() throws Exception {
	    TestUtil.logTrace("setup3");
	    try {

	      super.setup();
	      removeTestData();
	      createTestData3();
	      TestUtil.logTrace("Done creating test data");

	    } catch (Exception e) {
	      TestUtil.logErr("Unexpected exception occurred", e);
	      throw new Exception("Setup failed:", e);
	    }
	  }
	 
	  public void createTestData3() {
		    TestUtil.logTrace("createTestData3");

		    try {
		      getEntityTransaction().begin();
		      d1 = new DataTypes(1, (byte) 5);

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
	   * @testName: transientTest
	   * 
	   * @assertion_ids: PERSISTENCE:SPEC:1327.2
	   * 
	   * @test_Strategy: when transient is specified, verify data isn't persisted
	   */
	  @Test
	  public void transientTest() throws Exception {

	    boolean pass = false;
	    byte newByte = (byte) 111;

	    try {
	      getEntityTransaction().begin();
	      clearCache();
	      d1 = getEntityManager().find(DataTypes.class, 1);
	      if (null != d1) {
	        if ((d1.getTransient() == (byte) 0)) {
	          TestUtil.logTrace(
	              "First find returned expected result:" + d1.getTransient());
	          d1.setTransient(newByte);

	          getEntityManager().merge(d1);
	          getEntityManager().flush();
	          clearCache();
	          d1 = null;
	          d1 = getEntityManager().find(DataTypes.class, 1);

	          if (d1.getTransient() == (byte) 0) {
	            pass = true;
	            TestUtil.logTrace(
	                "Second find returned expected value:" + d1.getTransient());
	          } else {
	            TestUtil
	                .logErr("Second find expected:0, actual:" + d1.getTransient());
	          }
	        } else {
	          TestUtil.logErr(
	              "Expected first find to return:0, actual:" + d1.getTransient());
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
	      throw new Exception("transientTest failed");
	  }


}
