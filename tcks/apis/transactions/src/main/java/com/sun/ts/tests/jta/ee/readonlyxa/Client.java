/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jta.ee.readonlyxa;

import com.sun.ts.tests.common.connector.util.ConnectorStatus;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.jta.ee.transactional.Helper;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.transaction.TransactionalException;
import jakarta.transaction.UserTransaction;

import static java.util.logging.Level.INFO;


public class Client extends EJBLiteClientBase {

	@Inject
	ReadOnlyTestBean bean;

	@Resource(lookup = "java:comp/UserTransaction")
	private UserTransaction ut;

	/*
	 * Test that an insert can cause an exception when a resource is in read-only mode.
	 * By default, read-only mode is supported on TSDataSource.
	 */
	public void testInsertWithReadOnlyXAResource() throws Exception {
		String result = "TransactionalException not received";
		try {
			bean.setup();
			try {
				bean.insert();
			} catch (TransactionalException te) {
				if (te.getCause() instanceof RuntimeException ) {
					result = "Received expected TransactionalException with nested RuntimeException";
				} else {
					throw new Exception("Received TransactionalException without nested RuntimeException");
				}

			} catch (Exception e) {
				e.printStackTrace();
				result = "Received unexcepted Exception :" + e.getMessage();
			}
		} finally {
			bean.tearDown();
		}

		if (result.equals("Received expected TransactionalException with nested RuntimeException")) {
			Helper.getLogger().log(INFO, result);
			appendReason("Received expected TransactionalException with nested RuntimeException");
		} else {
			throw new Exception(result);
		}
	}

	/*
	 * Test that when TSDataSource does not support the read-only mode, that the insert will be ok.
	 * Since the transaction is in read-only mode though, the JTA implementation must roll back the resource though,
	 * so the lookup after the insert must fail.
	 */
	public void testInsertWithNonReadOnlyXAResource() throws Exception {
		String result = "TransactionalException not received";
		try {
			ConnectorStatus.getConnectorStatus().setSupportReadOnly(false);
			bean.setup();
			bean.insert();
			try {
				bean.get();
			} catch (TransactionalException te) {
				if (te.getCause() instanceof RuntimeException ) {
					result = "Received expected TransactionalException with nested RuntimeException";
				} else {
					throw new Exception("Received TransactionalException without nested RuntimeException");
				}

			} catch (Exception e) {
				e.printStackTrace();
				result = "Received unexcepted Exception :" + e.getMessage();
			}
		} finally {
			bean.tearDown();
			ConnectorStatus.getConnectorStatus().setSupportReadOnly(true);
		}

		if (result.equals("Received expected TransactionalException with nested RuntimeException")) {
			Helper.getLogger().log(INFO, result);
			appendReason("Received expected TransactionalException with nested RuntimeException");
		} else {
			throw new Exception(result);
		}
	}


}