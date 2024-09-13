/*
 * Copyright (c) 2017, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa22.se.generators.sequencegenerators;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;






import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Client extends PMClientBase {

	

	private static final long serialVersionUID = 22L;

	String schemaGenerationDir = null;
	String sTestCase = "jpa_jpa22_se_generators_sequencegenerators";

	boolean supportSequence;

	public Client() {
	}

	/*
	 * @class.setup_props: db.supports.sequence;
	 */
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			
			supportSequence = Boolean.valueOf(System.getProperty("db.supports.sequence"));

			schemaGenerationDir = System.getProperty("user.dir");
			if (!schemaGenerationDir.endsWith(File.separator)) {
				schemaGenerationDir += File.separator;
			}
			schemaGenerationDir += "schemaGeneration";
			logMsg( "schemaGenerationDir=" + this.schemaGenerationDir);

			File f = new File(schemaGenerationDir);
			logMsg( "Delete existing directory ");
			deleteItem(f);
			logMsg( "Create new directory ");
			if (!f.mkdir()) {
				String msg = "Could not mkdir:" + f.getAbsolutePath();
				throw new Exception(msg);
			}
			removeTestData();

		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: sequenceGeneratorTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3488;
	 * 
	 * @test_Strategy: Test the @SequenceGenerator annotation
	 */
		public void sequenceGeneratorTest() throws Exception {
		if (!supportSequence) {
			logMsg( "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
			return;
		}
		boolean pass1a = false;
		boolean pass1b = false;
		boolean pass2a = false;
		boolean pass2b = false;
		boolean pass3 = false;
		boolean pass4 = false;

		logMsg( "Create the script(s)");
		final String CREATEFILENAME = schemaGenerationDir + File.separator + "create_" + this.sTestCase + ".sql";
		final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_" + this.sTestCase + ".sql";

		File f1 = new File(CREATEFILENAME);
		logTrace( "Deleting previous create script");
		deleteItem(f1);
		File f2 = new File(DROPFILENAME);
		logTrace( "Deleting previous drop script");
		deleteItem(f2);

		Properties props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "none");
		props.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "false");
		props.put("jakarta.persistence.schema-generation.scripts.create-target", convertToURI(CREATEFILENAME));
		props.put("jakarta.persistence.schema-generation.scripts.drop-target", convertToURI(DROPFILENAME));

		displayProperties(props);

		logMsg( "Executing Persistence.createEntityManagerFactory(...)");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), props);
		emf.close();
		emf = null;

		logMsg( "Check script(s) content");

		List<String> expected = new ArrayList<String>();
		expected.add("CREATE TABLE SCHEMAGENSIMPLE");
		expected.add("ID");
		expected.add("PRIMARY KEY (ID)");
		pass1a = findDataInFile(f1, expected);
		pass1b = findDataInFile(f1, "CREATE SEQUENCE SEQGENERATOR START WITH 10");

		/*
		 * CREATE TABLE SCHEMAGENSIMPLE (ID INTEGER NOT NULL, PRIMARY KEY (ID)) CREATE
		 * SEQUENCE SEQGENERATOR START WITH 10
		 */

		expected.clear();
		expected.add("DROP TABLE");
		expected.add("SCHEMAGENSIMPLE");
		pass2a = findDataInFile(f2, expected);
		expected.clear();
		expected.add("DROP SEQUENCE");
		expected.add("SEQGENERATOR");
		pass2b = findDataInFile(f2, expected);

		logTrace( "Execute the create script");
		props = getPersistenceUnitProperties();

		props.put("jakarta.persistence.schema-generation.database.action", "create");
		props.put("jakarta.persistence.schema-generation.scripts.action", "none");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
		props.put("jakarta.persistence.schema-generation.create-script-source", convertToURI(CREATEFILENAME));
		displayProperties(props);

		logMsg( "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);

		clearEMAndEMF();
		try {
			logMsg( "Persist some data");
			getEntityTransaction(true).begin();
			Simple s = new Simple();
			getEntityManager().persist(s);
			getEntityTransaction().commit();
			clearCache();
			Simple s2 = getEntityManager().find(Simple.class, s.getId());
			if (s2 == null) {
				logTrace(
						"Received unexpected null from getEntityManager().find(Simple.class, 10)");
			} else if (s.equals(s2)) {
				logTrace( "Received expected result:" + s.toString());
				pass3 = true;
			} else {
				logErr( "Expected:" + s.toString());
				logErr( "Actual:" + s2.toString());
			}
		} catch (Throwable t) {
			logErr( "Received unexpected exception", t);
		}
		clearEMAndEMF();

		logTrace( "Execute the drop script");
		props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "drop");
		props.put("jakarta.persistence.schema-generation.scripts.action", "none");
		props.put("jakarta.persistence.schema-generation.drop-script-source", convertToURI(DROPFILENAME));
		displayProperties(props);

		logMsg( "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);
		clearEMAndEMF();

		logMsg( "Try to persist an entity, it should fail");
		try {
			getEntityTransaction(true).begin();
			Simple s3 = new Simple(2);
			getEntityManager().persist(s3);
			getEntityManager().flush();
			getEntityTransaction().commit();
			logErr( "An exception should have been thrown if drop had occurred successfully");
		} catch (Exception ex) {
			logTrace( "Receive expected exception");
			pass4 = true;
		}
		logTrace( "pass1a:" + pass1a);
		logTrace( "pass1b:" + pass1b);
		logTrace( "pass2a:" + pass2a);
		logTrace( "pass2b:" + pass2b);
		logTrace( "pass3:" + pass3);
		logTrace( "pass4:" + pass4);
		if (!pass1a || !pass1b || !pass2a || !pass2b || !pass3 || !pass4) {
			throw new Exception("sequenceGeneratorTest failed");
		}
	}

	
	public void cleanup() throws Exception {
		try {
			logTrace( "cleanup");
			removeTestData();
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {

        }
	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			logMsg( "Try to drop table SCHEMAGENSIMPLE");
			getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENSIMPLE").executeUpdate();
			getEntityTransaction().commit();
		} catch (Throwable t) {
			logMsg(
					"AN EXCEPTION WAS THROWN DURING DROP TABLE SCHEMAGENSIMPLE, IT MAY OR MAY NOT BE A PROBLEM, "
							+ t.getMessage());
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
				clearEntityTransaction();

				// ensure that we close the EM and EMF before proceeding.
				clearEMAndEMF();
			} catch (Exception re) {
				logErr( "Unexpected Exception in removeTestData:", re);
			}
		}
	}

}
