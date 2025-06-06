/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.lob;


import java.util.Arrays;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;





import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {



	private DataTypes dataTypes;

	private byte[] smallByteArray = null;


	public Client() {
	}

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			
			removeTestData();
			createTestData();
			logTrace( "Done creating test data");

		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: lobTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: byte[]
	 *
	 */
		public void lobTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;

		byte[] largeByteArray = null;

		try {
			getEntityTransaction().begin();
			logMsg( "FIND DataTypes and verify initial value");
			dataTypes = getEntityManager().find(DataTypes.class, 1);
			if ((null != dataTypes) && (Arrays.equals(smallByteArray, dataTypes.getByteArrayData()))) {
				logTrace( "Expected results received");
				pass1 = true;
				logTrace( "DataType Entity is not null, setting byteData ");
				largeByteArray = createLargeByteArray();
				dataTypes.setByteArrayData(largeByteArray);
				getEntityManager().merge(dataTypes);
				getEntityManager().flush();

				logMsg( "FIND DataTypes again and verify updated value");
				dataTypes = getEntityManager().find(DataTypes.class, 1);

				logTrace( "Check results");
				if ((null != dataTypes) && (Arrays.equals(largeByteArray, dataTypes.getByteArrayData()))) {
					logTrace( "Expected results received");
					pass2 = true;
				} else {
					logErr( "Unexpected result in array comparison.");
				}
			} else {
				logErr( "Unexpected result in array comparison.");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass1 || !pass2) {
			throw new Exception("lobTest failed");
		}
	}

	// Methods used for Tests
	public void createTestData() {
		logTrace( "createTestData");

		try {
			getEntityTransaction().begin();
			smallByteArray = createSmallByteArray();
			dataTypes = new DataTypes(1, smallByteArray);
			getEntityManager().persist(dataTypes);

			getEntityManager().flush();

			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected Exception in createTestData:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

	}

	private byte[] createSmallByteArray() {

		// Create a String of size 1MB
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < 1024; i++) {
			strbuf.append(i);
		}

		String value = strbuf.toString();
		System.out.println("String Buffer :" + value);

		// get byte array from the string
		final byte myByte[] = value.getBytes();

		// store primitive byte array to array of Byte objects
		byte convertedByte[] = new byte[myByte.length];
		for (int i = 0; i < myByte.length; i++) {
			convertedByte[i] = Byte.valueOf(myByte[i]);
		}

		return convertedByte;

	}

	private byte[] createLargeByteArray() {

		// Create a String of size 4MB
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < 4096; i++) {
			strbuf.append(i);
		}

		String value = strbuf.toString();
		System.out.println("String Buffer :" + value);

		// get byte array from the string
		final byte myByte[] = value.getBytes();

		// store primitive byte array to array of Byte objects
		byte convertedByte[] = new byte[myByte.length];
		for (int i = 0; i < myByte.length; i++) {
			convertedByte[i] = Byte.valueOf(myByte[i]);
		}

		return convertedByte;

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
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES").executeUpdate();
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
